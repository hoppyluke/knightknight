package com.supergreenowl.tunnel.model;

import com.badlogic.gdx.utils.Array;

public class Tunnel {

	/**
	 * Length of the tunnel in metres.
	 */
	public static final float LENGTH = 50f;
	
	/**
	 * Constant indicating a position not in the tunnel.
	 */
	public static final float NOT_IN_TUNNEL = Float.MIN_VALUE;
	
	/**
	 * Soldiers in the tunnel moving from west to east.
	 */
	public Array<Soldier> west;
	
	/**
	 * Soldiers in the tunnel moving from east to west.
	 */
	public Array<Soldier> east;
	
	public TunnelState state;
	
	public Tunnel() {
		west = new Array<Soldier>(true, 16);
		east = new Array<Soldier>(true, 16);
		state = TunnelState.Open;
	}
	
	/**
	 * Puts a soldier in the tunnel at the correct end and starts them walking.
	 * @param s
	 */
	public void enter(Soldier s) {
		
		s.setState(SoldierState.Walking);
		
		if(s.direction == Direction.West) {
			s.position = 0f - Soldier.RADIUS;
			west.add(s);
		}
		else if(s.direction == Direction.East) {
			s.position = LENGTH + Soldier.RADIUS;
			east.add(s);
		}
	
	}
		
}
