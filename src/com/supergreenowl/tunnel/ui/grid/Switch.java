package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A {@link GridItem} that represents a on/off setting. The item has texture regions to represent the two states
 * and toggles state when touched. Provides the same functionality as {@link ToggleItem} but for a Boolean rather than an enum.
 * @author luke
 */
public class Switch extends TouchItem {

	public boolean isOn;
	
	private TextureRegion on, off;
	private float onX, onY, offX, offY;
	
	private SwitchListener switchListener;
	
	public Switch(TextureRegion background, TextureRegion on, TextureRegion off, boolean isOn) {
		super(background);
		this.on = on;
		this.off = off;
		this.isOn = isOn;

		this.switchListener = null;
	}

	@Override
	public void place(float x, float y) {
		super.place(x, y);
		
		int bgWidth = texture.getRegionWidth();
		int bgHeight = texture.getRegionHeight();
		
		onX = (float) Math.floor(x + ((bgWidth - on.getRegionWidth()) / 2f));
		onY = (float) Math.floor(y + ((bgHeight - on.getRegionHeight()) / 2f));
		
		offX = (float) Math.floor(x + ((bgWidth - off.getRegionWidth()) / 2f));
		offY = (float) Math.floor(y + ((bgHeight - off.getRegionHeight()) / 2f));
	}
	
	/**
	 * Sets the listener that will receive the switch event from this switch.
	 * @param switchListener New listener or {@code null} to remove the listener.
	 */
	public void setSwitchListener(SwitchListener switchListener) {
		this.switchListener = switchListener;
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		if(isOn) batch.draw(on, onX, onY);
		else batch.draw(off, offX, offY);
	}

	@Override
	public void onTouch(GridItem sender, float x, float y) {
		isOn = !isOn;
		if(switchListener != null) switchListener.onSwitch(this);
	}
}
