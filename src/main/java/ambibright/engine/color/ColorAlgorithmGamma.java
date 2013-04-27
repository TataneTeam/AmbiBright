package ambibright.engine.color;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.config.Config;

/**
 * Algorithm used to apply gamma correction. <a href=
 * "http://stackoverflow.com/questions/14298169/apply-gamma-correction-to-packed-integer-pixel"
 * >Took from here</a>
 */
public class ColorAlgorithmGamma extends ColorAlgorithm {

	private static final Logger logger = LoggerFactory.getLogger(ColorAlgorithmGamma.class);

	private int[] gammaTable;

	public ColorAlgorithmGamma(Config config) {
		super(config);
		buildGammaTable(config.getGamma());
		config.addPropertyChangeListener(Config.CONFIG_COLOR_GAMMA, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				buildGammaTable((Float) evt.getNewValue());
			}
		});
	}

	/**
	 * Called when a new gamma value is set to rebuild the gamma table.
	 */
	private synchronized void buildGammaTable(float gamma) {
		logger.debug("Building new gamma table for the value : {}", gamma);
		int[] table = new int[256];
		float ginv = 1 / gamma;
		double colors = 255.0;
		for (int i = 0; i < table.length; i++) {
			table[i] = (int) Math.round(colors * Math.pow(i / colors, ginv));
		}
		gammaTable = table;
	}

	@Override
	public void apply(int[] color) {
		color[0] = gammaTable[color[0]];
		color[1] = gammaTable[color[1]];
		color[2] = gammaTable[color[2]];
	}

}
