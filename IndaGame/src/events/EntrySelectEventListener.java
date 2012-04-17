package events;

import java.util.EventListener;

/**
 * This event listener specializes in menu select events.
 */
public interface EntrySelectEventListener extends EventListener
{
	/**
	 * Handle the event if fired.
	 * 
	 * @param event
	 *            The event data.
	 */
	public void handleEvent(EntrySelectEvent event);
}
