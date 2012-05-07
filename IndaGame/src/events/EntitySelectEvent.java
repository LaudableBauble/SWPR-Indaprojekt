package events;

import java.util.EventObject;

import main.Entity;

/**
 * This event has information regarding an entity selection.
 */
public class EntitySelectEvent extends EventObject
{
	/**
	 * The entity data.
	 */
	public Entity Entity;

	/**
	 * Constructor for an entity selection event.
	 */
	public EntitySelectEvent(Entity source)
	{
		super(source);

		// Store the entity.
		Entity = source;
	}
}
