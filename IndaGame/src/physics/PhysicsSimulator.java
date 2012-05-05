package physics;

import infrastructure.Enums.DepthDistribution;

import java.awt.Rectangle;
import java.util.ArrayList;

import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A physics simulator does just what its name implies; it tries to simulate the physics of bodies so that the game world behaves realistically.
 */
public class PhysicsSimulator
{
	// Create the body array that'll hold all bodies in play.
	public ArrayList<Body> _Bodies;
	// The list of forces to add.
	public ArrayList<Force> _Forces;

	// The gravity.
	public double _Gravity;

	// The percentile energy decrease.
	public double energyDecrease;

	/**
	 * Constructor for a physics simulator.
	 */
	public PhysicsSimulator()
	{
		initialize();
	}

	/**
	 * Initialize the physics simulator.
	 */
	private void initialize()
	{
		// Initialize the Body array.
		_Bodies = new ArrayList<Body>();
		// Initialize the Forces list.
		_Forces = new ArrayList<Force>();
		// Set up the Gravity.
		_Gravity = 0.3;
		// The energy decrease.
		energyDecrease = 0.05;
	}

	/**
	 * Update the physics manager.
	 */
	public void update()
	{
		// Check if the array isn't empty.
		try
		{
			// Loop through all bodies.
			for (Body b1 : _Bodies)
			{
				// Clear all previous collisions.
				b1.clearCollisions();
				// Ground collision?
				boolean ground = false;

				// Loop through all bodies and check for collision.
				for (Body b2 : _Bodies)
				{
					// Check so it's not the same body, or if both bodies are set to static.
					if (b1 == b2 || (b1.getIsStatic() && b2.getIsStatic()))
					{
						continue;
					}

					// Check if the bodies are within range. If so, continue to the narrow phase part.
					if (broadPhase(b1, b2))
					{
						// Get the layered MTV by doing a narrow phase collision check.
						Vector2 mtv = narrowPhase(b1.getShape(), b2.getShape());

						// Check for ground collision and alter bodies if necessary.
						if (checkGroundCollision(b1, b2, mtv))
						{
							// Add the collision to the body.
							b1.addCollision(b2);

							if (b1.getIsStatic() || b1.getIsImmaterial() || b2.getIsImmaterial())
							{
								continue;
							}

							// Move body1 above body2 and null the movement on the z-axis (otherwise the body gets stuck).
							b1.getShape().setBottomDepth(b2.getShape().getTopDepth(b1.getLayeredPosition()) + _Gravity / 2);
							b1.getVelocity().setZ(0);
							if (b2.getShape().getDepthDistribution() != DepthDistribution.Uniform)
							{
								b1.setVelocity(Vector3.empty());
							}
							ground = true;
						}
						else
						{

							// Ensure that the would-be collision occurred in allowed height space.
							mtv = getLayeredCollision(b1, b2, mtv);

							// Check if the bodies intersect.
							if (mtv != null)
							{
								if (!b1.getIsImmaterial() && !b2.getIsImmaterial())
								{
									// Calculate the Impact force and vector for both
									// bodies.
									// addForce(impactForce(b1, b2));
									// addForce(impactForce(b2, b1));
									// Move the bodies so that they don't intersect each other anymore.
									clearIntersection(b1, b2, mtv);
								}

								// Add the collision to the body.
								b1.addCollision(b2);
							}
						}
					}
				}

				// If the entity is dynamic and not standing on the ground, apply gravity.
				if (!ground && !b1.getIsStatic() && !b1.getIsImmaterial())
				{
					b1.addGravity(_Gravity);
				}

				// Add the friction.
				addFrictionForce(getBodyFriction(b1));
				// Add all forces to the body.
				addForcesToBody(getForces(b1));
				// Update the body.
				b1.update();
			}

			// Clear the List of all Forces.
			_Forces.clear();
		}
		// Catch the exception.
		catch (Exception e)
		{
			System.out.println(this + ": Update Physics Error. (" + e + ")");
		}
	}

