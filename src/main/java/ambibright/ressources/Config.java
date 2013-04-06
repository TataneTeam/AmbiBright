package ambibright.ressources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import ambibright.engine.colorAnalyser.SquareAnalyser;

public class Config {

	public enum Parameters {

		CONFIG_LED_NB_TOP(24),
		CONFIG_LED_NB_LEFT(14),
		CONFIG_SCREEN_DEVICE(0),
		CONFIG_ARDUINO_PORT("COM1"),
		CONFIG_ARDUINO_DATA_RATE(115200),
		CONFIG_PROCESS_LIST("XBMC.exe vlc.exe"),
		CONFIG_RGB_R(0),
		CONFIG_RGB_G(0),
		CONFIG_RGB_B(0),
		CONFIG_CHECK_PROCESS(true),
		CONFIG_SQUARE_SIZE(30),
		CONFIG_ANALYSE_PITCH(1),
		CONFIG_FPS(24),
		CONFIG_DELAY_CHECK_RATIO(1000),
		CONFIG_DELAY_CHECK_PROCESS(5000),
		CONFIG_MONITORING_XY("0 0"),
		CONFIG_SQUARE_ANALYSER(SquareAnalyser.MainColor),
		CONFIG_UPDATE_URL("https://raw.github.com/TataneTeam/AmbiBright/master/", true);

		private Object defaultValue;
		private boolean forceValue = false;

		private Parameters(Object defaultValue, boolean forceValue) {
			this.defaultValue = defaultValue;
			this.forceValue = forceValue;
		}

		private Parameters(Object defaultValue) {
			this.defaultValue = defaultValue;
		}

		public Object getDefaultValue() {
			return defaultValue;
		}

		public boolean isForceValue() {
			return forceValue;
		}

	}

	private Properties properties;
	private String path;

	public Config(String path) {
		this.path = path;
		properties = new Properties();
	}

	public void init() {
        boolean exists = loadIfExists();
		for (Parameters parameter : Parameters.values()) {
			if (!isSet(parameter) || parameter.isForceValue()) {
				put(parameter, parameter.getDefaultValue().toString());
			}
		}
        if(!exists){
            save();
        }
	}

    private boolean loadIfExists() {
        File configFile = new File(path);
        if(!configFile.exists()){
            return false;
        }
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(configFile);
            properties.load(stream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(null != stream){
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

	public void save() {
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(path);
			properties.store(stream, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void put(Parameters parameter, String value) {
		properties.put(parameter.toString(), value);
	}

	public String get(Parameters parameter) {
		return properties.getProperty(parameter.toString());
	}

	public boolean isSet(Parameters parameter) {
		return properties.containsKey(parameter.toString());
	}

}
