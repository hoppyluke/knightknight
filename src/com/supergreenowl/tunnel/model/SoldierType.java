package com.supergreenowl.tunnel.model;

/**
 * Defines constants for the different types of soldiers in the game.
 * {@code int} constants for the different soldier types are cunningly designed so they can be used to index
 * an array that holds soldier types in the order WRGBCMY (for example the {@link #COST} array).
 * @author luke
 *
 */
public class SoldierType {

	private SoldierType() {}
	
	/**
	 * Cost matrix for soldiers of different types. Indexed by soldier type.
	 */
	public static final int[] COST = new int[] { 1, 3, 3, 3, 3, 3, 3 };
	
	/** Number of hitpoints each soldier has. Indexed by soldier type. */
	public static final int[] HITPOINTS = new int[] { 1, 3, 3, 3, 3, 3, 3 }; 
	
	/** Type value indicating no type. */
	public static final int NONE = Integer.MIN_VALUE;
	
	public static final int WHITE = 0;
	
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	
	public static final int CYAN = 4;
	public static final int MAGENTA = 5;
	public static final int YELLOW = 6;
	
	/** Number of different soldier types. */
	public static final int COUNT = 7;
}
