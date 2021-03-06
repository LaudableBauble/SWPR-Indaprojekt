package graphics;

import infrastructure.Enums.Orientation;
import infrastructure.Enums.Visibility;
import infrastructure.GameTimer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import auxillary.Helper;
import auxillary.Vector2;

/**
 * A sprite is either an image or a collection of images used to simulate an animation.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Sprite
{
	// Fields.
	private SpriteManager _Manager;
	private BufferedImage _Texture;
	@XmlElement(name = "Position")
	private Vector2 _Position;
	@XmlElement(name = "PositionOffset")
	private Vector2 _PositionOffset;
	@XmlElement(name = "Name")
	private String _Name;
	@XmlElement(name = "Tag")
	private String _Tag;
	@XmlElement(name = "Frames")
	private ArrayList<Frame> _Frames;
	private int _FrameIndex;
	@XmlElement(name = "TimePerFrame")
	private float _TimePerFrame;
	@XmlElement(name = "FrameStartIndex")
	private int _FrameStartIndex;
	@XmlElement(name = "FrameEndIndex")
	private int _FrameEndIndex;
	@XmlElement(name = "EnableAnimation")
	private boolean _EnableAnimation;
	@XmlElement(name = "AnimationDirection")
	private boolean _AnimationDirection;
	private float _TotalElapsedTime;
	@XmlElement(name = "Rotation")
	private double _Rotation;
	@XmlElement(name = "Scale")
	private double _Scale;
	@XmlElement(name = "Depth")
	private int _Depth;
	@XmlElement(name = "Transparence")
	private float _Transparence;
	@XmlElement(name = "Visibility")
	private Visibility _Visibility;
	@XmlElement(name = "Orientation")
	private Orientation _Orientation;

	/**
	 * Empty constructor for a sprite.
	 */
	public Sprite()
	{
		this("");
	}

	/**
	 * Constructor for a sprite.
	 * 
	 * @param name
	 *            The name of the sprite.
	 */
	public Sprite(String name)
	{
		// Initialize the sprite.
		initialize(name);
	}

	/**
	 * Initialize the sprite.
	 * 
	 * @param name
	 *            The name of the sprite.
	 */
	private void initialize(String name)
	{
		// Initialize some variables.
		_Manager = null;
		_Name = name;
		_Position = new Vector2(0, 0);
		_TimePerFrame = .2f;
		_Scale = 1;
		_Depth = 0;
		_Rotation = 0;
		_PositionOffset = Vector2.empty();
		_Tag = "";
		_Transparence = 1;
		_AnimationDirection = true;
		_Visibility = Visibility.Visible;
		_Orientation = Orientation.Right;
		_Frames = new ArrayList<Frame>();
		_TotalElapsedTime = 0;
	}

	/**
	 * Load content.
	 */
	public void loadContent()
	{
		// Load the first frame, if there exists one.
		loadFrame();
	}

	/**
	 * Update the sprite and all its frames.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Update the frames.
		if (_EnableAnimation)
		{
			updateIndices();
			updateFrame(gameTime);
		}
	}

	/**
	 * Draw the sprite and its current frame to the screen.
	 * 
	 * @param graphics
	 *            The graphics component to use.
	 */
	public void draw(Graphics2D graphics)
	{
		// If the sprite is not visible, end here.
		if (_Visibility == Visibility.Invisible) { return; }

		// Manage the orientation.
		if (_Orientation == Orientation.Left)
		{
		}

		// Try to draw.
		try
		{
			// Temporarily save the old matrix.
			// AffineTransform old = graphics.getTransform();
			// Whip up a drawing matrix to enable scale and rotation and add it to the current matrix.
			// graphics.transform(AffineTransform.getRotateInstance(_Rotation).getScaleInstance(_Scale, _Scale));

			// Rotate and scale the sprite.
			/*
			 * AffineTransform matrix = new AffineTransform(); matrix.translate(_Frames.get(_FrameIndex).getOrigin().x, _Frames.get(_FrameIndex).getOrigin().y); matrix.rotate(_Rotation);
			 * matrix.scale(_Scale, _Scale); matrix.translate(-_Frames.get(_FrameIndex).getOrigin().x / _Scale, -_Frames.get(_FrameIndex).getOrigin().y / _Scale);
			 * 
			 * BufferedImageOp bio = new AffineTransformOp(matrix, AffineTransformOp.TYPE_BILINEAR);
			 */

			// The 'real' position, including the offset and origin.
			Vector2 position = Vector2.subtract(Vector2.add(_Position, _PositionOffset), _Frames.get(_FrameIndex).getOrigin());

			// Draw the sprite.
			graphics.drawImage(_Texture, (int) position.x, (int) position.y, null);
			// graphics.drawImage(bio.filter(_Texture, null), (int) position.x, (int) position.y, null);

			// Revert to the old matrix configuration.
			// graphics.setTransform(old);
		}
		// Catch
		catch (Exception e)
		{
			System.out.println(this + ": Draw Sprite Error. (" + e + ")");
		}
	}

	/**
	 * Add a frame to the sprite.
	 * 
	 * @param frame
	 *            The frame to add.
	 * @return The added frame.
	 */
	public Frame addFrame(Frame frame)
	{
		// Add the frame to the list of frames.
		_Frames.add(frame);
		_FrameEndIndex++;
		return frame;
	}

	/**
	 * Get a frame's index.
	 * 
	 * @param name
	 *            The path name of the frame.
	 * @return The index of the frame.
	 */
	public int getFrameIndex(String name)
	{
		// Loop through the list of frames and find the one with the right name.
		for (Frame f : new ArrayList<Frame>(_Frames))
		{
			if (f.getPathName().equals(name)) { return _Frames.indexOf(f); }
		}

		// No frame found.
		return -1;
	}

	/**
	 * Remove a frame from the sprite.
	 * 
	 * @param name
	 *            The path name of the frame.
	 */
	public void removeFrame(String name)
	{
		_Frames.remove(getFrameIndex(name));
	}

	/**
	 * Load the current frame's texture.
	 */
	public void loadFrame()
	{
		try
		{
			// Quit if there is not enough frames.
			if (_FrameIndex >= _Frames.size()) { return; }

			// If a frame has a texture already stored on its premises, load that texture.
			if (_Frames.get(_FrameIndex).getTexture() != null)
			{
				_Texture = _Frames.get(_FrameIndex).getTexture();
			}
			// Otherwise load one by using the name of the frame.
			else
			{
				_Texture = Helper.loadImage(_Frames.get(_FrameIndex).getPathName(), true);
			}
		}
		catch (Exception e)
		{
			System.out.println(this + ": Load Frame Error. (" + e + ")");
		}
	}

	/**
	 * Update the sprite's frames.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void updateFrame(GameTimer gameTime)
	{
		// Get the time since the last Update.
		_TotalElapsedTime += (float) gameTime.getElapsedTime().TotalSeconds();

		// If it's not time to change frame yet, stop here.
		if (_TotalElapsedTime < _TimePerFrame) { return; }

		// If the animation is going forward.
		if (_AnimationDirection)
		{
			// If the number of drawn frames are less than the total, change to the next.
			if (_FrameIndex < _FrameEndIndex)
			{
				_FrameIndex++;
			}
			// Make the animation start over.
			else
			{
				_FrameIndex = _FrameStartIndex;
			}
		}
		// If the animation is going backwards.
		else
		{
			// If the number of drawn frames are less than the total, change to the next.
			if (_FrameIndex > _FrameStartIndex)
			{
				_FrameIndex--;
			}
			// Make the animation start over.
			else
			{
				_FrameIndex = _FrameEndIndex;
			}
		}

		// Load the new frame's texture into memory.
		loadFrame();

		// Subtract the time per frame, to be certain the next frame is drawn in time.
		_TotalElapsedTime -= _TimePerFrame;
	}

	/**
	 * Update all indices so that they stay within bounds.
	 */
	private void updateIndices()
	{
		// The min and max values.
		int min = 0;
		int max = _Frames.size() - 1;

		// Clamp all indeces.
		_FrameIndex = Math.min(Math.max(_FrameIndex, 0), Math.max(max, 0));
		_FrameStartIndex = Math.min(Math.max(_FrameStartIndex, 0), Math.max(max, 0));
		_FrameEndIndex = Math.min(Math.max(_FrameEndIndex, 0), Math.max(max, 0));
	}

	/**
	 * Get the manager this sprite belongs to.
	 * 
	 * @return The sprite manager.
	 */
	public SpriteManager getSpriteManager()
	{
		return _Manager;
	}

	/**
	 * Set the manager this sprite belongs to.
	 * 
	 * @param manager
	 *            The sprite manager.
	 */
	public void setSpriteManager(SpriteManager manager)
	{
		_Manager = manager;
	}

	/**
	 * Get the sprite's current texture.
	 * 
	 * @return The texture.
	 */
	public BufferedImage getTexture()
	{
		return _Texture;
	}

	/**
	 * Get the sprite's position. This is where the origin of the sprite will be drawn.
	 * 
	 * @return The position.
	 */
	public Vector2 getPosition()
	{
		return _Position;
	}

	/**
	 * Set the sprite's position. This is where the origin of the sprite will be drawn.
	 * 
	 * @param position
	 *            The new position.
	 */
	public void setPosition(Vector2 position)
	{
		_Position = position;
	}

	/**
	 * Set the sprite's position offset, ie. the amount to add to the position during drawing.
	 * 
	 * @param offset
	 *            The new position offset.
	 */
	public void setPositionOffset(Vector2 offset)
	{
		_PositionOffset = offset;
	}

	/**
	 * Get the sprite's name. This has no bearing on any image paths.
	 * 
	 * @return The name.
	 */
	public String getName()
	{
		return _Name;
	}

	/**
	 * Get the tag of the body, used to get a specific sprite.
	 * 
	 * @return The tag.
	 */
	public String getTag()
	{
		return _Tag;
	}

	/**
	 * Get the number of frames.
	 * 
	 * @return The number of frames.
	 */
	public int getFrameCount()
	{
		return _Frames.size();
	}

	/**
	 * Get the current frame index.
	 * 
	 * @return The current frame index.
	 */
	public int getCurrentFrameIndex()
	{
		return _FrameIndex;
	}

	/**
	 * Set the current frame index.
	 * 
	 * @param index
	 *            The current frame index.
	 */
	public void setCurrentFrameIndex(int index)
	{
		_FrameIndex = index;
	}

	/**
	 * Get the current frame.
	 * 
	 * @return The current frame.
	 */
	public Frame getCurrentFrame()
	{
		return (_FrameIndex < _Frames.size()) ? _Frames.get(_FrameIndex) : null;
	}

	/**
	 * Set the time in seconds every frame has on screen before the next appears.
	 * 
	 * @param timePerFrame
	 *            The new time per frame.
	 */
	public void setTimePerFrame(float timePerFrame)
	{
		_TimePerFrame = timePerFrame;
	}

	/**
	 * Set whether the sprite enables animations or not.
	 * 
	 * @param enable
	 *            Whether the sprite enables animations.
	 */
	public void setEnableAnimation(boolean enable)
	{
		_EnableAnimation = enable;
	}

	/**
	 * Get the depth the sprite is being drawn at.
	 * 
	 * @return The depth.
	 */
	public int getDepth()
	{
		return _Depth;
	}

	/**
	 * Set the transparence of the sprite. The values lies between 0 and 1.
	 * 
	 * @param transparence
	 *            The new transparence.
	 */
	public void setTransparence(float transparence)
	{
		_Transparence = transparence;
	}

	/**
	 * Set the state of visibility for the sprite.
	 * 
	 * @param visiblity
	 *            The new state of visibility.
	 */
	public void setVisibility(Visibility visiblity)
	{
		_Visibility = visiblity;
	}

	/**
	 * Set the sprite's scale. 1 is default.
	 * 
	 * @param scale
	 *            The new scale.
	 */
	public void setScale(double scale)
	{
		_Scale = scale;
	}

	/**
	 * Set the sprite's rotation.
	 * 
	 * @param rotation
	 *            The new rotation.
	 */
	public void setRotation(double rotation)
	{
		_Rotation = rotation;
	}
}
