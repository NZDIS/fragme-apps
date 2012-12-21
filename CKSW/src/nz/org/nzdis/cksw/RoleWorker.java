package nz.org.nzdis.cksw;

public class RoleWorker extends Role {

	// Adjustable parameters
	private final double abilityFactor = 1.0 + random.nextTrimmedScaledGaussian() * Cksw.randomAbilityRange;
	// Prevent over-spending etc
	private static final double baseGoodsProducedPerTurn = Cksw.baseGoodsAmount;
	private static final double goodsPercentageToReserve = 0.5;
	private static final int goodsTurnsToReserve = 3;
	private static final int maxToolsToHold = 3;
	
	public RoleWorker() {
		super(Cksw.ROLE_WORKER);
	}
	
	@Override
	public void perform(Agent agent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// Produce base goods
		double producedGoods = baseGoodsProducedPerTurn * abilityFactor;
		agent.goods += producedGoods;
		
		/////////////////////////////////////////////////////////////////////////////////////
		// Buy tools
		if (((agent.toolBox.numberOfTools() < maxToolsToHold) || (maxToolsToHold == 0)) && (Cksw.skilled.size() > 0)) {
			double goodsPercentageAmountToReserve = agent.goods * goodsPercentageToReserve;
			double goodsDaysToReserve = goodsTurnsToReserve * Agent.goodsConsumedPerTurn;
			double goodsToReserve = Math.max(goodsPercentageAmountToReserve, goodsDaysToReserve);
			double goodsToUse = agent.goods - goodsToReserve;
			
			if (goodsToUse >= Tool.cost()) {
				Agent skilledAcquaintance = Cksw.skilled.get(random.nextInt(Cksw.skilled.size()));
				if (skilledAcquaintance.toolBox.numberOfTools() > 0) {
					if (!agent.toolBox.containsToolType(skilledAcquaintance.toolBox.getTool(0).type()))
					goodsToUse -= Tool.cost();
					
					agent.goods -= Tool.cost();
					skilledAcquaintance.goods += Tool.cost();
					
					Tool tool = skilledAcquaintance.toolBox.removeTool(0);
					agent.toolBox.addTool(tool);
				}
			}
		}
		
		/////////////////////////////////////////////////////////////////////////////////////
		// Produce bonus goods with tools
		double producedBonusGoods = 0;
		for (int i = agent.toolBox.numberOfTools()-1; i >= 0; i--) {
			if (agent.toolBox.getTool(i).use()) {
				producedBonusGoods += agent.toolBox.getTool(i).bonusGoodsProducedPerTurn();
			}
			if (agent.toolBox.getTool(i).numberOfUsesRemaining() <= 0) {
				agent.toolBox.removeTool(i);
			}
		}
		agent.goods += producedBonusGoods;
	}

	public static double value(Agent agent) {
		// Value of current resources
		double a = agent.goods;
		// Value of tools when used
		double b = agent.toolBox.numberOfTools() * baseGoodsProducedPerTurn;
		// Lack of labor
		double c = Cksw.agents.size() / (Cksw.workers.size() + 1.0);
		double result = (a + b) * c;
		System.out.println("Worker value: " + a + ",   " + b + ",   " + c + ",   " + result);
		return result;
	}

}
