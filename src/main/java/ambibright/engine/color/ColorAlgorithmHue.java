package ambibright.engine.color;

import java.awt.Color;

import ambibright.ressources.Config;
import ambibright.ressources.Config.Parameters;

public class ColorAlgorithmHue extends ColorAlgorithm {

	private float[] hsbVals;
	private int current;

	public ColorAlgorithmHue( Config config ) {
        super(config);
		hsbVals = new float[3];
	}

    public void apply(int[] color) {
		Color.RGBtoHSB(color[0], color[1], color[2], hsbVals);
		current = Color.HSBtoRGB(configValue, hsbVals[1], hsbVals[2]);
		color[0] = (current & 0x00ff0000) >> 16;
		color[1] = (current & 0x0000ff00) >> 8;
		color[2] = current & 0x000000ff;
	}

    public String getName() {
		return "Color Hue";
	}

    public float getMinValue() {
		return 0;
	}

    public float getMaxValue() {
		return Float.MAX_VALUE;
	}

    public Parameters getParameter() {
		return Parameters.CONFIG_COLOR_HUE;
	}

}
