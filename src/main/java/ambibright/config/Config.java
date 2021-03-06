package ambibright.config;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.engine.squareAnalyser.SquareAnalyser;
import ambibright.engine.capture.ScreenCapture;

public class Config {

	public static class PropertyChangeRegistration {
		private Config config;
		private String property;
		private PropertyChangeListener listener;

		private PropertyChangeRegistration(Config config, String property, PropertyChangeListener listener) {
			this.config = config;
			this.property = property;
			this.listener = listener;
		}

		public void removeListener() {
			config.removePropertyChangeListener(property, listener);
		}
	}

	public static final String GROUP_CONFIG = "GROUP_CONFIG";
	public static final String GROUP_COLOR = "GROUP_COLOR";
	public static final String GROUP_MANUAL = "GROUP_MANUAL";
	public static final String CONFIG_LED_NB_TOP = "CONFIG_LED_NB_TOP";
	public static final String CONFIG_LED_NB_LEFT = "CONFIG_LED_NB_LEFT";
	public static final String CONFIG_SCREEN_DEVICE = "CONFIG_SCREEN_DEVICE";
	public static final String CONFIG_ARDUINO_PORT = "CONFIG_ARDUINO_PORT";
	public static final String CONFIG_ARDUINO_DATA_RATE = "CONFIG_ARDUINO_DATA_RATE";
	public static final String CONFIG_PROCESS_LIST = "CONFIG_PROCESS_LIST";
	public static final String CONFIG_RGB_R = "CONFIG_RGB_R";
	public static final String CONFIG_RGB_G = "CONFIG_RGB_G";
	public static final String CONFIG_RGB_B = "CONFIG_RGB_B";
	public static final String CONFIG_CHECK_PROCESS = "CONFIG_CHECK_PROCESS";
	public static final String CONFIG_SQUARE_SIZE = "CONFIG_SQUARE_SIZE";
	public static final String CONFIG_ANALYSE_PITCH = "CONFIG_ANALYSE_PITCH";
	public static final String CONFIG_FPS = "CONFIG_FPS";
	public static final String CONFIG_DELAY_CHECK_RATIO = "CONFIG_DELAY_CHECK_RATIO";
	public static final String CONFIG_DELAY_CHECK_PROCESS = "CONFIG_DELAY_CHECK_PROCESS";
	public static final String CONFIG_MONITORING_XY = "CONFIG_MONITORING_XY";
	public static final String CONFIG_SQUARE_ANALYSER = "CONFIG_SQUARE_ANALYSER";
	public static final String CONFIG_UPDATE_URL = "CONFIG_UPDATE_URL";
	public static final String CONFIG_SMOOTHING = "CONFIG_SMOOTHING";
	public static final String CONFIG_SHOW_FPS_FRAME = "CONFIG_SHOW_FPS_FRAME";
	public static final String CONFIG_BLACK_OTHER_SCREENS = "CONFIG_BLACK_OTHER_SCREENS";
	public static final String CONFIG_COLOR_GAMMA = "CONFIG_COLOR_GAMMA";
	public static final String CONFIG_COLOR_HUE = "CONFIG_COLOR_HUE";
	public static final String CONFIG_COLOR_SATURATION = "CONFIG_COLOR_SATURATION";
	public static final String CONFIG_COLOR_BRIGHTNESS = "CONFIG_COLOR_BRIGHTNESS";
	public static final String CONFIG_SCREEN_CAPTURE = "CONFIG_SCREEN_CAPTURE";
    public static final String CONFIG_ASPECT_RATIO_DEBUG = "CONFIG_ASPECT_RATIO_DEBUG";
	private static final Logger logger = LoggerFactory.getLogger(Config.class);
	private static final String configFileName = "AmbiBright.properties";
	private static final Config instance;
	static {
		instance = new Config();
		instance.init();
	}

	public static Config getInstance() {
		return instance;
	}

