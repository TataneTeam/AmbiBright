package ambibright.config;

import java.util.ArrayList;
import java.util.List;

import ambibright.engine.capture.ScreenCapture;

public class ScreenCaptureProvider implements ListProvider {
	private static final List<Object> list;
	static {
		list = new ArrayList<Object>();
		for (ScreenCapture c : ScreenCapture.values()) {
			list.add(c);
		}
	}

	@Override
	public List<Object> getPossibleItems() {
		return list;
	}

	@Override
	public Object getValueFromItem(Object item) {
		return item;
	}

	@Override
	public Object getItemFromValue(Object value) {
		return value;
	}
}
