package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.controller.GameConfig.SoldierAvailability;
import com.supergreenowl.tunnel.model.SoldierType;
import com.supergreenowl.tunnel.utils.Random;

/**
 * A selection strategy that selects mainly white soldiers and two other colours.
 * @author luke
 *
 */
public class SnowySelectionStrategy implements SelectionStrategy {
	
	private static final int WHITES_PER_ITERATION = 6;
	
	@Override
	public void select(Player player, int coins, SoldierAvailability available) {
		
		// If white soldiers aren't allowed, this strategy is pointless so fall back to balanced
		if(!available.allows(SoldierType.WHITE)) {
			BalancedSelectionStrategy balanced = new BalancedSelectionStrategy(false);
			balanced.select(player, coins, available);
			return;
		}

		int maxAllowed = available.allows(SoldierType.YELLOW) ? SoldierType.YELLOW : SoldierType.BLUE;
		
		int firstType = Random.nextInt(maxAllowed) + 1; // Add 1 so white isn't chosen
		int secondType = Random.nextInt(maxAllowed) + 1;
		while(secondType == firstType) secondType = Random.nextInt(maxAllowed) + 1;
		
		int whiteCost = SoldierType.COST[SoldierType.WHITE];
		int costPerIteration = whiteCost * WHITES_PER_ITERATION + SoldierType.COST[firstType];
		
		int[] soldiers = player.selectedSoldiers;
		int selectedType = firstType;
		
		soldiers[SoldierType.WHITE] = 0;
		
		// Buy whites alternating with favourite colour
		while(coins >= costPerIteration) {
			
			soldiers[SoldierType.WHITE] += WHITES_PER_ITERATION;
			if(soldiers[selectedType] == Player.NONE_SELECTED) soldiers[selectedType] = 0;
			soldiers[selectedType]++;
			
			if(selectedType == firstType) selectedType = secondType;
			else selectedType = firstType;
			
			coins -= costPerIteration;
		}
		
		// Buy as many whites as possible
		int moreWhites = coins / whiteCost;
		soldiers[SoldierType.WHITE] += moreWhites;
		coins -= whiteCost * moreWhites;
	}

}
