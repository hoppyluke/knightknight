package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Button extends TouchItem {

	private TextureRegion enabled, disabled;
	
	/** Whether this button is disabled. */
	public boolean isDisabled;
	
	private float enabledX, enabledY, disabledX, disabledY;
	
	private ClickListener clickListener;
	
	public Button(TextureRegion background, TextureRegion enabled, TextureRegion disabled) {
		super(background);
		this.enabled = enabled;
		this.disabled = disabled;
		this.isDisabled = false;
		this.clickListener = null;
	}
	
	/**
	 * Sets the click listener for this button.
	 * @param clickListener New listener or {@code null} to remove listener.
	 */
	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public void place(float x, float y) {
		super.place(x, y);
		
		int bgWidth = texture.getRegionWidth();
		int bgHeight = texture.getRegionHeight();
		
		enabledX = (float) Math.floor(x + ((bgWidth - enabled.getRegionWidth()) / 2f));
		enabledY = (float) Math.floor(y + ((bgHeight - enabled.getRegionHeight()) / 2f));
		
		disabledX = (float) Math.floor(x + ((bgWidth - disabled.getRegionWidth()) / 2f));
		disabledY = (float) Math.floor(y + ((bgHeight - disabled.getRegionHeight()) / 2f));
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		if(isDisabled) batch.draw(disabled, disabledX, disabledY);
		else batch.draw(enabled, enabledX, enabledY);
	}

	@Override
	public void onTouch(GridItem sender, float x, float y) {
		if(!isDisabled && clickListener != null) clickListener.onClick(this);
	}
}
