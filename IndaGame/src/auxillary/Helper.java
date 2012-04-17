package auxillary;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import physics.Shape;

/**
 * A helper class that contains a number of useful methods.
 */
public final class Helper
{
	public static final Color CornFlowerBlue = new Color(100, 149, 237);
	public static final Color Mantis = new Color(116, 195, 101);

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

	// Convert from a centroid vector a top left vector.
	public static Vector2 toTopLeft(Vector2 v, double width, double height)
	{
		// Subtract half the width to the X-coordinate and half the height to the Y-coordinate.
		return (new Vector2((v.x - (width / 2)), (v.y - (height / 2))));
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
}
