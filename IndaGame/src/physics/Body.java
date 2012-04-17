package physics;

import auxillary.Vector2;

public class Body
{
	// The PhysicsSimulator this body is part of.
	private PhysicsSimulator _Physics;

	// The shape of the body.
	private Shape _Shape;

	// The Friction Coefficient.
	private double _FrictionCoefficient;
	// The mass of the Body.
	private double _Mass;
	// The acceleration value.
	private double _AccelerationValue;

	// The velocity and maximum velocity.
	private Vector2 _Velocity;
	private double _MaxVelocity;

	// If the body is static, in other words immovable.
	private boolean _IsStatic = false;

	// Debug Variable!!!
	public boolean _IsClicked = false;

	/**
	 * Constructor for a body. Uses default values.
	 */
	public Body()
	{
		this(null);
	}

	/**
	 * Constructor for a body. Uses default values.
	 * 
	 * @param physics
	 *            The physics simulator that this body will be a part of.
	 */
	public Body(PhysicsSimulator physics)
	{
		this(1, 1, 10, .25f, physics);
	}

	/**
	 * Constructor for a body.
	 * 
	 * @param width
	 *            The width of the body.
	 * @param height
	 *            The height of the body.
	 * @param mass
	 *            The mass of the body.
	 * @param friction
	 *            The friction coefficient of the body.
	 * @param physics
	 *            The physics simulator that this body will be a part of.
	 */
	public Body(float width, float height, double mass, double friction, PhysicsSimulator physics)
	{
		// Set the variables and create the shape.
		initialize(width, height, mass, friction, physics);
	}

	/**
	 * Initialize the body.
	 * 
	 * @param width
	 *            The width of the body.
	 * @param height
	 *            The height of the body.
	 * @param mass
	 *            The mass of the body.
	 * @param friction
	 *            The friction coefficient of the body.
	 * @param physics
	 *            The physics simulator that this body will be a part of.
	 */
	protected void initialize(float width, float height, double mass, double friction, PhysicsSimulator physics)
	{
		// Initialize a couple of variables.
		_Shape = new Shape(width, height);
		_IsStatic = false;
		_MaxVelocity = 8;
		_Mass = mass;
		_Velocity = new Vector2(0, 0);
		_FrictionCoefficient = friction;
		_AccelerationValue = 1;
		_Physics = physics;
	}

	/**
	 * Update the body.
	 */
	public void update()
	{
		// Check if the body isn't static.
		if (!_IsStatic)
		{
			// Add the velocity to the position.
			_Shape.setPosition(Vector2.add(_Shape.getPosition(), _Velocity));
		}
		else
		{
			// Null the velocity.
			_Velocity = new Vector2(0, 0);
		}
	}

	/**
	 * Add this body to its physics simulator, if it isn't already.
	 */
	public void addBody()
	{
		// Check if the PhysicsSimultor isn't null.
		if (_Physics != null)
		{
			// Try to add the body.
			try
			{
				// Check if the body already is added.
				if (!_Physics.exists(this))
				{
					_Physics.addBody(this);
				}
			}
			// Catch the Exceptions that may arise.
			catch (Exception e)
			{
				System.out.println(this + ": Error adding body. (" + e + ")");
			}
		}
		// Print out the error.
		else
		{
			System.out.println(this + ": Error reaching PhysicsSimulator.");
		}
	}

	/**
	 * Add a force to the physics simulator.
	 * 
	 * @param force
	 *            The force to add.
	 */
	public void addForce(Vector2 force)
	{
		// Check if the PhysicsSimultor isn't null.
		if (_Physics != null)
		{
			// Try to add the force.
			try
			{
				_Physics.addForce(new Force(this, force));
			}
			// Catch the Exceptions that may arise.
			catch (Exception e)
			{
				System.out.println(this + ": Error adding force. (" + e + ")");
			}
		}
		// Print out the error.
		else
		{
			System.out.println(this + ": Error reaching PhysicsSimulator.");
		}
	}

	/**
	 * Get the body's shape.
	 * 
	 * @return The shape of the body.
	 */
	public Shape getShape()
	{
		return _Shape;
	}

	/**
	 * Get the body's position.
	 * 
	 * @return The position of the body.
	 */
	public Vector2 getPosition()
	{
		return _Shape.getPosition();
	}

	/**
	 * Get the body's velocity.
	 * 
	 * @return The velocity of the body.
	 */
	public Vector2 getVelocity()
	{
		return _Velocity;
	}

	/**
	 * Get the body's maximum velocity.
	 * 
	 * @return The maximum velocity of the body.
	 */
	public double getMaxVelocity()
	{
		return _MaxVelocity;
	}

	/**
	 * Get the body's mass.
	 * 
	 * @return The mass of the body.
	 */
	public double getMass()
	{
		return _Mass;
	}

	/**
	 * Get the body's friction coefficient.
	 * 
	 * @return The friction coefficient of the body.
	 */
	public double getFrictionCoefficient()
	{
		return _FrictionCoefficient;
	}

	/**
	 * Get the body's acceleration value.
	 * 
	 * @return The acceleration value of the body.
	 */
	public double getAccelerationValue()
	{
		return _AccelerationValue;
	}

	/**
	 * Set the body's velocity.
	 * 
	 * @param velocity
	 *            The new velocity.
	 */
	public void setVelocity(Vector2 velocity)
	{
		_Velocity = velocity;
	}

	/**
	 * Set the body's staticness.
	 * 
	 * @param isStatic
	 *            Whether the body is static or not.
	 */
	public void setIsStatic(boolean isStatic)
	{
		_IsStatic = isStatic;
	}

	/**
	 * Get the body's staticness.
	 * @return Whether the body is static or not.
	 */
	public boolean getIsStatic()
	{
		return _IsStatic;
	}
}