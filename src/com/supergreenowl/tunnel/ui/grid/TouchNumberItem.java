package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.supergreenowl.tunnel.ui.HorizontalAlign;
import com.supergreenowl.tunnel.ui.NumberWriter;
import com.supergreenowl.tunnel.ui.VerticalAlign;

public class TouchNumberItem extends NumberItem implements TouchListener {

	public boolean isDisabled;
	
	private NumberWriter disabledWriter;
	private ClickListener clickListener;
	
	public TouchNumberItem(TextureRegion background, NumberWriter enabledWriter, NumberWriter disabledWriter) {
		super(background, enabledWriter);
		this.disabledWriter = disabledWriter;
		this.isDisabled = false;
		setTouchListener(this);
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		if(isDisabled) {
			// Draw disabled number
			batch.draw(texture, x, y);
			disabledWriter.write(number, numberX, numberY, batch, HorizontalAlign.Centre, VerticalAlign.Middle);
		}
		else {
			// Draw enabled number
			super.draw(batch);
		}
		
	}

	/**
	 * Fires the on click event if this item is not disabled.
	 */
	@Override
	public void onTouch(GridItem sender, float x, float y) {
		if(!isDisabled && clickListener != null) {
			clickListener.onClick(this);
		}
	}
	
	/**
	 * Sets the click listener for this item.
	 * @param clickListener New listener or {@code null} to remove listener.
	 */
	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}
}
