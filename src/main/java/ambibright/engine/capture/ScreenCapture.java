package ambibright.engine.capture;

import java.awt.Rectangle;

/**
 * @author Nicolas Morel
 */
public enum ScreenCapture implements ScreenCaptureMethod
{

	Robot(new RobotScreenCapture()), GDI(new GdiScreenCapture());

	private ScreenCaptureMethod screenCapture;

	ScreenCapture( ScreenCaptureMethod screenCapture ) {
		this.screenCapture = screenCapture;
	}

	public Image captureScreen(Rectangle bounds) {
		return screenCapture.captureScreen(bounds);
	}
}
