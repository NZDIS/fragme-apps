package nz.org.nzdis.cksw;

import java.util.ArrayList;

public class Community {
	
	private static final int initialNumberOfCommanders = 1;
	private static final int initialNumberOfKnowledge = 5;
	private static final int initialNumberOfSkilled = 10;
	private static final int initialNumberOfWorkers = 100;

	public static ArrayList<Agent> agents = new ArrayList<Agent>();
	public static ArrayList<Agent> commanders = new ArrayList<Agent>();
	public static ArrayList<Agent> knowledge = new ArrayList<Agent>();
	public static ArrayList<Agent> skilled = new ArrayList<Agent>();
	public static ArrayList<Agent> workers = new ArrayList<Agent>();

	public Agent boss() {
		if (commanders.size() > 0) {
			return commanders.get(0);
		}
		return null;
	}
	
	public Community(Cksw cksw) {
		
		// Initial commanders
		for (int i = 0; i < initialNumberOfCommanders; i++) {
			Agent agent = new Agent(Cksw.newId(), new RoleCommander());
			agent.setStopper(cksw.schedule.scheduleRepeating(agent, Cksw.SCHEDULE_ORDERING_AGENT, 1.0));
			Cksw.commanders.add(agent);
			Cksw.agents.add(agent);
			commanders.add(agent);
			agents.add(agent);
		}
		
		// Initial knowledge
		for (int i = 0; i < initialNumberOfKnowledge; i++) {
			Agent agent = new Agent(Cksw.newId(), new RoleKnowledge());
			agent.setStopper(cksw.schedule.scheduleRepeating(agent, Cksw.SCHEDULE_ORDERING_AGENT, 1.0));
			Cksw.knowledge.add(agent);
			Cksw.agents.add(agent);
			knowledge.add(agent);
			agents.add(agent);
		}
		
		// Initial skilled
		for (int i = 0; i < initialNumberOfSkilled; i++) {
			Agent agent = new Agent(Cksw.newId(), new RoleSkilled(0));
			agent.setStopper(cksw.schedule.scheduleRepeating(agent, Cksw.SCHEDULE_ORDERING_AGENT, 1.0));
			Cksw.skilled.add(agent);
			Cksw.agents.add(agent);
			skilled.add(agent);
			agents.add(agent);
		}
		
		// Initial workers
		for (int i = 0; i < initialNumberOfWorkers; i++) {
			Agent agent = new Agent(Cksw.newId(), new RoleWorker());
			agent.setStopper(cksw.schedule.scheduleRepeating(agent, Cksw.SCHEDULE_ORDERING_AGENT, 1.0));
			Cksw.workers.add(agent);
			Cksw.agents.add(agent);
			workers.add(agent);
			agents.add(agent);
		}
		
	}
}
