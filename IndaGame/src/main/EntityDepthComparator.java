package main;

import java.util.Comparator;

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
		// Compare the entities to each other.
		if (e1.getDepth() < e2.getDepth())
		{
			return -1;
		}
		else if (e1.getDepth() > e2.getDepth()) { return 1; }

		// The entities' depth values are equal, return 0.
		return 0;
	}
}
