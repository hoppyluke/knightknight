package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.model.Direction;

/**
 * AI player that sends out its soldiers in a random order at a fixed interval.
 * @author luke
 *
 */
public class EasyAI extends AI {

	private static final float REACTION_SPEED = 3f;
		
	public EasyAI(Direction direction, SelectionStrategy strategy) {
		super(direction, REACTION_SPEED, strategy);
	}

	@Override
	protected void onTick(float gameTime) {
		
		// Send out next soldier if one is due
		if((totalSoldiers() > 0)) {
			int soldierType = selectRandomSoldier();
			if(soldierType != Player.NONE_SELECTED) {
				send(soldierType);
			}
		}
	}
	
	@Override
	protected void onDispatched(float gameTime, int soldierType) {
		// Easy AI is too stupid to react to this event.
	}

	@Override
	protected void onPreAutoDispatch(float gameTime) {
		// Easy AI is too stupid to react to this event.
	}
}
