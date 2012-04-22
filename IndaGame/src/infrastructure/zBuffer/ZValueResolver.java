package infrastructure.zBuffer;

/**
 * Converts given x and y-coordinates to z-coordinates, thus simulating depth.
 * 
 * @author caiiiycuk
 */
public interface ZValueResolver
{

	/**
	 * @param x
	 *            given x coordinate
	 * @param y
	 *            given y coordinate
	 * @return z coordinate of x, y
	 */
	double resolve(double x, double y);

	/**
	 * @return is Antialiasing Enabled
	 */
	boolean isAntialiasingEnabled();

	/**
	 * @param isEnabled
	 *            enable or disable antialias
	 */
	void setAntialiasingEnabled(boolean isEnabled);

}
