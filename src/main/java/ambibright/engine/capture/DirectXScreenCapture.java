package ambibright.engine.capture;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.ressources.CurrentBounds;
import ambibright.engine.jni.DirectXCapture;

/**
 * Implementation of {@link ambibright.engine.capture.ScreenCaptureMethod} that
 * uses JNI to invoke natively the GDI capture screen method.
 *
 * @author Nicolas Morel
 */
public class DirectXScreenCapture implements ScreenCaptureMethod {

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

	private static final Logger logger = LoggerFactory.getLogger(DirectXScreenCapture.class);
	private final Object lock = new Object();
	private boolean initialized;
	private int currentScreenDevice = -1;
	private int deltaX;

	DirectXScreenCapture() {
		// TODO doesn't work because the config constructor uses the ScreenCapture enum that call this constructor
		// Config.getInstance().addPropertyChangeListener(Config.CONFIG_SCREEN_CAPTURE,
		// new PropertyChangeListener() {
		// @Override
		// public void propertyChange(PropertyChangeEvent evt) {
		// if (ScreenCapture.DirectX == evt.getOldValue()) {
		// synchronized (lock) {
		// destroy();
		// }
		// }
		// }
		// });
	}

	private void init(int screenDevice) {
		logger.info("Initialize DirectX screen capture with the screen device {}", screenDevice);
		DirectXCapture.initDirectX(screenDevice);
		currentScreenDevice = screenDevice;
		deltaX = CurrentBounds.getScreenBounds(screenDevice).x;
		initialized = true;
	}

	private void destroy() {
		if (initialized) {
			logger.info("Destroy DirectX screen capture context");
			DirectXCapture.destroyDirectX();
			currentScreenDevice = -1;
			initialized = false;
		}
	}

	@Override
	public Image captureScreen(Rectangle bounds, int screenDevice) {
		synchronized (lock) {
			if (currentScreenDevice != screenDevice) {
				destroy();
				init(screenDevice);
			}
			return new ImageImpl(bounds.width, bounds.height, DirectXCapture.captureScreenDirectX(bounds.x - deltaX, bounds.y, bounds.width, bounds.height));
		}
	}
}
