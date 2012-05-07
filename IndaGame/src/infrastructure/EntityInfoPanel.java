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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

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
	// The entity position.
	private JLabel _Position;
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
		_Position = new JLabel("Position: ");

		// Create the checkbox.
		_StaticState = new JCheckBox("Is Static?");
		_StaticState.setFocusable(false);
		_StaticState.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				// Set the entity to be static or dynamic.
				_Entity.getBody().setIsStatic(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		// Add all components to the panel.
		add(_Name);
		add(_Position);
		add(_StaticState);

		// Set all components' position and size.
		_Name.setLocation(10, 40);
		_Name.setSize(100, 20);
		_Position.setLocation(10, 60);
		_Position.setSize(150, 20);
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

		// Update the entity's position.
		if (_Entity != null)
		{
			_Position.setText(Vector3.round(_Entity.getBody().getPosition(), 0).toString());
		}

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
			// Set the entity.
			_Entity = entity;

			// Pass along the entity's data.
			_StaticState.setSelected(_Entity.getBody().getIsStatic());

			// Initialize the entity to the state of this info panel.
			_Entity.getBody().setIsImmaterial(true);
			_Image = Helper.loadImage(entity.getSprites().getSprite(0).getCurrentFrame().getPathName(), true);
			_ImagePosition = new Vector2((getWidth() / 2) - (_Image.getWidth() / 2), 200);
		}
		catch (Exception e)
		{
			System.out.println(this + ": Error setting entity. (" + e + ")");
		}

		// Return the entity.
		return entity;
	}
}
