package com.supergreenowl.tunnel.analyser;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.Soldier;
import com.supergreenowl.tunnel.model.SoldierType;
import com.supergreenowl.tunnel.model.TunnelState;

public class AnalysisEngine {

	private static final char RED = 'R';
	private static final char GREEN = 'G';
	private static final char BLUE = 'B';
	private static final char CYAN = 'C';
	private static final char MAGENTA = 'M';
	private static final char YELLOW = 'Y';
	private static final char WHITE = 'W';
	
	public int west = 0;
	public int east = 0;
	public int draws = 0;
	public int combinations, westCombinations, eastCombinations;
	
	
	public void analyse(String west, String east) {
		
		Set<String> westOptions = uniquePermutations(west);
		Set<String> eastOptions = uniquePermutations(east);
		
		westCombinations = westOptions.size();
		eastCombinations = eastOptions.size();
		combinations = westOptions.size() * eastOptions.size();
		this.west = 0;
		this.east = 0;
		draws = 0;
		
		for(String w : westOptions) {
			for(String e : eastOptions) {
				Queue<Soldier> westArmy = stringToArmy(w, Direction.West);
				Queue<Soldier> eastArmy = stringToArmy(e, Direction.East);
				
				TunnelState outcome = CombatAnalyser.fight(westArmy, eastArmy);
				if(outcome.equals(TunnelState.West)) this.west++;
				else if(outcome.equals(TunnelState.East)) this.east++;
				else draws++;
			}
		}
	}
	
	private Set<String> uniquePermutations(String s) {
		HashSet<String> set = new HashSet<String>();
		permutation(set, "", s);
		return set;
	}
	
	/**
	 * Recursively calculates all permutations of {@code str} and accumulates them in a collection.
	 * @param permutations Collection to accumulate permutations in.
	 * @param prefix
	 * @param str
	 */
	private void permutation(Collection<String> permutations, String prefix, String str) {
	    int n = str.length();
	    if (n == 0) permutations.add(prefix);
	    else {
	        for (int i = 0; i < n; i++)
	           permutation(permutations, prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
	    }

	}
	
	private Queue<Soldier> stringToArmy(String army, Direction direction) {
		
		LinkedList<Soldier> queue = new LinkedList<Soldier>();
		
		for(int i = 0; i < army.length(); i++) {
			char c = army.charAt(i);
			int t = charToSoldierType(c);
			Soldier s = new Soldier(direction, t);
			queue.add(s);
		}
		
		return queue;
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
