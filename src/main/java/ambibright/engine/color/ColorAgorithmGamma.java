package ambibright.engine.color;

import ambibright.ressources.Config.Parameters;

public class ColorAgorithmGamma extends ColorAlgorithm {
	
	private final float gamma;
	
	public ColorAgorithmGamma(){
		super();
		gamma = 1 / configValue;
	}

	void apply(int[] color) {
		color[0] = (int) (255 * Math.pow(color[0] / 255, gamma));
		color[1] = (int) (255 * Math.pow(color[1] / 255, gamma));
		color[2] = (int) (255 * Math.pow(color[2] / 255, gamma));
	}

	String getName() {
		return "Color Gamma";
	}

	float getMinValue() {
		return 0;
	}

	float getMaxValue() {
		return Float.MAX_VALUE;
	}

	Parameters getParameter() {
		return Parameters.CONFIG_COLOR_GAMMA;
	}

}
