package ambibright.engine.capture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Nicolas Morel
 */
public class ImageMock implements Image {

	private BufferedImage image;

	public ImageMock(String imageName) {

		try {
			this.image = ImageIO.read(new File("src/test/resources/images", imageName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public RgbColor getRGB(int x, int y) {
		return getRGB(x, y, new RgbColor());
	}

	@Override
	public RgbColor getRGB(int x, int y, RgbColor rgb) {
		int color = image.getRGB(x, y);
		rgb.update((color & 0x00ff0000) >> 16, (color & 0x0000ff00) >> 8, color & 0x000000ff);
		return rgb;
	}

	@Override
	public BufferedImage getBufferedImage() {
		return image;
	}

	@Override
	public void flush() {
		image.flush();
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}
}
