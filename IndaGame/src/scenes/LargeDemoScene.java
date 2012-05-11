package scenes;

import infrastructure.Enums.DepthDistribution;
import infrastructure.Enums.Visibility;

import main.Character;
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
public class LargeDemoScene extends Scene
{
	// Character.
	private Character _Character;

	// The exits.
	private Exit _Exit1;
	private Exit _Exit2;

	// Entities.
	private Entity _Shelf1;
	private Entity _Shelf2;
	private Entity _Shelf3;
	private Entity _Shelf4;
	private Entity _Shelf5;
	private Entity _Block1;
	private Entity _Block2;
	private Entity _Block3;
	private Entity _Block4;
	private Entity _Block5;
	private Entity _Block6;
	private Entity _DarkBlock1;
	private Entity _DarkBlock2;
	private Entity _DarkBlock3;
	private Entity _DarkBlock4;
	private Entity _DarkBlock5;
	private Entity _DarkBlock6;
	private Entity _DarkBlock7;
	private Entity _DarkBlock8;
	private Entity _DarkBlock9;
	private Entity _DarkBlock10;
	private Entity _DarkBlock11;
	private Entity _DarkBlock12;
	private Entity _DarkBlock13;
	private Entity _Stairs1;
	private Entity _Stairs2;
	private Entity _Stairs3;
	private Entity _Stairs4;
	private Entity _Stairs5;
	private Entity _Stairs6;
	private Entity _Stairs7;
	private Entity _Stairs8;
	private Entity _Pathway1;
	private Entity _Pathway2;
	private Entity _Pathway3;
	private Entity _Pathway4;
	private Entity _PathwayArch1;
	private Entity _PathwayPlatform1;
	private Entity _PathwayPlatform2;
	private Entity _PathwayPlatform3;
	private Entity _Pillar1;
	private Entity _Pillar2;
	private Entity _MarbleArch;
	private Entity _MarbleWall1;
	private Entity _MarbleWall2;
	private Entity _Floor;

	/**
	 * Constructor for a scene.
	 * 
	 * @param manager
	 *            The scene manager this scene is part of.
	 */
	public LargeDemoScene(SceneManager manager)
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
		_Name = "LargeDemoScene";

		// Create a character.
		_Character = new main.Character(this, "Skeleton", 3);
		_Character.getBody().setPosition(new Vector3(1040, 1010, 50));

		// Create the exit.
		_Exit1 = new Exit(this, "EscapeScene", new Vector3(980, 980, 15));
		_Exit1.getBody().setPosition(new Vector3(1015, 690, 0));
		_Exit1.getBody().setIsStatic(true);

		// Create the exit.
		_Exit2 = new Exit(this, "BedroomScene", new Vector3(890, 900, 15));
		_Exit2.getBody().setPosition(new Vector3(1000, 1230, 0));
		_Exit2.getBody().setIsStatic(true);

		// Create the shelf (south of pathway arch).
		_Shelf1 = new Entity(this);
		_Shelf1.getBody().setPosition(new Vector3(940, 1025, 0));
		_Shelf1.getBody().setIsStatic(true);

		// Create the shelf (east of pathway platform 3).
		_Shelf2 = new Entity(this);
		_Shelf2.getBody().setPosition(new Vector3(1270, 1080, 0));
		_Shelf2.getBody().setIsStatic(true);

		// Create the shelf (south of block 4).
		_Shelf3 = new Entity(this);
		_Shelf3.getBody().setPosition(new Vector3(900, 945, 0));
		_Shelf3.getBody().setIsStatic(true);

		// Create the shelf (south of dark block 11).
		_Shelf4 = new Entity(this);
		_Shelf4.getBody().setPosition(new Vector3(1300, 940, 0));
		_Shelf4.getBody().setIsStatic(true);

		// Create the shelf (south of dark block 7).
		_Shelf5 = new Entity(this);
		_Shelf5.getBody().setPosition(new Vector3(670, 850, 0));
		_Shelf5.getBody().setIsStatic(true);

		// Create a block (north of staircase 2).
		_Block1 = new Entity(this);
		_Block1.getBody().setPosition(new Vector3(710.5, 942, 0));
		_Block1.getBody().setIsStatic(true);

