package main;

import java.util.Comparator;

import auxillary.Helper;
import auxillary.Vector2;
import auxillary.Vector3;

import physics.Shape;

/**
 * This comparator compares two Entity objects and sorts them by ascending depth. This method is far from bullet-proof and should only be used as a sort of prebuffer.
 */
public class EntityDepthComparator implements Comparator<Entity>
{
	/**
	 * Compare two Entity objects to each other by their depth values.
	 */
	@Override
	public int compare(Entity e1, Entity e2)
	{
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
			double y1 = Helper.getScreenPosition(new Vector3(0, e1.getBody().getShape().getBottomLeft().y, e1.getBody().getShape().getBottomDepth())).y;
			double y2 = Helper.getScreenPosition(new Vector3(0, e2.getBody().getShape().getBottomLeft().y, e2.getBody().getShape().getBottomDepth())).y;

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
