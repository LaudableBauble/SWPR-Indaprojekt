package main;

import graphics.Frame;
import graphics.Sprite;
import graphics.SpriteManager;
import infrastructure.Enums.Visibility;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.event.KeyEvent;

import org.omg.CORBA._PolicyStub;

import physics.Body;
import physics.PhysicsSimulator;
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
		_Body.getShape().setWidth(_Sprites.getSprite(0).getCurrentFrame().getWidth() / 2);
		_Body.getShape().setHeight(_Sprites.getSprite(0).getCurrentFrame().getHeight() / 4);
		_Body.getShape().setDepth(_Sprites.getSprite(0).getCurrentFrame().getHeight() / 4);

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
	}

	/**
	 * Change scenes if the player has collided with an exit.
	 */
	private void changeScene()
	{
		// Create the variable to store the exit.
		Exit exit = null;

		// Check if the player has collided with an exit.
		for (Body body : _Body.getCollisions())
		{
			if (body.getEntity().getClass() == Exit.class)
			{
				exit = (Exit) body.getEntity();
			}
			else if (body.getEntity().getName().equals("Stairs"))
			{
				Vector2 v1 = Vector2.subtract(_Body.getShape().getTopLeft(), _Body.getLayeredPosition());
				Vector2 v2 = Vector2.subtract(body.getShape().getTopLeft(), _Body.getLayeredPosition());

				System.out.println("Player top depth sort: " + _Body.getShape().getDepthSort(v1.x, v1.y));
				System.out.println(body.getEntity() + " top depth sort: " + body.getShape().getDepthSort(v2.x, v2.y));
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
	 * Get the direction of the player in degrees. If standing still, it will always point left.
	 * 
	 * @return The direction of the player.
	 */
	private double getDirection()
	{
		return _Body.getVelocity().toVector2().getAngle() * (180 / Math.PI);
	}
}