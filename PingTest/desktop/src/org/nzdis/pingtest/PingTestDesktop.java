package org.nzdis.pingtest;

import java.util.Random;

import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.helpers.StartupWaitForObjects;
import org.nzdis.fragme.objects.ChangeObserver;
import org.nzdis.fragme.objects.FMeObject;
import org.nzdis.fragme.util.NetworkUtils;



public class PingTestDesktop implements ChangeObserver {

	public FragMePingPacket pingPacket;
	
	// Stuff
	private boolean isRunning = true;
	Random rng = new Random();

	
	
	// Startup
	public static void main(String args[]){
		new PingTestDesktop();
	}
	
	
	
	public PingTestDesktop() {
		System.out.println("Starting");

		//String address = NetworkUtils.getNonLoopBackAddressByProtocol(NetworkUtils.IPV4);
		String address="127.0.0.1";
		System.out.println("Using address: " + address);
		
		String peerName = String.format("testDesktop%d", rng.nextInt(1000));
	
		// Setup FragMe
		//ControlCenter.setUpConnections("testGroup8", peerName, address);

		ControlCenter.setUpConnectionsWithHelper("testGroupPingTest", peerName, address, new StartupWaitForObjects(1));

		
		if (ControlCenter.getNoOfPeers() == 0) {
			// We are the first to launch, so create the FragMePingPacket
			System.out.println("Creating ping packet");
			new FragMePingPacket();
			pingPacket = (FragMePingPacket)ControlCenter.createNewObject(FragMePingPacket.class);
			PingPacketHistory.previousCounter = pingPacket.getCounter();
			pingPacket.register(this);
			startTimer();
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
			pingPacket.register(this);
			this.changed(pingPacket);
			startTimer();
			
		}
	}
		public void startTimer(){
		int lastCounter = pingPacket.getCounter();
		//long lastTime = System.currentTimeMillis();
		Long lastTime =System.nanoTime();
		
		int runs=0;
    		while(runs<10) {
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
        	long durationNano = (System.nanoTime() - lastTime);
        	long durationms=durationNano/1000000;
        	int numPings = pingPacket.getCounter() - lastCounter;
        	if (numPings > 0) {
	    		long averagePingMicros = durationNano / numPings/1000;
				System.out.println(runs + ", " +numPings+
						", " +durationms+", " + averagePingMicros);
        	}else{
        		//System.out.println(runs +" no packet sent");
        	}
        	
			lastCounter = pingPacket.getCounter();
			lastTime = System.nanoTime();
			runs++;
		}
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		ControlCenter.closeUpConnections();
		System.out.println("Finished");
		System.exit(0);
	}
	


	@Override
	public void changed(FMeObject object) {
		FragMePingPacket pp = (FragMePingPacket)object;
		int ct = pp.getCounter();
		if (ct == PingPacketHistory.previousCounter + 1) {
			pp.setCounter(ct + 1);
			PingPacketHistory.previousCounter = ct + 1;
			pp.change();
		}
	}



	@Override
	public void delegatedOwnership(FMeObject object) {
	}



	@Override
	public void deleted(FMeObject object) {
	}
	

}
