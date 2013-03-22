import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.List;

public class Ambi extends Thread {

	private int ledCountLeftRight = 14;
	private int ledCountTop = 24;
	private String arduinoConf = "COM5";
	private Screen screen;

	public Ambi(int screenDevice, int ledCountLeftRight, int ledCountTop, String arduinoConf) {
		super();
		this.ledCountLeftRight = ledCountLeftRight;
		this.ledCountTop = ledCountTop;
		this.arduinoConf = arduinoConf;
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[screenDevice].getDefaultConfiguration().getBounds();
		screen = new Screen(bounds, ledCountLeftRight, ledCountTop);
	}

	public void run() {
		ArduinoSender arduino = new ArduinoSender(arduinoConf);
		AmbiFrame af = new AmbiFrame(ledCountLeftRight, ledCountTop);
		int count = 0;
		int currentSecond = -1;
		int nbIter = 0;
		List<Color> colors;
		try {
			while (true) {
				sleep(10);
				if(Calendar.getInstance().get(Calendar.SECOND) != currentSecond){
					System.out.println(nbIter + " FPS");
					nbIter = 0;
					currentSecond = Calendar.getInstance().get(Calendar.SECOND);
				}else{
					nbIter++;
				}
				colors = screen.getColors();
				arduino.write(getArray(colors));
				af.refresh(colors);
				count++;
				if (count == 100) {
					screen.init();
					count = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			arduino.close();
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

	public static void main(String[] args) {
		new Ambi(1, 14, 24, "COM5").start();
	}

}
