package ambi.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA. User: Nico Date: 23/03/13 Time: 21:17 To change
 * this template use File | Settings | File Templates.
 */
public class ProcessCheckerService implements Runnable {
	private static final String PROCESS_CMD = System.getenv("windir") + "\\system32\\" + "tasklist.exe /FO CSV /NH";
	private final Manager manager;
	private final String apps;

	public ProcessCheckerService(Manager manager, String processList) {
		this.manager = manager;
		this.apps = processList;
	}

	public void run() {
		boolean shouldRun = false;
		BufferedReader input = null;
		try {
			String line;
			Process p = Runtime.getRuntime().exec(PROCESS_CMD);
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				line = line.substring(1, line.indexOf("\","));
				if (apps.contains(line)) {
					shouldRun = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (shouldRun) {
			manager.startColorsProcessing();
		} else if (!shouldRun) {
			manager.stopColorsProcessing();
		}
	}
}
