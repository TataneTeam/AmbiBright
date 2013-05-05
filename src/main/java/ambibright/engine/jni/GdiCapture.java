package ambibright.engine.jni;

import java.nio.ByteBuffer;

import gnu.io.NativeResource;

/**
 * @author Nicolas Morel
 */
public class GdiCapture {

    static {
        new NativeResource().load( "GdiCapture" );
    }

	public static native byte[] captureScreen(int top, int left, int width, int height);

    public static native ByteBuffer captureScreenBuffer(int top, int left, int width, int height);

    public static native void freeBuffer(ByteBuffer buffer);

}
