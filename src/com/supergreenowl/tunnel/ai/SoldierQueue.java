package com.supergreenowl.tunnel.ai;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * A pooled queue of {@link SoldierDispatch} instances.
 * @author luke
 *
 */
public class SoldierQueue {

	/**
	 * Pool of {@link SoldierDispatch} instances.
	 * @author luke
	 *
	 */
	private static class DispatchPool extends Pool<SoldierDispatch> {

		public DispatchPool(int capacity) {
			super(capacity);
		}
		
		@Override
		protected SoldierDispatch newObject() {
			return new SoldierDispatch();
		}
		
	}
	
	private static final int CAPACITY = 10;
	
	private Pool<SoldierDispatch> pool;
	private Array<SoldierDispatch> queue;
	
	public SoldierQueue() {
		pool = new DispatchPool(CAPACITY);
		queue = new Array<SoldierDispatch>(CAPACITY);
	}
	
	/**
	 * Queues a soldier to be dispatched later.
	 * @param soldierType Type of soldier to dispatch.
	 * @param gameTime Current game time in seconds.
	 * @param delay Number of seconds between the current time and the point at which the soldier should be dispatched.
	 */
	public void add(int soldierType, float gameTime, float delay) {
		SoldierDispatch d = pool.obtain();
		d.set(soldierType, gameTime + delay);
		queue.add(d);
	}

	/**
	 * Polls the queue to see if there are any dispatches due at the current time.
	 * @param gameTime Current game time (s).
	 * @return First dispatch in the queue that is due or {@code null} if the queue contains no dispatches that are due.
	 */
	public SoldierDispatch poll(float gameTime) {
		if(queue.size > 0) {
			SoldierDispatch d = queue.get(0);
			if(d.time <= gameTime) {
				queue.removeIndex(0);
				pool.free(d);
				return d;
			}
		}
		
		return null;
	}
}
