package ambi.engine;

import java.awt.*;
import java.awt.image.BufferedImage;

import ambi.ressources.CurrentBounds;

/**
 * Created with IntelliJ IDEA. User: Nico Date: 23/03/13 Time: 21:36 To change
 * this template use File | Settings | File Templates.
 */
public class AspectRatioService implements Runnable {
	private static final int blackLimit = 6;
	private final Rectangle fullScreenBounds;
	private final CurrentBounds currentBounds;
	private final Robot robot;
	private int current = 0;
	private int red = 0;
	private int green = 0;
	private int blue = 0;
	private int y = 0;
	private int x = 0;
	private BufferedImage image;

	public AspectRatioService(Rectangle fullScreenBounds, CurrentBounds currentBounds, Robot robot) {
		this.fullScreenBounds = fullScreenBounds;
		this.currentBounds = currentBounds;
		this.robot = robot;
	}

	public void run() {
		// Get current image
		image = robot.createScreenCapture(fullScreenBounds);

		// Detect top
		for (; y < fullScreenBounds.height / 4; y++) {
			current = image.getRGB(fullScreenBounds.width / 2, y);
			red = (current & 0x00ff0000) >> 16;
			green = (current & 0x0000ff00) >> 8;
			blue = current & 0x000000ff;
			if (red + blue + green > blackLimit) {
				break;
			}
		}
		if (y == fullScreenBounds.height / 4) {
			y = 0;
		}

		// Detect left
		for (; x < fullScreenBounds.width / 4; x++) {
			current = image.getRGB(x, fullScreenBounds.height / 2);
			red = (current & 0x00ff0000) >> 16;
			green = (current & 0x0000ff00) >> 8;
			blue = current & 0x000000ff;
			if (red + blue + green > blackLimit) {
				break;
			}
		}
		if (x == fullScreenBounds.width / 4) {
			x = 0;
		}

		// Flushing the image
		image.flush();
		image = null;

		currentBounds.updateBounds(new Rectangle(fullScreenBounds.x + x, fullScreenBounds.y + y, fullScreenBounds.width - (2 * x), fullScreenBounds.height - (2 * y)));
	}
}
