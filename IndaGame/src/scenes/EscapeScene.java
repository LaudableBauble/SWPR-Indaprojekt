package scenes;

import infrastructure.Enums.DepthDistribution;
import infrastructure.Enums.Visibility;

import main.Entity;
import main.Exit;
import main.Player;
import main.Scene;
import main.SceneManager;

import auxillary.Vector3;

import debug.DebugManager;

/**
 * This is a large demo scene.
 */
public class EscapeScene extends Scene
{
	// The exits.
	private Exit _Exit1;

	// Entities.
	private Entity _EndDoor;
	private Entity _Wall1;
	private Entity _Stairs;
	private Entity _Pillar1;
	private Entity _Pillar2;
	private Entity _Block1;
	private Entity _Pillar3;
	private Entity _Pillar4;
	private Entity _MarbleArch;
	private Entity _Floor;

	/**
	 * Constructor for a scene.
	 * 
	 * @param manager
	 *            The scene manager this scene is part of.
	 */
	public EscapeScene(SceneManager manager)
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
		_Name = "EscapeScene";

		// Create the exit.
		_Exit1 = new Exit(this, "LargeDemoScene", new Vector3(1015, 710, 160));
		_Exit1.getBody().setPosition(new Vector3(940, 980, 0));
		_Exit1.getBody().setIsStatic(true);

		// Create a wall (main wall).
		_Wall1 = new Entity(this);
		_Wall1.getBody().setPosition(new Vector3(1005, 957, 0));
		_Wall1.getBody().setIsStatic(true);

		// Create an end door.
		_EndDoor = new Entity(this);
		_EndDoor.getBody().setPosition(new Vector3(1355, 890, 0));
		_EndDoor.getBody().setIsImmaterial(true);

		// Create a staircase.
		_Stairs = new Entity(this);
		_Stairs.getBody().setPosition(new Vector3(1172, 980, 0));
		_Stairs.getBody().setIsStatic(true);
		_Stairs.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create a pillar (west of marble arch).
		_Pillar1 = new Entity(this);
		_Pillar1.getBody().setPosition(new Vector3(950, 1010, 0));
		_Pillar1.getBody().setIsStatic(true);

		// Create a pillar (south of staircase).
		_Pillar2 = new Entity(this);
		_Pillar2.getBody().setPosition(new Vector3(1060, 1010, 0));
		_Pillar2.getBody().setIsStatic(true);

		// Create a block (east of stairs).
		_Block1 = new Entity(this);
		_Block1.getBody().setPosition(new Vector3(1355, 950, 0));
		_Block1.getBody().setIsStatic(true);

		// Create a pillar (west of marble arch).
		_Pillar3 = new Entity(this);
		_Pillar3.getBody().setPosition(new Vector3(1331, 900, 0));
		_Pillar3.getBody().setIsStatic(true);

		// Create a pillar (east of marble arch).
		_Pillar4 = new Entity(this);
		_Pillar4.getBody().setPosition(new Vector3(1379, 900, 0));
		_Pillar4.getBody().setIsStatic(true);

		// Create a marble arch (north of dark block 8).
		_MarbleArch = new Entity(this);
		_MarbleArch.getBody().setPosition(new Vector3(1355, 900, 0));
		_MarbleArch.getBody().setIsStatic(true);

		// Create the floor.
		_Floor = new Entity(this);
		_Floor.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		addEntity(_Exit1);
		addEntity(_EndDoor);
		addEntity(_Wall1);
		addEntity(_Stairs);
		addEntity(_Pillar1);
		addEntity(_Pillar2);
		addEntity(_Block1);
		addEntity(_Pillar3);
		addEntity(_Pillar4);
		addEntity(_MarbleArch);
		addEntity(_Floor);

		// Give a name to all entities.
		_Exit1.setName("Exit1");
		_EndDoor.setName("EndDoor");
		_Wall1.setName("Wall1");
		_Stairs.setName("Stairs");
		_Pillar1.setName("Pillar1");
		_Pillar2.setName("Pillar2");
		_Block1.setName("Block1");
		_Pillar3.setName("Pillar3");
		_Pillar4.setName("Pillar4");
		_MarbleArch.setName("MarbleArch");
		_Floor.setName("Floor");
	}

	/**
	 * Load content.
	 */
	@Override
	public void loadContent()
	{
		_Exit1.loadContent("DoorDarkness[1].png", 1);
		_Exit1.getSprites().getSprite(0).setVisibility(Visibility.Invisible);
		_EndDoor.loadContent("DoorDarkness[1].png", 1);
		_Wall1.loadContent("MarbleWall[4].png", 5);
		_Stairs.loadContent("StoneStairsRight[4].png", 33);
		_Pillar1.loadContent("MarblePillar[1].png", 13);
		_Pillar2.loadContent("MarblePillar[1].png", 13);
		_Block1.loadContent("DarkTiledBlock[2].png", 134);
		_Pillar3.loadContent("MarblePillar[1].png", 13);
		_Pillar4.loadContent("MarblePillar[1].png", 13);
		_MarbleArch.loadContent("MarbleArch[1].png", 5);
		_Floor.loadContent("DarkTiledFloor[2].png");

		// Set their depths.
		_Exit1.getBody().getShape().setBottomDepth(1);
		_EndDoor.getBody().getShape().setBottomDepth(232);
		_Wall1.getBody().getShape().setBottomDepth(1);
		_Stairs.getBody().getShape().setBottomDepth(1);
		_Pillar1.getBody().getShape().setBottomDepth(1);
		_Pillar2.getBody().getShape().setBottomDepth(1);
		_Block1.getBody().getShape().setBottomDepth(186);
		_Pillar3.getBody().getShape().setBottomDepth(232);
		_Pillar4.getBody().getShape().setBottomDepth(232);
		_MarbleArch.getBody().getShape().setBottomDepth(272);
		_Floor.getBody().getShape().setBottomDepth(0);
	}
}