package main;

import graphics.Frame;
import graphics.Sprite;
import graphics.SpriteManager;
import infrastructure.Enums.DepthDistribution;
import infrastructure.GameTimer;
import input.InputManager;

import java.awt.Graphics2D;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import physics.Body;
import physics.PhysicsSimulator;
import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

/**
 * An exit is a physical door connecting two scenes.
 */
public class Exit extends Entity
{
	// The scene to go to.
	private String _Goto;
	private Vector3 _Entrance;
	private boolean _IsActive;

	/**
	 * Constructor for an entity.
	 * 
	 * @param scene
	 *            The scene this entity is part of.
	 * @param enter
	 *            The scene to enter.
	 * @param entrance
	 *            The entrance position.
	 */
	public Exit(Scene scene, String enter, Vector3 entrance)
	{
		super(scene);
		initialize(enter, entrance);
	}

	/**
	 * Initialize the entity.
	 * 
	 * @param enter
	 *            The scene to enter.
	 * @param entrance
	 *            The entrance position.
	 */
	protected void initialize(String enter, Vector3 entrance)
	{
		// Call the base method.
		// super.initialize(scene);

		// Initialize the variables.
		_Goto = enter;
		_Entrance = entrance;
		_IsActive = true;
	}

	/**
	 * Get the name of goto scene of the exit.
	 * 
	 * @return The goto scene.
	 */
	public String getGotoScene()
	{
		return _Goto;
	}

	/**
	 * Get the entrance position of the exit.
	 * 
	 * @return The entrance position.
	 */
	public Vector3 getEntrance()
	{
		return _Entrance;
	}

	/**
	 * Set whether the exit is active or not.
	 * 
	 * @param isActive
	 *            Is the exit active.
	 */
	public void setIsActive(boolean isActive)

	{
		_IsActive = isActive;
	}
}