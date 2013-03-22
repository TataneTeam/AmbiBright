package ambi.engine;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ambi.ressources.Factory;

public class ScreenMultiRobot {

	private Rectangle bounds, originalBounds;

	private int ledNumberLeftRight, ledNumberTop;

	private int squareSizeLeftRight, squareSizeTop;

	public static final int screenAnalysePitch = 2;

	public static final int blackLimit = 6;

	private BufferedImage original;

	private List<Color> result;

	private List<ScreenRobot> robots;

	public ScreenMultiRobot(Rectangle bounds, int ledNumberLeftRight, int ledNumberTop) {
		super();
		this.bounds = bounds;
		this.originalBounds = bounds;
		this.ledNumberLeftRight = ledNumberLeftRight;
		this.ledNumberTop = ledNumberTop;
		this.result = new ArrayList<Color>();
		this.robots = new ArrayList<ScreenRobot>();
		detectImageFormat();
	}

	private void createRobots() {
		robots.clear();
		int x, y;

		// Left from bottom to top
		for (y = bounds.height - squareSizeLeftRight; y >= 0; y -= squareSizeLeftRight) {
			robots.add(new ScreenRobot(new Rectangle(0, y, squareSizeLeftRight, squareSizeLeftRight)));
		}

		// Top from left to right
		for (x = squareSizeTop - 1; x + squareSizeTop < bounds.width; x += squareSizeTop) {
			robots.add(new ScreenRobot(new Rectangle(x, 0, squareSizeTop, squareSizeTop)));
		}

		// Right from top to bottom
		for (y = squareSizeLeftRight - 1; y + squareSizeLeftRight < bounds.height; y += squareSizeLeftRight) {
			robots.add(new ScreenRobot(new Rectangle(bounds.width - squareSizeLeftRight, y, squareSizeLeftRight, squareSizeLeftRight)));
		}

	}

	class ScreenRobot {

		private Rectangle bounds;
		private Robot robot;
		private int current, red, green, blue, nbPixel, x, y;
		private BufferedImage image;

		public ScreenRobot(Rectangle bounds) {
			super();
			this.bounds = bounds;
			try {
				robot = new Robot();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public Color getColor() {
			image = robot.createScreenCapture(bounds);
			current = 0;
			red = 0;
			green = 0;
			blue = 0;
			nbPixel = 0;
			for (x = 0; x < bounds.width; x += screenAnalysePitch) {
				for (y = 0; y < bounds.height; y += screenAnalysePitch) {
					current = image.getRGB(x, y);
					red += (current & 0x00ff0000) >> 16;
					green += (current & 0x0000ff00) >> 8;
					blue += current & 0x000000ff;
					nbPixel++;
				}
			}
			return new Color(red / nbPixel, green / nbPixel, blue / nbPixel);
		}

	}

	public void detectImageFormat() {
		int current = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		int y = 0;
		int x = 0;

		// Get original image
		original = getScreenCapture(originalBounds);

		// Detect top
		for (; y < originalBounds.height / 4; y++) {
			current = original.getRGB(originalBounds.width / 2, y);
			red = (current & 0x00ff0000) >> 16;
			green = (current & 0x0000ff00) >> 8;
			blue = current & 0x000000ff;
			if (red + blue + green > blackLimit) {
				break;
			}
		}
		if (y == originalBounds.height / 4) {
			y = 0;
		}

		// Detect left
		for (; x < originalBounds.width / 4; x++) {
			current = original.getRGB(x, originalBounds.height / 2);
			red = (current & 0x00ff0000) >> 16;
			green = (current & 0x0000ff00) >> 8;
			blue = current & 0x000000ff;
			if (red + blue + green > blackLimit) {
				break;
			}
		}
		if (x == originalBounds.width / 4) {
			x = 0;
		}

		// New capture zone
		bounds = new Rectangle(originalBounds.x + x, originalBounds.y + y, originalBounds.width - (2 * x), originalBounds.height - (2 * y));

		// Calculating squareSize
		squareSizeLeftRight = bounds.height / ledNumberLeftRight;
		squareSizeTop = bounds.width / ledNumberTop;

		// Create Robot
		createRobots();
	}

	public BufferedImage getScreenCapture(Rectangle bounds) {
		return Factory.getRobot().createScreenCapture(bounds);
	}

	// L > T > R
	public List<Color> getColors() {
		result.clear();
		for (ScreenRobot robot : robots) {
			result.add(robot.getColor());
		}
		return result;
	}

}
