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
		//Call the base method.
		super.initialize(physics);
		
		// Initialize the variables.
		_CanBeControlled = true;
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
		if (input.isKeyDown(KeyEvent.VK_LEFT) && (_Body.getVelocity().x > -_Body.getMaxVelocity()))
		{
			// Left.
			_Body.addForce(new Vector2(-_Body.getAccelerationValue(), 0));
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT) && (_Body.getVelocity().x < _Body.getMaxVelocity()))
		{
			// Right.
			_Body.addForce(new Vector2(_Body.getAccelerationValue(), 0));
		}
		if (input.isKeyDown(KeyEvent.VK_UP) && (_Body.getVelocity().y > -_Body.getMaxVelocity()))
		{
			// Up.
			_Body.addForce(new Vector2(0, -_Body.getAccelerationValue()));
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN) && (_Body.getVelocity().y < _Body.getMaxVelocity()))
		{
			// Down.
			_Body.addForce(new Vector2(0, _Body.getAccelerationValue()));
		}
	}
}