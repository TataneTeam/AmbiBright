package ambi.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ambi.ressources.Factory;

public class Ambi extends Thread {

	private Screen screen;
	private boolean stop = false;
	private int totalLED;

	public Ambi(int screenDevice, int ledCountLeftRight, int ledCountTop) {
		super();
		totalLED = ledCountLeftRight * 2 + ledCountTop - 2;
		screen = new Screen(Factory.getBounds(screenDevice), ledCountLeftRight, ledCountTop);
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
					AmbiEngineManagement.getAmbiFrame().setFps(fps);
					fps = 0;
					currentSecond = Calendar.getInstance().get(Calendar.SECOND);
				} else {
					fps++;
				}
				colors = screen.getColors();
				AmbiEngineManagement.getArduinoSender().write(getArray(colors));
				AmbiEngineManagement.getAmbiFrame().refresh(colors);
				count++;
				if (count == 100) {
					screen.init();
					count = 0;
				}
			}
			AmbiEngineManagement.getArduinoSender().write(getStopArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AmbiEngineManagement.getArduinoSender().close();
		}
	}

	public byte[] getArray(List<Color> colors) {
		byte[] result = new byte[6 + totalLED * 3];
		result[0] = 'A';
		result[1] = 'd';
		result[2] = 'a';
		result[3] = (byte) ((totalLED - 1) >> 8);
		result[4] = (byte) ((totalLED - 1) & 0xff);
		result[5] = (byte) (result[3] ^ result[4] ^ 0x55);
		int i = 6;
		int r = Factory.getRGB_R();
		int g = Factory.getRGB_G();
		int b = Factory.getRGB_B();
		for (Color color : colors) {
			result[i++] = (byte) Math.min(Math.max(color.getRed() + r, 0),255);
			result[i++] = (byte) Math.min(Math.max(color.getGreen() + g, 0),255);
			result[i++] = (byte) Math.min(Math.max(color.getBlue() + b, 0),255);
		}
		return result;
	}

	public byte[] getStopArray() {
		byte[] result = getArray(new ArrayList<Color>());
		Arrays.fill(result, 6, result.length, (byte) 0);
		return result;
	}

	public void stopProcessing() {
		stop = true;
	}

	public boolean isStop() {
		return stop;
	}

}
