package infrastructure;

import input.InputManager;

import java.awt.Graphics2D;

import auxillary.Helper;

/**
 * A screen is a single layer that has update and draw logic, and which can be combined with other layers to build up a complex menu system. For instance the main menu, the options menu, the "are you
 * sure you want to quit" message box, and the main game itself are all implemented as screens.
 */
public abstract class GameScreen
{
	/**
	 * Enum describes the screen transition state.
	 */
	public enum ScreenState
	{
		TransitionOn, Active, TransitionOff, Hidden,
	}

	// The fields.
	protected ScreenManager _ScreenManager;
	protected boolean _IsPopup = false;
	protected TimeSpan _TransitionOnTime;
	protected TimeSpan _TransitionOffTime;
	protected float _TransitionPosition;
	protected ScreenState _ScreenState;
	protected boolean _IsExiting;
	protected boolean _OtherScreenHasFocus;

	/**
	 * Initialize the game screen.
	 */
	public void initialize(ScreenManager screenManager)
	{
		// Initialize the fields.
		_ScreenManager = screenManager;
		_IsPopup = false;
		_TransitionOnTime = TimeSpan.Zero;
		_TransitionOffTime = TimeSpan.Zero;
		_TransitionPosition = 1;
		_ScreenState = ScreenState.TransitionOn;
		_IsExiting = false;
		_OtherScreenHasFocus = false;
	}

	/**
	 * Load graphics content for the screen.
	 */
	public void loadContent()
	{
	}

	/**
	 * Unload content for the screen.
	 */
	public void unloadContent()
	{
	}

	/**
	 * Allows the screen to run logic, such as updating the transition position. Unlike HandleInput, this method is called regardless of whether the screen is active, hidden, or in the middle of a
	 * transition.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param otherScreenHasFocus
	 *            Whether another screen has focus.
	 * @param coveredByOtherScreen
	 *            If covered by another screen.
	 */
	public void update(GameTimer gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		//Whether another screen has focus.
		_OtherScreenHasFocus = otherScreenHasFocus;

		//If exiting...
		if (_IsExiting)
		{
			// If the screen is going away to die, it should transition off.
			_ScreenState = ScreenState.TransitionOff;

			if (!updateTransition(gameTime, _TransitionOffTime, 1))
			{
				// When the transition finishes, remove the screen.
				_ScreenManager.removeScreen(this);
			}
		}
		else if (coveredByOtherScreen)
		{
			// If the screen is covered by another, it should transition off.
			if (updateTransition(gameTime, _TransitionOffTime, 1))
			{
				// Still busy transitioning.
				_ScreenState = ScreenState.TransitionOff;
			}
			else
			{
				// Transition finished!
				_ScreenState = ScreenState.Hidden;
			}
		}
		else
		{
			// Otherwise the screen should transition on and become active.
			if (updateTransition(gameTime, _TransitionOnTime, -1))
			{
				// Still busy transitioning.
				_ScreenState = ScreenState.TransitionOn;
			}
			else
			{
				// Transition finished!
				_ScreenState = ScreenState.Active;
			}
		}
	}

	/**
	 * Helper for updating the screen transition position.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param time
	 *            Time elapsed.
	 * @param direction
	 *            The direction of the transition.
	 * @return Whether the transition is complete.
	 */
	private boolean updateTransition(GameTimer gameTime, TimeSpan time, int direction)
	{
		// How much should we move by?
		float transitionDelta;

		if (time == TimeSpan.Zero)
		{
			transitionDelta = 1;
		}
		else
		{
			transitionDelta = (float) (gameTime.getElapsedTime().TotalMilliseconds() / time.TotalMilliseconds());
		}

		// Update the transition position.
		_TransitionPosition += transitionDelta * direction;

		// Did we reach the end of the transition?
		if ((_TransitionPosition <= 0) || (_TransitionPosition >= 1))
		{
			_TransitionPosition = Helper.clamp(_TransitionPosition, 0, 1);
			return false;
		}

		// Otherwise we are still busy transitioning.
		return true;
	}

