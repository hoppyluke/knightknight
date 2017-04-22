package com.supergreenowl.tunnel.ai;

import java.util.Arrays;

import com.supergreenowl.tunnel.model.SoldierType;

/**
 * Selection rule used by AI players to choose soldier(s) to dispatch.
 * @author luke
 *
 */
public class SelectionRule {

	/** Value representing a choice of no soldier type. */
	public static final int NONE = Integer.MIN_VALUE;
	
	/** Value representing a choice of any soldier type. */ 
	public static final int ANY = Integer.MAX_VALUE;
	
	/** Main soldier type to choose. */
	public int primaryType;
	
	/** Alternative soldier type to choose. */
	public int secondaryType;
	
	/**
	 * Additional soldier types to choose when primary and secondary types are not available.
	 * All of these types are treated as equally preferred. */
	public int[] additionalTypes;
	
	/** Quantity of soldiers required to meet this rule. */
	public int quantity;
	
	/**
	 * Can this rule be met by a combination of multiple different types?
	 */
	public boolean isTypeCombination;
	
	/** Ordered list of soldier types that met the last successful {@link #match(int[])}. */
	public int[] selection;
	
	/** Indicates if this rule can be met by a number of soldiers fewer than {@link #quantity} but greater than zero. */
	private boolean isPartialMatch;
	
	private int[] soldiers;
	
	/**
	 * Creates a new selection rule that will match 1 of any soldier type.
	 */
	public SelectionRule() {
		this(ANY, NONE, null, 1, false, false);
	}
	
	/**
	 * Creates a new selection rule that will match 1 of the specified soldier type.
	 * @param soldierType Type of soldier to match.
	 */
	public SelectionRule(int soldierType) {
		this(soldierType, NONE, null, 1, false, false);
	}
	
	/**
	 * Creates a new selection rule that will match the specified soldier type.
	 * @param soldierType Type of soldier to match.
	 * @param quantity Quantity of soldiers required for this rule to match.
	 */
	public SelectionRule(int soldierType, int quantity) {
		this(soldierType, NONE, null, quantity, false, false);
	}
	
	/**
	 * Creates a new selection rule that will match either of the specified soldier types. If both types match, the one with
	 * the greatest number of available soldiers will be matched.
	 * @param type1 First soldier type to match.
	 * @param type2 Second soldier type to match.
	 * @param quantity Quantity of soldiers required.
	 */
	public SelectionRule(int type1, int type2, int quantity) {
		this(type1, type2, null, quantity, false, false);
	}
	
	/**
	 * Creates new selection rule that will match a combination of the specified types. Will partially match and select a combination of soldiers
	 * that does not meet the specified quantity if necessary.
	 * @param preferred Preferred soldier type to match.
	 * @param additional Additional soldier types to match (all given equal preference and selected based on greatest quantity available).
	 * @param quantity Quantity of soldiers required to match the rule.
	 */
	public SelectionRule(int preferred, int[] additional, int quantity) {
		this(preferred, NONE, additional, quantity, true, true);
	}
	
	
	/**
	 * Creates a new selection rule that will match either of the specified soldier types.
	 * If a combination is allowed a (partial) combination of both types may be matched, giving preference to the preferred type.
	 * Otherwise a full match of one type or the other will be required (selected based on greatest quantity if both match).
	 * @param preferred First soldier type to match.
	 * @param additional Second soldier type to match.
	 * @param quantity Quantity of soldiers required.
	 * @param isCombination Whether a partial match based on a combination of both types is allowed.
	 */
	public SelectionRule(int preferred, int additional, int quantity, boolean isCombination) {
		this(preferred, additional, null, quantity, isCombination, isCombination);
	}
	
	/**
	 * Creates a new selection rule.
	 * @param primaryType Most preferred soldier type to match.
	 * @param secondaryType Alternate most preferred soldier type to match.
	 * @param additionalTypes Additional soldiers types (all of equal preference) that may be matched; or {@code null} if no additional types can match.
	 * @param quantity Quantity of soldiers required to match this rule.
	 * @param isTypeCombination Indicates if the rule can be matched by multiple types or not.
	 * @param isPartialMatch Indicates if the rule should match if some soldiers can be selected, even if the specified quantity cannot be met.
	 */
	private SelectionRule(int primaryType, int secondaryType, int[] additionalTypes, int quantity, boolean isTypeCombination, boolean isPartialMatch) {
		this.primaryType = primaryType;
		this.secondaryType = secondaryType;
		this.additionalTypes = additionalTypes;
		this.quantity = quantity;
		this.isTypeCombination = isTypeCombination;
		this.isPartialMatch = isPartialMatch;
		selection = new int[quantity];
		soldiers = new int[SoldierType.COUNT];
	}
	
	
	/**
	 * Attempts to find a selection of soldiers that meet this rule.
	 * If a matching selection is found then {@link #selection} will be set to this choice of soldiers. 
	 * @param soldiers Available soldiers to select from.
	 * @return True if this rule can be met by the available soldiers; false otherwise.
	 */
	public boolean match(int[] soldiers) {
		// Make local working copy of the soldiers array.
		// This is required as the selection rule needs to modify its local copy as it selects soldiers to meet rules with a quantity > 1 
		System.arraycopy(soldiers, 0, this.soldiers, 0, soldiers.length);
		Arrays.fill(selection, NONE);
		
		if(isTypeCombination) return combinationMatch();
		else return singleMatch();
	}
	
