package graphics;

import java.awt.image.BufferedImage;

import auxillary.Helper;
import auxillary.Vector2;

/**
 * A frame is basically an image used by a sprite to portray an animation by switching between a multitude of frames in fast succession.
 */
public class Frame
{
	private String _PathName;
	private int _Width;
	private int _Height;
	private Vector2 _Origin;
	private BufferedImage _Texture;

	/**
	 * Constructor for a frame.
	 * 
	 * @param name
	 *            The path name of the frame's image.
	 */
	public Frame(String name)
	{
		initialize(name, null);
	}

	/**
	 * Constructor for a frame.
	 * 
	 * @param texture
	 *            The texture to use as the frame's image.
	 */
	public Frame(BufferedImage texture)
	{
		initialize("", texture);
	}

	/**
	 * Initialize the frame.
	 * 
	 * @param name
	 *            The path name of frame's image.
	 * @param texture
	 *            The texture of the frame. Not needed for loadable images.
	 */
	private void initialize(String name, BufferedImage texture)
	{
		// Initialize a few variables.
		_PathName = name;
		_Texture = texture;

		// Update the frame's bounds.
		updateBounds();

		// Calculate the origin.
		_Origin = new Vector2(_Width / 2, _Height / 2);
	}

	/**
	 * Update the bounds of this frame.
	 */
	private void updateBounds()
	{
		// If a texture has already been loaded, use that. Otherwise load it now.
		if (_Texture != null)
		{
			_Height = _Texture.getHeight();
			_Width = _Texture.getWidth();
		}
		else
		{
			BufferedImage texture = Helper.loadImage(_PathName);
			_Height = texture.getHeight();
			_Width = texture.getWidth();
		}
	}

	/**
	 * Get the path name of the frame.
	 * 
	 * @return The path name.
	 */
	public String getPathName()
	{
		return _PathName;
	}

	/**
	 * Get the texture of the frame, if there exists one.
	 * 
	 * @return The texture of the frame.
	 */
	public BufferedImage getTexture()
	{
		return _Texture;
	}

	/**
	 * Get the height of the frame.
	 * 
	 * @return The height.
	 */
	public int getHeight()
	{
		return _Height;
	}

	/**
	 * Get the width of the frame.
	 * 
	 * @return The width.
	 */
	public int getWidth()
	{
		return _Width;
	}
}
