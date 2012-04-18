package graphics;

import infrastructure.Enums.Visibility;
import infrastructure.GameTimer;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import auxillary.Vector2;

/**
 * A sprite manager keeps track of a number of sprites. It is basically a collection of sprites.
 */
public class SpriteManager
{
	// The list of sprites.
	// Note: Every time the list needs to be iterated through use a new list to avoid exceptions when modifying it.
	private ArrayList<Sprite> _Sprites;

	/**
	 * Constructor for a sprite manager.
	 */
	public SpriteManager()
	{
		// Initialize the sprite manager.
		initialize();
	}

	/**
	 * Initialize the sprite manager.
	 */
	private void initialize()
	{
		// Initialize variables.
		_Sprites = new ArrayList<Sprite>();
	}

	/**
	 * Load content.
	 */
	public void loadContent()
	{
		// If there's any sprites in the list, load their content.
		for (Sprite sprite : new ArrayList<Sprite>(_Sprites))
		{
			sprite.loadContent();
		}
	}

	/**
	 * Update the sprite manager and all sprites.
	 * 
	 * @param gameTime
	 *            The game timer.
	 */
	public void update(GameTimer gameTime)
	{
		// Update the sprites.
		update(gameTime, null);
	}

	/**
	 * Update the sprite manager and all sprites.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param position
	 *            A position all sprites should be drawed at.
	 */
	public void update(GameTimer gameTime, Vector2 position)
	{
		// Sort the list of sprites by their depth. This is vital for drawing them in the right order.
		Collections.sort(_Sprites, new SpriteDepthComparator());

		// Update all sprites.
		for (Sprite sprite : new ArrayList<Sprite>(_Sprites))
		{
			if (position != null)
			{
				sprite.setPosition(position);
			}
			
			sprite.update(gameTime);
		}
	}

	/**
	 * Draw all sprites to screen.
	 * 
	 * @param graphics
	 *            The graphics component to use.
	 */
	public void draw(Graphics2D graphics)
	{
		// Loop through all sprites and draw them.
		for (Sprite sprite : new ArrayList<Sprite>(_Sprites))
		{
			sprite.draw(graphics);
		}
	}

	/**
	 * Add a sprite.
	 * 
	 * @param sprite
	 *            The sprite to add.
	 * @return The added sprite.
	 */
	public Sprite addSprite(Sprite sprite)
	{
		// Add the sprite to the list and return it.
		_Sprites.add(sprite);
		sprite.setSpriteManager(this);
		return sprite;
	}

	/**
	 * Get a sprite's index from its tag.
	 * 
	 * @param tag
	 *            The tag of the sprite.
	 * @return The index of the sprite with the specified tag.
	 */
	public int getSpriteIndex(String tag)
	{
		return _Sprites.indexOf(getSprite(tag));
	}

	/**
	 * Get a sprite given its tag.
	 * 
	 * @param tag
	 *            The tag of the sprite.
	 * @return The sprite with the specified tag.
	 */
	public Sprite getSprite(String tag)
	{
		// Loop through the list of sprites and find the one with the right name.
		for (Sprite sprite : new ArrayList<Sprite>(_Sprites))
		{
			if (sprite.getTag().equals(tag)) { return sprite; }
		}

		// Return null, no sprite found.
		return null;
	}

	/**
	 * Get the sprite with the given index.
	 * 
	 * @param index
	 *            The index of the sprite.
	 * @return The sprite.
	 */
	public Sprite getSprite(int index)
	{
		// If the index is out of bounds, quit here.
		if (index >= _Sprites.size()) { return null; }

		// Return the sprite.
		return _Sprites.get(index);
	}

	/**
	 * Insert a sprite to a given position.
	 * 
	 * @param index
	 *            Where to insert the sprite.
	 * @param sprite
	 *            The sprite to insert.
	 */
	public void insertSprite(int index, Sprite sprite)
	{
		// If the index is out of bounds, quit here.
		if (index >= _Sprites.size()) { return; }

		// Set the sprite.
		_Sprites.set(index, sprite);
		sprite.setSpriteManager(this);
	}

	/**
	 * Remove a sprite given its tag.
	 * 
	 * @param tag
	 *            The tag of the sprite to be removed.
	 */
	public void removeSprite(String tag)
	{
		removeSprite(getSpriteIndex(tag));
	}

	/**
	 * Remove a sprite.
	 * 
	 * @param index
	 *            The index of the sprite to remove.
	 */
	public void removeSprite(int index)
	{
		_Sprites.remove(index);
	}

	/**
	 * Get all sprites managed by this manager.
	 * 
	 * @return The list of sprites.
	 */
	public ArrayList<Sprite> getSprites()
	{
		return new ArrayList<Sprite>(_Sprites);
	}

	/**
	 * Change the state of visibility for all sprites.
	 * 
	 * @param visibility
	 *            The new state of visibility.
	 */
	private void setVisibility(Visibility visibility)
	{
		for (Sprite sprite : new ArrayList<Sprite>(_Sprites))
		{
			sprite.setVisibility(visibility);
		}
	}

	/**
	 * Get the manager's sprite count.
	 */
	public int getSpriteCount()
	{
		return _Sprites.size();
	}
}
