package ambi.engine;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.List;

import ambi.ressources.Factory;

public class Ambi extends Thread {

	private Screen screen;
	private boolean stop = false;

	public Ambi(int screenDevice, int ledCountLeftRight, int ledCountTop) {
		super();
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
		screen = new Screen(bounds, ledCountLeftRight, ledCountTop);
	}

	public void run() {
		int count = 0;
		int currentSecond = -1;
		int fps = 0;
		List<Color> colors;
		try {
			while (!stop) {
				sleep(10);
				if (Calendar.getInstance().get(Calendar.SECOND) != currentSecond) {
					Factory.getAmbiFrame().setFps(fps);
					fps = 0;
					currentSecond = Calendar.getInstance().get(Calendar.SECOND);
				} else {
					fps++;
				}
				colors = screen.getColors();
				Factory.getArduinoSender().write(getArray(colors));
				Factory.getAmbiFrame().refresh(colors);
				count++;
				if (count == 100) {
					screen.init();
					count = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Factory.getArduinoSender().close();
		}
	}

	public static byte[] getArray(List<Color> colors) {
		byte[] result = new byte[6 + colors.size() * 3];
		result[0] = 'A';
		result[1] = 'd';
		result[2] = 'a';
		result[3] = (byte) ((colors.size() - 1) >> 8);
		result[4] = (byte) ((colors.size() - 1) & 0xff);
		result[5] = (byte) (result[3] ^ result[4] ^ 0x55);
		int i = 6;
		for (Color color : colors) {
			result[i++] = (byte) color.getRed();
			result[i++] = (byte) color.getGreen();
			result[i++] = (byte) color.getBlue();
		}
		return result;
	}

}
