package events;

import java.util.EventListener;

/**
 * This event listener specializes in message box events.
 */
public interface MessageBoxEventListener extends EventListener
{
	/**
	 * Handle the event if fired.
	 * 
	 * @param event
	 *            The event data.
	 */
	public void handleEvent(MessageEvent event);
}
