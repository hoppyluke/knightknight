package com.supergreenowl.tunnel.controller;

/**
 * Defines AI difficulty levels.
 * @author luke
 *
 */
public enum Difficulty {
	Easy,
	Medium,
	Hard,
	Ordered;
	
	private static final char EASY = 'E';
	private static final char MEDIUM = 'M';
	private static final char HARD = 'H';
	private static final char ORDERED = 'O';
	
	/**
	 * Gets the difficulty level associated with the specified character encoding of difficulty.
	 * @param c
	 * @return Difficulty.
	 */
	public static Difficulty fromChar(char c) {
		switch(c) {
		case EASY: return Easy;
		case MEDIUM: return Medium;
		case HARD: return Hard;
		case ORDERED: return Ordered;
		default: throw new IllegalArgumentException(Character.toString(c) + " is not a valid difficulty.");
		}
	}
}