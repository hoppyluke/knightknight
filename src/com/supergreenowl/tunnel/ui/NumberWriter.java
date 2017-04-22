package com.supergreenowl.tunnel.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntArray;

/**
 * Draws numbers using {@code TextureRegions} as number glyphs.
 * @author luke
 *
 */
public class NumberWriter extends Aligned {

	private TextureRegion[] digits;
	private int[] width;
	private int height;
	
	private IntArray toDraw;
	
	/**
	 * Creates a new number writer.
	 * @param width Widths of the TextureRegions for each digit 0-9.
	 * @param height Height of digits (all digits are assumed as the same height).
	 * @param digits Images to draw each digit 0-9.
	 * @param bufferSize Initial capacity of the buffer used when rendering digits. Should be to the max expected length of numbers rendered (for example 2 for numbers < 100).
	 */
	public NumberWriter(int[] width, int height, TextureRegion[] digits, int bufferSize) {
		this.width = width;
		this.height = height;
		this.digits = digits;
		
		toDraw = new IntArray(bufferSize);
	}
	
	/**
	 * Draws a number anchored at the specified coordinate using the default alignment settings.
	 * @param n Number to draw.
	 * @param x Location of anchor point.
	 * @param y Location of anchor point.
	 * @param batch Sprite batch to draw to. 
	 */
	public void write(int n, float x, float y, SpriteBatch batch) {
		write(n, x, y, batch, defaultHorizontalAlign, defaultVerticalAlign);
	}
	
	/**
	 * Draws a number anchored at the specified coordinate.
	 * @param n Number to draw.
	 * @param x Location of anchor point.
	 * @param y Location of anchor point.
	 * @param batch Sprite batch to draw to.
	 * @param hAlign Horizontal alignment of number relative to anchor point.
	 * @param vAlign Vertical alignment of number relative to anchor point. 
	 */
	public void write(int n, float x, float y, SpriteBatch batch, HorizontalAlign hAlign, VerticalAlign vAlign) {
		
		String s = Integer.toString(n);
		int len = s.length();
		int w = 0;
		int d;
		
		// Loop through number once to calculate bounds
		for(int i = 0; i < len; i++) {
			d = Integer.parseInt(Character.toString(s.charAt(i)));
			if(toDraw.size <= i) toDraw.add(d);
			else toDraw.set(i, d);
			w += width[d];
		}
		
		float nx = x;
		float ny = y;
		
		if(hAlign == HorizontalAlign.Left) nx = x; 
		else if(hAlign == HorizontalAlign.Centre) nx = (float)Math.floor(x - (w / 2f)); // Drawing at 0.5 pixel coordinate looks bad!
		else if(hAlign == HorizontalAlign.Right) nx = x - w;
		
		if(vAlign == VerticalAlign.Baseline) ny = y;
		else if(vAlign == VerticalAlign.Middle) ny = (float)Math.floor(y - (height / 2f));
		else if(vAlign == VerticalAlign.Top) ny = y - height;
		
		for(int i = 0; i < len; i++) {
			d = toDraw.get(i);
			batch.draw(digits[d], nx, ny, width[d], height);
			nx += width[d];
		}
	}
	
}
