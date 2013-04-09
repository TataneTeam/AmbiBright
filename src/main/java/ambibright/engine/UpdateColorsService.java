package ambibright.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import ambibright.ressources.CurrentBounds;
import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.color.ColorAlgorithm;
import ambibright.engine.capture.ScreenCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compute the colors in screen and sends them to the Arduino
 */
public class UpdateColorsService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger( UpdateColorsService.class );

	private final ScreenCapture screenCapture;
	private final Set<ColorsChangeObserver> observers;
	private final List<ColorAlgorithm> colorAlgorithmList;
	private final int[][] result;
	private final int[][] old;
	private final byte[] arduinoArray;
	private final CurrentBounds currentBounds;
	private final SquareAnalyser colorAnalyser;
	private int deltaR, deltaG, deltaB, smoothing;
	private int pos;
	private int screenAnalysePitch;

	public UpdateColorsService(ScreenCapture screenCapture, Set<ColorsChangeObserver> observers, List<ColorAlgorithm> colorAlgorithmList, CurrentBounds currentBounds, SquareAnalyser colorAnalyser, int screenAnalysePitch, int nbLed, int red, int green, int blue, int smoothing, byte[] arduinoArray) {
		this.screenCapture = screenCapture;
		this.observers = observers;
		this.colorAlgorithmList = colorAlgorithmList;
		this.currentBounds = currentBounds;
		this.colorAnalyser = colorAnalyser;
		this.screenAnalysePitch = screenAnalysePitch;
		this.deltaR = red;
		this.deltaG = green;
		this.deltaB = blue;
		this.old = new int[nbLed][3];
		this.result = new int[nbLed][3];
		this.arduinoArray = arduinoArray;
		this.smoothing = smoothing;
	}

	public void run() {
		try {
            logger.debug( "Processing colors" );

			BufferedImage image = screenCapture.captureScreen(currentBounds.getBounds());

			byte[] colors = getColorsToSend(getColors(image));

			// Notify the observers
			for (ColorsChangeObserver observer : observers) {
				observer.onColorsChange(image, colors);
			}

			// Flushing the image
			image.flush();

            logger.debug( "Colors processed" );
		} catch (Exception e) {
			logger.error( "Error while processing the colors", e );
		}
	}

	// L > T > R
	private int[][] getColors(BufferedImage image) {
		pos = 0;

		// Compute for all screen parts
		for (Rectangle bound : currentBounds.getZones()) {
			int[] color = colorAnalyser.getColor(image, bound, screenAnalysePitch);
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
			old[i][0] = ((colors[i][0] * smoothing) + old[i][0]) / (1 + smoothing);
			old[i][1] = ((colors[i][1] * smoothing) + old[i][1]) / (1 + smoothing);
			old[i][2] = ((colors[i][2] * smoothing) + old[i][2]) / (1 + smoothing);
			arduinoArray[j++] = (byte) (Math.min(Math.max((old[i][0]) + deltaR, 0), 255));
			arduinoArray[j++] = (byte) (Math.min(Math.max((old[i][1]) + deltaG, 0), 255));
			arduinoArray[j++] = (byte) (Math.min(Math.max((old[i][2]) + deltaB, 0), 255));
		}
		return arduinoArray;
	}

	public void setDeltaRGB(int red, int green, int blue) {
		this.deltaR = red;
		this.deltaG = green;
		this.deltaB = blue;
	}

}
