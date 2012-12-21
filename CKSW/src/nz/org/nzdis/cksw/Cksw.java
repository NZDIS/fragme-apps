package nz.org.nzdis.cksw;

import java.util.ArrayList;

import sim.engine.SimState;

public class Cksw extends SimState {
	
	private static final int initialNumberOfCommunities = 1;
	private static final int initialNumberOfCriminals = 0;
	
	public static final String ROLE_COMMANDER = "Commander";
	public static final String ROLE_KNOWLEDGE = "Knowledge";
	public static final String ROLE_SKILLED = "Skilled";
	public static final String ROLE_WORKER = "Worker";
	public static final String ROLE_CRIMINAL = "Criminal";
	
	public static double time = 0.0;
	
	public static final int SCHEDULE_ORDERING_CONSUMPTION = 1000;
	public static final int SCHEDULE_ORDERING_AGENT = 2000;
	
	public static final double randomAbilityRange = 0.1;
	
	public static final double chartUpdateRate = 1;
	
	public static final double baseGoodsAmount = 100.0;
	
	// ID
	private static int idCounter = 1;
	public static int newId() { return idCounter++; }
	
	// Agent collections
	public static ArrayList<Community> communities = new ArrayList<Community>();
	public static ArrayList<Agent> agents = new ArrayList<Agent>();
	public static ArrayList<Agent> commanders = new ArrayList<Agent>();
	public static ArrayList<Agent> knowledge = new ArrayList<Agent>();
	public static ArrayList<Agent> skilled = new ArrayList<Agent>();
	public static ArrayList<Agent> workers = new ArrayList<Agent>();
	public static ArrayList<Agent> criminals = new ArrayList<Agent>();
	
	/**
	 * Application startup
	 * @param args Parameters passed to MASON from the command line during application startup
	 */
	public static void main(String[] args) {
		doLoop(Cksw.class, args);
		System.exit(0);
	}

	/**
	 * Sim creation
	 * @param seed Random number generator seed (use System.currentTimeMillis() if you want it to be random)
	 */
	public Cksw(long seed) { 
		super(System.currentTimeMillis());
	}

	/**
	 * Sim startup
	 */
	@Override
	public void start() {
		super.start();
		
		schedule.scheduleRepeating(new EcomonyUpdater(), SCHEDULE_ORDERING_CONSUMPTION, 1.0);

		// Initial communities
		for (int i = 0; i < initialNumberOfCommunities; i++) {
			Community community = new Community(this);
			communities.add(community);
		}

		// Initial criminals
		for (int i = 0; i < initialNumberOfCriminals; i++) {
			Agent agent = new Agent(newId(), new RoleCriminal());
			agent.setStopper(schedule.scheduleRepeating(agent, SCHEDULE_ORDERING_AGENT, 1.0));
			criminals.add(agent);
			agents.add(agent);
		}
		
		
	}

	/**
	 * Sim shutdown
	 */
	@Override
	public void finish() {
		super.finish();
	}
}
