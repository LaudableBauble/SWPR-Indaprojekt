package screens;

import infrastructure.Camera2D;
import infrastructure.EntityInfoPanel;
import infrastructure.Enums.DepthDistribution;
import infrastructure.FileTree;
import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.ScreenManager;
import infrastructure.TimeSpan;
import input.InputManager;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import main.Entity;
import main.Player;
import main.Scene;
import main.SceneManager;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;
import debug.DebugManager;

/**
 * This screen implements the actual game logic by reusing components from before.
 */
public class MapEditorScreen extends GameScreen
{
	// The GameFont, the main font of the game.
	private Font _GameFont;
	// The scene manager.
	private SceneManager _SceneManager;
	// The camera.
	private Camera2D _Camera;
	// The scene.
	private Scene _Scene;

	// The GUI.
	private JTabbedPane _Tabs;
	private FileTree _ImageTree;
	private FileTree _EntityTree;
	private EntityInfoPanel _InfoPanel;

	// The currently selected node.
	private DefaultMutableTreeNode _SelectedNode;
	// The selected entity.
	private Entity _SelectedEntity;
	// Whether to move entities on the y-axis.
	private boolean _MoveYAxis;
	// The x-coordinate to lock the mouse to.
	private int _LockX;

	// The player.
	private Player _Player;

	// Entities.
	private Entity _Shelf;
	private Entity _Block1;
	private Entity _Block2;
	private Entity _Stairs;
	private Entity _Floor;

	/**
	 * Constructor for a map editor screen.
	 * 
	 * @param screenManager
	 *            This screen's manager.
	 */
	public MapEditorScreen(ScreenManager screenManager)
	{
		// Set the time it takes for the Screen to transition on and off.
		_TransitionOnTime = TimeSpan.FromSeconds(1.5);
		_TransitionOffTime = TimeSpan.FromSeconds(0.5);

		// Set up some variables.
		_LockX = -1;
		_MoveYAxis = false;

		// Create the GUI components.
		_Tabs = new JTabbedPane();
		_Tabs.setFocusable(false);
		_ImageTree = new FileTree(new File("src/images"));
		_EntityTree = new FileTree(new File("src/images"));
		_InfoPanel = new EntityInfoPanel();

		// Add the GUI components to the frame.
		_Tabs.add("Images", _ImageTree);
		_Tabs.add("Entities", _EntityTree);
		screenManager.getGame().getWindow().add(_Tabs, BorderLayout.WEST);
		screenManager.getGame().getWindow().add(_InfoPanel, BorderLayout.EAST);

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

		// Create the shelf.
		_Shelf = new Entity(_Scene.getPhysicsSimulator());
		_Shelf.getBody().setPosition(new Vector3(1100, 1100, 0));
		_Shelf.getBody().setIsStatic(true);

		// Create a block.
		_Block1 = new Entity(_Scene.getPhysicsSimulator());
		_Block1.getBody().setPosition(new Vector3(950, 933.5, 0));
		_Block1.getBody().setIsStatic(true);

		// Create a block.
		_Block2 = new Entity(_Scene.getPhysicsSimulator());
		_Block2.getBody().setPosition(new Vector3(1000, 1020, 0));
		_Block2.getBody().setIsStatic(true);

		// Create a staircase.
		_Stairs = new Entity(_Scene.getPhysicsSimulator());
		_Stairs.getBody().setPosition(new Vector3(930, 1080, 0));
		_Stairs.getBody().setIsStatic(true);
		_Stairs.getBody().getShape().setDepthDistribution(DepthDistribution.Right);

		// Create the floor.
		_Floor = new Entity(_Scene.getPhysicsSimulator());
		_Floor.getBody().setPosition(new Vector3(1000, 1000, 0));
		_Floor.getBody().setIsStatic(true);

		// Add all entities to the scene.
		_Scene.addEntity(_Player);
		_Scene.addEntity(_Shelf);
		_Scene.addEntity(_Block1);
		_Scene.addEntity(_Block2);
		_Scene.addEntity(_Stairs);
		_Scene.addEntity(_Floor);
	}