		// Create a block (north of dark block 2).
		_Block2 = new Entity(this);
		_Block2.getBody().setPosition(new Vector3(1180, 940, 0));
		_Block2.getBody().setIsStatic(true);

		// Create a block (north of dark block 1).
		_Block3 = new Entity(this);
		_Block3.getBody().setPosition(new Vector3(852.5, 909, 0));
		_Block3.getBody().setIsStatic(true);

		// Create a block (north of block 1).
		_Block4 = new Entity(this);
		_Block4.getBody().setPosition(new Vector3(710.5, 875.5, 0));
		_Block4.getBody().setIsStatic(true);

		// Create a block (north of dark block 5, main stairs).
		_Block5 = new Entity(this);
		_Block5.getBody().setPosition(new Vector3(1000, 794.5, 0));
		_Block5.getBody().setIsStatic(true);

		// Create a block (east of block 2).
		_Block6 = new Entity(this);
		_Block6.getBody().setPosition(new Vector3(1324, 940, 0));
		_Block6.getBody().setIsStatic(true);

		// Create a dark block (west of pathway arch).
		_DarkBlock1 = new Entity(this);
		_DarkBlock1.getBody().setPosition(new Vector3(852, 1000, 0));
		_DarkBlock1.getBody().setIsStatic(true);

		// Create a dark block (east of pathway arch).
		_DarkBlock2 = new Entity(this);
		_DarkBlock2.getBody().setPosition(new Vector3(1149, 1000, 0));
		_DarkBlock2.getBody().setIsStatic(true);

		// Create a dark block (south of pathway platform 1).
		_DarkBlock3 = new Entity(this);
		_DarkBlock3.getBody().setPosition(new Vector3(900, 1183, 0));
		_DarkBlock3.getBody().setIsStatic(true);

		// Create a dark block (south of pathway platform 2).
		_DarkBlock4 = new Entity(this);
		_DarkBlock4.getBody().setPosition(new Vector3(1120, 1183, 0));
		_DarkBlock4.getBody().setIsStatic(true);

		// Create a dark block (north of pathway arch, main stairs).
		_DarkBlock5 = new Entity(this);
		_DarkBlock5.getBody().setPosition(new Vector3(1000, 872, 0));
		_DarkBlock5.getBody().setIsStatic(true);

		// Create a dark block (north of block 4, west stairs).
		_DarkBlock6 = new Entity(this);
		_DarkBlock6.getBody().setPosition(new Vector3(710.5, 771, 0));
		_DarkBlock6.getBody().setIsStatic(true);

		// Create a dark block (north of block 3, east of dark block 6).
		_DarkBlock7 = new Entity(this);
		_DarkBlock7.getBody().setPosition(new Vector3(847.5, 818, 0));
		_DarkBlock7.getBody().setIsStatic(true);

		// Create a dark block (north of block 5, main stairs).
		_DarkBlock8 = new Entity(this);
		_DarkBlock8.getBody().setPosition(new Vector3(895, 720, 0));
		_DarkBlock8.getBody().setIsStatic(true);

		// Create a dark block (east of dark block 2).
		_DarkBlock9 = new Entity(this);
		_DarkBlock9.getBody().setPosition(new Vector3(1283, 1000, 0));
		_DarkBlock9.getBody().setIsStatic(true);

		// Create a dark block (north of block 2).
		_DarkBlock10 = new Entity(this);
		_DarkBlock10.getBody().setPosition(new Vector3(1180, 830.5, 0));
		_DarkBlock10.getBody().setIsStatic(true);

		// Create a dark block (east of dark block 10).
		_DarkBlock11 = new Entity(this);
		_DarkBlock11.getBody().setPosition(new Vector3(1317, 863.5, 0));
		_DarkBlock11.getBody().setIsStatic(true);

		// Create a dark block (south of pathway platform 2).
		_DarkBlock12 = new Entity(this);
		_DarkBlock12.getBody().setPosition(new Vector3(1257, 1183, 0));
		_DarkBlock12.getBody().setIsStatic(true);

