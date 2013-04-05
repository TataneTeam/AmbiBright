package ambibright.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioReader extends Thread {

	private boolean stop = false;
	private AudioFormat format;

	private AudioReader(AudioFormat format) {
		super();
		this.format = format;
	}

	public void run() {
		TargetDataLine line = null;
		DataLine.Info info = null;
		ByteArrayOutputStream out = null;
		int numBytesRead;
		try {
			info = new DataLine.Info(TargetDataLine.class, format);
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			out = new ByteArrayOutputStream();
			byte[] data = new byte[line.getBufferSize() / 5];
			line.start();
			int count = 0;
			while (!stop) {
				numBytesRead = line.read(data, 0, data.length);
				out.write(data, 0, numBytesRead);
				if(count++ == 100){
					break;
				}
			}
			// Save
			byte[] abAudioData = out.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(abAudioData);
			AudioInputStream outputAIS = new AudioInputStream(bais, format, abAudioData.length / format.getFrameSize());
			AudioSystem.write(outputAIS, AudioFileFormat.Type.WAVE, new File("c:\\a.wav"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			line.stop();
			try {
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stopReading() {
		stop = true;
	}

	public static AudioFormat getDefaultFormat() {
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 44100; // 44,1 kHz
		int sampleSize = 16; // 16 Bit
		int channels = 2; // Stereo
		boolean bigEndian = false; // LittleEndian
		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	}

	public static void main(String[] args) {
		new AudioReader(getDefaultFormat()).start();
	}

}
