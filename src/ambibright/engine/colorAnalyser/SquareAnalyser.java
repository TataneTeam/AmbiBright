package ambibright.engine.colorAnalyser;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ambibright.engine.colorAnalyser.Algo.SquareAnalyzerAlgoAverage;
import ambibright.engine.colorAnalyser.Algo.SquareAnalyzerAlgoMain;

public enum SquareAnalyser {

	AverageColor(new SquareAnalyzerAlgoAverage()), MainColor(new SquareAnalyzerAlgoMain());

	private SquareAnalyserAlgorithm squareAnalyserAlgorithm;

	SquareAnalyser(SquareAnalyserAlgorithm squareAnalyserAlgorithm) {
		this.squareAnalyserAlgorithm = squareAnalyserAlgorithm;
	}

	public Integer[] getColor(BufferedImage image, Rectangle bound, int screenAnalysePitch) {
		return squareAnalyserAlgorithm.getColor(image, bound, screenAnalysePitch);
	}

}
