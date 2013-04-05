package ambibright.ressources;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ToolFile {

	public static final String fileSeparator = System.getProperty("file.separator");

	public static final String tempDirectory = System.getenv("TMP") + fileSeparator;

	public static final String userAgent = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.1.3)";

	public static String downloadFile(String url, String path) {
		String result = null;
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			HttpURLConnection urlc = (HttpURLConnection) new URL(url.replace(" ", "%20")).openConnection();
			urlc.setRequestProperty("User-Agent", userAgent);
			urlc.connect();
			in = new BufferedInputStream(urlc.getInputStream());
			fout = new FileOutputStream(path);
			byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
			result = path;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fout.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static List<String> getFileLines(String file) {
		List<String> result = new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				result.add(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<String> getDownloadedFileContent(String url) {
		List<String> result = null;
		String tempFile = getNewTempFile();
		if (downloadFile(url, tempFile) != null) {
			result = getFileLines(tempFile);
		}
		new File(tempFile).delete();
		return result;
	}

	public static String getNewTempFile() {
		return tempDirectory + Factory.appName + System.currentTimeMillis();
	}

}
