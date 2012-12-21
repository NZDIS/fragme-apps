package nz.org.nzdis.cksw;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.data.xy.XYSeries;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.util.media.chart.TimeSeriesChartGenerator;

public class CkswWithUI extends GUIState {

	public Display2D display;
	public JFrame displayFrame;

	// Charts
	public XYSeries seriesWealthCommanders;
	public XYSeries seriesWealthKnowledge;
	public XYSeries seriesWealthSkilled;
	public XYSeries seriesWealthWorkers;
	TimeSeriesChartGenerator chartWealth;
	public XYSeries seriesCountAgents;
	public XYSeries seriesCountCommanders;
	public XYSeries seriesCountKnowledge;
	public XYSeries seriesCountSkilled;
	public XYSeries seriesCountWorkers;
	TimeSeriesChartGenerator chartCount;
	public XYSeries seriesGoodsCost;
	TimeSeriesChartGenerator chartBaseGoodsCost;

	/**
	 *  Application startup
	 * @param args Parameters passed to MASON from the command line during application startup
	 */
	public static void main(String[] args) {
		new CkswWithUI().createController();
	}

	/**
	 * Interface controller creation
	 */
	public CkswWithUI() { super(new Cksw(System.currentTimeMillis())); }

	/**
	 * Interface controller creation
	 * @param state Previous state to load
	 */
	public CkswWithUI(SimState state) { super(state); }

	/**
	 * Allow the user to inspect the model
	 */
	public Object getSimulationInspectedObject() { return state; }  // non-volatile

	/**
	 * Get the name of the simulation
	 * @return The name of the simulation
	 */
	public static String getName() { return "Cksw"; }

	/**
	 * Init
	 */
	@Override
	public void init(Controller controller) {
		super.init(controller);

		// Main display
		display = new Display2D(100.0, 100.0, this);
		displayFrame = display.createFrame();
		controller.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		// attach the portrayals from bottom to top
		//ajakjshga 
		display.setBackdrop(Color.white);

		// Wealth chart
		chartWealth = new sim.util.media.chart.TimeSeriesChartGenerator();
		chartWealth.setTitle("Wealth");
		chartWealth.setYAxisLabel("Average wealth");
		chartWealth.setXAxisLabel("Time");
		JFrame wealthFrame = chartWealth.createFrame(this);
		wealthFrame.setVisible(true);
		wealthFrame.pack();
		controller.registerFrame(wealthFrame);

		// Count chart
		chartCount = new sim.util.media.chart.TimeSeriesChartGenerator();
		chartCount.setTitle("Count");
		chartCount.setYAxisLabel("Count of roles");
		chartCount.setXAxisLabel("Time");
		JFrame countFrame = chartCount.createFrame(this);
		countFrame.setVisible(true);
		countFrame.pack();
		controller.registerFrame(countFrame);

		// Base goods cost chart
		chartBaseGoodsCost = new sim.util.media.chart.TimeSeriesChartGenerator();
		chartBaseGoodsCost.setTitle("Base Goods Cost");
		chartBaseGoodsCost.setYAxisLabel("Base Cost of Goods");
		chartBaseGoodsCost.setXAxisLabel("Time");
		JFrame baseGoodsCostFrame = chartBaseGoodsCost.createFrame(this);
		baseGoodsCostFrame.setVisible(true);
		baseGoodsCostFrame.pack();
		controller.registerFrame(baseGoodsCostFrame);
	}

	/**
	 * Start the UI
	 */
	@Override
	public void start() {
		super.start();
		setupPortrayals();

		chartWealth.removeAllSeries();
		seriesWealthCommanders = new org.jfree.data.xy.XYSeries("Commanders", false);
		seriesWealthKnowledge = new org.jfree.data.xy.XYSeries("Knowledge", false);
		seriesWealthSkilled = new org.jfree.data.xy.XYSeries("Skilled", false);
		seriesWealthWorkers = new org.jfree.data.xy.XYSeries("Workers", false);
		chartWealth.addSeries(seriesWealthCommanders, null);
		chartWealth.addSeries(seriesWealthKnowledge, null);
		chartWealth.addSeries(seriesWealthSkilled, null);
		chartWealth.addSeries(seriesWealthWorkers, null);
		scheduleRepeatingImmediatelyAfter(new ChartWealthUpdater(this));

		chartCount.removeAllSeries();
		seriesCountAgents = new org.jfree.data.xy.XYSeries("Agents", false);
		seriesCountCommanders = new org.jfree.data.xy.XYSeries("Commanders", false);
		seriesCountKnowledge = new org.jfree.data.xy.XYSeries("Knowledge", false);
		seriesCountSkilled = new org.jfree.data.xy.XYSeries("Skilled", false);
		seriesCountWorkers = new org.jfree.data.xy.XYSeries("Workers", false);
		chartCount.addSeries(seriesCountAgents, null);
		chartCount.addSeries(seriesCountCommanders, null);
		chartCount.addSeries(seriesCountKnowledge, null);
		chartCount.addSeries(seriesCountSkilled, null);
		chartCount.addSeries(seriesCountWorkers, null);
		scheduleRepeatingImmediatelyAfter(new ChartCountUpdater(this));

		chartBaseGoodsCost.removeAllSeries();
		seriesGoodsCost = new org.jfree.data.xy.XYSeries("Base Goods Cost", false);
		chartBaseGoodsCost.addSeries(seriesGoodsCost, null);
		scheduleRepeatingImmediatelyAfter(new ChartBaseGoodsCostUpdater(this));
	}

	/**
	 * Start the UI
	 * @param state Previous state to load
	 */
	@Override
	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}

	/**
	 * Set up the portrayals - the graphical representations of the data
	 */
	public void setupPortrayals() {
		//Cksw cksw = (Cksw)state;

		// reschedule the displayer
		display.reset();

		// redraw the display
		display.repaint();
	}
}
