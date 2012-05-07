package screens;

import infrastructure.Camera2D;
import infrastructure.GameScreen;
import infrastructure.GameTimer;
import infrastructure.TimeSpan;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import auxillary.Helper;
import auxillary.Vector2;

/**
 * The background screen sits behind all the other menu screens. It draws a background image that remains fixed in place regardless of whatever transitions the screens on top of it may be doing.
 */
public class BackgroundScreen extends GameScreen
{
	// The fields.
	BufferedImage _BackgroundTexture;
	Camera2D _Camera;

	/**
	 * Constructor for the background screen.
	 */
	public BackgroundScreen()
	{
		_TransitionOnTime = TimeSpan.FromSeconds(0.5);
		_TransitionOffTime = TimeSpan.FromSeconds(0.5);
	}

	/**
	 * Loads graphics content for this screen. The background texture is quite big, so we use our own local ContentManager to load it. This allows us to unload before going from the menus into the
	 * game itself, whereas if we used the shared ContentManager provided by the Game class, the content would remain loaded forever.
	 */
	public void loadContent()
	{
		_BackgroundTexture = Helper.loadImage("Distant_City.png", true);
		_Camera = new Camera2D(_ScreenManager.getWindowBounds(), new Vector2(_BackgroundTexture.getWidth(), _BackgroundTexture.getHeight()));
	}

	/**
	 * Updates the background screen. Unlike most screens, this should not transition off even if it has been covered by another screen: it is supposed to be covered, after all! This overload forces
	 * the coveredByOtherScreen parameter to false in order to stop the base update method wanting to transition off.
	 */
	public void update(GameTimer gameTime, boolean otherScreenHasFocus, boolean coveredByOtherScreen)
	{
		// Call the base method.
		super.update(gameTime, otherScreenHasFocus, false);

		// Update the camera.
		float sin = ((float) Math.sin(gameTime.getTotalElapsedTime().Seconds() / 12) * .25f);
		float cos = ((float) Math.cos(gameTime.getTotalElapsedTime().Seconds() / 12) * .25f);
		_Camera.moveAmount(new Vector2(sin, cos));
		_Camera.zoom(sin * cos * .005f);
		_Camera.update(gameTime);
	}

	/**
	 * Draws the background screen.
	 * 
	 * @param gameTime
	 *            The game timer.
	 * @param graphics
	 *            The graphics component.
	 */
	public void draw(GameTimer gameTime, Graphics2D graphics)
	{
		// Save the old graphics matrix and insert the camera matrix in its place.
		AffineTransform old = graphics.getTransform();
		graphics.setTransform(_Camera.getTransformMatrix());

		// Prepare for drawing with an alpha value.
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getTransitionPosition()));
		graphics.drawImage(_BackgroundTexture, 0, 0, null);
		graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		// Reinstate the old graphics matrix.
		graphics.setTransform(old);
	}
}
