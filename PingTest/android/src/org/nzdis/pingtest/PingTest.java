package org.nzdis.pingtest;

import java.util.Random;

import org.jgroups.log.Trace;
import org.nzdis.fragme.ControlCenter;
import org.nzdis.fragme.helpers.StartupWaitForObjects;
import org.nzdis.fragme.objects.ChangeObserver;
import org.nzdis.fragme.objects.FMeObject;
import org.nzdis.fragme.util.NetworkUtils;

import android.os.Handler;
import android.os.Message;

public class PingTest extends Thread implements ChangeObserver {

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
		ControlCenter.setUpConnectionsWithHelper("testGroupPingTest", peerName, address, new StartupWaitForObjects(1));
		
		if (ControlCenter.getNoOfPeers() == 0) {
			// We are the first to launch, so create the FragMePingPacket
			println("Creating ping packet");
			new FragMePingPacket();
			pingPacket = (FragMePingPacket)ControlCenter.createNewObject(FragMePingPacket.class);
			PingPacketHistory.previousCounter = pingPacket.getCounter();
			pingPacket.register(this);
			startTimer();
		} else {
        	// We are not the first, so find the ping packet that has already been created
			println("Using existing ping packet");
			pingPacket = (FragMePingPacket)ControlCenter.getObjectManager().getAllObjects(FragMePingPacket.class).firstElement();
			PingPacketHistory.previousCounter = pingPacket.getCounter() - 1;
			pingPacket.register(this);
			pingPacket.changed(pingPacket);
			startTimer();
		}
		
		//println("Finished startup, running");

	}

	public void startTimer() {
		int lastCounter = pingPacket.getCounter();
		long lastTime = System.nanoTime();
		
		int runs=0;
		while(runs<55) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	
	    	long durationNano = System.nanoTime() - lastTime;
	    	long durationMs = durationNano / 1000000;
	    	int numPings = pingPacket.getCounter() - lastCounter;
	    	if (numPings > 0) {
	    		long averagePingMicros = durationNano / numPings / 1000;
	    		//println(peerName + "Current count: " + pingPacket.getCounter() +
	    		// " Ping microseconds: " + averagePingMillis);
	    		println(runs + ", " + numPings + ", " + durationMs + ", " + averagePingMicros);
	    	} else {
	    		//println("Current count: " + pingPacket.getCounter() + " No packets sent");
	    		println(runs + " no packet sent");
	    	}

	    	lastCounter = pingPacket.getCounter();
	    	lastTime = System.nanoTime();
	    	runs++;
		}
		
		ControlCenter.closeUpConnections();
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
