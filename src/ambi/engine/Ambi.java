package ambi.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;

import ambi.ressources.Factory;

public class Ambi extends Thread {

	private Screen screen;
	private boolean stop = false;
	private boolean running = false;
	private int r, g, b;
	private Integer[][] old;

	public Ambi(int screenDevice, int ledCountLeftRight, int ledCountTop) {
		super();
		screen = new Screen(Factory.getBounds(screenDevice), ledCountLeftRight, ledCountTop);
		r = Factory.getRGB_R();
		g = Factory.getRGB_G();
		b = Factory.getRGB_B();
		old = new Integer[ledCountLeftRight + ledCountLeftRight + ledCountTop - 2][3];
		for (int i = 0; i < ledCountLeftRight + ledCountLeftRight + ledCountTop - 2; i++) {
			old[i][0] = 0;
			old[i][1] = 0;
			old[i][2] = 0;
		}
	}

	public void run() {
		int currentSecond = 0, lastSecondCheck = 0;
		int second = 0;
		int fps = 0;
		int sleep = Factory.getTreahSleep();
		Integer[][] colors;
		try {
			while (!stop) {
				second = Calendar.getInstance().get(Calendar.SECOND);
				if (lastSecondCheck != second && second % 5 == 0) {
					running = !Factory.isCheckProcess() || shouldRun();
					if (running) {
						screen.detectImageFormat();
					} else {
						stopRunning();
					}
					sleep = Factory.getTreahSleep();
					lastSecondCheck = second;
				}
				if (running) {
					if (second != currentSecond) {
						AmbiEngineManager.getAmbiFrame().setInfo(fps + " FPS");
						fps = 1;
						currentSecond = second;
						r = Factory.getRGB_R();
						g = Factory.getRGB_G();
						b = Factory.getRGB_B();
					} else {
						fps++;
					}
					colors = screen.getColors();
					AmbiEngineManager.getArduinoSender().write(getArray(colors));
					AmbiEngineManager.getAmbiFrame().refresh(colors);
					sleep(sleep);
				} else {
					sleep(800);
				}
			}
			stopRunning();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AmbiEngineManager.getArduinoSender().close();
		}
	}

	private void stopRunning() {
		AmbiEngineManager.getAmbiFrame().setInfo("Not running");
		AmbiEngineManager.getArduinoSender().write(getStopArray());
	}

	public byte[] getArray(Integer[][] colors) {
		byte[] result = new byte[6 + colors.length * 3];
		result[0] = 'A';
		result[1] = 'd';
		result[2] = 'a';
		result[3] = (byte) ((colors.length - 1) >> 8);
		result[4] = (byte) ((colors.length - 1) & 0xff);
		result[5] = (byte) (result[3] ^ result[4] ^ 0x55);
		int j = 6;
		for (int i = 0; i < colors.length; i++) {
			result[j++] = (byte) ((Math.min(Math.max((colors[i][0]) + r, 0), 255) + old[i][0]) / 2);
			result[j++] = (byte) ((Math.min(Math.max((colors[i][1]) + g, 0), 255) + old[i][1]) / 2);
			result[j++] = (byte) ((Math.min(Math.max((colors[i][2]) + b, 0), 255) + old[i][2]) / 2);
			old[i] = colors[i];
		}
		return result;
	}

	public byte[] getStopArray() {
		byte[] result = getArray(new Integer[0][0]);
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
