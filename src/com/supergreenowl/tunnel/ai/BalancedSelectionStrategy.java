package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.controller.GameConfig.SoldierAvailability;
import com.supergreenowl.tunnel.model.SoldierType;

/**
 * Selection strategy which chooses a balanced force of as many RGBCMY soldiers as possible. Any remaining coins are then spent on white soldiers.
 * @author luke
 *
 */
public class BalancedSelectionStrategy implements SelectionStrategy {

	private boolean selectWhites;
	
	public BalancedSelectionStrategy() {
		this(false);
	}
	
	/**
	 * 
	 * @param selectWhites Indicates if white soldiers should be picked as part of the core selection.
	 */
	public BalancedSelectionStrategy(boolean selectWhites) {
		this.selectWhites = selectWhites;
	}
	
	@Override
	public void select(Player player, int coins, SoldierAvailability available) {
				
		boolean isWhiteAvailable = available.allows(SoldierType.WHITE);
		
		int selected = SoldierType.BLUE;
		
		// choose R, G, B, C, M, Y in turn until coins < 3
		while(coins >= SoldierType.COST[SoldierType.RED]) {
			
			// Choose next type to select
			selected = nextSoldierType(selected, available);
			
			// Select soldier of next type
			// NB THE AVAILABLE SOLDIERS ARRAY IS INITIALIZED TO Player.NONE_SELECTED AND INCREMENTING THIS IS NOT MEANINGUL
			if(player.selectedSoldiers[selected] == Player.NONE_SELECTED) player.selectedSoldiers[selected] = 0;
			player.selectedSoldiers[selected]++;
			coins -= SoldierType.COST[selected];
		}
		
		// if coins > 0 and white available then choose white
		while(isWhiteAvailable && coins >= SoldierType.COST[SoldierType.WHITE]) {
			if(player.selectedSoldiers[SoldierType.WHITE] == Player.NONE_SELECTED) player.selectedSoldiers[SoldierType.WHITE] = 0;
			player.selectedSoldiers[SoldierType.WHITE]++;
			coins -= SoldierType.COST[SoldierType.WHITE];
		}
		
	}
	
	/**
	 * Chooses the next soldier type to select based on the current type. Cycles through types in order (but excluding WHITE).
	 * @param type
	 * @param available
	 * @return
	 */
	private int nextSoldierType(int type, SoldierAvailability available) {
		if(type == SoldierType.RED) return SoldierType.GREEN;
		else if(type == SoldierType.GREEN) return SoldierType.BLUE;
		else if(type == SoldierType.BLUE) {
			if(available.allows(SoldierType.CYAN)) return SoldierType.CYAN; // if CMY is allowed..
			else if(selectWhites && available.allows(SoldierType.WHITE)) return SoldierType.WHITE; // If WRGB is allowed and whites are to be picked...
			else return SoldierType.RED;
		}
		else if(type == SoldierType.CYAN) return SoldierType.MAGENTA;
		else if(type == SoldierType.MAGENTA) return SoldierType.YELLOW;
		else if(type == SoldierType.YELLOW) return selectWhites ? SoldierType.WHITE : SoldierType.RED;
		else return SoldierType.RED; // if type == SoldierType.WHITE
	}
	
}
