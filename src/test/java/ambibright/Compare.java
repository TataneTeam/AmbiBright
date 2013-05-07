package ambibright;

/**
 * @author Nicolas Morel
 */

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import org.junit.Test;

import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.capture.ScreenCapture;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.JniScreenCapture;
import ambibright.engine.capture.Image;
import ambibright.engine.capture.DefaultScreenCapture;

public class Compare {

	@Test
	public void testScreenCapture() {
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds();
		int nbIteration = 1000;

		testScreenCapture(bounds, nbIteration, DefaultScreenCapture.getInstance());
		testScreenCapture(bounds, nbIteration, new JniScreenCapture());
	}

	private void testScreenCapture(Rectangle bounds, int nbIteration, ScreenCapture screenCapture) {
		Image image;

		long startTime = System.nanoTime();
		for (int i = 0; i < nbIteration; i++) {
			image = screenCapture.captureScreen(bounds);
			image.flush();
		}
		long finishTime = System.nanoTime();

		long nanotime = finishTime - startTime;
		nanotime = nanotime / nbIteration;
		System.out.println("Average screen capture time with " + screenCapture + ": " + (nanotime / 1000l) + " µs");
	}

	@Test
	public void testBrowsePixels() {
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds();

		testBrowsePixels(bounds, DefaultScreenCapture.getInstance());
		testBrowsePixels(bounds, new JniScreenCapture());
	}

	private void testBrowsePixels(Rectangle bounds, ScreenCapture screenCapture) {
		Image image = null;
		RgbColor color = new RgbColor();
		try {
			image = screenCapture.captureScreen(bounds);
			long startTime = System.nanoTime();
			for (int w = 0; w < bounds.width; w++) {
				for (int h = 0; h < bounds.height; h++) {
					image.getRGB(w, h, color);
				}
			}
			long finishTime = System.nanoTime();

			long nanotime = finishTime - startTime;
			System.out.println("Browsing " + (bounds.width * bounds.height) + " pixels with " + screenCapture + " " + "took " + (nanotime / 1000l) + "" + " µs");
		} finally {
			if (null != image) {
				image.flush();
			}
		}
	}

	@Test
	public void testSquareAnalyser() {
		ScreenCapture screenCapture = new JniScreenCapture();
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds();
		Image image = screenCapture.captureScreen(bounds);
		bounds = new Rectangle(0, 0, 100, 50);
		long startTime, finishTime;
		int[] color = null;

		for (SquareAnalyser squareAnalyser : SquareAnalyser.values()) {
			startTime = System.nanoTime();
			for (int i = 0; i < 1000; i++) {
				color = squareAnalyser.getColor(image, bounds, 1);
			}
			finishTime = System.nanoTime();
			long nanotime = (finishTime - startTime) / 1000l;
			System.out.println("Average square analyser time with " + squareAnalyser + ": " + (nanotime / 1000l) + " " +
                "" + "µs | color : " + color[0] + "," + color[1] + "," + color[2]);
		}
	}
}
