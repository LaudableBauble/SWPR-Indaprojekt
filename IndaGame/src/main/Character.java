package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.Timer;

import graphics.Frame;
import graphics.Sprite;
import graphics.SpriteManager;
import infrastructure.Enums.Visibility;
import infrastructure.GameTimer;
import input.InputManager;

//import java.awt.event.KeyEvent;

import physics.Body;
import physics.PhysicsSimulator;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A player is an entity that can be controlled by a user.
 */
public class Character extends Entity
{
	// If the Character can be controlled by the player.
	// private boolean _CanBeControlled;
	// The maximum speed the player can travel willingly in any direction.
	private double _MaxSpeed;
	// The currently active sprite.
	private Sprite _CurrentSprite;
	// Whether the character wants to move.
	private boolean _WantToMove;
	// The filename of the character's main sprite.
	private String _FileName;
	// The number of frames per sprite.
	private int _FrameCount;
	// The elapsed time since last random amble.
	private float _ElapsedTime;
	// The time to wait between moving randomly and the time to walk.
	private float _TimeToWait;
	private float _TimeToWalk;
	// The character's speed.
	private float _Speed;
	// The target entity.
	private Entity _Target;

	/**
	 * Constructor for a character.
	 * 
	 * @param scene
	 *            The scene this Character is part of.
	 */
	public Character(Scene scene, String chara, int nrOfPics)
	{
		super(scene);
		initialize(scene, chara, nrOfPics);
	}

	/**
	 * Initialize the Character.
	 * 
	 * @param physics
	 *            The physics simulator this entity is part of.
	 */
	protected void initialize(Scene scene, String chara, int nrOfPics)
	{
		// Call the base method.
		// super.initialize(physics);

		// Initialize the variables.
		// _CanBeControlled = true;
		_MaxSpeed = 2;
		_FileName = chara;
		_Body.setAccelerationValue(3);
		_FrameCount = nrOfPics;
		_WantToMove = false;
		_ElapsedTime = 0;
		_TimeToWalk = 2;
		_TimeToWait = 4;
		_Speed = 1;

		// Try to find a target.
		updateTarget(false);
	}

	/**
	 * Load content.
	 */
	public void loadContent()
	{
		// Clear all sprites.
		_Sprites = new SpriteManager();

		// Add and load all the different sprites.
		Sprite down = _Sprites.addSprite(new Sprite("Down"));
		Sprite up = _Sprites.addSprite(new Sprite("Up"));
		Sprite right = _Sprites.addSprite(new Sprite("Right"));
		Sprite left = _Sprites.addSprite(new Sprite("Left"));

		// Adding the pictures depending on how many are available.
		for (int x = 1; x <= _FrameCount; x++)
		{
			down.addFrame(new Frame("Character/" + _FileName + "/" + _FileName + "down" + x + ".png"));
			up.addFrame(new Frame("Character/" + _FileName + "/" + _FileName + "up" + x + ".png"));
			right.addFrame(new Frame("Character/" + _FileName + "/" + _FileName + "right" + x + ".png"));
			left.addFrame(new Frame("Character/" + _FileName + "/" + _FileName + "left" + x + ".png"));
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
		_Body.getShape().setDepth(_Sprites.getSprite(0).getCurrentFrame().getHeight() / 3);
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

		// Hunt a target or move randomly.
		if (!huntTarget())
		{
			moveRandomly(gameTime);
		}

		// Determine which sprite should be drawn.
		changeSprite();
	}

	/**
	 * Hunt a target.
	 * 
	 * @return Whether the character was on a hunt or not.
	 */
	private boolean huntTarget()
	{
		// Try to find a target if none has been found.
		updateTarget(false);

		// If no target has been found or if the distance to it is too far, skip here.
		if (_Target == null || Vector3.distance(_Target.getPosition(), getPosition()) > 200) { return false; }
		
		//If the velocity is too high, skip.
		if (_Body.getVelocity().toVector2().getLength() > _MaxSpeed) { return false; }
		
		_Body.addForce(Vector2.multiply(Vector3.subtract(_Target.getPosition(), getPosition()).toVector2().normalize(), _Speed));

		// Check if the character has collided with the player.
		for (Body body : _Body.getCollisions())
		{
			if (body.getEntity() instanceof Player)
			{
				// player = (Player) body.getEntity();
				// player.healthReduce();
			}
		}

		return true;
	}

	/**
	 * Allow the character to move randomly about.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	private void moveRandomly(GameTimer gameTime)
	{
		// Get the time since the last Update.
		_ElapsedTime += (float) gameTime.getElapsedTime().TotalSeconds();

		// Get a random direction and move, but only if there is some time left on the timer.
		if (_ElapsedTime < _TimeToWalk && _Body.getVelocity().toVector2().getLength() < _MaxSpeed)
		{
			_Body.addForce(Vector2.multiply(_Body.getVelocity().toVector2(), _Speed));
		}

		// If it's not time to move in a new direction yet, stop here.
		if (_ElapsedTime < (_TimeToWalk + _TimeToWait)) { return; }

		// Pick a walking direction.
		_Body.addForce(Vector2.multiply(Helper.getRandomDirection(), _Speed));

		// Subtract the time to walk and wait, effectively resetting the timer.
		_ElapsedTime -= _TimeToWalk + _TimeToWait;
		// Generate a new time to walk and wait.
		_TimeToWalk = 1 + (int) (Math.random() * 2);
		_TimeToWait = 1 + (int) (Math.random() * 2);
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
	 * Update the target of the character's hunt.
	 * 
	 * @param force
	 *            Whether to force an update, regardless if there already exists a target.
	 */
	public void updateTarget(boolean force)
	{
		// If we already have a target, skip looking fo one.
		if (force || _Target != null) { return; }

		// Try to find a target among the scene's entities.
		for (Entity entity : _Scene.getEntities())
		{
			if (entity.getClass() == Player.class)
			{
				_Target = entity;
			}
		}
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