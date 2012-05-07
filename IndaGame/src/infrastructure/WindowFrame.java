package infrastructure;

import input.InputManager;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.ColorModel;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A window frame is the window where the game will be drawn. Think of the game as the background of the frame and all other GUI components as layers on top.
 */
public class WindowFrame extends JFrame
{
	// The Bounds of the window.
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	// The Keyboard and Mouse input handlers.
	public InputManager inputManager = InputManager.getInstance();
	// The Double Buffering Instance.
	public BufferStrategy strategy;

	// The current graphics instance.
	public Graphics2D _Graphics;

	// The back-buffer color.
	private Color _BackBufferColor;

	/**
	 * Constructor for a window frame.
	 */
	public WindowFrame()
	{
		// Create a JFrame as window.
		super("Inda11");

		// Set the window's bounds, make the window visible and make the window unable to resize.
		setVisible(true);
		setSize(WIDTH, HEIGHT);
		setResizable(false);

		// Create a JPanel to display everything on.
		JPanel panel = (JPanel) getContentPane();
		// Set the panel's bounds and transparance.
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setOpaque(false);
		panel.setDoubleBuffered(false);

		// Set the back-buffer color to gray.
		_BackBufferColor = Color.gray;

		// Listen for Windows trying to close the window.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the buffer strategy and save it.
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// Add Key and Mouse listeners.
		addKeyListener(inputManager);
		addMouseListener(inputManager);
		addMouseMotionListener(inputManager);

		// Finally, request focus to the window's canvas.
		requestFocus();
	}

	// Update the WindowPanel.
	public void update()
	{
		// Update the key Inputs.
		inputManager.update();
	}

	/**
	 * Begin drawing.
	 */
	public void drawBegin()
	{
		// Get the Graphics instance and translate it to the right position.
		_Graphics = (Graphics2D) strategy.getDrawGraphics();
		
		// Clear the back-buffer.
		_Graphics.setColor(_BackBufferColor);
		_Graphics.fillRect(0, 0, getWidth(), getHeight());

		// Set some graphics flags.
		_Graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		_Graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		// Set the color.
		_Graphics.setColor(Color.black);
	}

	/**
	 * End the drawing.
	 */
	public void drawEnd()
	{
		// Show the frame.
		_Graphics.setTransform(AffineTransform.getTranslateInstance(getInsets().left, getInsets().top));
		getLayeredPane().paintComponents(_Graphics);
		strategy.show();
		_Graphics.dispose();

		// Synchronize with the display refresh rate.
		Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * Get the graphics component for this window.
	 * 
	 * @return The graphics component.
	 */
	public Graphics2D getBufferGraphics()
	{
		// Create the Graphics Object.
		Graphics2D g = null;

		// Try to get the temporary Graphics Object.
		try
		{
			g = _Graphics;
		}
		// Catch the exception.
		catch (Exception e)
		{
			System.out.println(this + ": Error getting Graphics Object. (" + e + ")");
		}

		// Return the Graphics Object.
		return (g);
	}

	/**
	 * Set the back-buffer color.
	 * 
	 * @param color
	 *            The new back-buffer color.
	 */
	public void setBackBufferColor(Color color)
	{
		// If the color is null, quit.
		if (color == null) { return; }

		// Set the color.
		_BackBufferColor = color;
	}
}