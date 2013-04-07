package ambibright.engine;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import ambibright.engine.colorAnalyser.SquareAnalyser;
import ambibright.ihm.MonitoringFrame;
import ambibright.ressources.CurrentBounds;
import ambibright.ressources.Factory;

/**
 * Created with IntelliJ IDEA. User: Nico Date: 23/03/13 Time: 22:00 To change
 * this template use File | Settings | File Templates.
 */
public class UpdateColorsService implements Runnable {

	private final Robot robot;
	// on pourrait changer ce comportement pour mettre une liste d'observer qui
	// consomment les couleurs pour totalement découpler le traitement
	private final ArduinoSender arduino;
	private final MonitoringFrame monitoringFrame;
	private final int[][] result;
	private final CurrentBounds currentBounds;
	private final SquareAnalyser colorAnalyser;
	private int deltaR, deltaG, deltaB, smoothing;
	private int pos;
	private BufferedImage image;
	private final int[][] old;
	private int currentSecond = 0;
	private int second = 0;
	private int fps = 0;
	private int screenAnalysePitch;

	public UpdateColorsService(Robot robot, ArduinoSender arduino, MonitoringFrame monitoringFrame, CurrentBounds currentBounds, SquareAnalyser colorAnalyser, int screenAnalysePitch, int nbLed, int red, int green, int blue, int smoothing) {
		this.robot = robot;
		this.arduino = arduino;
		this.monitoringFrame = monitoringFrame;
		this.currentBounds = currentBounds;
		this.colorAnalyser = colorAnalyser;
		this.screenAnalysePitch = screenAnalysePitch;
		this.deltaR = red;
		this.deltaG = green;
		this.deltaB = blue;
		this.old = new int[nbLed][3];
		this.result = new int[nbLed][3];
		this.smoothing = smoothing;
	}

	public void run() {
		try {
			second = Calendar.getInstance().get(Calendar.SECOND);
			if (second != currentSecond) {
				monitoringFrame.setInfo(fps + " FPS");
				Factory.get().getSimpleFPSFrame().setValue(fps + " FPS");
				fps = 1;
				currentSecond = second;
			} else {
				fps++;
			}
			byte[] colors = getColorsToSend(getColors());
			arduino.write(colors);
			monitoringFrame.refresh(colors);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// L > T > R
	private int[][] getColors() {
		pos = 0;
		image = robot.createScreenCapture(currentBounds.getBounds());

		// Compute for all screen parts
		for (Rectangle bound : currentBounds.getZones()) {
			result[pos++] = colorAnalyser.getColor(image, bound, screenAnalysePitch);
		}

		// Flushing the image
		image.flush();
		image = null;

		return result;
	}

	private byte[] getColorsToSend(int[][] colors) {
		byte[] result = arduino.getArray();
		int j = 6;
		for (int i = 0; i < colors.length; i++) {
			old[i][0] = ((colors[i][0] * smoothing) + old[i][0]) / (1 + smoothing);
			old[i][1] = ((colors[i][1] * smoothing) + old[i][1]) / (1 + smoothing);
			old[i][2] = ((colors[i][2] * smoothing) + old[i][2]) / (1 + smoothing);
			result[j++] = (byte) (Math.min(Math.max((old[i][0]) + deltaR, 0), 255));
			result[j++] = (byte) (Math.min(Math.max((old[i][1]) + deltaG, 0), 255));
			result[j++] = (byte) (Math.min(Math.max((old[i][2]) + deltaB, 0), 255));
		}
		return result;
	}

	public void setDeltaRGB(int red, int green, int blue) {
		this.deltaR = red;
		this.deltaG = green;
		this.deltaB = blue;
	}

}
