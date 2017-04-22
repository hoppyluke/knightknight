package com.supergreenowl.tunnel.ui;

import com.badlogic.gdx.math.Rectangle;
import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.model.Direction;

public class DispatchButton {

	public boolean isEnabled;
	public final Direction direction;
	public final int soldierType;
	
	public Rectangle bounds;
	
	public DispatchButton(Direction direction, int soldierType) {
		this.direction = direction;
		this.soldierType = soldierType; 
		this.isEnabled = true;
		this.bounds = new Rectangle();
	}
	
	/**
	 * Gets an array of dispatch soldier buttons for a player.
	 * @param p
	 * @return Array containing a button for each soldier type the player has selected. Null if player is not a human and so
	 * doesn't need any buttons.
	 */
	public static DispatchButton[] getButtons(Player p) {
		
		if(!p.isHuman) return null; // No buttons needed for AI
		
		// First work out how many buttons this player needs
		int count = 0;
		for(int i = 0; i < p.soldiers.length; i++) {
			if(p.soldiers[i] != Player.NONE_SELECTED) count++;
		}
		
		// Create dispatch soldier buttons for each element
		DispatchButton[] buttons = new DispatchButton[count];
		int next = 0;
		
		for(int i = 0; i < p.soldiers.length; i++) {
			if(p.soldiers[i] != Player.NONE_SELECTED) {
				buttons[next++] = new DispatchButton(p.direction, i);
			}
		}
		
		return buttons;
	}
	
}
