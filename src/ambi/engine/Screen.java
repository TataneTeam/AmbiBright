package ambi.engine;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ambi.ressources.Factory;

public class Screen {

	private Rectangle bounds, originalBounds;

	private int ledNumberLeftRight, ledNumberTop;

	private int squareSizeLeftRight, squareSizeTop;

	public static final int screenAnalysePitch = 2;

	public static final int blackLimit = 6;

	private BufferedImage image, original;

	private List<Color> result;

	private int current, red, green, blue, nbPixel, x, y, posX, posY,squareSize;

	public Screen(Rectangle bounds, int ledNumberLeftRight, int ledNumberTop) {
		super();
		this.bounds = bounds;
		this.originalBounds = bounds;
		this.ledNumberLeftRight = ledNumberLeftRight;
		this.ledNumberTop = ledNumberTop;
		this.result = new ArrayList<Color>();
		detectImageFormat();
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
		
		// Flushing the image
		original.flush();
		original = null;

		// New capture zone
		bounds = new Rectangle(originalBounds.x + x, originalBounds.y + y, originalBounds.width - (2 * x), originalBounds.height - (2 * y));

		// Calculating squareSize
		squareSizeLeftRight = bounds.height / ledNumberLeftRight;
		squareSizeTop = bounds.width / ledNumberTop;
		
		//saveImage(getScreenCapture(bounds), new File("detectImageFormat.png"));
	}

	public BufferedImage getScreenCapture(Rectangle bounds) {
		return Factory.getRobot().createScreenCapture(bounds);
	}

	// L > T > R
	public List<Color> getColors() {
		squareSize = Factory.getSquareSize();
		result.clear();
		image = getScreenCapture(bounds);
		
		// Left from bottom to top
		for (y = bounds.height - squareSizeLeftRight; y >= 0; y -= squareSizeLeftRight) {
			result.add(getColor(0, y, squareSize, squareSizeLeftRight));
		}

		// Top from left to right
		for (x = squareSizeTop - 1; x + squareSizeTop < bounds.width; x += squareSizeTop) {
			result.add(getColor(x, 0, squareSizeTop, squareSize));
		}

		// Right from top to bottom
		for (y = squareSizeLeftRight - 1; y + squareSizeLeftRight < bounds.height; y += squareSizeLeftRight) {
			result.add(getColor(bounds.width - squareSizeLeftRight, y, squareSize, squareSizeLeftRight));
		}
		
		// Flushing the image
		image.flush();
		image = null;

		return result;
	}

	private Color getColor(int x, int y, int width, int height) {
		current = 0;
		red = 0;
		green = 0;
		blue = 0;
		nbPixel = 0;
		for (posX = 0; posX < width; posX += screenAnalysePitch) {
			for (posY = 0; posY < height; posY += screenAnalysePitch) {
				current = image.getRGB(x + posX, y + posY);
				red += (current & 0x00ff0000) >> 16;
				green += (current & 0x0000ff00) >> 8;
				blue += current & 0x000000ff;
				nbPixel++;
			}
		}
		return new Color(red / nbPixel, green / nbPixel, blue / nbPixel);
	}
	
	public void saveImage(BufferedImage image, File file){
		try {
			ImageIO.write(image, "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
