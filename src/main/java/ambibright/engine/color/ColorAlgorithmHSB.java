package ambibright.engine.color;

import java.awt.Color;

import ambibright.config.Config;

public class ColorAlgorithmHSB extends ColorAlgorithm {

	private float[] hsbVals;
	private int current;

	public ColorAlgorithmHSB(Config config) {
		super(config);
		hsbVals = new float[3];
	}

	@Override
	public void apply(int[] color) {
		Color.RGBtoHSB(color[0], color[1], color[2], hsbVals);
		current = Color.HSBtoRGB(minMaxValue(hsbVals[0] + config.getHue()), minMaxValue(hsbVals[1] + config.getSaturation()), minMaxValue(hsbVals[2] + config.getBrightness()));
		color[0] = (current & 0x00ff0000) >> 16;
		color[1] = (current & 0x0000ff00) >> 8;
		color[2] = current & 0x000000ff;
	}

	private float minMaxValue(float value) {
		if (value <= 0f) {
			return 0f;
		} else if (value >= 1f) {
			return 1f;
		} else {
			return value;
		}
	}

}
