package ambi.engine;

import ambi.ihm.AmbiFrame;
import ambi.ressources.Factory;

public class AmbiEngineManagement {

	private static AmbiFrame ambiFrame;

	private static Ambi ambi;

	private static ArduinoSender arduinoSender;

	public static void start() {
		getAmbi().start();
	}

	public static void stop() {
		getAmbi().stopProcessing();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getAmbiFrame().dispose();
		getArduinoSender().close();
	}

	public static void restart() {
		if (!getAmbi().isStop()) {
			stop();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		reset();
		start();
	}

	private static void reset() {
		ambi = null;
		ambiFrame = null;
		arduinoSender = null;
	}

	public static AmbiFrame getAmbiFrame() {
		if (ambiFrame == null) {
			ambiFrame = new AmbiFrame(Factory.getLedNBLeft(), Factory.getLedNBTop());
		}
		return ambiFrame;
	}

	public static Ambi getAmbi() {
		if (ambi == null) {
			ambi = new Ambi(Factory.getScreenDevice(), Factory.getLedNBLeft(), Factory.getLedNBTop());
		}
		return ambi;
	}

	public static ArduinoSender getArduinoSender() {
		if (arduinoSender == null) {
			arduinoSender = new ArduinoSender(Factory.getArduinoSerial());
		}
		return arduinoSender;
	}

}
