package graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import auxillary.Helper;
import auxillary.Vector2;

/**
 * A sprite is either an image or a collection of images used to simulate an animation.
 */
public class Sprite
{
	// Create the sprites.
	private ArrayList<HashMap> _Sprites;
	// Create a String array that holds all sprite paths.
	private ArrayList<ArrayList<String>> _SpritePath;
	// The Sprite Index.
	private int _SpriteIndex;
	// The Frame Index.
	private int _FrameIndex;
	// The Time per Frame, in milliseconds.
	private double _TimePerFrame;
	// The elapsed time until the beginning of the current frame. 0 = the start time each frame update, 1 = the difference since the start time.
	private double[] _CurrentElapsedTime;
	// Whether the sprite is allowed to animate.
	private boolean _AnimationEnabled;

	/**
	 * Constructor for a sprite.
	 */
	public Sprite()
	{
		// Initialize the variables.
		initialize();
	}

	private void initialize()
	{
		// Initialize the variables.
		_Sprites = new ArrayList<HashMap>();
		_SpritePath = new ArrayList<ArrayList<String>>();
		_Sprites.add(new HashMap());
		_SpritePath.add(new ArrayList<String>());
		_SpriteIndex = 0;
		_FrameIndex = 0;
		_TimePerFrame = 0;
		_CurrentElapsedTime = new double[2];
		_CurrentElapsedTime[0] = System.currentTimeMillis();
		_CurrentElapsedTime[1] = 0;
	}

	/**
	 * Load content.
	 * 
	 * @param path
	 *            The path of an image.
	 */
	public void loadContent(String path)
	{
		// Load the sprite and send it to the sprites variable.
		_Sprites.get(0).put(path, loadSprite(path, _SpriteIndex));
	}

	/**
	 * Update the sprite.
	 */
	public void update()
	{
		// Stop here if animation is not enabled.
		if (!_AnimationEnabled) { return; }

		// Add the elapsed time since last update.
		_CurrentElapsedTime[1] = (System.currentTimeMillis() - _CurrentElapsedTime[0]);

		// If it's time to change sprite frame.
		if (_CurrentElapsedTime[1] >= _TimePerFrame)
		{
			// Check if the sprite frame limit isn't reached.
			if (_FrameIndex < (_SpritePath.get(_SpriteIndex).size() - 1))
			{
				// Increment the sprite frame index.
				_FrameIndex++;
			}
			// Else, reset the spriteIndex.
			else
			{
				_FrameIndex = 0;
			}

			// Reset the elapsed time counter.
			_CurrentElapsedTime[0] = System.currentTimeMillis();
		}
	}

	/**
	 * Draw the sprite.
	 * 
	 * @param graphics
	 *            The graphics component.
	 * @param path
	 *            The path of the image.
	 * @param v
	 *            The position of the image.
	 */
	public void draw(Graphics2D graphics, String path, Vector2 v)
	{
		graphics.drawImage(getSprite(path, _SpriteIndex), (int) v.x, (int) v.y, null);
	}

	/**
	 * Draw the sprite.
	 * 
	 * @param graphics
	 *            The graphics component.
	 * @param pathIndex
	 *            The path index of the image.
	 * @param v
	 *            The position of the image.
	 */
	public void draw(Graphics2D graphics, int pathIndex, Vector2 v)
	{
		graphics.drawImage(getSprite(pathIndex, _SpriteIndex), (int) v.x, (int) v.y, null);
	}

	/**
	 * Draw the sprite.
	 * 
	 * @param graphics
	 *            The graphics component.
	 * @param v
	 *            The position of the image.
	 */
	public void draw(Graphics2D graphics, Vector2 v)
	{
		// Try.
		try
		{
			// Draw the sprite at the correct vector.
			graphics.drawImage(getSprite(_FrameIndex, _SpriteIndex), (int) v.x, (int) v.y, null);
		}
		// Catch
		catch (Exception e)
		{
			System.out.println(this + ": Draw Sprite Error. (" + e + ")");
		}
	}

	/**
	 * Load the sprite.
	 * 
	 * @param path
	 *            The path of the image.
	 * @param index
	 *            The index of the image.
	 * @return The image.
	 */
	public BufferedImage loadSprite(String path, int index)
	{
		// Create the Url variable that'll hold the sprite path.
		URL url = null;

		// Try.
		try
		{
			// Add the sprite Path.
			_SpritePath.get(index).add(path);
		}
		// Catch the exceptions and display them.
		catch (Exception e)
		{
			System.out.println(this + ": Add Sprite Path Error. (" + e + ")");
		}

		// Load the sprite and return it.
		return Helper.loadImage(path);
	}

