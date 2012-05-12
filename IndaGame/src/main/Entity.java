package main;

import graphics.Frame;
import graphics.Sprite;
import graphics.SpriteManager;
import infrastructure.Enums.DepthDistribution;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.Graphics2D;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import physics.Body;
import physics.PhysicsSimulator;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * An entity, sporting a body and a sprite, is the most basic building blocks of the physical game world.
 */
public class Entity
{
	// The name of the entity. Primarily used as file name when serialized.
	protected String _Name;
	protected Scene _Scene;
	// The Sprite and Body.
	protected SpriteManager _Sprites;
	protected Body _Body;

	/**
	 * Constructor for an entity.
	 * 
	 * @param scene
	 *            The scene this entity is part of.
	 */
	public Entity(Scene scene)
	{
		initialize(scene);
	}

	/**
	 * Initialize the entity.
	 * 
	 * @param scene
	 *            The scene this entity is part of.
	 */
	protected void initialize(Scene scene)
	{
		// Initialize the variables.
		_Name = "";
		_Scene = scene;
		_Sprites = new SpriteManager();
		_Body = new Body(_Scene != null ? _Scene.getPhysicsSimulator() : null);
		_Body.setEntity(this);
		_Body.addBody();
	}

	/**
	 * Load content and add a sprite. This uses default body shape values.
	 * 
	 * @param spritePath
	 *            The path of the main sprite.
	 */
	public void loadContent(String spritePath)
	{
		loadContent(spritePath, -1);
	}

	/**
	 * Load content and add a sprite.
	 * 
	 * @param spritePath
	 *            The path of the main sprite.
	 * @param height
	 *            The height of the shape as seen on picture. This is used for collision data. -1 results in the use of the full height of the sprite and a depth of 1.
	 */
	public void loadContent(String spritePath, float height)
	{
		// Clear all sprites.
		_Sprites = new SpriteManager();

		// Add a sprite.
		_Sprites.addSprite(new Sprite("Entity"));
		_Sprites.getSprite(0).addFrame(new Frame(spritePath));

		// Load all sprites' content.
		_Sprites.loadContent();

		// Set the shape of the body.
		_Body.getShape().setWidth(_Sprites.getSprite(0).getCurrentFrame().getWidth());
		_Body.getShape().setHeight((height == -1) ? _Sprites.getSprite(0).getCurrentFrame().getHeight() : height);
		_Body.getShape().setDepth((height == -1) ? 1 : _Sprites.getSprite(0).getCurrentFrame().getHeight() - height);

		// Update the sprite's position offset.
		_Sprites.getSprite(0).setPositionOffset(new Vector2(0, -_Sprites.getSprite(0).getCurrentFrame().getOrigin().y + (_Body.getShape().getHeight() / 2)));
	}

	/**
	 * Handle input.
	 * 
	 * @param input
	 *            The input manager.
	 */
	public void handleInput(InputManager input)
	{
		// Check if the left mouse button is down.
		if (input.isMouseButtonDown(1))
		{
			// Transform the mouse coordinates into world space.

			// Check if the Body has been clicked on.
			if ((input.mouseEventPosition().x <= (_Body.getLayeredPosition().x + (_Body.getShape().getWidth() / 2)))
					&& (input.mouseEventPosition().x >= (_Body.getLayeredPosition().x - (_Body.getShape().getWidth() / 2)))
					&& (input.mouseEventPosition().y <= (_Body.getLayeredPosition().y + (_Body.getShape().getHeight() / 2)))
					&& (input.mouseEventPosition().y >= (_Body.getLayeredPosition().y - (_Body.getShape().getHeight() / 2))))
			{
				// Turn the debug isClicked variable on.
				_Body._IsClicked = !_Body._IsClicked;
			}
		}
	}

