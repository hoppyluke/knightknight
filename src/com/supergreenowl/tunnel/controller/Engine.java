package com.supergreenowl.tunnel.controller;

import com.badlogic.gdx.utils.Array;
import com.supergreenowl.tunnel.model.CombatEngine;
import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.Soldier;
import com.supergreenowl.tunnel.model.SoldierState;
import com.supergreenowl.tunnel.model.SoldierType;
import com.supergreenowl.tunnel.model.Tunnel;
import com.supergreenowl.tunnel.model.TunnelState;

/**
 * Main game engine.
 * @author luke
 *
 */
public class Engine {

	/** Duration of pre-game pause in seconds. */
	public static final float PRE_GAME_PAUSE = 3f;
	
	/** Time at which a soldier will be automatically dispatched if none has been yet (s). */
	public static final float AUTO_DISPATCH_TIME = 5f;
	
	/** Frequency of score updates in seconds. */
	private static final float SCORE_UPDATE_FREQUENCY = 0.5f;
	
	private static final int SCORE_MULTIPLIER = 1000;
	private static final int POINTS_PER_SECOND = 100;
	
	public Tunnel tunnel;
	
	/** Indicates whether or not the western player has dispatched any soldiers yet. */
	public boolean isDispatchedWest;
	/** Indicates whether or not the eastern player has dispatched any soldiers yet. */
	public boolean isDispatchedEast;
	
	/** Time since game began in seconds. */
	public float gameTime;
	
	/** Player at the west end of the tunnel. */
	public Player west;
	
	/** Player at the east end of the tunnel. */
	public Player east;
	
	/** Time that the game has been paused for in seconds.  */
	public float pausedTime;
	
	/** Indicates if this engine is still in the pre-game pause state. */ 
	public boolean isPaused;
	
	/**
	 * Listener that receives combat events from this engine.
	 */
	public CombatListener listener;
	
	private CombatEngine combat;
	private float scoreLastUpdated;
		
	public Engine(Player west, Player east) {		
		gameTime = 0f;
		scoreLastUpdated = 0f;
		tunnel = new Tunnel();
		combat = new CombatEngine();
		isDispatchedWest = isDispatchedEast = false;
		
		this.west = west;
		this.east = east;
		west.setEngine(this);
		east.setEngine(this);
		
		this.isPaused = true;
		this.pausedTime = 0f;
	}
	
