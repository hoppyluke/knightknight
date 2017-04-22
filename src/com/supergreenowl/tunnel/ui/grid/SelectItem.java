package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A {@link GridItem} that can be selected or unselected. The selection state is toggled by touching the item.
 * Can also be disabled which prevents selection.
 * @author luke
 *
 */
public class SelectItem extends TouchItem {

	/** Whether or not this item is currently selected. */
	public boolean isSelected;
	
	/** Whether or not this item is disabled. Disabling the item prevents selection. */
	public boolean isDisabled;
	
	private SelectionListener selectionListener;
	private TextureRegion selectedBackground, foreground, disabledForeground;
	private float enabledX, enabledY, disabledX, disabledY;
	
	/**
	 * Creates a new {@code SelectItem} that is not selected and is enabled.
	 * @param background Background image for the item when not selected.
	 * @param selectedBackground Background image for the item when selected.
	 * @param foreground Foreground image when enabled.
	 * @param disabledForeground Foreground image when disabled.
	 */
	public SelectItem(TextureRegion background, TextureRegion selectedBackground, TextureRegion foreground, TextureRegion disabledForeground) {
		super(background);
		
		this.selectedBackground = selectedBackground;
		this.foreground = foreground;
		this.disabledForeground = disabledForeground;
		
		this.isSelected = false;
		this.isDisabled = false;
		this.selectionListener = null;
	}
	
	/**
	 * Sets the listener that will receive selection events from this item.
	 * @param selectionListener New listener or {@code null} to remove the listener.
	 */
	public void setSelectionListener(SelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}
	
	@Override
	public void place(float x, float y) {
		super.place(x, y);
		
		int bgWidth = texture.getRegionWidth();
		int bgHeight = texture.getRegionHeight();
		
		enabledX = (float) Math.floor(x + ((bgWidth - foreground.getRegionWidth()) / 2f));
		enabledY = (float) Math.floor(y + ((bgHeight - foreground.getRegionHeight()) / 2f));
		
		disabledX = (float) Math.floor(x + ((bgWidth - disabledForeground.getRegionWidth()) / 2f));
		disabledY = (float) Math.floor(y + ((bgHeight - disabledForeground.getRegionHeight()) / 2f));
	}

	@Override
	public void draw(SpriteBatch batch) {
		if(isSelected) batch.draw(selectedBackground, x, y);
		else super.draw(batch);
		
		if(isDisabled) batch.draw(disabledForeground, disabledX, disabledY);
		else batch.draw(foreground, enabledX, enabledY);
	}

	/**
	 * Toggles selection of this item when it is touched.
	 */
	@Override
	public void onTouch(GridItem sender, float x, float y) {
		if(!isDisabled) {
			isSelected = !isSelected;
			if(selectionListener != null) {
				if(isSelected) selectionListener.onSelected(this);
				else selectionListener.onUnselected(this);
			}
		}
	}
}
