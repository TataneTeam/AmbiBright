package ambibright.engine.capture;

import java.awt.Rectangle;

/**
 * Interface for screen capture
 */
public interface ScreenCapture {
	Image captureScreen(Rectangle bounds);
}
