package ambibright.engine.color;

import java.awt.Color;

import ambibright.ressources.Config.Parameters;

public class ColorAgorithmSaturation extends ColorAlgorithm {

	private float[] hsbVals;
	private int current;

	public ColorAgorithmSaturation() {
		super();
		hsbVals = new float[3];
	}

	void apply(int[] color) {
		Color.RGBtoHSB(color[0], color[1], color[2], hsbVals);
		current = Color.HSBtoRGB(hsbVals[0], configValue, hsbVals[2]);
		color[0] = (current & 0x00ff0000) >> 16;
		color[1] = (current & 0x0000ff00) >> 8;
		color[2] = current & 0x000000ff;
	}

	String getName() {
		return "Color Saturation";
	}

	float getMinValue() {
		return 0;
	}

	float getMaxValue() {
		return 1;
	}

	Parameters getParameter() {
		return Parameters.CONFIG_COLOR_SATURATION;
	}

}
