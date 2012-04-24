package infrastructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

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

	// The depth inputbox.
	private JSpinner _Spinner;

	/**
	 * Constructor for an entity info panel.
	 */
	public EntityInfoPanel()
	{
		// Set some settings.
		setLayout(new BorderLayout());
		setOpaque(false);
		setVisible(true);
		setSize(150, 200);
		setPreferredSize(getSize());

		// Add some components.
		_Spinner = new JSpinner(new SpinnerNumberModel());
		_Spinner.setFocusable(false);
		for (Component component : _Spinner.getEditor().getComponents())
		{
			component.setFocusable(false);
		}
		add(_Spinner, BorderLayout.SOUTH);

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
		g.drawString("Entity", 10, 50);
		g.drawImage(_Image, (int) _ImagePosition.x, (int) _ImagePosition.y, null);
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
