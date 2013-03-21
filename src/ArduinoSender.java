import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ArduinoSender implements SerialPortEventListener {

	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	private SerialPort serialPort;
	private OutputStream output;
	private BufferedReader input;

	public ArduinoSender(String port) {
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			output = serialPort.getOutputStream();
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
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

	public void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				input.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}