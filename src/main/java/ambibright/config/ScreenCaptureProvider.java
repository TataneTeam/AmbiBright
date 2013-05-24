package ambibright.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ambibright.engine.jni.GdiCapture;
import ambibright.engine.jni.DirectXCapture;
import ambibright.engine.capture.ScreenCapture;
import ambibright.engine.capture.RobotScreenCapture;
import ambibright.engine.capture.GdiScreenCapture;
import ambibright.engine.capture.DirectXScreenCapture;

public class ScreenCaptureProvider implements ListProvider<ScreenCapture> {

	private Map<String, LazyInstanceProvider<? extends ScreenCapture>> stringProviderMap = new HashMap<String, LazyInstanceProvider<? extends ScreenCapture>>();
	private Map<Class<? extends ScreenCapture>, String> screenCaptureToNameMap = new HashMap<Class<? extends ScreenCapture>, String>();

	public ScreenCaptureProvider() {
		addScreenCapture(RobotScreenCapture.class, "Robot");
		if (GdiCapture.load()) {
			addScreenCapture(GdiScreenCapture.class, "GDI");
		}
		if (DirectXCapture.load()) {
			addScreenCapture(DirectXScreenCapture.class, "DirectX");
		}
	}

	private <T extends ScreenCapture> void addScreenCapture(Class<T> clazz, String name) {
		stringProviderMap.put(name, new LazyInstanceProvider<T>(clazz));
		screenCaptureToNameMap.put(clazz, name);
	}

	@Override
	public Collection<String> getAllDisplayableItems() {
		return stringProviderMap.keySet();
	}

	@Override
	public ScreenCapture getValueFromDisplayableItem(String item) {
		LazyInstanceProvider<? extends ScreenCapture> lazy = stringProviderMap.get(item);
		return null == lazy ? null : lazy.getInstance();
	}

	@Override
	public String getDisplayableItemFromValue(ScreenCapture value) {
		return screenCaptureToNameMap.get(value.getClass());
	}

	@Override
	public ScreenCapture getValueFromConfig(String configValue) {
		return getValueFromDisplayableItem(configValue);
	}

	@Override
	public String getConfigFromValue(ScreenCapture value) {
		return getDisplayableItemFromValue(value);
	}
}
