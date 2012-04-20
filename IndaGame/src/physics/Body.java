package physics;

import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A body extends a shape to also include movement by altering its velocity, something the physics simulator does with its use of forces.
 */
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
	private Vector3 _Velocity;
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
		this(1, 1, 1, 10, .25f, physics);
	}

	/**
	 * Constructor for a body.
	 * 
	 * @param width
	 *            The width of the shape (x-coordinate).
	 * @param height
	 *            The height of the shape (y-coordinate).
	 * @param depth
	 *            The depth of the shape (z-coordinate).
	 * @param mass
	 *            The mass of the body.
	 * @param friction
	 *            The friction coefficient of the body.
	 * @param physics
	 *            The physics simulator that this body will be a part of.
	 */
	public Body(float width, float height, float depth, double mass, double friction, PhysicsSimulator physics)
	{
		// Set the variables and create the shape.
		initialize(width, height, depth, mass, friction, physics);
	}

	/**
	 * Initialize the body.
	 * 
	 * @param width
	 *            The width of the shape (x-coordinate).
	 * @param height
	 *            The height of the shape (y-coordinate).
	 * @param depth
	 *            The depth of the shape (z-coordinate).
	 * @param mass
	 *            The mass of the body.
	 * @param friction
	 *            The friction coefficient of the body.
	 * @param physics
	 *            The physics simulator that this body will be a part of.
	 */
	protected void initialize(float width, float height, float depth, double mass, double friction, PhysicsSimulator physics)
	{
		// Initialize a couple of variables.
		_Shape = new Shape(width, height, depth);
		_IsStatic = false;
		_MaxVelocity = 8;
		_Mass = mass;
		_Velocity = new Vector3(0, 0, 0);
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
			_Shape.setPosition(Vector3.add(_Shape.getPosition(), _Velocity));
		}
		else
		{
			// Null the velocity.
			_Velocity = new Vector3(0, 0, 0);
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
	 * Get the body's layered position, ie. only the x and y-coordinates.
	 * 
	 * @return The position of the body.
	 */
	public Vector2 getLayeredPosition()
	{
		return _Shape.getLayeredPosition();
	}

	/**
	 * Get the body's position, ie. all three dimensions.
	 * 
	 * @return The position of the body.
	 */
	public Vector3 getPosition()
	{
		return _Shape.getPosition();
	}

	/**
	 * Set the body's position, ie. all three dimensions.
	 * 
	 * @position The new position of the body.
	 */
	public void setPosition(Vector3 position)
	{
		_Shape.setPosition(position);
	}

	/**
	 * Get the body's velocity.
	 * 
	 * @return The velocity of the body.
	 */
	public Vector3 getVelocity()
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
	 * Set the mass of the body.
	 * 
	 * @param mass
	 *            The new mass.
	 */
	public void setMass(double mass)
	{
		_Mass = mass;
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
	 * Set the body's friction coefficient.
	 * 
	 * @param friction
	 *            The new friction coefficient of the body.
	 */
	public void setFrictionCoefficient(double friction)
	{
		_FrictionCoefficient = friction;
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
	 * Set the body's acceleration value.
	 * 
	 * @param The
	 *            new acceleration value of the body.
	 */
	public void setAccelerationValue(double acceleration)
	{
		_AccelerationValue = acceleration;
	}

	/**
	 * Set the body's velocity.
	 * 
	 * @param velocity
	 *            The new velocity.
	 */
	public void setVelocity(Vector3 velocity)
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
	 * 
	 * @return Whether the body is static or not.
	 */
	public boolean getIsStatic()
	{
		return _IsStatic;
	}

	/**
	 * Set the maximum velocity of this body.
	 * 
	 * @param The
	 *            max velocity.
	 */
	public void setMaxVelocity(double max)
	{
		_MaxVelocity = max;
	}
}