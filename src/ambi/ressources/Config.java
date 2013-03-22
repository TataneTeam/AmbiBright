package ambi.ressources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Config {

	public enum Parameters {
		CONFIG_LED_NB_TOP, CONFIG_LED_NB_LEFT, CONFIG_SCREEN_DEVICE, CONFIG_ARDUINO_PORT, CONFIG_PROCESS_LIST, CONFIG_RGB_R, CONFIG_RGB_G, CONFIG_RGB_B, CONFIG_CHECK_PROCESS
	}

	private Properties properties;
	private String path;

	public Config(String path) {
		this.path = path;
		properties = new Properties();
	}

	public void load() {
		try {
			properties.load(new FileInputStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			properties.store(new FileOutputStream(path), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void put(Parameters parameter, String value) {
		properties.put(parameter.toString(), value);
	}

	public String get(Parameters parameter) {
		return properties.getProperty(parameter.toString());
	}

}
