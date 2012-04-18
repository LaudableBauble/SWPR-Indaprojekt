package graphics;

import java.util.Comparator;

/**
 * This comparator compares two Sprite objects and sorts them by ascending depth.
 */
public class SpriteDepthComparator implements Comparator<Sprite>
{
	/**
	 * Compare two Sprite objects to each other by their depth values.
	 */
	@Override
	public int compare(Sprite s1, Sprite s2)
	{
		// Compare the sprites to each other.
		if (s1.getDepth() < s2.getDepth())
		{
			return -1;
		}
		else if (s1.getDepth() > s2.getDepth()) { return 1; }

		// The sprites' depth values are equal, return 0.
		return 0;
	}
}
