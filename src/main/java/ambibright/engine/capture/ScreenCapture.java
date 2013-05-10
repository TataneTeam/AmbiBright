package ambibright.engine.capture;

import java.awt.Rectangle;

/**
 * @author Nicolas Morel
 */
public enum ScreenCapture implements ScreenCaptureMethod {

	Robot(new RobotScreenCapture()), GDI(new GdiScreenCapture()), DirectX(new DirectXScreenCapture());
	private ScreenCaptureMethod screenCapture;

	ScreenCapture(ScreenCaptureMethod screenCapture) {
		this.screenCapture = screenCapture;
	}

	public Image captureScreen(Rectangle bounds, int screenDevice) {
		return screenCapture.captureScreen(bounds, screenDevice);
	}
}
