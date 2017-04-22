package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * A {@link GridItem} that can contain other items. Contained items are centred relative to the parent container.
 * Items added to the container are drawn in the order they were added (so the item added last is drawn on top).
 * Note that since the container is a {@code GridItem}, it has its own graphical representation.
 * @author luke
 *
 */
public class ContainerItem extends GridItem {

	protected Array<GridItem> contents;
	
	public ContainerItem(TextureRegion textureRegion) {
		super(textureRegion);
		contents = new Array<GridItem>(5);
	}
	
	public ContainerItem(TextureRegion... regions) {
		this(regions[0]);
		
		for(int i = 1; i < regions.length; i++) {
			add(regions[i]);
		}
	}
	
	/**
	 * Adds an item to this container.
	 * @param item
	 */
	public void add(GridItem item) {
		contents.add(item);
	}
	
	public void add(TextureRegion texture) {
		contents.add(new GridItem(texture));
	}

	@Override
	public void place(float x, float y) {
		super.place(x, y);
		
		int n = contents.size;
		for(int i = 0; i < n; i++) {
			// Centre child items relative to this container
			GridItem item = contents.get(i);
			float itemX = (float) Math.floor(x + ((float)(width - item.width) / 2f));
			float itemY = (float) Math.floor(y + ((float)(height - item.height) / 2f));
			item.place(itemX, itemY);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		int n = contents.size;
		for(int i = 0; i < n; i++) {
			// Draw child items
			GridItem item = contents.get(i);
			item.draw(batch);
		}
	}
	
	

}