	@Configurable(label = "Top LED number", key = CONFIG_LED_NB_TOP, defaultValue = "24")
	private volatile int nbLedTop;
	@Configurable(label = "Left/Right LED number", key = CONFIG_LED_NB_LEFT, defaultValue = "14")
	private volatile int nbLedLeft;
	@Configurable(label = "Screen", key = CONFIG_SCREEN_DEVICE, defaultValue = "0")
	@PredefinedList(provider = ScreenDeviceProvider.class)
	private volatile int screenDevice;
	@Configurable(label = "Arduino Serial Port", key = CONFIG_ARDUINO_PORT, defaultValue = "COM1")
	private volatile String arduinoSerialPort;
	@Configurable(label = "Arduino Data Rate", key = CONFIG_ARDUINO_DATA_RATE, defaultValue = "115200")
	private volatile int arduinoDataRate;
	@Configurable(label = "Check for Process list", key = CONFIG_PROCESS_LIST, defaultValue = "XBMC.exe vlc.exe")
	private volatile String checkProcessList;
	@Configurable(label = "Red", key = CONFIG_RGB_R, defaultValue = "0", group = GROUP_COLOR)
	@IntInterval(min = -150, max = 150)
	private volatile int deltaRed;
	@Configurable(label = "Green", key = CONFIG_RGB_G, defaultValue = "0", group = GROUP_COLOR)
	@IntInterval(min = -150, max = 150)
	private volatile int deltaGreen;
	@Configurable(label = "Blue", key = CONFIG_RGB_B, defaultValue = "0", group = GROUP_COLOR)
	@IntInterval(min = -150, max = 150)
	private volatile int deltaBlue;
	@Configurable(label = "Check for Process", key = CONFIG_CHECK_PROCESS, defaultValue = "true")
	private volatile boolean checkProcess;
	@Configurable(label = "Square Size", key = CONFIG_SQUARE_SIZE, defaultValue = "30")
	private volatile int squareSize;
	@Configurable(label = "Capture analyse pitch", key = CONFIG_ANALYSE_PITCH, defaultValue = "1")
	private volatile int analysePitch;
	@Configurable(label = "FPS", key = CONFIG_FPS, defaultValue = "24")
	private volatile int fps;
	@Configurable(label = "Delay check ratio", key = CONFIG_DELAY_CHECK_RATIO, defaultValue = "750")
	private volatile int checkRatioDelay;
	@Configurable(label = "Delay check process", key = CONFIG_DELAY_CHECK_PROCESS, defaultValue = "5000")
	private volatile int checkProcessDelay;
	@Configurable(label = "Monitoring frame position", key = CONFIG_MONITORING_XY, defaultValue = "0 0", group = GROUP_MANUAL)
	private volatile String monitoringFramePosition;
	@Configurable(label = "Square analyser", key = CONFIG_SQUARE_ANALYSER, defaultValue = "MainColor")
	@PredefinedList(provider = SquareAnalyserProvider.class)
	private volatile SquareAnalyser squareAnalyser;
	@Configurable(label = "Update Url", key = CONFIG_UPDATE_URL, defaultValue = "https://raw.github.com/TataneTeam/AmbiBright/master/", forceValue = true)
	private volatile String updateUrl;
	@Configurable(label = "Smoothing", key = CONFIG_SMOOTHING, defaultValue = "1")
	@IntInterval(min = 1, max = 6)
	private volatile int smoothing;
	@Configurable(label = "Show FPS Frame", key = CONFIG_SHOW_FPS_FRAME, defaultValue = "false")
	private volatile boolean showFpsFrame;
	@Configurable(label = "Black other screens", key = CONFIG_BLACK_OTHER_SCREENS, defaultValue = "false")
	private volatile boolean blackOtherScreens;
	@Configurable(label = "Gamma", key = CONFIG_COLOR_GAMMA, defaultValue = "1", group = GROUP_COLOR)
	@FloatInterval(min = 0.05f, max = 5.05f)
	private volatile float gamma;
	@Configurable(label = "Hue", key = CONFIG_COLOR_HUE, defaultValue = "0", group = GROUP_COLOR)
	@FloatInterval(min = -1f, max = 1f)
	private volatile float hue;
	@Configurable(label = "Saturation", key = CONFIG_COLOR_SATURATION, defaultValue = "0", group = GROUP_COLOR)
	@FloatInterval(min = -1f, max = 1f)
	private volatile float saturation;
	@Configurable(label = "Brightness", key = CONFIG_COLOR_BRIGHTNESS, defaultValue = "0", group = GROUP_COLOR)
	@FloatInterval(min = -1f, max = 1f)
	private volatile float brightness;
	@Configurable(label = "Screen capture method", key = CONFIG_SCREEN_CAPTURE, defaultValue = "Robot")
	@PredefinedList(provider = ScreenCaptureProvider.class)
	private volatile ScreenCapture screenCapture;
    @Configurable(label = "Notify when aspect ratio change", key = CONFIG_ASPECT_RATIO_DEBUG, defaultValue = "false")
    private volatile boolean aspectRatioDebug;
	private Map<String, Field> keyToField = new LinkedHashMap<String, Field>();
	private Map<Field, String> fieldToKey = new LinkedHashMap<Field, String>();
	private Map<String, List<Field>> groupToFields = new LinkedHashMap<String, List<Field>>();
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	private Config() {
	}

