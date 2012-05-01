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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import scenes.SmallDemoScene;

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

	// The GUI.
	private JTabbedPane _Tabs;
	private FileTree _ImageTree;
	private FileTree _EntityTree;
	private EntityInfoPanel _InfoPanel;
	private JMenuBar _MenuBar;

	// The currently selected node.
	private DefaultMutableTreeNode _SelectedNode;
	// The selected entity.
	private Entity _SelectedEntity;

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

		// Create the GUI components.
		_Tabs = new JTabbedPane();
		_Tabs.setFocusable(false);
		_ImageTree = new FileTree(new File("src/data/images"));
		_EntityTree = new FileTree(new File("src/data/entities"));
		_InfoPanel = new EntityInfoPanel();
		_MenuBar = new JMenuBar();

		// Set up the menu bar.
		JMenu file = new JMenu("File");
		_MenuBar.add(file);

		// Set up file.
		JMenuItem loadScene = new JMenuItem("Load Scene");
		loadScene.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Save the current entity to file.
				_SelectedEntity.setName("Entity1");
				// Helper.saveEntity(_SelectedEntity);
			}
		});
		JMenuItem saveScene = new JMenuItem("Save Scene");
		saveScene.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Save the current entity to file.
				_SelectedEntity.setName("Entity1");
				Helper.saveEntity(_SelectedEntity);
			}
		});
		file.add(loadScene);
		file.add(saveScene);

		// Add the GUI components to the frame.
		_Tabs.add("Images", _ImageTree);
		_Tabs.add("Entities", _EntityTree);
		screenManager.getGame().getWindow().add(_Tabs, BorderLayout.WEST);
		screenManager.getGame().getWindow().add(_InfoPanel, BorderLayout.EAST);
		screenManager.getGame().getWindow().setJMenuBar(_MenuBar);

		// Set up the camera.
		_Camera = new Camera2D(new Rectangle(0, 0, (int) screenManager.getWindowBounds().x, (int) screenManager.getWindowBounds().y), new Rectangle(0, 0, 3000, 3000));
		_Camera.setPosition(new Vector2(1000, 1000));

		// Create the scene manager.
		_SceneManager = new SceneManager(_Camera);

		// Enable debug.
		DebugManager.getInstance().debug = true;

		// Create the scene.
		_SceneManager.addScene(new SmallDemoScene(_SceneManager));
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

		// Set the info panel's entity.
		_InfoPanel.setEntity(_SceneManager.getCurrentScene().getEntities().get(0));

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
			// The mouse position in the world.
			Vector2 mouse = _Camera.convertScreenToWorld(input.mousePosition());

			// Due to unknown error getting an accurate mouse position, subtract a bit.
			_SelectedEntity.getBody().getShape().setLayeredPosition(new Vector2(mouse.x - 145, mouse.y));

			// If the user has pressed the left mouse button, try to add the entity to the scene.
			if (input.isMouseButtonClicked(1))
			{
				addSelectedEntityToScene();
			}

			// If to increase the entity's depth.
			if (input.isKeyDown(KeyEvent.VK_2))
			{
				// The entity's bottom depth position.
				_SelectedEntity.getBody().getShape().setBottomDepth(_SelectedEntity.getBody().getShape().getBottomDepth() + .8);
			}
			// If to decrease the entity's depth.
			else if (input.isKeyDown(KeyEvent.VK_1))
			{
				// The entity's bottom depth position.
				_SelectedEntity.getBody().getShape().setBottomDepth(_SelectedEntity.getBody().getShape().getBottomDepth() - .8);
			}

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
		// If the tree's selected node does not match the one stored, change them.
		if (_SelectedNode != ((FileTree) _Tabs.getSelectedComponent()).getSelectedNode() || force)
		{
			// Switch selected node.
			_SelectedNode = ((FileTree) _Tabs.getSelectedComponent()).getSelectedNode();

			// Remove the old entity from the scene and physics simulator.
			if (_SelectedEntity != null)
			{
				_SceneManager.getCurrentScene().removeEntity(_SelectedEntity);
			}

			// Create an entity from the selected node.
			Entity entity = new Entity(_SceneManager.getCurrentScene().getPhysicsSimulator());

			// If the node is not null, continue.
			if (_SelectedNode != null)
			{
				// Load the entity's content. Remove the first 10 characters from parent (src/data/images).
				entity.loadContent(_SelectedNode.getParent().toString().substring(15) + "\\" + _SelectedNode.toString());
				// Add the entity to the scene.
				_SceneManager.getCurrentScene().addEntity(entity);
			}

			// Add the entity to the info panel.
			_InfoPanel.setEntity(entity);
		}
	}
}
