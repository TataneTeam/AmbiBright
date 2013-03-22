package ambi.ressources;

import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

	public static String getAppList() {
		return getConfig().get(Parameters.CONFIG_APP_LIST);
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

	public static List<String> getProcessList() {
		List<String> result = new ArrayList<String>();
		try {
			String line;
			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe /FO CSV /NH");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				result.add(line.substring(1, line.indexOf("\",")));
			}
			input.close();
		} catch (Exception err) {
			err.printStackTrace();
		}

		return result;
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
}
