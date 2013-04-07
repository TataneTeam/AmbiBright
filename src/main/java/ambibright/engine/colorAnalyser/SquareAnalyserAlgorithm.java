package ambibright.engine.colorAnalyser;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public interface SquareAnalyserAlgorithm {

	public static final int[] defaultColor = new int[3];

	public int[] getColor(BufferedImage image, Rectangle bound, int screenAnalysePitch);

}
