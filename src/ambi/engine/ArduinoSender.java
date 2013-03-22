package ambi.engine;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.OutputStream;

import ambi.ressources.Factory;

public class ArduinoSender {

	private SerialPort serialPort;
	private OutputStream output;

	public ArduinoSender(String port) {
		try {
			CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(port);
			serialPort = (SerialPort) portId.open(this.getClass().getName(), 500);
			serialPort.setSerialPortParams(Factory.getArduinoDataRate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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