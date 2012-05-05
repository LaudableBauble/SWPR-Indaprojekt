package main;

import infrastructure.Camera2D;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import debug.DebugManager;

/**
 * A scene manager keeps track of all the scenes for a game and switches between them.
 */
public class SceneManager
{
	// The camera.
	private Camera2D _Camera;
	// List of entities.
	private ArrayList<Scene> _Scenes;
	// The current scene.
	private Scene _CurrentScene;

	/**
	 * Constructor for a scene manager.
	 * 
	 * @param camera
	 *            The camera that will be used to view the scenes with.
	 */
	public SceneManager(Camera2D camera)
	{
		initialize(camera);
	}

	/**
	 * Initialize the scene manager.
	 * 
	 * @param camera
	 *            The camera that will be used to view the scenes with.
	 */
	protected void initialize(Camera2D camera)
	{
		// Initialize the variables.
		_Camera = camera;
		_Scenes = new ArrayList<>();
		_CurrentScene = null;
	}

	/**
	 * Load content for the scene manager.
	 */
	public void loadContent()
	{
		// Load the current scene's content.
		if (_CurrentScene != null)
		{
			_CurrentScene.loadContent();
		}
	}

	/**
	 * Handle input.
	 * 
	 * @param input
	 *            The input manager.
	 */
	public void handleInput(InputManager input)
	{
		// Allow the current scene to handle input.
		if (_CurrentScene != null)
		{
			_CurrentScene.handleInput(input);
		}
	}

	/**
	 * Update the scene manager.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Update the current scene.
		if (_CurrentScene != null)
		{
			_CurrentScene.update(gameTime);
		}

		// Update the camera.
		_Camera.update(gameTime);
		// Share the camera matrix with the debug manager.
		DebugManager.getInstance().setTransformMatrix(_Camera.getTransformMatrix());
	}

	/**
	 * Draw the scenes in this manager.
	 * 
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(Graphics2D graphics)
	{
		// Save the old graphics matrix and insert the camera matrix in its place.
		AffineTransform old = graphics.getTransform();
		graphics.setTransform(_Camera.getTransformMatrix());

		// Draw the current scene.
		if (_CurrentScene != null)
		{
			_CurrentScene.draw(graphics);
		}

		// Reinstate the old graphics matrix.
		graphics.setTransform(old);
	}

	/**
	 * Add a scene to the manager.
	 * 
	 * @param scene
	 *            The scene to add.
	 * @return The added scene.
	 */
	public Scene addScene(Scene scene)
	{
		// Add the scene and set its manager.
		_Scenes.add(scene);
		scene.setSceneManager(this);

		// Set the current scene if it has not already.
		if (_CurrentScene == null && _Scenes.size() > 0)
		{
			_CurrentScene = _Scenes.get(0);
		}

		return scene;
	}

	/**
	 * Get the the camera.
	 * 
	 * @return The camera.
	 */
	public Camera2D getCamera()
	{
		return _Camera;
	}

	/**
	 * Get the currently active scene.
	 * 
	 * @return The active scene.
	 */
	public Scene getCurrentScene()
	{
		return _CurrentScene;
	}

	/**
	 * Set the scene to be active.
	 * 
	 * @param index
	 *            The index of the scene to be active.
	 */
	public void setCurrentScene(int index)
	{
		_CurrentScene = _Scenes.get(index);
	}
}