	/**
	 * Get a sprite.
	 * 
	 * @param path
	 *            The path of the image.
	 * @param index
	 *            The index of the image.
	 * @return The image.
	 */
	public BufferedImage getSprite(String path, int index)
	{
		// Get the Sprite.
		BufferedImage img = (BufferedImage) _Sprites.get(index).get(path);

		// If the sprite doesn't exist, load it instead.
		if (img == null)
		{
			// Load the Sprite.
			img = loadSprite("Images/" + path, index);
			_Sprites.get(index).put(path, img);
		}

		// Return the sprite.
		return img;
	}

	/**
	 * Get a sprite.
	 * 
	 * @param pathIndex
	 *            The path index of the image.
	 * @param index
	 *            The index of the image.
	 * @return The image.
	 */
	public BufferedImage getSprite(int pathIndex, int index)
	{
		// Check if the pathIndex is within range.
		if (_SpritePath.get(index).size() >= pathIndex)
		{
			return (BufferedImage) _Sprites.get(index).get(_SpritePath.get(index).get(pathIndex));
		}
		// Else load the last sprite in the array.
		else
		{
			return (BufferedImage) _Sprites.get(index).get(_SpritePath.get(index).get(_SpritePath.get(index).size() - 1));
		}
	}

	/**
	 * Add a image to the sprite.
	 * 
	 * @param path
	 *            The path of the image.
	 */
	public void addSprite(String path)
	{
		// Try to add a new sprite.
		try
		{
			// Add a String array and a new HashMap to the respective Lists.
			_Sprites.add(new HashMap());
			_SpritePath.add(new ArrayList<String>());
			// Load the sprite and send it to the sprites variable.
			_Sprites.get(_Sprites.size() - 1).put(path, loadSprite(path, _Sprites.size() - 1));
		}
		// Catch the exceptions and display them.
		catch (Exception e)
		{
			System.out.println(this + ": Add Sprite Error. (" + e + ")");
		}
	}

	/**
	 * Add a frame to the sprite.
	 * 
	 * @param path
	 *            The path of the image.
	 * @param index
	 *            The index of the sprite.
	 */
	public void addFrame(String path, int index)
	{
		// Try to add a new sprite frame.
		try
		{
			// Load the sprite and send it to the sprites variable.
			_Sprites.get(index).put(path, loadSprite(path, index));
		}
		// Catch the exceptions and display them.
		catch (Exception e)
		{
			System.out.println(this + ": Add Sprite Frame Error. (" + e + ")");
		}
	}

	/**
	 * Get the sprite's width.
	 * 
	 * @param path
	 *            The path of the image.
	 * @param index
	 *            The index of the sprite.
	 * @return The width of the sprite.
	 */
	public int getWidth(String path, int index)
	{
		return getSprite(path, index).getWidth(null);
	}

	/**
	 * Get the sprite's height.
	 * 
	 * @param path
	 *            The path of the image.
	 * @param index
	 *            The index of the sprite.
	 * @return The height of the sprite.
	 */
	public int getHeight(String path, int index)
	{
		return getSprite(path, index).getHeight(null);
	}

	/**
	 * Get the sprite's width.
	 * 
	 * @param pathIndex
	 *            The path index of the image.
	 * @param index
	 *            The index of the sprite.
	 * @return The width of the sprite.
	 */
	public int getWidth(int pathIndex, int index)
	{
		return getSprite(pathIndex, index).getWidth(null);
	}

	/**
	 * Get the sprite's height.
	 * 
	 * @param pathIndex
	 *            The path index of the image.
	 * @param index
	 *            The index of the sprite.
	 * @return The height of the sprite.
	 */
	public int getHeight(int pathIndex, int index)
	{
		return getSprite(pathIndex, index).getHeight(null);
	}

	/**
	 * Get the sprite's timePerFrame variable.
	 * 
	 * @return The time per frame.
	 */
	public double getTimePerFrame()
	{
		return _TimePerFrame;
	}

	/**
	 * Get the sprite's spriteIndex variable.
	 * 
	 * @return The sprite's index.
	 */
	public int getSpriteIndex()
	{
		return _SpriteIndex;
	}

	/**
	 * Set the sprite's timePerFrame variable.
	 * 
	 * @param value
	 *            The new time per frame.
	 */
	public void setTimePerFrame(double value)
	{
		_TimePerFrame = value;
	}

	/**
	 * Set the sprite's spriteIndex variable.
	 * 
	 * @param value
	 *            The new sprite index.
	 */
	public void setSpriteIndex(int value)
	{
		_SpriteIndex = value;
		_FrameIndex = 0;
	}

	/**
	 * Set whether the sprite is allowed to animate. If true, this will reset the sprite's frame index.
	 * 
	 * @param value
	 *            Whether the sprite is allowed to animate.
	 */
	public void setAnimationEnabled(boolean value)
	{
		_AnimationEnabled = value;
		_FrameIndex = (_AnimationEnabled) ? _FrameIndex : 0;
	}
}