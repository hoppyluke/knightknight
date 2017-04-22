package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Provides and loads images for the level select screen.
 * @author luke
 *
 */
public class LevelSelectImages implements Disposable {

	private static final String FILE_NAME = "levelselect.png";
	
	private static final int ATLAS_HEIGHT = 256;
	
	private static final int W1 = 128;
	private static final int W2 = 256;
	
	private static final int H2 = 64;
	private static final int H3 = 96;
	
	private static final int X_00 = 0;
	private static final int X_10 = W2;
	private static final int X_15 = W2 + W1;
	private static final int X_20 = W2 + W2;
	
	private static final int X_NUMBERS = 320;
	
	private static final int Y_BRICKS = ATLAS_HEIGHT - H3;
	private static final int Y_NUMBERS = ATLAS_HEIGHT - H3 - H2;
	private static final int Y_TITLE = ATLAS_HEIGHT - H3 - H2 - H3;
	
	public static final int[] NUMBER_WIDTH = new int[] { 33, 22, 31, 30, 29, 30, 32, 30, 30, 32 };
	public static final int NUMBER_HEIGHT = H2;
	private static final int[] NUMBER_X = calculateNumberX(X_00);
	private static final int[] NUMBER_DISABLED_X = calculateNumberX(X_NUMBERS);
	
	private final Texture image;
	
	public final TextureRegion brick, brickSmall, brickActive;
	public final TextureRegion title, back, forward, forwardDisabled;
	public final TextureRegion[] digits, disabledDigits;
	
	public LevelSelectImages() {
		image = new Texture(Gdx.files.internal(FILE_NAME));
		
		brick = new TextureRegion(image, X_00, Y_BRICKS, W2, H3);
		brickSmall = new TextureRegion(image, X_10, Y_BRICKS, W1, H3);
		brickActive = new TextureRegion(image, X_15, Y_BRICKS, W1, H3);
		
		title = new TextureRegion(image, X_00, Y_TITLE, W2, H3);
		back = new TextureRegion(image, X_10, Y_TITLE, W1, H3);
		forward = new TextureRegion(image, X_15, Y_TITLE, W1, H3);
		forwardDisabled = new TextureRegion(image, X_20, Y_TITLE, W1, H3);
		
		digits = new TextureRegion[10];
		disabledDigits = new TextureRegion[10];
		for(int i = 0; i < 10; i++) {
			digits[i] = new TextureRegion(image, NUMBER_X[i], Y_NUMBERS, NUMBER_WIDTH[i], H2);
			disabledDigits[i] = new TextureRegion(image, NUMBER_DISABLED_X[i], Y_NUMBERS, NUMBER_WIDTH[i], H2);
		}
	}
	
	@Override
	public void dispose() {
		image.dispose();
	}
	
	/**
	 * Calculates the x coordinates within the texture atlas for the number glyphs based on their widths.
	 * @return x coordinates of each number glyph.
	 */
	private static int[] calculateNumberX(int start) {
		int[] xs = new int[10];
		
		int x = start;
		
		for(int i = 0; i < 10; i++) {
			xs[i] = x;
			x += NUMBER_WIDTH[i];
		}
		
		return xs;
	}

}
