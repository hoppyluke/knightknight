package com.supergreenowl.tunnel.utils;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * A simple linked queue that pools its nodes. 
 * @author luke
 *
 * @param <T> Type of items in this queue.
 */
public class PooledQueue<T> {

	/**
	 * Linked node in a PooledQueue.
	 * @author luke
	 * @param <T> Type of item in queue.
	 */
	private static class Node<T> implements Poolable {
		
		public T value;
		public Node<T> next;
		
		public Node() {
			value = null;
			next = null;
		}
		
		@Override
		public void reset() {
			value = null;
			next = null;
		}		
	}
	
	/**
	 * Pool of nodes to prevent new nodes from being created every time something is added to the queue.
	 * @author luke
	 */
	private class NodePool extends Pool<Node<T>> {
		
		@Override
		protected Node<T> newObject() {
			return new Node<T>();
		}
		
	}
	
	private Pool<Node<T>> nodePool;
	private Node<T> head;
	private int size;
	
	/**
	 * Creates a new pooled queue.
	 */
	public PooledQueue() {
		this.nodePool = new NodePool();
		this.head = null;
		this.size = 0;
	}
	
	/**
	 * Removes all items from the queue.
	 */
	public void clear() {
		Node<T> n = head;
		while(n != null) {
			Node<T> next = head.next;
			nodePool.free(n);
			n = next;
		}
				
		head = null;
		size = 0;
	}
	
	/**
	 * Determines if the queue is currently empty.
	 * @return
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Gets the number of items currently in the queue.
	 * @return
	 */
	public int size() {
		return size;
	}

	/**
	 * Adds an item to the end of the queue.
	 * @param item Item to add.
	 */
	public void add(T item) {
		Node<T> node = nodePool.obtain();
		node.value = item;
		
		if(head == null) head = node;
		else {
			Node<T> tail = head;
			while(tail.next != null) tail = tail.next;
			tail.next = node;
		}
		
		size++;
	}

	/**
	 * Retrieves but does not remove the item at the head of the queue.
	 * @return Item at head or null if queue is empty.
	 */
	public T peek() {
		if(head == null) return null;
		return head.value;
	}

	/**
	 * Retrieves and removes the item at the head of the queue.
	 * @return Item that was at head of the queue or null if the queue is empty.
	 */
	public T poll() {
		if(head == null) return null;
		
		T value = head.value; // Get item at head
		Node<T> next = head.next; // Work out new head (might be null)
		nodePool.free(head); // Put current head node back in pool
		head = next; // Set new head node
		size--; // Item has been removed so update queue size
		
		return value;
	}
}
