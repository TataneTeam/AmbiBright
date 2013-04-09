package ambibright.engine.squareAnalyser;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface SquareAnalyserAlgorithm {

	public int[] getColor(BufferedImage image, Rectangle bound, int screenAnalysePitch);

}
