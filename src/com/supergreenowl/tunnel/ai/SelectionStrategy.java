package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.controller.Player;

/**
 * Strategy used by AI players to choose soldiers.
 * @author luke
 *
 */
public interface SelectionStrategy {

	/**
	 * Sets the available soldiers for a player. Chooses which soldiers to use based on the coins available.
	 * @param coins Number of coins available to spend.
	 * @param available Which soldier types can be selected in the game.
	 */
	void select(Player player, int coins, GameConfig.SoldierAvailability available);
	
}
