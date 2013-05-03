package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;

import ambibright.engine.capture.Image;
import ambibright.engine.squareAnalyser.SquareAnalyserAlgorithm;

public class SquareAnalyzerAlgoAverage implements SquareAnalyserAlgorithm {

    private Image.RgbColor current;
	private int red, green, blue, nbPixel, posX, posY;

	@Override
	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {
		red = 0;
		green = 0;
		blue = 0;
		nbPixel = 0;
		for (posX = 0; posX < bound.width; posX += screenAnalysePitch) {
			for (posY = 0; posY < bound.height; posY += screenAnalysePitch) {
                current = image.getRGB(bound.x + posX, bound.y + posY);
				red += current.red();
				green += current.green();
				blue += current.blue();
				nbPixel++;
			}
		}
		return new int[] { red / nbPixel, green / nbPixel, blue / nbPixel };
	}
}
