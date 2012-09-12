package org.nzdis.fragtheball;



import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;



public class FragTheBallAndroid extends Activity {

	private GLESView surfaceView;
	
	private PowerManager.WakeLock wl = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    	surfaceView = new GLESView(this);
    	setContentView(surfaceView);
    	
    	PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    	wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
    }

  	@Override
  	protected void onResume() {
    	super.onResume();
    	wl.acquire();
    	surfaceView.onResume();
    	new Thread(new Runnable() {
    		public void run() {
    			GLESView.resume();
    		}
    	}).start();
  	}

	  @Override
  	protected void onPause() {
    	super.onPause();
    	wl.release();
    	surfaceView.onPause();
    	GLESView.pause();
  	}
	
}
