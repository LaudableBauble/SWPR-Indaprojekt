package scenes;

import infrastructure.Enums.DepthDistribution;

import main.Entity;
import main.Exit;
import main.Player;
import main.Character;
import main.Scene;
import main.SceneManager;

import auxillary.Vector3;

import debug.DebugManager;

/**
 * This is a large demo scene.
 */
public class BedroomScene extends Scene
{
	// The characters.
	private Character _Character;

	// The exits.
	private Exit _Exit1;

	// Entities.
	private Entity _Key;
	private Entity _Shelf;
	private Entity _Wall1;
	private Entity _Wall2;
	private Entity _Block1;
	private Entity _Block2;
	private Entity _Block3;
	private Entity _Stairs;
	private Entity _Pillar1;
	private Entity _Pillar2;
	private Entity _MarbleArch;
	private Entity _Floor;

	/**
	 * Constructor for a scene.
	 * 
	 * @param manager
	 *            The scene manager this scene is part of.
	 */
	public BedroomScene(SceneManager manager)
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
		_Name = "BedroomScene";

		// Create a character.
		_Character = new Character(this, "Robeguy", 3);
		_Character.getBody().setPosition(new Vector3(1000, 1025, 0));

		// Create the exit.
		_Exit1 = new Exit(this, "LargeDemoScene", new Vector3(1000, 1200, 15));
		_Exit1.getBody().setPosition(new Vector3(890, 858, 0));
		_Exit1.getBody().setIsStatic(true);

		// Create a key.
		_Key = new Entity(this);
		_Key.getBody().setPosition(new Vector3(960, 900, 0));
		_Key.getBody().setIsImmaterial(true);

		// Create the shelf.
		_Shelf = new Entity(this);
		_Shelf.getBody().setPosition(new Vector3(960, 920, 0));

		// Create a wall (main wall).
		_Wall1 = new Entity(this);
		_Wall1.getBody().setPosition(new Vector3(1070, 920, 0));
		_Wall1.getBody().setIsStatic(true);

		// Create a wall (east of wall 1).
		_Wall2 = new Entity(this);
		_Wall2.getBody().setPosition(new Vector3(1183.5, 920, 0));
		_Wall2.getBody().setIsStatic(true);

		// Create a block (main block north).
		_Block1 = new Entity(this);
		_Block1.getBody().setPosition(new Vector3(1130, 943, 0));
		_Block1.getBody().setIsStatic(true);

		// Create a block (main block south).
		_Block2 = new Entity(this);
		_Block2.getBody().setPosition(new Vector3(1130, 1077, 0));
		_Block2.getBody().setIsStatic(true);

		// Create a block (main block west).
		_Block3 = new Entity(this);
		_Block3.getBody().setPosition(new Vector3(993, 943, 0));
		_Block3.getBody().setIsStatic(true);

		// Create a staircase.
		_Stairs = new Entity(this);
		_Stairs.getBody().setPosition(new Vector3(1037.5, 1026.5, 0));
		_Stairs.getBody().setIsStatic(true);
		_Stairs.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create a pillar (west of marble arch).
		_Pillar1 = new Entity(this);
		_Pillar1.getBody().setPosition(new Vector3(866, 860, 0));
		_Pillar1.getBody().setIsStatic(true);

		// Create a pillar (east of marble arch).
		_Pillar2 = new Entity(this);
		_Pillar2.getBody().setPosition(new Vector3(914, 860, 0));
		_Pillar2.getBody().setIsStatic(true);

		// Create a marble arch (north of dark block 8).
		_MarbleArch = new Entity(this);
		_MarbleArch.getBody().setPosition(new Vector3(890, 860, 0));
		_MarbleArch.getBody().setIsStatic(true);

		// Create the floor.
		_Floor = new Entity(this);
		_Floor.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		addEntity(_Character);
		addEntity(_Exit1);
		addEntity(_Key);
		addEntity(_Shelf);
		addEntity(_Wall1);
		addEntity(_Wall2);
		addEntity(_Block1);
		addEntity(_Block2);
		addEntity(_Block3);
		addEntity(_Stairs);
		addEntity(_Pillar1);
		addEntity(_Pillar2);
		addEntity(_MarbleArch);
		addEntity(_Floor);

		// Give a name to all entities.
		_Character.setName("Robeguy");
		_Exit1.setName("Exit1");
		_Key.setName("Key");
		_Shelf.setName("Shelf");
		_Wall1.setName("Wall1");
		_Wall2.setName("Wall2");
		_Block1.setName("Block1");
		_Block2.setName("Block2");
		_Block3.setName("Block3");
		_Stairs.setName("Stairs");
		_Pillar1.setName("Pillar1");
		_Pillar2.setName("Pillar2");
		_MarbleArch.setName("MarbleArch");
		_Floor.setName("Floor");
	}

	/**
	 * Load content.
	 */
	@Override
	public void loadContent()
	{
		_Character.loadContent();
		_Exit1.loadContent("DoorDarkness[1].png", 1);
		_Key.loadContent("GoldenKey[1].png", 2);
		_Shelf.loadContent("Bookshelf[1].png", 12);
		_Wall1.loadContent("MarbleWall[2].png", 5);
		_Wall2.loadContent("MarbleWall[3].png", 49);
		_Block1.loadContent("DarkTiledBlock[2].png", 134);
		_Block2.loadContent("DarkTiledBlock[2].png", 134);
		_Block3.loadContent("DarkTiledBlock[2].png", 134);
		_Stairs.loadContent("StoneStairsRight[3].png", 33);
		_Pillar1.loadContent("MarblePillar[1].png", 13);
		_Pillar2.loadContent("MarblePillar[1].png", 13);
		_MarbleArch.loadContent("MarbleArch[1].png", 5);
		_Floor.loadContent("DarkTiledFloor[1].png");

		// Set their depths.
		_Character.getBody().getShape().setBottomDepth(1);
		_Exit1.getBody().getShape().setBottomDepth(1);
		_Key.getBody().getShape().setBottomDepth(47);
		_Shelf.getBody().getShape().setBottomDepth(47);
		_Wall1.getBody().getShape().setBottomDepth(47);
		_Wall2.getBody().getShape().setBottomDepth(47);
		_Block1.getBody().getShape().setBottomDepth(1);
		_Block2.getBody().getShape().setBottomDepth(1);
		_Block3.getBody().getShape().setBottomDepth(1);
		_Stairs.getBody().getShape().setBottomDepth(1);
		_Pillar1.getBody().getShape().setBottomDepth(1);
		_Pillar2.getBody().getShape().setBottomDepth(1);
		_MarbleArch.getBody().getShape().setBottomDepth(40);
		_Floor.getBody().getShape().setBottomDepth(0);
	}
}