package main;

import java.util.Comparator;

import auxillary.Helper;
import auxillary.Vector3;

import physics.Shape;

/**
 * This comparator compares two Entity objects and sorts them by ascending depth.
 */
public class EntityDepthComparator implements Comparator<Entity>
{

	/**
	 * Compare two Entity objects to each other by their depth values.
	 */
	//@Override
	public int compareOld2(Entity e1, Entity e2)
	{
		// The entities' bottom positions on screen.
		double d1 = e1.getBody().getShape().getBottomLeft().y + e1.getBody().getShape().getBottomDepth();
		double d2 = e2.getBody().getShape().getBottomLeft().y + e2.getBody().getShape().getBottomDepth();

		// Go by their screen y-coordinates instead.
		if (d1 < d2)
		{
			return -1;
		}
		else if (d1 > d2)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Compare two Entity objects to each other by their depth values.
	 */
	public int compareOld(Entity e1, Entity e2)
	{
		// The entities' bottom positions on screen.
		double y1 = Helper.getScreenPosition(new Vector3(0, e1.getBody().getShape().getBottomLeft().y, e1.getBody().getShape().getBottomDepth())).y;
		double y2 = Helper.getScreenPosition(new Vector3(0, e2.getBody().getShape().getBottomLeft().y, e2.getBody().getShape().getBottomDepth())).y;

		// Go by their screen y-coordinates instead.
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
			// The entities' bottom position.
			double z1 = e1.getBody().getShape().getBottomDepth();
			double z2 = e1.getBody().getShape().getBottomDepth();

			// Go by their z-coordinate.
			if (z1 < z2)
			{
				return -1;
			}
			else if (z1 > z2)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}

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
