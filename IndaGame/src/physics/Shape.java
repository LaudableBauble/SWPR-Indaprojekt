package physics;

import infrastructure.Enums.DepthDistribution;

import java.awt.Rectangle;

import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A shape is a geometrical form used primarily for collision detection. When used by a body it can interact with the world around it, otherwise it is nothing more than a ghost in the eyes of the
 * engine. Currently only supports rectangular boxes and triangles with rectangular bases.
 */
public class Shape
{
	// The width, height and position. Note that position is always where the origin of the shape is.
	private float _Width;
	private float _Height;
	private float _Depth;
	private Vector3 _Position;
	private float _Rotation;
	private Vector2 _Origin;
	private DepthDistribution _DepthDistribution;

	/**
	 * Constructor for a shape.
	 * 
	 * @param width
	 *            The width of the shape (x-coordinate).
	 * @param height
	 *            The height of the shape (y-coordinate).
	 * @param depth
	 *            The depth of the shape (z-coordinate).
	 */
	public Shape(float width, float height, float depth)
	{
		// Continue down the chain of constructors.
		this(Vector3.empty(), width, height, depth);
	}

	/**
	 * Constructor for a shape.
	 * 
	 * @param position
	 *            The position of the shape.
	 * @param width
	 *            The width of the shape (x-coordinate).
	 * @param height
	 *            The height of the shape (y-coordinate).
	 * @param depth
	 *            The depth of the shape (z-coordinate).
	 */
	public Shape(Vector3 position, float width, float height, float depth)
	{
		// Initialize the shape.
		initialize(position, width, height, depth);
	}

