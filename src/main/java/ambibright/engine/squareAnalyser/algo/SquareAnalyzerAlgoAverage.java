package ambibright.engine.squareAnalyser.algo;

import java.awt.Rectangle;

import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.capture.RgbColor;
import ambibright.engine.capture.Image;

public class SquareAnalyzerAlgoAverage implements SquareAnalyser
{

	@Override
	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {
		// RgbColor used to store the value. We create only one instance and
		// update it to avoid the creation of million of objects.
		RgbColor colorHolder = new RgbColor();

		int red = 0;
		int green = 0;
		int blue = 0;
		int nbPixel = 0;
		for (int posX = 0; posX < bound.width; posX += screenAnalysePitch) {
			for (int posY = 0; posY < bound.height; posY += screenAnalysePitch) {
				image.getRGB(bound.x + posX, bound.y + posY, colorHolder);
				red += colorHolder.red();
				green += colorHolder.green();
				blue += colorHolder.blue();
				nbPixel++;
			}
		}
		return new int[] { red / nbPixel, green / nbPixel, blue / nbPixel };
	}
}
