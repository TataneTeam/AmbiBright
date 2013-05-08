package ambibright.engine.capture;

import java.awt.Rectangle;

/**
 * Interface for screen capture
 */
public interface ScreenCaptureMethod {
	/**
	 * Capture the screen inside given bounds
	 *
	 * @param bounds
	 * @return Captured image
	 */
	Image captureScreen(Rectangle bounds);
}
