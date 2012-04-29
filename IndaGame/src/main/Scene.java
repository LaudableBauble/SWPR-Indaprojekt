package main;

import graphics.DepthComposite;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import auxillary.Vector2;

import physics.PhysicsSimulator;
import debug.DebugManager;

/**
 * A scene is a map of the game world and can be populated by entities.
 */
public class Scene
{
	// The scene manager.
	SceneManager _SceneManager;
	// The physics simulator.
	private PhysicsSimulator _Physics;
	// List of entities.
	private ArrayList<Entity> _Entities;
	// The composite z-buffer.
	private DepthComposite _Composite;

	/**
	 * Empty constructor for a scene.
	 */
	public Scene()
	{
		initialize(null);
	}

	/**
	 * Constructor for a scene.
	 * 
	 * @param manager
	 *            The scene manager this scene is part of.
	 */
	public Scene(SceneManager manager)
	{
		initialize(manager);
	}

	/**
	 * Initialize the scene.
	 * 
	 * @param manager
	 *            The scene manager this scene is part of.
	 */
	protected void initialize(SceneManager manager)
	{
		// Initialize the variables.
		_SceneManager = manager;
		_Entities = new ArrayList<>();
		_Physics = new PhysicsSimulator();
		_Composite = new DepthComposite(_SceneManager.getCamera().getViewportSize());

		// Let the debug manager know about the physics simulator.
		DebugManager.getInstance().setPhysicsSimulator(_Physics);
	}

	/**
	 * Load content.
	 */
	public void loadContent()
	{
		// Load all entities' content.

	}

	/**
	 * Handle input.
	 * 
	 * @param input
	 *            The input manager.
	 */
	public void handleInput(InputManager input)
	{
		// Let all entities respond to input.
		for (Entity entity : _Entities)
		{
			entity.handleInput(input);
		}
	}

	/**
	 * Update the entity.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Update the physics simulator.
		_Physics.update();

		// Sort the list of entities by descending depth.
		// Collections.sort(_Entities, new EntityDepthComparator());

		// Update all entities.
		for (Entity entity : _Entities)
		{
			entity.update(gameTime);
		}
	}

	/**
	 * Draw the entity.
	 * 
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(Graphics2D graphics)
	{
		// Enable depth sorting by composite.
		graphics.setComposite(_Composite);

		// Draw all entities.
		for (Entity entity : _Entities)
		{
			// Prepare the graphics device for depth-sorting.
			((DepthComposite) graphics.getComposite()).setEntity(entity);
			entity.draw(graphics);
		}

		// Notify the depth composite that the frame has ended, at least for the scene.
		_Composite.endFrame();
	}

	/**
	 * Add an entity to the scene. Assumes that its physics simulator already has been set and notified of its existence.
	 * 
	 * @param entity
	 *            The entity to add.
	 * @return The added entity.
	 */
	public Entity addEntity(Entity entity)
	{
		_Entities.add(entity);
		return entity;
	}

	/**
	 * Remove an entity from the scene. This also removes the entity's body from the physics simulator.
	 * 
	 * @param entity
	 *            The entity to remove.
	 */
	public void removeEntity(Entity entity)
	{
		_Entities.remove(entity);
		_Physics.removeBody(entity.getBody());
	}

	/**
	 * Get the scene's physics simulator.
	 * 
	 * @return The scene's physics simulator.
	 */
	public PhysicsSimulator getPhysicsSimulator()
	{
		return _Physics;
	}

	/**
	 * Set the scene's manager.
	 * 
	 * @param manager
	 *            The new manager.
	 */
	public void setSceneManager(SceneManager manager)
	{
		_SceneManager = manager;
	}
}