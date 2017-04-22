package com.supergreenowl.tunnel.controller;

/**
 * Listener that receives combat events from the game engine.
 * @author luke
 *
 */
public interface CombatListener {

	/**
	 * Event raised when a soldier is hit.
	 */
	void onHit();
	
	/**
	 * Event raised when a soldier dies.
	 */
	void onDeath();
	
}
