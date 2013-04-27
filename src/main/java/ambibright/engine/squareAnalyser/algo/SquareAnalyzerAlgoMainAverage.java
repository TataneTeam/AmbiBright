package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyserAlgorithm;

public class SquareAnalyzerAlgoMainAverage implements SquareAnalyserAlgorithm {

	public static final float mainColorPourcentage = 0.4f;

	private Map<Integer, Integer> map;
	private int posX, posY, current, nbPixel;
	private int red, green, blue, max, maxColor, currentOccur;

	public SquareAnalyzerAlgoMainAverage() {
		map = new HashMap<Integer, Integer>();
	}

	@Override
	public int[] getColor(BufferedImage image, Rectangle bound, int screenAnalysePitch) {

		// Reset vars
		map.clear();
		nbPixel = 0;
		red = 0;
		green = 0;
		blue = 0;
		max = 0;

		for (posX = 0; posX < bound.width && posX + bound.x < image.getWidth(); posX += screenAnalysePitch) {
			for (posY = 0; posY < bound.height && posY + bound.y < image.getHeight(); posY += screenAnalysePitch) {
				current = image.getRGB(bound.x + posX, bound.y + posY);
				nbPixel++;

				// For average
				red += (current & 0x00ff0000) >> 16;
				green += (current & 0x0000ff00) >> 8;
				blue += current & 0x000000ff;

				// For max
				if (map.containsKey(current)) {
					currentOccur = map.get(current) + 1;
					map.put(current, currentOccur);
					if (currentOccur > max) {
						max = currentOccur;
						maxColor = current;
					}
				} else {
					map.put(current, 1);
				}

			}
		}

		if (max > (nbPixel * mainColorPourcentage)) {
			return new int[] { (maxColor & 0x00ff0000) >> 16, (maxColor & 0x0000ff00) >> 8, maxColor & 0x000000ff };
		} else {
			return new int[] { red / nbPixel, green / nbPixel, blue / nbPixel };
		}

	}

}
