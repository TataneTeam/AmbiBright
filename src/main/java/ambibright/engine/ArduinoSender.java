package ambibright.engine;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;

public class ArduinoSender implements ColorsChangeObserver{

	public static final String defaultTestString = "Ada";

	@SuppressWarnings("rawtypes")
	public static String getArduinoPort(int dataRate, String testString) {
		String result = null;
		CommPortIdentifier portId;
		SerialPort serialPort = null;
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			try {
				portId = (CommPortIdentifier) portList.nextElement();
				serialPort = portId.open("Arduino", 500);
				serialPort.setSerialPortParams(dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.enableReceiveThreshold(1);
				BufferedReader buffer = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
				if (testString.equals(buffer.readLine())) {
					result = serialPort.getName().substring(serialPort.getName().lastIndexOf("/") + 1);
					break;
				}
			} catch (Exception e) {
			} finally {
				try {
					serialPort.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	private final byte[] array;
	private SerialPort serialPort;
	private OutputStream output;

	public ArduinoSender(int ledNumberLeftRight, int ledNumberTop) {
		int totalLeds = ledNumberLeftRight * 2 + ledNumberTop - 2;
		array = new byte[6 + totalLeds * 3];
		array[0] = 'A';
		array[1] = 'd';
		array[2] = 'a';
		array[3] = (byte) ((totalLeds - 1) >> 8);
		array[4] = (byte) ((totalLeds - 1) & 0xff);
		array[5] = (byte) (array[3] ^ array[4] ^ 0x55);
	}

	public void open(String port, int dataRate) throws Exception {
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
		serialPort = portId.open(this.getClass().getName(), 500);
		serialPort.setSerialPortParams(dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		output = serialPort.getOutputStream();
	}

	public void close() {
		if (serialPort != null) {
			byte[] stopTrame = getArray();
			Arrays.fill(stopTrame, 6, stopTrame.length, (byte) 0);
			write(stopTrame);
			serialPort.close();
			serialPort = null;
			output = null;
		}
	}

	public byte[] getArray() {
		return array;
	}

	public void write(byte[] array) {
		try {
			output.write(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @Override
    public void onColorsChange( BufferedImage image, byte[] colors )
    {
        write( colors );
    }
}
