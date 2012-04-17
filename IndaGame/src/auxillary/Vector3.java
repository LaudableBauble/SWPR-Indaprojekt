package auxillary;

/**
 * A vector is a position in the game, represented in the form of three double values; x, y and z.
 */
public class Vector3
{
	// Create the variables.
	public double x, y, z;

	/**
	 * An empty constructor.
	 */
	public Vector3()
	{
		x = 0;
		y = 0;
		z = 0;
	}

	/**
	 * Constructor for a vector3.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param z
	 *            The z-coordinate.
	 */
	public Vector3(double x, double y, double z)
	{
		// Pass along the values.
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
