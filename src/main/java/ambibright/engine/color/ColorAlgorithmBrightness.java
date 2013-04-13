package ambibright.engine.color;

import java.awt.Color;

import ambibright.ressources.Config;
import ambibright.ressources.Config.Parameters;

public class ColorAlgorithmBrightness extends ColorAlgorithm {

	private float[] hsbVals;
	private int current;

	public ColorAlgorithmBrightness(Config config) {
		super(config);
		hsbVals = new float[3];
	}

	public void apply(int[] color) {
		Color.RGBtoHSB(color[0], color[1], color[2], hsbVals);
		current = Color.HSBtoRGB(hsbVals[0], hsbVals[1], Math.max(0f, Math.min(1f, (hsbVals[2] + configValue))));
		color[0] = (current & 0x00ff0000) >> 16;
		color[1] = (current & 0x0000ff00) >> 8;
		color[2] = current & 0x000000ff;
	}

	public String getName() {
		return "Color Brightness";
	}

	public float getMinValue() {
		return -1;
	}

	public float getMaxValue() {
		return 1;
	}

	public Parameters getParameter() {
		return Parameters.CONFIG_COLOR_BRIGHTNESS;
	}

}
