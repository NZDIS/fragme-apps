package nz.org.nzdis.cksw;

import ec.util.MersenneTwister;

public class ToolTypes {
	private static final int initialNumberOfToolTypes = 10;
	
	private static int currentMaxType = initialNumberOfToolTypes;
	
	private static final MersenneTwister random = new MersenneTwister();

	public static int getRandomToolType() {
		int newType = random.nextInt(currentMaxType + 1);
		if (newType > currentMaxType) {
			currentMaxType = newType;
		}
		return newType;
	}

	public static int getStartupRandomToolType() {
		int newType = random.nextInt(currentMaxType);
		return newType;
	}
}
