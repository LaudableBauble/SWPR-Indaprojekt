package screens;

import infrastructure.Camera2D;
import infrastructure.EntityInfoPanel;
import infrastructure.FileTree;
import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.SceneTree;
import infrastructure.ScreenManager;
import infrastructure.TimeSpan;
import input.InputManager;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.tree.DefaultMutableTreeNode;

import scenes.SmallDemoScene;

import main.Entity;
import main.Player;
import main.SceneManager;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;
import debug.DebugManager;
import events.EntitySelectEvent;
import events.EntitySelectEventListener;
import events.PathSelectEvent;
import events.PathSelectEventListener;

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

	// The player.
	private Player _Player;

	// The GUI.
	private JTabbedPane _Tabs;
	private FileTree _EntityTree;
	private SceneTree _SceneTree;
	private EntityInfoPanel _InfoPanel;
	private JMenuBar _MenuBar;

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

		// Set up the camera.
		_Camera = new Camera2D(screenManager.getWindowBounds(), new Vector2(3000, 3000));
		_Camera.setPosition(new Vector2(1000, 1000));

		// Create the scene manager, enable debug and add a scene.
		_SceneManager = new SceneManager(this, _Camera);
		DebugManager.getInstance().debug = true;
		_SceneManager.addScene(new SmallDemoScene(_SceneManager));

		// Create the player.
		_Player = new Player(_SceneManager.getCurrentScene());
		_Player.setName("Player");
		_Player.getBody().setBottomPosition(new Vector3(910, 1080, 100));
		_SceneManager.getCurrentScene().addEntity(_Player);
		DebugManager.getInstance().setDebugBody(_Player.getBody());

		// Create the GUI components.
		_Tabs = new JTabbedPane();
		_Tabs.setFocusable(false);
		_EntityTree = new FileTree(new File(Helper.ContentRoot + "/entities"));
		_SceneTree = new SceneTree(_SceneManager.getCurrentScene());
		_InfoPanel = new EntityInfoPanel(_SceneManager.getCurrentScene());
		_MenuBar = new JMenuBar();

		// Handle the tree events.
		_EntityTree.addEventListener(new PathSelectEventListener()
		{
			@Override
			public void handleEvent(PathSelectEvent event)
			{
				// Update the selected entity.
				changeSelectedEntity(event.Path);
			}
		});
		_SceneTree.addEventListener(new EntitySelectEventListener()
		{
			@Override
			public void handleEvent(EntitySelectEvent event)
			{
				// Update the selected entity.
				changeSelectedEntity(event.Entity);
			}
		});

		// Set up the menu bar.
		JMenu file = new JMenu("File");
		_MenuBar.add(file);

		// Set up file.
		JMenuItem openScene = new JMenuItem("Open Scene");
		openScene.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Create a file chooser and show it.
				JFileChooser fc = new JFileChooser(Helper.ContentRoot + "/scenes");
				int decision = fc.showOpenDialog(_ScreenManager.getGame().getWindow());

				// If the user wants to load a file, do so.
				if (decision == JFileChooser.APPROVE_OPTION)
				{
					// Convert the path into relative form.
					String path = fc.getSelectedFile().getPath();
					path = path.substring(path.indexOf("scenes") + 7);

					_SceneManager.addScene(Helper.loadScene(path, _SceneManager));
					_SceneManager.setCurrentScene(1);

					// Update the scene tree.
					_SceneTree.updateTree(_SceneManager.getCurrentScene());
				}
			}
		});
		JMenuItem saveScene = new JMenuItem("Save Scene");
		saveScene.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Save the current scene to file.
				Helper.saveScene(_SceneManager.getCurrentScene());
			}
		});
		JMenuItem saveEntity = new JMenuItem("Save Entity");
		saveScene.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Save the current entity to file.
				Helper.saveEntity(_SelectedEntity);
			}
		});
		file.add(openScene);
		file.add(saveScene);
		file.add(saveEntity);

		// Add the GUI components to the frame.
		_Tabs.add("Entities", _EntityTree);
		_Tabs.add("Scene", _SceneTree);
		screenManager.getGame().getWindow().add(_Tabs, BorderLayout.WEST);
		screenManager.getGame().getWindow().add(_InfoPanel, BorderLayout.EAST);
		screenManager.getGame().getWindow().setJMenuBar(_MenuBar);
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

		// Set the info panel's entity.
		setSelectedEntity(_SceneManager.getCurrentScene().getEntities().get(0));

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
			updateSelectedEntity(input);

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
	 * Move the selected entity according to input.
	 * 
	 * @param input
	 *            The input to go by.
	 */
	private void updateSelectedEntity(InputManager input)
	{
		// If the selected entity is null, quit.
		if (_SelectedEntity == null) { return; }

		// The mouse position in the world.
		_SelectedEntity.getBody().getShape().setLayeredPosition(_Camera.convertScreenToWorld(input.mousePosition()));

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
	}

	/**
	 * Add the selected entity to the scene.
	 */
	private void addSelectedEntityToScene()
	{
		// If the entity is currently colliding with something, stop here.
		if (_SelectedEntity.getBody().getCollisions().size() != 0) { return; }

		// Because the entity has already been added to the scene, all we do is not remove him from it.
		_SelectedEntity.getBody().setIsImmaterial(false);
		_SelectedEntity = null;
		_SceneTree.updateTree();
	}

	/**
	 * Update the currently selected entity and consequently the info panel's entity.
	 * 
	 * @param path
	 *            The path of the entity.
	 */
	private void changeSelectedEntity(String path)
	{
		// Remove the old entity from the scene and physics simulator.
		if (_SelectedEntity != null)
		{
			_SceneManager.getCurrentScene().removeEntity(_SelectedEntity);
		}

		// Create an entity from the selected node and add it to the scene.
		Entity entity = loadEntity(path);
		_SceneManager.getCurrentScene().addEntity(entity);

		// Add the entity to the info panel and debug manager.
		setSelectedEntity(entity);
	}

	/**
	 * Update the currently selected entity and consequently the info panel's entity.
	 * 
	 * @param entity
	 *            The entity to select.
	 */
	private void changeSelectedEntity(Entity entity)
	{
		// Remove the old entity from the scene and physics simulator.
		if (_SelectedEntity != null)
		{
			_SceneManager.getCurrentScene().removeEntity(_SelectedEntity);
		}

		// Add the entity to the scene.
		_SceneManager.getCurrentScene().addEntity(entity);

		// Add the entity to the info panel and debug manager.
		setSelectedEntity(entity);
	}

	/**
	 * Load the entity mapped at the specified path.
	 * 
	 * @param path
	 *            The path of the entity to load. Assumes that the path is relative to the root of the game, ie. src folder.
	 * @return The loaded entity.
	 */
	private Entity loadEntity(String path)
	{
		// Load the entity from file. Remove the first 17 characters from the path (src/data/entities).
		Entity entity = Helper.loadEntity(path.substring(Helper.ContentRoot.length() + 9));
		// Add the body to the physics simulator.
		_SceneManager.getCurrentScene().getPhysicsSimulator().addBody(entity.getBody());

		// Return the entity.
		return entity;
	}

	/**
	 * Set the selected entity.
	 * 
	 * @param entity
	 *            The new entity to be selected.
	 */
	private void setSelectedEntity(Entity entity)
	{
		// Add the entity to the info panel and debug manager.
		_SelectedEntity = entity;
		_InfoPanel.setEntity(entity);
		DebugManager.getInstance().setDebugBody(entity.getBody());
	}
}
