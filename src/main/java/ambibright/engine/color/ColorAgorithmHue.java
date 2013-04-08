package ambibright.engine.color;

import java.awt.Color;

import ambibright.ressources.Config.Parameters;

public class ColorAgorithmHue extends ColorAlgorithm {

	private float[] hsbVals;
	private int current;

	public ColorAgorithmHue() {
		super();
		hsbVals = new float[3];
	}

	void apply(int[] color) {
		Color.RGBtoHSB(color[0], color[1], color[2], hsbVals);
		current = Color.HSBtoRGB(configValue, hsbVals[1], hsbVals[2]);
		color[0] = (current & 0x00ff0000) >> 16;
		color[1] = (current & 0x0000ff00) >> 8;
		color[2] = current & 0x000000ff;
	}

	String getName() {
		return "Color Hue";
	}

	float getMinValue() {
		return 0;
	}

	float getMaxValue() {
		return Float.MAX_VALUE;
	}

	Parameters getParameter() {
		return Parameters.CONFIG_COLOR_HUE;
	}

}
