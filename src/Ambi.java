import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

public class Ambi extends Thread {

	public void run() {
		//ArduinoSender arduino = new ArduinoSender("COM5");
		Rectangle bounds = new Rectangle(0, 0, 1920, 1080);
		Screen screen = new Screen(bounds, 14, 24, false);
		AmbiFrame af = new AmbiFrame(14,24);
		try {
			while (true) {
			
				List<Color> colors = screen.getColors();
				//arduino.write(getArray(colors));
				for(int i = 0; i < 14; i++){
					af.setColor(13 -i, 0, colors.get(i));
				}
				for(int i = 1; i < 24; i++){
					af.setColor(0, i, colors.get(i + 13));
				}
				for(int i = 1; i < 14; i++){
					af.setColor(i, 23, colors.get(i + 36));
				}
				sleep(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		//	arduino.close();
		}
	}

	public byte[] getArray(List<Color> colors) {
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
	

	public static void main(String[] args) {
		new Ambi().start();
	}
}
