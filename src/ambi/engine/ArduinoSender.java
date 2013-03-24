package ambi.engine;

import java.io.OutputStream;
import java.util.Arrays;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class ArduinoSender
{
    private SerialPort serialPort;
    private OutputStream output;
    private final byte[] array;

    public ArduinoSender( String port, int dataRate, int ledNumberLeftRight, int ledNumberTop )
    {
        int totalLeds = ledNumberLeftRight * 2 + ledNumberTop - 2;
        array = new byte[6 + totalLeds * 3];
        array[0] = 'A';
        array[1] = 'd';
        array[2] = 'a';
        array[3] = (byte) ((totalLeds - 1) >> 8);
        array[4] = (byte) ((totalLeds - 1) & 0xff);
        array[5] = (byte) (array[3] ^ array[4] ^ 0x55);

        try
        {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier( port );
            serialPort = (SerialPort) portId.open( this.getClass().getName(), 500 );
            serialPort.setSerialPortParams( dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE );
            output = serialPort.getOutputStream();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        if ( serialPort != null )
        {
            serialPort.close();
        }
    }

    public byte[] getArray()
    {
        return array;
    }

    public void stop()
    {
        byte[] stopTrame = getArray();
        Arrays.fill( stopTrame, 6, stopTrame.length, (byte) 0 );
        write( stopTrame );
    }

    public void write( byte[] array )
    {
        try
        {
//            output.write( array );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

}
