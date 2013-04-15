package ambibright.engine;

import java.awt.image.BufferedImage;

/**
 * Observes the colors' changes
 */
public interface ColorsChangeObserver {
	void onColorsChange(BufferedImage image, byte[] colors);
}
