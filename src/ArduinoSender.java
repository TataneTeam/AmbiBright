import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.OutputStream;

public class ArduinoSender  {

	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 115200;

	private SerialPort serialPort;
	private OutputStream output;

	public ArduinoSender(String port) {
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			output = serialPort.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void write(byte[] array) {
		try {
			output.write(array);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void close() {
		if (serialPort != null) {
			serialPort.close();
		}
	}

}