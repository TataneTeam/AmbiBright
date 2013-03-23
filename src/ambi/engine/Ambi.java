package ambi.engine;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ambi.ressources.Factory;

public class Ambi extends Thread {

	private Screen screen;
	private boolean stop = false;
	private int totalLED;
	private boolean running = false;

	public Ambi(int screenDevice, int ledCountLeftRight, int ledCountTop) {
		super();
		totalLED = ledCountLeftRight * 2 + ledCountTop - 2;
		screen = new Screen(Factory.getBounds(screenDevice), ledCountLeftRight, ledCountTop);
	}

	public void run() {
		int currentSecond = 0, lastSecondCheck = 0;
		int second = 0;
		int fps = 0;
		List<Color> colors;
		try {
			AmbiEngineManager.getAmbiFrame().setInfo("Not running");
			while (!stop) {
				second = Calendar.getInstance().get(Calendar.SECOND);
				if (lastSecondCheck != second && second % 5 == 0) {
					running = !Factory.isCheckProcess() || shouldRun();
					if(running){
						screen.detectImageFormat();
					}
					lastSecondCheck = second;
				}
				if (running) {
					if (second != currentSecond) {
						AmbiEngineManager.getAmbiFrame().setInfo(fps + " FPS");
						fps = 1;
						currentSecond = second;
					} else {
						fps++;
					}
					colors = screen.getColors();
					AmbiEngineManager.getArduinoSender().write(getArray(colors));
					AmbiEngineManager.getAmbiFrame().refresh(colors);

					sleep(10);
				} else {
					AmbiEngineManager.getArduinoSender().write(getStopArray());
					AmbiEngineManager.getAmbiFrame().setInfo("Not running");
					sleep(800);
				}
			
			}
			AmbiEngineManager.getArduinoSender().write(getStopArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AmbiEngineManager.getArduinoSender().close();
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

	public boolean shouldRun() {
		boolean result = false;
		String apps = Factory.getProcessList();
		BufferedReader input = null;
		try {
			String line;
			Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe /FO CSV /NH");
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				line = line.substring(1, line.indexOf("\","));
				if (apps.contains(line)) {
					result = true;
					break;
				}
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
