package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.Timer;

import graphics.Frame;
import graphics.Sprite;
import infrastructure.Enums.Visibility;
import infrastructure.GameTimer;
import input.InputManager;

//import java.awt.event.KeyEvent;

import physics.PhysicsSimulator;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A player is an entity that can be controlled by a user.
 */
public class Character extends Entity implements ActionListener
{
	// If the Character can be controlled by the player.
	// private boolean _CanBeControlled;
	// The maximum speed the player can travel willingly in any direction.
	private double _MaxSpeed;
	// The currently active sprite.
	private Sprite _CurrentSprite;
	// filename of char
	private String character;
	private int nrPics;
	Timer time = new Timer(500, this);

	/**
	 * Constructor for a Character.
	 * 
	 * @param physics
	 *            The physics simulator this Character is part of.
	 */
	public Character(PhysicsSimulator physics, String chara, int nrOfPics)
	{
		super(physics);
		initialize(physics, chara, nrOfPics);
	}

	/**
	 * Initialize the Character.
	 * 
	 * @param physics
	 *            The physics simulator this entity is part of.
	 */
	protected void initialize(PhysicsSimulator physics, String chara, int nrOfPics)
	{
		// Call the base method.
		// super.initialize(physics);

		// Initialize the variables.
		// _CanBeControlled = true;
		_MaxSpeed = 2;
		character = chara;
		_Body.setAccelerationValue(3);
		nrPics = nrOfPics;
		time.start();
	}

	/**
	 * Load content.
	 */
	public void loadContent()
	{
		// Add and load all the different sprites.
		Sprite down = _Sprites.addSprite(new Sprite("Down"));
		Sprite up = _Sprites.addSprite(new Sprite("Up"));
		Sprite right = _Sprites.addSprite(new Sprite("Right"));
		Sprite left = _Sprites.addSprite(new Sprite("Left"));

		// Adding the pictures depending on how many are available.
		for (int x = 1; x <= nrPics; x++)
		{
			down.addFrame(new Frame("Character/" + character + "/" + character + "down" + x + ".png"));
			up.addFrame(new Frame("Character/" + character + "/" + character + "up" + x + ".png"));
			right.addFrame(new Frame("Character/" + character + "/" + character + "right" + x + ".png"));
			left.addFrame(new Frame("Character/" + character + "/" + character + "left" + x + ".png"));
		}

		// Only make one sprite visible.
		up.setVisibility(Visibility.Invisible);
		right.setVisibility(Visibility.Invisible);
		left.setVisibility(Visibility.Invisible);

		// Current sprite is facing down.
		_CurrentSprite = down;

		// Load all sprites' content.
		_Sprites.loadContent();

		// Set the shape of the body.
		_Body.getShape().setWidth(_Sprites.getSprite(0).getCurrentFrame().getWidth());
		_Body.getShape().setHeight(_Sprites.getSprite(0).getCurrentFrame().getHeight() / 2);

		// Set the depth.
		_Body.getShape().setDepth(5);
	}

	/**
	 * Handle input.
	 * 
	 * @param input
	 *            The input manager.
	 */
	public void actionPerformed(ActionEvent e)
	{
		boolean move = false;
		int acc = 4;
		Random rand = new Random();
		Object source = e.getSource();

		// // If the Character can't be controlled, end here.
		// if (!_CanBeControlled) { return; }
		// if(true){
		if (source == time)
		{
			// Whether to move or not.
			move = !move;

			// Get the the random direction.
			int direction = rand.nextInt(4);

			if (move && direction == 0)
			{
				_Body.addForce(new Vector2(-acc, 0));
			}
			if (move && direction == 1)
			{
				_Body.addForce(new Vector2(acc, 0));
			}
			if (move && direction == 2)
			{
				_Body.addForce(new Vector2(0, -acc));
			}
			if (move && direction == 3)
			{
				_Body.addForce(new Vector2(0, acc));
			}
		}
		// }
		// If an arrow key is pressed.
		// if (input.isKeyDown(KeyEvent.VK_LEFT) && (_Body.getVelocity().x > -_MaxSpeed))
		// {
		// // Left.
		// _Body.addForce(new Vector2(-_Body.getAccelerationValue(), 0));
		// }
		// if (input.isKeyDown(KeyEvent.VK_RIGHT) && (_Body.getVelocity().x < _MaxSpeed))
		// {
		// // Right.
		// _Body.addForce(new Vector2(_Body.getAccelerationValue(), 0));
		// }
		// if (input.isKeyDown(KeyEvent.VK_UP) && (_Body.getVelocity().y > -_MaxSpeed))
		// {
		// // Up.
		// _Body.addForce(new Vector2(0, -_Body.getAccelerationValue()));
		// }
		// if (input.isKeyDown(KeyEvent.VK_DOWN) && (_Body.getVelocity().y < _MaxSpeed))
		// {
		// // Down.
		// _Body.addForce(new Vector2(0, _Body.getAccelerationValue()));
		// }
	}

	/**
	 * Update the Character.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Call the base method.
		super.update(gameTime);

		// Determine which sprite should be drawn.
		changeSprite();
	}

	/**
	 * Change the sprite depending on velocity direction. Makes the player look towards where he is heading.
	 */
	private void changeSprite()
	{
		// If the player is not moving, stop here.
		if (_Body.getVelocity().getLength() == 0)
		{
			// If the player stands still there should be no animation.
			_CurrentSprite.setEnableAnimation((_Body.getVelocity().getLength() == 0) ? false : true);
			return;
		}

		// Determine which sprite should be drawn.
		_CurrentSprite = getCurrentSprite(getDirection());

		// Make the sprite visible and enable its animation.
		_CurrentSprite.setVisibility(Visibility.Visible);
		_CurrentSprite.setEnableAnimation(true);

		// For all other sprites, make them invisible and disable their animation.
		for (Sprite s : _Sprites.getSprites())
		{
			if (_CurrentSprite != s)
			{
				s.setVisibility(Visibility.Invisible);
				s.setEnableAnimation(false);
			}
		}
	}

	/**
	 * Get the current sprite based on player velocity direction.
	 * 
	 * @param dir
	 *            The direction of the player.
	 * @return The current sprite.
	 */
	private Sprite getCurrentSprite(double dir)
	{
		// If facing down.
		if (dir >= 45 && dir <= 135)
		{
			return _Sprites.getSprite(0);
		}
		// If facing up.
		else if (dir >= -135 && dir <= -45)
		{
			return _Sprites.getSprite(1);
		}
		// If facing right.
		else if (dir >= -45 && dir <= 45)
		{
			return _Sprites.getSprite(2);
		}
		// If facing left.
		else if ((dir >= 135 && dir <= 180) || (dir >= -180 && dir <= -135)) { return _Sprites.getSprite(3); }

		// No sprite matched.
		return null;
	}

	/**
	 * Get the direction of the player in degrees. If standing still, it will always point left.
	 * 
	 * @return The direction of the player.
	 */
	private double getDirection()
	{
		return _Body.getVelocity().toVector2().getAngle() * (180 / Math.PI);
	}
}