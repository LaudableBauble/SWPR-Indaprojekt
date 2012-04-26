package screens;

import infrastructure.Camera2D;
import infrastructure.Enums.DepthDistribution;
import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.ScreenManager;
import infrastructure.TimeSpan;
import input.InputManager;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import main.Entity;
import main.Player;
import main.Character;
import main.Scene;
import main.SceneManager;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;
import debug.DebugManager;

/**
 * This screen implements the actual game logic by reusing components from before.
 */
public class GameplayScreen extends GameScreen
{
	// The GameFont, the main font of the game.
	private Font _GameFont;
	// The scene manager.
	private SceneManager _SceneManager;
	// The camera.
	private Camera2D _Camera;

	// The scene.
	private Scene _Scene;

	// The player.
	private Player _Player;

	// Character.
	private Character _Character;

	// Entities.
	private Entity _Shelf;
	private Entity _Block1;
	private Entity _Block2;
	private Entity _DarkBlock1;
	private Entity _DarkBlock2;
	private Entity _DarkBlock3;
	private Entity _DarkBlock4;
	private Entity _DarkBlock5;
	private Entity _Stairs1;
	private Entity _Stairs2;
	private Entity _Stairs3;
	private Entity _Stairs4;
	private Entity _Pathway1;
	private Entity _Pathway2;
	private Entity _Pathway3;
	private Entity _Pathway4;
	private Entity _PathwayArch1;
	private Entity _PathwayPlatform1;
	private Entity _PathwayPlatform2;
	private Entity _Floor;

