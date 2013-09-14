package ambibright.engine.squareAnalyser;

import java.awt.Rectangle;

import ambibright.engine.capture.Image;

public interface SquareAnalyser
{

	void getColor(Image image, Rectangle bound, int screenAnalysePitch, int[] holder);

}
