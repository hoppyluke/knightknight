package com.supergreenowl.tunnel.ui.grid;

/**
 * Listener that recies events from a {@link Switch}.
 * @author luke
 * @see Switch
 */
public interface SwitchListener {

	/**
	 * Event triggered when the switch is turned on or off.
	 * @param sender Switch that was switched.
	 */
	void onSwitch(Switch sender);
}
