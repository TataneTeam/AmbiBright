package ambibright.config;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScreenDeviceProvider implements ListProvider {

	private final Map<Object, Object> itemToValue = new LinkedHashMap<Object, Object>();
	private final Map<Object, Object> valueToItem = new LinkedHashMap<Object, Object>();

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
	public Collection<Object> getPossibleItems() {
		return valueToItem.values();
	}

	@Override
	public Object getValueFromItem(Object item) {
		return itemToValue.get(item);
	}

	@Override
	public Object getItemFromValue(Object value) {
		return valueToItem.get(value);
	}
}
