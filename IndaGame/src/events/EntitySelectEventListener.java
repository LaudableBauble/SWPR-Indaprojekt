package events;

import java.util.EventListener;

/**
 * This event listener specializes in entity selection events.
 */
public interface EntitySelectEventListener extends EventListener
{
	/**
	 * Handle the event if fired.
	 * 
	 * @param event
	 *            The event data.
	 */
	public void handleEvent(EntitySelectEvent event);
}
