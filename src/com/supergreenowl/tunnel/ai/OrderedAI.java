package com.supergreenowl.tunnel.ai;

import com.supergreenowl.tunnel.campaign.SoldierList;
import com.supergreenowl.tunnel.model.Direction;

/**
 * {@link AI} that sends out soldiers at a fixed interval according to a specified order.
 * @author luke
 * @see SoldierList
 *
 */
public class OrderedAI extends AI {

	private static final float REACTION_SPEED = 3f;
	
	private SoldierList list;

	public OrderedAI(Direction direction, SoldierList list) {
		super(direction, REACTION_SPEED, new SoldierListSelectionStrategy(list));
		this.list = list;
	}

	@Override
	protected void onTick(float gameTime) {
		// Send out next soldier if one is due
		if(list.hasNext()) {
			int soldierType = list.nextSoldier();
			send(soldierType);
		}
	}

	@Override
	protected void onPreAutoDispatch(float gameTime) {
		/* Does not react to this event. */
	}

	@Override
	protected void onDispatched(float gameTime, int soldierType) {
		/* Does not react to this event. */
	}
	
	@Override
	protected void reset() {
		super.reset();
		list.reset();
	}

}
