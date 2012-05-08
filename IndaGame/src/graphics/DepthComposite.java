package graphics;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import main.Entity;

import auxillary.Vector2;

/**
 * A depth composite emulates a Z-Buffer and is used to simulate depth in 2D drawing.
 */
public class DepthComposite implements Composite, CompositeContext
{
	protected final static byte R_BAND = 0;
	protected final static byte G_BAND = 1;
	protected final static byte B_BAND = 2;
	protected final static byte A_BAND = 3;

	protected double[] clearBuffer;
	protected double[] buffer;
	protected int _Width;
	protected int _Height;
	protected Entity _Entity;

	public DepthComposite(Vector2 size)
	{
		// Set the width and height of the buffers.
		_Width = (int) size.x;
		_Height = (int) size.y;

		// Set up the buffers.
		buffer = new double[_Height * _Width];
		clearBuffer = new double[_Height * _Width];
		Arrays.fill(clearBuffer, Double.MIN_VALUE);
		clearBufferBit();
	}

	/**
	 * {@inheritDoc}
	 */
	public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints)
	{
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public void compose(Raster src, Raster dstIn, WritableRaster dstOut)
	{
		if (_Entity == null) { throw new IllegalArgumentException("You must set an entity before drawing anything with this composite."); }

		try
		{
			// Get the max bounds of the writable raster.
			int maxX = dstOut.getMinX() + dstOut.getWidth();
			int maxY = dstOut.getMinY() + dstOut.getHeight();

			// Translate coordinates from the raster's space to the SampleModel's space.
			int dstInX = -dstIn.getSampleModelTranslateX();
			int dstInY = -dstIn.getSampleModelTranslateY();

			// Whether the source raster supports a 4th color band, ie. alpha.
			boolean supportsAlpha = src.getNumBands() >= 4;

			// For each pixel in the writable raster.
			for (int y = dstOut.getMinY(); y < maxY; y++)
			{
				for (int x = dstOut.getMinX(); x < maxX; x++)
				{
					// Get the depth (z) for both the destination and source rasters.
					double dstZ = getZ(dstInX + x, dstInY + y);
					double srcZ = _Entity.getDepthSort(x, y);

					// Get the pixel's alpha value.
					int alpha = supportsAlpha ? src.getSample(x, y, A_BAND) : 1;

					// If to overwrite or keep the source raster's data.
					if (srcZ > dstZ && alpha > 0)
					{
						setZ(dstInX + x, dstInY + y, srcZ);
						dstOut.setSample(x, y, R_BAND, src.getSample(x, y, R_BAND)); // R
						dstOut.setSample(x, y, G_BAND, src.getSample(x, y, G_BAND)); // G
						dstOut.setSample(x, y, B_BAND, src.getSample(x, y, B_BAND)); // B
					}
					else if (srcZ == dstZ && alpha > 0)
					{
						dstOut.setSample(x, y, R_BAND, src.getSample(x, y, R_BAND)); // R
						dstOut.setSample(x, y, G_BAND, src.getSample(x, y, G_BAND)); // G
						dstOut.setSample(x, y, B_BAND, src.getSample(x, y, B_BAND)); // B
					}
					else
					{
						dstOut.setSample(x, y, R_BAND, dstIn.getSample(x, y, R_BAND)); // R
						dstOut.setSample(x, y, G_BAND, dstIn.getSample(x, y, G_BAND)); // G
						dstOut.setSample(x, y, B_BAND, dstIn.getSample(x, y, B_BAND)); // B
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(this + ": Depth Composite Error. (" + e + ", Entity: " + _Entity.getName() + ")");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void dispose()
	{
	}

	/**
	 * Notify the depth composite that the current frame has come to an end.
	 */
	public void endFrame()
	{
		// Clear the z-buffer.
		clearBufferBit();
	}

	/**
	 * Clear ZBuffer (fill buffer with Float.MAX_VALUE).
	 */
	public void clearBufferBit()
	{
		System.arraycopy(clearBuffer, 0, buffer, 0, buffer.length);
	}

	/**
	 * Get the currently processed entity.
	 * 
	 * @return The current entity.
	 */
	public Entity getEntity()
	{
		return _Entity;
	}

	/**
	 * Set the next entity to process.
	 * 
	 * @param entity
	 *            The next entity to process.
	 */
	public void setEntity(Entity entity)
	{
		_Entity = entity;
	}

	/**
	 * Set z-value in buffer for given point.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param value
	 *            The z-value.
	 */
	public void setZ(int x, int y, double value)
	{
		if (x >= _Width || x < 0 || y >= _Height || y < 0) { throw new IllegalArgumentException("Point [" + x + ", " + y + "] is outside of the Z Buffer array"); }

		buffer[y * _Width + x] = value;
	}

	/**
	 * Get Z Buffer values in array.
	 * 
	 * @return The values in the array.
	 */
	public double[] getBuffer()
	{
		return buffer;
	}

	/**
	 * Get the z-value of the given pixel in the buffer.
	 * 
	 * @param realX
	 *            The x-coordinate of the pixel.
	 * @param realY
	 *            The y-coordinate of the pixel.
	 * @return The z-value for the specified pixel.
	 */
	public double getZ(int realX, int realY)
	{
		return buffer[realY * _Width + realX];
	}

	/**
	 * Get the interval for the buffer's z values, ie. the min and max value.
	 * 
	 * @param xPosition
	 *            The x-coordinate of the top-left corner.
	 * @param yPosition
	 *            The y-coordinate of the top-left corner.
	 * @param width
	 *            The width of the area.
	 * @param height
	 *            The height of the area.
	 * @return The interval (min and max) of the entity's depth sorting values.
	 */
	public Vector2 getZInterval(int xPosition, int yPosition, int width, int height)
	{
		// The min and max values.
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		// Iterate through all pixels in the sprite.
		for (int y = yPosition; y < height; y++)
		{
			for (int x = xPosition; x < width; x++)
			{
				// Get the z value.
				double z = getZ(x, y);

				// Determine if the min and max values have changed.
				min = (z < min) ? z : min;
				max = (z > max) ? z : max;
			}
		}

		// Return the interval.
		return new Vector2(min, max);
	}
}
