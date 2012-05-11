package infrastructure;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * A camera used for viewing 2D on screen.
 */
public class Camera2D
{
	// Fields.
	private Vector2 _Position;
	private float _ZoomValue;
	private float _Rotation;
	private Vector2 _Origin;
	private float _CameraSpeed;
	private float _MaxZoom;
	private float _MinZoom;
	private Rectangle _Viewport;
	private Rectangle _WorldRect;
	private AffineTransform _Transform;

	/**
	 * Create the camera from which eyes the player will look upon the game world.
	 * 
	 * @param viewport
	 *            The viewport that the camera will cover.
	 * @param world
	 *            The game world's outer bounds.
	 */
	public Camera2D(Vector2 viewport, Vector2 world)
	{
		// Initialize the camera.
		initialize(new Rectangle(0, 0, (int) viewport.x, (int) viewport.y), new Rectangle(0, 0, (int) world.x, (int) world.y));
	}

	/**
	 * Initialize the camera.
	 * 
	 * @param viewport
	 *            The viewport that the camera will cover.
	 * @param world
	 *            The game world's outer bounds.
	 */
	private void initialize(Rectangle viewport, Rectangle world)
	{
		// Initialize stuff.
		_Viewport = viewport;
		_WorldRect = world;
		_Transform = new AffineTransform();
		_CameraSpeed = 10f;
		_Rotation = 0.0f;
		_Position = new Vector2(0, 0);
		_Origin = new Vector2(_Viewport.getWidth() / 2, _Viewport.getHeight() / 2);
		_MaxZoom = .6f;
		_MinZoom = 2;

		// Set the camera's zoom.
		zoom(1f);
	}

	/**
	 * Update the camera.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Update the camera's position, so that it keeps within bounds.
		clamp();

		// Transform the matrix accordingly.
		updateCamera();
	}

	/**
	 * Move the camera.
	 * 
	 * @param amount
	 *            The amount of movement.
	 */
	public void moveAmount(Vector2 amount)
	{
		// Set the position.
		_Position = Helper.constrainMovement(_Position, amount, new Vector2(_CameraSpeed, _CameraSpeed));
		// Clamp the position.
		clamp();
		// Update the matrix.
		updateCamera();
	}

	/**
	 * Move the camera.
	 * 
	 * @param direction
	 *            The direction of the movement.
	 */
	public void move(Vector2 direction)
	{
		// Set the position.
		moveAmount(Vector2.multiply(direction, _CameraSpeed));
	}

	/**
	 * Move the camera.
	 * 
	 * @param destination
	 *            The destination to move to.
	 */
	public void moveTo(Vector2 destination)
	{
		// If the distance to the destination is too short, stop here.
		if (Vector2.getDistance(_Position, destination) < 100) { return; }

		// Get the direction.
		Vector2 direction = Vector2.subtract(destination, _Position).normalize();

		// Set the position.
		moveAmount(Vector2.multiply(direction, 2));
	}

	/**
	 * Rotate the camera.
	 * 
	 * @param angle
	 *            The angle of the rotation in radians.
	 */
	public void rotate(float angle)
	{
		_Rotation += angle;
	}

	/**
	 * Zoom the camera.
	 * 
	 * @param amount
	 *            The amount to zoom in/out.
	 */
	public void zoom(float amount)
	{
		// Zoom in accordingly.
		_ZoomValue += amount;
		// Make sure that the zoom will be within set range.
		_ZoomValue = Helper.clamp(_ZoomValue, _MaxZoom, _MinZoom);
	}

	/**
	 * Get the camera's transformation matrix. This also updates the matrix.
	 * 
	 * @return The camera's transformation matrix.
	 */
	public AffineTransform getTransformMatrix()
	{
		_Transform.setToIdentity();
		_Transform.rotate(_Rotation);
		_Transform.scale(_ZoomValue, _ZoomValue);
		_Transform.translate(-_Position.x, -_Position.y);
		_Transform.translate(_Origin.x / _ZoomValue, _Origin.y / _ZoomValue);

		// Return the matrix.
		return _Transform;
	}

	/**
	 * Update the camera's position, zoom and rotation.
	 * 
	 * @param position
	 *            The new position of the camera.
	 * @param zoomValue
	 *            The new zoom value of the camera.
	 * @param rotation
	 *            The new rotation of the camera.
	 */
	private void updateCamera(Vector2 position, float zoomValue, float rotation)
	{
		// Set the camera's position, zoom and rotation.
		_Position = position;
		_ZoomValue = zoomValue;
		_Rotation = rotation;

		// Update the transformation matrix.
		updateCamera();
	}

	/**
	 * Update the camera's transformation matrix.
	 */
	private void updateCamera()
	{
		// Update the transformation matrix.
		getTransformMatrix();
	}

	/**
	 * Clamp the camera within the viewport.
	 */
	private void clamp()
	{
		// Update the camera's position, so that it keeps within bounds.
		if (_Position.x < ((_Viewport.getMinX() + _Origin.x) / _ZoomValue))
		{
			_Position.x = (_Viewport.getMinX() + _Origin.x) / _ZoomValue;
		}
		if (_Position.y < ((_Viewport.getMinY() + _Origin.y) / _ZoomValue))
		{
			_Position.y = (_Viewport.getMinY() + _Origin.y) / _ZoomValue;
		}
		if (_Position.x > (_WorldRect.getWidth() - ((_Viewport.getMaxX() - _Origin.x) / _ZoomValue)))
		{
			_Position.x = _WorldRect.getWidth() - ((_Viewport.getMaxX() - _Origin.x) / _ZoomValue);
		}
		if (_Position.y > (_WorldRect.getHeight() - ((_Viewport.getMaxY() - _Origin.y) / _ZoomValue)))
		{
			_Position.y = _WorldRect.getHeight() - ((_Viewport.getMaxY() - _Origin.y) / _ZoomValue);
		}
	}

	/**
	 * Convert screen coordinates to world coordinates.
	 * 
	 * @param location
	 *            The location on the screen.
	 * @return The location in world coordinates.
	 */
	public Vector2 convertScreenToWorld(Vector2 location)
	{
		Vector3 t = new Vector3(location, 0);

		// t = _graphics.Viewport.Unproject(t, _projection, _view, Matrix.Identity);

		// return new Vector2(t.X, t.Y);
		// The inverted transformation matrix.
		AffineTransform inverse = new AffineTransform(_Transform);
		try
		{
			inverse.invert();
		}
		catch (Exception e)
		{
		}

		// Transform the camera vector and return the converted coordinates.
		return Vector2.transform(location, inverse);
	}

	/**
	 * Convert world coordinates to screen coordinates.
	 * 
	 * @param location
	 *            The location in the world.
	 * @return The location on the screen.
	 */
	public Vector2 convertWorldToScreen(Vector2 location)
	{
		Vector3 t = new Vector3(location, 0);

		// t = _graphics.Viewport.Project(t, _projection, _view, Matrix.Identity);

		return new Vector2(t.x, t.y);
	}

	/**
	 * Get the camera viewport's position.
	 * 
	 * @return The viewport's position.
	 */
	public Vector2 getPosition()
	{
		return _Position;
	}

	/**
	 * Set the camera viewport's position.
	 * 
	 * @param position
	 *            The viewport's position.
	 */
	public void setPosition(Vector2 position)
	{
		updateCamera(position, _ZoomValue, _Rotation);
	}

	/**
	 * Get the camera's viewport.
	 * 
	 * @return The viewport.
	 */
	public Vector2 getViewportSize()
	{
		return new Vector2(_Viewport.width, _Viewport.height);
	}
}
