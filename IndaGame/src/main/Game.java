package main;

import infrastructure.GameTimer;
import infrastructure.ScreenManager;
import infrastructure.TimeSpan;
import infrastructure.WindowFrame;
import input.InputManager;
import screens.BackgroundScreen;
import screens.MainMenuScreen;
import debug.DebugManager;

public class Game
{
	// The time per frame in milliseconds. FPS = 25.
	public static final int TimePerFrame = 40;

	// The Window that displays everything.
	public WindowFrame window;

	// The game timer.
	private GameTimer timer;

	// The screen manager.
	private ScreenManager _ScreenManager;

	private boolean exit;

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
		window = new WindowFrame();
		timer = new GameTimer();
		_ScreenManager = new ScreenManager(this);
		exit = false;

		// Initialize.
		initialize();
	}

	public void initialize()
	{
		// Add screens to the screen manager.
		_ScreenManager.addScreen(new BackgroundScreen());
		_ScreenManager.addScreen(new MainMenuScreen());

		// Add this game to the input manager.
		InputManager.getInstance().setGame(this);
	}

	public void loadContent()
	{
		// Load all content.
		_ScreenManager.loadContent();
	}

	public void handleInput()
	{
		// Send the Key and Mouse Events to the Debug Manager.
		DebugManager.getInstance().handleInput(window.inputManager);

		// Calculate the used time by the handle input function.
		DebugManager.getInstance().setPhaseTime(0);
	}

	public void draw()
	{
		// Begin drawing.
		window.drawBegin();

		// Let the screen manager distribute the drawing.
		_ScreenManager.draw(timer);

		// Debug Draw.
		DebugManager.getInstance().draw(window.getBufferGraphics());

		// End drawing.
		window.drawEnd();

		// Calculate the used time by the drawing function.
		DebugManager.getInstance().setPhaseTime(3);
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
			DebugManager.getInstance().setPhaseStartTime();

			// Handle the mouse and keyboard input.
			handleInput();

			// Calculate the used time by the physics update function.
			DebugManager.getInstance().setPhaseTime(1);

			// Calculate the used time by the body update function.
			DebugManager.getInstance().setPhaseTime(2);

			// Draw all Objects.
			draw();

			// Update the screen manager.
			_ScreenManager.update(timer);
			// Update the Window.
			window.update();

			// Calculate the used time at end of the game loop. FIX THE FPS COUNTER!!!
			DebugManager.getInstance().setPhaseEndTime();

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
	 * Whether the game is visible.
	 * 
	 * @return Whether the game is visible.
	 */
	public boolean isVisible()
	{
		return true;
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
	public WindowFrame getWindow()
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