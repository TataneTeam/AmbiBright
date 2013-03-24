package ambi.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import ambi.ihm.MonitoringFrame;
import ambi.ressources.CurrentBounds;

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
	private final Integer[][] result;
	private final CurrentBounds currentBounds;

	private final int ledNumberLeftRight;
	private final int ledNumberTop;
	private final int squareSize;
	private final int screenAnalysePitch;
	private final int r;
	private final int g;
	private final int b;

	private int current, red, green, blue, nbPixel, x, y, posX, posY, pos;
	private BufferedImage image;
	private int squareSizeLeftRight, squareSizeTop;
	private Integer[][] old;
	private int currentSecond = 0;
	private int second = 0;
	private int fps = 0;

	public UpdateColorsService(Robot robot, ArduinoSender arduino, MonitoringFrame monitoringFrame, CurrentBounds currentBounds, int ledNumberLeftRight, int ledNumberTop, int squareSize, int screenAnalysePitch, int red, int green, int blue) {
		this.robot = robot;
		this.arduino = arduino;
		this.monitoringFrame = monitoringFrame;
		this.currentBounds = currentBounds;
		this.ledNumberLeftRight = ledNumberLeftRight;
		this.ledNumberTop = ledNumberTop;

		this.squareSize = squareSize;
		this.screenAnalysePitch = screenAnalysePitch;

		this.r = red;
		this.g = green;
		this.b = blue;

		old = new Integer[ledNumberLeftRight + ledNumberLeftRight + ledNumberTop - 2][3];
		for (int i = 0; i < ledNumberLeftRight + ledNumberLeftRight + ledNumberTop - 2; i++) {
			old[i][0] = 0;
			old[i][1] = 0;
			old[i][2] = 0;
		}

		this.result = new Integer[2 * ledNumberLeftRight + ledNumberTop - 2][3];
	}

	public void run() {
		try {
			second = Calendar.getInstance().get(Calendar.SECOND);
			if (second != currentSecond) {
				monitoringFrame.setInfo(fps + " FPS");
				fps = 1;
				currentSecond = second;
			} else {
				fps++;
			}

			Integer[][] colors = getColors();
			arduino.write(getColorsToSend(colors));
			monitoringFrame.refresh(colors);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// L > T > R
	private Integer[][] getColors() {
		Rectangle bounds = currentBounds.getBounds();
		squareSizeLeftRight = bounds.height / ledNumberLeftRight;
		squareSizeTop = bounds.width / ledNumberTop;

		pos = 0;
		image = robot.createScreenCapture(bounds);

		// Left from bottom to top
		for (y = bounds.height - squareSizeLeftRight; y >= 0; y -= squareSizeLeftRight) {
			getColor(pos++, 0, y, squareSize, squareSizeLeftRight);
		}

		// Top from left to right
		for (x = squareSizeTop - 1; x + squareSizeTop < bounds.width; x += squareSizeTop) {
			getColor(pos++, x, 0, squareSizeTop, squareSize);
		}

		// Right from top to bottom
		for (y = squareSizeLeftRight - 1; y + squareSizeLeftRight < bounds.height; y += squareSizeLeftRight) {
			getColor(pos++, bounds.width - squareSizeLeftRight, y, squareSize, squareSizeLeftRight);
		}

		// Flushing the image
		image.flush();
		image = null;

		return result;
	}

	private void getColor(int ledNumber, int x, int y, int width, int height) {
		current = 0;
		red = 0;
		green = 0;
		blue = 0;
		nbPixel = 0;
		for (posX = 0; posX < width; posX += screenAnalysePitch) {
			for (posY = 0; posY < height; posY += screenAnalysePitch) {
				current = image.getRGB(x + posX, y + posY);
				red += (current & 0x00ff0000) >> 16;
				green += (current & 0x0000ff00) >> 8;
				blue += current & 0x000000ff;
				nbPixel++;
			}
		}
		result[ledNumber][0] = red / nbPixel;
		result[ledNumber][1] = green / nbPixel;
		result[ledNumber][2] = blue / nbPixel;
	}

	private byte[] getColorsToSend(Integer[][] colors) {
		byte[] result = arduino.getArray();

		int j = 6;
		for (int i = 0; i < colors.length; i++) {
			result[j++] = (byte) ((Math.min(Math.max((colors[i][0]) + r, 0), 255) + old[i][0]) / 2);
			result[j++] = (byte) ((Math.min(Math.max((colors[i][1]) + g, 0), 255) + old[i][1]) / 2);
			result[j++] = (byte) ((Math.min(Math.max((colors[i][2]) + b, 0), 255) + old[i][2]) / 2);
			old[i] = colors[i];
		}
		return result;
	}

}
