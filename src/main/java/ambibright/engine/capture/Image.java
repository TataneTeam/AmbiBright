package ambibright.engine.capture;

import java.awt.image.BufferedImage;

/**
 * @author Nicolas Morel
 */
public interface Image {

	int getWidth();

	int getHeight();

	RgbColor getRGB(int x, int y);

    RgbColor getRGB(int x, int y, RgbColor rgb);

	BufferedImage getBufferedImage();

	void flush();

}
