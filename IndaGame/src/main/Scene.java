package main;

import graphics.DepthComposite;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

import auxillary.Vector2;
import auxillary.Vector3;

import physics.PhysicsSimulator;
import debug.DebugManager;

/**
 * A scene is a map of the game world and can be populated by entities.
 */
public class Scene
{
	// The name of the scene. Primarily used as file name when serialized.
	protected String _Name;
	// The scene manager.
	protected SceneManager _SceneManager;
	// The physics simulator.
	protected PhysicsSimulator _Physics;
	// List of entities.
	protected ArrayList<Entity> _Entities;
	// The composite z-buffer.
	protected DepthComposite _Composite;
	// The entrance positions.
	protected ArrayList<Vector3> _Entrances;

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
		_Name = "Scene";
		_SceneManager = manager;
		_Entities = new ArrayList<>();
		_Physics = new PhysicsSimulator();
		_Composite = new DepthComposite(_SceneManager.getCamera().getViewportSize());
		_Entrances = new ArrayList<Vector3>();

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
		for (Entity entity : new ArrayList<Entity>(_Entities))
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

		// Update all entities.
		for (Entity entity : new ArrayList<Entity>(_Entities))
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
		Composite old = graphics.getComposite();
		graphics.setComposite(_Composite);

		// Draw all entities.
		for (Entity entity : new ArrayList<Entity>(_Entities))
		{
			// Prepare the graphics device for depth-sorting.
			((DepthComposite) graphics.getComposite()).setEntity(entity);
			entity.draw(graphics);
		}

		// Notify the depth composite that the frame has ended, at least for the scene.
		_Composite.endFrame();
		graphics.setComposite(old);
	}

	/**
	 * Add an entity to the scene.
	 * 
	 * @param entity
	 *            The entity to add.
	 * @return The added entity.
	 */
	public Entity addEntity(Entity entity)
	{
		_Entities.add(entity);
		entity.setScene(this);
		_Physics.addBody(entity.getBody());
		Collections.sort(_Entities, new EntityDepthComparator());
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
	 * Add an entrance position to the scene.
	 * 
	 * @param entrance
	 *            The entrance to add.
	 * @return The added entrance position.
	 */
	public Vector3 addEntrance(Vector3 entrance)
	{
		_Entrances.add(entrance);
		return entrance;
	}

	/**
	 * Get the scene's name.
	 * 
	 * @return The name of the scene.
	 */
	public String getName()
	{
		return _Name;
	}

	/**
	 * Set the scene's name.
	 * 
	 * @param name
	 *            The new name of the scene.
	 */
	public void setName(String name)
	{
		_Name = name;
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
	 * Get the scene's manager.
	 * 
	 * @return The scene's manager.
	 */
	public SceneManager getSceneManager()
	{
		return _SceneManager;
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

	/**
	 * Get the list of entities.
	 * 
	 * @return The list of entities.
	 */
	public ArrayList<Entity> getEntities()
	{
		return _Entities;
	}

	/**
	 * Set the list of entities.
	 * 
	 * @param entities
	 *            The new list of entities.
	 */
	public void setEntities(ArrayList<Entity> entities)
	{
		_Entities = entities;
	}

	/**
	 * Get the list of entrances.
	 * 
	 * @return The list of entrances.
	 */
	public ArrayList<Vector3> getEntrances()
	{
		return _Entrances;
	}
}