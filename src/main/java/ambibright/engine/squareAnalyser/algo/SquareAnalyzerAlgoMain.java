package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyserAlgorithm;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoMain implements SquareAnalyserAlgorithm {

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

	private final Map<RgbColor, Counter> map;
    private final RgbColor colorHolder;

	private int posX, posY, nbPixel;
	private int counter;
	private int red, green, blue;
	private int half;

	public SquareAnalyzerAlgoMain() {
		map = new HashMap<RgbColor, Counter>();
        colorHolder = new RgbColor();
	}

	@Override
	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {
		map.clear();
		counter = -1;
		red = 0;
		green = 0;
		blue = 0;
        // no need to parse the rest of the pixel if we already found a main color
		half = ((bound.width * bound.height) / screenAnalysePitch) / 2;

		for (posX = 0; posX < bound.width; posX += screenAnalysePitch) {
			for (posY = 0; posY < bound.height; posY += screenAnalysePitch) {
				image.getRGB(bound.x + posX, bound.y + posY, colorHolder);

				Counter colorCounter = map.get(colorHolder);
				if (null == colorCounter) {
					colorCounter = new Counter();
					map.put(new RgbColor(colorHolder.red(), colorHolder.green(), colorHolder.blue()), colorCounter);
				} else {
					colorCounter.increment();
				}
				if (colorCounter.get() > counter) {
					counter = colorCounter.get();
					red = colorHolder.red();
					green = colorHolder.green();
					blue = colorHolder.blue();
				}
				if (counter > half) {
					break;
				}
			}
		}
		return new int[] { red, green, blue };
	}
}
