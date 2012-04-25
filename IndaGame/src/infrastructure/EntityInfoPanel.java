package infrastructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import auxillary.Helper;
import auxillary.Vector2;

import main.Entity;

/**
 * An entity info panel displays information about an entity.
 */
public class EntityInfoPanel extends JPanel
{
	// The entity.
	private Entity _Entity;
	// The entity's main image.
	private BufferedImage _Image;
	// The origin of the image.
	private Vector2 _ImagePosition;

	// The entity name.
	private JLabel _Name;
	// The depth explanation.
	private JLabel _DepthDescription;
	// The depth spinner.
	private JSpinner _DepthSpinner;
	// The isStatic checkbox.
	private JCheckBox _StaticState;

	/**
	 * Constructor for an entity info panel.
	 */
	public EntityInfoPanel()
	{
		// Set some settings.
		setLayout(null);
		setOpaque(false);
		setVisible(true);
		setSize(150, 200);
		setPreferredSize(getSize());

		// Create the labels.
		_Name = new JLabel("Entity");
		_DepthDescription = new JLabel("Depth:");

		// Create the checkbox.
		_StaticState = new JCheckBox("Is Static?");
		_StaticState.setFocusable(false);
		_StaticState.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				// Set the entity to be static or dynamic.
				_Entity.getBody().setIsStatic((e.getStateChange() == ItemEvent.SELECTED));
			}
		});

		// Create the spinner.
		_DepthSpinner = new JSpinner(new SpinnerNumberModel());
		_DepthSpinner.setFocusable(false);
		for (Component component : _DepthSpinner.getEditor().getComponents())
		{
			component.setFocusable(false);
		}

		// Add all components to the panel.
		add(_Name);
		add(_DepthDescription);
		add(_DepthSpinner);
		add(_StaticState);

		// Set all components' position and size.
		_Name.setLocation(10, 50);
		_Name.setSize(100, 20);
		_DepthDescription.setLocation(10, 300);
		_DepthDescription.setSize(50, 20);
		_DepthSpinner.setLocation(60, 300);
		_DepthSpinner.setSize(50, 20);
		_StaticState.setLocation(10, 330);
		_StaticState.setSize(150, 20);

		// Initialize some variables.
		_Entity = new Entity(null);
		_Image = null;
		_ImagePosition = Vector2.empty();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		// Call the base method.
		super.paintComponents(g);

		// Draw a blank rectangle as base.
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Draw the entity's data.
		g.setColor(Color.black);
		g.drawImage(_Image, (int) _ImagePosition.x, (int) _ImagePosition.y, null);
	}

	/**
	 * Get the entity of this info panel.
	 * 
	 * @return The entity.
	 */
	public Entity getEntity()
	{
		return _Entity;
	}

	/**
	 * Set the entity to display information about.
	 * 
	 * @param entity
	 *            The entity to display information about.
	 * @return The entity.
	 */
	public Entity setEntity(Entity entity)
	{
		// Expect exceptions.
		try
		{
			// Set the entity and load its main image.
			_Entity = entity;
			_Entity.getBody().setIsImmaterial(true);
			_Image = Helper.loadImage(entity.getSprites().getSprite(0).getCurrentFrame().getPathName(), true);
			_ImagePosition = new Vector2((getWidth() / 2) - (_Image.getWidth() / 2), 100);
		}
		catch (Exception e)
		{
			System.out.println(this + ": Error setting entity. (" + e + ")");
		}

		// Return the entity.
		return entity;
	}
}
