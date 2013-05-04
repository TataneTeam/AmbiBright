package ambibright.engine.capture;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

/**
 * @author Nicolas Morel
 */
public class JnaScreenCapture implements ScreenCapture {
	interface GDI32 extends com.sun.jna.platform.win32.GDI32, com.sun.jna.platform.win32.WinGDI, com.sun.jna.platform.win32.WinDef {

		GDI32 INSTANCE = (GDI32) Native.loadLibrary(GDI32.class);
		int SRCCOPY = 0xCC0020;

		boolean BitBlt(HDC hdcDest, int nXDest, int nYDest, int nWidth, int nHeight, HDC hdcSrc, int nXSrc, int nYSrc, int dwRop);

		HDC GetDC(HWND hWnd);

		boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, byte[] pixels, BITMAPINFO bi, int usage);

		boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, short[] pixels, BITMAPINFO bi, int usage);

		boolean GetDIBits(HDC dc, HBITMAP bmp, int startScan, int scanLines, int[] pixels, BITMAPINFO bi, int usage);
	}

	interface User32 extends com.sun.jna.platform.win32.User32 {

		User32 INSTANCE = (User32) Native.loadLibrary(User32.class, W32APIOptions.UNICODE_OPTIONS);

		com.sun.jna.platform.win32.WinDef.HWND GetDesktopWindow();
	}

	private static class ImageImpl implements Image {

		private final int width, height;
		private byte[] pointer;
		private BufferedImage bufferedImage;

		private ImageImpl(int width, int height, byte[] pointer) {
			this.width = width;
			this.height = height;
			this.pointer = pointer;
		}

		@Override
		public int getWidth() {
			return width;
		}

		@Override
		public int getHeight() {
			return height;
		}

		@Override
		public RgbColor getRGB(int x, int y) {
			return getRGB(x, y, new RgbColor());
		}

		@Override
		public RgbColor getRGB(int x, int y, RgbColor rgb) {
			int pos = 4 * ((y * width) + x);
			rgb.update(pointer[pos + 2] & 0xff, pointer[pos + 1] & 0xff, pointer[pos] & 0xff);
            return rgb;
		}

		@Override
		public BufferedImage getBufferedImage() {
			if (null == bufferedImage) {
				bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				for (int x = 0; x < width; x++)
					for (int y = 0; y < height; y++) {
						int pos = 4 * ((y * width) + x);
						int b = pointer[pos++] & 0xff;
						int g = pointer[pos++] & 0xff;
						int r = pointer[pos] & 0xff;
						bufferedImage.setRGB(x, y, (0xFF << 24) | (r << 16) | (g << 8) | b);
					}
			}
			return bufferedImage;
		}

		@Override
		public void flush() {
			pointer = null;
		}
	}

	static final User32 USER = User32.INSTANCE;
	private static final GDI32 GDI = GDI32.INSTANCE;

	@Override
	public Image captureScreen(Rectangle bounds) {

		WinDef.HWND hDesktopWnd = null;
		WinDef.HDC hDesktopDC = null;
		WinDef.HBITMAP outputBitmap = null;
		WinDef.HDC blitDC = null;

		try {
			hDesktopWnd = USER.GetDesktopWindow();
			hDesktopDC = GDI.GetDC(hDesktopWnd);
			outputBitmap = GDI.CreateCompatibleBitmap(hDesktopDC, bounds.width, bounds.height);
			blitDC = GDI.CreateCompatibleDC(hDesktopDC);
			WinNT.HANDLE oldBitmap = GDI.SelectObject(blitDC, outputBitmap);

			GDI.BitBlt(blitDC, 0, 0, bounds.width, bounds.height, hDesktopDC, bounds.x, bounds.y, GDI32.SRCCOPY);
			GDI.SelectObject(blitDC, oldBitmap);
			WinGDI.BITMAPINFO bi = new WinGDI.BITMAPINFO(40);
			bi.bmiHeader.biSize = 40;
			bi.bmiHeader.biPlanes = 1;
			bi.bmiHeader.biBitCount = 32;
			bi.bmiHeader.biWidth = bounds.width;
			bi.bmiHeader.biHeight = -bounds.height;
			bi.bmiHeader.biCompression = WinGDI.BI_RGB;
			bi.bmiHeader.biSizeImage = 4 * bounds.width * bounds.height;

			byte[] datas = new byte[bi.bmiHeader.biSizeImage];
			GDI.GetDIBits(blitDC, outputBitmap, 0, bounds.height, datas, bi, WinGDI.DIB_RGB_COLORS);
			return new ImageImpl(bounds.width, bounds.height, datas);
		} finally {
			if (null != blitDC) {
				GDI.DeleteDC(blitDC);
			}
			if (null != outputBitmap) {
				GDI.DeleteObject(outputBitmap);
			}
			if (null != hDesktopDC && null != hDesktopWnd) {
				USER.ReleaseDC(hDesktopWnd, hDesktopDC);
			}
		}
	}
}
