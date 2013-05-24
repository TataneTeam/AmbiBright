package ambibright.engine.jni;

import gnu.io.NativeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicolas Morel
 */
public final class DirectXCapture {
    private static final Logger logger = LoggerFactory.getLogger( DirectXCapture.class );
    private static boolean loaded = false;

    /**
     * Load the native library
     */
    public static boolean load() {
        if (!loaded) {
            try {
                new NativeResource().load("DirectXCapture");
                loaded = true;
            } catch (Exception e) {
                logger.error("Error loading native library DirectXCapture", e);
                return false;
            }
        }
        return loaded;
    }

	public static native void initDirectX(int screenDevice);

	public static native byte[] captureScreenDirectX(int x, int y, int width, int height);

	public static native void destroyDirectX();

}
