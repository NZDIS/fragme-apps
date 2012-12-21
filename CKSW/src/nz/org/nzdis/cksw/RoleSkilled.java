package nz.org.nzdis.cksw;

public class RoleSkilled extends Role {
	
	// Adjustable parameters
	//private static final double initialToolCostToMake = Cksw.agentBaseGoodsConsumedPerTurn * 1.0;
	private final double abilityFactor = 1.0 + random.nextTrimmedScaledGaussian() * Cksw.randomAbilityRange;
	private static final double goodsPercentageToReserve = 0.5;
	private static final int goodsTurnsToReserve = 3;
	private static final int toolsProducedPerTurn = 1;
	private static final int maxToolsToHold = 3;

	// Instance parameters
	private int toolType;
	//private double baseToolCostToMake = initialToolCostToMake * abilityFactor;

	public RoleSkilled(int toolType) {
		super(Cksw.ROLE_SKILLED);
		this.toolType = toolType;
	}
	
	@Override
	public void perform(Agent agent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// Produce tools
		if ((agent.toolBox.numberOfTools() < maxToolsToHold) || (maxToolsToHold == 0)) {
			double goodsToReserveByPercentage = (int)(agent.goods * goodsPercentageToReserve);
			double goodsToReserveByTurns = goodsTurnsToReserve * Agent.goodsConsumedPerTurn;
			double goodsToReserve = Math.max(goodsToReserveByPercentage, goodsToReserveByTurns);
			double goodsToUse = agent.goods - goodsToReserve;
			
			int toolsToProduceThisTurn = toolsProducedPerTurn;
			double toolCostToMakeThisTurn = Tool.goodsToProduce() * (1.0 - abilityFactor);
			while((goodsToUse >= toolCostToMakeThisTurn) && (toolsToProduceThisTurn > 0) && (agent.toolBox.numberOfTools() < maxToolsToHold)) {
				if (agent.goods > toolCostToMakeThisTurn) {
					agent.toolBox.addTool(new Tool(toolType));
					toolsToProduceThisTurn--; 
					goodsToUse -= toolCostToMakeThisTurn;
				} else {
					toolsToProduceThisTurn = 0; 
				}
			}
		}
	}

	public static double value(Agent agent) {
		// Value of held goods that can be turned into tools
		double a = agent.goods / Tool.goodsToProduce() * Tool.cost();
		// Value of tools already owned and can be sold
		double b = agent.toolBox.numberOfTools() * Tool.cost();
		// The cost to become skilled (training, equipment, etc)
		double c = 0.0; //costToSwitch();
		// The value of shortage of skilled people
		int toolTurnsProducedPerSkilledPerTurn = Tool.initialNumberOfUsesRemaining * toolsProducedPerTurn;
		double toolTurnsProducedPerTurn = toolTurnsProducedPerSkilledPerTurn * Cksw.skilled.size() * 1.2;
		double d = Cksw.workers.size() / (toolTurnsProducedPerTurn + 1.0);
		// Result
		double result = (a + b - c) * d;
		System.out.println("Skilled value: " + a + ",   " + b + ",   " + c + ",   " + d + ",   " + result);
		return result;
	}
	
}
