package screens;

import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.ScreenManager;
import infrastructure.TimeSpan;
import infrastructure.WindowFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import auxillary.Helper;

/**
 * The loading screen coordinates transitions between the menu system and the game itself. Normally one screen will transition off at the same time as the next screen is transitioning on, but for
 * larger transitions that can take a longer time to load their data, we want the menu system to be entirely gone before we start loading the game. This is done as follows:
 * 
 * - Tell all the existing screens to transition off. - Activate a loading screen, which will transition on at the same time. - The loading screen watches the state of the previous screens. - When it
 * sees they have finished transitioning off, it activates the real next screen, which may take a long time to load its data. The loading screen will be the only thing displayed while this load is
 * taking place.
 */
public class LoadingScreen extends GameScreen
{
	// The fields.
	protected boolean _LoadingIsSlow;
	protected boolean _OtherScreensAreGone;
	protected GameScreen[] _ScreensToLoad;

	/**
	 * The constructor is private: loading screens should be activated via the static Load method instead.
	 * 
	 * @param screenManager
	 *            The screen manager responsible for this screen.
	 * @param loadingIsSlow
	 *            Whether loading is slow.
	 * @param screensToLoad
	 *            The screens to load.
	 */
	private LoadingScreen(ScreenManager screenManager, boolean loadingIsSlow, GameScreen[] screensToLoad)
	{
		_LoadingIsSlow = loadingIsSlow;
		_ScreensToLoad = screensToLoad;
		_TransitionOnTime = TimeSpan.FromSeconds(0.5);
	}

	/**
	 * Activates the loading screen.
	 * 
	 * @param screenManager
	 *            The screen manager responsible for this screen.
	 * @param loadingIsSlow
	 *            Whether loading is slow.
	 * @param screensToLoad
	 *            The screens to load.
	 */
	public static void load(ScreenManager screenManager, boolean loadingIsSlow, GameScreen... screensToLoad)
	{
		// Tell all current screens to transition off.
		for (GameScreen screen : screenManager.getScreens())
		{
			screen.exitScreen();
		}

		// Create and activate the loading screen.
		LoadingScreen loadingScreen = new LoadingScreen(screenManager, loadingIsSlow, screensToLoad);
		screenManager.addScreen(loadingScreen);
	}

	/**
	 * Updates the loading screen.
	 */
	public void update(GameTimer gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		// Call the base method.
		super.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);

		// If all the previous screens have finished transitioning off, it is time to actually perform the load.
		if (_OtherScreensAreGone)
		{
			_ScreenManager.removeScreen(this);

			for (GameScreen screen : _ScreensToLoad)
			{
				if (screen != null)
				{
					_ScreenManager.addScreen(screen);
				}
			}

			// Once the load has finished, we use ResetElapsedTime to tell
			// the game timing mechanism that we have just finished a very
			// long frame, and that it should not try to catch up.
			_ScreenManager.getGame().getGameTimer().resetElapsedTime();
		}
	}

	/**
	 * Draws the loading screen.
	 * 
	 * @param gameTime
	 *            The timer.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{
		// If we are the only active screen, that means all the previous screens
		// must have finished transitioning off. We check for this in the Draw
		// method, rather than in Update, because it isn't enough just for the
		// screens to be gone: in order for the transition to look good we must
		// have actually drawn a frame without them before we perform the load.
		if ((_ScreenState == ScreenState.Active) && (_ScreenManager.getScreens().length == 1))
		{
			_OtherScreensAreGone = true;
		}

		// The gameplay screen takes a while to load, so we display a loading
		// message while that is going on, but the menus load very quickly, and
		// it would look silly if we flashed this up for just a fraction of a
		// second while returning from the game to the menus. This parameter
		// tells us how long the loading is going to take, so we know whether
		// to bother drawing the message.
		if (_LoadingIsSlow)
		{
			// The font, color and text.
			Font font = _ScreenManager.getMenuFont();
			Color color = new Color(255, 255, 255, getTransitionAlpha());
			final String message = "Loading...";

			// Create a transformation for the text.
			AffineTransform transform = new AffineTransform();
			transform.translate(WindowFrame.WIDTH / 2, WindowFrame.HEIGHT / 2);

			// Draw the text.
			Helper.drawString(message, font, transform, color, graphics);
		}
	}

}
