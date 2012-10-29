package org.nzdis.fragtheball;

import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.objects.FMeObject;

public class FragMePlayer extends FMeObject {

	private static final long serialVersionUID = -5167240080926045242L;
	
	
	
	protected float positionX = 0.5f;
	protected float positionY = 0.5f;
	protected float positionZ = -2.0f;
	protected float orientation = 0.0f;
	protected float health = 0.0f;
	protected float energy = 0.0f;
	protected int score = 0;
	
	
	
	@Override
	public void deserialize(FMeObject serObject) {
		this.positionX = ((FragMePlayer) serObject).getPositionX();
		this.positionY = ((FragMePlayer) serObject).getPositionY();
		this.positionZ = ((FragMePlayer) serObject).getPositionZ();
		this.orientation = ((FragMePlayer) serObject).getOrientation();
		this.health = ((FragMePlayer) serObject).getHealth();
		this.energy = ((FragMePlayer) serObject).getEnergy();
		this.score = ((FragMePlayer) serObject).getScore();
	}

	private static class Factory extends FragMeFactory {
		protected FactoryObject create() {
			return new FragMePlayer();
		}
	}

	static {
		FragMeFactory.addFactory(new Factory(), FragMePlayer.class);
	}

	@Override
	public void changed(FMeObject object) {
	}

	@Override
	public void delegatedOwnership(FMeObject object) {
	}

	@Override
	public void deleted(FMeObject object) {
	}	
	
	public float getPositionX() {
		return this.positionX;
	}
	
	public float getPositionY() {
		return this.positionY;
	}
	
	public float getPositionZ() {
		return this.positionZ;
	}
	
	public float getOrientation() {
		return this.orientation;
	}
	
	public float getHealth() {
		return this.health;
	}
	
	public float getEnergy() {
		return this.energy;
	}
	
	public int getScore() {
		return this.score;
	}

}
