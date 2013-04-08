package ambibright.engine.capture;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Interface for screen capture
 */
public interface ScreenCapture
{
    BufferedImage captureScreen(Rectangle bounds);
}
