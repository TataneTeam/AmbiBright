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
	public BufferedImage captureScreen(Rectangle bounds) {
		return robot.createScreenCapture(bounds);
	}
}
