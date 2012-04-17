package events;

import java.util.EventObject;

/**
 * This event has information regarding a menu selection.
 */
public class EntrySelectEvent extends EventObject
{
	/**
	 * Constructor for a menu select event.
	 */
	public EntrySelectEvent(Object source)
	{
		super(source);
	}
}
