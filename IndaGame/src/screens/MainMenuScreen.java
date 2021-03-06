package screens;

import events.EntrySelectEvent;
import events.EntrySelectEventListener;
import events.MessageBoxEventListener;
import events.MessageEvent;

/**
 * The main menu screen is the first thing displayed when the game starts up.
 */
public class MainMenuScreen extends MenuScreen
{
	/**
	 * Constructor fills in the menu contents.
	 */
	public MainMenuScreen()
	{
		// Call the base constructor.
		super("Main Menu");

		// Create our menu entries.
		MenuEntry playGameMenuEntry = new MenuEntry("Play Game");
		MenuEntry mapEditorMenuEntry = new MenuEntry("Map Editor");
		MenuEntry aboutMenuEntry = new MenuEntry("About");
		MenuEntry exitMenuEntry = new MenuEntry("Exit");

		// Hook up menu event handlers.
		playGameMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				playGameMenuEntrySelected();
			}
		});
		// Hook up menu event handlers.
		mapEditorMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				mapEditorMenuEntrySelected();
			}
		});
		aboutMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				aboutMenuEntrySelected();
			}
		});
		exitMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				onCancel();
			}
		});

		// Add entries to the menu.
		_MenuEntries.add(playGameMenuEntry);
		_MenuEntries.add(mapEditorMenuEntry);
		_MenuEntries.add(aboutMenuEntry);
		_MenuEntries.add(exitMenuEntry);
	}

	/**
	 * Event handler for when the Play Game menu entry is selected.
	 */
	void playGameMenuEntrySelected()
	{
		LoadingScreen.load(_ScreenManager, true, new GameplayScreen(_ScreenManager));
	}

	/**
	 * Event handler for when the Map Editor menu entry is selected.
	 */
	void mapEditorMenuEntrySelected()
	{
		LoadingScreen.load(_ScreenManager, true, new MapEditorScreen(_ScreenManager));
	}

	/**
	 * Event handler for when the Options menu entry is selected.
	 * 
	 * @param sender
	 * @param e
	 */
	void aboutMenuEntrySelected()
	{
		_ScreenManager.addScreen(new AboutMenuScreen());
	}

	/**
	 * When the user cancels the main menu, ask if they want to exit the game.
	 */
	protected void onCancel()
	{
		// The warning message.
		final String message = "Are you sure you want to exit?";

		// Create and subscribe to a message box.
		MessageBoxScreen confirmExitMessageBox = new MessageBoxScreen(message);
		confirmExitMessageBox.addMessageEventListener(new MessageBoxEventListener()
		{
			public void handleEvent(MessageEvent e)
			{
				if (e.Accepted)
				{
					confirmExitMessageBoxAccepted();
				}
			}
		});
		_ScreenManager.addScreen(confirmExitMessageBox);
	}

	/**
	 * Event handler for when the user selects OK on the "are you sure you want to exit" message box.
	 */
	private void confirmExitMessageBoxAccepted()
	{
		_ScreenManager.getGame().exit();
	}

}
