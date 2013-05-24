package ambibright.engine.capture;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ScreenCapture} that uses
 * {@link Robot#createScreenCapture(java.awt.Rectangle)} to capture the screen.
 */
public class RobotScreenCapture implements ScreenCapture
{

	private static class ImageImpl implements Image {

		private final BufferedImage image;

		private ImageImpl(BufferedImage image) {
			this.image = image;
		}

		@Override
		public RgbColor getRGB(int x, int y) {
			return getRGB(x, y, new RgbColor());
		}

		@Override
		public RgbColor getRGB(int x, int y, RgbColor rgb) {
			int color = image.getRGB(x, y);
			rgb.update((color & 0x00ff0000) >> 16, (color & 0x0000ff00) >> 8, color & 0x000000ff);
			return rgb;
		}

		@Override
		public BufferedImage getBufferedImage() {
			return image;
		}

		@Override
		public void flush() {
			image.flush();
		}

		@Override
		public int getWidth() {
			return image.getWidth();
		}

		@Override
		public int getHeight() {
			return image.getHeight();
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(RobotScreenCapture.class);
	private final Robot robot;

	public RobotScreenCapture() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			logger.error("Error instantiating a Robot", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Image captureScreen(Rectangle bounds, int screenDevice) {
		return new ImageImpl(robot.createScreenCapture(bounds));
	}
}
