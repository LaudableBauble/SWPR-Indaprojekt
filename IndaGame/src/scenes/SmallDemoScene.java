package scenes;

import graphics.DepthComposite;
import infrastructure.GameTimer;
import infrastructure.Enums.DepthDistribution;
import input.InputManager;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import main.Character;
import main.Entity;
import main.Player;
import main.Scene;
import main.SceneManager;

import auxillary.Vector2;
import auxillary.Vector3;

import physics.PhysicsSimulator;
import debug.DebugManager;

/**
 * This is a large demo scene.
 */
public class SmallDemoScene extends Scene
{
	// The player.
	private Player _Player;

	// Entities.
	private Entity _Shelf;
	private Entity _Block1;
	private Entity _Block2;
	private Entity _Stairs;
	private Entity _Floor;

	/**
	 * Constructor for a scene.
	 * 
	 * @param manager
	 *            The scene manager this scene is part of.
	 */
	public SmallDemoScene(SceneManager manager)
	{
		super(manager);
	}

	/**
	 * Initialize the scene.
	 * 
	 * @param manager
	 *            The scene manager this scene is part of.
	 */
	@Override
	protected void initialize(SceneManager manager)
	{
		// Call the base method.
		super.initialize(manager);

		// Create the player.
		_Player = new Player(getPhysicsSimulator());
		_Player.getBody().setBottomPosition(new Vector3(910, 1080, 100));
		DebugManager.getInstance().setDebugBody(_Player.getBody());

		// Create the shelf.
		_Shelf = new Entity(getPhysicsSimulator());
		_Shelf.getBody().setPosition(new Vector3(1100, 1100, 0));
		_Shelf.getBody().setIsStatic(true);

		// Create a block.
		_Block1 = new Entity(getPhysicsSimulator());
		_Block1.getBody().setPosition(new Vector3(950, 933.5, 0));
		_Block1.getBody().setIsStatic(true);

		// Create a block.
		_Block2 = new Entity(getPhysicsSimulator());
		_Block2.getBody().setPosition(new Vector3(1000, 1020, 0));
		_Block2.getBody().setIsStatic(true);

		// Create a staircase.
		_Stairs = new Entity(getPhysicsSimulator());
		_Stairs.getBody().setPosition(new Vector3(930, 1080, 0));
		_Stairs.getBody().setIsStatic(true);
		_Stairs.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create the floor.
		_Floor = new Entity(getPhysicsSimulator());
		_Floor.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		addEntity(_Player);
		addEntity(_Shelf);
		addEntity(_Block1);
		addEntity(_Block2);
		addEntity(_Stairs);
		addEntity(_Floor);

		// Give a name to all entities.
		_Player.setName("Player");
		_Shelf.setName("Shelf");
		_Block1.setName("Block1");
		_Block2.setName("Block2");
		_Stairs.setName("Stairs");
		_Floor.setName("Floor");
	}

	/**
	 * Load content.
	 */
	@Override
	public void loadContent()
	{
		// Load all entities' content.
		_Player.loadContent();
		_Shelf.loadContent("Bookshelf[1].png", 12);
		_Block1.loadContent("ElevatedBlock[3].png", 48);
		_Block2.loadContent("ElevatedBlock[2].png", 85);
		_Stairs.loadContent("StoneStairsRight[2].png", 33);
		_Floor.loadContent("DarkTiledFloor[1].png");

		// Set their depths.
		_Shelf.getBody().getShape().setBottomDepth(1);
		_Block1.getBody().getShape().setBottomDepth(1);
		_Block2.getBody().getShape().setBottomDepth(1);
		_Stairs.getBody().getShape().setBottomDepth(1);
		_Floor.getBody().getShape().setBottomDepth(0);
	}
}