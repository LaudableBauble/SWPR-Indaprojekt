package screens;

import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.TimeSpan;
import input.InputManager;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import main.Entity;
import main.EntityDepthComparator;
import main.Player;
import physics.PhysicsSimulator;
import auxillary.Helper;
import auxillary.Vector2;

/**
 * This screen implements the actual game logic by reusing components from before.
 */
public class GameplayScreen extends GameScreen
{
	// The GameFont, the main font of the game.
	private Font _GameFont;
	// The PhysicsSimulator.
	private PhysicsSimulator _Physics;

	// List of entities.
	ArrayList<Entity> _Entities;

	// The player.
	private Player _Player;

	// A bookshelf.
	private Entity _Shelf;

	/**
	 * Constructor for the game screen.
	 */
	public GameplayScreen()
	{
		// Set the time it takes for the Screen to transition on and off.
		_TransitionOnTime = TimeSpan.FromSeconds(1.5);
		_TransitionOffTime = TimeSpan.FromSeconds(0.5);

		// Set up the physics simulator.
		_Physics = new PhysicsSimulator();

		// Create the player.
		_Player = new Player(_Physics);
		_Player.getBody().getShape().setPosition(new Vector2(300, 300));

		// Create the shelf.
		_Shelf = new Entity(_Physics);
		_Shelf.getBody().getShape().setPosition(new Vector2(500, 300));
		_Shelf.getBody().setIsStatic(true);

		// Add the entities to the list.
		_Entities = new ArrayList<>();
		_Entities.add(_Player);
		_Entities.add(_Shelf);
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

		// Draw all entities.
		for (Entity entity : _Entities)
		{
			entity.draw(graphics);
		}

		// Draw the physics manager.
		_Physics.draw(graphics);
	}
}
