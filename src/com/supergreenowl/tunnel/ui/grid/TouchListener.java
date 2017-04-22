package com.supergreenowl.tunnel.ui.grid;

/**
 * Listener that responds to touch events for {@link GridItem} objects.
 * @author luke
 *
 */
public interface TouchListener {

	/**
	 * Logic to run when a {@link GridItem} is touched.
	 * @param sender {@code GridItem} that was touched.
	 * @param x Touch location in viewport space.
	 * @param y Touch location in viewport space.
	 */
	void onTouch(GridItem sender, float x, float y);
	
}
