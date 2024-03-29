package org.nzdis.fragtheball;



import java.util.ArrayList;
import java.util.Random;

import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.util.NetworkUtils;

import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Window;
import android.view.WindowManager;



public class FragTheBallAndroid extends Activity implements SensorEventListener {

	private AppThread appThread;
	
	private static Context context;

	private GLESView surfaceView;
	
	private PowerManager.WakeLock wl = null;
    
    private SensorManager sensorManager;
    private Sensor accelerometer;

	public Player myPlayer;
	public ArrayList<Player>myPlayers = new ArrayList<Player>();
	Random rng = new Random();
	public boolean isRunning = false;
	
	public int updatesPerSecond = 30;

    // Program startup
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Setup android 
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		appThread = new AppThread();
		appThread.start();
    	
		// Setup sensors (activate later - onResume)
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
        // Setup view (activate later - onResume)
    	surfaceView = new GLESView(this);
    	setContentView(surfaceView);
    	
    	// Setup wake lock (activate later - onResume)
    	PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
    	wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TerrainExample");
    }
    
    public static Context getAppContext() {
        return FragTheBallAndroid.context;
    }

    // Resume after lock screen or background
  	@Override
  	protected void onResume() {
    	super.onResume();
    	
    	try {
    		while (!isRunning) {
    			Thread.sleep(100);
    		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	// Activate sensors
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        
        // Activate view
    	surfaceView.onResume();
    	
    	// Activate wake lock
    	wl.acquire();
  	}

  	// Hide screen due to lock screen or background or shutdown
	@Override
  	protected void onPause() {
    	super.onPause();
    	
    	// Deactivate view
    	surfaceView.onPause();
    	
    	// Deactivate sensors
        sensorManager.unregisterListener(this);
        
        // Deactivate wake lock
    	wl.release();
  	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	// Sensor data is coming in - update the acceleration
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			//Log.i("FragTheBall", "updated acceleration: " + event.values[0] + "," + event.values[1] + "," + event.values[2]);
			myPlayer.setAcceleration(-event.values[0],  event.values[1],  event.values[2]);
		}
	}
	
    class AppThread extends Thread {
		public void run() {
			// Setup FragMe
			String address = NetworkUtils.getNonLoopBackAddressByProtocol(NetworkUtils.IPV4);
			if (address == null) {
				System.err.println("Could not find a local ip address");
				return;
			}
			System.out.println("Using address: " + address);
			String peerName = String.format("Android%d", rng.nextInt(1000));
			ControlCenter.setUpConnections("FragTheBall", peerName, address);

			// Setup Player
			myPlayer = new Player();
			myPlayer.setAcceleration(0,  0,  0);
			myPlayer.fmPlayer.positionX = 0.5f;
			myPlayer.fmPlayer.positionY = 0.5f;
			myPlayer.fmPlayer.positionZ = 0.0f;
			
			int numExtraBalls = 0;
			for (int i = 0; i < numExtraBalls; i++) {
				myPlayers.add(new Player());
			}
			
			isRunning = true;
			while(isRunning) {
				myPlayer.update();
				for (int i = 0; i < numExtraBalls; i++) {
					myPlayers.get(i).randomiseAcceleration();
					myPlayers.get(i).update();
				}
				//Log.i("FragTheBall", "updated position: " + app.myPlayer.fmPlayer.positionX + "," + app.myPlayer.fmPlayer.positionY + "," + app.myPlayer.fmPlayer.positionZ);
	        	try {
					Thread.sleep(1000/updatesPerSecond);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
