#include "stdafx.h"
#include "DirectXCapture.h"

IDirect3D9*			g_pD3D=NULL;
IDirect3DDevice9*	g_pd3dDevice=NULL;
IDirect3DSurface9*	g_pSurface=NULL;

JNIEXPORT void JNICALL Java_ambibright_engine_jni_DirectXCapture_initDirectX
  (JNIEnv *env, jclass clz, jint screen){
	
	D3DDISPLAYMODE	ddm;
	D3DPRESENT_PARAMETERS	d3dpp;

	g_pD3D=Direct3DCreate9(D3D_SDK_VERSION);
	g_pD3D->GetAdapterDisplayMode(screen,&ddm);

	ZeroMemory(&d3dpp,sizeof(D3DPRESENT_PARAMETERS));

	d3dpp.Windowed=true;
	d3dpp.Flags=D3DPRESENTFLAG_LOCKABLE_BACKBUFFER;
	d3dpp.BackBufferFormat=ddm.Format;
	d3dpp.BackBufferHeight=ddm.Height;
	d3dpp.BackBufferWidth=ddm.Width;
	d3dpp.MultiSampleType=D3DMULTISAMPLE_NONE;
	d3dpp.SwapEffect=D3DSWAPEFFECT_DISCARD;
	d3dpp.PresentationInterval=D3DPRESENT_INTERVAL_DEFAULT;
	d3dpp.FullScreen_RefreshRateInHz=D3DPRESENT_RATE_DEFAULT;

	g_pD3D->CreateDevice(screen, D3DDEVTYPE_HAL, NULL, D3DCREATE_SOFTWARE_VERTEXPROCESSING, &d3dpp, &g_pd3dDevice);
	g_pd3dDevice->CreateOffscreenPlainSurface(ddm.Width, ddm.Height, D3DFMT_A8R8G8B8, D3DPOOL_SCRATCH, &g_pSurface, NULL);
}

JNIEXPORT jbyteArray JNICALL Java_ambibright_engine_jni_DirectXCapture_captureScreenDirectX
  (JNIEnv *env, jclass clz, jint x, jint y, jint width, jint height){
	
	RECT rect = {0};
	rect.left = x;
	rect.top = y;
	rect.right = width + x;
	rect.bottom = height + y;

    g_pd3dDevice->GetFrontBufferData(0, g_pSurface);

	D3DLOCKED_RECT	lockedRect;
	g_pSurface->LockRect(&lockedRect, &rect, D3DLOCK_NO_DIRTY_UPDATE|D3DLOCK_NOSYSLOCK|D3DLOCK_READONLY);

	jbyteArray result = env->NewByteArray(4 * width * height);

	for(int i = 0; i < height; i++)
	{
		env->SetByteArrayRegion(result, i * width * 32 / 8, width * 32 / 8, (jbyte*) lockedRect.pBits + i * lockedRect.Pitch);		
	}
	g_pSurface->UnlockRect();

	return result;
}

JNIEXPORT void JNICALL Java_ambibright_engine_jni_DirectXCapture_destroyDirectX
  (JNIEnv *env, jclass clz){

    if(g_pSurface)
	{
		g_pSurface->Release();
		g_pSurface=NULL;
	}
	if(g_pd3dDevice)
	{
		g_pd3dDevice->Release();
		g_pd3dDevice=NULL;
	}
	if(g_pD3D)
	{
		g_pD3D->Release();
		g_pD3D=NULL;
	}

}