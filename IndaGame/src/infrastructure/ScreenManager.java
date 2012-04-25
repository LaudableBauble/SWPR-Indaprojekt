package infrastructure;

import infrastructure.GameScreen.ScreenState;
import input.InputManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.Game;
import auxillary.Vector2;

/**
 * The screen manager is a component which manages one or more GameScreen instances. It maintains a stack of screens, calls their Update and Draw methods at the appropriate times, and automatically
 * routes input to the topmost active screen.
 */
public class ScreenManager
{
	// The data fields.
	private Game _Game;
	private WindowFrame _Window;
	private ArrayList<GameScreen> _Screens;
	private ArrayList<GameScreen> _ScreensToUpdate;
	private InputManager _Input;
	private boolean _IsInitialized;
	private Font _TextFont;
	private Font _MenuFont;

	/**
	 * Constructs a new screen manager component.
	 * 
	 * @param game
	 *            The game that this screen manager is part of.
	 */
	public ScreenManager(Game game)
	{
		// Initialize the screen manager.
		initialize(game);
	}

	/**
	 * Initializes the screen manager.
	 */
	public void initialize(Game game)
	{
		// Initialize all fields.
		_Game = game;
		_Window = game.getWindow();
		_Screens = new ArrayList<GameScreen>();
		_ScreensToUpdate = new ArrayList<GameScreen>();
		_Input = InputManager.getInstance();
		_IsInitialized = true;
		_TextFont = new Font("Lucida Sans", Font.PLAIN, 12);
		_MenuFont = new Font("Lucida Sans", Font.PLAIN, 22);
	}

	/**
	 * Load your graphics content.
	 */
	public void loadContent()
	{
		// Load content belonging to the screen manager.
		// _BlankTexture = Helper.loadImage("GameScreen/Textures/blank");

		// Tell each of the screens to load their content.
		for (GameScreen screen : _Screens)
		{
			screen.loadContent();
		}
	}

	/**
	 * Unload your graphics content.
	 */
	protected void unloadContent()
	{
		// Tell each of the screens to unload their content.
		for (GameScreen screen : _Screens)
		{
			screen.unloadContent();
		}
	}

	/**
	 * Allows each screen to run logic.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Make a copy of the master screen list, to avoid confusion if
		// the process of updating one screen adds or removes others.
		_ScreensToUpdate.clear();

		for (GameScreen screen : _Screens)
		{
			_ScreensToUpdate.add(screen);
		}

		boolean otherScreenHasFocus = false;
		boolean coveredByOtherScreen = false;

		// Loop as long as there are screens waiting to be updated.
		while (_ScreensToUpdate.size() > 0)
		{
			// Pop the topmost screen off the waiting list.
			GameScreen screen = _ScreensToUpdate.get(_ScreensToUpdate.size() - 1);
			_ScreensToUpdate.remove(_ScreensToUpdate.size() - 1);

			// Update the screen.
			screen.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);

			if (screen.getScreenState() == ScreenState.TransitionOn || screen.getScreenState() == ScreenState.Active)
			{
				// If this is the first active screen we came across, give it a chance to handle input.
				if (!otherScreenHasFocus)
				{
					screen.handleInput(_Input);

					otherScreenHasFocus = true;
				}

				// If this is an active non-popup, inform any subsequent screens that they are covered by it.
				if (!screen.getIsPopup())
				{
					coveredByOtherScreen = true;
				}
			}
		}
	}

	/**
	 * Tells each screen to draw itself.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime)
	{
		for (GameScreen screen : _Screens)
		{
			if (screen.getScreenState() == ScreenState.Hidden)
			{
				continue;
			}

			// Draw the screen.
			screen.draw(gameTime, _Window.getBufferGraphics());
		}
	}

	/**
	 * Adds a new screen to the screen manager.
	 * 
	 * @param screen
	 *            The screen to add.
	 */
	public void addScreen(GameScreen screen)
	{
		screen.setScreenManager(this);
		screen.setIsExiting(false);

		// If we have a graphics device, tell the screen to load content.
		if (_IsInitialized)
		{
			screen.loadContent();
		}

		_Screens.add(screen);
	}

	/**
	 * Removes a screen from the screen manager. You should normally use GameScreen.ExitScreen instead of calling this directly, so the screen can gradually transition off rather than just being
	 * instantly removed.
	 * 
	 * @param screen
	 *            The screen to remove.
	 */
	public void removeScreen(GameScreen screen)
	{
		// If we have a graphics device, tell the screen to unload content.
		if (_IsInitialized)
		{
			screen.unloadContent();
		}

		_Screens.remove(screen);
		_ScreensToUpdate.remove(screen);
	}

	/**
	 * Expose an array holding all the screens. We return a copy rather than the real master list, because screens should only ever be added or removed using the AddScreen and RemoveScreen methods.
	 * 
	 * @return An array containing all screens.
	 */
	public GameScreen[] getScreens()
	{
		return _Screens.toArray(new GameScreen[_Screens.size()]);
	}

	/**
	 * Helper draws a translucent black fullscreen sprite, used for fading screens in and out, and for darkening the background behind popups.
	 * 
	 * @param alpha
	 *            The alpha value in bytes. 255 = no transparency, 0 = full transparency.
	 */
	public void fadeBackBufferToBlack(int alpha)
	{
		// Try to fade the buffer.
		try
		{
			// Save the current color.
			Color old = _Window.getBufferGraphics().getColor();

			// Set the background color and fill the screen with it. Switch back to the old color.
			_Window.getBufferGraphics().setColor(new Color(0, 0, 0, alpha));
			_Window.getBufferGraphics().fillRect(0, 0, _Window.getWidth(), _Window.getHeight());
			_Window.getBufferGraphics().setColor(old);
		}
		catch (Exception e)
		{
			System.out.println(this + ": Error fading screen. (" + e + ") - Alpha: " + alpha);
		}
	}

	/**
	 * Get the screen manager's text font.
	 * 
	 * @return The text font used by the screen manager.
	 */
	public Font getTextFont()
	{
		return _TextFont;
	}

	/**
	 * Get the screen manager's menu font.
	 * 
	 * @return The menu font used by the screen manager.
	 */
	public Font getMenuFont()
	{
		return _MenuFont;
	}

	/**
	 * Get the screen manager's graphics component.
	 * 
	 * @return The graphics component used by the screen manager.
	 */
	public Graphics2D getGraphics()
	{
		return _Window.getBufferGraphics();
	}

	/**
	 * Get the window's bounds.
	 * 
	 * @return The bounds of the window used by the screen manager.
	 */
	public Vector2 getWindowBounds()
	{
		return new Vector2(_Window.WIDTH, _Window.HEIGHT);
	}

	/**
	 * Get the game that powers all of this.
	 * 
	 * @return The core game component.
	 */
	public Game getGame()
	{
		return _Game;
	}
}
