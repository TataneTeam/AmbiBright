package ambibright.config;

import java.util.Collection;

public interface ListProvider {
	Collection<Object> getPossibleItems();

	Object getValueFromItem(Object item);

	Object getItemFromValue(Object value);
}
