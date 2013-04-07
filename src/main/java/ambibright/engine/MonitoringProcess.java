package ambibright.engine;

import java.awt.Robot;

import ambibright.ihm.MonitoringFrame;
import ambibright.ihm.SimpleFPSFrame;
import ambibright.ressources.CurrentBounds;

public class MonitoringProcess implements Runnable {

	private UpdateColorsService updateColorService;
	private MonitoringFrame monitoringFrame;
	private SimpleFPSFrame simpleFPSFrame;
	private CurrentBounds currentBounds;
	private Robot robot;
	private long lastCheck, fps;
	private String formatedText;

	public MonitoringProcess(UpdateColorsService updateColorService, MonitoringFrame monitoringFrame, SimpleFPSFrame simpleFPSFrame, Robot robot, CurrentBounds currentBounds) {
		super();
		this.updateColorService = updateColorService;
		this.monitoringFrame = monitoringFrame;
		this.simpleFPSFrame = simpleFPSFrame;
		this.robot = robot;
		this.currentBounds = currentBounds;
		this.lastCheck = System.currentTimeMillis();
	}

	public void run() {
		if (simpleFPSFrame.isVisible() || monitoringFrame.isVisible()) {
			fps = updateColorService.getAndResetIterationNumber() / ((System.currentTimeMillis() - lastCheck) / 1000);
			formatedText = fps + " fps";
			lastCheck = System.currentTimeMillis();
			if (simpleFPSFrame.isVisible()) {
				simpleFPSFrame.setValue(formatedText);
			}
			if (monitoringFrame.isVisible()) {
				monitoringFrame.setInfo(formatedText);
				monitoringFrame.setImage(robot.createScreenCapture(currentBounds.getBounds()));
			}
		}
	}

}
