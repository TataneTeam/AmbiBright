package ambibright.engine.capture;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ambibright.engine.jni.GdiCapture;

/**
 * Implementation of {@link ScreenCaptureMethod} that uses JNI to invoke
 * natively the GDI capture screen method.
 *
 * @author Nicolas Morel
 */
public class GdiScreenCapture implements ScreenCaptureMethod {

	private static class ImageImpl implements Image {

		private final int width, height;
		private byte[] datas;
		private BufferedImage bufferedImage;

		private ImageImpl(int width, int height, byte[] datas) {
			this.width = width;
			this.height = height;
			this.datas = datas;
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
			rgb.update(datas[pos + 2] & 0xff, datas[pos + 1] & 0xff, datas[pos] & 0xff);
			return rgb;
		}

		@Override
		public BufferedImage getBufferedImage() {
			if (null == bufferedImage) {
				bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				byte[] bufferedImageDatas = new byte[width * height * 3];
				int pos = 0;
				int dpos = 0;
				for (int i = 0; i < width * height; i++) {
					bufferedImageDatas[dpos++] = datas[pos + 2];
					bufferedImageDatas[dpos++] = datas[pos + 1];
					bufferedImageDatas[dpos++] = datas[pos];
					pos += 4;
				}
				bufferedImage.getRaster().setDataElements(0, 0, width, height, bufferedImageDatas);
			}
			return bufferedImage;
		}

		@Override
		public void flush() {
			datas = null;
		}
	}

	GdiScreenCapture() {
	}

	@Override
	public Image captureScreen(Rectangle bounds) {
		return new ImageImpl(bounds.width, bounds.height, GdiCapture.captureScreen(bounds.x, bounds.y, bounds.width, bounds.height));
	}
}