	/**
	 * Update the entity.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Update the sprites.
		_Sprites.update(gameTime, Helper.getScreenPosition(new Vector3(_Body.getLayeredPosition(), _Body.getShape().getBottomDepth())));
	}

	/**
	 * Draw the entity.
	 * 
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(Graphics2D graphics)
	{
		// Draw the sprite.
		_Sprites.draw(graphics);
	}

	/**
	 * Get a depth sorting value for this entity at the given local x and y-coordinates, ie. as seen from its image.
	 * 
	 * @param x
	 *            The local x-coordinate.
	 * @param y
	 *            The local y-coordinate.
	 * @return The depth sorting value for this entity.
	 */
	public double getDepthSort(int x, int y)
	{
		// No stored depth value found, get a fresh one.
		return _Body.getShape().getDepthSort(x, y);
	}

	/**
	 * Get the entity's body.
	 * 
	 * @return The body of the entity.
	 */
	public Body getBody()
	{
		return _Body;
	}

	/**
	 * Get the manager of all this entity's sprites.
	 * 
	 * @return The sprite manager.
	 */
	public SpriteManager getSprites()
	{
		return _Sprites;
	}

	/**
	 * Set the entity's depth. This also modifies its height as well as updates the main sprite.
	 * 
	 * @param depth
	 *            The new depth.
	 */
	public void setDepth(float depth)
	{
		// Set the body's height and depth.
		_Body.getShape().setHeight((depth < 1) ? _Sprites.getSprite(0).getCurrentFrame().getHeight() : _Sprites.getSprite(0).getCurrentFrame().getHeight() - depth);
		_Body.getShape().setDepth((depth < 1) ? 1 : depth);

		// Update the sprite's position offset.
		_Sprites.getSprite(0).setPositionOffset(new Vector2(0, -_Sprites.getSprite(0).getCurrentFrame().getOrigin().y + (_Body.getShape().getHeight() / 2)));
	}

	/**
	 * Set the entity's height. This also modifies its depth as well as updates the main sprite.
	 * 
	 * @param height
	 *            The new height.
	 */
	public void setHeight(float height)
	{
		// Set the body's height and depth.
		_Body.getShape().setHeight((height == -1) ? _Sprites.getSprite(0).getCurrentFrame().getHeight() : height);
		_Body.getShape().setDepth((height == -1) ? 1 : _Sprites.getSprite(0).getCurrentFrame().getHeight() - height);

		// Update the sprite's position offset.
		_Sprites.getSprite(0).setPositionOffset(new Vector2(0, -_Sprites.getSprite(0).getCurrentFrame().getOrigin().y + (_Body.getShape().getHeight() / 2)));
	}

	/**
	 * Get the entity's name.
	 * 
	 * @return The name of the entity.
	 */
	public String getName()
	{
		return _Name;
	}

	/**
	 * Set the entity's name.
	 * 
	 * @param name
	 *            The new name of the entity.
	 */
	public void setName(String name)
	{
		_Name = name;
	}

	/**
	 * Set the entity's body.
	 * 
	 * @param body
	 *            The body of the entity.
	 */
	public void setBody(Body body)
	{
		_Body = body;
	}

	/**
	 * Set the manager of all the entity's sprites.
	 * 
	 * @param sprites
	 *            The new sprite manager.
	 */
	public void setSprites(SpriteManager sprites)
	{
		_Sprites = sprites;
	}

	/**
	 * Get the entity's position, ie. all three dimensions.
	 * 
	 * @return The position of the entity.
	 */
	public Vector3 getPosition()
	{
		return _Body.getPosition();
	}

	@Override
	public String toString()
	{
		return _Name;
	}

	/**
	 * Get the scene the entity is part of.
	 * 
	 * @return The scene of the entity.
	 */
	public Scene getScene()
	{
		return _Scene;
	}

	/**
	 * Set the scene the entity is part of.
	 * 
	 * @param scene
	 *            The scene of the entity.
	 */
	public void setScene(Scene scene)
	{
		_Scene = scene;
	}
}