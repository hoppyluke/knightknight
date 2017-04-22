package com.supergreenowl.tunnel.controller;

import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.SoldierType;
import com.supergreenowl.tunnel.utils.Random;

public class Player {
	
	/**
	 * Entry in the available soldiers array indicating that this player never had any soldiers of a
	 * given type available.
	 */
	public static final int NONE_SELECTED = Integer.MIN_VALUE;
	
	/**
	 * The direction from which this player sends soldiers into the tunnel.
	 */
	public final Direction direction;
	
	/**
	 * Indicates if this player is controlled by a human or AI.
	 */
	public boolean isHuman;
	
	/**
	 * Number of soldiers actually available to send into the tunnel of each type.
	 * This is used by the game engine to determine the number of remaining soldiers for the game.
	 * Indexed by {@link SoldierType} constants.
	 */
	public int[] soldiers;
	
	/**
	 * Number of soldiers chosen by this player of each type.
	 * Indexed by {@link SoldierType} constants.
	 */
	public int[] selectedSoldiers;
	
	/** Number of points earnt by this player. */
	public int score;
		
	/**
	 * Engine running the game that this player is in.
	 */
	protected Engine engine;
	
	public Player(boolean isHuman, Direction direction) {
		
		this.isHuman = isHuman;
		this.direction = direction;
		this.score = 0;
		
		// Initialise available soldiers array with no soldiers selected
		soldiers = new int[] { NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED };
		selectedSoldiers = new int[] { NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED, NONE_SELECTED };
	}

	/**
	 * Calculates the total number of soldiers available to this player.
	 * @return
	 */
	public int totalSoldiers() {
		int total = 0;
		int len = soldiers.length;
		
		for(int i = 0; i < len; i++) {
			if(soldiers[i] != NONE_SELECTED) total += soldiers[i];
		}
		
		return total;
	}
	
	/**
	 * Calculates the total cost of the soldiers selected by this player.
	 * @return
	 */
	public int totalCost() {
		int cost = 0;
		int len = selectedSoldiers.length;
		
		for(int i = 0; i < len; i++) {
			if(selectedSoldiers[i] > 0) {
				cost += SoldierType.COST[i] * selectedSoldiers[i];
			}
		}
		
		return cost;
	}
	
	/**
	 * Gets the player to play (for AI players only).
	 * @param gameTime
	 */
	public void play(float gameTime) {
		// Default implementation is empty for human players.
	}
	
	/**
	 * Notifies the player that a soldier has been dispatched by its opponent.
	 * @param soldierType Type of soldier dispatched.
	 * @param time Game time at which the soldier was dispatched.
	 */
	public void notify(int soldierType, float time) {
		// Default implementation is empty for human players.
	}
	
	/**
	 * Sets the game engine and initialises the actual soldiers available array.
	 * @param engine
	 */
	public void setEngine(Engine engine) {
		this.engine = engine;
		this.score = 0;
		System.arraycopy(selectedSoldiers, 0, soldiers, 0, selectedSoldiers.length);
	}
	
	/**
	 * Selects one soldier at random that this player has available to dispatch.
	 * @return Type of the selected soldier or {@link Player.NONE_SELECTED} if no soldiers are available.
	 */
	public int selectRandomSoldier() {
		
		// Count number of soldier types available
		int typesAvailable = 0;
		
		for(int i = 0; i < soldiers.length; i++) {
			if(soldiers[i] > 0) typesAvailable++;
		}
		
		if(typesAvailable == 0) return Player.NONE_SELECTED;
		
		// Choose randomly a type to send based on types available
		// when typeToSend = n is the position of the type to send within the list of ONLY available types
		// e.g. typeToSend = 2, available types = W, R, B, C
		// then we should send a blue (W = 0. R = 1, B = 2, C = 3)
		int typeToSend = Random.nextInt(typesAvailable);
		
		// (The reason I'm going to all this trouble is non-selected types can make certain types more likely
		// if you just loop through the types from a random start location e.g. if you only have RGB then if
		// W, R, C, M or Y is picked then the loop will reach R first.)
		
		// Loop through available soldiers again to work out which type corresponds to the offset calculated
		int typeIndex = -1;
		for(int i = 0; i < soldiers.length; i++) {
			if(soldiers[i] > 0) {
				if(++typeIndex == typeToSend) {
					return i;
				}
			}
		}
		
		return Player.NONE_SELECTED;
	}
}