		// Create a dark block (north of block 5, east of dark block 8, main stairs).
		_DarkBlock13 = new Entity(this);
		_DarkBlock13.getBody().setPosition(new Vector3(1128, 720, 0));
		_DarkBlock13.getBody().setIsStatic(true);

		// Create a staircase (west of dark block 1).
		_Stairs1 = new Entity(this);
		_Stairs1.getBody().setPosition(new Vector3(759.5, 982.5, 0));
		_Stairs1.getBody().setIsStatic(true);
		_Stairs1.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create a staircase (west of dark block 2).
		_Stairs2 = new Entity(this);
		_Stairs2.getBody().setPosition(new Vector3(1056.5, 1033, 0));
		_Stairs2.getBody().setIsStatic(true);
		_Stairs2.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create a staircase (north of dark block 5, main stairs).
		_Stairs3 = new Entity(this);
		_Stairs3.getBody().setPosition(new Vector3(1000, 931.5, 0));
		_Stairs3.getBody().setIsStatic(true);
		_Stairs3.getBody().getShape().setDepthDistribution(DepthDistribution.Top);

		// Create a staircase (east of block 1).
		_Stairs4 = new Entity(this);
		_Stairs4.getBody().setPosition(new Vector3(807.5, 942, 0));
		_Stairs4.getBody().setIsStatic(true);
		_Stairs4.getBody().getShape().setDepthDistribution(DepthDistribution.Left);

		// Create a staircase (east of dark block 7).
		_Stairs5 = new Entity(this);
		_Stairs5.getBody().setPosition(new Vector3(755, 868.5, 0));
		_Stairs5.getBody().setIsStatic(true);
		_Stairs5.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create a staircase (north of block 5, main stairs).
		_Stairs6 = new Entity(this);
		_Stairs6.getBody().setPosition(new Vector3(1015, 861, 0));
		_Stairs6.getBody().setIsStatic(true);
		_Stairs6.getBody().getShape().setDepthDistribution(DepthDistribution.Top);

		// Create a staircase (south of dark block 8, main stairs).
		_Stairs7 = new Entity(this);
		_Stairs7.getBody().setPosition(new Vector3(1015, 778, 0));
		_Stairs7.getBody().setIsStatic(true);
		_Stairs7.getBody().getShape().setDepthDistribution(DepthDistribution.Top);

		// Create a staircase (west of dark block 11).
		_Stairs8 = new Entity(this);
		_Stairs8.getBody().setPosition(new Vector3(1224.5, 914, 0));
		_Stairs8.getBody().setIsStatic(true);
		_Stairs8.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create a pathway.
		_Pathway1 = new Entity(this);
		_Pathway2 = new Entity(this);
		_Pathway3 = new Entity(this);
		_Pathway4 = new Entity(this);
		_PathwayArch1 = new Entity(this);
		_Pathway1.getBody().setPosition(new Vector3(936.5, 1000, 0));
		_Pathway2.getBody().setPosition(new Vector3(968.5, 1000, 0));
		_PathwayArch1.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Pathway3.getBody().setPosition(new Vector3(1032.5, 1000, 0));
		_Pathway4.getBody().setPosition(new Vector3(1064.5, 1000, 0));
		_Pathway1.getBody().setIsStatic(true);
		_Pathway2.getBody().setIsStatic(true);
		_Pathway3.getBody().setIsStatic(true);
		_Pathway4.getBody().setIsStatic(true);
		_PathwayArch1.getBody().setIsStatic(true);

		// Create a pathway platform (south of dark block 1).
		_PathwayPlatform1 = new Entity(this);
		_PathwayPlatform1.getBody().setPosition(new Vector3(852, 1107.5, 0));
		_PathwayPlatform1.getBody().setIsStatic(true);

		// Create a pathway platform (south of dark block 2).
		_PathwayPlatform2 = new Entity(this);
		_PathwayPlatform2.getBody().setPosition(new Vector3(1149, 1107.5, 0));
		_PathwayPlatform2.getBody().setIsStatic(true);

		// Create a pathway platform (south of dark block 9).
		_PathwayPlatform3 = new Entity(this);
		_PathwayPlatform3.getBody().setPosition(new Vector3(1305, 1107.5, 0));
		_PathwayPlatform3.getBody().setIsStatic(true);

