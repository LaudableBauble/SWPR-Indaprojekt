package debug;

import input.InputManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import physics.Body;
import physics.PhysicsSimulator;
import physics.Shape;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * The debug manager handles various debugging tasks, such as drawing the shape of each body.
 */
public class DebugManager
{
	// The singleton debug instance.
	private static DebugManager _Instance;

	// The physics simulator.
	private PhysicsSimulator _Physics;
	// The drawing matrix.
	private AffineTransform _Transform;

	// The time each phase takes.
	private long startTime;
	private long phaseStartTime;
	private long phaseEndTime;
	private long[] phaseTime;

	// The Debug Boolean.
	public boolean debug;

	// Debug Variables!!!
	private int forcesToAdd;
	private Body debugBody;

	/**
	 * Private constructor for a debug manager.
	 */
	private DebugManager()
	{
		initialize();
	}

	/**
	 * Initialize the debug manager.
	 */
	private void initialize()
	{
		// Initialize the time counters.
		startTime = System.currentTimeMillis();
		setPhaseStartTime();
		phaseEndTime = 0;
		phaseTime = new long[4];
		// Disable the debug.
		debug = false;
		// Initialize the debugBody variable.
		debugBody = new Body();
		_Transform = new AffineTransform();
	}

	/**
	 * Handle input.
	 * 
	 * @param input
	 *            The input manager.
	 */
	public void handleInput(InputManager input)
	{
		// Turn on/off debug mode.
		if (input.isKeyDown(KeyEvent.VK_ENTER))
		{
			debug = !debug;
		}

		// If debug mode is not on, stop here.
		if (!debug) { return; }

		// If no physics simulator has been set, exit here.
		if (_Physics == null) { return; }

		// Increase the gravity.
		if (input.isKeyDown(KeyEvent.VK_F))
		{
			_Physics.setGravity(_Physics.getGravity() + 0.01);
		}
		// Decrease the gravity.
		if (input.isKeyDown(KeyEvent.VK_D))
		{
			_Physics.setGravity(_Physics.getGravity() - 0.01);
		}

		// Increase the impact energyDecrease.
		if (input.isKeyDown(KeyEvent.VK_H))
		{
			_Physics.energyDecrease += 0.01;
		}
		// Decrease the impact energyDecrease.
		if (input.isKeyDown(KeyEvent.VK_G))
		{
			_Physics.energyDecrease -= 0.01;
		}

		// Increase the Body's mass.
		if (input.isKeyDown(KeyEvent.VK_S))
		{
			debugBody.setMass(debugBody.getMass() + 0.25);
		}
		// Decrease the Body's mass.
		if (input.isKeyDown(KeyEvent.VK_A))
		{
			debugBody.setMass(debugBody.getMass() - 0.25);
		}

		// Increase the Body's frictionCoefficient.
		if (input.isKeyDown(KeyEvent.VK_N))
		{
			debugBody.setFrictionCoefficient(debugBody.getFrictionCoefficient() + 0.025);
		}
		// Decrease the Body's frictionCoefficient.
		if (input.isKeyDown(KeyEvent.VK_B))
		{
			debugBody.setFrictionCoefficient(debugBody.getFrictionCoefficient() - 0.025);
		}

		// Increase the Body's maxVelocity.
		if (input.isKeyDown(KeyEvent.VK_V))
		{
			debugBody.setMaxVelocity(debugBody.getMaxVelocity() + 0.25);
		}
		// Decrease the Body's maxVelocity.
		if (input.isKeyDown(KeyEvent.VK_C))
		{
			debugBody.setMaxVelocity(debugBody.getMaxVelocity() - 0.25);
		}

		// Increase the Body's accelerationValue.
		if (input.isKeyDown(KeyEvent.VK_X))
		{
			debugBody.setAccelerationValue(debugBody.getAccelerationValue() + 0.025);
		}
		// Decrease the Body's accelerationValue.
		if (input.isKeyDown(KeyEvent.VK_Z))
		{
			debugBody.setAccelerationValue(debugBody.getAccelerationValue() - 0.025);
		}
	}

