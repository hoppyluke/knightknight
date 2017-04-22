package com.supergreenowl.tunnel.campaign;

import com.supergreenowl.tunnel.controller.Difficulty;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.controller.GameConfig.SoldierAvailability;

/**
 * A campaign level.
 * @author luke
 *
 */
public class Level {

	/** The ID of level number of this level. */
	public int id;
	
	public Difficulty difficulty;
	
	public GameConfig.SoldierAvailability available;
	
	public SoldierList playerSoldiers;
	public SoldierList aiSoldiers;
	
	public int coins;
	
	/**
	 * Creates a new level.
	 * @param id Level number/ID.
	 * @param difficulty Character encoding of AI difficulty.
	 * @param playerSoldiers String representing player soldiers (or null if player gets to select their soldiers).
	 * @param aiSoldiers String representing AI soldiers (or null if AI gets to select their soldiers).
	 * @param coins Number of coins available for soldier selection.
	 */
	public Level(int id, char difficulty, String playerSoldiers, String aiSoldiers, int coins, char available) {
		this.id = id;
		this.difficulty = Difficulty.fromChar(difficulty);
		this.available = SoldierAvailability.fromChar(available);
		
		if(playerSoldiers == null) this.playerSoldiers = null;
		else this.playerSoldiers = new SoldierList(playerSoldiers);
		
		if(aiSoldiers == null) this.aiSoldiers = null;
		else this.aiSoldiers = new SoldierList(aiSoldiers);
		
		this.coins = coins;
	}

}
