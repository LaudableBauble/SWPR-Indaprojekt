package screens;

import infrastructure.GameTimer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.event.EventListenerList;

import auxillary.Helper;
import auxillary.Vector2;
import events.EntrySelectEvent;
import events.EntrySelectEventListener;

/**
 * Helper class represents a single entry in a MenuScreen. By default this just draws the entry text string, but it can be customized to display menu entries in different ways. This also provides an
 * event that will be raised when the menu entry is selected.
 */
public class MenuEntry
{
	/**
	 * The text rendered for this entry.
	 */
	private String _Text;

	/**
	 * Tracks a fading selection effect on the entry. The entries transition out of the selection effect when they are deselected.
	 */
	private float _SelectionFade;

	private EventListenerList _EventListeners;

	/**
	 * Constructs a new menu entry with the specified text.
	 * 
	 * @param text
	 *            The text rendered for this entry.
	 */
	public MenuEntry(String text)
	{
		// Initialize some fields.
		_Text = text;
		_EventListeners = new EventListenerList();
	}

	/**
	 * Updates the menu entry.
	 */
	public void update(MenuScreen screen, boolean isSelected, GameTimer gameTime)
	{
		// When the menu selection changes, entries gradually fade between
		// their selected and deselected appearance, rather than instantly
		// popping to the new state.
		float fadeSpeed = (float) gameTime.getElapsedTime().TotalSeconds() * 4;

		if (isSelected)
		{
			_SelectionFade = Math.min(_SelectionFade + fadeSpeed, 1);
		}
		else
		{
			_SelectionFade = Math.max(_SelectionFade - fadeSpeed, 0);
		}
	}

	/**
	 * Draws the menu entry. This can be overridden to customize the appearance.
	 */
	public void draw(MenuScreen screen, Vector2 position, boolean isSelected, GameTimer gameTime, Graphics2D graphics)
	{
		// Draw the selected entry in yellow, otherwise white.
		Color color = isSelected ? Color.YELLOW : Color.WHITE;

		// Pulsate the size of the selected menu entry.
		double time = gameTime.getTotalElapsedTime().TotalSeconds();
		float pulsate = (float) Math.sin(time * 6) + 1;
		float scale = 1 + pulsate * 0.05f * _SelectionFade;

		// Modify the alpha to fade text out during transitions.
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), screen.getTransitionAlpha());
		Font font = screen.getScreenManager().getMenuFont();

		// Draw text, centered on the middle of each line.
		Rectangle2D rect = font.getStringBounds(_Text, graphics.getFontRenderContext());
		Vector2 origin = Vector2.multiply(new Vector2(rect.getWidth(), rect.getHeight()), 2);
		origin.x = 0;

		// Create a transformation for the text.
		AffineTransform transform = new AffineTransform();
		transform.translate(position.x - origin.x, position.y - origin.y);
		transform.scale(scale, scale);

		// Draw the text.
		Helper.drawString(_Text, font, transform, color, graphics);
	}

	/**
	 * This methods allows classes to register for entry select events.
	 * 
	 * @param listener
	 *            The listener class.
	 */
	public void addEntrySelectEventListener(EntrySelectEventListener listener)
	{
		_EventListeners.add(EntrySelectEventListener.class, listener);
	}

	/**
	 * This methods allows classes to unregister for entry select events.
	 * 
	 * @param listener
	 *            The listener class.
	 */
	public void removeEntrySelectEventListener(EntrySelectEventListener listener)
	{
		_EventListeners.remove(EntrySelectEventListener.class, listener);
	}

	/**
	 * Method for raising the selected event.
	 */
	protected void selectEntryInvoke()
	{
		// For all listeners, enlighten them.
		for (EntrySelectEventListener listener : _EventListeners.getListeners(EntrySelectEventListener.class))
		{
			listener.handleEvent(new EntrySelectEvent(this));
		}
	}

	/**
	 * Queries how much space this menu entry requires.
	 * 
	 * @param screen
	 *            The screen in which this entry resides.
	 * @return The height of the menu entry.
	 */
	public int getHeight(MenuScreen screen)
	{
		return (int) screen.getScreenManager().getMenuFont().getStringBounds(_Text, screen.getScreenManager().getGraphics().getFontRenderContext()).getHeight();
	}

	/**
	 * Gets the text of this menu entry.
	 * 
	 * @return The text of the menu entry.
	 */
	public String getText()
	{
		return _Text;
	}
}
