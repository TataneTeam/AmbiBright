package ambibright.engine.capture;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Default implementation of {@link ScreenCapture} that uses
 * {@link Robot#createScreenCapture(java.awt.Rectangle)} to capture the screen.
 */
public class DefaultScreenCapture implements ScreenCapture {

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
			throw new IllegalStateException(e);
		}
	}

	@Override
	public BufferedImage captureScreen(Rectangle bounds) {
		return robot.createScreenCapture(bounds);
	}
}