	/**
	 * Draw stuff.
	 * 
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(Graphics2D graphics)
	{
		// Check if debug is enabled, otherwise stop.
		if (!debug) { return; }
		// If no physics simulator has been set, stop.
		if (_Physics == null) { return; }

		// Save the old color.
		Color old = graphics.getColor();

		// Loop through all bodies.
		for (Body b : _Physics.getBodies())
		{
			// Check if the array isn't empty.
			try
			{
				// Change the color for the debug window.
				graphics.setColor(Color.red);

				// If the body is in a collision, use a blue color instead.
				if (b.getCollisions().size() > 0)
				{
					graphics.setColor(Color.blue);
				}

				// Save the old graphics matrix and insert the camera matrix in its place.
				AffineTransform oldMatrix = graphics.getTransform();
				graphics.setTransform(_Transform);

				// The body's shape and position on screen.
				Shape shape = b.getShape().getLayeredShape(debugBody.getShape().getBottomDepth());
				Vector2 position = Helper.getScreenPosition(shape.getPosition());

				// Draw the body's shape.
				graphics.drawRect((int) (position.x - shape.getWidth() / 2), (int) (position.y - shape.getHeight() / 2), (int) shape.getWidth(), (int) shape.getHeight());

				// Change the color for the debug window.
				graphics.setColor(Color.black);

				// Draw the body's position and velocity.
				graphics.drawString("P: " + Vector3.round(b.getPosition(), 0).toString() + " - V: " + Vector3.round(b.getVelocity(), 0).toString(), (int) position.x,
						(int) position.y - 2);

				// Reinstate the old graphics matrix.
				graphics.setTransform(oldMatrix);
			}
			// Catch the exception.
			catch (Exception e)
			{
				// If there was a argument exception error, do not bother.
				if (e.getClass() != IllegalArgumentException.class)
				{
					System.out.println(this + ": Draw Error. (" + e + ")");
				}
			}

			// If the body's debug variable is true.
			if (b._IsClicked)
			{
				// If a new body has been clicked.
				if (debugBody != b)
				{
					// Try.
					try
					{
						debugBody._IsClicked = false;
						debugBody = b;
					}
					// Catch the possible exception.
					catch (Exception e)
					{
						System.out.println(this + ": Debug Body Error. (" + e + ")");
					}
				}
			}
		}
		
		//Reset the graphics matrix.
		graphics.setTransform(new AffineTransform());

		// Change the color for the debug window.
		graphics.setColor(Color.lightGray);
		// Draw a debug window.
		graphics.fillRect(0, 0, 130, 480);

		// Change the color back to black.
		graphics.setColor(Color.black);

		// Display the gravity.
		graphics.drawString("DF, Gravity: " + Vector2.round(_Physics.getGravity(), 1), 2, 20);
		// Display the number of bodies.
		graphics.drawString("Bodies: " + _Physics.bodyCount(), 2, 35);
		// Display the number of forces to add.
		graphics.drawString("Forces: " + forcesToAdd, 2, 50);
		// Display the energyDecrease at each collision.
		graphics.drawString("GH, Impact ED: " + Vector2.round(_Physics.energyDecrease, 1), 2, 65);

		// Display the used time.
		graphics.drawString("Input: " + phaseTime[0] + " ms", 2, 95);
		graphics.drawString("Physics: " + phaseTime[1] + " ms", 2, 110);
		graphics.drawString("Body: " + phaseTime[2] + " ms", 2, 125);
		graphics.drawString("Draw: " + phaseTime[3] + " ms", 2, 140);
		graphics.drawString("Total: " + phaseEndTime + " ms", 2, 155);

		// Draw the Debug Body's information.
		if (debugBody != null)
		{
			// Draw the information.
			graphics.drawString("------- Body -------", 2, 170);
			graphics.drawString("Pos: " + Vector2.round(debugBody.getLayeredPosition(), 0).toString(), 2, 185);
			graphics.drawString("Velocity: " + Vector3.round(debugBody.getVelocity(), 0).toString(), 2, 200);
			graphics.drawString("AS, Mass: " + Math.round(debugBody.getMass()), 2, 215);
			graphics.drawString("NB, FrictionCoe: " + Math.round(debugBody.getFrictionCoefficient()), 2, 230);
			graphics.drawString("Width: " + debugBody.getShape().getWidth(), 2, 245);
			graphics.drawString("Height: " + debugBody.getShape().getHeight(), 2, 260);
			graphics.drawString("VC, Max Velocity: " + Vector2.round(debugBody.getMaxVelocity(), 1), 2, 275);
			graphics.drawString("ZX, Acc Value: " + Vector2.round(debugBody.getAccelerationValue(), 1), 2, 290);
		}

		// Draw the time since game start in milliseconds.
		graphics.drawString("Time: " + (System.currentTimeMillis() - startTime) + " ms", 2, 400);

		// Draw the FPS.
		if (phaseEndTime > 0)
		{
			graphics.drawString(String.valueOf(1000 / phaseEndTime) + " FPS", 2, 415);
		}
		// Draw a blank.
		else
		{
			graphics.drawString("--- FPS", 0, 415);
		}

		// Let the player know that the magic happens with ENTER.
		graphics.drawString("Press ENTER to toggle", 2, 460);

		// Revert to the old color.
		graphics.setColor(old);
	}

	/**
	 * Get the singleton instance of this debug manager.
	 * 
	 * @return The singleton debug manager.
	 */
	public static DebugManager getInstance()
	{
		// If the debug manager has not been created yet, do so.
		if (_Instance == null)
		{
			_Instance = new DebugManager();
		}

		// Return the instance.
		return _Instance;
	}

	// Set the startTime.
	public void setPhaseStartTime()
	{
		// Set the start time.
		phaseStartTime = System.currentTimeMillis();
	}

	// Set the endTime.
	public void setPhaseEndTime()
	{
		// Set the end time.
		phaseEndTime = (System.currentTimeMillis() - phaseStartTime);
	}

	// Set a time for one of the phaseTime Counters.
	public void setPhaseTime(int index)
	{
		// Set the time to the correct index and subtract the start time.
		phaseTime[index] = (System.currentTimeMillis() - phaseStartTime);

		// Calculate the time difference between this phase and the others.
		if (index != 0)
		{
			// Loop through all phases up to this point.
			for (int phase = 0; phase < index; phase++)
			{
				// Subtract the phase time.
				phaseTime[index] -= phaseTime[phase];
			}
		}
	}

	/**
	 * Set the physics simulator to follow.
	 * 
	 * @param physics
	 *            The physics simulator.
	 */
	public void setPhysicsSimulator(PhysicsSimulator physics)
	{
		_Physics = physics;
	}

	/**
	 * Set the drawing matrix to use.
	 * 
	 * @param matrix
	 *            The drawing matrix to use.
	 */
	public void setTransformMatrix(AffineTransform matrix)
	{
		_Transform = matrix;
	}

	/**
	 * Set the debug body.
	 * 
	 * @param body
	 *            The new debug body.
	 */
	public void setDebugBody(Body body)
	{
		debugBody = body;
	}

	/**
	 * Get the debug body.
	 * 
	 * @return The debug body.
	 */
	public Body getDebugBody()
	{
		return debugBody;
	}
}