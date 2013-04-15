package ambibright.engine.color;

import ambibright.ressources.Config;
import ambibright.ressources.Config.Parameters;

public class ColorAlgorithmGamma extends ColorAlgorithm {

	private float gamma;

	public ColorAlgorithmGamma(Config config) {
		super(config);
	}

	public void apply(int[] color) {
		gamma = 1 / configValue;
		color[0] = (int) (255 * Math.pow(color[0] / 255, gamma));
		color[1] = (int) (255 * Math.pow(color[1] / 255, gamma));
		color[2] = (int) (255 * Math.pow(color[2] / 255, gamma));
	}

	public String getName() {
		return "Color Gamma";
	}

	public float getMinValue() {
		return 0;
	}

	public float getMaxValue() {
		return Float.MAX_VALUE;
	}

	public Parameters getParameter() {
		return Parameters.CONFIG_COLOR_GAMMA;
	}

}
