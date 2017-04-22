package com.supergreenowl.tunnel.model;

/**
 * Possible different states a soldier may be in.
 * @author luke
 *
 */
public enum SoldierState {
	
	/**
	 * State for a soldier that is stopped and waiting for the soldier in front.
	 * The soldier could be waiting for an enemy to die or a friendly soldier to walk out the way.
	 */
	Stopped,
	
	/**
	 * State for a soldier that is walking down the tunnel.
	 */
	Walking,
	
	/**
	 * State for a soldier that is in combat.
	 */
	Hitting,
	
	/**
	 * State for a soldier that has lost combat and is dying.
	 */
	Dying,
	
	/**
	 * State for a soldier that is dead already.
	 */
	Dead
}
