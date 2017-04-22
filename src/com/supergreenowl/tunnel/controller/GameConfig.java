package com.supergreenowl.tunnel.controller;

import com.supergreenowl.tunnel.model.SoldierType;

/**
 * Defines the configuration and setup of a game.
 * @author luke
 *
 */
public class GameConfig {

	/**
	 * Defines different types of games that can be played.
	 * @author luke
	 *
	 */
	public enum Type {
		Campaign,
		QuickPlay,
		Multiplayer
	}
	
	/**
	 * Defines the size of an army for a quick play game.
	 * @author luke
	 *
	 */
	public enum ArmySize {
		Small,
		Medium,
		Large;
		
		/**
		 * Converts an army size into a specific number of coins to spend.
		 * @return
		 */
		public int toCoins() {
			if(this.equals(Small)) return 18;
			else if(this.equals(Medium)) return 27;
			else if(this.equals(Large)) return 36;
			else return 18; // default to small
		}
	}
	
	/**
	 * Defines the different options for which soldier types are available for purchase.
	 * @author luke
	 *
	 */
	public enum SoldierAvailability {
		RGB,
		WRGB,
		WRGBCMY;
		
		/**
		 * Determines if this soldier availability allows soldiers of the specified type.
		 */
		public boolean allows(int soldierType) {
			if(this.equals(RGB)) {
				if(soldierType == SoldierType.RED
						|| soldierType == SoldierType.GREEN
						|| soldierType == SoldierType.BLUE)
					return true;
			}
			else if(this.equals(WRGB)) {
				if(soldierType == SoldierType.RED
						|| soldierType == SoldierType.GREEN
						|| soldierType == SoldierType.BLUE
						|| soldierType == SoldierType.WHITE)
					return true;
			}
			else if(this.equals(WRGBCMY)) {
				if(soldierType == SoldierType.RED
						|| soldierType == SoldierType.GREEN
						|| soldierType == SoldierType.BLUE
						|| soldierType == SoldierType.WHITE
						|| soldierType == SoldierType.CYAN
						|| soldierType == SoldierType.MAGENTA
						|| soldierType == SoldierType.YELLOW)
					return true;
			}
			return false;
		}
		
		public static SoldierAvailability fromChar(char c) {
			if(c == 'R') return SoldierAvailability.RGB;
			else if(c == 'W') return SoldierAvailability.WRGB;
			else if(c == 'C') return SoldierAvailability.WRGBCMY;
			
			return SoldierAvailability.RGB;
		}
	}
	
	/**
	 * Indicates the current game is not a specific campaign level.
	 */
	public static final int NO_LEVEL = 0;
	
	/**
	 * Number of coins available when purchasing troops.
	 */
	public int coins;
	
	/**
	 * Type of game to be played.
	 */
	public Type type;
	
	/**
	 * Difficulty of the AI (if any).
	 */
	public Difficulty difficulty;
	
	/**
	 * Which soldiers are available for purchase.
	 */
	public SoldierAvailability available;
	
	/**
	 * Size of armies in a quick play battle.
	 */
	public ArmySize armySize;
	
	/**
	 * Player at the west end.
	 */
	public Player west;
	
	/**
	 * Player at the east end.
	 */
	public Player east;
	
	/**
	 * ID of the current level (if this is a campaign game).
	 * @see NO_LEVEL
	 */
	public int levelId;
	
	public GameConfig(Type type) {
		this(type, Difficulty.Easy, SoldierAvailability.RGB, ArmySize.Small);
	}
	
	public GameConfig(Type type, Difficulty difficulty, SoldierAvailability available) {
		this(type, difficulty, available, ArmySize.Small);
	}
	
	public GameConfig(Type type, Difficulty difficulty, SoldierAvailability available, ArmySize armySize) {
		this.type = type;
		this.difficulty = difficulty;
		this.available = available;
		this.armySize = armySize;
		this.coins = armySize.toCoins();
		this.levelId = NO_LEVEL;
	}
}
