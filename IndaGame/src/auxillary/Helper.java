package auxillary;

import infrastructure.zBuffer.ZPlaneResolver;
import infrastructure.zBuffer.ZValueResolver;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import physics.Shape;

/**
 * A helper class that contains a number of useful methods.
 */
public final class Helper
{
	// Colors.
	public static final Color CornFlowerBlue = new Color(100, 149, 237);
	public static final Color Mantis = new Color(116, 195, 101);

	/**
	 * The ratio between height (Y-coordinate) and depth (Z-coordinate). Used to simulate depth. Positive depth is 'upwards'.
	 */
	public static final double HeightPerDepthRatio = ((double) 1 / (double) 1);

	/**
	 * Get the 2D screen position from a 3D vector.
	 * 
	 * @param v
	 *            The 3D vector.
	 * @return The 3D vector projected into 2D.
	 */
	public static Vector2 getScreenPosition(Vector3 v)
	{
		return new Vector2(v.x, v.y - v.z * HeightPerDepthRatio);
	}

	/**
	 * Clamp a value between a min and max.
	 * 
	 * @param i
	 *            The value to clamp.
	 * @param low
	 *            The min value.
	 * @param high
	 *            The max value.
	 * @return The clamped value.
	 */
	public static double clamp(double i, double low, double high)
	{
		return java.lang.Math.max(java.lang.Math.min(i, high), low);
	}

	/**
	 * Clamp a value between a min and max.
	 * 
	 * @param i
	 *            The value to clamp.
	 * @param low
	 *            The min value.
	 * @param high
	 *            The max value.
	 * @return The clamped value.
	 */
	public static float clamp(float i, float low, float high)
	{
		return java.lang.Math.max(java.lang.Math.min(i, high), low);
	}

	/**
	 * Clamp a value between a min and max.
	 * 
	 * @param i
	 *            The value to clamp.
	 * @param low
	 *            The min value.
	 * @param high
	 *            The max value.
	 * @return The clamped value.
	 */
	public static int clamp(int i, int low, int high)
	{
		return java.lang.Math.max(java.lang.Math.min(i, high), low);
	}

	/**
	 * Clamp a value between a min and max.
	 * 
	 * @param i
	 *            The value to clamp.
	 * @param low
	 *            The min value.
	 * @param high
	 *            The max value.
	 * @return The clamped value.
	 */
	public static long clamp(long i, long low, long high)
	{
		return java.lang.Math.max(java.lang.Math.min(i, high), low);
	}

	/**
	 * Calculate the centroid of a body.
	 */
	public static Vector2 toCentroid(Rectangle shape)
	{
		// Add half the width to the X-coordinate and half the height to the Y-coordinate.
		return (new Vector2((shape.x + (shape.width / 2)), (shape.y + (shape.height / 2))));
	}

	// Calculate the centroid of a body.
	public static Vector2 toCentroid(Vector2 v, double width, double height)
	{
		// Add half the width to the X-coordinate and half the height to the Y-coordinate.
		return (new Vector2((v.x + (width / 2)), (v.y + (height / 2))));
	}

	// Calculate the centroid of a body.
	public static Vector2 toCentroid(double x, double y, double width, double height)
	{
		// Add half the width to the X-coordinate and half the height to the Y-coordinate.
		return (new Vector2((x + (width / 2)), (y + (height / 2))));
	}

	// Convert from a centroid vector a top left vector.
	public static Vector2 toTopLeft(Rectangle shape)
	{
		// Subtract half the width to the X-coordinate and half the height to the Y-coordinate.
		return (new Vector2((shape.x - (shape.width / 2)), (shape.y - (shape.height / 2))));
	}

	/**
	 * Convert from a centroid vector a top left vector.
	 * 
	 * @param shape
	 *            The shape instance to use.
	 * @return The position of the shape converted into a top-left format, ignoring rotation.
	 */
	public static Vector2 toTopLeft(Shape shape)
	{
		// Subtract half the width from the X-coordinate and half the height from the Y-coordinate.
		return new Vector2(shape.getPosition().x - (shape.getWidth() / 2), shape.getPosition().y - (shape.getHeight() / 2));
	}

