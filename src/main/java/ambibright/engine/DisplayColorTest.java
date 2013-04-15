package ambibright.engine;

import ambibright.ressources.Factory;

public class DisplayColorTest {

	private byte[][] colors;
	private byte[] sent;
	private int currentPos = 0;
	private int lenght;
	private ArduinoSender arduinoSender;

	public DisplayColorTest(int lenght, ArduinoSender arduinoSender) {
		this.lenght = lenght;
		this.arduinoSender = arduinoSender;
		float frequency = lenght / 200f;
		colors = new byte[lenght][3];
		for (int i = 0; i < lenght; ++i) {
			colors[i][0] = (byte) (Math.sin(frequency * i + 0) * 127 + 128);
			colors[i][1] = (byte) (Math.sin(frequency * i + 2) * 127 + 128);
			colors[i][2] = (byte) (Math.sin(frequency * i + 4) * 127 + 128);
		}
		sent = arduinoSender.getArray();
	}

	public void next() {
		int j = 6;
		int p = currentPos++;
		for (int i = 0; i < colors.length; i++) {
			p = (i + currentPos) % lenght;
			sent[j++] = colors[p][0];
			sent[j++] = colors[p][1];
			sent[j++] = colors[p][2];
		}
		arduinoSender.write(sent);
	}

	public static void main(String[] args) throws Exception {

		ArduinoSender arduinoSender = Factory.get().getArduinoSender();
		arduinoSender.open();

		DisplayColorTest dt = new DisplayColorTest(Factory.get().getConfig().getLedTotalNumber(), arduinoSender);
		while (true) {
			try {
				Thread.sleep(20);
				dt.next();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
