package screens;

import infrastructure.GameTimer;

import java.awt.Graphics2D;

import events.EntrySelectEvent;
import events.EntrySelectEventListener;
import events.MessageBoxEventListener;
import events.MessageEvent;

/**
 * The pause menu comes up over the top of the game, giving the player options to resume or quit.
 */
public class PauseMenuScreen extends MenuScreen
{
	/**
	 * Constructor for the pause menu screen.
	 */
	public PauseMenuScreen()
	{
		// Call the base constructor.
		super("Paused");

		// Flag that there is no need for the game to transition
		// off when the pause menu is on top of it.
		_IsPopup = true;

		// Create our menu entries.
		MenuEntry resumeGameMenuEntry = new MenuEntry("Resume Game");
		MenuEntry quitGameMenuEntry = new MenuEntry("Quit Game");

		// Hook up menu event handlers.
		resumeGameMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				onCancel();
			}
		});
		quitGameMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				quitGameMenuEntrySelected();
			}
		});

		// Add entries to the menu.
		_MenuEntries.add(resumeGameMenuEntry);
		_MenuEntries.add(quitGameMenuEntry);
	}

	/**
	 * Event handler for when the Quit Game menu entry is selected.
	 */
	void quitGameMenuEntrySelected()
	{
		// The message to show the player.
		final String message = "Are you sure you want to quit this game?";

		// Create and subscribe to a message box.
		MessageBoxScreen confirmQuitMessageBox = new MessageBoxScreen(message);
		confirmQuitMessageBox.addMessageEventListener(new MessageBoxEventListener()
		{
			public void handleEvent(MessageEvent e)
			{
				if (e.Accepted)
				{
					confirmQuitMessageBoxAccepted();
				}
			}
		});
		_ScreenManager.addScreen(confirmQuitMessageBox);
	}

	/**
	 * Event handler for when the user selects OK on the "are you sure you want to quit" message box. This uses the loading screen to transition from the game back to the main menu screen.
	 */
	void confirmQuitMessageBoxAccepted()
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

		// Draw the pause menu.
		super.draw(gameTime, graphics);
	}
}