	/**
	 * Load graphics content for the map editor.
	 */
	public void loadContent()
	{
		// Set the background color to blue.
		_ScreenManager.getGame().getWindow().setBackBufferColor(Helper.CornFlowerBlue);

		// Load the scene manager's content.
		_SceneManager.loadContent();

		// Load the player's content.
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

		// Set the info panel's entity.
		_InfoPanel.setEntity(_Player);

		// Once the load has finished, we use ResetElapsedTime to tell the game's
		// timing mechanism that we have just finished a very long frame, and that
		// it should not try to catch up.
		_ScreenManager.getGame().getGameTimer().resetElapsedTime();
	}

	/**
	 * Lets the editor respond to player input. Unlike the Update method, this will only be called when the screen is active.
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

			// Update the selected entity's position.
			if (!_MoveYAxis)
			{
				_SelectedEntity.getBody().getShape().setLayeredPosition(_Camera.convertScreenToWorld(input.mousePosition()));
			}
			else
			{
				_SelectedEntity.getBody().getShape().setBottomDepth(_SelectedEntity.getBody().getPosition().y - _Camera.convertScreenToWorld(input.mousePosition()).y);
			}

			// If the user has pressed the left mouse button, try to add the entity to the scene.
			if (input.isMouseButtonClicked(1))
			{
				addSelectedEntityToScene();
			}

			// If the user holds down the shift button, move the entity on the z-axis only.
			_MoveYAxis = input.isKeyDown(KeyEvent.VK_SHIFT);

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
	 * Updates the state of the editor. This method checks the GameScreen.IsActive property, so the screen will stop updating when the pause menu is active, or if you tab away to a different
	 * application.
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
		// Update the selected entity.
		_SelectedEntity = _InfoPanel.getEntity();

		// Update the selected node.
		updateSelectedNode(false);
	}

	/**
	 * Draws the editor screen.
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

	/**
	 * Add the selected entity to the scene.
	 */
	private void addSelectedEntityToScene()
	{
		// If the entity is not currently colliding with anything, add it.
		if (_SelectedEntity.getBody().getCollisions().size() == 0)
		{
			// Because the entity has already been added to the scene, all we do is not remove him from it.
			_SelectedEntity.getBody().setIsImmaterial(false);
			_SelectedEntity = null;

			// Get a new entity to add.
			updateSelectedNode(true);
		}
	}

	/**
	 * Update the currently selected node and consequently the info panel's entity.
	 * 
	 * @param force
	 *            Force an update regardless if the node has changed or not.
	 */
	private void updateSelectedNode(boolean force)
	{
		// If the tree's selected node does not match the one stored, change 'em.
		if (_SelectedNode != ((FileTree) _Tabs.getSelectedComponent()).getSelectedNode() || force)
		{
			// Switch selected node.
			_SelectedNode = ((FileTree) _Tabs.getSelectedComponent()).getSelectedNode();

			// Remove the old entity from the scene and physics simulator.
			if (_SelectedEntity != null)
			{
				_Scene.removeEntity(_SelectedEntity);
			}

			// Create an entity from the selected node.
			Entity entity = new Entity(_Scene.getPhysicsSimulator());

			// If the node is not null, continue.
			if (_SelectedNode != null)
			{
				// Load the entity's content. Remove the first 10 characters from parent (src/images).
				entity.loadContent(_SelectedNode.getParent().toString().substring(10) + "\\" + _SelectedNode.toString());
				// Add the entity to the scene.
				_Scene.addEntity(entity);
			}

			// Add the entity to the info panel.
			_InfoPanel.setEntity(entity);
		}
	}

	/**
	 * Lock the mouse axis to the z-axis. Obsolete?
	 * 
	 * @param lock
	 *            Whether to lock or unlock the mouse axis.
	 */
	private void lockAxis(boolean lock)
	{
		// If to unlock, do so and then end.
		if (!lock)
		{
			_LockX = -1;
			return;
		}

		// If the saved x-coordinate is -1, then overwrite it.
		if (_LockX == -1)
		{
			_LockX = (int) InputManager.getInstance().mousePosition().x;
		}

		// Lock the mouse to this x-coordinate.
		try
		{
			(new Robot()).mouseMove(_LockX, (int) InputManager.getInstance().mousePosition().y);
		}
		catch (AWTException e)
		{

		}
	}
}
