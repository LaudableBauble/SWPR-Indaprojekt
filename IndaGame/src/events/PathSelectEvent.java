package events;

import java.util.EventObject;

import main.Entity;

/**
 * This event has information regarding a path selection.
 */
public class PathSelectEvent extends EventObject
{
	/**
	 * The path of the event.
	 */
	public String Path;

	/**
	 * Constructor for a path select event.
	 */
	public PathSelectEvent(String source)
	{
		super(source);

		Path = source;
	}
}
