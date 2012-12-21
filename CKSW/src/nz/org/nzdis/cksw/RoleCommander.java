package nz.org.nzdis.cksw;

public class RoleCommander extends Role {

	// Commander tax rates
	public static final double taxCollectedFromSkilledIncome = 0.1;
	public static final double taxCollectedFromWorkerIncome = 0.1;
	
	public RoleCommander() {
		super(Cksw.ROLE_COMMANDER);
	}
	
	@Override
	public void perform(Agent agent) {
		/////////////////////////////////////////////////////////////////////////////////////
		// Collect taxes
		
		for (int i = 0; i < Cksw.skilled.size(); i++) {
			if (Cksw.skilled.get(i).goods >= taxCollectedFromSkilledIncome) {
				Cksw.skilled.get(i).goods -= taxCollectedFromSkilledIncome;
				agent.goods += taxCollectedFromSkilledIncome;
			}
		}
		
		for (int i = 0; i < Cksw.workers.size(); i++) {
			if (Cksw.workers.get(i).goods >= taxCollectedFromWorkerIncome) {
				Cksw.workers.get(i).goods -= taxCollectedFromWorkerIncome;
				agent.goods += taxCollectedFromWorkerIncome;
			}
		}
	}

	public static double value(Agent agent) {
		return 0.0;
	}

}
