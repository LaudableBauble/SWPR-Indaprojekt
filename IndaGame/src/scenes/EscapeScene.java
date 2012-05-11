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
	private Entity _Wall1;
	private Entity _Stairs;
	private Entity _Pillar1;
	private Entity _Pillar2;
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

		// Create the floor.
		_Floor = new Entity(this);
		_Floor.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		addEntity(_Exit1);
		addEntity(_Wall1);
		addEntity(_Stairs);
		addEntity(_Pillar1);
		addEntity(_Pillar2);
		addEntity(_Floor);

		// Give a name to all entities.
		_Exit1.setName("Exit1");
		_Wall1.setName("Wall1");
		_Stairs.setName("Stairs");
		_Pillar1.setName("Pillar1");
		_Pillar2.setName("Pillar2");
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
		_Wall1.loadContent("MarbleWall[4].png", 5);
		_Stairs.loadContent("StoneStairsRight[4].png", 33);
		_Pillar1.loadContent("MarblePillar[1].png", 13);
		_Pillar2.loadContent("MarblePillar[1].png", 13);
		_Floor.loadContent("DarkTiledFloor[2].png");

		// Set their depths.
		_Exit1.getBody().getShape().setBottomDepth(1);
		_Wall1.getBody().getShape().setBottomDepth(1);
		_Stairs.getBody().getShape().setBottomDepth(1);
		_Pillar1.getBody().getShape().setBottomDepth(1);
		_Pillar2.getBody().getShape().setBottomDepth(1);
		_Floor.getBody().getShape().setBottomDepth(0);
	}
}