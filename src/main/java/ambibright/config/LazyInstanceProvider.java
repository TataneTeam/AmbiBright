package ambibright.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicolas Morel
 */
public class LazyInstanceProvider<T> {

	private static final Logger logger = LoggerFactory.getLogger(LazyInstanceProvider.class);
	private Class<T> clazz;
	private T instance;

	LazyInstanceProvider(Class<T> clazz) {
		this.clazz = clazz;
	}

	T getInstance() {
		if (null == instance) {
			logger.debug("Create instance of class {}", clazz.getName());
			try {
				instance = clazz.newInstance();
			} catch (Exception e) {
				logger.error("Error creating class {}", clazz.getName(), e);
			}
		}
		return instance;
	}
}
