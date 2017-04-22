package com.supergreenowl.tunnel.analyser;

import java.util.Queue;

import com.supergreenowl.tunnel.model.CombatEngine;
import com.supergreenowl.tunnel.model.Soldier;
import com.supergreenowl.tunnel.model.TunnelState;

public class CombatAnalyser {
	
	public static TunnelState fight(Queue<Soldier> west, Queue<Soldier> east) {
		
		// Let the two armies fight each other until at least 1 is out of soldiers
		while(!west.isEmpty() && !east.isEmpty()) {
			Soldier w = west.peek();
			Soldier e = east.peek();
			
			// Let the two soldiers fight each other until at least one dies
			fight(w, e);
			
			// Remove any dead soldiers
			if(w.hp <= 0) west.remove();
			if(e.hp <= 0) east.remove();
		}
		
		// Determine winner
		if(west.isEmpty()) {
			if(east.isEmpty()) return TunnelState.Draw;
			else return TunnelState.East;
		}
		else return TunnelState.West;
	}
	
	
	/**
	 * Runs a fight between two soldiers.
	 * @param west
	 * @param east
	 */
	private static void fight(Soldier west, Soldier east) {
		while(west.hp > 0 && east.hp > 0) {
			west.hp -= CombatEngine.DAMAGE[east.type][west.type];
			east.hp -= CombatEngine.DAMAGE[west.type][east.type];
		}
	}	
}
