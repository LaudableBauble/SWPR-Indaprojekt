package main;

import java.util.Comparator;

import auxillary.Vector2;

/**
 * This comparator compares two Entity objects and sorts them by ascending depth.
 */
public class EntityDepthComparator implements Comparator<Entity>
{
	/**
	 * Compare two Entity objects to each other by their depth values.
	 */
	@Override
	public int compare(Entity e1, Entity e2)
	{
		// The entities' depth data.
		Vector2 v1 = new Vector2(e1.getBody().getShape().getTopDepth(), e1.getBody().getPosition().y);
		Vector2 v2 = new Vector2(e2.getBody().getShape().getTopDepth(), e2.getBody().getPosition().y);

		// Compare the entities to each other.
		if (v1.x < v2.x)
		{
			return -1;
		}
		else if (v1.x > v2.x)
		{
			return 1;
		}
		else if (v1.x == v2.x)
		{
			if (v1.y < v2.y)
			{
				return -1;
			}
			else if (v1.y > v2.y) { return 1; }
		}

		// The entities' depth values are equal, return 0.
		return 0;
	}
}
