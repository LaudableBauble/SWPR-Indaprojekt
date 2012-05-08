package auxillary;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

import physics.Shape;

/**
 * A vector2 is a position in the game, represented in the form of two double values; x and y.
 */
@XmlRootElement
public class Vector2
{
	// Create the variables.
	public double x, y;

	// The empty constructor.
	public Vector2()
	{
	}

	// The other constructor.
	public Vector2(double posX, double posY)
	{
		// Pass along the values.
		x = posX;
		y = posY;
	}

	/**
	 * Constructor for a vector.
	 * 
	 * @param p
	 *            The point vector.
	 */
	public Vector2(Point p)
	{
		// Pass along the values.
		x = p.getX();
		y = p.getY();
	}

	/**
	 * Constructor for creating a vector from a Rectangle2D instance.
	 * 
	 * @param rectangle
	 *            The rectangle instance.
	 */
	public Vector2(Rectangle2D rectangle)
	{
		// Pass along the values.
		x = rectangle.getX();
		y = rectangle.getY();
	}

	/**
	 * Set the x-coordinate.
	 * 
	 * @param x
	 *            The new x-coordinate.
	 */
	public void setX(double x)
	{
		this.x = x;
	}

	/**
	 * Set the y-coordinate.
	 * 
	 * @param y
	 *            The new y-coordinate.
	 */
	public void setY(double y)
	{
		this.y = y;
	}

	// Add two Vectors.
	public static Vector2 add(Vector2 v1, Vector2 v2)
	{
		// Add the Vectors and return the sum.
		return (new Vector2((v1.x + v2.x), (v1.y + v2.y)));
	}

	// Add a Vector with a value.
	public static Vector2 add(Vector2 v, double value)
	{
		// Add the Vector and value and return the sum.
		return (new Vector2((v.x + value), (v.y + value)));
	}

	// Subtract two Vectors.
	public static Vector2 subtract(Vector2 v1, Vector2 v2)
	{
		// Subtract the Vectors and return the difference.
		return (new Vector2((v1.x - v2.x), (v1.y - v2.y)));
	}

	// Multiply two Vectors.
	public static Vector2 multiply(Vector2 v1, Vector2 v2)
	{
		// Multiply the Vector with the float value and return the product.
		return (checkNaN(new Vector2((v1.x * v2.x), (v1.y * v2.y))));
	}

	// Multiply a Vector with a float value.
	public static Vector2 multiply(Vector2 v, double f)
	{
		// Multiply the Vector with the float value and return the product.
		return (checkNaN(new Vector2((v.x * f), (v.y * f))));
	}

	// Divide two Vectors.
	public static Vector2 divide(Vector2 v1, Vector2 v2)
	{
		// Divide the Vector with the float value and return the result.
		return (checkNaN(new Vector2((v1.x / v2.x), (v1.y / v2.y))));
	}

	// Divide a Vector with a float value.
	public static Vector2 divide(Vector2 v, double f)
	{
		// Divide the Vector with the float value and return the result.
		return (checkNaN(new Vector2((v.x / f), (v.y / f))));
	}

	// Inverse the direction of a Vector.
	public static Vector2 inverse(Vector2 v)
	{
		// Inverse the direction of the Vector and return the result.
		return (new Vector2((v.x * -1), (v.y * -1)));
	}

	/**
	 * Inverse this vector.
	 * 
	 * @return This vector inverted.
	 */
	public Vector2 inverse()
	{
		return inverse(this);
	}

	// Convert this Vector into aVector with absolute values.
	public static Vector2 absolute(Vector2 v)
	{
		// Inverse the direction of the Vector and return the result.
		return (new Vector2(Math.abs(v.x), Math.abs(v.y)));
	}

	/**
	 * Normalize a vector.
	 * 
	 * @param v
	 *            The vector to normalize.
	 * @return The normalized vector.
	 */
	public static Vector2 normalize(Vector2 v)
	{
		return Vector2.divide(v, Math.sqrt(v.x * v.x + v.y * v.y));
	}

	/**
	 * Normalize the vector.
	 * 
	 * @return The normalized vector.
	 */
	public Vector2 normalize()
	{
		return normalize(this);
	}

	/**
	 * Get a vector perpendicular to this one. (x, y) => (-y, x); Note: This assumes clockwise ordering of vertices.
	 * 
	 * @return A perpendicular vector.
	 */
	public Vector2 perpendicular()
	{
		return new Vector2(-y, x);
	}

	/**
	 * Perform a dot product on the vector.
	 * 
	 * @param v
	 *            The vector to use.
	 * @return A product vector.
	 */
	public double dot(Vector2 v)
	{
		return x * v.x + y * v.y;
	}

	/**
	 * See if this vector overlaps another. Note: Only works for one-dimensional vectors, ie. lines.
	 * 
	 * @param v
	 *            The vector to check.
	 * @return Whether the two vectors overlap.
	 */
	public boolean overlap(Vector2 v)
	{
		return overlap(v, false);
	}

