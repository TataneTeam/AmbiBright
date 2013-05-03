package ambibright.engine;

import ambibright.engine.capture.Image;

/**
 * Observes the colors' changes
 */
public interface ColorsChangeObserver {
	void onColorsChange(Image image, byte[] colors);
}