	/**
	 * Add a body to the physics simulator.
	 * 
	 * @param body
	 *            The body to add.
	 */
	public void addBody(Body body)
	{
		// Check if the array is initialized.
		if (_Bodies != null)
		{
			// Try to add the body at the end of the array.
			try
			{
				// If the body isn't already in the folds of the physics simulator.
				if (!_Bodies.contains(body))
				{
					_Bodies.add(body);
					body.setPhysicsSimulator(this);
				}
			}
			// Catch the exception and display relevant information.
			catch (Exception e)
			{
				System.out.println(this + ": Error adding body. (" + e + ")");
			}
		}
	}

	/**
	 * Remove a body from the physics simulator.
	 * 
	 * @param body
	 *            The body to remove.
	 */
	public void removeBody(Body body)
	{
		_Bodies.remove(body);
	}

	/**
	 * Add a force to the physics simulator.
	 * 
	 * @param force
	 *            The force to add.
	 */
	public void addForce(Force force)
	{
		// Check if the array is initialized.
		if (_Forces != null)
		{
			// Try to add the Force at the end of the list.
			try
			{
				_Forces.add(force);
			}
			// Catch the exception and display relevant information.
			catch (Exception e)
			{
				System.out.println(this + ": Error adding force. (" + e + ")");
			}
		}
	}

	/**
	 * Do a broad phase collision check. (Currently checks if the bodies might intersect on the XY-axis.
	 * 
	 * @param b1
	 *            The first body to check.
	 * @param b2
	 *            The second body to check.
	 * @return Whether the two bodies are close enough to warrant a possible collision.
	 */
	private boolean broadPhase(Body b1, Body b2)
	{
		// Try this.
		try
		{
			// Check if the bodies are within range.
			return Vector2.getDistance(b1.getLayeredPosition(), b2.getLayeredPosition()) < (Math.max(b1.getShape().getWidth(), b1.getShape().getHeight()) + Math.max(b2
					.getShape().getWidth(), b2.getShape().getHeight()));
		}
		// Catch the exception and display relevant information.
		catch (Exception e)
		{
			System.out.println(this + ": Broad Phase Error. (" + e + ") - Body1: " + b1 + " Body2: " + b2);
		}

		// Something went wrong.
		return false;
	}

