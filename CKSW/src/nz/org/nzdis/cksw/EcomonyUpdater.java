package nz.org.nzdis.cksw;

import sim.engine.SimState;
import sim.engine.Steppable;

public class EcomonyUpdater implements Steppable {

	@Override
	public void step(SimState simState) {
		double totalGoods;
		
		totalGoods = 0;
		for (int i = 0; i < Cksw.agents.size(); i++) {
			totalGoods += Cksw.agents.get(i).goods;
		}
		
		//Cksw.goodsCost = Cksw.totalMoney / totalGoods;
		System.out.println("Total goods: " + totalGoods);
	}

}
