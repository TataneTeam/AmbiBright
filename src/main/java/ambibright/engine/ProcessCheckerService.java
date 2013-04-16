package ambibright.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.config.Config;

/**
 * Checks if any of the configured process is running
 */
public class ProcessCheckerService implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(ProcessCheckerService.class);
	private static final String PROCESS_CMD = System.getenv("windir") + "\\system32\\" + "tasklist.exe /FO CSV /NH";
	private final Manager manager;
	private final Config config;

	public ProcessCheckerService(Manager manager, Config config) {
		this.manager = manager;
		this.config = config;
	}

	public void run() {
		boolean shouldRun = false;

		String apps = config.getCheckProcessList();
		logger.debug("Checking if any of the process '{}' is currently running", apps);
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
			logger.error("Error while checking process", e);
		} finally {
			if (null != input) {
				try {
					input.close();
				} catch (IOException e) {
					logger.warn("Error closing the input", e);
				}
			}
		}
		logger.debug("Process found : {}", shouldRun);

		if (shouldRun) {
			manager.startColorsProcessing();
		} else if (!shouldRun) {
			manager.stopColorsProcessing();
		}
	}
}