	/**
	 * See if this vector overlaps another. Note: Only works for one-dimensional vectors, ie. lines.
	 * 
	 * @param v
	 *            The vector to check.
	 * @param margin
	 *            Whether to allow margin overlaps, ie. same end-point position.
	 * @return Whether the two vectors overlap.
	 */
	public boolean overlap(Vector2 v, boolean margin)
	{
		return (margin) ? max(this) >= min(v) && max(v) >= min(this) : max(this) > min(v) && max(v) > min(this);
	}

	/**
	 * Get the middle values of two overlapping vectors. Example: (3, -1) and (0, 9) yields (0, 3).
	 * 
	 * @param v1
	 *            The first two coordinates.
	 * @param v2
	 *            The second two coordinates.
	 * @return The middle values of the two vectors. Null if there is no overlap between the vectors.
	 */
	public static Vector2 getMiddleValues(Vector2 v1, Vector2 v2)
	{
		// If no overlap, quit here.
		if (!v1.overlap(v2)) { return null; }

		// Port the vectors to an array and sort it.
		double[] v = new double[] { v1.x, v1.y, v2.x, v2.y };
		Arrays.sort(v);

		// Return the middle values.
		return new Vector2(v[1], v[2]);
	}

	/**
	 * Get the overlap between the vectors. Example: (3, -1) and (0, 9) yields 3-0 = 3.
	 * 
	 * @param v1
	 *            The first two coordinates.
	 * @param v2
	 *            The second two coordinates.
	 * @return The overlap between the two vectors. -1 if there is no overlap.
	 */
	public static double getOverlap(Vector2 v1, Vector2 v2)
	{
		// If no overlap, quit here.
		if (!v1.overlap(v2)) { return -1; }

		// Port the vectors to an array and sort it.
		double[] v = new double[] { v1.x, v1.y, v2.x, v2.y };
		Arrays.sort(v);

		// Return the difference of the middle values.
		return Math.max(v[1], v[2]) - Math.min(v[1], v[2]);
	}

	/**
	 * Compare two vectors by their values. The vectors need to be in the order of (x = min, y = max). Allows margin overlap, ie. same end-point position.
	 * 
	 * @return 1 if the first vector is greater, -1 if the second vector is greater and 0 if the vectors overlap.
	 */
	public static int isOverlaping(Vector2 v1, Vector2 v2)
	{
		// Compare the vectors to each other.
		if (!v1.overlap(v2, false))
		{
			if (v1.x > v2.y)
			{
				return 1;
			}
			else if (v2.x > v1.y) { return -1; }
		}

		// The vectors overlap, return 0.
		return 0;
	}

	// Add the supplied Vector to this Vector.
	public void addToThis(Vector2 v)
	{
		// Add the Vectors.
		this.x = (this.x + v.x);
		this.y = (this.y + v.y);
	}

	// Check the Vector for NaN values.
	public static Vector2 checkNaN(Vector2 v)
	{
		// Create the return vector, unNaNed.
		Vector2 vReturn = new Vector2(0, 0);
		// Check for NaN.
		if (v.x == v.x)
		{
			vReturn.x = v.x;
		}
		if (v.y == v.y)
		{
			vReturn.y = v.y;
		}

		// Return the result.
		return vReturn;
	}

	// Get the Vector's length.
	public static double getLength(Vector2 v)
	{
		// Calculate the length of the Vector and return the result.
		return getDistance(v, new Vector2(0, 0));
	}

	/**
	 * Get this vector's length.
	 * 
	 * @return This vector's length.
	 */
	public double getLength()
	{
		return getDistance(this, new Vector2(0, 0));
	}

	// Get the distance between two vectors.
	public static double getDistance(Vector2 v1, Vector2 v2)
	{
		// Calculate the distance between two vectors.
		double dx = (v1.x - v2.x);
		double dy = (v1.y - v2.y);

		// Return the result.
		return (Math.sqrt((dx * dx) + (dy * dy)));
	}

	// Calculate the direction from an angle.
	public static Vector2 getDirection(double angleRadians)
	{
		return new Vector2((double) -Math.cos(angleRadians), (double) -Math.sin(angleRadians));
	}

	// Calculate the direction from a Vector and it length.
	public static Vector2 getDirection(Vector2 v, double length)
	{
		return divide(v, length);
	}

	// Calculate the direction from this Vector using its own length.
	public static Vector2 getDirection(Vector2 v)
	{
		return divide(v, getLength(v));
	}

	// Get the angle between two Vectors.
	public static double getAngle(Vector2 faceThis, Vector2 position)
	{
		// Return the angle.
		return Math.atan2((double) (faceThis.y - position.y), (double) (faceThis.x - position.x));
	}

	// Get the angle between of a Vector by lengthen it.
	public static double getAngle(Vector2 vector)
	{
		// Return the angle.
		return Math.atan2((double) (vector.y - multiply(vector, 1.5).y), (double) (vector.x - multiply(vector, 1.5).x));
	}

	/**
	 * Transform a vector with a matrix.
	 * 
	 * @param v
	 *            The vector to transform.
	 * @param matrix
	 *            The matrix to use.
	 * @return The transformed vector.
	 */
	public static Vector2 transform(Vector2 v, AffineTransform matrix)
	{
		return new Vector2((Point) matrix.transform(new Point((int) v.x, (int) v.y), new Point(0, 0)));
	}