		// Create a pillar (west of marble arch).
		_Pillar1 = new Entity(this);
		_Pillar1.getBody().setPosition(new Vector3(991, 690, 0));
		_Pillar1.getBody().setIsStatic(true);

		// Create a pillar (east of marble arch).
		_Pillar2 = new Entity(this);
		_Pillar2.getBody().setPosition(new Vector3(1039, 690, 0));
		_Pillar2.getBody().setIsStatic(true);

		// Create a marble arch (north of dark block 8).
		_MarbleArch = new Entity(this);
		_MarbleArch.getBody().setPosition(new Vector3(1015, 690, 0));
		_MarbleArch.getBody().setIsStatic(true);

		// Create a marble wall (west of pillar 1).
		_MarbleWall1 = new Entity(this);
		_MarbleWall1.getBody().setPosition(new Vector3(823, 680, 0));
		_MarbleWall1.getBody().setIsStatic(true);

		// Create a marble wall (east of pillar 2).
		_MarbleWall2 = new Entity(this);
		_MarbleWall2.getBody().setPosition(new Vector3(1207, 680, 0));
		_MarbleWall2.getBody().setIsStatic(true);

		// Create the floor.
		_Floor = new Entity(this);
		_Floor.getBody().setPosition(new Vector3(1000, 1050, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		addEntity(_Character);
		addEntity(_Exit1);
		addEntity(_Exit2);
		addEntity(_Shelf1);
		addEntity(_Shelf2);
		addEntity(_Shelf3);
		addEntity(_Shelf4);
		addEntity(_Shelf5);
		addEntity(_Block1);
		addEntity(_Block2);
		addEntity(_Block3);
		addEntity(_Block4);
		addEntity(_Block5);
		addEntity(_Block6);
		addEntity(_DarkBlock1);
		addEntity(_DarkBlock2);
		addEntity(_DarkBlock3);
		addEntity(_DarkBlock4);
		addEntity(_DarkBlock5);
		addEntity(_DarkBlock6);
		addEntity(_DarkBlock7);
		addEntity(_DarkBlock8);
		addEntity(_DarkBlock9);
		addEntity(_DarkBlock10);
		addEntity(_DarkBlock11);
		addEntity(_DarkBlock12);
		addEntity(_DarkBlock13);
		addEntity(_Stairs1);
		addEntity(_Stairs2);
		addEntity(_Stairs3);
		addEntity(_Stairs4);
		addEntity(_Stairs5);
		addEntity(_Stairs6);
		addEntity(_Stairs7);
		addEntity(_Stairs8);
		addEntity(_Pathway1);
		addEntity(_Pathway2);
		addEntity(_Pathway3);
		addEntity(_Pathway4);
		addEntity(_PathwayArch1);
		addEntity(_PathwayPlatform1);
		addEntity(_PathwayPlatform2);
		addEntity(_PathwayPlatform3);
		addEntity(_Pillar1);
		addEntity(_Pillar2);
		addEntity(_MarbleArch);
		addEntity(_MarbleWall1);
		addEntity(_MarbleWall2);
		addEntity(_Floor);

		// Give a name to all entities.
		_Character.setName("Character");
		_Exit1.setName("Exit1");
		_Exit1.setName("Exit2");
		_Shelf1.setName("Shelf1");
		_Shelf2.setName("Shelf2");
		_Shelf3.setName("Shelf3");
		_Shelf4.setName("Shelf4");
		_Shelf5.setName("Shelf5");
		_Block1.setName("Block1");
		_Block2.setName("Block2");
		_Block3.setName("Block3");
		_Block4.setName("Block4");
		_Block5.setName("Block5");
		_Block6.setName("Block6");
		_DarkBlock1.setName("DarkBlock1");
		_DarkBlock2.setName("DarkBlock2");
		_DarkBlock3.setName("DarkBlock3");
		_DarkBlock4.setName("DarkBlock4");
		_DarkBlock5.setName("DarkBlock5");
		_DarkBlock6.setName("DarkBlock6");
		_DarkBlock7.setName("DarkBlock7");
		_DarkBlock8.setName("DarkBlock8");
		_DarkBlock9.setName("DarkBlock9");
		_DarkBlock10.setName("DarkBlock10");
		_DarkBlock11.setName("DarkBlock11");
		_DarkBlock12.setName("DarkBlock12");
		_DarkBlock13.setName("DarkBlock13");
		_Stairs1.setName("Stairs1");
		_Stairs2.setName("Stairs2");
		_Stairs3.setName("Stairs3");
		_Stairs4.setName("Stairs4");
		_Stairs5.setName("Stairs5");
		_Stairs6.setName("Stairs6");
		_Stairs7.setName("Stairs7");
		_Stairs8.setName("Stairs8");
		_Pathway1.setName("Pathway1");
		_Pathway2.setName("Pathway2");
		_Pathway3.setName("Pathway3");
		_Pathway4.setName("Pathway4");
		_PathwayPlatform1.setName("PathwayPlatform1");
		_PathwayPlatform2.setName("PathwayPlatform2");
		_PathwayPlatform3.setName("PathwayPlatform3");
		_Pillar1.setName("Pillar1");
		_Pillar1.setName("Pillar2");
		_MarbleArch.setName("MarbleArch");
		_MarbleWall1.setName("MarbleWall1");
		_MarbleWall1.setName("MarbleWall2");
		_Floor.setName("Floor");
	}

	/**
	 * Load content.
	 */
	@Override
	public void loadContent()
	{
		// Load all entities' content.
		_Character.loadContent();
		_Exit1.loadContent("DoorDarkness[1].png", 1);
		_Exit2.loadContent("DoorDarkness[1].png", 1);
		_Exit2.getSprites().getSprite(0).setVisibility(Visibility.Invisible);
		_Shelf1.loadContent("Bookshelf[1].png", 12);
		_Shelf2.loadContent("Bookshelf[1].png", 12);
		_Shelf3.loadContent("Bookshelf[1].png", 12);
		_Shelf4.loadContent("Bookshelf[1].png", 12);
		_Shelf5.loadContent("Bookshelf[1].png", 12);
		_Block1.loadContent("ElevatedBlock[3].png", 48);
		_Block2.loadContent("ElevatedBlock[2].png", 85);
		_Block3.loadContent("ElevatedBlock[3].png", 48);
		_Block4.loadContent("ElevatedBlock[2].png", 85);
		_Block5.loadContent("ElevatedBlock[4].png", 85);
		_Block6.loadContent("ElevatedBlock[2].png", 85);
		_DarkBlock1.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock2.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock3.loadContent("DarkTiledBlock[1].png", 70);
		_DarkBlock4.loadContent("DarkTiledBlock[1].png", 70);
		_DarkBlock5.loadContent("DarkTiledBlock[3].png", 70);
		_DarkBlock6.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock7.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock8.loadContent("DarkTiledBlock[3].png", 70);
		_DarkBlock9.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock10.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock11.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock12.loadContent("DarkTiledBlock[1].png", 70);
		_DarkBlock13.loadContent("DarkTiledBlock[3].png", 70);
		_Stairs1.loadContent("StoneStairsRight[3].png", 33);
		_Stairs2.loadContent("StoneStairsRight[3].png", 33);
		_Stairs3.loadContent("StoneStairsTop[2].png", 46);
		_Stairs4.loadContent("StoneStairsLeft[1].png", 33);
		_Stairs5.loadContent("StoneStairsRight[3].png", 33);
		_Stairs6.loadContent("StoneStairsTop[2].png", 46);
		_Stairs7.loadContent("StoneStairsTop[2].png", 46);
		_Stairs8.loadContent("StoneStairsRight[3].png", 33);
		_Pathway1.loadContent("StonePathwayBlock[1].png", 33);
		_Pathway2.loadContent("StonePathwayBlock[1].png", 33);
		_Pathway3.loadContent("StonePathwayBlock[1].png", 33);
		_Pathway4.loadContent("StonePathwayBlock[1].png", 33);
		_PathwayArch1.loadContent("StonePathwayArch[1].png", 33);
		_PathwayPlatform1.loadContent("StonePathwayBlock[2].png", 81);
		_PathwayPlatform2.loadContent("StonePathwayBlock[4].png", 81);
		_PathwayPlatform3.loadContent("StonePathwayBlock[3].png", 81);
		_Pillar1.loadContent("MarblePillar[1].png", 13);
		_Pillar2.loadContent("MarblePillar[1].png", 13);
		_MarbleArch.loadContent("MarbleArch[1].png", 5);
		_MarbleWall1.loadContent("MarbleWall[1].png", 22);
		_MarbleWall2.loadContent("MarbleWall[1].png", 22);
		_Floor.loadContent("WoodTiledFloor[1].png");

		// Set their depths.
		_Exit1.getBody().getShape().setBottomDepth(144);
		_Exit2.getBody().getShape().setBottomDepth(1);
		_Shelf1.getBody().getShape().setBottomDepth(1);
		_Shelf2.getBody().getShape().setBottomDepth(1);
		_Shelf3.getBody().getShape().setBottomDepth(48);
		_Shelf4.getBody().getShape().setBottomDepth(97);
		_Shelf5.getBody().getShape().setBottomDepth(97);
		_Block1.getBody().getShape().setBottomDepth(1);
		_Block2.getBody().getShape().setBottomDepth(49);
		_Block3.getBody().getShape().setBottomDepth(1);
		_Block4.getBody().getShape().setBottomDepth(48);
		_Block5.getBody().getShape().setBottomDepth(48);
		_Block6.getBody().getShape().setBottomDepth(49);
		_DarkBlock1.getBody().getShape().setBottomDepth(1);
		_DarkBlock2.getBody().getShape().setBottomDepth(1);
		_DarkBlock3.getBody().getShape().setBottomDepth(1);
		_DarkBlock4.getBody().getShape().setBottomDepth(1);
		_DarkBlock5.getBody().getShape().setBottomDepth(1);
		_DarkBlock6.getBody().getShape().setBottomDepth(97);
		_DarkBlock7.getBody().getShape().setBottomDepth(97);
		_DarkBlock8.getBody().getShape().setBottomDepth(97);
		_DarkBlock9.getBody().getShape().setBottomDepth(1);
		_DarkBlock10.getBody().getShape().setBottomDepth(97);
		_DarkBlock11.getBody().getShape().setBottomDepth(97);
		_DarkBlock12.getBody().getShape().setBottomDepth(1);
		_DarkBlock13.getBody().getShape().setBottomDepth(97);
		_Stairs1.getBody().getShape().setBottomDepth(1);
		_Stairs2.getBody().getShape().setBottomDepth(1);
		_Stairs3.getBody().getShape().setDepth(48);
		_Stairs3.getBody().getShape().setBottomDepth(1);
		_Stairs4.getBody().getShape().setBottomDepth(48);
		_Stairs5.getBody().getShape().setBottomDepth(97);
		_Stairs6.getBody().getShape().setDepth(48);
		_Stairs6.getBody().getShape().setBottomDepth(49);
		_Stairs7.getBody().getShape().setDepth(48);
		_Stairs7.getBody().getShape().setBottomDepth(97);
		_Stairs8.getBody().getShape().setBottomDepth(97);
		_Pathway1.getBody().getShape().setBottomDepth(1);
		_Pathway2.getBody().getShape().setBottomDepth(1);
		_Pathway3.getBody().getShape().setBottomDepth(1);
		_Pathway4.getBody().getShape().setBottomDepth(1);
		_PathwayArch1.getBody().getShape().setBottomDepth(22);
		_PathwayPlatform1.getBody().getShape().setBottomDepth(37);
		_PathwayPlatform2.getBody().getShape().setBottomDepth(37);
		_PathwayPlatform3.getBody().getShape().setBottomDepth(1);
		_Pillar1.getBody().getShape().setBottomDepth(144);
		_Pillar2.getBody().getShape().setBottomDepth(144);
		_MarbleArch.getBody().getShape().setBottomDepth(184);
		_MarbleWall1.getBody().getShape().setBottomDepth(144);
		_MarbleWall2.getBody().getShape().setBottomDepth(144);
		_Floor.getBody().getShape().setBottomDepth(0);
	}
}