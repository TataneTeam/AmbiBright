package ambibright.engine.colorAnalyser.Algo;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.colorAnalyser.SquareAnalyserAlgorithm;

public class SquareAnalyzerAlgoMainAverage implements SquareAnalyserAlgorithm {

	public static final int nbMain = 3;

	private Map<Integer, Integer> map;
	private int posX, posY, current, nbPixel;
	private int red, green, blue, minimunCount;
	private Object[] sorted;

	public SquareAnalyzerAlgoMainAverage() {
		map = new HashMap<Integer, Integer>();
	}

	public Integer[] getColor(BufferedImage image, Rectangle bound, int screenAnalysePitch) {
		// Init vars
		map.clear();
		nbPixel = 0;
		red = 0;
		green = 0;
		blue = 0;

		// Get all colors
		for (posX = 0; posX < bound.width && posX + bound.x < image.getWidth(); posX += screenAnalysePitch) {
			for (posY = 0; posY < bound.height && posY + bound.y < image.getHeight(); posY += screenAnalysePitch) {
				current = image.getRGB(bound.x + posX, bound.y + posY);
				if (map.containsKey(current)) {
					map.put(current, map.get(current) + 1);
				} else {
					map.put(current, 1);
				}
			}
		}

		// Get minimum count
		if (map.size() > nbMain) {
			sorted = map.keySet().toArray();
			Arrays.sort(sorted, Collections.reverseOrder());
			minimunCount = (Integer) sorted[nbMain - 1];
		} else {
			minimunCount = 0;
		}

		// Get the average
		for (int key : map.keySet()) {
			if (map.get(key) >= minimunCount) {
				red += (current & 0x00ff0000) >> 16;
				green += (current & 0x0000ff00) >> 8;
				blue += current & 0x000000ff;
				nbPixel++;
			}
		}
		return new Integer[] { red / nbPixel, green / nbPixel, blue / nbPixel };
	}

}
