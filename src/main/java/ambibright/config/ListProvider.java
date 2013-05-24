package ambibright.config;

import java.util.Collection;

public interface ListProvider<T> {

	Collection<String> getAllDisplayableItems();

	T getValueFromDisplayableItem(String item);

    String getDisplayableItemFromValue(T value);

    T getValueFromConfig(String configValue);

    String getConfigFromValue(T value);

}
