package ambi.engine;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ambi.ressources.Factory;

public class Ambi extends Thread {

	private Screen screen;
	private boolean stop = false;
	private int totalLED, fps = 0;
	private boolean running = false;
	private Timer timer;
	private int timerPeriod = 5000;

	public Ambi(int screenDevice, int ledCountLeftRight, int ledCountTop) {
		super();
		totalLED = ledCountLeftRight * 2 + ledCountTop - 2;
		screen = new Screen(Factory.getBounds(screenDevice), ledCountLeftRight, ledCountTop);
		timer = new Timer();
	}

	public void run() {
		List<Color> colors;
		try {
			timer.schedule(new CheckProcess(), 0, timerPeriod);
			AmbiEngineManagement.getAmbiFrame().setInfo("Not running");
			while (!stop) {
				if (running) {
					fps++;
					colors = screen.getColors();
					AmbiEngineManagement.getArduinoSender().write(getArray(colors));
					AmbiEngineManagement.getAmbiFrame().refresh(colors);
				} else {
					AmbiEngineManagement.getArduinoSender().write(getStopArray());
					AmbiEngineManagement.getAmbiFrame().setInfo("Not running");
					sleep(800);
				}
			}
			AmbiEngineManagement.getArduinoSender().write(getStopArray());
			timer.cancel();
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
			result[i++] = (byte) Math.min(Math.max(color.getRed() + r, 0), 255);
			result[i++] = (byte) Math.min(Math.max(color.getGreen() + g, 0), 255);
			result[i++] = (byte) Math.min(Math.max(color.getBlue() + b, 0), 255);
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

	class CheckProcess extends TimerTask {
		public void run() {

			// Display FPS
			if (running) {
				AmbiEngineManagement.getAmbiFrame().setInfo(fps / (timerPeriod / 1000) + " FPS");
				fps = 0;
			}

			// Check image format
			screen.getImageFormat();

			// Check Process
			String apps = Factory.getProcessList();
			BufferedReader input = null;
			try {
				String line;
				Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe /FO CSV /NH");
				input = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = input.readLine()) != null) {
					line = line.substring(1, line.indexOf("\","));
					if (apps.contains(line)) {
						running = true;
						return;
					}
				}
				running = false;
			} catch (Exception err) {
				err.printStackTrace();
			} finally {
				try {
					input.close();
				} catch (Exception e) {
				}
			}

		}
	}

}
