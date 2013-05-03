package ambibright.engine.capture;

import java.awt.image.BufferedImage;

/**
 * @author Nicolas Morel
 */
public interface Image {

	interface RgbColor {
		int red();

		int green();

		int blue();
	}

	int getWidth();

	int getHeight();

	RgbColor getRGB(int x, int y);

	BufferedImage getBufferedImage();

	void flush();

}
