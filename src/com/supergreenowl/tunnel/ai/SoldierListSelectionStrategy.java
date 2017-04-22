package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.campaign.SoldierList;
import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.controller.GameConfig.SoldierAvailability;

/**
 * Selection strategy that selects the soldiers specified in a list.
 * @author luke
 *
 */
public class SoldierListSelectionStrategy implements SelectionStrategy {

	private SoldierList soldiers;
	
	public SoldierListSelectionStrategy(SoldierList soldiers) {
		this.soldiers = soldiers;
	}
	
	@Override
	public void select(Player player, int coins, SoldierAvailability available) {
		soldiers.select(player.selectedSoldiers);
	}
	
}