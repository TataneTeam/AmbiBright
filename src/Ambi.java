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
				//arduino.write(screen.getArray());
				List<Color> colors = screen.getColors();
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
			//arduino.close();
		}
	}

	public static void main(String[] args) {
		new Ambi().start();
	}

}
