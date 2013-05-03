package ambibright;

/**
 * @author Nicolas Morel
 */

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import ambibright.engine.capture.ScreenCapture;
import ambibright.engine.capture.JnaScreenCapture;
import ambibright.engine.capture.Image;
import ambibright.engine.capture.DefaultScreenCapture;

public class Compare {

	public static void main(String[] args) {

		Image jnaImage = null;
		Image robotImage = null;
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration().getBounds();
		long startTime, finishTime;

		int nbIteration = 1000;

		// on fait un premier dans le vent pour charger les libs
		ScreenCapture screenCapture = new JnaScreenCapture();

		startTime = System.currentTimeMillis();
		for (int i = 0; i < nbIteration; i++) {
			jnaImage = screenCapture.captureScreen(rect);
		}
		finishTime = System.currentTimeMillis();

		System.out.println("Capture With JNA Library: " + (finishTime - startTime) / nbIteration);

		screenCapture = DefaultScreenCapture.getInstance();

		startTime = System.currentTimeMillis();
		for (int i = 0; i < nbIteration; i++) {
			robotImage = screenCapture.captureScreen(rect);
		}
		finishTime = System.currentTimeMillis();

		System.out.println("Capture With Robot Class " + (finishTime - startTime) / nbIteration);

		Image.RgbColor color;

		startTime = System.currentTimeMillis();
		for (int w = 0; w < nbIteration; w++) {
			for (int h = 0; h < nbIteration; h++) {
				color = jnaImage.getRGB(w, h);
				// System.out.print(color);
			}
		}
		finishTime = System.currentTimeMillis();

		System.out.println();
		System.out.println("Browsing Colors With JNA " + (finishTime - startTime));

		startTime = System.currentTimeMillis();
		for (int w = 0; w < nbIteration; w++) {
			for (int h = 0; h < nbIteration; h++) {
				color = robotImage.getRGB(w, h);
				// System.out.print(color);
			}
		}
		finishTime = System.currentTimeMillis();

		System.out.println();
		System.out.println("Browsing Colors With Robot Class " + (finishTime - startTime));
	}
}
