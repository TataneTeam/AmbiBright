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
public class DefaultScreenCapture implements ScreenCapture {

    private static class ImageImpl implements Image {

		private final BufferedImage image;

		private ImageImpl(BufferedImage image) {
			this.image = image;
		}

		@Override
		public RgbColor getRGB(int x, int y) {
			return new RgbColorImpl(image.getRGB(x, y));
		}

		@Override
		public BufferedImage getBufferedImage() {
			return image;
		}

        @Override
        public void flush()
        {
            image.flush();
        }

        @Override
        public int getWidth()
        {
            return image.getWidth();
        }

        @Override
        public int getHeight()
        {
            return image.getHeight();
        }
    }

	private static final Logger logger = LoggerFactory.getLogger(DefaultScreenCapture.class);
	private static DefaultScreenCapture instance;

	public static DefaultScreenCapture getInstance() {
		if (null == instance) {
			instance = new DefaultScreenCapture();
		}
		return instance;
	}

	private final Robot robot;

	private DefaultScreenCapture() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			logger.error("Error instantiating a Robot", e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public Image captureScreen(Rectangle bounds) {
		return new ImageImpl(robot.createScreenCapture(bounds));
	}
}
