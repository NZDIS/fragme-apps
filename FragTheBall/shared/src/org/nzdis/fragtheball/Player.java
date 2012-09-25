package org.nzdis.fragtheball;

import java.util.Random;

import org.nzdis.fragme.ControlCenter;


public class Player {
	
	protected float velocityX = 0.0f;
	protected float velocityY = 0.0f;
	protected float velocityZ = 0.0f;
	protected FragMePlayer fmPlayer;

	protected float accelerationX = 0.0f;
	protected float accelerationY = 0.0f;
	protected float accelerationZ = 0.0f;
	
	protected float accelerationMax = 10.0f;

	protected static float accMin = 0.0f;
	//protected static float accMin = 0.35f;

	protected static float xMax = 1.0f;
	protected static float yMax = 1.0f;
	protected static float xMin = 0.0f;
	protected static float yMin = 0.0f;
	protected static float zMin = -2.0f;
	protected static float xMaxDoubled = xMax * 2.0f;
	protected static float yMaxDoubled = yMax * 2.0f;
	protected static float xMinDoubled = xMin * 2.0f;
	protected static float yMinDoubled = yMin * 2.0f;
	protected static float zMinDoubled = zMin * 2.0f;
	protected static float gravity = 9.8f;
	
	// Scale the effect of acceleration (bigger numbers slow things down faster)
	protected static float accelerationFactor = 0.01f;
	protected static float frictionFactor = 0.01f;
	protected static float gravityFactor = 0.0f;
	
	protected boolean isActive = false;
	protected long lastUpdateTime;

	Random rng = new Random();
	
	public Player() {
		// !!! What happens if fragme receives an object which it hasn't instantiated before?
		new FragMePlayer();
		fmPlayer = (FragMePlayer)ControlCenter.createNewObject(FragMePlayer.class);
	}
	
	public FragMePlayer getFMPlayer() {
		return this.fmPlayer;
	}

	public float getVelocityX() {
		return this.velocityX;
	}
	
	public float getVelocityY() {
		return this.velocityY;
	}
	
	public float getVelocityZ() {
		return this.velocityZ;
	}
	
	public void setAcceleration(float aX, float aY, float aZ) {
		// Make sure you remove gravity from your variables before calling this method
		accelerationX = Math.max(Math.min(aX, accelerationMax), -accelerationMax);
		accelerationY = Math.max(Math.min(aY, accelerationMax), -accelerationMax);
		accelerationZ = Math.max(Math.min(aZ, accelerationMax), -accelerationMax);
	}
	
	public void randomiseAcceleration() {
		accelerationX = (rng.nextFloat() * 2.0f - 1.0f) * accelerationMax;
		accelerationY = (rng.nextFloat() * 2.0f - 1.0f) * accelerationMax;
		accelerationZ = 0.0f;
	}
	
	public void update() {
		if (!isActive) {
			isActive = true;
			lastUpdateTime = System.currentTimeMillis();
			return;
		}
		
		long currentTime = System.currentTimeMillis(); 
		float frameDuration = ((float)(currentTime - lastUpdateTime)) / 1000.0f;
		lastUpdateTime = currentTime;
		
		// Acceleration
		if (accelerationX < -accMin || accelerationX > accMin) velocityX += accelerationX * accelerationFactor * frameDuration;
		if (accelerationY < -accMin || accelerationY > accMin) velocityY += accelerationY * accelerationFactor * frameDuration;
		if (accelerationZ > accMin) {
			velocityZ -= (accelerationZ - gravity) * accelerationFactor * frameDuration;
		}
		velocityZ -= gravity * gravityFactor * frameDuration;

		// Air friction
		velocityX *= (1.0f - frictionFactor * frameDuration);
		velocityY *= (1.0f - frictionFactor * frameDuration);
		velocityZ *= (1.0f - frictionFactor * 0.4f * frameDuration);

		// Update position
		fmPlayer.positionX += velocityX * frameDuration;
		fmPlayer.positionY += velocityY * frameDuration;
		fmPlayer.positionZ += velocityZ * frameDuration;

		//
		if (fmPlayer.positionX < xMin) {
			fmPlayer.positionX = xMinDoubled - fmPlayer.positionX;
			velocityX = -velocityX;
			// !!! Reverse the direction of acceleration, for the desktop model without input 
			//accelerationX = -accelerationX;
		}
		if (fmPlayer.positionX > xMax) {
			fmPlayer.positionX = xMaxDoubled - fmPlayer.positionX;
			velocityX = -velocityX;
			// !!! Reverse the direction of acceleration, for the desktop model without input 
			//accelerationX = -accelerationX;
		}
		if (fmPlayer.positionY < yMin) {
			fmPlayer.positionY = yMinDoubled - fmPlayer.positionY;
			velocityY = -velocityY;
			// !!! Reverse the direction of acceleration, for the desktop model without input 
			//accelerationY = -accelerationY;
		}
		if (fmPlayer.positionY > yMax) {
			fmPlayer.positionY = yMaxDoubled - fmPlayer.positionY;
			velocityY = -velocityY;
			// !!! Reverse the direction of acceleration, for the desktop model without input 
			//accelerationY = -accelerationY;
		}

		if (fmPlayer.positionZ < zMin) {
			fmPlayer.positionZ = zMinDoubled - fmPlayer.positionZ;
			velocityZ = -velocityZ;
		}

		//System.out.printf("%f %f %f\n", fmplayer.positionX, fmplayer.positionY, fmplayer.positionZ);
		fmPlayer.change();
	}
}
