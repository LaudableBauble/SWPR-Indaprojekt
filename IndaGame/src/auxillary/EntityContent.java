package auxillary;

import graphics.Sprite;
import graphics.SpriteManager;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import main.Entity;

import physics.Body;

/**
 * This class is an intermediate step for an entity. Every time an entity is serialized or deserialized it needs to be converted into this.
 */
@XmlRootElement(name = "Entity")
public class EntityContent
{
	// The name of the entity. Primarily used as file name when serialized.
	@XmlAttribute(name = "Name")
	protected String _Name;
	@XmlElement(name = "Sprites")
	protected SpriteManager _Sprites;
	@XmlElement(name = "Body")
	protected Body _Body;

	/**
	 * Empty constructor for an entity content. Important for the XML-binding.
	 */
	public EntityContent()
	{

	}

	/**
	 * Create a content-form for a given entity.
	 * 
	 * @param entity
	 *            The entity to convert into content-form.
	 * @return The entity content.
	 */
	public static EntityContent createContent(Entity entity)
	{
		// Create a new entity content holder.
		EntityContent content = new EntityContent();

		// Populate it with data.
		content.setName(entity.getName());
		content.setSprites(entity.getSprites());
		content.setBody(entity.getBody());

		// Return the content data.
		return content;
	}

	/**
	 * Create an entity from this content data.
	 * 
	 * @return An entity.
	 */
	public Entity createEntity()
	{
		try
		{
			// Create the entity.
			Entity entity = new Entity(_Body.getPhysicsSimulator());

			// Populate with with data.
			entity.setName(_Name);
			entity.setSprites(_Sprites);
			entity.setBody(_Body);

			// For all sprites, set their sprite manager.
			for (Sprite sprite : _Sprites.getSprites())
			{
				sprite.setSpriteManager(_Sprites);
			}

			// Load the sprites' content.
			_Sprites.loadContent();

			// Return the entity.
			return entity;
		}
		catch (Exception e)
		{
			System.out.println(this + ": Create Entity Error - " + e.getMessage());
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
	 * Set the entity's body.
	 * 
	 * @param body
	 *            The body of the entity.
	 */
	public void setBody(Body body)
	{
		_Body = body;
	}

	/**
	 * Set the manager of all the entity's sprites.
	 * 
	 * @param sprites
	 *            The new sprite manager.
	 */
	public void setSprites(SpriteManager sprites)
	{
		_Sprites = sprites;
	}
}
