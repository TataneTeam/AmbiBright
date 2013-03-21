import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Screen {

	private Robot robot;

	private Rectangle bounds;

	private int ledNumberLeftRight, ledNumberTop;

	private int squareSizeLeftRight, squareSizeTop;

	private boolean auto;

	public static final int screenAnalysePitch = 1;

	private BufferedImage image;

	public Screen(Rectangle bounds, int ledNumberLeftRight, int ledNumberTop, boolean auto) {
		super();
		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.bounds = bounds;
		this.ledNumberLeftRight = ledNumberLeftRight;
		this.ledNumberTop = ledNumberTop;
		this.auto = auto;
		squareSizeLeftRight = (int) (bounds.getHeight() / ledNumberLeftRight);
		squareSizeTop = (int) (bounds.getWidth() / ledNumberTop);
	}

	// L > T > R
	public List<Color> getColors() {
		List<Color> result = new ArrayList<Color>();
		image = robot.createScreenCapture(bounds);

		// Left from bottom to top
		for (int y = image.getHeight() - squareSizeLeftRight; y > 0; y -= squareSizeLeftRight) {
			result.add(getColor(0, y, squareSizeLeftRight, squareSizeLeftRight));
		}

		// Top from left to right
		for (int x = squareSizeTop; x < image.getWidth(); x += squareSizeTop) {
			result.add(getColor(x, 0, squareSizeTop, squareSizeTop));
		}

		// Right from top to bottom
		for (int y = squareSizeLeftRight; (y + squareSizeLeftRight) < image.getHeight(); y += squareSizeLeftRight) {
			result.add(getColor(image.getWidth() - squareSizeLeftRight, y, squareSizeLeftRight, squareSizeLeftRight));
		}

		return result;
	}

	private Color getColor(int x, int y, int width, int height) {
		int current = 0;
		int red = 0;
		int green = 0;
		int blue = 0;
		int nbPixel = 0;
		for (int posX = 0; posX < width; posX += screenAnalysePitch) {
			for (int posY = 0; posY < height; posY += screenAnalysePitch) {
				try {
					current = image.getRGB(x + posX, y + posY);
					red += (current & 0x00ff0000) >> 16;
					green += (current & 0x0000ff00) >> 8;
					blue += current & 0x000000ff;
					nbPixel++;
				} catch (Exception e) {
					System.out.println(image.getWidth() + " " + image.getHeight() + " " + (x + posX) + " " + (y + posY));
				}
			}
		}
		return new Color(red / nbPixel, green / nbPixel, blue / nbPixel);
	}

	public byte[] getArray() {
		List<Color> colors = getColors();
		byte[] result = new byte[6 + colors.size() * 3];
		result[0] = 'A';
		result[1] = 'd';
		result[2] = 'a';
		result[3] = (byte) ((colors.size() ) >> 8);
		result[4] = (byte) ((colors.size() ) & 0xff);
		result[5] = (byte) (result[3] ^ result[4] ^ 0x55);
		int i = 6;
		for (Color color : colors) {
			result[i++] = (byte) color.getRed() ;
			result[i++] = (byte) color.getGreen() ;
			result[i++] = (byte) color.getBlue() ;
		}
		return result;
	}

}