	/**
	 * Get this vector's angle, in the range of -pi to pi. (0, 0) is assumed to be the center and a direction towards the positive x-axis (right) returns an angle of 0. Uses clockwise ordering.
	 * 
	 * @return The direction in radians.
	 */
	public double getAngle()
	{
		return Math.atan2(this.y, this.x);
	}

	/**
	 * Get an empty vector.
	 * 
	 * @return An empty vector.
	 */
	public static Vector2 empty()
	{
		return new Vector2(0, 0);
	}

	// Convert into String.
	public String toString()
	{
		// Return a String of the Vector.
		return (new String("(" + x + ", " + y + ")"));
	}

	// Clam the Vector above or/and beneath a value.
	public static Vector2 clamp(Vector2 vOrigin, Vector2 vClam, double value)
	{
		// Create the return vector.
		Vector2 vReturn = vClam;

		// The X Coordinate.
		if (vOrigin.x >= value)
		{
			vReturn.x = Math.max(vClam.x, value);
		}
		if (vOrigin.x < value)
		{
			vReturn.x = Math.min(vClam.x, value);
		}
		// The Y Coordinate.
		if (vOrigin.y >= value)
		{
			vReturn.y = Math.max(vClam.y, value);
		}
		if (vOrigin.y < value)
		{
			vReturn.y = Math.min(vClam.y, value);
		}

		// Return the clamped Vector.
		return vReturn;
	}

	/**
	 * Clamp a vector between a boundary.
	 * 
	 * @param v
	 *            The vector to clamp.
	 * @param boundary
	 *            The boundary vector. Clamps between the normal and inverse boundary.
	 * @return The clamped vector.
	 */
	public static Vector2 clamp(Vector2 v, Vector2 boundary)
	{
		// Create the return vector.
		Vector2 vReturn = v;

		// The X Coordinate.
		if (v.x >= 0)
		{
			vReturn.x = Math.min(v.x, boundary.x);
		}
		if (v.x < 0)
		{
			vReturn.x = Math.max(v.x, -boundary.x);
		}
		// The Y Coordinate.
		if (v.y >= 0)
		{
			vReturn.y = Math.min(v.y, boundary.y);
		}
		if (v.y < 0)
		{
			vReturn.y = Math.max(v.y, -boundary.y);
		}

		// Return the clamped Vector.
		return vReturn;
	}

	// Clam the Vector beneath a value.
	public static Vector2 clamp(Vector2 v, double value)
	{
		// Return the clamped Vector.
		return clamp(v, new Vector2(value, value));
	}

	// Round the Vector.
	public static Vector2 round(Vector2 v, int decimals)
	{
		// Create the Rounding Object for the X-coordinate.
		BigDecimal bdX = new BigDecimal(v.x);
		bdX = bdX.setScale(decimals, BigDecimal.ROUND_UP);
		// Create the Rounding Object for the Y-coordinate.
		BigDecimal bdY = new BigDecimal(v.y);
		bdY = bdY.setScale(decimals, BigDecimal.ROUND_UP);

		// Return the Rounded Vector.
		return (new Vector2(bdX.doubleValue(), bdY.doubleValue()));
	}

	// Round a value.
	public static double round(double v, int decimals)
	{
		// Create the Rounding Object for the X-coordinate.
		BigDecimal bd = new BigDecimal(v);
		bd = bd.setScale(decimals, BigDecimal.ROUND_UP);

		// Return the Rounded Vector.
		return (bd.doubleValue());
	}

	/**
	 * Returns the greatest absolute of the two vector coordinates.
	 * 
	 * @param v
	 *            The vector.
	 * @return The greatest value.
	 */
	public static double maxAbs(Vector2 v)
	{
		return Math.max(Math.abs(v.x), Math.abs(v.x));
	}

	/**
	 * Returns the greatest of the two vector coordinates.
	 * 
	 * @param v
	 *            The vector.
	 * @return The greatest value.
	 */
	public static double max(Vector2 v)
	{
		return Math.max(v.x, v.y);
	}

	// Calculates X and Y values separate and returns a Vector containing the greatest of them both. Has to be of the same sign.
	public static Vector2 max(Vector2 v1, Vector2 v2)
	{
		// Get the greatest value of two vectors.
		double dx = Math.max(v1.x, v2.x);
		double dy = Math.max(v1.y, v2.y);

		// Return the result.
		return (new Vector2(dx, dy));
	}

	// Calculates X and Y values separate and returns a Vector containing the smallest of them both. Has to be of the same sign.
	public static Vector2 min(Vector2 v1, Vector2 v2)
	{
		// Get the smallest value of two vectors.
		double dx = Math.min(v1.x, v2.x);
		double dy = Math.min(v1.y, v2.y);

		// Return the result.
		return (new Vector2(dx, dy));
	}

	/**
	 * Returns the smallest of the two vector coordinates.
	 * 
	 * @param v
	 *            The vector.
	 * @return The smallest value.
	 */
	public static double min(Vector2 v)
	{
		return Math.min(v.x, v.y);
	}
}
