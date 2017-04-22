package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.supergreenowl.tunnel.ui.HorizontalAlign;
import com.supergreenowl.tunnel.ui.NumberWriter;
import com.supergreenowl.tunnel.ui.VerticalAlign;

/**
 * A {@link GridItem} that displays a number.
 * @author luke
 *
 */
public class NumberItem extends GridItem {

	private NumberWriter numberWriter;
	
	/** Number displayed. */
	public int number;
	
	protected float numberX, numberY;
	
	/**
	 * Creates a new {@code NumberItem}.
	 * @param background Background texture.
	 * @param numberWriter {@link NumberWriter} used to draw the number. 
	 */
	public NumberItem(TextureRegion background, NumberWriter numberWriter) {
		super(background);
		this.numberWriter = numberWriter;
		this.number = 0;
	}

	@Override
	public void place(float x, float y) {
		super.place(x, y);
		numberX = x + (float)Math.floor(width / 2f);
		numberY = y + (float)Math.floor(height / 2f);
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		numberWriter.write(number, numberX, numberY, batch, HorizontalAlign.Centre, VerticalAlign.Middle);
	}
	
	
	
	

}
