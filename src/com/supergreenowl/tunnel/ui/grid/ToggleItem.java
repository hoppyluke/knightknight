package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.supergreenowl.tunnel.ui.ColourFactory;

/**
 * A {@link GridItem} that holds a state and toggles its state when it is touched.
 * @author luke
 *
 * @param <E> {@code Enum} that represents the different state values available.
 */
public abstract class ToggleItem<E extends Enum<E>> extends TouchItem {
	
	/**
	 * Current state.
	 */
	public E state;
	
	/**
	 * Whether this item is disabled or not.
	 */
	public boolean isDisabled;
	
	private TextureRegion stateDisplay;
	private TextureRegion disabledRegion;
	
	private float stateX, stateY, disabledX, disabledY;
	
	public ToggleItem(TextureRegion background, E state) {
		super(background);
		this.state = state;
		this.isDisabled = false;
		this.disabledRegion = null;
		updateStateDisplay();
	}

	/**
	 * Calculates the next state of this item based on the current state.
	 * @return
	 */
	public abstract E nextState();

	/**
	 * Gets the {@code TextureRegion} that should be used to represent the current state of this item.
	 * @return
	 */
	public abstract TextureRegion getRegion();
	
	/**
	 * Sets the texture used to represent the state when this item is disabled.
	 * This texture will be drawn instead of the current state when the item is disabled.
	 * @param disabled Texture to represent disabled state or {@code null} to indicate that the current state should be used even when the item
	 * is disabled.
	 */
	public void setDisabledStateDisplay(TextureRegion disabled) {
		this.disabledRegion = disabled;
	}
	
	@Override
	public void onTouch(GridItem sender, float x, float y) {
		if(!isDisabled) {
			state = nextState();
			updateStateDisplay();
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		
		Color previousColor = batch.getColor();
		if(isDisabled && disabledRegion == null) {
			batch.setColor(ColourFactory.DISABLED_COLOUR);
		}
		
		super.draw(batch);
		
		if(isDisabled && disabledRegion != null) batch.draw(disabledRegion, disabledX, disabledY);
		else batch.draw(stateDisplay, stateX, stateY);	
		
		if(isDisabled && disabledRegion == null) {
			batch.setColor(previousColor);
		}
	}

	@Override
	public void place(float x, float y) {
		super.place(x, y);
		updateStateDisplay();
		
		if(disabledRegion != null) {
			disabledX = (float)Math.floor(this.x + (texture.getRegionWidth() - disabledRegion.getRegionWidth()) / 2f);
			disabledY = (float)Math.floor(this.y + (texture.getRegionHeight() - disabledRegion.getRegionHeight()) / 2f);
		}
	}

	/**
	 * Updates the {@code TextureRegion} used to represent the state of this item.
	 */
	private void updateStateDisplay() {
		stateDisplay = getRegion();
		
		stateX = (float) Math.floor(this.x + ((texture.getRegionWidth() - stateDisplay.getRegionWidth()) / 2f));
		stateY = (float) Math.floor(this.y + ((texture.getRegionHeight() - stateDisplay.getRegionHeight()) / 2f));
	}
}