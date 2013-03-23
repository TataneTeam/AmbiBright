package ambi.ressources;

import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.Robot;
import java.net.URL;

import javax.swing.ImageIcon;

import ambi.ihm.Tray;
import ambi.ressources.Config.Parameters;

public class Factory {

	public static String appName = "Ambi";

	public static String imageIconPath = "icon.png";

	public static String configFileName = "AmbiConfig";

	public static String fontName = "Calibri";

	public static Font font = new Font(fontName, Font.PLAIN, 11);

	public static Font fontBold = new Font(fontName, Font.BOLD, 11);

	private static Robot robot;

	private static Config config;

	private static Tray tray;

	public static Image getImageIcon() {
		return createImage(imageIconPath);
	}

	public static Image createImage(String path) {
		Image result = null;
		try {
			URL imageURL = Factory.class.getResource(path);
			result = new ImageIcon(imageURL).getImage();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Robot getRobot() {
		if (robot == null) {
			try {
				robot = new Robot();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return robot;
	}

	public static Config getConfig() {
		if (config == null) {
			config = new Config(configFileName);
			config.load();
		}
		return config;
	}

	public static String getArduinoSerial() {
		return getConfig().get(Parameters.CONFIG_ARDUINO_PORT);
	}
	
	public static Integer getArduinoDataRate() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_ARDUINO_DATA_RATE));
	}

	public static String getProcessList() {
		return getConfig().get(Parameters.CONFIG_PROCESS_LIST);
	}

	public static Integer getScreenDevice() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_SCREEN_DEVICE));
	}

	public static Integer getLedNBLeft() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_LED_NB_LEFT));
	}

	public static Integer getLedNBTop() {
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_LED_NB_TOP));
	}
	
	public static Integer getRGB_R(){
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_RGB_R));
	}
	
	public static Integer getRGB_G(){
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_RGB_G));
	}
	
	public static Integer getRGB_B(){
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_RGB_B));
	}
	
	public static Integer getSquareSize(){
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_SQUARE_SIZE));
	}

	public static Integer getTreahSleep(){
		return Integer.valueOf(getConfig().get(Parameters.CONFIG_THREAD_SLEEP));
	}

	public static Tray getTray() {
		if (tray == null) {
			tray = new Tray();
		}
		return tray;
	}

	public static Component setFont(Component component) {
		component.setFont(font);
		return component;
	}

	public static Component setFontBold(Component component) {
		component.setFont(fontBold);
		return component;
	}

	public static MenuItem setFont(MenuItem component) {
		component.setFont(font);
		return component;
	}
	
	public static Rectangle getBounds(int screenDevice){
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
	}

	public static Rectangle getBounds() {
		return getBounds(getScreenDevice());
	}
	
	public static boolean isCheckProcess(){
		return "true".equals(Factory.getConfig().get(Parameters.CONFIG_CHECK_PROCESS).toLowerCase());
	}
}
