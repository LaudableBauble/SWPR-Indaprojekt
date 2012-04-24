package screens;

import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.TimeSpan;
import input.InputManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.event.EventListenerList;

import auxillary.Helper;
import auxillary.Vector2;
import events.MessageBoxEventListener;
import events.MessageEvent;

/**
 * A popup message box screen, used to display "are you sure?" confirmation messages.
 */
public class MessageBoxScreen extends GameScreen
{
	private String _Message;
	BufferedImage _Texture;
	private EventListenerList _EventListeners;

	/**
	 * Constructor automatically includes the standard "Enter=OK, Esc=cancel" usage text prompt.
	 * 
	 * @param message
	 *            The message to display.
	 */
	public MessageBoxScreen(String message)
	{
		this(message, true);
	}

	/**
	 * Constructor lets the caller specify whether to include the standard "Enter=OK, Esc=cancel" usage text prompt.
	 * 
	 * @param message
	 *            The message to display.
	 * @param includeUsageText
	 *            Whether to include the standard usage text or not.
	 */
	public MessageBoxScreen(String message, boolean includeUsageText)
	{
		// The usage text to include.
		final String usageText = "\nSpace, Enter = OK" + "\nEsc = cancel";

		// The final message to display.
		if (includeUsageText)
		{
			_Message = message + usageText;
		}
		else
		{
			_Message = message;
		}

		// Initialize the screen.
		_IsPopup = true;
		_EventListeners = new EventListenerList();

		// The transition timings.
		_TransitionOnTime = TimeSpan.FromSeconds(0.2);
		_TransitionOffTime = TimeSpan.FromSeconds(0.2);
	}

	/**
	 * Loads graphics content for this screen.
	 */
	public void loadContent()
	{
		// Load the texture.
		_Texture = Helper.loadImage("Gradient[1].png", true);
	}

	/**
	 * Responds to user input, accepting or cancelling the message box.
	 */
	public void handleInput(InputManager input)
	{
		if (input.getMenuSelect())
		{
			// Raise the accepted event, then exit the message box.
			acceptedInvoke();
			exitScreen();
		}
		else if (input.getMenuCancel())
		{
			// Raise the cancelled event, then exit the message box.
			cancelledInvoke();
			exitScreen();
		}
	}

	/**
	 * Draws the message box.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{
		// Get the font.
		Font font = _ScreenManager.getTextFont();

		// Darken down any other screens that were drawn beneath the popup.
		_ScreenManager.fadeBackBufferToBlack(getTransitionAlpha() * 2 / 3);

		// Center the message text in the window.
		Vector2 viewport = _ScreenManager.getWindowBounds();
		Rectangle2D textSize = font.getStringBounds(_Message, graphics.getFontRenderContext());
		Vector2 textPosition = Vector2.divide(new Vector2(viewport.x - textSize.getWidth(), viewport.y - textSize.getHeight()), 2);

		// The background includes a border somewhat larger than the text itself.
		final int hPad = 32;
		final int vPad = 16;

		Rectangle2D rect = new Rectangle((int) textPosition.x - hPad, (int) textPosition.y - vPad, (int) textSize.getWidth() + hPad * 2, (int) textSize.getHeight() + vPad * 2);

		// Draw the background rectangle with fading during transitions.
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getTransitionPosition()));
		graphics.drawImage(_Texture, (int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight(), null);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		// Draw the message box text.
		Helper.drawString(_Message, font, AffineTransform.getTranslateInstance(textPosition.x, textPosition.y), new Color(255, 255, 255, getTransitionAlpha()), graphics);
	}

	/**
	 * This methods allows classes to register for message events.
	 * 
	 * @param listener
	 *            The listener class.
	 */
	public void addMessageEventListener(MessageBoxEventListener listener)
	{
		_EventListeners.add(MessageBoxEventListener.class, listener);
	}

	/**
	 * This methods allows classes to unregister for message events.
	 * 
	 * @param listener
	 *            The listener class.
	 */
	public void removeMessageEventListener(MessageBoxEventListener listener)
	{
		_EventListeners.remove(MessageBoxEventListener.class, listener);
	}

	/**
	 * Method for raising the accepted event.
	 */
	protected void acceptedInvoke()
	{
		// Invoke the event.
		invokeEvent(new MessageEvent(this, true));
	}

	/**
	 * Method for raising the cancelled event.
	 */
	protected void cancelledInvoke()
	{
		// Invoke the event.
		invokeEvent(new MessageEvent(this, false));
	}

	/**
	 * Method for raising an event.
	 * 
	 * @param e
	 *            The event arguments.
	 */
	protected void invokeEvent(MessageEvent e)
	{
		// For all listeners, enlighten them.
		for (MessageBoxEventListener listener : _EventListeners.getListeners(MessageBoxEventListener.class))
		{
			listener.handleEvent(e);
		}
	}
}