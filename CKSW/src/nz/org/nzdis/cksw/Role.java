package nz.org.nzdis.cksw;

import ec.util.MersenneTwister;

public abstract class Role {

	protected static final MersenneTwister random = new MersenneTwister();

	private String roleIdentifier;
	public String getRoleIdentifier() { return roleIdentifier; }
	
	public Role(String newIdentifier) { this.roleIdentifier = newIdentifier; }
	
	/**
	 * Perform the role
	 */
	public abstract void perform(Agent agent);
	
	/**
	 * Determine how valuable this role is or would be in the agent's current situation  
	 */
	//abstract double value(Agent agent);
	
	/**
	 * Determine the additional wealth due to the current role 
	 */
	public double wealth() { return 0.0; }
}
