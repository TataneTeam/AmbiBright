package ambibright.engine.capture;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import ambibright.engine.jni.GdiCapture;

/**
 * @author Nicolas Morel
 */
public class JniScreenCapture implements ScreenCapture {

	private static class ImageImpl implements Image {

		private final int width, height;
		private byte[] pointer;
		private BufferedImage bufferedImage;

		private ImageImpl(int width, int height, byte[] pointer) {
			this.width = width;
			this.height = height;
			this.pointer = pointer;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public RgbColor getRGB(int x, int y) {
			return getRGB(x, y, new RgbColor());
		}

		@Override
		public RgbColor getRGB(int x, int y, RgbColor rgb) {
			int pos = 4 * ((y * width) + x);
			rgb.update(pointer[pos + 2] & 0xff, pointer[pos + 1] & 0xff, pointer[pos] & 0xff);
			return rgb;
		}

		@Override
		public BufferedImage getBufferedImage() {
			if (null == bufferedImage) {
				bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++) {
						int pos = 4 * ((y * width) + x);
						int b = pointer[pos++] & 0xff;
						int g = pointer[pos++] & 0xff;
						int r = pointer[pos] & 0xff;
						bufferedImage.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
					}
			}
			return bufferedImage;
		}

		@Override
		public void flush() {
			pointer = null;
		}
	}

	private static class ImageBufferImpl implements Image {

		private final int width, height;
		private ByteBuffer pointer;
		private BufferedImage bufferedImage;

		private ImageBufferImpl(int width, int height, ByteBuffer pointer) {
			this.width = width;
			this.height = height;
			this.pointer = pointer;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public RgbColor getRGB(int x, int y) {
			return getRGB(x, y, new RgbColor());
		}

		@Override
		public RgbColor getRGB(int x, int y, RgbColor rgb) {
			int pos = 4 * ((y * width) + x);
			rgb.update(pointer.get(pos + 2) & 0xff, pointer.get(pos + 1) & 0xff, pointer.get(pos) & 0xff);
			return rgb;
		}

		@Override
		public BufferedImage getBufferedImage() {
			if (null == bufferedImage) {
				bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++) {
						int pos = 4 * ((y * width) + x);
						int b = pointer.get(pos++) & 0xff;
						int g = pointer.get(pos++) & 0xff;
						int r = pointer.get(pos) & 0xff;
						bufferedImage.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
					}
			}
			return bufferedImage;
		}

		@Override
		public void flush() {
			GdiCapture.freeBuffer(pointer);
			pointer = null;
		}
	}

	@Override
	public Image captureScreen(Rectangle bounds) {
		return new ImageImpl(bounds.width, bounds.height, GdiCapture.captureScreen(bounds.x, bounds.y, bounds.width, bounds.height));
		// return new ImageBufferImpl(bounds.width, bounds.height,
		// GdiCapture.captureScreenBuffer(bounds.x, bounds.y, bounds.width,
		// bounds.height));
	}
}
