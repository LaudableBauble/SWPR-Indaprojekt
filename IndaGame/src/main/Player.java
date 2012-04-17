package main;

import input.InputManager;

import java.awt.event.KeyEvent;

import physics.PhysicsSimulator;
import auxillary.Vector2;

/**
 * A player is an entity that can be controlled by a user.
 */
public class Player extends Entity
{
	// If the player can be controlled by the player.
	private boolean _CanBeControlled;
	// The maximum speed the player can travel willingly in any direction.
	private double _MaxSpeed;

	/**
	 * Constructor for a player.
	 * 
	 * @param physics
	 *            The physics simulator this player is part of.
	 */
	public Player(PhysicsSimulator physics)
	{
		super(physics);
	}

	/**
	 * Initialize the player.
	 * 
	 * @param physics
	 *            The physics simulator this entity is part of.
	 */
	protected void initialize(PhysicsSimulator physics)
	{
		// Call the base method.
		super.initialize(physics);

		// Initialize the variables.
		_CanBeControlled = true;
		_MaxSpeed = 2;
	}

	/**
	 * Load content.
	 * 
	 * @param spritePath
	 *            The path of the player's sprite.
	 */
	public void loadContent(String spritePath)
	{
		// Get the Object's version of this method.
		super.loadContent(spritePath);

		// Add and load all the different sprites.
		// Front.
		_Sprite.addFrame("Character/ZombieGuy1_Front[1].png", 0);
		_Sprite.addFrame("Character/ZombieGuy1_Front[2].png", 0);
		_Sprite.addFrame("Character/ZombieGuy1_Front[3].png", 0);
		// Back.
		_Sprite.addSprite("Character/ZombieGuy1_Back[0].png");
		_Sprite.addFrame("Character/ZombieGuy1_Back[1].png", 1);
		_Sprite.addFrame("Character/ZombieGuy1_Back[2].png", 1);
		_Sprite.addFrame("Character/ZombieGuy1_Back[3].png", 1);
		// Right.
		_Sprite.addSprite("Character/ZombieGuy1_Right[0].png");
		_Sprite.addFrame("Character/ZombieGuy1_Right[1].png", 2);
		_Sprite.addFrame("Character/ZombieGuy1_Right[2].png", 2);
		// Left.
		_Sprite.addSprite("Character/ZombieGuy1_Left[0].png");
		_Sprite.addFrame("Character/ZombieGuy1_Left[1].png", 3);
		_Sprite.addFrame("Character/ZombieGuy1_Left[2].png", 3);

		// Change the timePerFrame.
		_Sprite.setTimePerFrame(100);
	}

	/**
	 * Handle input.
	 * 
	 * @param input
	 *            The input manager.
	 */
	public void handleInput(InputManager input)
	{
		// Call the base method.
		super.handleInput(input);

		// If the player can't be controlled, end here.
		if (!_CanBeControlled) { return; }

		// If an arrow key is pressed.
		if (input.isKeyDown(KeyEvent.VK_LEFT) && (_Body.getVelocity().x > -_MaxSpeed))
		{
			// Left.
			_Body.addForce(new Vector2(-_Body.getAccelerationValue(), 0));
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT) && (_Body.getVelocity().x < _MaxSpeed))
		{
			// Right.
			_Body.addForce(new Vector2(_Body.getAccelerationValue(), 0));
		}
		if (input.isKeyDown(KeyEvent.VK_UP) && (_Body.getVelocity().y > -_MaxSpeed))
		{
			// Up.
			_Body.addForce(new Vector2(0, -_Body.getAccelerationValue()));
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN) && (_Body.getVelocity().y < _MaxSpeed))
		{
			// Down.
			_Body.addForce(new Vector2(0, _Body.getAccelerationValue()));
		}
	}

	/**
	 * Update the player.
	 */
	public void update()
	{
		// Call the base method.
		super.update();

		// Determine which sprite should be drawn.
		changeSpriteIndex();

		// If the player stands still there should be no animation.
		_Sprite.setAnimationEnabled((_Body.getVelocity().getLength() < 1) ? false : true);
	}

	/**
	 * Change the index of the sprite depending on velocity direction. Makes the player look towards where he is heading.
	 */
	private void changeSpriteIndex()
	{
		// If the player is not moving, stop here.
		if (_Body.getVelocity().getLength() == 0) { return; }

		// Calculate the player's direction in radians.
		double dir = getDirection();

		// Determine which sprite should be drawn.
		// If facing down.
		if ((dir >= 45) && (dir <= 135))
		{
			if (_Sprite.getSpriteIndex() != 1)
			{
				_Sprite.setSpriteIndex(1);
			}
		}
		// If facing up.
		else if ((dir >= -135) && (dir <= -45))
		{
			if (_Sprite.getSpriteIndex() != 0)
			{
				_Sprite.setSpriteIndex(0);
			}
		}
		// If facing right.
		else if ((dir >= 135) && (dir >= -135))
		{
			if (_Sprite.getSpriteIndex() != 2)
			{
				_Sprite.setSpriteIndex(2);
			}
		}
		// If facing left.
		else if ((dir >= -45) && (dir <= 45))
		{
			if (_Sprite.getSpriteIndex() != 3)
			{
				_Sprite.setSpriteIndex(3);
			}
		}
	}

	/**
	 * Get the direction of the player in radians. If standing still, it will always point left.
	 * 
	 * @return The direction of the player.
	 */
	private double getDirection()
	{
		return Vector2.getAngle(_Body.getVelocity()) * (180 / Math.PI);
	}
}