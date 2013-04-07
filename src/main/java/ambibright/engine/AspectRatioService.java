package ambibright.engine;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import ambibright.ressources.CurrentBounds;

/**
 * Created with IntelliJ IDEA. User: Nico Date: 23/03/13 Time: 21:36 To change
 * this template use File | Settings | File Templates.
 */
public class AspectRatioService implements Runnable {
	private static final int blackLimit = 6;
	private final Rectangle fullScreenBounds;
	private final CurrentBounds currentBounds;
	private final Robot robot;
	private Rectangle lastScreenBounds;
	private int red = 0;
	private int green = 0;
	private int blue = 0;
	private int y = 0;
	private int x = 0;
	private int testY, testX;
	private BufferedImage image;

	public AspectRatioService(Rectangle fullScreenBounds, CurrentBounds currentBounds, Robot robot) {
		this.fullScreenBounds = fullScreenBounds;
		this.currentBounds = currentBounds;
		this.robot = robot;
		this.lastScreenBounds = fullScreenBounds;
	}

	public void run() {
		// Get current image
		image = robot.createScreenCapture(fullScreenBounds);

		// Detect top
		y = fullScreenBounds.height / 4;
		for (testX = 0; testX < fullScreenBounds.width; testX += fullScreenBounds.width / 5) {
			for (testY = 0; testY < fullScreenBounds.height / 4; testY++) {
				if (!isBlack(image.getRGB(testX, testY))) {
					y = Math.min(y, testY);
					break;
				}
			}
		}

		// Detect left
		x = fullScreenBounds.width / 4;
		for (testY = 0; testY < fullScreenBounds.height; testY += fullScreenBounds.height / 5) {
			for (testX = 0; testX < fullScreenBounds.width / 4; testX++) {
				if (!isBlack(image.getRGB(testX, testY))) {
					x = Math.min(x, testX);
					break;
				}
			}
		}

		// Flushing the image
		image.flush();
		image = null;

		Rectangle newBounds = new Rectangle(fullScreenBounds.x + x, fullScreenBounds.y + y, fullScreenBounds.width - (2 * x), fullScreenBounds.height - (2 * y));
		if (!lastScreenBounds.equals(newBounds)) {
			lastScreenBounds = newBounds;
			currentBounds.updateBounds(newBounds);
		}
	}

	public boolean isBlack(int color) {
		red = (color & 0x00ff0000) >> 16;
		green = (color & 0x0000ff00) >> 8;
		blue = color & 0x000000ff;
		return (red + blue + green) <= blackLimit;
	}
}
