package ambibright.ressources;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import ambibright.ihm.Tray;
import ambibright.ihm.SimpleFPSFrame;
import ambibright.ihm.MonitoringFrame;
import ambibright.ihm.ColorFrame;
import ambibright.ihm.BlackScreenManager;
import ambibright.ihm.AmbiFont;
import ambibright.engine.color.ColorAlgorithmHSB;
import ambibright.engine.color.ColorAlgorithmGamma;
import ambibright.engine.color.ColorAlgorithm;
import ambibright.engine.capture.ScreenCapture;
import ambibright.engine.capture.DefaultScreenCapture;
import ambibright.engine.UpdateColorsService;
import ambibright.engine.ProcessCheckerService;
import ambibright.engine.Manager;
import ambibright.engine.ColorsChangeObserver;
import ambibright.engine.AspectRatioService;
import ambibright.engine.ArduinoSender;
import ambibright.config.Config;

public class Factory {
	public static final String appName = "AmbiBright";
	public static final String imageIconPath = "icon.png";
	private static final Factory instance = new Factory();
	private static final String separator = " ";

	public static Factory get() {
		return instance;
	}

	private final Config config;
	private final AmbiFont ambiFont;
	private final MonitoringFrame ambiFrame;
	private final ArduinoSender arduinoSender;
	private final Rectangle fullScreenBounds;
	private final CurrentBounds currentBounds;
	private final Manager manager;
	private final SimpleFPSFrame simpleFPSFrame;
	private final BlackScreenManager blackScreenManager;
	private final List<? extends ColorAlgorithm> colorAlgorithmList;

	private Factory() {
		this.config = Config.getInstance();

		colorAlgorithmList = Arrays.asList(new ColorAlgorithmGamma(config), new ColorAlgorithmHSB(config));

		this.ambiFont = new AmbiFont();

		this.fullScreenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[getConfig().getScreenDevice()].getDefaultConfiguration().getBounds();
		this.currentBounds = new CurrentBounds(fullScreenBounds, getConfig().getNbLedLeft(), config.getNbLedTop(), config.getSquareSize());

		new Tray(getImageIcon(), ambiFont, config);
		this.ambiFrame = new MonitoringFrame(getConfig().getNbLedLeft(), getConfig().getNbLedTop(), getImageIcon());
		this.arduinoSender = new ArduinoSender(getConfig());
		this.manager = new Manager(config);
		this.simpleFPSFrame = new SimpleFPSFrame();
		this.blackScreenManager = new BlackScreenManager();
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

	public Rectangle getBounds() {
		return getBounds(getConfig().getScreenDevice());
	}

	private Rectangle getBounds(int screenDevice) {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
	}

	public ScreenCapture getScreenCapture() {
		return DefaultScreenCapture.getInstance();
	}

	public MonitoringFrame getAmbiFrame() {
		return ambiFrame;
	}

	public ArduinoSender getArduinoSender() {
		return arduinoSender;
	}

	public AspectRatioService newAspectRatioService() {
		return new AspectRatioService(getBounds(), currentBounds, getScreenCapture());
	}

	public ProcessCheckerService newProcessCheckerService() {
		return new ProcessCheckerService(manager, getConfig());
	}

	public UpdateColorsService newUpdateColorsService(Set<ColorsChangeObserver> observers) {
		return new UpdateColorsService(getConfig(), getScreenCapture(), observers, colorAlgorithmList, currentBounds, getArduinoSender().getArray());
	}

	public ColorFrame getNewColorFrame() {
		return new ColorFrame(Factory.get().getBounds(), ambiFont, getConfig());
	}

	public CurrentBounds getCurrentBounds() {
		return currentBounds;
	}

	public Point getMonitoringLocation() {
		String[] values = getConfig().getMonitoringFramePosition().split(separator);
		return new Point(Integer.valueOf(values[0]), Integer.valueOf(values[1]));
	}

	public void saveMonitoringLocation(int x, int y) {
		getConfig().setMonitoringFramePosition(x + separator + y);
		getConfig().save();
	}

	public SimpleFPSFrame getSimpleFPSFrame() {
		return simpleFPSFrame;
	}

	public BlackScreenManager getBlackScreenManager() {
		return blackScreenManager;
	}

}
