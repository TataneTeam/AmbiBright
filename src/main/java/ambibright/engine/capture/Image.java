package ambibright.engine.capture;

import java.awt.image.BufferedImage;

/**
 * @author Nicolas Morel
 */
public interface Image {

	interface RGB {
		int red();

		int green();

		int blue();
	}

    int getWidth();

    int getHeight();

	RGB getRGB(int x, int y);

	BufferedImage getBufferedImage();

    void flush();

}
