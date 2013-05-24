package ambibright.engine.capture;

import java.awt.Rectangle;

/**
 * Interface for screen capture
 */
public interface ScreenCapture
{
	/**
	 * Capture the screen inside given bounds
	 *
	 * @param bounds bounds relative to the principal screen device
     * @param screenDevice screen device
	 * @return Captured image
	 */
	Image captureScreen(Rectangle bounds, int screenDevice);
}
