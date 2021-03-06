package scenes;

import infrastructure.Enums.DepthDistribution;

import main.Entity;
import main.Player;
import main.Scene;
import main.SceneManager;

import auxillary.Vector3;

import debug.DebugManager;

/**
 * This is a large demo scene.
 */
public class SmallDemoScene extends Scene
{
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

		// Name the scene.
		_Name = "SmallDemoScene";

		// Create the shelf.
		_Shelf = new Entity(this);
		_Shelf.getBody().setPosition(new Vector3(1100, 1100, 0));
		_Shelf.getBody().setIsStatic(true);

		// Create a block.
		_Block1 = new Entity(this);
		_Block1.getBody().setPosition(new Vector3(950, 933.5, 0));
		_Block1.getBody().setIsStatic(true);

		// Create a block.
		_Block2 = new Entity(this);
		_Block2.getBody().setPosition(new Vector3(1000, 1020, 0));
		_Block2.getBody().setIsStatic(true);

		// Create a staircase.
		_Stairs = new Entity(this);
		_Stairs.getBody().setPosition(new Vector3(930, 1080, 0));
		_Stairs.getBody().setIsStatic(true);
		_Stairs.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create the floor.
		_Floor = new Entity(this);
		_Floor.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		addEntity(_Shelf);
		addEntity(_Block1);
		addEntity(_Block2);
		addEntity(_Stairs);
		addEntity(_Floor);

		// Give a name to all entities.
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