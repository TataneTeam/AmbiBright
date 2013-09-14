package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoMainAverage implements SquareAnalyser
{

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

	public SquareAnalyzerAlgoMainAverage() {
		map = new HashMap<RgbColor, Counter>();
		colorHolder = new RgbColor();
	}

	@Override
	public void getColor(Image image, Rectangle bound, int screenAnalysePitch, int[] holder) {

		// Reset vars
		map.clear();

		// average
        int redAverage = 0;
        int greenAverage = 0;
        int blueAverage = 0;
        int nbPixel = 0;

		// main
        int redMain = 0;
        int greenMain = 0;
        int blueMain = 0;
        int threshold = ((bound.width * bound.height) / screenAnalysePitch) * mainColorPourcentage / 100;
        int counter = -1;

		for (int posX = 0; posX < bound.width; posX += screenAnalysePitch) {
			for (int posY = 0; posY < bound.height; posY += screenAnalysePitch) {
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
            holder[0] = redMain;
            holder[1] = greenMain;
            holder[2] = blueMain;
		} else {
            holder[0] = redAverage / nbPixel;
            holder[1] = greenAverage / nbPixel;
            holder[2] = blueAverage / nbPixel;
		}

	}

}
