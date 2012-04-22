package main;

import java.util.Comparator;

import physics.Shape;

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
		double d1 = e1.getBody().getShape().getBottomDepth();
		double d2 = e2.getBody().getShape().getBottomDepth();
		String s1 = e1.getSprites().getSprite(0).getCurrentFrame().getPathName();
		String s2 = e2.getSprites().getSprite(0).getCurrentFrame().getPathName();

		// Whether the entities' shapes overlap.
		int overlap = Shape.isOverlaping(e1.getBody().getShape(), e2.getBody().getShape());

		// If the entities do not overlap, we are finished here.
		if (overlap != 0)
		{
			return overlap;
		}
		else
		{
			// The entities' bottom positions.
			double y1 = e1.getBody().getShape().getBottomLeft().y;
			double y2 = e2.getBody().getShape().getBottomLeft().y;

			// If an overlap exists, go by their y-coordinates instead.
			if (y1 < y2)
			{
				return -1;
			}
			else if (y1 > y2)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
}