	/**
	 * Do a narrow phase collision check between two shapes by using SAT (Separating Axis Theorem). If a collision has occurred, get the MTV (Minimum Translation Vector) of the two intersecting
	 * shapes.
	 * 
	 * @param s1
	 *            The first shape to check.
	 * @param s2
	 *            The second shape to check.
	 * @return The MTV of the intersection or null if the collision was negative.
	 */
	public Vector2 narrowPhase(Shape s1, Shape s2)
	{
		// The minimum amount of overlap. Start real high.
		double overlap = Double.MAX_VALUE;
		// The smallest axis found.
		Vector2 smallest = null;

		try
		{
			// Get the axes of both bodies.
			Vector2[][] axes = new Vector2[][] { s1.getAxes(), s2.getAxes() };

			// Iterate over the axes of both bodies.
			for (Vector2[] v : axes)
			{
				// Iterate over both bodies' axes.
				for (Vector2 a : v)
				{
					// Project both bodies onto the axis.
					Vector2 p1 = s1.project(a);
					Vector2 p2 = s2.project(a);

					// Get the overlap.
					double o = Vector2.getOverlap(p1, p2);

					// Do the projections overlap?
					if (o == -1)
					{
						// We can guarantee that the shapes do not overlap.
						return null;
					}
					else
					{
						// Check for minimum.
						if (o < overlap)
						{
							// Store the minimum overlap and the axis it was projected upon. Make sure that the separation vector is pointing the right way.
							overlap = o;
							smallest = a;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(this + ": Narrow Phase Error. (" + e + ")");
		}

		// We now know that every axis had an overlap on it, which means we can
		// guarantee an intersection between the bodies.
		return Vector2.multiply(smallest, overlap);
	}

	/**
	 * Check for collisions between two bodies at a certain range of z-coordinates (height).
	 * 
	 * @param b1
	 *            The first body to check.
	 * @param b2
	 *            The second body to check.
	 * @param mtv
	 *            The MTV of the layered collision.
	 * @return The MTV of the intersection or null if the collision was negative.
	 */
	private Vector2 getLayeredCollision(Body b1, Body b2, Vector2 mtv)
	{
		// If there is no layered collision between the bodies, stop here.
		if (mtv == null) { return null; }

		// Get the dynamic and static body.
		Body a = b1.getIsStatic() ? b2 : b1;
		Body b = (a == b1) ? b2 : b1;

		// Get the min and max heights for both bodies.
		Vector2 h1 = new Vector2(a.getShape().getBottomDepth(), a.getShape().getTopDepth(a.getLayeredPosition()));
		Vector2 h2 = new Vector2(b.getShape().getBottomDepth(), b.getShape().getTopDepth(a.getLayeredPosition()));

		// Get min and max heights for possible collisions between the bodies.
		Vector2 heights = Vector2.getMiddleValues(h1, h2);

		// If there were no matching heights found, no collision possible.
		if (heights == null || (h2.y - h1.x) < 3) { return null; }

		// Return the MTV.
		return mtv;
	}

	/**
	 * Check for collision between two bodies where one is coming from above the other. This is a preemptive step due to the use of body velocity to project future positions.
	 * 
	 * @param b1
	 *            The first body to check.
	 * @param b2
	 *            The second body to check.
	 * @param mtv
	 *            The MTV of the collision.
	 * @return Whether there was a ground collision or not, from the first body's perspective.
	 */
	private boolean checkGroundCollision(Body b1, Body b2, Vector2 mtv)
	{
		// The first body has to be dynamic or if there is no layered collision between the bodies, stop here.
		if (mtv == null) { return false; }

		// Get the dynamic and static body.
		Body a = b1.getIsStatic() ? b2 : b1;
		Body b = (a == b1) ? b2 : b1;

		// Both bodies' depth positions.
		Vector2 h1 = new Vector2(a.getShape().getBottomDepth(), a.getShape().getTopDepth());
		Vector2 h2 = new Vector2(b.getShape().getBottomDepth(), b.getShape().getTopDepth(a.getLayeredPosition()));

		// The difference in height.
		double diff = h1.x - h2.y;

		// If the distance between the bodies is either greater than the threshold or less than the velocity needed to collide, no collision.
		if (diff > Math.max(-a.getVelocity().z + _Gravity, 0) || (h2.y - h1.x) > 2) {

		return false; }

		// There must be a ground collision after all.
		return true;
	}

	/**
	 * Pull two bodies that are intersecting apart by using the MTV.
	 * 
	 * @param b1
	 *            The first body.
	 * @param b2
	 *            The second body.
	 * @param mtv
	 *            The MTV to use.
	 */
	private void clearIntersection(Body b1, Body b2, Vector2 mtv)
	{
		// If the MTV is null, stop here.
		if (mtv == null) { return; }

		// Add the MTV to the first body and subtract it from the second. Only move dynamic bodies!
		if (!b1.getIsStatic())
		{
			b1.getShape().setLayeredPosition(Vector2.add(b1.getShape().getLayeredPosition(), mtv));
			b1.setVelocity(Vector3.empty());
		}
		if (!b2.getIsStatic())
		{
			b2.getShape().setLayeredPosition(Vector2.subtract(b2.getShape().getLayeredPosition(), mtv));
			b2.setVelocity(Vector3.empty());
		}
	}

	/**
	 * Calculate the impact force vector.
	 * 
	 * @param b1
	 *            The first body.
	 * @param b2
	 *            The second body.
	 * @return The impact force vector.
	 */
	private Force impactForce(Body b1, Body b2)
	{
		// Calculate the Energy of the two bodies before impact.
		Vector2 energyB1 = Vector3.absolute(Vector3.multiply(b1.getVelocity(), b1.getMass())).toVector2();
		Vector2 energyB2 = Vector3.absolute(Vector3.multiply(b2.getVelocity(), b2.getMass())).toVector2();
		Vector2 energyBT = Vector2.add(energyB1, energyB2);

		// Get the intersection rectangle.
		Rectangle intersection = b1.getShape().getIntersection(b2.getShape());
		// Calculate the Collision point.
		Vector2 collision = Helper.toCentroid(intersection);

		// The mass ratio between the objects.
		double massRatio = b2.getMass() / b1.getMass();

		// The average kinetic energy. Multiply with something to lower the collision force and also with the mass ratio.
		Vector2 averageEnergy = Vector2.multiply(Vector2.multiply(Vector2.divide(energyBT, 2), energyDecrease), massRatio);

		// Multiply the Average kinetic Energy with the collision vector direction relative to the body's position.
		Vector2 impact = Vector2.multiply(averageEnergy, Vector2.getDirection(Vector2.getAngle(collision, b1.getLayeredPosition())));

		// Return the average vector.
		return new Force(b1, impact);
	}

	/**
	 * Calculate the impact force by using the kinetic energy.
	 * 
	 * @param b1
	 *            The first body.
	 * @param b2
	 *            The second body.
	 * @return The impact force between the two bodies.
	 */
	private Force impactForceEnergy(Body b1, Body b2)
	{
		// Calculate the Kinetic Energy of the two bodies.
		Vector2 energyB1 = Vector3.divide(Vector3.multiply(Vector3.absolute(Vector3.multiply(b1.getVelocity(), b1.getVelocity())), b1.getMass()), 2).toVector2();
		Vector2 energyB2 = Vector3.divide(Vector3.multiply(Vector3.absolute(Vector3.multiply(b2.getVelocity(), b2.getVelocity())), b2.getMass()), 2).toVector2();

		// Get the intersection rectangle.
		Rectangle intersection = b1.getShape().getIntersection(b2.getShape());
		// Calculate the Collision point.
		Vector2 collision = Helper.toCentroid(intersection);

		// The average kinetic energy. Multiply with something to lower the
		// collision force.
		Vector2 averageEnergy = Vector2.multiply(Vector2.divide(Vector2.add(energyB1, energyB2), 2), energyDecrease);

		// Multiply the Average Kinetic Energy with the collision vector
		// direction relative to the body's position.
		Vector2 impact = Vector2.multiply(averageEnergy, Vector2.getDirection(Vector2.getAngle(collision, b1.getLayeredPosition())));

		// Return the average vector.
		return (new Force(b1, impact));
	}

	/**
	 * Get the number of bodies.
	 * 
	 * @return The number of bodies in this physics simulator.
	 */
	public int bodyCount()
	{
		// Create the return variable.
		int result = 0;

		// Try.
		try
		{
			// Set the body count.
			result = _Bodies.size();
		}
		// Catch the exception and display relevant information.
		catch (Exception e)
		{
			System.out.println(this + ": Body Count Error. (" + e + ")");
		}

		// Return the result.
		return result;
	}

	/**
	 * Get the number of forces in this iteration.
	 * 
	 * @return The number of forces.
	 */
	public int forceCount()
	{
		// Create the return variable.
		int result = 0;

		// Try.
		try
		{
			// Set the force count.
			result = _Forces.size();
		}
		// Catch the exception and display relevant information.
		catch (Exception e)
		{
			System.out.println(this + ": Force Count Error. (" + e + ")");
		}

		// Return the result.
		return result;
	}

	/**
	 * Get the forces connected to a certain body.
	 * 
	 * @param body
	 *            The body used to find connected forces.
	 * @return The connected forces.
	 */
	public ArrayList<Force> getForces(Body body)
	{
		// Create the return variable.
		ArrayList<Force> force = new ArrayList<Force>();

		// Try this.
		try
		{
			// Loop through the forces array.
			for (Force f : _Forces)
			{
				// Try to match the bodies in the Force.
				if (f.getBody() == body)
				{
					force.add(f);
				}
			}
		}
		// Catch the exception.
		catch (Exception e)
		{
			System.out.println(this + ": Force Exists Error. (" + e + ")");
		}

		// Return all forces belonging to a body.
		return force;
	}

	/**
	 * Check if a body is in the array of bodies.
	 * 
	 * @param body
	 *            The body to look for.
	 * @return Whether the body do indeed exist in the physics simulator.
	 */
	public boolean exists(Body body)
	{
		// Create the return variable.
		boolean exists = false;

		// Try this.
		try
		{
			// Loop through the body array.
			for (Body b : _Bodies)
			{
				// Try to match the bodies.
				if (body == b)
				{
					exists = true;
				}
			}
		}
		// Catch the exception.
		catch (Exception e)
		{
			System.out.println(this + ": Body Exists Error. (" + e + ")");
		}

		// Return the result.
		return exists;
	}

	/**
	 * Add forces to their respective bodies.
	 * 
	 * @param bodyForces
	 *            The forces.
	 */
	private void addForcesToBody(ArrayList<Force> bodyForces)
	{
		// Check if the list isn't empty.
		if (!bodyForces.isEmpty())
		{
			// Try this.
			try
			{
				// Loop through the force list.
				for (Force f : bodyForces)
				{
					// Add the Force to the body.
					f.getBody().setVelocity(Vector3.add(f.getBody().getVelocity(), f.getForce()));
				}
			}
			// Catch the exception.
			catch (Exception e)
			{
				System.out.println(this + ": Adding Force to Body Error. (" + e + ")");
			}
		}
	}

	/**
	 * Calculate the friction force and its direction for a body.
	 * 
	 * @param b
	 *            The body to calculate for.
	 * @return The friction.
	 */
	private Force getBodyFriction(Body b)
	{
		// First, multiply the friction coefficient with the gravity's force exertion on the body (mass * gravity value),
		// then with the direction of the velocity. Inverse the vector and finally return the result.
		// return (new Force(b, Vector.multiply(Vector.getDirection(Vector.getAngle(b.position, b.velocity)), (b.frictionCoefficient * (b.mass * gravity)))));
		return new Force(b, Vector2.multiply(Vector2.inverse(Vector2.getDirection(b.getVelocity().toVector2(), Vector3.getLength(Vector3.absolute(b.getVelocity())))),
				(b.getFrictionCoefficient() * (b.getMass() * _Gravity))));
	}

	/**
	 * Add a friction force to a body.
	 * 
	 * @param f
	 *            The friction force.
	 */
	private void addFrictionForce(Force f)
	{
		// Try this.
		try
		{
			// ////////////////////////////////////////////////////////
			// System.out.println(this + ": The Friction: (" + f.getForce().toString() + ")");
			// Calculate the Friction.
			Vector2 friction = Vector3.add(f.getBody().getVelocity(), f.getForce()).toVector2();
			// ////////////////////////////////////////////////////////
			// System.out.println(this + ": Old Velocity: (" + f.body.velocity.toString() + ")");
			// Clam the friction above or beneath zero and subtract it from the velocity.
			f.getBody().setVelocity(new Vector3(Vector2.clamp(f.getBody().getVelocity().toVector2(), friction, 0), f.getBody().getVelocity().z));
			// ////////////////////////////////////////////////////////
			// System.out.println(this + ": Velocity with applied Friction: (" + friction.toString() + ")");
			// System.out.println(this + ": ----------------------------------------------------------------------------");
		}
		// Catch the exception.
		catch (Exception e)
		{
			System.out.println(this + ": Adding Friction Force to Body Error. (" + e + ")");
		}
	}

	/**
	 * Get the list of bodies.
	 * 
	 * @return The list of bodies.
	 */
	public ArrayList<Body> getBodies()
	{
		return new ArrayList<Body>(_Bodies);
	}

	/**
	 * Get the gravity.
	 * 
	 * @return The gravity.
	 */
	public double getGravity()
	{
		return _Gravity;
	}

	/**
	 * Set the gravity.
	 * 
	 * @param gravity
	 *            The new gravity.
	 */
	public void setGravity(double gravity)
	{
		_Gravity = gravity;
	}
}