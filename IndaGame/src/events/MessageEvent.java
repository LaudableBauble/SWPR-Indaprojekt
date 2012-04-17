package events;

import java.util.EventObject;

/**
 * This event has information regarding a message box.
 */
public class MessageEvent extends EventObject
{
	//Whether the message was accepted or not.
	public boolean Accepted;
	
	/**
	 * Constructor for a message event.
	 */
	public MessageEvent(Object source, boolean accepted)
	{
		super(source);
		
		Accepted = accepted;
	}
}
