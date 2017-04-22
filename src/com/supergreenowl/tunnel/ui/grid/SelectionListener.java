package com.supergreenowl.tunnel.ui.grid;

/**
 * Listener that receives events from a {@link SelectItem} when it is selected or unselected.
 * @author luke
 *
 */
public interface SelectionListener {

	/**
	 * Event fired when a {@code SelectItem} is selected.
	 * @param sender Item that was selected.
	 */
	void onSelected(SelectItem sender);
	
	/**
	 * Event fired when a {@code SelectItem} is unselected.
	 * @param sender Item that was unselected.
	 */
	void onUnselected(SelectItem sender);
	
}
