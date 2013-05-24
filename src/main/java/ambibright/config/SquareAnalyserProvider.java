package ambibright.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMainAverage;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoMain;
import ambibright.engine.squareAnalyser.algo.SquareAnalyzerAlgoAverage;

public class SquareAnalyserProvider implements ListProvider<SquareAnalyser> {

	private Map<String, LazyInstanceProvider<? extends SquareAnalyser>> stringProviderMap = new HashMap<String, LazyInstanceProvider<? extends SquareAnalyser>>();
	private Map<Class<? extends SquareAnalyser>, String> screenCaptureToNameMap = new HashMap<Class<? extends SquareAnalyser>, String>();

	public SquareAnalyserProvider() {
		addSquareAnalyserAlgorithm(SquareAnalyzerAlgoAverage.class, "AverageColors");
		addSquareAnalyserAlgorithm(SquareAnalyzerAlgoMain.class, "MainColor");
		addSquareAnalyserAlgorithm(SquareAnalyzerAlgoMainAverage.class, "MainAverageColor");
	}

	private <T extends SquareAnalyser> void addSquareAnalyserAlgorithm(Class<T> clazz, String name) {
		stringProviderMap.put(name, new LazyInstanceProvider<T>(clazz));
		screenCaptureToNameMap.put(clazz, name);
	}

	@Override
	public Collection<String> getAllDisplayableItems() {
		return stringProviderMap.keySet();
	}

	@Override
	public SquareAnalyser getValueFromDisplayableItem(String item) {
		return stringProviderMap.get(item).getInstance();
	}

	@Override
	public String getDisplayableItemFromValue(SquareAnalyser value) {
		return screenCaptureToNameMap.get(value.getClass());
	}

	@Override
	public SquareAnalyser getValueFromConfig(String configValue) {
		return getValueFromDisplayableItem(configValue);
	}

	@Override
	public String getConfigFromValue(SquareAnalyser value) {
		return getDisplayableItemFromValue(value);
	}
}