	/**
	 * Tries to match this rule with a single soldier type and sets the {@link #selection} to the type and quantity that matched (if possible).
	 * @return True if the available soldiers can meet this rule; false otherwise.
	 */
	private boolean singleMatch() {
		
		int maxType;
		
		// NB: currently this does not allow a partial match (unlike combinationMatch).
		
		//  If any soldier is requested, choose the type that there is most of
		if(primaryType == ANY) {
			maxType = maximum(soldiers);
			if(maxType != NONE) {
				Arrays.fill(selection, maxType);
				return true;
			}
		}
		
		// Attempt to satisfy rule with primary or secondary type
		maxType = maximum(soldiers, primaryType, secondaryType);
		if(maxType != NONE && soldiers[maxType] >= quantity) {
			Arrays.fill(selection, maxType);
			return true;
		}
		
		// Attempt to match with additional types
		maxType = maximum(soldiers, additionalTypes);
		if(maxType != NONE && soldiers[maxType] >= quantity) {
			Arrays.fill(selection, maxType);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Tries to match this rule with a combination of different soldier types and sets the {@link #selection} to the combination that matched (if possible).
	 * @return True if the available soldiers can meet this rule; false otherwise.
	 */
	private boolean combinationMatch() {
		int numberSelected = 0;
		
		// This doesn't allow any or none to be specified as primary/secondary type. Maybe it should?
		
		// Select as many soldiers of the preferred type as possible
		if(primaryType != ANY && primaryType != NONE) {
			while(soldiers[primaryType] > 0 && numberSelected < quantity) {
				selection[numberSelected++] = primaryType;
				soldiers[primaryType]--;
			}
		}
		
		// Select as many soldiers of the secondary type as possible
		if(secondaryType != ANY && secondaryType != NONE) {
			while(soldiers[secondaryType] > 0 && numberSelected < quantity) {
				selection[numberSelected++] = secondaryType;
				soldiers[secondaryType]--;
			}
		}
		
		// Select any soldiers of the additional types as required
		while(numberSelected < quantity) {
			int selectedType = maximum(soldiers, additionalTypes);
			if(selectedType == NONE) break;
			selection[numberSelected++] = selectedType;
			soldiers[selectedType]--;
		}
		
		// Determine if rule has been partially or fully matched
		return isPartialMatch ? numberSelected > 0 : numberSelected == quantity;
	}
	
	/**
	 * Finds the type of soldier which is in greatest supply and has at least {@link #quantity} available. If there are multiple soldiers with equal greatest supply,
	 * chooses arbitrarily between them.
	 * @param soldiers Array of counts of available soldiers indexed by soldier type (i.e. {@code Player.soldiers}).
	 * @return Soldier type with the most available soldiers or {@link #NONE} if no soldiers are available.
	 */
	private int maximum(int[] soldiers) {
		
		int maxType = NONE;
		int maxCount = 0;
		
		int len = soldiers.length;
		for(int i = 0; i < len; i++) {
			int count = soldiers[i];
			if(count >= quantity && count > maxCount) {
				maxCount = count;
				maxType = i;
			}
		}
		
		return maxType;
	}
	
	/**
	 * Finds the type of soldiers within the specified types array for which there is the maximum number of soldiers available.
	 * @param soldiers Available soldiers.
	 * @param type1 First type to look for.
	 * @param type2 Second type to look for.
	 * @return Whichever type of {@code type1} or {@code type2} has the most soldiers available; {@link #NONE} if no soldiers of any of the requested types are available.
	 */
	private int maximum(int[] soldiers, int type1, int type2) {
		
		int maxType = NONE;
		int maxCount = 0;
		
		int len = soldiers.length;
		for(int i = 0; i < len; i++) {
			if(i == type1 || i == type2) {
				int count = soldiers[i];
				if(count > maxCount) {
					maxCount = count;
					maxType = i;
				}
			}
		}
		
		return maxType;
	}
	
	/**
	 * Finds the type of soldiers within the specified types array for which there is the maximum number of soldiers available.
	 * @param soldiers Available soldiers.
	 * @param types Soldier types to look for.
	 * @return Whichever type in {@code types} has the most soldiers available; {@link #NONE} if no soldiers of any of the requested types are available.
	 */
	private int maximum(int[] soldiers, int[] types) {
		
		if(types == null || types.length == 0) return NONE;
		
		int maxType = NONE;
		int maxCount = 0;
		
		int len = soldiers.length;
		for(int i = 0; i < len; i++) {
			int typesLength = types.length;
			for(int j = 0; j < typesLength; j++) {
				if(types[j] == i) {
					int count = soldiers[i];
					if(count > maxCount) {
						maxCount = count;
						maxType = i;
					}
				}
			}
		}
		
		return maxType;
	}
}
