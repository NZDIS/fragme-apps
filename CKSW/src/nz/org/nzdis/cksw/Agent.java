package nz.org.nzdis.cksw;

import java.util.ArrayList;

import ec.util.MersenneTwister;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;

public class Agent implements Steppable {

	public static final double goodsConsumedPerTurn = 100.0;
	private static final double initialGoods = goodsConsumedPerTurn * 10.0;
	private static final double initialGoodsVariation = goodsConsumedPerTurn * 0.2;
	
	// Private random number generator
	protected static final MersenneTwister random = new MersenneTwister();

	// Unique id
	private int id = 0;
	public int id() { return id; }
	
	// Stopper used to kill this agent
	private Stoppable stopper;
	public void setStopper(Stoppable stoppable) { this.stopper = stoppable; }
	public Stoppable stopper() { return stopper; }
	
	// Properties
	private Role role = null;
	private ArrayList<Role> oldRoles = new ArrayList<Role>();
	//private double entrepreneurship = 0;
	
	/////////////////////////////////////////////////////////////////////////////////////
	// Resources
	public double goods = initialGoods + random.nextTrimmedScaledGaussian() * initialGoodsVariation;
	public ToolBox toolBox = new ToolBox();
	public double knowledge = 1.0;
	
	/////////////////////////////////////////////////////////////////////////////////////
	// Wealth
	/**
	 * Determine the wealth of the agent
	 * @return The total goods plus the value of all unused tools
	 */
	public double wealth() {
		return goods + toolBox.numberOfTools() * Tool.cost();
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// Constructor
	/**
	 * Create a new agent
	 * @param initialRole The initial role of this agent, or null if it has none
	 */
	public Agent(int id, Role initialRole) {
		this.id = id;
		if (initialRole != null) {
			this.role = initialRole;
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// Role
	/**
	 * Test if an agent performs the specified role
	 * @param roleIdentifier The role to check for
	 * @return True if the agent performs the role with the specified identifier, otherwise false
	 */
	public boolean performsRole(String roleIdentifier) {
		if (role.getRoleIdentifier().equals(roleIdentifier)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Give this agent a new role
	 * @param newRole The new role that the agent should perform
	 */
	public void setRole(Role newRole) {
		oldRoles.add(this.role);
		this.role = newRole;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// Choose Role
	/**
	 * Choose the best role for this agent and switch to it
	 */
	public void chooseRole() {
		String bestRoleIdentifier = role.getRoleIdentifier();
		double bestRoleValue = 0.0;
		double roleValue = 0.0;
		
		roleValue = RoleWorker.value(this);
		if (roleValue > bestRoleValue) {
			bestRoleValue = roleValue;
			bestRoleIdentifier = Cksw.ROLE_WORKER;
		}
		
		roleValue = RoleSkilled.value(this);
		if (roleValue > bestRoleValue) {
			bestRoleValue = roleValue;
			bestRoleIdentifier = Cksw.ROLE_SKILLED;
		}
		
		if (!bestRoleIdentifier.equals(role.getRoleIdentifier())) {
			//System.out.print("Changing role from " + role.getRoleIdentifier());
			if (role.getClass() == RoleCommander.class) {
				Cksw.commanders.remove(this);
			} else if (role.getClass() == RoleKnowledge.class) {
				Cksw.knowledge.remove(this);
			} else if (role.getClass() == RoleSkilled.class) {
				Cksw.skilled.remove(this);
			} else if (role.getClass() == RoleWorker.class) {
				Cksw.workers.remove(this);
			} else if (role.getClass() == RoleCriminal.class) {
				Cksw.criminals.remove(this);
			}
			if (bestRoleIdentifier.equals(Cksw.ROLE_COMMANDER)) {
				setRole(new RoleCommander());
				Cksw.commanders.add(this);
			} else if (bestRoleIdentifier.equals(Cksw.ROLE_KNOWLEDGE)) {
				setRole(new RoleKnowledge());
				Cksw.knowledge.add(this);
			} else if (bestRoleIdentifier.equals(Cksw.ROLE_SKILLED)) {
				setRole(new RoleSkilled(ToolTypes.getStartupRandomToolType()));
				Cksw.skilled.add(this);
			} else if (bestRoleIdentifier.equals(Cksw.ROLE_WORKER)) {
				setRole(new RoleWorker());
				Cksw.workers.add(this);
			} else if (bestRoleIdentifier.equals(Cksw.ROLE_CRIMINAL)) {
				setRole(new RoleCriminal());
				Cksw.criminals.add(this);
			}
			//System.out.println(" to " + role.getRoleIdentifier());
		}
	}

	
	/////////////////////////////////////////////////////////////////////////////////////
	// Step
	/**
	 * Update this agent for the current time step - execute its role
	 */
	@Override
	public void step(SimState simState) {
		/////////////////////////////////////////////////////////////////////////////////////
		// Choose the best role
		//chooseRole();
		
		/////////////////////////////////////////////////////////////////////////////////////
		// Use the role
		role.perform(this);
		
		/////////////////////////////////////////////////////////////////////////////////////
		// Consume goods
		if (goods >= goodsConsumedPerTurn) {
			goods -= goodsConsumedPerTurn;
		} else {
			System.err.println("Starvation at time: " + simState.schedule.getSteps() + " id: " + this.id + " role: " + this.role.getRoleIdentifier());
			this.kill();
		}
		
		/////////////////////////////////////////////////////////////////////////////////////
		// Knowledge wears out
		// This is calculated from the old value and the known knowledge people
	}
	
	
	public void kill() {
		System.err.println("Goods: " + goods + " Tools: " + toolBox.numberOfTools());
		if (role.getClass() == RoleCommander.class) {
			Cksw.commanders.remove(this);
		} else if (role.getClass() == RoleKnowledge.class) {
			Cksw.knowledge.remove(this);
		} else if (role.getClass() == RoleSkilled.class) {
			Cksw.skilled.remove(this);
		} else if (role.getClass() == RoleWorker.class) {
			Cksw.workers.remove(this);
		} else if (role.getClass() == RoleCriminal.class) {
			Cksw.criminals.remove(this);
		}
		Cksw.agents.remove(this);
		stopper.stop();
	}

}


