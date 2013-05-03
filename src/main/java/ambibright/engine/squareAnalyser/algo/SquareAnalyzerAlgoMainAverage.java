package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyserAlgorithm;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoMainAverage implements SquareAnalyserAlgorithm {

	public static final float mainColorPourcentage = 0.4f;
	private Map<Image.RgbColor, Integer> map;
	private Image.RgbColor maxColor;
	private int posX, posY, nbPixel;
	private int red, green, blue, max, currentOccur;

	public SquareAnalyzerAlgoMainAverage() {
		map = new HashMap<Image.RgbColor, Integer>();
	}

	@Override
	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {

		// Reset vars
		map.clear();
		nbPixel = 0;
		red = 0;
		green = 0;
		blue = 0;
		max = 0;

		for (posX = 0; posX < bound.width && posX + bound.x < image.getWidth(); posX += screenAnalysePitch) {
			for (posY = 0; posY < bound.height && posY + bound.y < image.getHeight(); posY += screenAnalysePitch) {
				Image.RgbColor current = image.getRGB(bound.x + posX, bound.y + posY);
				nbPixel++;

				// For average
				red += current.red();
				green += current.green();
				blue += current.blue();

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
			return new int[] { maxColor.red(), maxColor.green(), maxColor.blue() };
		} else {
			return new int[] { red / nbPixel, green / nbPixel, blue / nbPixel };
		}

	}

}
