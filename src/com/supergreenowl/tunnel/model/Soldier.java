package com.supergreenowl.tunnel.model;

public class Soldier {

	/**
	 * Default soldier speed (m/s).
	 */
	public static final float SPEED = 5f;
	
	/**
	 * Size a soldier occupies (m).
	 */
	public static final float DIAMETER = 6f;
	
	/**
	 * Half the size of a soldier (m).
	 */
	public static final float RADIUS = DIAMETER / 2f;
	
	/**
	 * Current state of the soldier.
	 */
	public SoldierState state;
	
	/**
	 * Time the soldier has been in its current state (s).
	 */
	public float stateTime;
	
	/**
	 * Direction from which soldier entered the tunnel.
	 */
	public final Direction direction;
	
	/**
	 * Type of this soldier.
	 */
	public final int type;
	
	/**
	 * Number of hitpoints the soldier has remaining.
	 */
	public int hp;
	
	/**
	 * Total original hit points for this soldier.
	 */
	public final int maxHp;
	
	/**
	 * Position of the soldier in the tunnel (m).
	 */
	public float position;
	
	/**
	 * Speed at which the soldier moves (m/s).
	 */
	public float speed;
	
	public Soldier(Direction direction, int type) {
		this(direction, type, SoldierType.HITPOINTS[type]);
	}
	
	public Soldier(Direction direction, int type, int hp) {
		this.direction = direction;
		this.type = type;
		this.maxHp = this.hp = hp;
		this.state = SoldierState.Stopped;
		this.position = Tunnel.NOT_IN_TUNNEL;
		this.speed = SPEED;
		
		// Soldiers entering from the east move in the opposite direction
		if(direction == Direction.East)
			this.speed *= -1f;
	}
	
	/**
	 * Sets the state of the soldier and resets the time in current state.
	 * @param state New state.
	 */
	public void setState(SoldierState state) {
		if(this.state != state) {
			this.state = state;
			this.stateTime = 0f;
		}
	}
	
}
