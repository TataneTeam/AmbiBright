package ambibright.ressources;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;
import java.util.Set;

import ambibright.config.Config;
import ambibright.engine.ArduinoSender;
import ambibright.engine.AspectRatioService;
import ambibright.engine.ColorsChangeObserver;
import ambibright.engine.Manager;
import ambibright.engine.ProcessCheckerService;
import ambibright.engine.UpdateColorsService;
import ambibright.engine.color.ColorAlgorithm;
import ambibright.engine.color.ColorAlgorithmGamma;
import ambibright.engine.color.ColorAlgorithmHSB;
import ambibright.ihm.AmbiFont;
import ambibright.ihm.BlackScreenManager;
import ambibright.ihm.ColorFrame;
import ambibright.ihm.MonitoringFrame;
import ambibright.ihm.SimpleFPSFrame;
import ambibright.ihm.Tray;

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
	private final CurrentBounds currentBounds;
	private final Manager manager;
	private final SimpleFPSFrame simpleFPSFrame;
	private final BlackScreenManager blackScreenManager;
	private final ColorAlgorithm[] colorAlgorithms;

	private Factory() {
		this.config = Config.getInstance();

		colorAlgorithms = new ColorAlgorithm[]{new ColorAlgorithmGamma(config), new ColorAlgorithmHSB(config)};

		this.ambiFont = new AmbiFont();

		this.currentBounds = new CurrentBounds(config);

		new Tray(getImageIcon(), ambiFont, config);
		this.ambiFrame = new MonitoringFrame(getConfig().getNbLedLeft(), getConfig().getNbLedTop(), getImageIcon());
		this.arduinoSender = new ArduinoSender(getConfig());
		this.manager = new Manager(config);
		this.simpleFPSFrame = new SimpleFPSFrame(currentBounds);
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

	public MonitoringFrame getAmbiFrame() {
		return ambiFrame;
	}

	public ArduinoSender getArduinoSender() {
		return arduinoSender;
	}

	public AspectRatioService newAspectRatioService() {
		return new AspectRatioService(currentBounds, getConfig());
	}

	public ProcessCheckerService newProcessCheckerService() {
		return new ProcessCheckerService(manager, getConfig());
	}

	public UpdateColorsService newUpdateColorsService(Set<ColorsChangeObserver> observers) {
		return new UpdateColorsService(getConfig(), observers, colorAlgorithms, currentBounds, getArduinoSender().getArray());
	}

	public ColorFrame getNewColorFrame() {
		return new ColorFrame(ambiFont, getConfig(), currentBounds);
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
