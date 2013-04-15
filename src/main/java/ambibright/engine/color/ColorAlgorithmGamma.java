package ambibright.engine.color;

import ambibright.config.Config;

public class ColorAlgorithmGamma extends ColorAlgorithm {

	private float gamma;

	public ColorAlgorithmGamma(Config config) {
		super(config);
	}

	public void apply(int[] color) {
		gamma = 1 / config.getGamma();
		color[0] = (int) (255 * Math.pow(color[0] / 255, gamma));
		color[1] = (int) (255 * Math.pow(color[1] / 255, gamma));
		color[2] = (int) (255 * Math.pow(color[2] / 255, gamma));
	}

}
