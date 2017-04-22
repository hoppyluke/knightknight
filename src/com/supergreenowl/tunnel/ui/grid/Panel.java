package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A {@link ContainerItem} that lays out its components horizontally centred relative to the background but vertically adjacent to each other.
 * @author luke
 *
 */
public class Panel extends ContainerItem {

	public Panel(TextureRegion... regions) {
		super(regions);
	}

	@Override
	public void place(float x, float y) {
		this.x = x;
		this.y = y;
		
		// Calculate total width and height of contents
		int w = 0;
		int n = contents.size;
		for(int i = 0; i < n; i++) {
			GridItem item = contents.get(i);
			w += item.width; // total width = sum(width) 
		}
		
		// Centre all contents based on the total width
		float itemX = x + (float)Math.floor((width - w) / 2f);
		
		for(int i = 0; i < n; i++) {
			GridItem item = contents.get(i);
			
			float itemY = y + (float)Math.floor((height - item.height) / 2f);
			item.place(itemX, itemY);
			itemX += item.width;
		}
	}

	
	
}