	/**
	 * Convert from a centroid vector a top right vector.
	 * 
	 * @param shape
	 *            The shape instance to use.
	 * @return The position of the shape converted into a top-right format, ignoring rotation.
	 */
	public static Vector2 toTopRight(Shape shape)
	{
		// Add half the width to the X-coordinate and subtract half the height from the Y-coordinate.
		return new Vector2(shape.getPosition().x + (shape.getWidth() / 2), shape.getPosition().y - (shape.getHeight() / 2));
	}

	/**
	 * Convert from a centroid vector a bottom left vector.
	 * 
	 * @param shape
	 *            The shape instance to use.
	 * @return The position of the shape converted into a bottom-left format, ignoring rotation.
	 */
	public static Vector2 toBottomLeft(Shape shape)
	{
		// Subtract half the width from the X-coordinate and add half the height to the Y-coordinate.
		return new Vector2(shape.getPosition().x - (shape.getWidth() / 2), shape.getPosition().y + (shape.getHeight() / 2));
	}

	/**
	 * Convert from a centroid vector a bottom right vector.
	 * 
	 * @param shape
	 *            The shape instance to use.
	 * @return The position of the shape converted into a bottom-right format, ignoring rotation.
	 */
	public static Vector2 toBottomRight(Shape shape)
	{
		// Add half the width to the X-coordinate and half the height to the Y-coordinate.
		return new Vector2(shape.getPosition().x + (shape.getWidth() / 2), shape.getPosition().y + (shape.getHeight() / 2));
	}

	// Convert from a centroid vector a top left vector.
	public static Vector2 toTopLeft(double x, double y, double width, double height)
	{
		// Subtract half the width to the X-coordinate and half the height to the Y-coordinate.
		return (new Vector2((x - (width / 2)), (y - (height / 2))));
	}

	// Convert from a centroid vector to a top left vector.
	public static Vector2 toTopLeft(Vector2 v, double width, double height)
	{
		// Subtract half the width to the X-coordinate and half the height to the Y-coordinate.
		return (new Vector2((v.x - (width / 2)), (v.y - (height / 2))));
	}

	/**
	 * Move a source vector a number of steps, within given values.
	 * 
	 * @param source
	 *            The source vector.
	 * @param amount
	 *            The amount to move.
	 * @param boundary
	 *            The maximum movement possible.
	 * @return The constrained vector.
	 */
	public static Vector2 constrainMovement(Vector2 source, Vector2 amount, Vector2 boundary)
	{
		return Vector2.add(source, Vector2.clamp(amount, boundary));
	}

	/**
	 * Rotate a vector around a point.
	 * 
	 * @param position
	 *            The vector to rotate.
	 * @param origin
	 *            The origin of the rotation.
	 * @param rotation
	 *            The amount of rotation in radians.
	 * @return The rotated vector.
	 */
	public static Vector2 rotateVector(Vector2 position, Vector2 origin, float rotation)
	{
		return new Vector2((float) (origin.x + (position.x - origin.x) * Math.cos(rotation) - (position.y - origin.y) * Math.sin(rotation)), (float) (origin.y
				+ (position.y - origin.y) * Math.cos(rotation) + (position.x - origin.x) * Math.sin(rotation)));
	}

	/**
	 * Load an image.
	 * 
	 * @param path
	 *            Where to look for the image. The image is expected to be located in the images folder. Be sure to include the file suffix in the path.
	 * @return The loaded image.
	 */
	public static BufferedImage loadImage(String path)
	{
		// Try to load the image and deal with the eventual exceptions.
		try
		{
			// Load the image and return it.
			return ImageIO.read(new File("src/images/" + path));
		}
		// Catch.
		catch (Exception e)
		{
			// Display information if the loading fails.
			System.out.println("Can't find image:" + path);
			System.out.println("Load Image Error: " + e.getClass().getName() + " " + e.getMessage());
			System.exit(0);
			return null;
		}
	}

