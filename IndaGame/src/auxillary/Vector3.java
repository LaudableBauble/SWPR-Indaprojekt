package auxillary;

import java.math.BigDecimal;

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

	/**
	 * Constructor for a vector3.
	 * 
	 * @param v
	 *            The vector2 to use.
	 * @param z
	 *            The z-coordinate.
	 */
	public Vector3(Vector2 v, double z)
	{
		this(v.x, v.y, z);
	}

	/**
	 * Constructor for a vector3.
	 * 
	 * @param v
	 *            The vector2 to use.
	 */
	public Vector3(Vector2 v)
	{
		this(v.x, v.y, 0.0);
	}

	/**
	 * Get a vector2 from this vector3.
	 * 
	 * @return Get a vector2.
	 */
	public Vector2 vector2()
	{
		return new Vector2(x, y);
	}

	/**
	 * Add two vectors and return the sum.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The sum of the two vectors.
	 */
	public static Vector3 add(Vector3 v1, Vector2 v2)
	{
		return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z);
	}

	/**
	 * Add two vectors and return the sum.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The sum of the two vectors.
	 */
	public static Vector3 add(Vector3 v1, Vector3 v2)
	{
		return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	/**
	 * Add a value to a vector.
	 * 
	 * @param v
	 *            The vector to add to.
	 * @param value
	 *            The value to add.
	 * @return The sum vector.
	 */
	public static Vector3 add(Vector3 v, double value)
	{
		return new Vector3(v.x + value, v.y + value, v.z + value);
	}

	/**
	 * Subtract two vectors.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The difference vector.
	 */
	public static Vector3 subtract(Vector3 v1, Vector3 v2)
	{
		return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}

	/**
	 * Multiply two vectors.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The product vector.
	 */
	public static Vector3 multiply(Vector3 v1, Vector3 v2)
	{
		return checkNaN(new Vector3(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z));
	}

	/**
	 * Multiply two vectors.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The product vector.
	 */
	public static Vector3 multiply(Vector3 v1, Vector2 v2)
	{
		return checkNaN(new Vector3(v1.x * v2.x, v1.y * v2.y, v1.z));
	}

	/**
	 * Multiply a vector with a value.
	 * 
	 * @param v
	 *            The first vector.
	 * @param value
	 *            The value to multiply with.
	 * @return The product vector.
	 */
	public static Vector3 multiply(Vector3 v, double value)
	{
		return checkNaN(new Vector3(v.x * value, v.y * value, v.z * value));
	}

	/**
	 * Divide two vectors.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The resulting vector.
	 */
	public static Vector3 divide(Vector3 v1, Vector3 v2)
	{
		return checkNaN(new Vector3(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z));
	}

	/**
	 * Divide a vector with a value.
	 * 
	 * @param v
	 *            The first vector.
	 * @param value
	 *            The value to divide with.
	 * @return The resulting vector.
	 */
	public static Vector3 divide(Vector3 v, double value)
	{
		return checkNaN(new Vector3(v.x / value, v.y / value, v.z / value));
	}

	/**
	 * Inverse the direction of a vector.
	 * 
	 * @param v
	 *            The vector to inverse.
	 * @return The inverse vector.
	 */
	public static Vector3 inverse(Vector3 v)
	{
		return new Vector3(v.x * -1, v.y * -1, v.z * -1);
	}

	/**
	 * Get a vector with the absolute coordinates of the given vector.
	 * 
	 * @param v
	 *            The vector to turn absolute.
	 * @return The absolute vector.
	 */
	public static Vector3 absolute(Vector3 v)
	{
		return new Vector3(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z));
	}

	/**
	 * Check the vector for NaN values.
	 * 
	 * @param v
	 *            The vector to check.
	 * @return The non-NaN vector.
	 */
	public static Vector3 checkNaN(Vector3 v)
	{
		// Create the return vector, unNaNed.
		Vector3 vReturn = Vector3.empty();

		// Check for NaN.
		if (v.x == v.x)
		{
			vReturn.x = v.x;
		}
		if (v.y == v.y)
		{
			vReturn.y = v.y;
		}
		if (v.z == v.z)
		{
			vReturn.y = v.z;
		}

		// Return the result.
		return vReturn;
	}

	/**
	 * Get the length of the vector.
	 * 
	 * @param v
	 *            The vector.
	 * @return The length of the vector.
	 */
	public static double getLength(Vector3 v)
	{
		return getDistance(v, Vector3.empty());
	}

	/**
	 * Get this vector's length.
	 * 
	 * @return This vector's length.
	 */
	public double getLength()
	{
		return getDistance(this, Vector3.empty());
	}

	/**
	 * Get the distance between two vectors.
	 * 
	 * @param v1
	 *            The first vector.
	 * @param v2
	 *            The second vector.
	 * @return The distance.
	 */
	public static double getDistance(Vector3 v1, Vector3 v2)
	{
		// Calculate the distance between two vectors.
		double dx = v1.x - v2.x;
		double dy = v1.y - v2.y;
		double dz = v1.z - v2.z;

		// Return the result.
		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}

	/**
	 * Round a vector.
	 * 
	 * @param v
	 *            The vector to round.
	 * @param decimals
	 *            Number of decimals.
	 * @return The rounded vector.
	 */
	public static Vector3 round(Vector3 v, int decimals)
	{
		// Create the Rounding Object for the X-coordinate.
		BigDecimal bdX = new BigDecimal(v.x);
		bdX = bdX.setScale(decimals, BigDecimal.ROUND_UP);
		// Create the Rounding Object for the Y-coordinate.
		BigDecimal bdY = new BigDecimal(v.y);
		bdY = bdY.setScale(decimals, BigDecimal.ROUND_UP);
		// Create the Rounding Object for the Z-coordinate.
		BigDecimal bdZ = new BigDecimal(v.z);
		bdZ = bdZ.setScale(decimals, BigDecimal.ROUND_UP);

		// Return the rounded vector.
		return new Vector3(bdX.doubleValue(), bdY.doubleValue(), bdZ.doubleValue());
	}

	/**
	 * Convert a vector3 to vector2.
	 * 
	 * @return The converted vector.
	 */
	public Vector2 toVector2()
	{
		return new Vector2(x, y);
	}

	/**
	 * Convert a vector to a string.
	 */
	public String toString()
	{
		return new String("(" + x + ", " + y + ", " + z + ")");
	}

	/**
	 * Get an empty vector.
	 * 
	 * @return An empty vector.
	 */
	public static Vector3 empty()
	{
		return new Vector3(0, 0, 0);
	}
}
