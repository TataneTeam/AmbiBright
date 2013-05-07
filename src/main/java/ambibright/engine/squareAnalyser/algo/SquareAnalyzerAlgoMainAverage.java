package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyserAlgorithm;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoMainAverage implements SquareAnalyserAlgorithm {

	private class Counter {
		private int counter = 1;

		private Counter increment() {
			counter++;
			return this;
		}

		private int get() {
			return counter;
		}
	}

	private static final int mainColorPourcentage = 40;
	private final Map<RgbColor, Counter> map;
	private final RgbColor colorHolder;
	// average
	private int redAverage, greenAverage, blueAverage, nbPixel;
	// main
	private int redMain, greenMain, blueMain, threshold, counter;
	private int posX, posY;

	public SquareAnalyzerAlgoMainAverage() {
		map = new HashMap<RgbColor, Counter>();
		colorHolder = new RgbColor();
	}

	@Override
	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {

		// Reset vars
		map.clear();

		// average
		redAverage = 0;
		greenAverage = 0;
		blueAverage = 0;
		nbPixel = 0;

		// main
		redMain = 0;
		greenMain = 0;
		blueMain = 0;
		threshold = ((bound.width * bound.height) / screenAnalysePitch) * mainColorPourcentage / 100;
		counter = -1;

		for (posX = 0; posX < bound.width; posX += screenAnalysePitch) {
			for (posY = 0; posY < bound.height; posY += screenAnalysePitch) {
				image.getRGB(bound.x + posX, bound.y + posY, colorHolder);

				// For average
				redAverage += colorHolder.red();
				greenAverage += colorHolder.green();
				blueAverage += colorHolder.blue();
                nbPixel++;

				// For maxCounter
				Counter colorCounter = map.get(colorHolder);
				if (null == colorCounter) {
					colorCounter = new Counter();
					map.put(new RgbColor(colorHolder.red(), colorHolder.green(), colorHolder.blue()), colorCounter);
				} else {
					colorCounter.increment();
				}
                if (colorCounter.get() > counter) {
                    counter = colorCounter.get();
                    redMain = colorHolder.red();
                    greenMain = colorHolder.green();
                    blueMain = colorHolder.blue();
                }
                if (counter > threshold) {
                    break;
                }
			}
		}

		if (counter > threshold) {
			return new int[] { redMain, greenMain, blueMain };
		} else {
			return new int[] { redAverage / nbPixel, greenAverage / nbPixel, blueAverage / nbPixel };
		}

	}

}
