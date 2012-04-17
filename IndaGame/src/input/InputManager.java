/**
 * File: InputManager.java
 * 
 * This file is free to use and modify as it is for educational use.
 * brought to you by Game Programming Snippets (http://gpsnippets.blogspot.com/)
 * 
 * Revisions:
 * 1.1 Initial Revision 
 * 
 */
package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

import auxillary.Vector2;

/**
 * This class was created to show how implement a keyboard polling system using java.awt.event.KeyListener this allows the ability to capture multiple keys being pressed simultaneously. To use this
 * class just simply add it as a key listener to your JFrame and it will be populated with input.
 */
public final class InputManager extends MouseInputAdapter implements KeyListener
{
	// Array of key states used to log the time of every key press.
	private long[] keys = new long[256];

	// One for each ASCII character. They are true if respectively not pressed, pressed and newly pressed.
	private boolean[] key_state_up = new boolean[256];
	private boolean[] key_state_down = new boolean[256];
	private boolean[] key_state_pressed = new boolean[256];

	// Variable that indicates when any key(s) are being pressed.
	private boolean keyPressed = false;
	// Variable that indicates that some key was released this frame.
	private boolean keyReleased = false;
	// A string used as a buffer by widgets or other text input controls.
	private String keyCache = "";

	// Variable that indicates when a mouse button is pressed. 0 = none, 1 = button1, 2 = button2, 3 = button3.
	private boolean[] mouseButtonPressed = new boolean[4];
	// Variable that indicates when a mouse button is dragged.
	private boolean[] mouseButtonDragged = new boolean[4];
	// Variable that indicates when a mouse button is released.
	private boolean[] mouseButtonReleased = new boolean[4];

	// Variable that indicates the position of the mouse when the latest event fired.
	private Vector2 eventPosition = new Vector2();

	// The only instantiated object
	private static InputManager instance = new InputManager();

	/**
	 * Empty Constructor: Nothing really needed here.
	 */
	protected InputManager()
	{
	}

	/**
	 * Singleton accessor the only means of getting the instantiated object.
	 * 
	 * @return The one and only InputManager object.
	 */
	public static InputManager getInstance()
	{
		return instance;
	}

	/**
	 * This function is specified in the KeyListener interface. It sets the state elements for whatever key was pressed.
	 * 
	 * @param e
	 *            The KeyEvent fired by the awt Toolkit
	 */
	public void keyPressed(KeyEvent e)
	{
		// If the keycode is within 0 and 256.
		if ((e.getKeyCode() >= 0) && (e.getKeyCode() < 256))
		{
			// Add the time this key was pressed.
			keys[e.getKeyCode()] = System.currentTimeMillis();
			// The key is newly pressed and held down.
			key_state_pressed[e.getKeyCode()] = key_state_down[e.getKeyCode()] ? false : true;
			key_state_down[e.getKeyCode()] = true;
			key_state_up[e.getKeyCode()] = false;

			// A key is pressed and thus not released.
			keyPressed = true;
			keyReleased = false;
		}
	}

	/**
	 * This function is specified in the KeyListener interface. It sets the state elements for whatever key was released.
	 * 
	 * @param e
	 *            The KeyEvent fired by the awt Toolkit.
	 */
	public void keyReleased(KeyEvent e)
	{
		// If the keycode is within 0 and 256.
		if ((e.getKeyCode() >= 0) && (e.getKeyCode() < 256))
		{
			// Reset the time stamp this key was pressed.
			keys[e.getKeyCode()] = 0;
			// The key is now up.
			key_state_up[e.getKeyCode()] = true;
			key_state_down[e.getKeyCode()] = false;

			// A key isn't pressed.
			keyPressed = false;
			keyReleased = true;
		}
	}

	/**
	 * This function is called if certain keys are pressed, namely those used for text input.
	 * 
	 * @param e
	 *            The KeyEvent fired by the awt Toolkit.
	 */
	public void keyTyped(KeyEvent e)
	{
		// Add the char to the keyCache.
		keyCache += e.getKeyChar();
	}

	/**
	 * This function is specified in the MouseListener interface. It sets the state elements for whatever mouse button was pressed.
	 * 
	 * @param e
	 *            The MouseEvent fired by the awt Toolkit
	 */
	public void mousePressed(MouseEvent e)
	{
		// The Mouse Button is Pressed.
		mouseButtonPressed[e.getButton()] = true;
		// The Mouse Button isn't Released.
		mouseButtonReleased[e.getButton()] = false;
		// The Mouse position.
		eventPosition.x = e.getX();
		eventPosition.y = e.getY();
	}

	/**
	 * This function is specified in the MouseListener interface. It sets the state elements for whatever mouse button was dragged.
	 * 
	 * @param e
	 *            The MouseEvent fired by the awt Toolkit
	 */
	public void mouseDragged(MouseEvent e)
	{
		// The Mouse Button is Dragged.
		mouseButtonDragged[e.getButton()] = true;
		// The Mouse position.
		eventPosition.x = e.getX();
		eventPosition.y = e.getY();
	}