	/**
	 * Allows the screen to handle user input. Unlike Update, this method is only called when the screen is active, and not when some other screen has taken the focus.
	 * 
	 * @param input
	 *            The input.
	 */
	public void handleInput(InputManager input)
	{
	}

	/**
	 * This is called when the screen should draw itself.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{

	}

	/**
	 * Tells the screen to go away. Unlike ScreenManager.RemoveScreen, which instantly kills the screen, this method respects the transition timings and will give the screen a chance to gradually
	 * transition off.
	 */
	public void exitScreen()
	{
		if (_TransitionOffTime == TimeSpan.Zero)
		{
			// If the screen has a zero transition time, remove it immediately.
			_ScreenManager.removeScreen(this);
		}
		else
		{
			// Otherwise flag that it should transition off and then exit.
			_IsExiting = true;
		}
	}

	/**
	 * Normally when one screen is brought up over the top of another, the first screen will transition off to make room for the new one. This property indicates whether the screen is only a small
	 * popup, in which case screens underneath it do not need to bother transitioning off.
	 * 
	 * @return If the screen is a popup.
	 */
	public boolean getIsPopup()
	{
		return _IsPopup;
	}

	/**
	 * Indicates how long the screen takes to transition on when it is activated.
	 * 
	 * @return The time it takes for the screen to transition onto the screen.
	 */
	public TimeSpan getTransitionOnTime()
	{
		return _TransitionOnTime;
	}

	/**
	 * Indicates how long the screen takes to transition off when it is deactivated.
	 * 
	 * @return The time it takes for the screen to transition off the screen.
	 */
	public TimeSpan getTransitionOffTime()
	{
		return _TransitionOffTime;
	}

	/**
	 * Gets the current position of the screen transition, ranging from one (fully active, no transition) to zero (transitioned fully off to nothing).
	 * 
	 * @return The position of the transition.
	 */
	public float getTransitionPosition()
	{
		return 1 - _TransitionPosition;
	}

	/**
	 * Gets the current alpha of the screen transition, ranging from 255 (fully active, no transition) to 0 (transitioned fully off to nothing).
	 * 
	 * @return The transition alpha.
	 */
	public int getTransitionAlpha()
	{
		return (int) (255 - _TransitionPosition * 255);
	}

	/**
	 * Gets the current screen transition state.
	 * 
	 * @return The screen state.
	 */
	public ScreenState getScreenState()
	{
		return _ScreenState;
	}

	/**
	 * There are two possible reasons why a screen might be transitioning off. It could be temporarily going away to make room for another screen that is on top of it, or it could be going away for
	 * good. This property indicates whether the screen is exiting for real.
	 * 
	 * @return If the screen is exiting.
	 */
	public boolean getIsExiting()
	{
		return _IsExiting;
	}

	/**
	 * There are two possible reasons why a screen might be transitioning off. It could be temporarily going away to make room for another screen that is on top of it, or it could be going away for
	 * good. If set, the screen will automatically remove itself as soon as the transition finishes.
	 * 
	 * @param isExiting
	 *            If the screen should be exiting.
	 */
	public void setIsExiting(boolean isExiting)
	{
		_IsExiting = isExiting;
	}

	/**
	 * Checks whether this screen is active and can respond to user input.
	 * 
	 * @return If the screen is active.
	 */
	public boolean getIsActive()
	{
		return !_OtherScreenHasFocus && (_ScreenState == ScreenState.TransitionOn || _ScreenState == ScreenState.Active);
	}

	/**
	 * Gets the manager that this screen belongs to.
	 * 
	 * @return The screen manager this screen belongs to.
	 */
	public ScreenManager getScreenManager()
	{
		return _ScreenManager;
	}

	/**
	 * Sets the manager that this screen belongs to.
	 * 
	 * @param screenManager
	 *            The new screen manager.
	 */
	public void setScreenManager(ScreenManager screenManager)
	{
		_ScreenManager = screenManager;
	}
}
