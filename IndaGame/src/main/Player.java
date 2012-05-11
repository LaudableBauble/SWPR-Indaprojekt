package main;

import graphics.Frame;
import graphics.Sprite;
import graphics.SpriteManager;
import infrastructure.Enums.DepthDistribution;
import infrastructure.Enums.Visibility;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.omg.CORBA._PolicyStub;

import physics.Body;
import physics.PhysicsSimulator;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A player is an entity that can be controlled by a user.
 */
public class Player extends Entity
{
	// If the player can be controlled by the player.
	private boolean _CanBeControlled;
	// The maximum speed the player can travel willingly in any direction.
	private double _MaxSpeed;
	// The currently active sprite.
	private Sprite _CurrentSprite;
	// The health.
	private float _Health;
	// Whether the player is dead.
	private boolean _IsDead;
	// Whether the player has a key or not.
	private boolean _HasKey;

	/**
	 * Constructor for a player.
	 * 
	 * @param scene
	 *            The scene this player is part of.
	 */
	public Player(Scene scene)
	{
		super(scene);
	}

	/**
	 * Initialize the player.
	 * 
	 * @param scene
	 *            The scene this entity is part of.
	 */
	protected void initialize(Scene scene)
	{
		// Call the base method.
		super.initialize(scene);

		// Initialize the variables.
		_CanBeControlled = true;
		_MaxSpeed = 2;
		_Health = 10;
		_IsDead = false;
		_HasKey = false;
	}

	/**
	 * Load content.
	 */
	public void loadContent()
	{
		// Clear all sprites.
		_Sprites = new SpriteManager();

		// Front.
		Sprite front = _Sprites.addSprite(new Sprite("Front"));
		front.addFrame(new Frame("Character/ZombieGuy1_Front[1].png"));
		front.addFrame(new Frame("Character/ZombieGuy1_Front[2].png"));
		front.addFrame(new Frame("Character/ZombieGuy1_Front[3].png"));

		// Back.
		Sprite back = _Sprites.addSprite(new Sprite("Back"));
		back.addFrame(new Frame("Character/ZombieGuy1_Back[0].png"));
		back.addFrame(new Frame("Character/ZombieGuy1_Back[1].png"));
		back.addFrame(new Frame("Character/ZombieGuy1_Back[2].png"));
		back.addFrame(new Frame("Character/ZombieGuy1_Back[3].png"));

		// Right.
		Sprite right = _Sprites.addSprite(new Sprite("Right"));
		right.addFrame(new Frame("Character/ZombieGuy1_Right[0].png"));
		right.addFrame(new Frame("Character/ZombieGuy1_Right[1].png"));
		right.addFrame(new Frame("Character/ZombieGuy1_Right[2].png"));

		// Left.
		Sprite left = _Sprites.addSprite(new Sprite("Left"));
		left.addFrame(new Frame("Character/ZombieGuy1_Left[0].png"));
		left.addFrame(new Frame("Character/ZombieGuy1_Left[1].png"));
		left.addFrame(new Frame("Character/ZombieGuy1_Left[2].png"));

		// Only make one sprite visible.
		back.setVisibility(Visibility.Invisible);
		right.setVisibility(Visibility.Invisible);
		left.setVisibility(Visibility.Invisible);

		// Current sprite is facing up.
		_CurrentSprite = front;

		// Load all sprites' content.
		_Sprites.loadContent();

		// Set the shape of the body.
		_Body.getShape().setWidth(15);
		_Body.getShape().setHeight(15);
		_Body.getShape().setDepth(_Sprites.getSprite(0).getCurrentFrame().getHeight() / 3);

		// Update the sprites' position offset.
		front.setPositionOffset(new Vector2(0, -front.getCurrentFrame().getOrigin().y + (_Body.getShape().getHeight() / 2)));
		back.setPositionOffset(new Vector2(0, -back.getCurrentFrame().getOrigin().y + (_Body.getShape().getHeight() / 2)));
		right.setPositionOffset(new Vector2(0, -right.getCurrentFrame().getOrigin().y + (_Body.getShape().getHeight() / 2)));
		left.setPositionOffset(new Vector2(0, -left.getCurrentFrame().getOrigin().y + (_Body.getShape().getHeight() / 2)));
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
		// If the space key is pressed.
		if (input.isKeyDown(KeyEvent.VK_SPACE) && Math.abs(_Body.getVelocity().z) < 1)
		{
			// Up in the air.
			_Body.addForce(new Vector3(0, 0, _Body.getAccelerationValue()));
		}
	}

