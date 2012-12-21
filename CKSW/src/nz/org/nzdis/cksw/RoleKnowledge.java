package nz.org.nzdis.cksw;

public class RoleKnowledge extends Role {

	private static final double initialInherentKnowledge = 1.0;
	
	private static final double maxKnowledgeBenefit = 2.0;
	private static final double minInherentKnowledge = 0.2;
	private static final double knowledgeLossPerTurn = 0.1;
	
	// From 0.0 to 1.0
	private double inherentKnowledge = initialInherentKnowledge;
	
	public RoleKnowledge() {
		super(Cksw.ROLE_KNOWLEDGE);
	}
	
	// From 1.0 to RoleKnowledge.maxKnowledgeBenefit (can use to multiply)
	public double totalKnowledge(Agent agent) {
		return 1.0 + (inherentKnowledge + agent.knowledge) / maxKnowledgeBenefit;
	}
	
	@Override
	public void perform(Agent agent) {
		// Loss of knowledge over time
		inherentKnowledge -= knowledgeLossPerTurn;
		if (inherentKnowledge <= minInherentKnowledge) {
			inherentKnowledge = initialInherentKnowledge;
		}
	}

	public static double value(Agent agent) {
		return 0.0;
	}

}
