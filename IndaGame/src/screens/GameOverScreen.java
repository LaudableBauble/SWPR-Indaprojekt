package screens;

import java.awt.Graphics2D;

import infrastructure.GameTimer;
import events.EntrySelectEvent;
import events.EntrySelectEventListener;

/**
 * The game over screen comes up over the top of the game, telling the player he has failed.
 */
public class GameOverScreen extends MenuScreen
{
	/**
	 * Constructor for the game over screen.
	 * 
	 * @param win
	 *            Whether the game was won.
	 */
	public GameOverScreen(boolean win)
	{		
		// Call the base constructor.
		super(win ? "Game Won" : "Game Over");

		// Flag that there is no need for the game to transition off when the pause menu is on top of it.
		_IsPopup = true;

		// Create our menu entries.
		MenuEntry quitGameMenuEntry = new MenuEntry("Quit Game");

		// Hook up menu event handlers.
		quitGameMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				quitGameMenuEntrySelected();
			}
		});

		// Add entries to the menu.
		_MenuEntries.add(quitGameMenuEntry);
	}

	/**
	 * Event handler for when the Quit Game menu entry is selected.
	 */
	void quitGameMenuEntrySelected()
	{
		LoadingScreen.load(_ScreenManager, false, new BackgroundScreen(), new MainMenuScreen());
	}

	/**
	 * Draws the pause menu screen. This darkens down the gameplay screen that is underneath us, and then chains to the base MenuScreen.Draw.
	 * 
	 * @param gameTime
	 *            The timer that runs the game.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{
		// Fade the screen ever so slightly.
		_ScreenManager.fadeBackBufferToBlack(getTransitionAlpha() * 2 / 3);

		// Draw the menu.
		super.draw(gameTime, graphics);
	}
}
