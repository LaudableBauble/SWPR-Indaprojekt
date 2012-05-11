package auxillary;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import main.Entity;
import main.Scene;
import main.SceneManager;

import physics.Shape;

/**
 * A helper class that contains a number of useful methods.
 */
public final class Helper
{
	// Colors.
	public static final Color CornFlowerBlue = new Color(100, 149, 237);
	public static final Color Mantis = new Color(116, 195, 101);
	public static final Color ZealPalace = new Color(48, 32, 16);

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
		return Math.max(java.lang.Math.min(i, high), low);
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
		return Math.max(java.lang.Math.min(i, high), low);
	}

	/**
	 * Get a random direction vector.
	 * 
	 * @return The random direction.
	 */
	public static Vector2 getRandomDirection()
	{
		return new Vector2(Math.random() * ((Math.random() > .5) ? 1 : -1), Math.random() * ((Math.random() > .5) ? 1 : -1));
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
	 *            Where to look for the image relative to this game. The image is expected to be located in the images folder. Be sure to include the file suffix in the path.
	 * @param pathHelp
	 *            If you need help with setting up the path, ie. positioning you in the images folder.
	 * @return The loaded image.
	 */
	public static BufferedImage loadImage(String path, boolean pathHelp)
	{
		// If path help is enabled.
		if (pathHelp) { return loadImage("src/data/images/" + path); }
		return loadImage(path);
	}

	/**
	 * Load an image.
	 * 
	 * @param path
	 *            Where to look for the image relative to this game. Be sure to include the file suffix in the path.
	 * @return The loaded image.
	 */
	public static BufferedImage loadImage(String path)
	{
		// Try to load the image and deal with the probable exceptions.
		try
		{
			// Load the image and return it.
			return makeCompatible(ImageIO.read(new File(path)));
			// return ImageIO.read(new File(path));
		}
		// Catch.
		catch (Exception e)
		{
			// Display information if the loading fails.
			System.out.println("Cannot find image:" + path);
			System.out.println("Load Image Error: " + e.getClass().getName() + " " + e.getMessage());
			System.exit(0);
			return null;
		}
	}

	/**
	 * Makes an image compatible to the depth composite, ie. it forces the image to use a component color model.
	 * 
	 * @param image
	 *            The image to make compatible.
	 * @return The compatible image.
	 */
	public static BufferedImage makeCompatible(BufferedImage image)
	{
		// Create the compatible image.
		// BufferedImage compatible = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		// BufferedImage compatible = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage compatible = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()
				.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);

		// Draw the image onto the ensured compatible image, converting their color model's in the process.
		Graphics2D g = compatible.createGraphics();
		g.drawRenderedImage(image, new AffineTransform());
		g.dispose();

		// Return the compatible image.
		return compatible;
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
	 * Load a scene from file by deserialization. Assumes that the file is located in the data/scenes folder. Be sure to include the file suffix in the path.
	 * 
	 * @param path
	 *            The path to the scene file.
	 * @param manager
	 *            The scene's manager.
	 * @return The loaded scene.
	 */
	public static Scene loadScene(String path, SceneManager manager)
	{
		try
		{
			// Create the file.
			File file = new File("src/data/scenes/" + path);

			// Set up the unmarshaller and load the scene content.
			SceneContent content = (SceneContent) JAXBContext.newInstance(SceneContent.class).createUnmarshaller().unmarshal(file);

			// Convert the content data into a scene and return it.
			return content.createScene(manager);

		}
		catch (JAXBException e)
		{
			System.out.println("Load Scene Error: " + e.getClass().getName() + " - " + e.getMessage());
		}

		// No scene found.
		return null;
	}

	/**
	 * Save a scene to file by serialization.
	 * 
	 * @param scene
	 *            The scene to save.
	 */
	public static void saveScene(Scene scene)
	{
		try
		{
			// Create the file.
			File file = new File("src/data/scenes/" + scene.getName() + ".xml");

			// Create the scene content data.
			SceneContent content = SceneContent.createContent(scene);

			// Set up the marshaller.
			Marshaller marshaller = JAXBContext.newInstance(SceneContent.class).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Serialize the scene to the file.
			marshaller.marshal(content, file);

		}
		catch (JAXBException e)
		{
			System.out.println("Save Scene Error: " + e.getClass().getName() + " - " + e.getMessage());
		}
	}

	/**
	 * Load an entity from file by deserialization. Assumes that the file is located in the data/entities folder. Be sure to include the file suffix in the path.
	 * 
	 * @param path
	 *            The path to the entity file.
	 * @return The loaded entity.
	 */
	public static Entity loadEntity(String path)
	{
		try
		{
			// Create the file.
			File file = new File("src/data/entities/" + path);

			// Set up the unmarshaller and load the entity content.
			EntityContent content = (EntityContent) JAXBContext.newInstance(EntityContent.class).createUnmarshaller().unmarshal(file);

			// Convert the content data into an entity and return it.
			return content.createEntity();

		}
		catch (JAXBException e)
		{
			System.out.println("Load Entity Error: " + e.getClass().getName() + " - " + e.getMessage());
		}

		// No entity found.
		return null;
	}

	/**
	 * Save an entity to file by serialization.
	 * 
	 * @param entity
	 *            The entity to save.
	 */
	public static void saveEntity(Entity entity)
	{
		try
		{
			// Create the file.
			File file = new File("src/data/entities/" + entity.getName() + ".xml");

			// Create the entity content data.
			EntityContent content = EntityContent.createContent(entity);

			// Set up the marshaller.
			Marshaller marshaller = JAXBContext.newInstance(EntityContent.class).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Serialize the entity to the file.
			marshaller.marshal(content, file);

		}
		catch (JAXBException e)
		{
			System.out.println("Save Entity Error: " + e.getClass().getName() + " - " + e.getMessage());
		}
	}

	/**
	 * Create an image from an array of pixel data.
	 * 
	 * @param pixels
	 *            The array of pixels.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @return The image.
	 */
	public static BufferedImage getImageFromArray(int[] pixels, int width, int height)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = (WritableRaster) image.getData();
		raster.setPixels(0, 0, width, height, pixels);
		return image;
	}

	/**
	 * Create a grayscale image from an array.
	 * 
	 * @param data
	 *            The data array.
	 * @param width
	 *            The width of the image.
	 * @param height
	 *            The height of the image.
	 * @return The image.
	 */
	public static BufferedImage createGrayscale(int[][] data, int width, int height)
	{
		// Create the image.
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Go through the array and set all pixels.
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				int value = data[y][x] << 16 | data[y][x] << 8 | data[y][x];
				image.setRGB(x, y, value);
			}
		}

		return image;
	}

	/**
	 * Save an image to disk.
	 * 
	 * @param image
	 *            The image to save.
	 */
	public static void saveImage(BufferedImage image)
	{
		try
		{
			ImageIO.write(image, "bmp", new File("src/data/image.bmp"));
		}
		catch (Exception e)
		{
			System.out.println("Save Image Error: " + e.getClass().getName() + " " + e.getMessage());
		}
	}
}
