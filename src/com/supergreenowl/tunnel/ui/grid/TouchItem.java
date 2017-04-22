package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A {@link GridItem} that acts as its own {@link TouchListener}.
 * @author luke
 * @see GridItem#setTouchListener(TouchListener)
 */
public abstract class TouchItem extends GridItem implements TouchListener {

	public TouchItem(TextureRegion textureRegion) {
		super(textureRegion);
		setTouchListener(this);
	}

	@Override
	public abstract void onTouch(GridItem sender, float x, float y);

}
