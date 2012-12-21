package nz.org.nzdis.cksw;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;

public class ChartWealthUpdater implements Steppable {
	
	CkswWithUI ckswGuiState;
	
	public ChartWealthUpdater(CkswWithUI ckswGuiState) {
		this.ckswGuiState = ckswGuiState;
	}

	public void step(SimState simState) {
		double time = simState.schedule.getTime();

		if ((time >= Schedule.EPOCH) && (time < Schedule.AFTER_SIMULATION) & ((time % Cksw.chartUpdateRate) == 0)) {
			int totalWealth;
			
			totalWealth = 0;
			for (int i = 0; i < Cksw.commanders.size(); i++) {
				totalWealth += Cksw.commanders.get(i).wealth();
			}
			if (Cksw.commanders.size() > 0) {
				ckswGuiState.seriesWealthCommanders.add(time, totalWealth / Cksw.commanders.size(), true);
			}
			
			totalWealth = 0;
			for (int i = 0; i < Cksw.knowledge.size(); i++) {
				totalWealth += Cksw.knowledge.get(i).wealth();
			}
			if (Cksw.knowledge.size() > 0) {
				ckswGuiState.seriesWealthKnowledge.add(time, totalWealth / Cksw.knowledge.size(), true);
			}
			
			totalWealth = 0;
			for (int i = 0; i < Cksw.skilled.size(); i++) {
				totalWealth += Cksw.skilled.get(i).wealth();
			}
			if (Cksw.skilled.size() > 0) {
				ckswGuiState.seriesWealthSkilled.add(time, totalWealth / Cksw.skilled.size(), true);
			}
			
			totalWealth = 0;
			for (int i = 0; i < Cksw.workers.size(); i++) {
				totalWealth += Cksw.workers.get(i).wealth();
			}
			if (Cksw.workers.size() > 0) {
				ckswGuiState.seriesWealthWorkers.add(time, totalWealth / Cksw.workers.size(), true);
			}
		}
	}
	
}
