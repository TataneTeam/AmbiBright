package ambibright.engine.color;

import ambibright.config.Config;

/**
 *
 */
public abstract class ColorAlgorithm {

	protected final Config config;

	public ColorAlgorithm(Config config) {
		this.config = config;
	}

	/**
	 * @param color
	 *            color in r, g, b
	 */
	public abstract void apply(int[] color);

}
