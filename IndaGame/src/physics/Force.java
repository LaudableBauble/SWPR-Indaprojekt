package physics;

import auxillary.Vector2;

/**
 * A force instance is interpreted by the physics engine and is the only way unconnected bodies can communicate with each other in the game, ie. through collisions.
 */
public class Force
{
	// The target body, force direction and strength.
	private Body _Body;
	private Vector2 _Force;

	/**
	 * Constructor for a force instance.
	 */
	public Force()
	{
		this(new Body(), new Vector2(0, 0));
	}

	/**
	 * Constructor for a force instance.
	 * 
	 * @param body
	 *            The target body.
	 * @param force
	 *            The velocity of the force.
	 */
	public Force(Body body, Vector2 force)
	{
		// Set the variables.
		_Body = body;
		_Force = force;
	}

	/**
	 * Get a blank force instance.
	 * 
	 * @return Blank force instance.
	 */
	public static Force blank()
	{
		return (new Force());
	}

	/**
	 * Get the force's target body.
	 */
	public Body getBody()
	{
		return _Body;
	}

	/**
	 * Get the force's velocity.
	 */
	public Vector2 getForce()
	{
		return _Force;
	}
}