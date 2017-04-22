package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.SoldierType;

public class MediumAI extends AI {

	private static final float REACTION_SPEED = 2f;
	
	/**
	 * Rules used to select a soldier to dispatch. First index is the type of soldier dispatched by the
	 * enemy and the second is the order of preference of rules for that type.
	 * That is {@code rules[t][0]} is the most preferred rule to find a soldier to combat soldier type {@code t}.
	 */
	private SelectionRule[][] rules;
	
	private SelectionRule anySoldier;
	
	public MediumAI(Direction direction, SelectionStrategy selectionStrategy) {
		super(direction, REACTION_SPEED, selectionStrategy);
		
		// Set up selection rules - ordinary rules
		SelectionRule white = new SelectionRule(SoldierType.WHITE);
		SelectionRule red = new SelectionRule(SoldierType.RED);
		SelectionRule green = new SelectionRule(SoldierType.GREEN);
		SelectionRule blue = new SelectionRule(SoldierType.BLUE);
		SelectionRule cyan = new SelectionRule(SoldierType.CYAN);
		SelectionRule magenta = new SelectionRule(SoldierType.MAGENTA);
		SelectionRule yellow = new SelectionRule(SoldierType.YELLOW);
		SelectionRule whites3 = new SelectionRule(SoldierType.WHITE, 3);
		anySoldier = new SelectionRule();
		SelectionRule cyanOrMagenta = new SelectionRule(SoldierType.CYAN, SoldierType.MAGENTA, 1);
		SelectionRule cyanOrYellow = new SelectionRule(SoldierType.CYAN, SoldierType.YELLOW, 1);
		SelectionRule magentaOrYellow = new SelectionRule(SoldierType.MAGENTA, SoldierType.YELLOW, 1);
		
		// Set up selection rules - panic rules! These are the last resort rules which
		// knowingly send out soldiers vulnerable to the opponents soldier 
		SelectionRule redPanic = new SelectionRule(SoldierType.WHITE, SoldierType.GREEN, 3, true);
		SelectionRule greenPanic = new SelectionRule(SoldierType.WHITE, SoldierType.BLUE, 3, true);
		SelectionRule bluePanic = new SelectionRule(SoldierType.WHITE, SoldierType.RED, 3, true);
		
		SelectionRule cyanPanic = new SelectionRule(SoldierType.WHITE, new int[] { SoldierType.GREEN, SoldierType.BLUE, SoldierType.MAGENTA}, 3);
		SelectionRule magentaPanic = new SelectionRule(SoldierType.WHITE, new int[] { SoldierType.RED, SoldierType.BLUE, SoldierType.YELLOW}, 3);
		SelectionRule yellowPanic = new SelectionRule(SoldierType.WHITE, new int[] { SoldierType.RED, SoldierType.GREEN, SoldierType.CYAN}, 3);
		
		rules = new SelectionRule[][] {
				new SelectionRule[] { white, anySoldier }, // w
				new SelectionRule[] { magentaOrYellow, blue, whites3, red, redPanic }, // r
				new SelectionRule[] { cyanOrYellow, red, whites3, green, greenPanic }, // g
				new SelectionRule[] { cyanOrMagenta, green, whites3, blue, bluePanic }, // b
				new SelectionRule[] { red, yellow, whites3, cyan, cyanPanic }, // c
				new SelectionRule[] { green, cyan, whites3, magenta, magentaPanic}, // m
				new SelectionRule[] { blue, magenta, whites3, yellow, yellowPanic } // y
		};
	}

	@Override
	protected void onDispatched(float gameTime, int soldierType) {
		
		// I have nothing to respond with
		if(totalSoldiers() == 0) return;
		
		// Try to find best soldier to combat opposing soldier
		SelectionRule[] applicableRules = this.rules[soldierType];
		
		// Loop through applicable rules and choose the first
		int len = applicableRules.length;
		for(int i = 0; i < len; i++) {
			SelectionRule r = applicableRules[i];
			if(r.match(soldiers)) {
				send(r.selection, gameTime);
				return;
			}
		}
		
		// If no rules could be matched that there is nothing I can do!
	}

	@Override
	protected void onTick(float gameTime) {
		
		// If opponent has no soldiers, send anything to win!
		if(isOpponentOutOfSoldiers() && totalSoldiers() > 0) {
			if(anySoldier.match(soldiers)) send(anySoldier.selection, gameTime);
		}
	}

	@Override
	protected void onPreAutoDispatch(float gameTime) {
		
		// Try to send white to avoid auto-dispatch
		if(soldiers[SoldierType.WHITE] > 0) {
			send(SoldierType.WHITE);
		}
		else {
			// if I can't send white, send whatever I have most of!
			if(anySoldier.match(soldiers)) {
				send(anySoldier.selection, gameTime);
			}
		}
	}
	
}
