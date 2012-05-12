package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import auxillary.Helper;
import auxillary.Vector2;

import infrastructure.GameTimer;
import infrastructure.GameScreen.ScreenState;
import events.EntrySelectEvent;
import events.EntrySelectEventListener;

/**
 * The about screen comes up over the top of the game, giving the player some information.
 */
public class AboutMenuScreen extends MenuScreen
{
	// Fields.
	private String _Text;
	private Vector2 _TextPosition;

	/** Constructor for the help screen. */
	public AboutMenuScreen()
	{
		// Call the base constructor.
		super("About");

		// Set the text.
		_Text = "The player can be moved by the arrow keys and made to jump by pressing Space. The debug manager can be toggled by pressing F1."
				+ "The map editor is not complete and lacks an array of important features. This is more of a demo showcasing the engine than an actual game.";

		// Set the position of the text.
		_TextPosition = new Vector2(150, 100);

		// Create our menu entries.
		MenuEntry returnMenuEntry = new MenuEntry("Return");

		// Hook up menu event handlers.
		returnMenuEntry.addEntrySelectEventListener(new EntrySelectEventListener()
		{
			public void handleEvent(EntrySelectEvent e)
			{
				onCancel();
			}
		});

		// Add entries to the menu.
		_MenuEntries.add(returnMenuEntry);
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
		// Get the font.
		Font font = _ScreenManager.getMenuFont();
		Font textFont = _ScreenManager.getTextFont();

		// The position of the menu and text.
		Vector2 position = new Vector2(_MenuPosition.x, _MenuPosition.y);
		Vector2 textPosition = new Vector2(_TextPosition.x, _TextPosition.y);

		// Make the menu slide into place during transitions, using a
		// power curve to make things look more interesting (this makes
		// the movement slow down as it nears the end).
		float transitionOffset = (float) Math.pow(_TransitionPosition, 2);

		if (_ScreenState == ScreenState.TransitionOn)
		{
			position.x -= transitionOffset * 512;
			textPosition.x += transitionOffset * 512;
		}
		else
		{
			position.x += transitionOffset * 1024;
			textPosition.x -= transitionOffset * 1024;
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
		Vector2 titlePosition = new Vector2(_TitlePosition.x, _TitlePosition.y);
		Vector2 titleOrigin = Vector2.divide(new Vector2(font.getStringBounds(_MenuTitle, graphics.getFontRenderContext())), 2);
		Color titleColor = new Color(62, 69, 71, getTransitionAlpha());
		float titleScale = 1f;

		titlePosition.y -= transitionOffset * 100;

		// Create a transformation for the text.
		AffineTransform transform = new AffineTransform();
		transform.scale(titleScale, titleScale);
		transform.translate(titlePosition.x - titleOrigin.x, titlePosition.y - titleOrigin.y);

		// Draw the text.
		Helper.drawString(_MenuTitle, font, transform, titleColor, graphics);

		// The about text.
		Color textColor = new Color(250, 250, 250, getTransitionAlpha());
		float textScale = 1.5f;

		// The loop data.
		int index = 0;
		int rowStart = 0;
		boolean done = false;

		while (!done)
		{
			// When to make a new row.
			if (textFont.getStringBounds(_Text.substring(rowStart, index), graphics.getFontRenderContext()).getWidth() > 300)
			{
				// Get the start of the next row.
				index = _Text.substring(index).indexOf(" ") + 1 + index;
				if (_Text.substring(index).indexOf(" ") == -1)
				{
					index = _Text.length();
				}

				// Position the text correctly.
				textPosition.y += 15;
				Vector2 textOrigin = Vector2.divide(new Vector2(textFont.getStringBounds(_Text.substring(rowStart, index), graphics.getFontRenderContext())), 2);

				// Create a transformation for the text.
				transform = new AffineTransform();
				transform.scale(textScale, textScale);
				transform.translate(textPosition.x - textOrigin.x, textPosition.y - textOrigin.y);

				// Draw the text.
				Helper.drawString(_Text.substring(rowStart, index), textFont, transform, textColor, graphics);

				// Increment the row.
				rowStart = index;
			}
			else
			{
				index++;

				if (index > _Text.length())
				{
					done = true;
				}
			}

		}
	}
}
