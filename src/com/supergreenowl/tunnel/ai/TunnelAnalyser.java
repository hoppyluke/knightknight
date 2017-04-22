package com.supergreenowl.tunnel.ai;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.supergreenowl.tunnel.model.CombatEngine;
import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.Soldier;
import com.supergreenowl.tunnel.model.Tunnel;
import com.supergreenowl.tunnel.utils.PooledQueue;

/**
 * Analyses a tunnel to determine which soldiers will survive combat.
 * @author luke
 *
 */
public class TunnelAnalyser {

	private static class SoldierPool extends Pool<SoldierOutcome> {
		public SoldierPool(int capacity) {
			super(capacity);
		}
		
		@Override
		protected SoldierOutcome newObject() {
			return new SoldierOutcome();
		}
	}
	
	public static class SoldierOutcome {
		int type;
		int hp;
		float position;
		Direction direction;
		
		public void set(Soldier s) {
			this.type = s.type;
			this.direction = s.direction;
			this.hp = s.hp;
			this.position = s.position;
		}
	}
	
	private Pool<SoldierOutcome> pool;
	private PooledQueue<SoldierOutcome> west;
	private PooledQueue<SoldierOutcome> east;
	private Array<SoldierOutcome> outcome;
	
	public TunnelAnalyser() {
		pool = new SoldierPool(10);
		west = new PooledQueue<SoldierOutcome>();
		east = new PooledQueue<SoldierOutcome>();
		outcome = new Array<SoldierOutcome>();
	}
	
	/**
	 * Analyses the soldiers currently in a tunnel and determines which soldiers will survive.
	 * @param tunnel Tunnel to analyse.
	 * @return Collection of soldier outcomes for those soldiers that will survive. Note that the same instance is returned by each call.
	 */
	public Array<SoldierOutcome> analyse(Tunnel tunnel) {
		west.clear();
		east.clear();
		outcome.clear();
		
		add(tunnel.west);
		add(tunnel.east);
		
		while(!west.isEmpty() && !east.isEmpty()) {
			SoldierOutcome w = west.peek();
			SoldierOutcome e = east.peek();
			
			analyse(w, e);
			
			if(w.hp <= 0) west.poll();
			if(e.hp <= 0) east.poll();
		}
		
		// Add all surviving soldiers to the outcome collection
		SoldierOutcome s = west.poll();
		while(s != null) {
			outcome.add(s);
			s = west.poll();
		}
		
		s = east.poll();
		while(s != null) {
			outcome.add(s);
			s = east.poll();
		}
		
		return outcome;
	}
	
	/**
	 * Analyses combat between 2 soldiers and updates their hitpoints.
	 * @param west
	 * @param east
	 */
	private void analyse(SoldierOutcome west, SoldierOutcome east) {
		while(west.hp > 0 && east.hp > 0) {
			west.hp -= CombatEngine.DAMAGE[east.type][west.type];
			east.hp -= CombatEngine.DAMAGE[west.type][east.type];
		}
	}
	
	/**
	 * Adds multiple soldiers to the collection for analysis.
	 * @param soldiers
	 */
	private void add(Iterable<Soldier> soldiers) {
		for(Soldier s : soldiers) {
			SoldierOutcome outcome = pool.obtain();
			outcome.set(s);
			if(s.direction == Direction.West) west.add(outcome);
			else east.add(outcome);
		}
	}
}
