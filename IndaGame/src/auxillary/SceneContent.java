package auxillary;

import java.util.ArrayList;

import graphics.Sprite;
import graphics.SpriteManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import main.Entity;
import main.Scene;
import main.SceneManager;

import physics.Body;

/**
 * This class is an intermediate step for a scene. Every time a scene is serialized or deserialized it needs to be converted into this.
 */
@XmlRootElement(name = "Scene")
@XmlAccessorType(XmlAccessType.NONE)
public class SceneContent
{
	// The name of the scene. Primarily used as file name when serialized.
	@XmlAttribute(name = "Name")
	protected String _Name;
	@XmlElement(name = "Entities")
	protected ArrayList<EntityContent> _Entities;

	/**
	 * Empty constructor for a scene content. Important for the XML-binding.
	 */
	public SceneContent()
	{
		// Create the list of entities.
		_Entities = new ArrayList<EntityContent>();
	}

	/**
	 * Create a content-form for a given scene.
	 * 
	 * @param scene
	 *            The scene to convert into content-form.
	 * @return The scene content.
	 */
	public static SceneContent createContent(Scene scene)
	{
		// Create a new entity content holder.
		SceneContent content = new SceneContent();

		// Populate it with data.
		content.setName(scene.getName());
		for (Entity entity : scene.getEntities())
		{
			content.getEntities().add(EntityContent.createContent(entity));
		}

		// Return the content data.
		return content;
	}

	/**
	 * Create a scene from this content data.
	 * 
	 * @param manager
	 *            The scene manager which will handle the scene.
	 * @return The scene.
	 */
	public Scene createScene(SceneManager manager)
	{
		try
		{
			// Create the scene.
			Scene scene = new Scene(manager);

			// Populate with with data.
			scene.setName(_Name);

			// For all entity contents; convert them, add their bodies to the physics simulator and add them to the scene.
			for (EntityContent content : _Entities)
			{
				Entity entity = content.createEntity();
				scene.getPhysicsSimulator().addBody(entity.getBody());
				scene.addEntity(entity);
			}

			// Return the scene.
			return scene;
		}
		catch (Exception e)
		{
			System.out.println(this + ": Create Scene Error - " + e.getMessage());
		}

		// No entity to return.
		return null;
	}

	/**
	 * Set the entity's name.
	 * 
	 * @param name
	 *            The new name of the entity.
	 */
	public void setName(String name)
	{
		_Name = name;
	}

	/**
	 * Get the entities of this scene.
	 * 
	 * @return The list of entities.
	 */
	public ArrayList<EntityContent> getEntities()
	{
		return _Entities;
	}

	/**
	 * Set the entities of this scene.
	 * 
	 * @param entities
	 *            The list of entities.
	 */
	public void setEntities(ArrayList<EntityContent> entities)
	{
		_Entities = entities;
	}
}
