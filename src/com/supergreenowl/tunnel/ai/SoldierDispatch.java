package com.supergreenowl.tunnel.ai;

/**
 * Point in the game at which the opponent dispatched a soldier into the tunnel.
 * @author luke
 *
 */
public class SoldierDispatch {
	
	public static final int NONE = Integer.MIN_VALUE;
	
	public float time;
	public int type;
	
	public SoldierDispatch() {
		this(NONE, 0f);
	}
	
	public SoldierDispatch(int type, float time) {
		this.type = type;
		this.time = time;
	}
	
	public void set(int type, float time) {
		this.type = type;
		this.time = time;
	}
}