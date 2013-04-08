package ambibright.engine.color;

import ambibright.ressources.Config;

/**
 *
 */
public interface ColorAlgorithm
{
    /**
     * @param color color in r, g, b
     */
    void apply(int[] color);

    // TODO delete those methods once we got a better configuration system

    /**
     * @return name of the parameter used by this algo
     */
    String getName();

    /**
     * @return the minimal value of the parameter used by this algo
     */
    float getMinValue();

    /**
     * @return the maximal value of the parameter used by this algo
     */
    float getMaxValue();

    /**
     * @return the parameter used by this algo
     */
    Config.Parameters getParameter();

}
