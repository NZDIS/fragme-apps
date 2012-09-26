package org.nzdis.pingtest;

import java.util.Random;

import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.util.NetworkUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	public FragMePingPacket pingPacket;
	
	// Stuff
	private boolean isRunning = true;
	Random rng = new Random();

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		System.out.println("Starting");

		
		String address = NetworkUtils.getNonLoopBackAddressByProtocol(NetworkUtils.IPV4);
		System.out.println("Using address: " + address);
		
		String peerName = String.format("testDesktop%d", rng.nextInt(1000));
	
		// Setup FragMe
		ControlCenter.setUpConnections("testGroupNathan", peerName, address);
		
		if (ControlCenter.getNoOfPeers() == 0) {
			// We are the first to launch, so create the FragMePingPacket
			System.out.println("Creating ping packet");
			new FragMePingPacket();
			pingPacket = (FragMePingPacket)ControlCenter.createNewObject(FragMePingPacket.class);
			PingPacketHistory.previousCounter = pingPacket.getCounter();
		} else {
        	// We are not the first, so find the ping packet that has already been created
			while(ControlCenter.getObjectManager().getAllObjects(FragMePingPacket.class).isEmpty()) {
				System.out.println("Waiting for first object to be received");
	        	try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Using existing ping packet");
			pingPacket = (FragMePingPacket)ControlCenter.getObjectManager().getAllObjects(FragMePingPacket.class).firstElement();
			PingPacketHistory.previousCounter = pingPacket.getCounter() - 1;
			pingPacket.changedObject();
		}
		
		int lastCounter = pingPacket.getCounter();
		long lastTime = System.currentTimeMillis();
    	while(isRunning) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
        	long duration = System.currentTimeMillis() - lastTime;
        	int difference = pingPacket.getCounter() - lastCounter;
        	//if (difference > 0) {
	    		long ping = 500 * duration / difference;
				System.out.println(peerName + " Current count: " + pingPacket.getCounter() + 
						" Ping: " + ping);
        	//}
        	
			lastCounter = pingPacket.getCounter();
			lastTime = System.currentTimeMillis();
		}
		
		ControlCenter.closeUpConnections();
		System.out.println("Finished");
		System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
