package com.supergreenowl.tunnel.ai;

import com.badlogic.gdx.utils.Array;
import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.Soldier;
import com.supergreenowl.tunnel.model.SoldierType;
import com.supergreenowl.tunnel.model.Tunnel;
import com.supergreenowl.tunnel.utils.IntSet;

public class HardAI extends AI {

	private static final float REACTION_SPEED = 1.5f;
	
	/** Distance from the end of the tunnel at which a soldier becomes a threat. */
	private static final float DANGER_ZONE = Tunnel.LENGTH - (Soldier.SPEED * REACTION_SPEED * 2f);	
	
	/** Number of soldiers considered when choosing what to send. */
	private static final int SOLDIERS = 3;
	
	private SelectionRule anySoldier;
	private TunnelAnalyser analyser;
	
	private float dangerZone;
	
	private IntSet[] sets;
	private IntSet workingSet;

	private SelectionRule[][] rules;
	
	public HardAI(Direction direction, SelectionStrategy selectionStrategy) {
		super(direction, REACTION_SPEED, selectionStrategy);
		analyser = new TunnelAnalyser();
		anySoldier = new SelectionRule();
		
		sets = new IntSet[] {
				new IntSet(SoldierType.RED, SoldierType.GREEN, SoldierType.BLUE, SoldierType.CYAN, SoldierType.MAGENTA, SoldierType.YELLOW), // w
				new IntSet(SoldierType.BLUE, SoldierType.MAGENTA, SoldierType.YELLOW), // r
				new IntSet(SoldierType.RED, SoldierType.CYAN, SoldierType.YELLOW), // g
				new IntSet(SoldierType.GREEN, SoldierType.MAGENTA, SoldierType.CYAN), // b
				new IntSet(SoldierType.RED, SoldierType.YELLOW), // c
				new IntSet(SoldierType.GREEN, SoldierType.CYAN), // m
				new IntSet(SoldierType.BLUE, SoldierType.MAGENTA) // y
		};
		workingSet = new IntSet(SoldierType.COUNT);
		
		if(direction == Direction.West) dangerZone = DANGER_ZONE;
		else dangerZone = Tunnel.LENGTH - DANGER_ZONE;
				
		SelectionRule whites3 = new SelectionRule(SoldierType.WHITE, 3);
		rules = new SelectionRule[][] {
			new SelectionRule[] { new SelectionRule(SoldierType.WHITE), anySoldier }, // W
			new SelectionRule[] { whites3, new SelectionRule(SoldierType.RED), new SelectionRule(SoldierType.WHITE, SoldierType.GREEN, 3) }, // R
			new SelectionRule[] { whites3, new SelectionRule(SoldierType.GREEN), new SelectionRule(SoldierType.WHITE, SoldierType.BLUE, 3) }, // G
			new SelectionRule[] { whites3, new SelectionRule(SoldierType.BLUE), new SelectionRule(SoldierType.WHITE, SoldierType.RED, 3) }, // B
			new SelectionRule[] { whites3, new SelectionRule(SoldierType.CYAN), new SelectionRule(SoldierType.WHITE, new int[] { SoldierType.GREEN, SoldierType.BLUE, SoldierType.MAGENTA }, 3) }, // C
			new SelectionRule[] { whites3, new SelectionRule(SoldierType.MAGENTA), new SelectionRule(SoldierType.WHITE, new int[] { SoldierType.RED, SoldierType.BLUE, SoldierType.YELLOW }, 3) }, // M
			new SelectionRule[] { whites3, new SelectionRule(SoldierType.YELLOW), new SelectionRule(SoldierType.WHITE, new int[] { SoldierType.RED, SoldierType.GREEN, SoldierType.CYAN }, 3) } // Y
		};
	}