	/**
	 * Initialize the shape.
	 * 
	 * @param position
	 *            The position of the shape.
	 * @param width
	 *            The width of the shape (x-coordinate).
	 * @param height
	 *            The height of the shape (y-coordinate).
	 * @param depth
	 *            The depth of the shape (z-coordinate).
	 */
	protected void initialize(Vector3 position, float width, float height, float depth)
	{
		// Initialize some variables.
		_Position = position;
		_Rotation = 0;
		_Width = width;
		_Height = height;
		_Depth = depth;
		_DepthDistribution = DepthDistribution.Uniform;

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
	 * Compare two shapes by their depth values. Allows margin overlap, ie. same end-point position.
	 * 
	 * @return 1 if the first shape is located 'higher', -1 if the second shape is 'located' higher and 0 if the shapes overlap.
	 */
	public static int isOverlaping(Shape s1, Shape s2)
	{
		// The entities' depth data.
		Vector2 v1 = new Vector2(s1.getBottomDepth(), Math.min(s1.getTopDepth(s1.getLayeredPosition()), s1.getTopDepth(s2.getLayeredPosition())));
		Vector2 v2 = new Vector2(s2.getBottomDepth(), Math.min(s2.getTopDepth(s1.getLayeredPosition()), s2.getTopDepth(s2.getLayeredPosition())));

		// Compare the shapes to each other.
		if (!v1.overlap(v2, false))
		{
			if (s1.getPosition().z < s2.getPosition().z)
			{
				return -1;
			}
			else if (s1.getPosition().z > s2.getPosition().z) { return 1; }
		}

		// The shapes' overlap, return 0.
		return 0;
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
		// Update the origin.
		_Origin = getCenter();
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
		// Update the origin.
		_Origin = getCenter();
	}

	/**
	 * Get the shape's depth.
	 */
	public float getDepth()
	{
		return _Depth;
	}

	/**
	 * Set the shape's depth.
	 * 
	 * @param depth
	 *            The new depth.
	 */
	public void setDepth(float depth)
	{
		_Depth = depth;
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
	 * Set the shape's position.
	 * 
	 * @param position
	 *            The new position.
	 */
	public void setPosition(Vector3 position)
	{
		_Position = position;
	}

	/**
	 * Set the shape's bottom position.
	 * 
	 * @param position
	 *            The new bottom position.
	 */
	public void setBottomPosition(Vector3 position)
	{
		_Position = new Vector3(position.toVector2(), position.z + getDepth() / 2);
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
	 * Set the shape's rotation in radians.
	 * 
	 * @param rotation
	 *            The new rotation.
	 */
	public void setRotation(float rotation)
	{
		_Rotation = rotation;
	}

	/**
	 * Get the center of the shape.
	 */
	public Vector2 getCenter()
	{
		return new Vector2(_Width / 2, _Height / 2);
	}

	/**
	 * Get the axes of this layered shape, ie. the normals of each edge, given a certain depth. Uses clockwise ordering.
	 * 
	 * @return An array of axes.
	 */
	public Vector2[] getAxes(double z)
	{
		return getLayeredShape(z).getAxes();
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
	 * Get the position (z + depth / 2) of the shape's top-edge, not acknowledging rotation.
	 * 
	 * @return The position (depth) of the shape's top-edge.
	 */
	public double getTopDepth()
	{
		return (_Position.z + (_Depth / 2));
	}

	/**
	 * Get the position (z + depth / 2) of the shape's top-edge, not acknowledging rotation, at a given layered position.
	 * 
	 * @param layeredPosition
	 *            The layered position to find the depth for.
	 * 
	 * @return The position (depth) of the shape's top-edge. Either the top depth or bottom depth if the shape does not occupy the space at the given position, depending on direction.
	 */
	public double getTopDepth(Vector2 layeredPosition)
	{
		// The depth to add.
		double depth;
		// The amount of the top step. Used to enable characters to reach the top of the slope without colliding with adjacent bodies.
		double amount = 5;

		// Check the depth distribution. Shorten the sloped shape to fix the issue with characters not reaching the top height of the slope due to collisions with adjacent bodies.
		switch (_DepthDistribution)
		{
			case Top:
			{
				// Calculate the depth.
				depth = ((_Position.y + (_Height / 2)) - layeredPosition.y) * (_Depth / (_Height - amount));
				break;
			}
			case Bottom:
			{
				// Calculate the depth.
				depth = (layeredPosition.y - (_Position.y - (_Height / 2))) * (_Depth / (_Height - amount));
				break;
			}
			case Right:
			{
				// Calculate the depth.
				depth = (layeredPosition.x - (_Position.x - (_Width / 2))) * (_Depth / (_Width - amount));
				break;
			}
			case Left:
			{
				// Calculate the depth.
				depth = ((_Position.x + (_Width / 2)) - layeredPosition.x) * (_Depth / (_Width - amount));
				break;
			}
			default:
			{
				// Calculate the depth.
				depth = _Depth;
				break;
			}
		}

		return (_Position.z - (_Depth / 2) + Math.min(Math.max(depth, 0), _Depth));
	}

	/**
	 * Get a layer from this shape given a z-coordinate.
	 * 
	 * @param z
	 *            The z-coordinate to get a layered shape from.
	 * @return The layered shape.
	 */
	public Shape getLayeredShape(double z)
	{
		// The depth.
		double depth = z - getBottomDepth();

		// If the depth provided does not stay within the shape's bounds, stop here.
		if (depth < 0 || depth > getTopDepth()) { throw new IllegalArgumentException(); }

		// Check the depth distribution.
		switch (_DepthDistribution)
		{
			case Top:
			{
				// Get the ratio between height and depth.
				double ratio = _Height / _Depth;

				// Get the amount of height to remove and calculate the new position.
				double height = depth * ratio;
				double y = _Position.y - (_Height / 2) + height + ((_Height - height) / 2);

				// Return the layered shape.
				return new Shape(new Vector3(_Position.x, y, z), _Width, _Height - (float) height, 1f);
			}
			case Bottom:
			{
				// Get the ratio between height and depth.
				double ratio = _Height / _Depth;

				// Get the amount of height to remove and calculate the new position.
				double height = depth * ratio;
				double y = _Position.y + (_Height / 2) - height - ((_Height - height) / 2);

				// Return the layered shape.
				return new Shape(new Vector3(_Position.x, y, z), _Width, _Height - (float) height, 1f);
			}
			case Right:
			{
				// Get the ratio between width and depth.
				double ratio = _Width / _Depth;

				// Get the amount of width to remove and calculate the new position.
				double width = depth * ratio;
				double x = _Position.x - (_Width / 2) + width + ((_Width - width) / 2);

				// Return the layered shape.
				return new Shape(new Vector3(x, _Position.y, z), _Width - (float) width, _Height, 1f);
			}
			case Left:
			{
				// Get the ratio between width and depth.
				double ratio = _Width / _Depth;

				// Get the amount of width to remove and calculate the new position.
				double width = depth * ratio;
				double x = _Position.x + (_Width / 2) - width - ((_Width - width) / 2);

				// Return the layered shape.
				return new Shape(new Vector3(x, _Position.y, z), _Width - (float) width, _Height, 1f);
			}
			default:
			{
				// Uniform depth distribution.
				return new Shape(new Vector3(_Position.toVector2(), z), _Width, _Height, 1f);
			}
		}
	}

	/**
	 * Get the position (z - depth / 2) of the shape's bottom-edge, not acknowledging rotation. Assumes the shape is rectangular.
	 * 
	 * @return The position (depth) of the shape's bottom-edge.
	 */
	public double getBottomDepth()
	{
		return (_Position.z - (_Depth / 2));
	}

	/**
	 * Set the position (z + depth / 2) of the shape's bottom-edge, not acknowledging rotation.
	 * 
	 * @param z
	 *            The position (depth) of the shape's bottom-edge.
	 */
	public void setBottomDepth(double z)
	{
		_Position.setZ(z + (_Depth / 2));
	}

	/**
	 * Get the shape's depth distribution.
	 * 
	 * @return The shape's depth distribution model.
	 */
	public DepthDistribution getDepthDistribution()
	{
		return _DepthDistribution;
	}

	/**
	 * Set the shape's depth distribution.
	 * 
	 * @param distribution
	 *            The new depth distribution model.
	 */
	public void setDepthDistribution(DepthDistribution distribution)
	{
		_DepthDistribution = distribution;
	}
}
