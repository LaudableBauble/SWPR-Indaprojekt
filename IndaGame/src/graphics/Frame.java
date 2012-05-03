package graphics;

import java.awt.image.BufferedImage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import auxillary.Helper;
import auxillary.Vector2;

/**
 * A frame is basically an image used by a sprite to portray an animation by switching between a multitude of frames in fast succession.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Frame
{
	@XmlElement(name = "PathName")
	private String _PathName;
	@XmlElement(name = "Width")
	private float _Width;
	@XmlElement(name = "Height")
	private float _Height;
	@XmlElement(name = "Origin")
	private Vector2 _Origin;
	private BufferedImage _Texture;

	/**
	 * Empty constructor for a frame.
	 */
	public Frame()
	{
		this("");
	}

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
			if (_PathName.equals("")) { return; }

			BufferedImage texture = Helper.loadImage(_PathName, true);
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
	 * Set the path name of the frame.
	 * 
	 * @param path
	 *            The path name.
	 */
	public void setPathName(String path)
	{
		_PathName = path;
		updateBounds();
		// Calculate the origin.
		_Origin = new Vector2(_Width / 2, _Height / 2);
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
	public float getHeight()
	{
		return _Height;
	}

	/**
	 * Get the width of the frame.
	 * 
	 * @return The width.
	 */
	public float getWidth()
	{
		return _Width;
	}

	/**
	 * Get the frame's origin.
	 * 
	 * @return The origin of the frame.
	 */
	public Vector2 getOrigin()
	{
		return _Origin;
	}
}
