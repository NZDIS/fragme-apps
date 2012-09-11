package org.nzdis.fragtheball;



import android.content.Context;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



public class GLESView extends GLSurfaceView {
	private ExampleRenderer renderer;

	public GLESView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		renderer = new ExampleRenderer();
		setRenderer(renderer);
	}

	static {
		System.loadLibrary("fragtheballrenderer");
	}

	public static native void pause();
	public static native void resume();
	public static native void drawFrame();
	public static native void surfaceChanged(int width, int height);
	public static native void surfaceCreated();
 
  private static class ExampleRenderer implements GLSurfaceView.Renderer {
    
	@Override
    public void onDrawFrame(GL10 unused) {
    	drawFrame();
    }

	@Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
    	surfaceChanged(width, height);
    }

	@Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
    	surfaceCreated();
    }
  }
}