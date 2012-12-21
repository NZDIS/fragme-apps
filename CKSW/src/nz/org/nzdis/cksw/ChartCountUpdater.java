package nz.org.nzdis.cksw;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;

public class ChartCountUpdater implements Steppable {
	
	CkswWithUI ckswGuiState;
	
	public ChartCountUpdater(CkswWithUI ckswGuiState) {
		this.ckswGuiState = ckswGuiState;
	}

	public void step(SimState simState) {
		double time = simState.schedule.getTime();

		if ((time >= Schedule.EPOCH) && (time < Schedule.AFTER_SIMULATION) & ((time % Cksw.chartUpdateRate) == 0)) {
			ckswGuiState.seriesCountAgents.add(time, Cksw.agents.size(), true);
			ckswGuiState.seriesCountCommanders.add(time, Cksw.commanders.size(), true);
			ckswGuiState.seriesCountKnowledge.add(time, Cksw.knowledge.size(), true);
			ckswGuiState.seriesCountSkilled.add(time, Cksw.skilled.size(), true);
			ckswGuiState.seriesCountWorkers.add(time, Cksw.workers.size(), true);
		}
	}
	
}
