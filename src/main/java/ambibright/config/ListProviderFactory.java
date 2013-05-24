package ambibright.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicolas Morel
 */
public class ListProviderFactory {

	private static final Logger logger = LoggerFactory.getLogger(ListProviderFactory.class);
	private static Map<Class<? extends ListProvider>, ListProvider> map = new ConcurrentHashMap<Class<? extends ListProvider>, ListProvider>();

	public static ListProvider getProvider(Class<? extends ListProvider> clazz) {
		ListProvider provider = map.get(clazz);
		if (null == provider) {
			logger.debug("Create instance of class {}", clazz.getName());
			try {
				provider = clazz.newInstance();
				map.put(clazz, provider);
			} catch (Exception e) {
				logger.error("Error creating class {}", clazz.getName(), e);
			}
		}
		return provider;
	}
}
