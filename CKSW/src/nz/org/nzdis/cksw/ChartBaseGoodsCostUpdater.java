package nz.org.nzdis.cksw;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;

public class ChartBaseGoodsCostUpdater implements Steppable {
	
	CkswWithUI ckswGuiState;
	
	public ChartBaseGoodsCostUpdater(CkswWithUI ckswGuiState) {
		this.ckswGuiState = ckswGuiState;
	}

	public void step(SimState simState) {
		double time = simState.schedule.getTime();

		if ((time >= Schedule.EPOCH) && (time < Schedule.AFTER_SIMULATION) && ((time % Cksw.chartUpdateRate) == 0)) {
			//ckswGuiState.seriesGoodsCost.add(time, Cksw.goodsCost, true);
		}
	}
	
}
