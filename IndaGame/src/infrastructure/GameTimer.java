package infrastructure;

/**
 * The class that keeps track of the timing of the game loop.
 */
public class GameTimer
{
	// The time when the timer was started.
	TimeSpan _StartTime;
	// The total elapsed time since the beginning.
	TimeSpan _TotalElapsedTime;
	// The elapsed time since last update.
	TimeSpan _ElapsedTime;
	// Whether the timer is currently running.
	boolean _IsRunning;

	/**
	 * Constructor for the game timer.
	 */
	public GameTimer()
	{

	}

	/**
	 * Turns this timer on. Has no effect if the it is already running.
	 */
	public void start()
	{
		// If the timer isn't currently running, turn it on.
		if (!_IsRunning)
		{
			_IsRunning = true;
			_StartTime = new TimeSpan(System.nanoTime() / TimeSpan.NanosecondsPerTick);
			_TotalElapsedTime = TimeSpan.Zero;
			_ElapsedTime = TimeSpan.Zero;
		}
	}

	/**
	 * Turns this timer off. Has no effect if the timer is not running.
	 */
	public void stop()
	{
		// If the timer is currently running, turn it off.
		if (_IsRunning)
		{
			_TotalElapsedTime.Add(TimeSpan.Subtract(System.nanoTime() / TimeSpan.NanosecondsPerTick, _StartTime));
			_ElapsedTime.Add(TimeSpan.Subtract(System.nanoTime() / TimeSpan.NanosecondsPerTick, _StartTime));
			_IsRunning = false;
		}
	}

	/**
	 * Resets this timer. The timer is stopped and the elapsed time is set to 0.
	 */
	public void reset()
	{
		// Reset the timer.
		_IsRunning = false;
		_TotalElapsedTime = TimeSpan.Zero;
		_ElapsedTime = TimeSpan.Zero;
	}

	/**
	 * Resets the elapsed time of this timer. Use this method if your game is recovering from a slow-running state, and the elapsed time is too large to be useful.
	 */
	public void resetElapsedTime()
	{
		// Reset the elapsed time.
		_ElapsedTime = TimeSpan.Zero;
	}

	/**
	 * Update the game timer. Every new update call constitutes as a new cycle in the game.
	 */
	public void update()
	{
		// Calculate the time elapsed since last update.
		_ElapsedTime = TimeSpan.Subtract(System.nanoTime() / TimeSpan.NanosecondsPerTick, _StartTime.Add(_TotalElapsedTime));
		// Add the elapsed time to the total elapsed time.
		_TotalElapsedTime = _TotalElapsedTime.Add(_ElapsedTime);
	}

	/**
	 * Get the elapsed time since last update.
	 * 
	 * @return The time elapsed since last update.
	 */
	public TimeSpan getElapsedTime()
	{
		return _ElapsedTime;
	}

	/**
	 * Get the total elapsed time since the start of the game (timer).
	 * 
	 * @return The time elapsed since start of the timer.
	 */
	public TimeSpan getTotalElapsedTime()
	{
		return _TotalElapsedTime;
	}
}
