package com.supergreenowl.tunnel.ui;

import com.badlogic.gdx.graphics.Color;

/**
 * Provides methods to create {@link Color} instances.
 * @author luke
 *
 */
public class ColourFactory {

	private static final int ALPHA_OPAQUE = 255;
	private static final float COLOUR_MAX = 255f;
	
	/** Screen background colour for menu screens. */
	public static final Color MENU_BACKGROUND = rgbToColour(50, 50, 50);
	
	/** Tint to use when rendering disabled UI items. */
	public static final Color DISABLED_COLOUR = new Color(0.45f, 0.45f, 0.45f, 1f);
	
	private ColourFactory() {
		// Private default constructor.
	}
	
	/**
	 * Creates a new opaque {@code Color} from 24-bit RGB values.
	 * @param r Red component of the colour in the interval [0-255].
	 * @param g Green component of the colour in the interval [0-255].
	 * @param b Blue component of the colour in the interval [0-255].
	 * @return {@code Color} object representing the specified colour.
	 */
	public static Color rgbToColour(int r, int g, int b) {
		return rbgToColour(r, g, b, ALPHA_OPAQUE);
	}
	
	/**
	 * Creates a new {@code Color} from 24-bit RGB values.
	 * @param r Red component of the colour in the interval [0-255].
	 * @param g Green component of the colour in the interval [0-255].
	 * @param b Blue component of the colour in the interval [0-255].
	 * @param a Alpha component of the colour in the interval [0-255].
	 * @return {@code Color} object representing the specified colour.
	 */
	public static Color rbgToColour(int r, int g, int b, int a) {
		float red = (float)r / COLOUR_MAX;
		float green = (float)g / COLOUR_MAX;
		float blue = (float)b / COLOUR_MAX;
		float alpha = (float)a / COLOUR_MAX;
		return new Color(red, green, blue, alpha);
	}
	
	/**
	 * Creates a new {@link Color} instance that corresponds to the specified RGBA8888 value. 
	 * @param value Colour in hexadecimal RGBA8888 format (i.e. 0xRRGGBBAA).
	 * @return Color instance representing this colour.
	 */
	public static Color rgbToColour(int value) {
		Color c = new Color();
		Color.rgba8888ToColor(c, value);
		return c;
	}
	
}
