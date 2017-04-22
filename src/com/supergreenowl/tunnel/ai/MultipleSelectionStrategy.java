package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.controller.GameConfig.SoldierAvailability;
import com.supergreenowl.tunnel.utils.Random;

/**
 * A {@link SelectionStrategy} which chooses randomly between multiple other selection strategies each time it is asked to select.
 * This allows the same AI player to use different strategies across multiple games.
 * @author luke
 *
 */
public class MultipleSelectionStrategy implements SelectionStrategy { 
	
	private SelectionStrategy[] strategies;
	
	/**
	 * Creates a new multiple selection strategy.
	 * @param strategies Selection strategies to choose between.
	 */
	public MultipleSelectionStrategy(SelectionStrategy... strategies) {
		this.strategies = strategies;
	}
	
	@Override
	public void select(Player player, int coins, SoldierAvailability available) {
		// Select a random strategy to use for each selection
		int strategyToUse = Random.nextInt(strategies.length);
		
		// Select soldiers according to chosen strategy
		strategies[strategyToUse].select(player, coins, available);
	}

}
