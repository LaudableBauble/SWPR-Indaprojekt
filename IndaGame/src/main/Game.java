package main;

import infrastructure.GameTimer;
import infrastructure.ScreenManager;
import infrastructure.TimeSpan;
import infrastructure.WindowPanel;

import java.awt.Canvas;

import screens.BackgroundScreen;
import screens.MainMenuScreen;

public class Game extends Canvas
{
	// The time per frame in milliseconds. FPS = 25.
	public static final int TimePerFrame = 40;

	// The Window that displays everything.
	public WindowPanel window;

	// The game timer.
	private GameTimer timer;

	// The screen manager.
	private ScreenManager _ScreenManager;

	private boolean exit;

	// The PhysicsSimulator.
	// public PhysicsSimulator physics;

	// The DebugManager.
	// public DebugManager debug;

	// The background.
	// public Sprite background;

	// Object creation.
	// public Player player;

	/**
	 * The static main method of the game. This is where the game starts.
	 * 
	 * @param args
	 *            The arguments supplied to the game.
	 */
	public static void main(String[] args)
	{
		// Create the game.
		Game game = new Game();
		// Load the game content.
		game.loadContent();
		// Start the Game Update Loop.
		game.updateLoop();
	}

	public Game()
	{
		// The window panel.
		window = new WindowPanel();
		timer = new GameTimer();
		_ScreenManager = new ScreenManager(this);
		exit = false;
		// physics = new PhysicsSimulator();
		// debug = new DebugManager();

		// The background.
		// background = new Sprite();

		// Initialize the PhysicsSimulator.
		// physics.initialize();
		// Enable debug.
		// debug.debug = true;

		// Initialize the Player and Sentry.
		// player.initialize(physics);

		// Initialize the zombies.
		// for (int i = 0; i < numberOfZombies; i++) { dummy.get(i).initialize(physics); }

		// Initialize.
		initialize();
	}

	public void initialize()
	{
		// Add screens to the screen manager.
		_ScreenManager.addScreen(new BackgroundScreen());
		_ScreenManager.addScreen(new MainMenuScreen());
		// Make the Player controllable by the player.
		// player.canBeControlled = true;
	}

	public void loadContent()
	{
		// Load all content.
		_ScreenManager.loadContent();
		// Load the Sprite.
		// background.loadContent("Grass[0].jpg");
		// player.loadContent("Monster[1].gif");

		// Load the zombies' content.
		// for (int i = 0; i < numberOfZombies; i++) { dummy.get(i).loadContent("ZombieGuy1_Front[0].png"); }
	}

	public void handleInput()
	{
		// Let the screen manager handle input.
		// _ScreenManager.handleInput(window.inputManager);
		// Send the Key and Mouse Events to the Physics Simulator.
		// physics.handleInput(window.inputManager);
		// Send the Key and Mouse Events to the Debug Manager.
		// debug.handleInput(window.inputManager, physics);

		// Send the Key and Mouse Events to the Objects.
		// player.handleInput(window.inputManager);

		// Handle the input for the zombies.
		// for (int i = 0; i < numberOfZombies; i++) { dummy.get(i).handleInput(window.inputManager); }

		// Calculate the used time by the handle input function.
		// debug.setPhaseTime(0);
	}

	public void draw()
	{
		// Begin drawing.
		window.drawBegin();

		// Let the screen manager distribute the drawing.
		_ScreenManager.draw(timer);

		// Draw the Background.
		// background.draw(window.getGraphics(), new Vector(0,0), this);
		// Draw our monster.
		// player.draw(window.getGraphics(), this);

		// Draw the zombies.
		// sentry.draw(window.getGraphics(), this);
		// for (int i = 0; i < numberOfZombies; i++) { dummy.get(i).draw(window.getGraphics(), this); }

		// Draw the PhysicsSimulator (If there's something to draw).
		// physics.draw(window.getGraphics());

		// Debug Draw.
		// debug.draw(window.getGraphics(), physics);

		// End drawing.
		window.drawEnd();

		// Calculate the used time by the drawing function.
		// debug.setPhaseTime(3);
	}

	public void updateLoop()
	{
		// Start the timer.
		timer.start();

		// Loop as long as the window is visible.
		while (isVisible() && !exit)
		{
			// Update the timer.
			timer.update();

			// Calculate the startup time.
			// debug.setPhaseStartTime();

			// Handle the mouse and keyboard input.
			handleInput();

			// Update the PhysicsSimulator.
			// physics.update();

			// Calculate the used time by the physics update function.
			// debug.setPhaseTime(1);

			// Update all Objects before they're drawn.
			// player.update();

			// Calculate the used time by the body update function.
			// debug.setPhaseTime(2);

			// Draw all Objects.
			draw();

			// Update the screen manager.
			_ScreenManager.update(timer);
			// Update the Window.
			window.update();

			// Calculate the used time at end of the game loop. FIX THE FPS COUNTER!!!
			// debug.setPhaseEndTime();

			// Update the Debug Manager.
			// debug.update();

			// Sleep some time.
			try
			{
				// The time left.
				int sleep = TimeSpan.FromMilliseconds(TimePerFrame).Subtract(timer.getElapsedTime()).Milliseconds();
				// If there is time left, sleep.
				if (sleep > 0)
				{
					Thread.sleep(sleep);
				}
			}
			// Catch the exceptions.
			catch (InterruptedException e)
			{
				System.out.println(this + ": Interrupted Error. (" + e + ")");
			}
		}

		// If to exit, exit.
		if (exit)
		{
			// Turn off the program.
			System.exit(0);
		}

		// Stop the timer.
		timer.stop();
	}

	/**
	 * Exit the game.
	 */
	public void exit()
	{
		exit = true;
	}

	/**
	 * Get the game's window.
	 * 
	 * @return The window that displays the game.
	 */
	public WindowPanel getWindow()
	{
		return window;
	}
	
	/**
	 * Get the game's timer.
	 * 
	 * @return The timer that the game uses.
	 */
	public GameTimer getGameTimer()
	{
		return timer;
	}
}