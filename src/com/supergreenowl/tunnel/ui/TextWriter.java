package com.supergreenowl.tunnel.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Provides the ability to write text to the screen using the standard font.
 * The font size is configured to be appropriate for in game high scores.
 * @author luke
 *
 */
public class TextWriter extends Aligned implements Disposable {

	private static final String MEDIEVAL_SHARP_FONT = "fonts/medievalsharp.fnt";
	private static final String MEDIEVAL_SHARP_BMP = "fonts/medievalsharp.png";
	
	private static final int GOLD_VALUE = 0xdaa520ff;
	private static final Color GOLD = ColourFactory.rgbToColour(GOLD_VALUE);
	
	private final BitmapFont medievalSharp;
	
	public TextWriter() {
		medievalSharp = new BitmapFont(Gdx.files.internal(MEDIEVAL_SHARP_FONT), Gdx.files.internal(MEDIEVAL_SHARP_BMP), false);
		medievalSharp.setColor(GOLD);
	}
	
	/**
	 * Writes text using the default alignment settings.
	 * @param text Text to display.
	 * @param batch SpriteBatch to draw to.
	 * @param x Location to draw.
	 * @param y Location to draw.
	 * @see #setDefaultAlignment(HorizontalAlign, VerticalAlign)
	 */
	public void write(String text, SpriteBatch batch, float x, float y) {
		write(text, batch, x, y, defaultHorizontalAlign, defaultVerticalAlign);
	}
	
	/**
	 * Writes text.
	 * @param text Text to display.
	 * @param batch SpriteBatch to draw to.
	 * @param x Location to draw.
	 * @param y Location to draw.
	 * @param hAlign Horizontal alignment of the text. Determines how the text is positioned relative to the x coordinate.
	 * @param vAlign Vertical alignment of the text. Determines how the text is positioned relative to the y coordinate.
	 */
	public void write(String text, SpriteBatch batch, float x, float y, HorizontalAlign hAlign, VerticalAlign vAlign) {
		BitmapFont.TextBounds bounds = medievalSharp.getBounds(text);
		
		float textX = x;
		float textY = y;
		
		if(hAlign == HorizontalAlign.Left) textX = x;
		else if(hAlign == HorizontalAlign.Centre) textX = x - (bounds.width / 2f);
		else if(hAlign == HorizontalAlign.Right) textX = x - bounds.width;
		
		if(vAlign == VerticalAlign.Top) textY = y;
		else if(vAlign == VerticalAlign.Middle) textY = y + (bounds.height / 2f);
		else if(vAlign == VerticalAlign.Baseline) textY = y + bounds.height;
		
		medievalSharp.draw(batch, text, textX, textY);
	}
			
	@Override
	public void dispose() {
		medievalSharp.dispose();
	}
	
	
	
}
