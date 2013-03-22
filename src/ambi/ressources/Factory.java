package ambi.ressources;

import java.awt.Image;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import ambi.engine.Ambi;
import ambi.engine.ArduinoSender;
import ambi.ihm.AmbiFrame;
import ambi.ihm.Tray;
import ambi.ressources.Config.Parameters;

public class Factory {

	public static String appName = "Ambi";

	public static String imageIconPath = "icon.png";

	public static String configFileName = "AmbiConfig";

	private static Robot robot;

	private static Config config;

	private static AmbiFrame ambiFrame;

	private static Ambi ambi;

	private static ArduinoSender arduinoSender;
	
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

	public static AmbiFrame getAmbiFrame() {
		if (ambiFrame == null) {
			ambiFrame = new AmbiFrame(getLedNBLeft(), getLedNBTop());
		}
		return ambiFrame;
	}

	public static Ambi getAmbi() {
		if (ambi == null) {
			ambi = new Ambi(getScreenDevice(), getLedNBLeft(), getLedNBTop());
		}
		return ambi;
	}

	public static ArduinoSender getArduinoSender() {
		if (arduinoSender == null) {
			arduinoSender = new ArduinoSender(getArduinoSerial());
		}
		return arduinoSender;
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
	
	public static Tray getTray(){
		if(tray == null){
			tray = new Tray();
		}
		return tray;
	}

}
