package ambibright.engine;

import java.awt.Rectangle;
import java.util.Set;

import ambibright.config.Config;
import ambibright.engine.capture.Image;
import ambibright.engine.color.ColorAlgorithm;
import ambibright.ressources.CurrentBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compute the colors in screen and sends them to the Arduino
 */
public class UpdateColorsService implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(UpdateColorsService.class);
	private final Config config;
	private final Set<ColorsChangeObserver> observers;
	private final ColorAlgorithm[] colorAlgorithms;
	private final int[][] result;
	private final int[][] old;
	private final byte[] arduinoArray;
	private final CurrentBounds currentBounds;

    public UpdateColorsService(Config config, Set<ColorsChangeObserver> observers, ColorAlgorithm[] colorAlgorithms,
                               CurrentBounds currentBounds, byte[] arduinoArray) {
		this.config = config;
		this.observers = observers;
		this.colorAlgorithms = colorAlgorithms;
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
			logger.trace("Processing colors");

			// we retrieve the current bounds and zone in the same lock to be
			// sure we have coherent datas
			int screenDevice;
			Rectangle bounds;
			Rectangle[] zones;
			currentBounds.readLock();
			try {
				screenDevice = currentBounds.getScreenDeviceNoLock();
				bounds = currentBounds.getBoundsNoLock();
				zones = currentBounds.getZonesNoLock();
			} finally {
				currentBounds.readUnlock();
			}

			Image image = config.getScreenCapture().captureScreen(bounds, screenDevice);

			byte[] colors = getColorsToSend(getColors(image, zones));

			// Notify the observers
			for (ColorsChangeObserver observer : observers) {
				observer.onColorsChange(image, colors);
			}

			// Flushing the image
			image.flush();

			logger.trace("Colors processed");
		} catch (Exception e) {
			logger.error("Error while processing the colors", e);
		}
	}

	// L > T > R
	private int[][] getColors(Image image, Rectangle[] zones) {
        int pos = 0;

		// Compute for all screen parts
		for (Rectangle bound : zones) {
            int[] color = result[pos++];
			config.getSquareAnalyser().getColor(image, bound, config.getAnalysePitch(), color);
			for (ColorAlgorithm algorithm : colorAlgorithms ) {
				algorithm.apply(color);
			}
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
