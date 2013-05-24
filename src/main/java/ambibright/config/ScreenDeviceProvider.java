package ambibright.config;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScreenDeviceProvider implements ListProvider<Integer> {

	private final Map<String, Integer> itemToValue = new LinkedHashMap<String, Integer>();
	private final Map<Integer, String> valueToItem = new LinkedHashMap<Integer, String>();

	public ScreenDeviceProvider() {
		int i = 0;
		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			String item = device.getIDstring() + " - " + device.getDefaultConfiguration().getBounds().width + "x" + device.getDefaultConfiguration().getBounds().height;
			itemToValue.put(item, i);
			valueToItem.put(i, item);
			i++;
		}
	}

	@Override
	public Collection<String> getAllDisplayableItems() {
		return valueToItem.values();
	}

	@Override
	public Integer getValueFromDisplayableItem(String item) {
		return itemToValue.get(item);
	}

	@Override
	public String getDisplayableItemFromValue(Integer value) {
		return valueToItem.get(value);
	}

	@Override
	public Integer getValueFromConfig(String configValue) {
		return Integer.parseInt(configValue);
	}

	@Override
	public String getConfigFromValue(Integer value) {
		return Integer.toString(value);
	}
}
