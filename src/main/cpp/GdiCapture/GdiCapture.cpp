// GdiCapture.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "ambibright_engine_jni_GdiCapture.h"

JNIEXPORT jbyteArray JNICALL Java_ambibright_engine_jni_GdiCapture_captureScreen
  (JNIEnv *env, jclass clz, jint top, jint left, jint width, jint height){
	HWND hDesktopWnd = GetDesktopWindow();
	HDC hDesktopDC = GetDC(hDesktopWnd);

	HDC hdcMem = CreateCompatibleDC(hDesktopDC);
	HBITMAP hBitmap = CreateCompatibleBitmap(hDesktopDC, width, height);
	HGDIOBJ hOldBitmap = SelectObject(hdcMem, hBitmap);

	BitBlt(hdcMem, 0, 0, width, height, hDesktopDC, top, left, SRCCOPY);
	SelectObject(hdcMem, hOldBitmap);

	BITMAPINFOHEADER bmi = {0};
    bmi.biSize = sizeof(BITMAPINFOHEADER);
	bmi.biPlanes = 1;
	bmi.biBitCount = 32;
	bmi.biWidth = width;
	bmi.biHeight = -height;
	bmi.biCompression = BI_RGB;
	bmi.biSizeImage = 4 * width * height;

	int size = 4 * width * height;

	jbyteArray result = env->NewByteArray(size);
	jbyte* datas = (jbyte*) malloc(size);

	GetDIBits(hdcMem, hBitmap, 0, height, datas, (BITMAPINFO*)&bmi, DIB_RGB_COLORS);

	env->SetByteArrayRegion(result, 0, size, datas);
	free(datas);

	ReleaseDC(hDesktopWnd,hDesktopDC);
    DeleteDC(hdcMem);
	DeleteObject(hBitmap);

	return result;
}

JNIEXPORT jobject JNICALL Java_ambibright_engine_jni_GdiCapture_captureScreenBuffer
  (JNIEnv *env, jclass clz, jint top, jint left, jint width, jint height){
	HWND hDesktopWnd = GetDesktopWindow();
	HDC hDesktopDC = GetDC(hDesktopWnd);

	HDC hdcMem = CreateCompatibleDC(hDesktopDC);
	HBITMAP hBitmap = CreateCompatibleBitmap(hDesktopDC, width, height);
	HGDIOBJ hOldBitmap = SelectObject(hdcMem, hBitmap);

	BitBlt(hdcMem, 0, 0, width, height, hDesktopDC, top, left, SRCCOPY);
	SelectObject(hdcMem, hOldBitmap);

	BITMAPINFOHEADER bmi = {0};
    bmi.biSize = sizeof(BITMAPINFOHEADER);
	bmi.biPlanes = 1;
	bmi.biBitCount = 32;
	bmi.biWidth = width;
	bmi.biHeight = -height;
	bmi.biCompression = BI_RGB;
	bmi.biSizeImage = 4 * width * height;

	int size = 4 * width * height;

	void* datas = malloc(size);
	GetDIBits(hdcMem, hBitmap, 0, height, datas, (BITMAPINFO*)&bmi, DIB_RGB_COLORS);

	jobject result = env->NewDirectByteBuffer(datas, size);

	ReleaseDC(hDesktopWnd,hDesktopDC);
    DeleteDC(hdcMem);
	DeleteObject(hBitmap);

	return result;
}

JNIEXPORT void JNICALL Java_ambibright_engine_jni_GdiCapture_freeBuffer
  (JNIEnv *env, jclass clz, jobject byteBuffer) {
	void *buffer = env->GetDirectBufferAddress(byteBuffer);
    free(buffer);
}
