package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A graphical item that can be positioned on a {@link GridScreen}.
 * @author luke
 *
 */
public class GridItem {
	
	/** Width in viewport units. */
	public int width;
	
	/** Height in viewport units. */
	public int height;
	
	/** Location in viewport space. */
	public float x;
	
	/** Location in viewport space. */
	public float y;
	
	/** The {@link GridScreen} that this item is displayed on. */
	public GridScreen parent;
	
	protected TextureRegion texture;
	
	/** Whether this item responds to touch events. */
	private boolean isTouchable;
	private TouchListener touchListener;
	
	public GridItem(TextureRegion textureRegion) {
		this.texture = textureRegion;
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		this.isTouchable = false;
		this.touchListener = null;
	}
	
	/**
	 * Updates the location of this grid item. Coordinates are in viewport space.
	 * @param x
	 * @param y
	 */
	public void place(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Renders this item to the specified batch.
	 * @param batch
	 */
	public void draw(SpriteBatch batch) {
		batch.draw(texture, x, y);
	}
	
	/**
	 * Sets the {@link TouchListener} for this item.
	 * @param listener New {@code TouchListener} or {@code null} to remove the listener.
	 */
	public void setTouchListener(TouchListener listener) {
		this.touchListener = listener;
		this.isTouchable = listener != null;
	}
	
	/**
	 * Determines if this item is touched by a touch at the specified location.
	 * @param x x coordinate of touch in viewport space.
	 * @param y y coordinate of touch in viewport space.
	 * @return True if this item is touchable and was touched; false otherwise.
	 */
	public boolean isTouched(float x, float y) {
		if(!isTouchable) return false;
		
		float xMax = this.x + (float)width;
		float yMax = this.y + (float)height;
		
		return this.x <= x && x <= xMax && this.y <= y && y <= yMax;
	}
	
	/**
	 * Notifies the {@code TouchListener} (if any) for this item that the item was touched.
	 * @param x
	 * @param y
	 */
	public void touch(float x, float y) {
		if(isTouchable && touchListener != null) {
			touchListener.onTouch(this, x, y);
		}
	}
	
}
