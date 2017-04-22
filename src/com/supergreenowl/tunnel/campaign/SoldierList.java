package com.supergreenowl.tunnel.campaign;

import com.badlogic.gdx.utils.IntArray;
import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.model.SoldierType;

/**
 * A list of soldiers for a campaign level. The list is initialised with a string such as "{@code WRGBCMYRGB}" indicating soldier types in a specified order. 
 * @author luke
 *
 */
public class SoldierList {

	private static final char RED = 'R';
	private static final char GREEN = 'G';
	private static final char BLUE = 'B';
	private static final char CYAN = 'C';
	private static final char MAGENTA = 'M';
	private static final char YELLOW = 'Y';
	private static final char WHITE = 'W';
	
	private IntArray order;
	private int pointer;
	private int length;
	
	public SoldierList(String list) {
				
		pointer = 0;
		
		length = list.length();
		order = new IntArray(length);
		for(int i = 0; i < length; i++) {
			int type = charToSoldierType(list.charAt(i));
			
			// Add soldier type to ordered list
			order.add(type);
		}
	}
	
	/**
	 * Determines if there are any soldiers left in this order.
	 * @return
	 */
	public boolean hasNext() {
		return pointer < length;
	}
	
	/**
	 * Gets the next soldier in the list.
	 * @return Type of the next soldier.
	 */
	public int nextSoldier() {
		if(pointer >= length) throw new IllegalStateException("There are no more soldiers in this list.");
		
		return order.get(pointer++);
	}
	
	/**
	 * Resets the order so any further {@link #nextSoldier()} calls start from the beginning of the list again.
	 */
	public void reset() {
		pointer = 0;
	}
	
	/**
	 * Sets the specified soldier selection array to be filled with the soldiers in this list.
	 */
	public void select(int[] selectedSoldiers) {
		
		for(int i = 0; i < length; i++) {
			
			int type = order.get(i);
			
			// Record a soldier of the type has been selected
			if(selectedSoldiers[type] == Player.NONE_SELECTED) selectedSoldiers[type] = 0;
			selectedSoldiers[type]++;
		}
		
	}
	
	/**
	 * Converts a character representing a soldier to a soldier type.
	 * @param c
	 * @return Soldier type corresponding to specified character.
	 */
	private int charToSoldierType(char c) {
		switch(c) {
			case RED: return SoldierType.RED;
			case GREEN: return SoldierType.GREEN;
			case BLUE: return SoldierType.BLUE;
			case CYAN: return SoldierType.CYAN;
			case MAGENTA: return SoldierType.MAGENTA;
			case YELLOW: return SoldierType.YELLOW;
			case WHITE: return SoldierType.WHITE;
			default: throw new IllegalArgumentException(Character.toString(c) + " is not a valid soldier type");
		}
	}	
}
