package ambibright.engine;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ambibright.ressources.Factory;

/**
 * Manages the thread of the application.
 */
public class Manager {

	private static final Logger logger = LoggerFactory.getLogger(Manager.class);

	private final Set<ColorsChangeObserver> observers;

	private ScheduledExecutorService processCheckerServiceExecutor;
	private ScheduledExecutorService aspectRatioServiceExecutor;
	private ScheduledExecutorService colorServiceExecutor;

	private UpdateColorsService updateColorsService;

	private boolean isRunning = false;

	public Manager() {
		this.observers = new CopyOnWriteArraySet<ColorsChangeObserver>();
	}

	public void start() {
		logger.info("Starting");

		processCheckerServiceExecutor = Executors.newScheduledThreadPool(1);
		processCheckerServiceExecutor.scheduleAtFixedRate(Factory.get().newProcessCheckerService(), 0, Factory.get().getDelayCheckProcess(), TimeUnit.MILLISECONDS);

		logger.info("Started");
	}

	public void stop() {
		logger.info("Stopping");

		if (null != processCheckerServiceExecutor) {
			processCheckerServiceExecutor.shutdown();
			processCheckerServiceExecutor = null;
		}
		stopColorsProcessing();

		logger.info("Stopped");
	}

	public void restart() {
		stop();
		start();
	}

	public void startColorsProcessing() {
		if (!isRunning) {
			logger.info("Starting color processing");

			ArduinoSender arduino = Factory.get().getArduinoSender();
			try {
				arduino.open(Factory.get().getArduinoSerial(), Factory.get().getArduinoDataRate());
				observers.add(arduino);
			} catch (Exception e) {
				logger.error("Arduino connection error", e);
				// If the communication with the arduino failed, we remove it
				// from the observers
				observers.remove(arduino);
				JOptionPane.showMessageDialog(null, "Arduino connection error:\n" + e, Factory.appName, JOptionPane.ERROR_MESSAGE);
			}

			aspectRatioServiceExecutor = Executors.newScheduledThreadPool(1);
			aspectRatioServiceExecutor.scheduleAtFixedRate(Factory.get().newAspectRatioService(), 0, Factory.get().getDelayCheckRatio(), TimeUnit.MILLISECONDS);

			updateColorsService = Factory.get().newUpdateColorsService(observers);

			colorServiceExecutor = Executors.newScheduledThreadPool(1);
			colorServiceExecutor.scheduleAtFixedRate(updateColorsService, 50, 1000 / Factory.get().getFpsWanted(), TimeUnit.MILLISECONDS);

			Factory.get().getSimpleFPSFrame().setVisible(Factory.get().isShowFPSFrame());

			if (Factory.get().isBlackOtherScreens()) {
				Factory.get().getBlackScreenManager().createBlackScreens(Factory.get().getBounds());
			}

			isRunning = true;
			logger.info("Color processing started");
		}
	}

	public void stopColorsProcessing() {
		if (isRunning) {
			logger.info("Stopping color processing");
			aspectRatioServiceExecutor.shutdown();
			aspectRatioServiceExecutor = null;

			colorServiceExecutor.shutdown();
			colorServiceExecutor = null;

			Factory.get().getAmbiFrame().setInfo("Not running");
			Factory.get().getArduinoSender().close();
			Factory.get().getSimpleFPSFrame().setVisible(false);
			Factory.get().getBlackScreenManager().removeBlackScreens();

			isRunning = false;
			logger.info("Color processing stopped");
		}
	}

	public void addObserver(ColorsChangeObserver observer) {
		observers.add(observer);
		logger.debug("Added the color change observer : {}", observer);
	}

	public void removeObserver(ColorsChangeObserver observer) {
		observers.remove(observer);
		logger.debug("Removed the color change observer : {}", observer);
	}
}
