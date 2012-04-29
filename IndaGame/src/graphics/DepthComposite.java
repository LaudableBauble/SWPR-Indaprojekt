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
	protected WritableRaster _OutputRaster;
	protected int _FrameSkipCount;

	public DepthComposite(Vector2 size)
	{
		// Set the width and height of the buffers.
		_Width = (int) size.x;
		_Height = (int) size.y;

		// Set up the buffers.
		buffer = new double[_Height * _Width];
		clearBuffer = new double[_Height * _Width];
		Arrays.fill(clearBuffer, Double.MIN_VALUE);
		_OutputRaster = null;
		_FrameSkipCount = 0;
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
		// If not time to update the depth-sorting and general drawing yet, use the stored output raster.
		if (_OutputRaster != null && _FrameSkipCount != 0)
		{
			// Overwrite the current output raster with an old one.
			dstOut.setRect(_OutputRaster);
			return;
		}

		if (_Entity == null) { throw new IllegalArgumentException("You must set an entity before drawing anything with this composite."); }

		// Get the max bounds of the writable raster.
		int maxX = dstOut.getMinX() + dstOut.getWidth();
		int maxY = dstOut.getMinY() + dstOut.getHeight();

		// Translate coordinates from the raster's space to the SampleModel's space.
		int dstInX = -dstIn.getSampleModelTranslateX();
		int dstInY = -dstIn.getSampleModelTranslateY();

		// For each pixel in the writable raster.
		for (int y = dstOut.getMinY(); y < maxY; y++)
		{
			// If the entity's shape has a uniform depth distribution, ie. it is not a slope, do not iterate through the width of the image
			// because all x-coordinates will return the same depth value.
			// If the source image sample slice contains alpha values however we have to iterate through all its pixels anyway.

			for (int x = dstOut.getMinX(); x < maxX; x++)
			{
				// Get the depth (z) for both the destination and source rasters.
				double dstZ = getZOf(dstInX + x, dstInY + y);
				double srcZ = _Entity.getDepthSort(x, y);

				// If to overwrite or keep the source raster's data.
				if (srcZ > dstZ && src.getSample(x, y, A_BAND) > 0)
				{
					setZOf(dstInX + x, dstInY + y, srcZ);
					dstOut.setSample(x, y, R_BAND, src.getSample(x, y, R_BAND)); // R
					dstOut.setSample(x, y, G_BAND, src.getSample(x, y, G_BAND)); // G
					dstOut.setSample(x, y, B_BAND, src.getSample(x, y, B_BAND)); // B
				}
				else if (srcZ == dstZ && src.getSample(x, y, A_BAND) > 0)
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

		// Save the output raster.
		// _OutputRaster = dstOut.createCompatibleWritableRaster();
		// _OutputRaster.setRect(dstOut);
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
		// Increment the counter and reset it if need be.
		_FrameSkipCount = (_FrameSkipCount >= 0) ? 0 : _FrameSkipCount + 1;
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
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param value
	 *            z-value
	 */
	public void setZOf(int x, int y, double value)
	{
		if (x >= _Width || x < 0 || y >= _Height || y < 0) { throw new IllegalArgumentException("Point [" + x + ", " + y + "] is outside of the Z Buffer array"); }

		buffer[y * _Width + x] = value;
	}

	/**
	 * Get Z Buffer values in array.
	 * 
	 * @return values in array
	 */
	public double[] getBuffer()
	{
		return buffer;
	}

	public double getZOf(int realX, int realY)
	{
		return buffer[realY * _Width + realX];
	}
}
