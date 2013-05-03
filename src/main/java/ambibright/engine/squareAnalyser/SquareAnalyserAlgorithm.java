package ambibright.engine.squareAnalyser;

import java.awt.Rectangle;

import ambibright.engine.capture.Image;

public interface SquareAnalyserAlgorithm {

	int[] getColor(Image image, Rectangle bound, int screenAnalysePitch);

}
