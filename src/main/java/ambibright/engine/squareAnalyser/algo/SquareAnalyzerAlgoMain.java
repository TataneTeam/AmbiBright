package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoMain implements SquareAnalyser
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

	private final Map<RgbColor, Counter> map;
    private final RgbColor colorHolder;

	public SquareAnalyzerAlgoMain() {
		map = new HashMap<RgbColor, Counter>();
        colorHolder = new RgbColor();
	}

	@Override
	public void getColor(Image image, Rectangle bound, int screenAnalysePitch, int[] holder) {
		map.clear();
		int counter = -1;
        int red = 0;
        int green = 0;
        int blue = 0;
        // no need to parse the rest of the pixel if we already found a main color
        int half = ((bound.width * bound.height) / screenAnalysePitch) / 2;

		for (int posX = 0; posX < bound.width; posX += screenAnalysePitch) {
			for (int posY = 0; posY < bound.height; posY += screenAnalysePitch) {
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

        holder[0] = red;
        holder[1] = green;
        holder[2] = blue;
	}
}
