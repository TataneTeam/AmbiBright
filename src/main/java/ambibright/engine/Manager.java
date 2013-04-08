package ambibright.engine;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import ambibright.ressources.Factory;

/**
 * Created with IntelliJ IDEA. User: Nico Date: 24/03/13 Time: 16:53 To change
 * this template use File | Settings | File Templates.
 */
public class Manager {

    private final Set<ColorsChangeObserver> observers;

	private ScheduledExecutorService processCheckerServiceExecutor;
	private ScheduledExecutorService aspectRatioServiceExecutor;
	private ScheduledExecutorService colorServiceExecutor;

    private UpdateColorsService updateColorsService;

	private boolean isRunning = false;

    public Manager(){
        this.observers = new CopyOnWriteArraySet<ColorsChangeObserver>();
    }

	public void start() {
		processCheckerServiceExecutor = Executors.newScheduledThreadPool(1);
		processCheckerServiceExecutor.scheduleAtFixedRate( Factory.get().newProcessCheckerService(), 0,
            Factory.get().getDelayCheckProcess(), TimeUnit.MILLISECONDS );
	}

	public void stop() {
		if (null != processCheckerServiceExecutor ) {
			processCheckerServiceExecutor.shutdown();
			processCheckerServiceExecutor = null;
		}
		stopColorsProcessing();
	}

	public void restart() {
		stop();
		start();
	}

	public void startColorsProcessing() {
		if (!isRunning) {
			System.out.println("Starting");

            ArduinoSender arduino = Factory.get().getArduinoSender();
			try {
                arduino.open(Factory.get().getArduinoSerial(), Factory.get().getArduinoDataRate());
                observers.add( arduino );
			} catch (Exception e) {
				e.printStackTrace();
				// If the communication with the arduino failed, we remove it from the observers
				observers.remove( arduino );
				JOptionPane.showMessageDialog(null, "Arduino connection error:\n" + e, Factory.appName, JOptionPane.ERROR_MESSAGE);
			}

			aspectRatioServiceExecutor = Executors.newScheduledThreadPool(1);
			aspectRatioServiceExecutor.scheduleAtFixedRate( Factory.get().newAspectRatioService(), 0,
                Factory.get().getDelayCheckRatio(), TimeUnit.MILLISECONDS );

            updateColorsService = Factory.get().newUpdateColorsService(observers);

			colorServiceExecutor = Executors.newScheduledThreadPool(1);
			colorServiceExecutor.scheduleAtFixedRate( updateColorsService, 50, 1000 / Factory.get()
                .getFpsWanted(), TimeUnit.MILLISECONDS );

			Factory.get().getSimpleFPSFrame().setVisible(Factory.get().isShowFPSFrame());

			if (Factory.get().isBlackOtherScreens()) {
				Factory.get().getBlackScreenManager().createBlackScreens(Factory.get().getBounds());
			}

			isRunning = true;
			System.out.println("Started");
		}
	}

	public void stopColorsProcessing() {
		if (isRunning) {
			System.out.println("Stopping");
			aspectRatioServiceExecutor.shutdown();
			aspectRatioServiceExecutor = null;

			colorServiceExecutor.shutdown();
			colorServiceExecutor = null;

			Factory.get().getAmbiFrame().setInfo("Not running");
			Factory.get().getArduinoSender().close();
			Factory.get().getSimpleFPSFrame().setVisible( false );
			Factory.get().getBlackScreenManager().removeBlackScreens();

			isRunning = false;
			System.out.println("Stopped");
		}
	}

    public void addObserver(ColorsChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ColorsChangeObserver observer) {
        observers.remove(observer);
    }
}
