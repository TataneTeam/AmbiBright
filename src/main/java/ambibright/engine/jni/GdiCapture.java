package ambibright.engine.jni;

import gnu.io.NativeResource;

/**
 * @author Nicolas Morel
 */
public class GdiCapture {

    static {
        new NativeResource().load( "GdiCapture" );
    }

	public static native byte[] captureScreen(int top, int left, int width, int height);

}
