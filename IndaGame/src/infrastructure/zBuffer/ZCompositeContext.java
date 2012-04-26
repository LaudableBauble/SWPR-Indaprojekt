package infrastructure.zBuffer;

import java.awt.CompositeContext;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * A z-composite context emulates a Z-buffer and thus simulates depth in 2D-drawing.
 * 
 * @author caiiiycuk
 */
public class ZCompositeContext implements CompositeContext
{

	protected final static byte R_BAND = 0;
	protected final static byte G_BAND = 1;
	protected final static byte B_BAND = 2;

	protected ZComposite zComposite;

	ZCompositeContext(ZComposite zComposite)
	{
		this.zComposite = zComposite;
	}

	/**
	 * {@inheritDoc}
	 */
	public void compose(Raster src, Raster dstIn, WritableRaster dstOut)
	{
		// Get the resolver.
		ZValueResolver zValueResolver = zComposite.getValueResolver();

		if (zValueResolver == null) { throw new IllegalArgumentException("You must set a ZValueResolver before drawing any polygons with this composite."); }

		// Get the max bounds of the writable raster.
		int maxX = dstOut.getMinX() + dstOut.getWidth();
		int maxY = dstOut.getMinY() + dstOut.getHeight();

		// For each pixel in the writable raster.
		for (int y = dstOut.getMinY(); y < maxY; y++)
		{
			for (int x = dstOut.getMinX(); x < maxX; x++)
			{
				// Translate coordinates from the raster's space to the SampleModel's space.
				int dstInX = -dstIn.getSampleModelTranslateX() + x;
				int dstInY = -dstIn.getSampleModelTranslateY() + y;

				// Get the depth (z) for both the destination and source rasters.
				double dstZ = zComposite.getZOf(dstInX, dstInY);
				double srcZ = zValueResolver.resolve(dstInX, dstInY);

				// If to overwrite or keep the source raster's data.
				if (srcZ < dstZ)
				{
					zComposite.setZOf(dstInX, dstInY, srcZ);
					dstOut.setSample(x, y, R_BAND, src.getSample(x, y, R_BAND)); // R
					dstOut.setSample(x, y, G_BAND, src.getSample(x, y, G_BAND)); // G
					dstOut.setSample(x, y, B_BAND, src.getSample(x, y, B_BAND)); // B
				}
				else if (srcZ == dstZ)
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

	/**
	 * {@inheritDoc}
	 */
	public void dispose()
	{
	}

}
