package physics;

import java.awt.Rectangle;

import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A shape is a geometrical form (rectangle) used primarily for collision detection. When used by a body it can interact with the world around it, otherwise it is nothing more than a ghost in the eyes
 * of the engine.
 */
public class Shape
{
	// The width, height and position. Note that position is always where the origin of the shape is.
	private float _Width;
	private float _Depth;
	private float _Height;
	private Vector3 _Position;
	private float _Rotation;
	private Vector2 _Origin;

	/**
	 * Constructor for a shape.
	 * 
	 * @param width
	 *            The width of the shape.
	 * @param height
	 *            The height of the shape.
	 */
	public Shape(float width, float height)
	{
		// Continue down the chain of constructors.
		this(new Vector2(0, 0), width, height);
	}

	/**
	 * Constructor for a shape.
	 * 
	 * @param position
	 *            The position of the shape.
	 * @param width
	 *            The width of the shape.
	 * @param height
	 *            The height of the shape.
	 */
	public Shape(Vector2 position, float width, float height)
	{
		// Initialize the shape.
		initialize(position, width, height);
	}

	/**
	 * Initialize the shape.
	 * 
	 * @param position
	 *            The position of the shape.
	 * @param width
	 *            The width of the shape.
	 * @param height
	 *            The height of the shape.
	 */
	protected void initialize(Vector2 position, float width, float height)
	{
		// Initialize some variables.
		_Position = new Vector3(position);
		_Rotation = 0;
		_Width = width;
		_Height = height;

		// Update the origin.
		_Origin = getCenter();
	}

	/**
	 * See if a shape intersects or collides with this shape.
	 * 
	 * @param shape
	 *            The other shape to check collisions with.
	 * @return Whether the two shapes intersect each other.
	 */
	public boolean intersects(Shape shape)
	{
		// First check collisions for ground box, ie. ignore height/depth dimension for now.
		// If there exists a collision, continue to see if the height/depth position of the two shapes also match.
		// If that is also true, then we have a collision.

		// Create the two base rectangles.
		Rectangle rect1 = new Rectangle((int) shape.getPosition().x, (int) shape.getPosition().y, (int) shape.getWidth(), (int) shape.getHeight());
		Rectangle rect2 = new Rectangle((int) _Position.x, (int) _Position.y, (int) _Width, (int) _Height);

		// If they intersect, continue.
		if (rect1.intersects(rect2)) { return true; }

		// No intersection at this point.
		return false;
	}

	/**
	 * Calculates an intersection rectangle from between the given shape and this instance. If no intersection exists null is returned.
	 * 
	 * @param shape
	 *            The shape to get an intersection from.
	 * @return The intersection rectangle.
	 */
	public Rectangle getIntersection(Shape shape)
	{
		// Create the two base rectangles.
		Rectangle rect1 = new Rectangle((int) shape.getPosition().x, (int) shape.getPosition().y, (int) shape.getWidth(), (int) shape.getHeight());
		Rectangle rect2 = new Rectangle((int) _Position.x, (int) _Position.y, (int) _Width, (int) _Height);

		// Return the intersection rectangle.
		return rect1.intersection(rect2);
	}

	/**
	 * Set the shape's width.
	 * 
	 * @param width
	 *            The new width.
	 */
	public void setWidth(float width)
	{
		_Width = width;
	}

	/**
	 * Get the shape's height.
	 */
	public float getHeight()
	{
		return _Height;
	}

	/**
	 * Get the shape's width.
	 */
	public float getWidth()
	{
		return _Width;
	}

	/**
	 * Set the shape's height.
	 * 
	 * @param height
	 *            The new height.
	 */
	public void setHeight(float height)
	{
		_Height = height;
	}

	/**
	 * Get the shape's position.
	 */
	public Vector3 getPosition()
	{
		return _Position;
	}

	/**
	 * Get the shape's layered 2D-position, ie. only the x and y-coordinate.
	 */
	public Vector2 getLayeredPosition()
	{
		return _Position.vector2();
	}

	/**
	 * Set the shape's position, ie. where the origin will be at.
	 * 
	 * @param position
	 *            The new position.
	 */
	public void setPosition(Vector3 position)
	{
		_Position = position;
	}

