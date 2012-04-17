package screens;

import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.TimeSpan;
import input.InputManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import auxillary.Helper;
import auxillary.Vector2;
import events.EntrySelectEvent;

/**
 * Base class for screens that contain a menu of options. The user can move up and down to select an entry, or cancel to back out of the screen.
 */
public abstract class MenuScreen extends GameScreen
{
	protected ArrayList<MenuEntry> _MenuEntries;
	protected int _SelectedEntry;
	protected String _MenuTitle;

	/**
	 * Constructor for the menu screen.
	 * 
	 * @param menuTitle
	 *            The title of the menu.
	 */
	public MenuScreen(String menuTitle)
	{
		// Initialize the fields.
		_MenuTitle = menuTitle;
		_TransitionOnTime = TimeSpan.FromSeconds(1);
		_TransitionOffTime = TimeSpan.FromSeconds(1);
		_SelectedEntry = 0;
		_MenuEntries = new ArrayList<MenuEntry>();
	}

	/**
	 * Responds to user input, changing the selected entry and accepting or cancelling the menu.
	 */
	public void handleInput(InputManager input)
	{
		// Move to the previous menu entry?
		if (input.getMenuUp())
		{
			_SelectedEntry--;

			if (_SelectedEntry < 0)
			{
				_SelectedEntry = _MenuEntries.size() - 1;
			}
		}

		// Move to the next menu entry?
		if (input.getMenuDown())
		{
			_SelectedEntry++;

			if (_SelectedEntry >= _MenuEntries.size())
			{
				_SelectedEntry = 0;
			}
		}

		// Accept or cancel the menu?
		if (input.getMenuSelect())
		{
			onSelectEntry(_SelectedEntry);
		}
		else if (input.getMenuCancel())
		{
			onCancel();
		}
	}

	/**
	 * Handler for when the user has chosen a menu entry.
	 */
	protected void onSelectEntry(int entryIndex)
	{
		_MenuEntries.get(_SelectedEntry).selectEntryInvoke();
	}

	/**
	 * Handler for when the user has cancelled the menu.
	 */
	protected void onCancel()
	{
		exitScreen();
	}

	/**
	 * Helper overload makes it easy to use OnCancel as a MenuEntry event handler.
	 * 
	 * @param sender
	 * @param e
	 */
	protected void onCancel(Object sender, EntrySelectEvent e)
	{
		onCancel();
	}

	/**
	 * Updates the menu.
	 */
	public void update(GameTimer gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		// Call the base method.
		super.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);

		// Update each nested MenuEntry object.
		for (int i = 0; i < _MenuEntries.size(); i++)
		{
			boolean isSelected = getIsActive() && (i == _SelectedEntry);

			_MenuEntries.get(i).update(this, isSelected, gameTime);
		}
	}

	/**
	 * Draws the menu.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{
		// Get the font.
		Font font = _ScreenManager.getMenuFont();
		// The position of the menu.
		Vector2 position = new Vector2(100, 300);

		// Make the menu slide into place during transitions, using a
		// power curve to make things look more interesting (this makes
		// the movement slow down as it nears the end).
		float transitionOffset = (float) Math.pow(_TransitionPosition, 2);

		if (_ScreenState == ScreenState.TransitionOn)
		{
			position.x -= transitionOffset * 512;
		}
		else
		{
			position.x += transitionOffset * 1024;
		}

		// Draw each menu entry in turn.
		for (int i = 0; i < _MenuEntries.size(); i++)
		{
			MenuEntry menuEntry = _MenuEntries.get(i);
			boolean isSelected = getIsActive() && (i == _SelectedEntry);
			menuEntry.draw(this, position, isSelected, gameTime, graphics);

			position.y += menuEntry.getHeight(this);
		}

		// Draw the menu title.
		Vector2 titlePosition = new Vector2(426, 80);
		Vector2 titleOrigin = Vector2.divide(new Vector2(font.getStringBounds(_MenuTitle, graphics.getFontRenderContext())), 2);
		Color titleColor = new Color(62, 69, 71, getTransitionAlpha());
		float titleScale = 1.5f;

		titlePosition.y -= transitionOffset * 100;

		// Create a transformation for the text.
		AffineTransform transform = new AffineTransform();
		transform.scale(titleScale, titleScale);
		transform.translate(titlePosition.x - titleOrigin.x, titlePosition.y - titleOrigin.y);

		// Draw the text.
		Helper.drawString(_MenuTitle, font, transform, titleColor, graphics);
	}

	/**
	 * Gets the list of menu entries, so derived classes can add or change the menu contents.
	 */
	protected ArrayList<MenuEntry> getMenuEntries()
	{
		return _MenuEntries;
	}
}
