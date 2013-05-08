package ambibright.engine.squareAnalyser;

import java.awt.Rectangle;

import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMainAverage;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMain;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoAverage;
import ambibright.engine.capture.Image;

public enum SquareAnalyser implements SquareAnalyserAlgorithm {

	AverageColors(new SquareAnalyzerAlgoAverage()),
    MainColor(new SquareAnalyzerAlgoMain()),
    MainAverageColor(new SquareAnalyzerAlgoMainAverage());

    private SquareAnalyserAlgorithm squareAnalyserAlgorithm;

	SquareAnalyser(SquareAnalyserAlgorithm squareAnalyserAlgorithm) {
		this.squareAnalyserAlgorithm = squareAnalyserAlgorithm;
	}

	public int[] getColor(Image image, Rectangle bound, int screenAnalysePitch) {
		return squareAnalyserAlgorithm.getColor(image, bound, screenAnalysePitch);
	}

}
