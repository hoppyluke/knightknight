package com.supergreenowl.tunnel.utils;

/**
 * Fixed capacity set of integers that avoids boxing and un-boxing.
 * The capacity will increase if necessary if a set is copied but otherwise cannot be modified.
 * @author luke
 *
 */
public class IntSet {

	private int capacity;
	private int[] values;
	private int pointer;
	private int[] workingSet;

	/**
	 * Creates a new set and fills it with the specified values.
	 * The capacity of the set is set to the number of values.
	 * @param values Values to add.
	 */
	public IntSet(int... values) {
		this.pointer = this.capacity = values.length;
		this.values = values;
		this.workingSet = new int[capacity];
	}
	
	/**
	 * Creates a new empty set with the specified capacity.
	 * @param capacity Set capacity.
	 */
	public IntSet(int capacity) {
		this.capacity = capacity;
		this.values = new int[capacity];
		this.pointer = 0;
		this.workingSet = new int[capacity];
	}
	
	/**
	 * Returns the number of items currently in this set.
	 * @return
	 */
	public int size() {
		return pointer;
	}
	
	/**
	 * Returns true if there are no elements in this set.
	 * @return
	 */
	public boolean isEmpty() {
		return pointer == 0;
	}
	
	/**
	 * Clears the contents of this set.
	 */
	public void clear() {
		pointer = 0;
	}
	
	/**
	 * Determines if this set contains an integer. Performance is linear.
	 * @param n Number to search for.
	 * @return True if the number is an element of this set.
	 */
	public boolean contains(int n) {
		return search(n, values, pointer);
	}
	
	/**
	 * Adds an integer to this set, if it is not already present.
	 * @param n Number to add.
	 * @return True if the number was added, false if it was already present in the set.
	 */
	public boolean add(int n) {
		if(contains(n)) return false;
		
		values[pointer++] = n;
		return true;
	}
	
	/**
	 * Removes an element from this set if it is present. 
	 * @param n Number to remove.
	 * @return True if the number was removed, false if it wasn't in the set to begin with.
	 */
	public boolean remove(int n) {
		
		for(int i = 0; i < pointer; i++) {
			if(values[i] == n) {
				// Shift the end of the array (after the removed element) one place left
				System.arraycopy(values, i+1, values, i, (pointer--) - i);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the element at position {@code i}.
	 * @param i Index of element to retrieve.
	 * @return Element at specified index.
	 */
	public int get(int i) {
		if(i < 0 || i >= pointer) throw new IndexOutOfBoundsException();
		
		return values[i];
	}
	
	/**
	 * Replaces the contents of this set with the contents of the specified set.
	 * The capacity of this set will increase if it is smaller than the capacity of the set being copied from.
	 * @param copyFrom Set to copy elements from.
	 */
	public void copy(IntSet copyFrom) {
	
		// Avoid re-instantiating values array if possible
		if(this.capacity < copyFrom.capacity) {
			this.capacity = copyFrom.capacity;
			this.values = new int[capacity];
			this.workingSet = new int[capacity];
		}
		
		System.arraycopy(copyFrom.values, 0, values, 0, copyFrom.pointer);
		
		this.pointer = copyFrom.pointer;
	}
	
	/**
	 * Sets the contents of this set to be the intersection of its current contents and the contents of another set.
	 * @param other Set to intersect with.
	 */
	public void intersect(IntSet other) {
		int itemsAdded = 0;
		int n;
		
		// Add elements of this set that are also elements of other to the working set
		for(int i = 0; i < pointer; i++) {
			n = values[i];
			if(search(n, other.values, other.pointer)) {
				workingSet[itemsAdded++] = n;
			}
		}
		
		// Copy intersection from working set to this set and re-position pointer
		System.arraycopy(workingSet, 0, values, 0, itemsAdded);
		pointer = itemsAdded;
	}
	
	/**
	 * Determines if this set and another set have any elements in common.
	 * @param other Other set to compare with.
	 * @return True if the is one or more element that is contained in both sets. False otherwise (including if either set is empty).
	 */
	public boolean containsAny(IntSet other) {
		for(int i = 0; i < other.pointer; i++) {
			if(search(other.values[i], values, pointer))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Performs a linear search of {@code array} from indices {@code 0} to {@code endPos - 1} (inclusive) for {@code value}.
	 * @param value Value to search for.
	 * @param array Array to search.
	 * @param endPos End position for search.
	 * @return True if and only if value is an element of array.
	 */
	private static boolean search(int value, int[] array, int endPos) {
		for(int i = 0; i < endPos; i++) {
			if(array[i] == value) return true;
		}
		
		return false;
	}
}
