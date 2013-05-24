package ambibright.engine.squareAnalyser;

import java.awt.Rectangle;

import ambibright.engine.capture.Image;

public interface SquareAnalyser
{

	int[] getColor(Image image, Rectangle bound, int screenAnalysePitch);

}
