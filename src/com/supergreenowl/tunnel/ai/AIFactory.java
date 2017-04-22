package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.campaign.SoldierList;
import com.supergreenowl.tunnel.controller.Difficulty;
import com.supergreenowl.tunnel.model.Direction;

/**
 * Provides methods to generate AI instances for specified difficulty levels.
 * @author luke
 * @see AI
 */
public class AIFactory {

	/**
	 * Creates an AI player of the specified difficulty level.
	 * @param difficulty
	 * @param direction
	 * @return
	 */
	public static AI create(Difficulty difficulty, Direction direction) {
		
		if(difficulty == Difficulty.Hard) {
			return new HardAI(direction, new BalancedSelectionStrategy());
		}
		if(difficulty == Difficulty.Medium) {
			SelectionStrategy multi = new MultipleSelectionStrategy(new BalancedSelectionStrategy(false), new BalancedSelectionStrategy(true), new SnowySelectionStrategy());
			return new MediumAI(direction, multi);
		}
		else {
			// NB: since this method doesn't take specified soldiers, it cannot create an OrderedAI
			// therefore it will create an EasyAI in that case
			return new EasyAI(direction, new BalancedSelectionStrategy());
		}
	}
	
	/**
	 * Creates an AI player that will use the specified soldiers.
	 * @param difficulty Difficulty level of the AI player.
	 * @param direction Direction the AI plays in.
	 * @param soldiers Soldiers available to the AI player; {@code null} to let the AI choose its own soldiers.
	 * @return AI.
	 */
	public static AI create(Difficulty difficulty, Direction direction, SoldierList soldiers) {
		if(soldiers == null) {
			return create(difficulty, direction);
		}
		else {
			SoldierListSelectionStrategy selection = new SoldierListSelectionStrategy(soldiers);
			if(difficulty == Difficulty.Hard) return new HardAI(direction, selection);
			else if(difficulty == Difficulty.Medium) return new MediumAI(direction, selection);
			else if(difficulty == Difficulty.Ordered) return new OrderedAI(direction, soldiers);
			else return new EasyAI(direction, selection);
		}
	}
	
}
