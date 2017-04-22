package com.supergreenowl.tunnel.ai;

import java.util.Arrays;

import com.badlogic.gdx.utils.Array;
import com.supergreenowl.tunnel.controller.Engine;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.Soldier;

/**
 * {@link Player} that is controlled by game AI.
 * @author luke
 *
 */
public abstract class AI extends Player {

	private static final float DISPATCH_DELAY = 0.5f;
	
	/**
	 * Strategy used by this player to choose which soldiers to purchase.
	 */
	public SelectionStrategy selection;
	
	/** Notifications of soldiers dispatched by the opponent. */
	private Array<SoldierDispatch> dispatches;
	
	/** Soldiers waiting to be dispatched. */
	private SoldierQueue queue;
	
	/**
	 * Reaction speed of this AI in seconds.
	 */
	public float reactionSpeed;
	
	/** Indicates if this AI has dispatched at least 1 soldier yet. */
	protected boolean hasDispatched;
	
	private float lastTick;
	
	public AI(Direction direction, float reactionSpeed, SelectionStrategy selectionStrategy) {
		super(false, direction);
		this.engine = null;
		this.reactionSpeed = reactionSpeed;
		this.selection = selectionStrategy;
		dispatches = new Array<SoldierDispatch>();
		queue = new SoldierQueue();
		
	}
	
	@Override
	public void notify(int soldierType, float time) {
		dispatches.add(new SoldierDispatch(soldierType, time));
	}
	
	@Override
	public void setEngine(Engine engine) {
		super.setEngine(engine);
		reset();
	}

	@Override
	public void play(float gameTime) {
		
		// Dispatch any queued soldiers
		SoldierDispatch queuedSoldier;
		while((queuedSoldier = queue.poll(gameTime)) != null) {
			send(queuedSoldier.type);
		}
		
		// React to any released soldiers
		while(dispatches.size > 0 && dispatches.get(0).time <= gameTime - reactionSpeed) {
			onDispatched(gameTime, dispatches.get(0).type);
			dispatches.removeIndex(0);
		}
		
		// Fire pre-auto dispatch event
		if(!hasDispatched && gameTime >= Engine.AUTO_DISPATCH_TIME - reactionSpeed) {
			onPreAutoDispatch(gameTime);
			hasDispatched = true; // even if AI did not actually dispatch, it's had a chance to 
		}
		
		// Allow time-based strategies
		if(gameTime >= lastTick + reactionSpeed) {
			onTick(gameTime);
			lastTick = gameTime;
		}
	}
	
	public void selectSoldiers(int coins, GameConfig.SoldierAvailability available) {
		Arrays.fill(selectedSoldiers, Player.NONE_SELECTED);
		selection.select(this, coins, available);
	}

	/**
	 * Sends a soldier into the tunnel (if a soldier of the specified type is available).
	 * @param soldierType Type of soldier to send.
	 */
	protected void send(int soldierType) {
		if(soldiers[soldierType] > 0) {
			engine.dispatchSoldier(direction, soldierType);
			hasDispatched = true;
		}
	}
	
	/**
	 * Queues a soldier to be sent into the tunnel later.
	 * @param soldierType Type of soldier to send.
	 * @param gameTime Current game time.
	 * @param delay Seconds before the soldier should be sent.
	 */
	protected void queue(int soldierType, float gameTime, float delay) {
		queue.add(soldierType, gameTime, delay);
	}
	
	/**
	 * Sends one or more soldiers into the tunnel.
	 * @param soldierTypes List of soldier types to send in order.
	 * @param gameTime Current game time (s).
	 */
	protected void send(int[] soldierTypes, float gameTime) {
		if(soldierTypes == null) return;
		
		for(int i = 0; i < soldierTypes.length; i++) {
			int type = soldierTypes[i];
			if(type == SelectionRule.NONE) return; // No more soldiers to send
			
			if(i == 0) send(type); // Send first selected soldier now
			else queue(type, gameTime, DISPATCH_DELAY); // Queue any remaining selections
		}
	}
	
	/**
	 * Determines if the opponent of this AI player has any soldiers remaining either in the tunnel or waiting to be sent in.
	 * @return True if the opponent has no more soldiers to send.
	 */
	protected boolean isOpponentOutOfSoldiers() {
		
		Array<Soldier> enemySoldiersInTunnel;
		Player opponent;
		
		if(direction == Direction.East) {
			enemySoldiersInTunnel = engine.tunnel.west;
			opponent = engine.west;
		}
		else {
			enemySoldiersInTunnel = engine.tunnel.east;
			opponent = engine.east;
		}
		
		return enemySoldiersInTunnel.size == 0 && opponent.totalSoldiers() == 0;
	}
		
	protected abstract void onDispatched(float gameTime, int soldierType);
	
	protected abstract void onPreAutoDispatch(float gameTime);
	
	protected abstract void onTick(float gameTime);
	
	/**
	 * Resets this AI so it can play another game.
	 */
	protected void reset() {
		hasDispatched = false;
		lastTick = 0f;
	}
}