	/**
	 * This function is specified in the MouseListener interface. It sets the state elements for whatever mouse button was released.
	 * 
	 * @param e
	 *            The MouseEvent fired by the awt Toolkit
	 */
	public void mouseReleased(MouseEvent e)
	{
		// The Mouse Button is Released.
		mouseButtonReleased[e.getButton()] = true;
		// The Mouse Button isn't Dragged.
		mouseButtonDragged[e.getButton()] = false;
		// The Mouse Button isn't Pressed.
		mouseButtonPressed[e.getButton()] = false;
		// The Mouse position.
		eventPosition.x = e.getX();
		eventPosition.y = e.getY();
	}

	/**
	 * Returns true if the key (0-256) is being pressed use the KeyEvent.VK_ key variables to check specific keys.
	 * 
	 * @param key
	 *            The ASCII value of the keyboard key being checked
	 * @return true is that key is currently being pressed.
	 */
	public boolean isKeyDown(int key)
	{
		// Return a true/false.
		return (key_state_down[key]);
	}

	/**
	 * Returns true if the key (0-256) is being pressed use the KeyEvent.VK_ key variables to check specific keys.
	 * 
	 * @param key
	 *            The ASCII value of the keyboard key being checked
	 * @return true is that key is currently being pressed.
	 */
	public boolean isKeyUp(int key)
	{
		// Return a true/false.
		return (key_state_up[key]);
	}

	/**
	 * Helper for checking if a key (0-256) was newly pressed during this update. Use the KeyEvent.VK_ key variables to check specific keys.
	 * 
	 * @param key
	 *            The key in question.
	 * @return Whether the key was newly pressed or not.
	 */
	public boolean isNewKeyPress(int key)
	{
		// If the key is newly pressed.
		return (key_state_down[key] && key_state_pressed[key]);
	}

	/**
	 * In case you want to know if a user is pressing a key but don't care which one.
	 * 
	 * @return true if one or more keys are currently being pressed.
	 */
	public boolean isAnyKeyDown()
	{
		// Return a true/false.
		return keyPressed;
	}

	/**
	 * In case you want to know if a user released a key but don't care which one.
	 * 
	 * @return true if one or more keys have been released this frame.
	 */
	public boolean isAnyKeyUp()
	{
		// Return a true/false.
		return keyReleased;
	}

	/**
	 * Returns true if the mouse button (0-3) is being pressed.
	 * 
	 * @param button
	 *            The value of the mouse button being checked
	 * @return true is that mouse button is currently being pressed.
	 */
	public boolean isMouseButtonDown(int button)
	{
		// Return a true/false.
		return (mouseButtonPressed[button]);
	}

	/**
	 * Returns true if the mouse button (0-3) is being dragged.
	 * 
	 * @param button
	 *            The value of the mouse button being checked
	 * @return true is that mouse button is currently being dragged.
	 */
	public boolean isMouseButtonDragged(int button)
	{
		// Return a true/false.
		return (mouseButtonDragged[button]);
	}

	/**
	 * Returns true if the mouse button (0-3) is being released.
	 * 
	 * @param button
	 *            The value of the mouse button being checked
	 * @return true is that mouse button is currently being released.
	 */
	public boolean isMouseButtonUp(int button)
	{
		// Return a true/false.
		return (mouseButtonReleased[button]);
	}

	/**
	 * Returns the position of the mouse when the latest event fired.
	 * 
	 * @return The mouse's position.
	 */
	public Vector2 mouseEventPosition()
	{
		// Return the event vector.
		return (eventPosition);
	}

	/**
	 * Only resets the key state up because you don't want keys to be showing as up forever which is what will happen unless the array is cleared.
	 */
	public void update()
	{
		// Clear out the key up states.
		key_state_up = new boolean[256];
		key_state_pressed = new boolean[256];
		// Clear the Key Released State.
		keyReleased = false;

		// If the keyCache is greater than 1024 characters, clear the buffer.
		if (keyCache.length() > 1024)
		{
			// Clear the buffer.
			keyCache = "";
		}
	}

	/**
	 * Checks for a "menu up" input action on the keyboard.
	 */
	public boolean getMenuUp()
	{
		return (isNewKeyPress(KeyEvent.VK_UP) || isNewKeyPress(KeyEvent.VK_W));
	}

	/**
	 * Checks for a "menu down" input action on the keyboard.
	 */
	public boolean getMenuDown()
	{
		return (isNewKeyPress(KeyEvent.VK_DOWN) || isNewKeyPress(KeyEvent.VK_S));
	}

	/**
	 * Checks for a "menu select" input action on the keyboard.
	 */
	public boolean getMenuSelect()
	{
		return (isNewKeyPress(KeyEvent.VK_SPACE) || isNewKeyPress(KeyEvent.VK_ENTER));
	}

	/**
	 * Checks for a "menu cancel" input action on the keyboard.
	 */
	public boolean getMenuCancel()
	{
		return (isNewKeyPress(KeyEvent.VK_ESCAPE));
	}

	/**
	 * Checks for a "pause the game" input action on the keyboard.
	 */
	public boolean getPauseGame()
	{
		return (isNewKeyPress(KeyEvent.VK_ESCAPE));
	}
}