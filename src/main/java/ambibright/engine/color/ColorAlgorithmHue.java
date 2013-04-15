package ambibright.engine.color;

import java.awt.*;

import ambibright.config.Config;

public class ColorAlgorithmHue extends ColorAlgorithm {

	private float[] hsbVals;
	private int current;

	public ColorAlgorithmHue(Config config) {
		super(config);
		hsbVals = new float[3];
	}

	public void apply(int[] color) {
		Color.RGBtoHSB(color[0], color[1], color[2], hsbVals);
		current = Color.HSBtoRGB(hsbVals[0] + config.getHue(), hsbVals[1], hsbVals[2]);
		color[0] = (current & 0x00ff0000) >> 16;
		color[1] = (current & 0x0000ff00) >> 8;
		color[2] = current & 0x000000ff;
	}

}
