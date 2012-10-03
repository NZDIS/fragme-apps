package org.nzdis.pingtest;

import java.util.Random;

import org.jgroups.log.Trace;
import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.helpers.StartupWaitForObjects;
import org.nzdis.fragme.util.NetworkUtils;

import android.os.Handler;
import android.os.Message;

public class PingTest extends Thread {

	public FragMePingPacket pingPacket;
	
	// Stuff
	public boolean isRunning = true;
	Random rng = new Random();
	
	Handler edittextHandler;

	public PingTest(Handler edittextHandler) {
		this.edittextHandler = edittextHandler;
		this.start();
	}

	public void println(String text) {
		Message msg = new Message();
		msg.obj = text;
		edittextHandler.sendMessage(msg);
		System.out.println(text);
	}
	
	public void run() {

		println("Starting...");
		
		Trace.trace = false;
		
		String address = NetworkUtils.getNonLoopBackAddressByProtocol(NetworkUtils.IPV4);
		if (address == null) {
			println("Could not find a local ip address");
			return;
		}
		println("Using address: " + address);
		
		String peerName = String.format("testDesktop%d", rng.nextInt(1000));
	
		// Setup FragMe
		ControlCenter.setUpConnectionsWithHelper("testGroupNathan", peerName, address, new StartupWaitForObjects(1));
		
		if (ControlCenter.getNoOfPeers() == 0) {
			// We are the first to launch, so create the FragMePingPacket
			println("Creating ping packet");
			new FragMePingPacket();
			pingPacket = (FragMePingPacket)ControlCenter.createNewObject(FragMePingPacket.class);
			PingPacketHistory.previousCounter = pingPacket.getCounter();
		} else {
        	// We are not the first, so find the ping packet that has already been created
			println("Using existing ping packet");
			pingPacket = (FragMePingPacket)ControlCenter.getObjectManager().getAllObjects(FragMePingPacket.class).firstElement();
			PingPacketHistory.previousCounter = pingPacket.getCounter() - 1;
			pingPacket.changedObject();
		}
		
		println("Finished startup, running");

		
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
	    	if (difference > 0) {
	    		long ping = duration * 2 / difference;
	    		println(peerName + "Current count: " + pingPacket.getCounter() + 
						" Ping (round trip): " + ping);
	    	} else {
	    		println("Current count: " + pingPacket.getCounter() + " No packets sent");
	    	}
	    	
			lastCounter = pingPacket.getCounter();
			lastTime = System.currentTimeMillis();
		}
	
		ControlCenter.closeUpConnections();
		System.exit(0);
	}
	
}
