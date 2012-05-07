package events;

import java.util.EventListener;

/**
 * This event listener specializes in path selection events.
 */
public interface PathSelectEventListener extends EventListener
{
	/**
	 * Handle the event if fired.
	 * 
	 * @param event
	 *            The event data.
	 */
	public void handleEvent(PathSelectEvent event);
}
