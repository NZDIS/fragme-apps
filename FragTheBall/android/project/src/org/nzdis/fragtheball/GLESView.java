package org.nzdis.fragtheball;



import java.util.Vector;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.nzdis.fragme.ControlCenter;



public class GLESView extends GLSurfaceView {
	private Renderer renderer;
	private FragTheBallAndroid app;
	
	public GLESView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		
		app = (FragTheBallAndroid)context;
		
		renderer = new Renderer(app);
		setRenderer(renderer);
	}

	public void onPause() {
		GLESView.myCleanup();
		super.onPause();
	}

	public static native void myCleanup();
	public static native void myDrawFrame();
	public static native void myDrawSphere(float x, float y, float z, boolean m);
	public static native void mySurfaceChanged(int width, int height);
	public static native void mySurfaceCreated();
	
	
	
	private static class Renderer implements GLSurfaceView.Renderer {

		FragTheBallAndroid app;
		
		public Renderer(FragTheBallAndroid app) {
			this.app = app;
		}
		
		@Override
		public void onDrawFrame(GL10 unused) {
			// Render all balls
			myDrawFrame();
			myDrawSphere(app.myPlayer.getFMPlayer().positionX, app.myPlayer.getFMPlayer().positionY, app.myPlayer.getFMPlayer().positionZ, true);
			Vector<FragMePlayer> v = ControlCenter.getAllObjects(FragMePlayer.class);
			for (int i = 0; i < v.size(); i++) {
				FragMePlayer fmp = v.elementAt(i);
				if (fmp != app.myPlayer.getFMPlayer()) {
					myDrawSphere(fmp.positionX, fmp.positionY, fmp.positionZ, false);
				}
			}
		}

		@Override
		public void onSurfaceChanged(GL10 unused, int width, int height) {
			mySurfaceChanged(width, height);
		}

		@Override
		public void onSurfaceCreated(GL10 unused, EGLConfig config) {
			mySurfaceCreated();
		}
	}
	
	static {
		System.loadLibrary("FragTheBallRenderer");
	}
}