	/**
	 * Updates the game state.
	 * @param elapsed
	 */
	public void update(float elapsed) {
		
		if(isPaused) {
			if((pausedTime += elapsed) >= PRE_GAME_PAUSE) {
				isPaused = false;
				elapsed =  pausedTime - PRE_GAME_PAUSE;
			}
			else return; // Still paused!
		}
		
		if(tunnel.state != TunnelState.Open) return;
		
		gameTime += elapsed;
		
		// Deduct points for elapsed time
		if(gameTime - scoreLastUpdated >= SCORE_UPDATE_FREQUENCY) {
			west.score -= elapsed * POINTS_PER_SECOND;
			if(west.score < 0) west.score = 0;
			east.score -= elapsed * POINTS_PER_SECOND;
			if(east.score < 0) east.score = 0;
			scoreLastUpdated = gameTime;
		}
		
		Array<Soldier> westSoldiers = tunnel.west;
		Array<Soldier> eastSoldiers = tunnel.east;
		
		int w = westSoldiers.size;
		int e = eastSoldiers.size;
		
		// There is no one in the tunnel - if there are no waiting soldiers either then it's a draw
		if(w == 0 && e == 0) {
			if(west.totalSoldiers() == 0 && east.totalSoldiers() == 0) {
				tunnel.state = TunnelState.Draw;
				return;
			}
		}
		
		// Let AI play
		west.play(gameTime);
		east.play(gameTime);
		
		// Auto-dispatch to prevent players from waiting for ever for their opponent to make the first move
		if(!isDispatchedWest && gameTime >= AUTO_DISPATCH_TIME) {
			int type = west.selectRandomSoldier();
			if(type != Player.NONE_SELECTED) {
				dispatchSoldier(Direction.West, type);
			}
		}
		
		if(!isDispatchedEast && gameTime >= AUTO_DISPATCH_TIME) {
			int type = east.selectRandomSoldier();
			if(type != Player.NONE_SELECTED) {
				dispatchSoldier(Direction.East, type);
			}
		}
		
		int len = Math.max(w, e);
		float wprev = Tunnel.NOT_IN_TUNNEL;
		float eprev = Tunnel.NOT_IN_TUNNEL;
		
		// Update soldiers alternating west then east from the front of each army
		// Move soldiers
		for(int i = 0; i < len; i++) {
			
			if(w > 0 && w > i) {
				// update west.get(i)
				Soldier s = westSoldiers.get(i);
				SoldierState ss = s.state;
				s.stateTime += elapsed;
				
				// Move a walking soldier
				float pos = s.position;
				if(ss == SoldierState.Walking) {
					pos += s.speed * elapsed;
					s.position = pos;
					
					if(pos >= Tunnel.LENGTH + Soldier.RADIUS) {
						tunnel.state = TunnelState.West;
						return;
					}
				}
				
				// Check for dead soldiers
				if(ss == SoldierState.Dying && s.stateTime >= CombatEngine.DYING_DURATION) {
					s.setState(SoldierState.Dead);
				}
				
				// Check for soldier waiting on dying soldier
				if(i == 0 && ss == SoldierState.Stopped) {
					if(e == 0) {
						// Dying soldier has been cleaned up and there are no enemies left - so walk again
						s.setState(SoldierState.Walking);
					}
					else if(eastSoldiers.get(0).state != SoldierState.Dying) {
						// Soldier in front is dead already (and might have been removed) - so walk again 
						s.setState(SoldierState.Walking);
					}
				}
				
				// Check collision with soldier in front
				if(wprev != Tunnel.NOT_IN_TUNNEL) {
					float distance = wprev - pos;
					
					if(distance <= Soldier.DIAMETER && ss != SoldierState.Stopped) {
						// stop if caught up to soldier ahead
						s.setState(SoldierState.Stopped);
					}
					else if(distance > Soldier.DIAMETER && ss == SoldierState.Stopped) {
						// resume moving if soldier ahead is now far enough away
						s.setState(SoldierState.Walking);
					}
				}
				
				wprev = pos; // Set previous position to position of this soldier
			}
			
			if(e > 0 && e > i) {
				// update east.get(i)
				Soldier s = eastSoldiers.get(i);
				SoldierState ss = s.state;
				s.stateTime += elapsed;
				
				// Move a walking soldier
				float pos = s.position;
				if(ss == SoldierState.Walking) {
					pos += s.speed * elapsed;
					s.position = pos;
					
					if(pos <= 0f - Soldier.RADIUS) {
						tunnel.state = TunnelState.East;
						return;
					}
				}
				
				// Check for dead soldiers
				if(ss == SoldierState.Dying && s.stateTime >= CombatEngine.DYING_DURATION) {
					s.setState(SoldierState.Dead);
				}
								
				// Check for soldier waiting on dying soldier
				if(i == 0 && ss == SoldierState.Stopped) {
					if(w == 0) {
						// Dying soldier has been cleaned up and there are no enemies left - so walk again
						s.setState(SoldierState.Walking);
					}
					else if(westSoldiers.get(0).state != SoldierState.Dying) {
						// Soldier in front is dead already (and might have been removed) - so walk again 
						s.setState(SoldierState.Walking);
					}
				}
				
				// Check collision with soldier in front
				if(eprev != Tunnel.NOT_IN_TUNNEL) {
					float distance = pos - eprev;
					
					if(distance <= Soldier.DIAMETER && ss != SoldierState.Stopped) {
						// stop if caught up to soldier ahead
						s.setState(SoldierState.Stopped);
					}
					else if(distance >= Soldier.DIAMETER && ss == SoldierState.Stopped) {
						// resume moving if soldier ahead is now far enough away
						s.setState(SoldierState.Walking);
					}
				}
				
				eprev = pos; // Set previous position to position of this soldier
			}
			
		}
		
		// Remove dead soldiers
		// This must be before combat as we want to check first live soldier for combat
		while(westSoldiers.size > 0 && westSoldiers.get(0).state == SoldierState.Dead) westSoldiers.removeIndex(0);
		while(eastSoldiers.size > 0 && eastSoldiers.get(0).state == SoldierState.Dead) eastSoldiers.removeIndex(0);
		
		// Check if combat should occur
		if(westSoldiers.size > 0 && eastSoldiers.size > 0) {
			Soldier westLead = westSoldiers.get(0);
			Soldier eastLead = eastSoldiers.get(0);
			
			if(westLead.state == SoldierState.Walking || eastLead.state == SoldierState.Walking) {
				float distance = eastLead.position - westLead.position;
				if(distance <= Soldier.DIAMETER) {
					westLead.setState(SoldierState.Hitting);
					eastLead.setState(SoldierState.Hitting);
				}
			}
			
			if(westLead.state == SoldierState.Hitting || eastLead.state == SoldierState.Hitting) {
				combat.fight(westLead, eastLead, listener);
				
				// Assign points for killing opponents
				if(westLead.state == SoldierState.Dying) {
					west.score -= SoldierType.COST[westLead.type] * SCORE_MULTIPLIER;
					if(west.score < 0) west.score = 0;
					east.score += SoldierType.COST[westLead.type] * SCORE_MULTIPLIER;
				}
				
				if(eastLead.state == SoldierState.Dying) {
					east.score -= SoldierType.COST[eastLead.type] * SCORE_MULTIPLIER;
					if(east.score < 0) east.score = 0;
					west.score += SoldierType.COST[eastLead.type] * SCORE_MULTIPLIER;
				}
			}
		}
	}
	
	/**
	 * Sends a soldier for one of the players into the tunnel if possible.
	 * @param direction Direction of the player to send a soldier for.
	 * @param soldierType Type of soldier to dispatch.
	 */
	public void dispatchSoldier(Direction direction, int soldierType) {
		Player player = direction == Direction.West ? west : east;
		
		if(player.soldiers[soldierType] > 0) {
			Soldier s = new Soldier(player.direction, soldierType);
			player.soldiers[soldierType]--;
			tunnel.enter(s);
			
			// If there is a CPU player then notify the AI that a soldier has been dispatched by it's opponent
			// Record that a soldier has been sent by this player to prevent auto-dispatch
			if(direction == Direction.West) {
				east.notify(soldierType, gameTime);
				isDispatchedWest = true;
			}
			else {
				west.notify(soldierType, gameTime);
				isDispatchedEast = true;
			}
			
		}
	}
	
}
