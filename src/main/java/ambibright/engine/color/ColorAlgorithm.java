package ambibright.engine.color;

import ambibright.ressources.Config;

/**
 *
 */
public abstract class ColorAlgorithm {

	protected float configValue;

	public ColorAlgorithm(Config config) {
		configValue = Float.valueOf(config.get(getParameter()));
	}

	/**
	 * @param color
	 *            color in r, g, b
	 */
	public abstract void apply(int[] color);

	// TODO delete those methods once we got a better configuration system

	/**
	 * @return name of the parameter used by this algo
	 */
	public abstract String getName();

	/**
	 * @return the minimal value of the parameter used by this algo
	 */
	public abstract float getMinValue();

	/**
	 * @return the maximal value of the parameter used by this algo
	 */
	public abstract float getMaxValue();

	/**
	 * @return the parameter used by this algo
	 */
	public abstract Config.Parameters getParameter();

	public void updateParameter(float value) {
		configValue = value;
	}
	
	public String toString(){
		return getName();
	}

}