	/**
	 * Constructor for the game screen.
	 * 
	 * @param screenManager
	 *            This screen's manager.
	 */
	public GameplayScreen(ScreenManager screenManager)
	{
		// Set the time it takes for the Screen to transition on and off.
		_TransitionOnTime = TimeSpan.FromSeconds(1.5);
		_TransitionOffTime = TimeSpan.FromSeconds(0.5);

		// Set up the camera.
		_Camera = new Camera2D(new Rectangle(0, 0, (int) screenManager.getWindowBounds().x, (int) screenManager.getWindowBounds().y), new Rectangle(0, 0, 3000, 3000));
		_Camera.setPosition(new Vector2(1000, 1000));

		// Create the scene manager.
		_SceneManager = new SceneManager(_Camera);

		// Enable debug.
		DebugManager.getInstance().debug = true;

		// Create the scene.
		_Scene = _SceneManager.addScene(new Scene());

		// Create the player.
		_Player = new Player(_Scene.getPhysicsSimulator());
		_Player.getBody().setBottomPosition(new Vector3(910, 1080, 100));
		DebugManager.getInstance().setDebugBody(_Player.getBody());

		// Create a character.
		_Character = new main.Character(_Scene.getPhysicsSimulator(), "hydra", 3);
		_Character.getBody().setPosition(new Vector3(1040, 1010, 50));

		// Create the shelf.
		_Shelf = new Entity(_Scene.getPhysicsSimulator());
		_Shelf.getBody().setPosition(new Vector3(1100, 1100, 0));
		_Shelf.getBody().setIsStatic(true);

		// Create a block.
		_Block1 = new Entity(_Scene.getPhysicsSimulator());
		_Block1.getBody().setPosition(new Vector3(950, 700, 0));
		_Block1.getBody().setIsStatic(true);

		// Create a block.
		_Block2 = new Entity(_Scene.getPhysicsSimulator());
		_Block2.getBody().setPosition(new Vector3(1100, 700, 0));
		_Block2.getBody().setIsStatic(true);
		
		// Create a dark block (left of pathway arch).
		_DarkBlock1 = new Entity(_Scene.getPhysicsSimulator());
		_DarkBlock1.getBody().setPosition(new Vector3(852, 1000, 0));
		_DarkBlock1.getBody().setIsStatic(true);
		
		// Create a dark block (right of pathway arch).
		_DarkBlock2 = new Entity(_Scene.getPhysicsSimulator());
		_DarkBlock2.getBody().setPosition(new Vector3(1149, 1000, 0));
		_DarkBlock2.getBody().setIsStatic(true);
		
		// Create a dark block (south of pathway platform 1).
		_DarkBlock3 = new Entity(_Scene.getPhysicsSimulator());
		_DarkBlock3.getBody().setPosition(new Vector3(900, 1183, 0));
		_DarkBlock3.getBody().setIsStatic(true);
		
		// Create a dark block (south of pathway platform 2).
		_DarkBlock4 = new Entity(_Scene.getPhysicsSimulator());
		_DarkBlock4.getBody().setPosition(new Vector3(1120, 1183, 0));
		_DarkBlock4.getBody().setIsStatic(true);
		
		// Create a dark block (north of pathway arch, main stairs).
		_DarkBlock5 = new Entity(_Scene.getPhysicsSimulator());
		_DarkBlock5.getBody().setPosition(new Vector3(1000, 872, 0));
		_DarkBlock5.getBody().setIsStatic(true);

		// Create a staircase.
		_Stairs1 = new Entity(_Scene.getPhysicsSimulator());
		_Stairs1.getBody().setPosition(new Vector3(650, 1100, 0));
		_Stairs1.getBody().setIsStatic(true);
		_Stairs1.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create a staircase.
		_Stairs2 = new Entity(_Scene.getPhysicsSimulator());
		_Stairs2.getBody().setPosition(new Vector3(770, 920, 0));
		_Stairs2.getBody().setIsStatic(true);
		_Stairs2.getBody().getShape().setDepthDistribution(DepthDistribution.Right);
		
		// Create a staircase (left of dark block 2).
		_Stairs3 = new Entity(_Scene.getPhysicsSimulator());
		_Stairs3.getBody().setPosition(new Vector3(1056.5, 1033, 0));
		_Stairs3.getBody().setIsStatic(true);
		_Stairs3.getBody().getShape().setDepthDistribution(DepthDistribution.Right);
		
		// Create a staircase (north of dark block 5).
		_Stairs4 = new Entity(_Scene.getPhysicsSimulator());
		_Stairs4.getBody().setPosition(new Vector3(1000, 910.5, 0));
		_Stairs4.getBody().setIsStatic(true);
		_Stairs4.getBody().getShape().setDepthDistribution(DepthDistribution.Top);

		// Create a pathway.
		_Pathway1 = new Entity(_Scene.getPhysicsSimulator());
		_Pathway2 = new Entity(_Scene.getPhysicsSimulator());
		_Pathway3 = new Entity(_Scene.getPhysicsSimulator());
		_Pathway4 = new Entity(_Scene.getPhysicsSimulator());
		_PathwayArch1 = new Entity(_Scene.getPhysicsSimulator());
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
		
		//Create a pathway platform (south of dark block 1).
		_PathwayPlatform1 = new Entity(_Scene.getPhysicsSimulator());
		_PathwayPlatform1.getBody().setPosition(new Vector3(852, 1107.5, 0));
		_PathwayPlatform1.getBody().setIsStatic(true);
		
		//Create a pathway platform (south of dark block 2).
		_PathwayPlatform2 = new Entity(_Scene.getPhysicsSimulator());
		_PathwayPlatform2.getBody().setPosition(new Vector3(1149, 1107.5, 0));
		_PathwayPlatform2.getBody().setIsStatic(true);

		// Create the floor.
		_Floor = new Entity(_Scene.getPhysicsSimulator());
		_Floor.getBody().setPosition(new Vector3(1000, 1050, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		_Scene.addEntity(_Player);
		_Scene.addEntity(_Character);
		_Scene.addEntity(_Shelf);
		_Scene.addEntity(_Block1);
		_Scene.addEntity(_Block2);
		_Scene.addEntity(_DarkBlock1);
		_Scene.addEntity(_DarkBlock2);
		_Scene.addEntity(_DarkBlock3);
		_Scene.addEntity(_DarkBlock4);
		_Scene.addEntity(_DarkBlock5);
		_Scene.addEntity(_Stairs1);
		_Scene.addEntity(_Stairs2);
		_Scene.addEntity(_Stairs3);
		_Scene.addEntity(_Stairs4);
		_Scene.addEntity(_Pathway1);
		_Scene.addEntity(_Pathway2);
		_Scene.addEntity(_Pathway3);
		_Scene.addEntity(_Pathway4);
		_Scene.addEntity(_PathwayArch1);
		_Scene.addEntity(_PathwayPlatform1);
		_Scene.addEntity(_PathwayPlatform2);
		_Scene.addEntity(_Floor);
	}

	/**
	 * Load graphics content for the game.
	 */
	public void loadContent()
	{
		// Set the background color to blue.
		_ScreenManager.getGame().getWindow().setBackBufferColor(Helper.CornFlowerBlue);

		// Load the scene manager's content.
		_SceneManager.loadContent();

		// Load the player's content.
		_Player.loadContent();
		_Character.loadContent();
		_Shelf.loadContent("Bookshelf[1].png", 12);
		_Block1.loadContent("ElevatedBlock[3].png", 48);
		_Block2.loadContent("ElevatedBlock[2].png", 85);
		_DarkBlock1.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock2.loadContent("DarkTiledBlock[2].png", 134);
		_DarkBlock3.loadContent("DarkTiledBlock[1].png", 70);
		_DarkBlock4.loadContent("DarkTiledBlock[1].png", 70);
		_DarkBlock5.loadContent("DarkTiledBlock[1].png", 70);
		_Stairs1.loadContent("StoneStairsRight[2].png", 33);
		_Stairs2.loadContent("StoneStairsRight[3].png", 33);
		_Stairs3.loadContent("StoneStairsRight[3].png", 33);
		_Stairs4.loadContent("StoneStairsTop[1].png", 30);
		_Pathway1.loadContent("StonePathwayBlock[1].png", 33);
		_Pathway2.loadContent("StonePathwayBlock[1].png", 33);
		_Pathway3.loadContent("StonePathwayBlock[1].png", 33);
		_Pathway4.loadContent("StonePathwayBlock[1].png", 33);
		_PathwayArch1.loadContent("StonePathwayArch[1].png", 33);
		_PathwayPlatform1.loadContent("StonePathwayBlock[2].png", 81);
		_PathwayPlatform2.loadContent("StonePathwayBlock[4].png", 81);
		_Floor.loadContent("WoodTiledFloor[1].png");

		// Set their depths.
		_Shelf.getBody().getShape().setBottomDepth(1);
		_Block1.getBody().getShape().setBottomDepth(1);
		_Block2.getBody().getShape().setBottomDepth(1);
		_DarkBlock1.getBody().getShape().setBottomDepth(1);
		_DarkBlock2.getBody().getShape().setBottomDepth(1);
		_DarkBlock3.getBody().getShape().setBottomDepth(1);
		_DarkBlock4.getBody().getShape().setBottomDepth(1);
		_DarkBlock5.getBody().getShape().setBottomDepth(1);
		_Stairs1.getBody().getShape().setBottomDepth(1);
		_Stairs2.getBody().getShape().setBottomDepth(1);
		_Stairs3.getBody().getShape().setBottomDepth(1);
		_Stairs4.getBody().getShape().setDepth(48);
		_Stairs4.getBody().getShape().setBottomDepth(1);
		_Pathway1.getBody().getShape().setBottomDepth(1);
		_Pathway2.getBody().getShape().setBottomDepth(1);
		_Pathway3.getBody().getShape().setBottomDepth(1);
		_Pathway4.getBody().getShape().setBottomDepth(1);
		_PathwayArch1.getBody().getShape().setBottomDepth(22);
		_PathwayPlatform1.getBody().getShape().setBottomDepth(37);
		_PathwayPlatform2.getBody().getShape().setBottomDepth(37);
		_Floor.getBody().getShape().setBottomDepth(0);

		// Once the load has finished, we use ResetElapsedTime to tell the game's
		// timing mechanism that we have just finished a very long frame, and that
		// it should not try to catch up.
		_ScreenManager.getGame().getGameTimer().resetElapsedTime();
	}

	/**
	 * Lets the game respond to player input. Unlike the Update method, this will only be called when the gameplay screen is active.
	 * 
	 * @param input
	 *            The input manager that relays the state of input.
	 */
	public void handleInput(InputManager input)
	{
		// If the InputState is null, throw an exception.
		// if (input == null) { throw new ArgumentNullException("input"); }

		// If the game should be paused, bring up the pause game screen.
		if (input.getPauseGame())
		{
			_ScreenManager.addScreen(new PauseMenuScreen());
		}
		// Otherwise let the game instance handle input as usual.
		else
		{
			// Let the scene manager respond to input.
			_SceneManager.handleInput(input);

			// If to zoom in.
			if (input.isKeyDown(KeyEvent.VK_O))
			{
				_Camera.zoom(.01f);
			}
			// If to zoom out.
			if (input.isKeyDown(KeyEvent.VK_P))
			{
				_Camera.zoom(-.01f);
			}
			// If to move up.
			if (input.isKeyDown(KeyEvent.VK_I))
			{
				_Camera.move(new Vector2(0, -1));
			}
			// If to move right.
			if (input.isKeyDown(KeyEvent.VK_L))
			{
				_Camera.move(new Vector2(1, 0));
			}
			// If to move down.
			if (input.isKeyDown(KeyEvent.VK_K))
			{
				_Camera.move(new Vector2(0, 1));
			}
			// If to move left.
			if (input.isKeyDown(KeyEvent.VK_J))
			{
				_Camera.move(new Vector2(-1, 0));
			}
		}
	}

	/**
	 * Updates the state of the game. This method checks the GameScreen.IsActive property, so the game will stop updating when the pause menu is active, or if you tab away to a different application.
	 * 
	 * @param gameTime
	 *            The timer of this game.
	 * @param otherScreenHasFocus
	 *            If another screen has focus.
	 * @param coveredByOtherScreen
	 *            If this screen is covered by another screen.
	 */
	public void update(GameTimer gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		// Update the game by updating this screen.
		super.update(gameTime, otherScreenHasFocus, coveredByOtherScreen);

		// Update the scene manager.
		_SceneManager.update(gameTime);
	}

	/**
	 * Draws the gameplay screen.
	 * 
	 * @param gameTime
	 *            The timer instance that runs the game.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{
		// If the game is transitioning on or off, fade it out to black.
		if (_TransitionPosition > 0)
		{
			_ScreenManager.fadeBackBufferToBlack(255 - getTransitionAlpha());
		}

		// Let the scene manager draw the current scene.
		_SceneManager.draw(graphics);
	}
}
