package nz.org.nzdis.cksw;

public class Tool {
	
	public static final int initialNumberOfUsesRemaining = 10;
	
	private int type;
	private int numberOfUsesRemaining = initialNumberOfUsesRemaining;
	
	public static final double cost() { return Cksw.baseGoodsAmount * 2.5; }
	public static final double goodsToProduce() { return Cksw.baseGoodsAmount; }
	public final double bonusGoodsProducedPerTurn() { return Cksw.baseGoodsAmount * 0.5; }
	
	public int type() { return type; }
	public int numberOfUsesRemaining() { return numberOfUsesRemaining; }
	
	
	public Tool(int type) {
		this.type = type;
	}
	
	public boolean use() {
		if (numberOfUsesRemaining > 0) {
			numberOfUsesRemaining --;
			return true;
		} else {
			return false;
		}
	}
}