	private void init() {
		keyToField.clear();
		Properties properties = loadIfExists();
		for (Field field : getClass().getDeclaredFields()) {
			Configurable configurable = field.getAnnotation(Configurable.class);
			if (null != configurable) {
				keyToField.put(configurable.key(), field);
				fieldToKey.put(field, configurable.key());

				List<Field> fieldsByGroup = groupToFields.get(configurable.group());
				if (null == fieldsByGroup) {
					fieldsByGroup = new ArrayList<Field>();
					groupToFields.put(configurable.group(), fieldsByGroup);
				}
				fieldsByGroup.add(field);

				String value;
				if (configurable.forceValue()) {
					value = configurable.defaultValue();
				} else if (isSet(properties, configurable.key())) {
					value = properties.getProperty(configurable.key());
				} else {
					value = configurable.defaultValue();
				}
				try {
					setValue(field, convertStringToFieldValue(field, value, configurable.defaultValue()), false);
				} catch (Exception e) {
					logger.error("Error converting property value {} : {}", configurable.key(), value, e);
					System.exit(0);
				}
			}
		}
		if (null == properties) {
			save();
		}
	}

	@SuppressWarnings("unchecked")
	private Object convertStringToFieldValue(Field field, String value, String fallbackValue) {
		if (field.isAnnotationPresent(PredefinedList.class)) {
			PredefinedList predefinedList = field.getAnnotation(PredefinedList.class);
			ListProvider listProvider = ListProviderFactory.getProvider(predefinedList.provider());
			Object res = listProvider.getValueFromConfig(value);
			if (null == res) {
				res = listProvider.getValueFromConfig(fallbackValue);
			}
			return res;
		} else if (String.class == field.getType()) {
			return value;
		} else if (boolean.class == field.getType()) {
			return Boolean.valueOf(value);
		} else if (int.class == field.getType()) {
			try {
				return Integer.valueOf(value);
			} catch (NumberFormatException e) {
				return Integer.valueOf(fallbackValue);
			}
		} else if (float.class == field.getType()) {
			try {
				return Float.valueOf(value);
			} catch (NumberFormatException e) {
				return Float.valueOf(fallbackValue);
			}
		} else if (field.getType().isEnum()) {
			try {
				return Enum.valueOf((Class<? extends Enum>) field.getType(), value);
			} catch (Exception e) {
				return Enum.valueOf((Class<? extends Enum>) field.getType(), fallbackValue);
			}
		} else {
			throw new IllegalArgumentException("Unknown type");
		}
	}