	/**
	 * Set the shape's layered 2D-position, ie. only the x and y-coordinate. The z-coordinate is kept.
	 * 
	 * @param position
	 *            The new position.
	 */
	public void setLayeredPosition(Vector2 position)
	{
		_Position = new Vector3(position.x, position.y, _Position.z);
	}

	/**
	 * Get the center of the shape.
	 */
	public Vector2 getCenter()
	{
		return new Vector2(_Width / 2, _Height / 2);
	}

	/**
	 * Get the axes of this shape, ie. the normals of each edge. Uses clockwise ordering.
	 * 
	 * @return An array of axes.
	 */
	public Vector2[] getAxes()
	{
		// Note that because of parallel edges in a rectangle only two edges have to be returned.
		return new Vector2[] { Vector2.subtract(getTopRight(), getTopLeft()).perpendicular().normalize(),
				Vector2.subtract(getBottomRight(), getTopRight()).perpendicular().normalize() };
	}

	/**
	 * Get the vertices of this shape. Uses clockwise ordering.
	 * 
	 * @return An array of vertices.
	 */
	public Vector2[] getVertices()
	{
		return new Vector2[] { getTopLeft(), getTopRight(), getBottomRight(), getBottomLeft() };
	}

	/**
	 * Project the shape onto the axis and return the end points of the resulting line. NOTE: The axis must be normalized to get accurate projections.
	 * 
	 * @param axis
	 *            The axis to project upon.
	 * @return A line in one-dimensional space.
	 */
	public Vector2 project(Vector2 axis)
	{
		// Set up the end points of the projection.
		double min = axis.dot(getTopLeft());
		double max = min;

		// Iterate through every vertex in the shape.
		for (Vector2 v : getVertices())
		{
			// Get the dot product.
			double p = axis.dot(v);

			// See if any new end points have emerged.
			if (p < min)
			{
				min = p;
			}
			else if (p > max)
			{
				max = p;
			}
		}

		// Return the projection.
		return new Vector2(min, max);
	}

	/**
	 * Get the shape's form at the given height value. Null is returned if the shape do not occupy a space at that height.
	 * 
	 * @param z
	 *            The z-coordinate or height.
	 * @return The shape's form at the given height.
	 */
	public Shape getLayeredShape(double z)
	{
		return this;
	}

	/**
	 * Get the position of the top-left corner of the shape, acknowledging rotation.
	 * 
	 * @return The position of the top-left corner.
	 */
	public Vector2 getTopLeft()
	{
		Vector2 topLeft = Helper.toTopLeft(this);
		return Helper.rotateVector(topLeft, Vector2.add(topLeft, _Origin), _Rotation);
	}

	/**
	 * Get the position of the top-right corner of the shape, acknowledging rotation.
	 * 
	 * @return The position of the top-right corner.
	 */
	public Vector2 getTopRight()
	{
		Vector2 topRight = Helper.toTopRight(this);
		return Helper.rotateVector(topRight, Vector2.add(topRight, new Vector2(-_Origin.x, _Origin.y)), _Rotation);
	}

	/**
	 * Get the position of the bottom-left corner of the shape, acknowledging rotation.
	 * 
	 * @return The position of the bottom-left corner.
	 */
	public Vector2 getBottomLeft()
	{
		Vector2 bottomLeft = Helper.toBottomLeft(this);
		return Helper.rotateVector(bottomLeft, Vector2.add(bottomLeft, new Vector2(_Origin.x, -_Origin.y)), _Rotation);
	}

	/**
	 * Get the position of the bottom-right corner of the shape, acknowledging rotation.
	 * 
	 * @return The position of the bottom-right corner.
	 */
	public Vector2 getBottomRight()
	{
		Vector2 bottomRight = Helper.toBottomRight(this);
		return Helper.rotateVector(bottomRight, Vector2.add(bottomRight, new Vector2(-_Origin.x, -_Origin.y)), _Rotation);
	}

	/**
	 * Get the position (height) of the shape's top-edge, not acknowledging rotation.
	 * 
	 * @return The position (height) of the shape's top-edge.
	 */
	public double getTopHeight()
	{
		return (_Position.z + (_Height / 2));
	}

	/**
	 * Get the position (height) of the shape's bottom-edge, not acknowledging rotation.
	 * 
	 * @return The position (height) of the shape's bottom-edge.
	 */
	public double getBottomHeight()
	{
		return (_Position.z - (_Height / 2));
	}
}
