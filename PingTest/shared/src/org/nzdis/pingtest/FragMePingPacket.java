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
	}

	@Override
	public void changedObject() {
		if (!hasChanged) {
			return;
		}
		if (counter == PingPacketHistory.previousCounter + 1) {
			counter = counter + 1;
			PingPacketHistory.previousCounter = counter;
			change();
		}
	}

	@Override
	public void deletedObject() {
	}

}
