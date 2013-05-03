package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyserAlgorithm;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoMain implements SquareAnalyserAlgorithm {

	private Map<Image.RGB, Integer> map;
	private int posX, posY, nbPixel;
	private Image.RGB current;

	public SquareAnalyzerAlgoMain() {
		map = new HashMap<Image.RGB, Integer>();
	}

	@Override
	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {
		map.clear();
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
		nbPixel = -1;
		for (Image.RGB key : map.keySet()) {
			if (map.get(key) > nbPixel) {
				nbPixel = map.get(key);
				current = key;
			}
		}
		return new int[] { current.red(), current.green(), current.blue() };
	}

}