	/**
	 * Draw a string to the screen, given some options.
	 * 
	 * @param s
	 *            The text to draw.
	 * @param font
	 *            The font to use.
	 * @param transform
	 *            The text transformations in use, ie. scale and rotation.
	 * @param color
	 *            The color of of the text.
	 * @param graphics
	 *            The graphics component running the show.
	 */
	public static void drawString(String s, Font font, AffineTransform transform, Color color, Graphics2D graphics)
	{
		// Save the old color and transformation.
		Color oldColor = graphics.getColor();
		AffineTransform oldTransform = graphics.getTransform();

		// Change the transformation and color of the graphics component temporarily.
		graphics.transform(transform);
		graphics.setColor(color);

		// Draw the text.
		graphics.fill(font.createGlyphVector(graphics.getFontRenderContext(), s).getOutline());

		// Restore the color and transformation of the graphics component.
		graphics.setTransform(oldTransform);
		graphics.setColor(oldColor);
	}

	/**
	 * Clear the screen with the specified color.
	 * 
	 * @param graphics
	 *            The graphics component.
	 * @param color
	 *            The color to clear the screen with.
	 */
	public static void clearScreen(Graphics2D graphics, Color color)
	{
		// Change the graphics component's color.
		Color old = graphics.getColor();
		graphics.setColor(color);

		// Clear the screen.
		graphics.fillRect(0, 0, (int) graphics.getClipBounds().getWidth(), (int) graphics.getClipBounds().getHeight());

		// Reset the color configuration.
		graphics.setColor(old);
	}

	/**
	 * Create a plane. A = y1 (z2 - z3) + y2 (z3 - z1) + y3 (z1 - z2) B = z1 (x2 - x3) + z2 (x3 - x1) + z3 (x1 - x2) C = x1 (y2 - y3) + x2 (y3 - y1) + x3 (y1 - y2) D = -(x1 (y2 z3 - y3 z2) + x2 (y3 z1
	 * - y1 z3) + x3 (y1 z2 - y2 z1) )
	 * 
	 * @param x1
	 * @param x2
	 * @param x3
	 * @param y1
	 * @param y2
	 * @param y3
	 * @param z1
	 * @param z2
	 * @param z3
	 * @return The plane: Ax + By + Cz + D = 0
	 */
	public static double[] getPlane(double x1, double x2, double x3, double y1, double y2, double y3, double z1, double z2, double z3)
	{
		double[] plane = new double[4];

		// A
		plane[0] = y1 * (z2 - z3) + y2 * (z3 - z1) + y3 * (z1 - z2);

		// B
		plane[1] = z1 * (x2 - x3) + z2 * (x3 - x1) + z3 * (x1 - x2);

		// C
		plane[2] = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);

		// D
		plane[3] = -(x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1));

		return plane;
	}

	/**
	 * Get the z-coordinate in a plane given an x and y-coordinate.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param plane
	 *            The plane: Ax + By + Cz + D = 0
	 * @return The z-coordinate (z = -(Ax + By + D) / C
	 */
	public static double getZ(double x, double y, double[] plane)
	{
		return -(plane[0] * x + plane[1] * y + plane[3]) / plane[2];
	}

	/**
	 * Create a ZValueResolver for a given polygon and three z coordinates.
	 * 
	 * @param xpoints
	 * @param ypoints
	 * @param z1
	 *            z-coordinate for point[0,0]
	 * @param z2
	 *            z-coordinate for point[1,1]
	 * @param z3
	 *            z-coordinate for point[2,2]
	 * @return
	 */
	public static ZValueResolver zForPolygonResolver(final int[] xpoints, final int[] ypoints, double z1, double z2, double z3)
	{
		if (xpoints.length < 3) { throw new IllegalArgumentException("Polygon must have >2 points"); }

		return new ZPlaneResolver(xpoints[0], xpoints[1], xpoints[2], ypoints[0], ypoints[1], ypoints[2], z1, z2, z3)
		{

			protected GeneralPath generalPath;

			{
				generalPath = new GeneralPath();
				generalPath.moveTo(xpoints[0], ypoints[0]);
				for (int i = 1; i < xpoints.length; i++)
				{
					generalPath.lineTo(xpoints[i], ypoints[i]);
				}
				generalPath.closePath();
			}

			public double resolve(double x, double y)
			{
				if (isAntialiasingEnabled())
				{
					if (!generalPath.contains(x, y)) { return Double.MAX_VALUE; }
				}

				return super.resolve(x, y);
			};
		};
	}
}