	private boolean isSet(Properties properties, String key) {
		return null != properties && properties.containsKey(key);
	}

	private Properties loadIfExists() {
		File configFile = new File(configFileName);
		if (!configFile.exists()) {
			return null;
		}
		FileInputStream stream = null;
		try {
			Properties properties = new Properties();
			stream = new FileInputStream(configFile);
			properties.load(stream);
			return properties;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void save() {
		Properties properties = new Properties();
		for (Field field : getClass().getDeclaredFields()) {
			Configurable configurable = field.getAnnotation(Configurable.class);
			if (null != configurable) {
				String value = convertFieldValueToString(field);
				properties.put(configurable.key(), value);
			}
		}

		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(configFileName);
			properties.store(stream, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != stream) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String convertFieldValueToString(Field field) {
		String value;
		try {
			if (field.isAnnotationPresent(PredefinedList.class)) {
				PredefinedList predefinedList = field.getAnnotation(PredefinedList.class);
				ListProvider listProvider = ListProviderFactory.getProvider(predefinedList.provider());
				value = listProvider.getConfigFromValue(field.get(this));
			} else if (String.class == field.getType()) {
				value = (String) field.get(this);
			} else if (boolean.class == field.getType()) {
				value = Boolean.toString(field.getBoolean(this));
			} else if (int.class == field.getType()) {
				value = Integer.toString(field.getInt(this));
			} else if (float.class == field.getType()) {
				value = Float.toString(field.getFloat(this));
			} else if (field.getType().isEnum()) {
				value = ((Enum<?>) field.get(this)).name();
			} else {
				throw new IllegalArgumentException("Unknown type");
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error retrieving a value", e);
		}
		return value;
	}

	public Collection<Field> getFieldsByGroup(String group) {
		return groupToFields.get(group);
	}

	public Object getValue(Field field) {
		try {
			return field.get(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error retrieving a value", e);
		}
	}

	public void setValue(Field field, Object value) {
		setValue(field, value, true);
	}

	private void setValue(Field field, Object value, boolean fireEvent) {
		try {
			Object oldValue = field.get(this);
			field.set(this, value);
			if (fireEvent) {
				changes.firePropertyChange(fieldToKey.get(field), oldValue, value);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error setting a value", e);
		}
	}

	public Config cloneConfig() {
		Config config = new Config();
		for (Field field : keyToField.values()) {
			try {
				field.set(config, field.get(this));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return config;
	}

	public void restore(Config config) {
		for (Field field : keyToField.values()) {
			try {
				setValue(field, field.get(config));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public void resetToDefault(Collection<Field> fields) {
		for (Field field : fields) {
			resetToDefault(field);
		}
	}

	public void resetToDefault(Field field) {
		String defaultValue = field.getAnnotation(Configurable.class).defaultValue();
		setValue(field, convertStringToFieldValue(field, defaultValue, defaultValue));
	}

	public PropertyChangeRegistration addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changes.addPropertyChangeListener(propertyName, listener);
		return new PropertyChangeRegistration(this, propertyName, listener);
	}

	private void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changes.removePropertyChangeListener(propertyName, listener);
	}

	public int getLedTotalNumber() {
		return getNbLedLeft() * 2 + getNbLedTop() - 2;
	}

	public int getNbLedTop() {
		return nbLedTop;
	}

	public void setNbLedTop(int nbLedTop) {
		int oldValue = this.nbLedTop;
		this.nbLedTop = nbLedTop;
		changes.firePropertyChange(CONFIG_LED_NB_TOP, oldValue, nbLedTop);
	}

	public int getNbLedLeft() {
		return nbLedLeft;
	}

	public void setNbLedLeft(int nbLedLeft) {
		int oldValue = this.nbLedLeft;
		this.nbLedLeft = nbLedLeft;
		changes.firePropertyChange(CONFIG_LED_NB_LEFT, oldValue, nbLedLeft);
	}

	public int getScreenDevice() {
		return screenDevice;
	}

	public void setScreenDevice(int screenDevice) {
		int oldValue = this.screenDevice;
		this.screenDevice = screenDevice;
		changes.firePropertyChange(CONFIG_SCREEN_DEVICE, oldValue, screenDevice);
	}

	public String getArduinoSerialPort() {
		return arduinoSerialPort;
	}

	public void setArduinoSerialPort(String arduinoSerialPort) {
		String oldValue = this.arduinoSerialPort;
		this.arduinoSerialPort = arduinoSerialPort;
		changes.firePropertyChange(CONFIG_ARDUINO_PORT, oldValue, arduinoSerialPort);
	}

	public int getArduinoDataRate() {
		return arduinoDataRate;
	}

	public void setArduinoDataRate(int arduinoDataRate) {
		int oldValue = this.arduinoDataRate;
		this.arduinoDataRate = arduinoDataRate;
		changes.firePropertyChange(CONFIG_ARDUINO_DATA_RATE, oldValue, arduinoDataRate);
	}

	public String getCheckProcessList() {
		return checkProcessList;
	}

	public void setCheckProcessList(String checkProcessList) {
		String oldValue = this.checkProcessList;
		this.checkProcessList = checkProcessList;
		changes.firePropertyChange(CONFIG_PROCESS_LIST, oldValue, checkProcessList);
	}

	public int getDeltaRed() {
		return deltaRed;
	}

	public void setDeltaRed(int deltaRed) {
		int oldValue = this.deltaRed;
		this.deltaRed = deltaRed;
		changes.firePropertyChange(CONFIG_RGB_R, oldValue, deltaRed);
	}

	public int getDeltaGreen() {
		return deltaGreen;
	}

	public void setDeltaGreen(int deltaGreen) {
		int oldValue = this.deltaGreen;
		this.deltaGreen = deltaGreen;
		changes.firePropertyChange(CONFIG_RGB_G, oldValue, deltaGreen);
	}

	public int getDeltaBlue() {
		return deltaBlue;
	}

	public void setDeltaBlue(int deltaBlue) {
		int oldValue = this.deltaBlue;
		this.deltaBlue = deltaBlue;
		changes.firePropertyChange(CONFIG_RGB_B, oldValue, deltaBlue);
	}

	public boolean isCheckProcess() {
		return checkProcess;
	}

	public void setCheckProcess(boolean checkProcess) {
		boolean oldValue = this.checkProcess;
		this.checkProcess = checkProcess;
		changes.firePropertyChange(CONFIG_CHECK_PROCESS, oldValue, checkProcess);
	}

	public int getSquareSize() {
		return squareSize;
	}

	public void setSquareSize(int squareSize) {
		int oldValue = this.squareSize;
		this.squareSize = squareSize;
		changes.firePropertyChange(CONFIG_SQUARE_SIZE, oldValue, squareSize);
	}

	public int getAnalysePitch() {
		return analysePitch;
	}

	public void setAnalysePitch(int analysePitch) {
		int oldValue = this.analysePitch;
		this.analysePitch = analysePitch;
		changes.firePropertyChange(CONFIG_ANALYSE_PITCH, oldValue, analysePitch);
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		int oldValue = this.fps;
		this.fps = fps;
		changes.firePropertyChange(CONFIG_FPS, oldValue, fps);
	}

	public int getCheckRatioDelay() {
		return checkRatioDelay;
	}

	public void setCheckRatioDelay(int checkRatioDelay) {
		int oldValue = this.checkRatioDelay;
		this.checkRatioDelay = checkRatioDelay;
		changes.firePropertyChange(CONFIG_DELAY_CHECK_RATIO, oldValue, checkRatioDelay);
	}

	public int getCheckProcessDelay() {
		return checkProcessDelay;
	}

	public void setCheckProcessDelay(int checkProcessDelay) {
		int oldValue = this.checkProcessDelay;
		this.checkProcessDelay = checkProcessDelay;
		changes.firePropertyChange(CONFIG_DELAY_CHECK_PROCESS, oldValue, checkProcessDelay);
	}

	public String getMonitoringFramePosition() {
		return monitoringFramePosition;
	}

	public void setMonitoringFramePosition(String monitoringFramePosition) {
		String oldValue = this.monitoringFramePosition;
		this.monitoringFramePosition = monitoringFramePosition;
		changes.firePropertyChange(CONFIG_MONITORING_XY, oldValue, monitoringFramePosition);
	}

	public SquareAnalyser getSquareAnalyser() {
		return squareAnalyser;
	}

	public void setSquareAnalyser(SquareAnalyser squareAnalyser) {
		SquareAnalyser oldValue = this.squareAnalyser;
		this.squareAnalyser = squareAnalyser;
		changes.firePropertyChange(CONFIG_SQUARE_ANALYSER, oldValue, squareAnalyser);
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		String oldValue = this.updateUrl;
		this.updateUrl = updateUrl;
		changes.firePropertyChange(CONFIG_UPDATE_URL, oldValue, updateUrl);
	}

	public int getSmoothing() {
		return smoothing;
	}

	public void setSmoothing(int smoothing) {
		int oldValue = this.smoothing;
		this.smoothing = smoothing;
		changes.firePropertyChange(CONFIG_SMOOTHING, oldValue, smoothing);
	}

	public boolean isShowFpsFrame() {
		return showFpsFrame;
	}

	public void setShowFpsFrame(boolean showFpsFrame) {
		boolean oldValue = this.showFpsFrame;
		this.showFpsFrame = showFpsFrame;
		changes.firePropertyChange(CONFIG_SHOW_FPS_FRAME, oldValue, showFpsFrame);
	}

	public boolean isBlackOtherScreens() {
		return blackOtherScreens;
	}

	public void setBlackOtherScreens(boolean blackOtherScreens) {
		boolean oldValue = this.blackOtherScreens;
		this.blackOtherScreens = blackOtherScreens;
		changes.firePropertyChange(CONFIG_BLACK_OTHER_SCREENS, oldValue, blackOtherScreens);
	}

	public float getGamma() {
		return gamma;
	}

	public void setGamma(float gamma) {
		float oldValue = this.gamma;
		this.gamma = gamma;
		changes.firePropertyChange(CONFIG_COLOR_GAMMA, oldValue, gamma);
	}

	public float getHue() {
		return hue;
	}

	public void setHue(float hue) {
		float oldValue = this.hue;
		this.hue = hue;
		changes.firePropertyChange(CONFIG_COLOR_HUE, oldValue, hue);
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		float oldValue = this.saturation;
		this.saturation = saturation;
		changes.firePropertyChange(CONFIG_COLOR_SATURATION, oldValue, saturation);
	}

	public float getBrightness() {
		return brightness;
	}

	public void setBrightness(float brightness) {
		float oldValue = this.brightness;
		this.brightness = brightness;
		changes.firePropertyChange(CONFIG_COLOR_BRIGHTNESS, oldValue, brightness);
	}

	public ScreenCapture getScreenCapture() {
		return screenCapture;
	}

	public void setScreenCapture(ScreenCapture screenCapture) {
		ScreenCapture oldValue = this.screenCapture;
		this.screenCapture = screenCapture;
		changes.firePropertyChange(CONFIG_SCREEN_CAPTURE, oldValue, brightness);
	}

    public boolean isAspectRatioDebug()
    {
        return aspectRatioDebug;
    }

    public void setAspectRatioDebug( boolean aspectRatioDebug )
    {
        boolean oldValue = this.aspectRatioDebug;
        this.aspectRatioDebug = aspectRatioDebug;
        changes.firePropertyChange(CONFIG_ASPECT_RATIO_DEBUG, oldValue, aspectRatioDebug);
    }
}