	@Override
	protected void onPreAutoDispatch(float gameTime) {
		
		Array<TunnelAnalyser.SoldierOutcome> outcome = analyser.analyse(engine.tunnel);
		if(outcome.size > 0) {
			
			// If opponent has sent anything then choose something to counter that
			// Work out if there are any soldier types that are effective against all the enemy soldiers in the tunnel
			sendSoldiers(outcome, gameTime);
		}
		else {
			// Avoid auto-dispatch by choosing either a white soldier or whatever this player has most of to send
			if(soldiers[SoldierType.WHITE] > 0) send(SoldierType.WHITE);
			else if(anySoldier.match(soldiers)) {
				send(anySoldier.selection, gameTime);
			}
		}
	}
	
	@Override
	protected void onDispatched(float gameTime, int soldierType) {
		/* Not responding to this event. */
	}

	@Override
	protected void onTick(float gameTime) {
		// Work out which soldiers currently in the tunnel will survive combat
		Array<TunnelAnalyser.SoldierOutcome> outcome = analyser.analyse(engine.tunnel);
		
		if(outcome.size == 0) {
			if(isOpponentOutOfSoldiers()) {
				// Everyone will die and opponent has nothing left so send anything to win!
				if(anySoldier.match(soldiers)) {
					send(anySoldier.selection, gameTime);
					return;
				}
			}
			
			// No soldiers will survive so wait for opponent to send something else
			return;
		}
		else if(outcome.get(0).direction == direction) {
			// Only soldiers left will be mine so do nothing - wait and see if they make it the whole way
			return;
		}
		else {
			// Surviving soldiers belong to my opponent - work out what is best to send to kill lots of them!
			TunnelAnalyser.SoldierOutcome firstSoldier = outcome.get(0);
			float firstSoldierPos = firstSoldier.position;
			if(outcome.size < SOLDIERS && ((direction == Direction.East && firstSoldierPos < dangerZone) || (direction == Direction.West && firstSoldierPos > dangerZone))) {
				// There aren't many soldiers in the tunnel and they are far away so wait for more to turn up
				// so that I can choose a better soldier to send
				return;
			}
			
			// There are lots of enemy soldiers and/or they are close so send something out to fight them
			sendSoldiers(outcome, gameTime);
		}
	}
	
	/**
	 * Sends soldiers into the tunnel in response to the outcome of analysis.
	 * @param outcome List of soldiers in tunnel that will survive combat.
	 * @param gameTime Current game time in seconds.
	 */
	private void sendSoldiers(Array<TunnelAnalyser.SoldierOutcome> outcome, float gameTime) {
		// Set the working set to soldiers that are effective against the first soldier in the tunnel
		workingSet.clear();
		
		int firstType = outcome.get(0).type;
		workingSet.copy(sets[firstType]);
		
		
		// Remove from the working set any soldier types that I don't have
		for(int i = 0; i < SoldierType.COUNT; i++) {
			if(soldiers[i] <= 0) workingSet.remove(i);
		}
		
		// Look for combinations with the soldier behind (if any)
		for(int i = 1; i < outcome.size; i++) {
			TunnelAnalyser.SoldierOutcome soldier = outcome.get(i);
			IntSet s = sets[soldier.type];
			if(workingSet.containsAny(s)) workingSet.intersect(s);
			else break;
		}
		
		// Go through set of types that are effective against what is in the tunnel 
		// See if I have a soldier to send from one of those types
		int len = workingSet.size();
		for(int i = 0; i < len; i++) {
			int type = workingSet.get(i);
			if(soldiers[type] > 0) {
				send(type);
				return;
			}
		}
		
		if(len == 0) {
			// There is no soldier combination to deal with even 1 soldier
			// Use selection rules to try and find panic combinations!
			SelectionRule[] applicableRules = rules[firstType];
			for(int i = 0; i < applicableRules.length; i++) {
				SelectionRule r = applicableRules[i];
				if(r.match(soldiers)) {
					send(r.selection, gameTime);
					return;
				}
			}
		}
	}
	
}
