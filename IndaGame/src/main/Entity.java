package main;

import graphics.Sprite;
import input.InputManager;

import java.awt.Graphics2D;

import physics.Body;
import physics.PhysicsSimulator;
import auxillary.Helper;

/**
 * An entity, sporting a body and a sprite, is the most basic building blocks of the physical game world.
 */
public class Entity
{
	// The Sprite and Body.
	protected Sprite _Sprite;
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
		_Sprite = new Sprite();
		_Body = new Body(physics);
		_Body.addBody();
	}

	/**
	 * Load content.
	 * 
	 * @param spritePath
	 *            The path of the main sprite.
	 */
	public void loadContent(String spritePath)
	{
		// Load the sprite.
		_Sprite.loadContent(spritePath);
		// Set the shape of the body.
		_Body.getShape().setWidth(_Sprite.getWidth(spritePath, 0));
		_Body.getShape().setHeight(_Sprite.getHeight(spritePath, 0));
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
			if ((input.mouseEventPosition().x <= (_Body.getPosition().x + (_Body.getShape().getWidth() / 2)))
					&& (input.mouseEventPosition().x >= (_Body.getPosition().x - (_Body.getShape().getWidth() / 2)))
					&& (input.mouseEventPosition().y <= (_Body.getPosition().y + (_Body.getShape().getHeight() / 2)))
					&& (input.mouseEventPosition().y >= (_Body.getPosition().y - (_Body.getShape().getHeight() / 2))))
			{
				// Turn the debug isClicked variable on.
				_Body._IsClicked = !(_Body._IsClicked);
			}
		}
	}

	/**
	 * Update the entity.
	 */
	public void update()
	{
		// Update the body and sprite.
		_Body.update();
		_Sprite.update();
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
		_Sprite.draw(graphics, Helper.toTopLeft(_Body.getPosition(), _Body.getShape().getWidth(), _Body.getShape().getHeight()));
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
}