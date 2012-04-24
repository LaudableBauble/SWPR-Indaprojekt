package main;

import graphics.Frame;
import graphics.Sprite;
import graphics.SpriteManager;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.Graphics2D;

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
	// The Sprite and Body.
	protected SpriteManager _Sprites;
	protected Body _Body;

	/**
	 * Constructor for an entity.
	 * 
	 * @param physics
	 *            The physics simulator this entity is part of.
	 */
	public Entity(PhysicsSimulator physics)
	{
		initialize(physics);
	}

	/**
	 * Initialize the entity.
	 * 
	 * @param physics
	 *            The physics simulator this entity is part of.
	 */
	protected void initialize(PhysicsSimulator physics)
	{
		// Initialize the variables.
		_Sprites = new SpriteManager();
		_Body = new Body(physics);
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
		// Check if the First Mouse Button is down.
		if (input.isMouseButtonDown(1))
		{
			// Check if the Body has been clicked on.
			if ((input.mouseEventPosition().x <= (_Body.getLayeredPosition().x + (_Body.getShape().getWidth() / 2)))
					&& (input.mouseEventPosition().x >= (_Body.getLayeredPosition().x - (_Body.getShape().getWidth() / 2)))
					&& (input.mouseEventPosition().y <= (_Body.getLayeredPosition().y + (_Body.getShape().getHeight() / 2)))
					&& (input.mouseEventPosition().y >= (_Body.getLayeredPosition().y - (_Body.getShape().getHeight() / 2))))
			{
				// Turn the debug isClicked variable on.
				_Body._IsClicked = !(_Body._IsClicked);
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
}