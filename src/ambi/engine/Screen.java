package ambi.engine;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ambi.ressources.Factory;

public class Screen {

	private Rectangle bounds, originalBounds;

	private int ledNumberLeftRight, ledNumberTop;

	private int squareSizeLeftRight, squareSizeTop;

	public static final int screenAnalysePitch = 1;

	public static final int blackLimit = 2;

	private BufferedImage image;

	public Screen(Rectangle bounds, int ledNumberLeftRight, int ledNumberTop) {
		super();
		this.bounds = bounds;
		this.originalBounds = bounds;
		this.ledNumberLeftRight = ledNumberLeftRight;
		this.ledNumberTop = ledNumberTop;
		init();
	}

	public void init() {
		int current = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		int y = 0;
		int x = 0;

		// Get original image
		BufferedImage original = getScreenCapture(originalBounds);

		// Detect top
		for (; y < original.getHeight() / 4; y++) {
			current = original.getRGB(original.getWidth() / 2, y);
			red = (current & 0x00ff0000) >> 16;
			green = (current & 0x0000ff00) >> 8;
			blue = current & 0x000000ff;
			if (red > blackLimit || blue > blackLimit || green > blackLimit) {
				break;
			}
		}
		if (y == original.getHeight() / 4) {
			y = 0;
		}

		// Detect left
		for (; x < original.getWidth() / 4; x++) {
			current = original.getRGB(x, original.getHeight() / 2);
			red = (current & 0x00ff0000) >> 16;
			green = (current & 0x0000ff00) >> 8;
			blue = current & 0x000000ff;
			if (red > blackLimit || blue > blackLimit || green > blackLimit) {
				break;
			}
		}
		if (x == original.getWidth() / 4) {
			x = 0;
		}

		// New capture zone
		bounds = new Rectangle(originalBounds.x + x, originalBounds.y + y, (int) originalBounds.getWidth() - (2 * x), (int) originalBounds.getHeight() - (2 * y));

		// Calculating squareSize
		squareSizeLeftRight = (int) (bounds.getHeight() / ledNumberLeftRight);
		squareSizeTop = (int) (bounds.getWidth() / ledNumberTop);
	}

	public BufferedImage getScreenCapture(Rectangle bounds) {
		return Factory.getRobot().createScreenCapture(bounds);
	}

	// L > T > R
	public List<Color> getColors() {
		List<Color> result = new ArrayList<Color>();
		image = getScreenCapture(bounds);

		// Left from bottom to top
		for (int y = image.getHeight() - squareSizeLeftRight; y >= 0; y -= squareSizeLeftRight) {
			result.add(getColor(0, y, squareSizeLeftRight, squareSizeLeftRight));
		}

		// Top from left to right
		for (int x = squareSizeTop - 1 ; x + squareSizeTop < image.getWidth()  ; x += squareSizeTop) {
			result.add(getColor(x, 0, squareSizeTop, squareSizeTop));
		}

		// Right from top to bottom
		for (int y = squareSizeLeftRight -1; y + squareSizeLeftRight < image.getHeight(); y += squareSizeLeftRight) {
			result.add(getColor(image.getWidth() - squareSizeLeftRight, y, squareSizeLeftRight, squareSizeLeftRight));
		}

		return result;
	}

	private Color getColor(int x, int y, int width, int height) {
		int current = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		int nbPixel = 0;
		for (int posX = 0; posX < width && posX + x <image.getWidth(); posX += screenAnalysePitch) {
			for (int posY = 0; posY < height  && posY + y <image.getHeight(); posY += screenAnalysePitch) {
				current = image.getRGB(x + posX, y + posY);
				red += (current & 0x00ff0000) >> 16;
				green += (current & 0x0000ff00) >> 8;
				blue += current & 0x000000ff;
				nbPixel++;
			}
		}
		return new Color(red / nbPixel, green / nbPixel, blue / nbPixel);
	}

}
