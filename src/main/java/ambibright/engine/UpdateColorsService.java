package ambibright.engine;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.config.Config;
import ambibright.engine.capture.ScreenCapture;
import ambibright.engine.color.ColorAlgorithm;
import ambibright.ressources.CurrentBounds;

/**
 * Compute the colors in screen and sends them to the Arduino
 */
public class UpdateColorsService implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(UpdateColorsService.class);
	private final Config config;
	private final ScreenCapture screenCapture;
	private final Set<ColorsChangeObserver> observers;
	private final List<? extends ColorAlgorithm> colorAlgorithmList;
	private final int[][] result;
	private final int[][] old;
	private final byte[] arduinoArray;
	private final CurrentBounds currentBounds;
	private int pos;

	public UpdateColorsService(Config config, ScreenCapture screenCapture, Set<ColorsChangeObserver> observers, List<? extends ColorAlgorithm> colorAlgorithmList, CurrentBounds currentBounds, byte[] arduinoArray) {
		this.config = config;
		this.screenCapture = screenCapture;
		this.observers = observers;
		this.colorAlgorithmList = colorAlgorithmList;
		this.currentBounds = currentBounds;

		// TODO see if we can recreate them on property change to keep a
		// singleton instance of this service
		int totalNbLed = config.getLedTotalNumber();
		this.old = new int[totalNbLed][3];
		this.result = new int[totalNbLed][3];
		this.arduinoArray = arduinoArray;
	}

	@Override
	public void run() {
		try {
			logger.debug("Processing colors");

			BufferedImage image = screenCapture.captureScreen(currentBounds.getBounds());

			byte[] colors = getColorsToSend(getColors(image));

			// Notify the observers
			for (ColorsChangeObserver observer : observers) {
				observer.onColorsChange(image, colors);
			}

			// Flushing the image
			image.flush();

			logger.debug("Colors processed");
		} catch (Exception e) {
			logger.error("Error while processing the colors", e);
		}
	}

	// L > T > R
	private int[][] getColors(BufferedImage image) {
		pos = 0;

		// Compute for all screen parts
		for (Rectangle bound : currentBounds.getZones()) {
			int[] color = config.getSquareAnalyser().getColor(image, bound, config.getAnalysePitch());
			for (ColorAlgorithm algorithm : colorAlgorithmList) {
				algorithm.apply(color);
			}
			result[pos++] = color;
		}

		return result;
	}

	private byte[] getColorsToSend(int[][] colors) {
		int j = 6;
		for (int i = 0; i < colors.length; i++) {
			old[i][0] = ((colors[i][0] * config.getSmoothing()) + old[i][0]) / (1 + config.getSmoothing());
			old[i][1] = ((colors[i][1] * config.getSmoothing()) + old[i][1]) / (1 + config.getSmoothing());
			old[i][2] = ((colors[i][2] * config.getSmoothing()) + old[i][2]) / (1 + config.getSmoothing());
			arduinoArray[j++] = (byte) (Math.min(Math.max((old[i][0]) + config.getDeltaRed(), 0), 255));
			arduinoArray[j++] = (byte) (Math.min(Math.max((old[i][1]) + config.getDeltaGreen(), 0), 255));
			arduinoArray[j++] = (byte) (Math.min(Math.max((old[i][2]) + config.getDeltaBlue(), 0), 255));
		}
		return arduinoArray;
	}

}