	/**
	 * Update the player.
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
		// If to change scenes.
		changeScene();

		// Check for fall damage, key pickup and game win.
		checkFall();
		pickUpKey();
		checkEndGame();
	}

	/**
	 * If the player has a high downward velocity and a low position, damage him.
	 */
	private void checkFall()
	{
		if (_Body.getVelocity().z < -10 && _Body.getPosition().z < 0)
		{
			reduceHealth(1);
		}
	}

	/**
	 * Change scenes if the player has collided with an exit.
	 */
	private void changeScene()
	{
		// We can only change scene if the player has picked up a key.
		if (!_HasKey) { return; }

		// Create the variable to store the exit.
		Exit exit = null;

		// Check if the player has collided with an exit.
		for (Body body : _Body.getCollisions())
		{
			if (body.getEntity().getClass() == Exit.class)
			{
				exit = (Exit) body.getEntity();
			}
		}

		// If no exit was found, exit.
		if (exit == null) { return; }

		// The exit is not inactive.
		exit.setIsActive(false);

		// Change the position to match the entrance.
		_Body.setPosition(exit.getEntrance());

		// Remove the player from this scene, change scenes and add the player to the goto scene.
		_Scene.removeEntity(this);
		_Scene.getSceneManager().setCurrentScene(exit.getGotoScene());
		_Scene.getSceneManager().getScene(exit.getGotoScene()).addEntity(this);
	}

	/**
	 * Pickup a key if the player has collided with it.
	 */
	private void pickUpKey()
	{
		// Check if the player has collided with an exit.
		for (Body body : _Body.getCollisions())
		{
			if (body.getEntity().getName().equals("Key"))
			{
				_HasKey = true;
				body.getEntity().getSprites().getSprite(0).setVisibility(Visibility.Invisible);
			}
		}
	}

	/**
	 * End the game if the player has collided with an end door.
	 */
	private void checkEndGame()
	{
		// Check if the player has collided with an end door.
		for (Body body : _Body.getCollisions())
		{
			if (body.getEntity().getName().equals("EndDoor") && !_IsDead)
			{
				_Scene.getSceneManager().getScreen().gameOver(true);
				_IsDead = true;
			}
		}
	}

	/**
	 * Change the sprite depending on velocity direction. Makes the player look towards where he is heading.
	 */
	private void changeSprite()
	{
		// If the player is not moving, stop here.
		if (_Body.getVelocity().toVector2().getLength() == 0)
		{
			// If the player stands still there should be no animation.
			_CurrentSprite.setEnableAnimation(false);
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
		if (dir >= 60 && dir <= 120)
		{
			return _Sprites.getSprite(0);
		}
		// If facing up.
		else if (dir >= -120 && dir <= -30)
		{
			return _Sprites.getSprite(1);
		}
		// If facing right.
		else if (dir >= -30 && dir <= 30)
		{
			return _Sprites.getSprite(2);
		}
		// If facing left.
		else if ((dir >= 150 && dir <= 180) || (dir >= -180 && dir <= -120)) { return _Sprites.getSprite(3); }

		// No sprite matched.
		return _CurrentSprite;
	}

	/**
	 * Reduce the player's health with a specified amount.
	 * 
	 * @param amount
	 *            The amount to reduce the health.
	 */
	public void reduceHealth(double amount)
	{
		_Health -= amount;

		// If the health has dropped beneath 0, end the game.
		if (_Health < 0 && !_IsDead)
		{
			_Scene.getSceneManager().getScreen().gameOver(false);
			_IsDead = true;
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