package com.supergreenowl.tunnel.model;

import com.supergreenowl.tunnel.controller.CombatListener;

/**
 * Performs combat calculations.
 * @author luke
 *
 */
public class CombatEngine {
	
	/**
	 * Time it takes for a soldier to hit another (s).
	 */
	public static final float HIT_DURATION = 1f;
	
	/**
	 * Time it takes for a soldier to die (s).
	 */
	public static final float DYING_DURATION = 0.8f;


	/**
	 * The damage matrix for soldier types. Indexed as attacker, defender.
	 */
	public static final int[][] DAMAGE = new int[][] {
		{1,1,1,1,1,1,1}, // w
		{1,1,3,1,3,1,1}, // r
		{1,1,1,3,1,3,1}, // g
		{1,3,1,1,1,1,3}, // b
		{1,0,3,3,1,3,1}, // c
		{1,3,0,3,1,1,3}, // m
		{1,3,3,0,3,1,1} // y
	};

	/**
	 * Time the west soldier in combat last hit its opponent.
	 * Time is measured from when the soldier started fighting (in s).
	 */
	private float westHit;
	
	/**
	 * Time the east soldier in combat last hit its opponent.
	 * Time is measured from when the soldier started fighting (in s).
	 */
	private float eastHit;
	
	public CombatEngine() {
		westHit = eastHit = 0f;
	}
	
	/**
	 * Gets the two specified soldiers to fight.
	 * @param west
	 * @param east
	 */
	public void fight(Soldier west, Soldier east, CombatListener listener) {
		// Time since each combatant last hit their opponent
		float westTime = west.stateTime - westHit;
		float eastTime = east.stateTime - eastHit;
		
		// Determine if the soldiers are ready to hit again yet
		boolean isWestHitting = west.state == SoldierState.Hitting && westTime >= HIT_DURATION;
		boolean isEastHitting = east.state == SoldierState.Hitting && eastTime >= HIT_DURATION;
		
		// Combat is simultaneous so perform both attacks before checking if anyone died
		if(isWestHitting) {
			east.hp -= DAMAGE[west.type][east.type];
			westHit = west.stateTime;
		}
		
		if(isEastHitting) {
			west.hp -= DAMAGE[east.type][west.type];
			eastHit = east.stateTime;
		}
		
		// Check if west is dead
		if(west.hp <= 0) {
			west.setState(SoldierState.Dying);
			if(east.hp > 0) east.setState(SoldierState.Stopped); // east stops and waits for west to die
			westHit = eastHit = 0f; // Combat has ended so reset hit timers
		}
		
		if(east.hp <= 0) {
			east.setState(SoldierState.Dying);
			if(west.hp > 0) west.setState(SoldierState.Stopped);
			westHit = eastHit = 0f;
		}
		
		if(listener != null) {
			if(west.hp <= 0 || east.hp <= 0) {
				listener.onDeath();
			}
			else if(isWestHitting || isEastHitting) {
				listener.onHit();
			}
		}
	}

}
