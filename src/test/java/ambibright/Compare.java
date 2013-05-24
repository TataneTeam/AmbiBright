package ambibright;

/**
 * @author Nicolas Morel
 */

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import org.junit.Test;

import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMainAverage;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMain;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoAverage;
import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.capture.ScreenCapture;
import ambibright.engine.capture.RobotScreenCapture;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;
import ambibright.engine.capture.GdiScreenCapture;
import ambibright.engine.capture.DirectXScreenCapture;

public class Compare {

	@Test
	public void testScreenCapture() {
		int screenDevice = 0;
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
		int nbIteration = 1000;

		testScreenCapture(bounds, screenDevice, nbIteration, new RobotScreenCapture());
		testScreenCapture(bounds, screenDevice, nbIteration, new GdiScreenCapture());
		testScreenCapture(bounds, screenDevice, nbIteration, new DirectXScreenCapture());
	}

	private void testScreenCapture(Rectangle bounds, int screenDevice, int nbIteration, ScreenCapture screenCapture) {
		// first time to warm up
		Image image = screenCapture.captureScreen(bounds, screenDevice);

		long startTime = System.nanoTime();
		for (int i = 0; i < nbIteration; i++) {
			image = screenCapture.captureScreen(bounds, screenDevice);
			image.flush();
		}
		long finishTime = System.nanoTime();

		long nanotime = finishTime - startTime;
		nanotime = nanotime / nbIteration;
		System.out.println("Average screen capture time with " + screenCapture + ": " + (nanotime / 1000l) + " µs");
	}

	@Test
	public void testBrowsePixels() {
		int screenDevice = 0;
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();

		testBrowsePixels(bounds, screenDevice, new RobotScreenCapture());
		testBrowsePixels(bounds, screenDevice, new GdiScreenCapture());
		testBrowsePixels(bounds, screenDevice, new DirectXScreenCapture());
	}

	private void testBrowsePixels(Rectangle bounds, int screenDevice, ScreenCapture screenCapture) {
		Image image = null;
		RgbColor color = new RgbColor();
		try {
			image = screenCapture.captureScreen(bounds, screenDevice);
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
		ScreenCapture screenCapture = new GdiScreenCapture();
		int screenDevice = 0;
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
		Image image = screenCapture.captureScreen(bounds, screenDevice);
		bounds = new Rectangle(0, 0, 100, 50);

		testSquareAnalyser(new SquareAnalyzerAlgoAverage(), image, bounds);
		testSquareAnalyser(new SquareAnalyzerAlgoMain(), image, bounds);
		testSquareAnalyser(new SquareAnalyzerAlgoMainAverage(), image, bounds);
	}

	private void testSquareAnalyser(SquareAnalyser squareAnalyser, Image image, Rectangle bounds) {
		// first one to initialize
		int[] color = squareAnalyser.getColor(image, bounds, 1);

		long startTime = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			color = squareAnalyser.getColor(image, bounds, 1);
		}
		long finishTime = System.nanoTime();

		long nanotime = (finishTime - startTime) / 10000l;

		System.out.println("Average square analyser time with " + squareAnalyser + ": " + (nanotime / 1000l) + " " + "" + "µs | color : " + color[0] + "," + color[1] + "," + color[2]);
	}
}
