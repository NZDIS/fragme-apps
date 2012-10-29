package org.nzdis.pingtest;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.helpers.StartupWaitForObjects;
import org.nzdis.fragme.util.NetworkUtils;



public class PingTestDesktop implements Observer{

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
			pingPacket.addObserver(this);
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
<<<<<<< HEAD
			PingPacketHistory.previousCounter = pingPacket.getCounter() - 1;
			//pingPacket.changedObject();
			pingPacket.addObserver(this);
			
			this.update(pingPacket, null);
			startTimer();
			
=======
			pingPacket.counter++;
			PingPacketHistory.previousCounter = pingPacket.counter;
			pingPacket.change();
>>>>>>> 20029c8da4101cfbd0e968e7134e1f4c8e4e6a4f
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
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		FragMePingPacket fpp=(FragMePingPacket)arg0;
		
		int ct=fpp.getCounter();
		//System.out.println("in update() "+ct);
		
		if (ct == PingPacketHistory.previousCounter + 1) {
			fpp.setCounter(ct+1);
			PingPacketHistory.previousCounter = ct+1;
			fpp.change();
		}
	}
	

}
