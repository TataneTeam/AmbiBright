package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;

import ambibright.engine.squareAnalyser.SquareAnalyserAlgorithm;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoAverage implements SquareAnalyserAlgorithm {

	/**
	 * RgbColor used to store the value. We create only one instance and update
	 * it to avoid the creation of million of objects.
	 */
	private final RgbColor colorHolder = new RgbColor();
	private int red, green, blue, nbPixel, posX, posY;

	@Override
	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {
		red = 0;
		green = 0;
		blue = 0;
		nbPixel = 0;
		for (posX = 0; posX < bound.width; posX += screenAnalysePitch) {
			for (posY = 0; posY < bound.height; posY += screenAnalysePitch) {
				image.getRGB(bound.x + posX, bound.y + posY, colorHolder );
				red += colorHolder.red();
				green += colorHolder.green();
				blue += colorHolder.blue();
				nbPixel++;
			}
		}
		return new int[] { red / nbPixel, green / nbPixel, blue / nbPixel };
	}
}
