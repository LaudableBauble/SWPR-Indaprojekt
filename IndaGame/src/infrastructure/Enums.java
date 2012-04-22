package infrastructure;

/**
 * This class is merely a container for different kinds of enums.
 */
public final class Enums
{
	/**
	 * The orientation of something, ie. a sprite. Can be none, left or right.
	 */
	public static enum Orientation
	{
		None, Left, Right
	}

	/**
	 * The visibility of something, ie. a sprite.
	 */
	public static enum Visibility
	{
		Invisible, Visible
	}

	/**
	 * The depth distribution in for example a shape. Uniform means that the depth is equal all over the shape, whereas right means that the depth is at its highest at right and at its lowest at left.
	 */
	public static enum DepthDistribution
	{
		Uniform, Top, Bottom, Right, Left
	}
}
