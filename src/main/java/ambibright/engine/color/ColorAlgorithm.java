package ambibright.engine.color;

import ambibright.ressources.Config;
import ambibright.ressources.Factory;

/**
 *
 */
public abstract class ColorAlgorithm {

	protected final float configValue;

	public ColorAlgorithm() {
		configValue = getConfigValue();
	}

	/**
	 * @param color
	 *            color in r, g, b
	 */
	abstract void apply(int[] color);

	// TODO delete those methods once we got a better configuration system

	/**
	 * @return name of the parameter used by this algo
	 */
	abstract String getName();

	/**
	 * @return the minimal value of the parameter used by this algo
	 */
	abstract float getMinValue();

	/**
	 * @return the maximal value of the parameter used by this algo
	 */
	abstract float getMaxValue();

	/**
	 * @return the parameter used by this algo
	 */
	abstract Config.Parameters getParameter();

	protected float getConfigValue() {
		return Float.valueOf(Factory.get().getConfig().get(getParameter()));
	}

}
