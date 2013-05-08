package ambibright.engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.engine.capture.Image;
import ambibright.config.Config;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class ArduinoSender implements ColorsChangeObserver {

	public static final String defaultTestString = "Ada";
	private static final Logger logger = LoggerFactory.getLogger(ArduinoSender.class);

	@SuppressWarnings("unchecked")
	public static String getArduinoPort(int dataRate, String testString) {
		String result = null;
		CommPortIdentifier portId;
		SerialPort serialPort = null;
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = portList.nextElement();
			try {
				serialPort = portId.open("Arduino", 500);
				serialPort.setSerialPortParams(dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				serialPort.enableReceiveThreshold(1);
				BufferedReader buffer = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
				if (testString.equals(buffer.readLine())) {
					result = serialPort.getName().substring(serialPort.getName().lastIndexOf("/") + 1);
					break;
				}
			} catch (Exception e) {
				logger.debug("Error communicating with the arduino at port {}", portId, e);
			} finally {
				if (null != serialPort) {
					try {
						serialPort.close();
					} catch (Exception e) {
						logger.error("Error closing the serial port", e);
					}
				}
			}
		}
		return result;
	}

	private final Config config;
	private byte[] array;
	private SerialPort serialPort;
	private OutputStream output;

	public ArduinoSender(Config config) {
		this.config = config;
	}

	public void open() throws Exception {
		int totalLeds = config.getNbLedLeft() * 2 + config.getNbLedTop() - 2;
		array = new byte[6 + totalLeds * 3];
		array[0] = 'A';
		array[1] = 'd';
		array[2] = 'a';
		array[3] = (byte) ((totalLeds - 1) >> 8);
		array[4] = (byte) ((totalLeds - 1) & 0xff);
		array[5] = (byte) (array[3] ^ array[4] ^ 0x55);

		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(config.getArduinoSerialPort());
		serialPort = portId.open(this.getClass().getName(), 500);
		serialPort.setSerialPortParams(config.getArduinoDataRate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
			logger.debug("Writing data to the Arduino");
			output.write(array);
			logger.debug("Data written");
		} catch (Exception e) {
			logger.error("Error writing data to the Arduino", e);
		}
	}

	@Override
	public void onColorsChange(Image image, byte[] colors) {
		write(colors);
	}
}
