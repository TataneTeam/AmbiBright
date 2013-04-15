package ambibright.engine.squareAnalyser;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoAverage;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMain;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMainAverage;

public enum SquareAnalyser {

	AverageColors(new SquareAnalyzerAlgoAverage()), MainColor(new SquareAnalyzerAlgoMain()), MainAverageColor(new SquareAnalyzerAlgoMainAverage());

	private SquareAnalyserAlgorithm squareAnalyserAlgorithm;

	SquareAnalyser(SquareAnalyserAlgorithm squareAnalyserAlgorithm) {
		this.squareAnalyserAlgorithm = squareAnalyserAlgorithm;
	}

	public int[] getColor(BufferedImage image, Rectangle bound, int screenAnalysePitch) {
		return squareAnalyserAlgorithm.getColor(image, bound, screenAnalysePitch);
	}

}
