package infrastructure.zBuffer;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.ColorModel;
import java.util.Arrays;

import auxillary.Vector2;

import physics.Shape;

/**
 * A depth composite emulates a Z-Buffer and is used to simulate depth in 2D drawing.
 */
public class DepthComposite implements Composite
{
	protected double[] clearBuffer;
	protected double[] buffer;
	protected int _Width;
	protected int _Height;
	protected Shape _Shape;

	protected ZValueResolver valueResolver;
	protected boolean antialiasingEnabled;

	public DepthComposite(Vector2 size)
	{
		//Set the width and height of the buffers.
		_Width = (int)size.x;
		_Height = (int)size.y;
		
		//Set up the buffers.
		buffer = new double[_Height * _Width];
		clearBuffer = new double[_Height * _Width];
		Arrays.fill(clearBuffer, Double.MAX_VALUE);
		clearBufferBit();
	}

	/**
	 * {@inheritDoc}
	 */
	public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints)
	{
		return new DepthCompositeContext(this);
	}

	/**
	 * Clear ZBuffer (fill buffer with Float.MAX_VALUE).
	 */
	public void clearBufferBit()
	{
		System.arraycopy(clearBuffer, 0, buffer, 0, buffer.length);
	}

	/**
	 * Get the currently processed shape.
	 * 
	 * @return The current shape.
	 */
	public Shape getShape()
	{
		return _Shape;
	}

	/**
	 * Set the next shape to process.
	 * 
	 * @param shape
	 *            The next shape to process.
	 */
	public void setShape(Shape shape)
	{
		_Shape = shape;
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
