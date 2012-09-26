package org.nzdis.pingtest;

import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;



public class FragMePingPacket extends FMeObject {
	private static final long serialVersionUID = -8697491800836330511L;

	
	public int counter = 0;
	
	public int getCounter() {
		return counter;
	}
	
	private static class Factory extends FragMeFactory {
		protected FactoryObject create() {
			return new FragMePingPacket();
		}
	}

	static {
		FragMeFactory.addFactory(new Factory(), FragMePingPacket.class);
	}
	

	@Override
	public void deserialize(FMeObject serObject) {
		this.counter = ((FragMePingPacket) serObject).getCounter();
	}

	@Override
	public void changedObject() {
		if (counter == PingTestDesktop.previousCounter + 1) {
			counter = counter + 1;
			PingTestDesktop.previousCounter = counter;
			change();
		} else {
			//System.err.println("Previous: " + PingTestDesktop.previousCounter + " Current: " + counter);
			//counter = counter + 1;
			//PingTestDesktop.previousCounter = counter;
			//change();
		}
	}

	@Override
	public void deletedObject() {
	}

}
