package org.nzdis.pingtest;

import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;


public class FragMePingPacket extends FMeObject {
	private static final long serialVersionUID = -8697491800836330511L;

	
	public int counter = 0;
	public boolean hasChanged = false;
	public boolean isNew = true;
	
	public int getCounter() {
		return counter;
	}
	
	public void setCounter(int counter){
		this.counter=counter;
	}// added for other class to set value
	
	private static class Factory extends FragMeFactory {
		protected FactoryObject create() {
			return new FragMePingPacket();
		}
	}

	static {
		FragMeFactory.addFactory(new Factory(), FragMePingPacket.class);
	}
	
	public FragMePingPacket() {
		this.register(this);
	}

	@Override
	public void deserialize(FMeObject serObject) {
		if (isNew) {
			isNew = false;
			hasChanged = true;
		} else {
			hasChanged = false;
		}
		if (((FragMePingPacket) serObject).getCounter() != this.counter) {
			this.counter = ((FragMePingPacket) serObject).getCounter();
			hasChanged = true;
		}
		//System.out.println("pingPacket deserialize: "+hasChanged+"  "+this.counter);
	}

	@Override
	public void changed(FMeObject object) {
		if (!hasChanged) {
			return;
		}
		//System.out.println("pingPacket Received a change notification!");
		this.setChanged();
		this.notifyObservers();
		/*if (counter == PingPacketHistory.previousCounter + 1) {
			counter = counter + 1;
			PingPacketHistory.previousCounter = counter;
			change();
		}*/
	}

	@Override
	public void delegatedOwnership(FMeObject object) {
	}

	@Override
	public void deleted(FMeObject object) {
	}

}
