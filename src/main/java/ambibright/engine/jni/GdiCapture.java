package ambibright.engine.jni;

import gnu.io.NativeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicolas Morel
 */
public final class GdiCapture {
	private static final Logger logger = LoggerFactory.getLogger(GdiCapture.class);
	private static boolean loaded = false;

	/**
	 * Load the native library
	 */
    public static boolean load() {
        if (!loaded) {
            try {
                new NativeResource().load("GdiCapture");
                loaded = true;
            } catch (Exception e) {
                logger.error("Error loading native library GdiCapture", e);
                return false;
            }
        }
        return loaded;
    }

	public static native byte[] captureScreen(int x, int y, int width, int height);

}
