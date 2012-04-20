package screens;

import infrastructure.Camera2D;
import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.ScreenManager;
import infrastructure.TimeSpan;
import input.InputManager;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;

import main.Entity;
import main.EntityDepthComparator;
import main.Player;
import physics.PhysicsSimulator;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;
import debug.DebugManager;

/**
 * This screen implements the actual game logic by reusing components from before.
 */
public class GameplayScreen extends GameScreen
{
	// The GameFont, the main font of the game.
	private Font _GameFont;
	// The PhysicsSimulator.
	private PhysicsSimulator _Physics;
	// The camera.
	private Camera2D _Camera;

	// List of entities.
	private ArrayList<Entity> _Entities;

	// The player.
	private Player _Player;

	// Entities.
	private Entity _Shelf;
	private Entity _Block;

	/**
	 * Constructor for the game screen.
	 * 
	 * @param screenManager
	 *            This screen's manager.
	 */
	public GameplayScreen(ScreenManager screenManager)
	{
		// Set the time it takes for the Screen to transition on and off.
		_TransitionOnTime = TimeSpan.FromSeconds(1.5);
		_TransitionOffTime = TimeSpan.FromSeconds(0.5);

		// Set up the physics simulator and let the debug manager know about it.
		_Physics = new PhysicsSimulator();
		DebugManager.getInstance().setPhysicsSimulator(_Physics);

		// Set up the camera.
		_Camera = new Camera2D(new Rectangle(0, 0, (int) screenManager.getWindowBounds().x, (int) screenManager.getWindowBounds().y), new Rectangle(0, 0, 3000, 3000));
		_Camera.setPosition(new Vector2(1000, 1000));

		// Enable debug.
		DebugManager.getInstance().debug = true;

		// Create the player.
		_Player = new Player(_Physics);
		_Player.getBody().setPosition(new Vector3(1100, 1000, 0));

		// Create the shelf.
		_Shelf = new Entity(_Physics);
		_Shelf.getBody().setPosition(new Vector3(1200, 1000, 0));
		_Shelf.getBody().setIsStatic(true);

		// Create the block.
		_Block = new Entity(_Physics);
		_Block.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Block.getBody().setIsStatic(true);

		// Add the entities to the list.
		_Entities = new ArrayList<>();
		_Entities.add(_Player);
		_Entities.add(_Shelf);
		_Entities.add(_Block);
	}

	/**
	 * Load graphics content for the game.
	 */
	public void loadContent()
	{
		// Set the background color to blue.
		_ScreenManager.getGame().getWindow().setBackBufferColor(Helper.CornFlowerBlue);

		// Load the player's content.
		_Player.loadContent();
		_Shelf.loadContent("Bookshelf[1].png");
		_Block.loadContent("ElevatedBlock[1].png");
		// _Shelf.getSprites().getSprite(0).setRotation(1);
		// _Shelf.getSprites().getSprite(0).setScale(2);

		// Once the load has finished, we use ResetElapsedTime to tell the game's
		// timing mechanism that we have just finished a very long frame, and that
		// it should not try to catch up.
		_ScreenManager.getGame().getGameTimer().resetElapsedTime();
	}

	/**
	 * Lets the game respond to player input. Unlike the Update method, this will only be called when the gameplay screen is active.
	 * 
	 * @param input
	 *            The input manager that relays the state of input.
	 */
	public void handleInput(InputManager input)
	{
		// If the InputState is null, throw an exception.
		// if (input == null) { throw new ArgumentNullException("input"); }

		// If the game should be paused, bring up the pause game screen.
		if (input.getPauseGame())
		{
			_ScreenManager.addScreen(new PauseMenuScreen());
		}
		// Otherwise let the game instance handle input as usual.
		else
		{
			// Let all entities respond to input.
			for (Entity entity : _Entities)
			{
				entity.handleInput(input);
			}

			// If to zoom in.
			if (input.isKeyDown(KeyEvent.VK_O))
			{
				_Camera.zoom(.01f);
			}
			// If to zoom out.
			if (input.isKeyDown(KeyEvent.VK_P))
			{
				_Camera.zoom(-.01f);
			}
			// If to move up.
			if (input.isKeyDown(KeyEvent.VK_I))
			{
				_Camera.move(new Vector2(0, -1));
			}
			// If to move right.
			if (input.isKeyDown(KeyEvent.VK_L))
			{
				_Camera.move(new Vector2(1, 0));
			}
			// If to move down.
			if (input.isKeyDown(KeyEvent.VK_K))
			{
				_Camera.move(new Vector2(0, 1));
			}
			// If to move left.
			if (input.isKeyDown(KeyEvent.VK_J))
			{
				_Camera.move(new Vector2(-1, 0));
			}
		}
	}

	/**
	 * Updates the state of the game. This method checks the GameScreen.IsActive property, so the game will stop updating when the pause menu is active, or if you tab away to a different application.
	 * 
	 * @param gameTime
	 *            The timer of this game.
	 * @param otherScreenHasFocus
	 *            If another screen has focus.
	 * @param coveredByOtherScreen
	 *            If this screen is covered by another screen.
	 */
	public void update(GameTimer gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		// Update the game by updating this screen.
		super.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);

		// Update the physics simulator.
		_Physics.update();

		// Sort the list of entities by descending depth.
		Collections.sort(_Entities, new EntityDepthComparator());

		// Update all entities.
		for (Entity entity : _Entities)
		{
			entity.update(gameTime);
		}

		// Update the camera.
		_Camera.update(gameTime);
		// Share the camera matrix with the debug manager.
		DebugManager.getInstance().setTransformMatrix(_Camera.getTransformMatrix());
	}

	/**
	 * Draws the gameplay screen.
	 * 
	 * @param gameTime
	 *            The timer instance that runs the game.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{
		// If the game is transitioning on or off, fade it out to black.
		if (_TransitionPosition > 0)
		{
			_ScreenManager.fadeBackBufferToBlack(255 - getTransitionAlpha());
		}

		// Save the old graphics matrix and insert the camera matrix in its place.
		AffineTransform old = graphics.getTransform();
		graphics.setTransform(_Camera.getTransformMatrix());

		// Draw all entities.
		for (Entity entity : _Entities)
		{
			entity.draw(graphics);
		}

		// Reinstate the old graphics matrix.
		graphics.setTransform(old);
	}
}
