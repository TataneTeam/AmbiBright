package ambibright.ressources;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.net.URL;

import javax.swing.ImageIcon;

import ambibright.engine.ArduinoSender;
import ambibright.engine.AspectRatioService;
import ambibright.engine.Manager;
import ambibright.engine.ProcessCheckerService;
import ambibright.engine.UpdateColorsService;
import ambibright.ihm.AmbiFont;
import ambibright.ihm.ColorFrame;
import ambibright.ihm.MonitoringFrame;
import ambibright.ihm.Tray;
import ambibright.ressources.Config.Parameters;

public class Factory {
	public static final String appName = "AmbiBright";
	public static final String imageIconPath = "icon.png";
	public static final String configFileName = "AmbiBright.properties";
	private static final Factory instance = new Factory();

	public static Factory get() {
		return instance;
	}

	private final Robot robot;
	private final Config config;
	private final AmbiFont ambiFont;
	private final Tray tray;
	private final MonitoringFrame ambiFrame;
	private final ArduinoSender arduinoSender;
	private final Rectangle fullScreenBounds;
	private final CurrentBounds currentBounds;
	private final Manager manager;
	private UpdateColorsService updateColorsService;

	private Factory() {
		this.config = new Config(configFileName);
		this.config.load();

		try {
			this.robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		this.ambiFont = new AmbiFont();

		this.fullScreenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[getScreenDevice()].getDefaultConfiguration().getBounds();
		this.currentBounds = new CurrentBounds(fullScreenBounds,getLedNBLeft(),getLedNBTop(), getSquareSize());

		this.tray = new Tray(getImageIcon(), ambiFont, config);
		this.ambiFrame = new MonitoringFrame(getLedNBLeft(), getLedNBTop(), getImageIcon());
		this.arduinoSender = new ArduinoSender(getArduinoSerial(), getArduinoDataRate(), getLedNBLeft(), getLedNBTop());
		this.manager = new Manager();
	}

	public Manager getManager() {
		return manager;
	}

	public Image getImageIcon() {
		return createImage(imageIconPath);
	}

	private Image createImage(String path) {
		Image result = null;
		try {
			URL imageURL = Factory.class.getResource(path);
			result = new ImageIcon(imageURL).getImage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Config getConfig() {
		return config;
	}

	public String getArduinoSerial() {
		return getConfig().get(Parameters.CONFIG_ARDUINO_PORT);
	}

	public Integer getArduinoDataRate() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_ARDUINO_DATA_RATE));
	}

	public String getProcessList() {
		return getConfig().get(Parameters.CONFIG_PROCESS_LIST);
	}

	public Integer getScreenDevice() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_SCREEN_DEVICE));
	}

	public Integer getLedNBLeft() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_LED_NB_LEFT));
	}

	public Integer getLedNBTop() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_LED_NB_TOP));
	}

	public Integer getAnalysePitch() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_ANALYSE_PITCH));
	}

	public Integer getRGB_R() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_RGB_R));
	}

	public Integer getRGB_G() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_RGB_G));
	}

	public Integer getRGB_B() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_RGB_B));
	}

	public Integer getSquareSize() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_SQUARE_SIZE));
	}

	public Integer getFpsWanted() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_FPS));
	}

	public Integer getDelayCheckRatio() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_DELAY_CHECK_RATIO));
	}

	public Integer getDelayCheckProcess() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_DELAY_CHECK_PROCESS));
	}

	public Rectangle getBounds() {
		return getBounds(getScreenDevice());
	}

	private Rectangle getBounds(int screenDevice) {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
	}

	public boolean isCheckProcess() {
		return "true".equals(getConfig().get(Parameters.CONFIG_CHECK_PROCESS).toLowerCase());
	}

	public MonitoringFrame getAmbiFrame() {
		return ambiFrame;
	}

	public ArduinoSender getArduinoSender() {
		return arduinoSender;
	}

	public AspectRatioService newAspectRatioService() {
		return new AspectRatioService(getBounds(), currentBounds, robot);
	}

	public ProcessCheckerService newProcessCheckerService() {
		return new ProcessCheckerService(manager, getProcessList());
	}

	public int getLedTotalNumber() {
		return 2 * getLedNBLeft() + getLedNBTop() - 2;
	}

	public UpdateColorsService newUpdateColorsService() {
		updateColorsService = new UpdateColorsService(robot, arduinoSender, ambiFrame, currentBounds, getLedTotalNumber(), getAnalysePitch(), getRGB_R(), getRGB_G(), getRGB_B());
		return updateColorsService;
	}

	public UpdateColorsService getUpdateColorsService() {
		return updateColorsService;
	}

	public Tray getTray() {
		return tray;
	}

	public ColorFrame getNewColorFrame() {
		return new ColorFrame(Factory.get().getBounds(), ambiFont);
	}

	public CurrentBounds getCurrentBounds() {
		return currentBounds;
	}

}
