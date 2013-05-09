package ambibright.engine.jni;

import gnu.io.NativeResource;

/**
 * @author Nicolas Morel
 */
public class DirectXCapture
{

    static {
        new NativeResource().load( "DirectXCapture" );
    }

    public static native void initDirectX(int screenDevice);

    public static native byte[] captureScreenDirectX(int x, int y, int width, int height);

    public static native void destroyDirectX();

}
