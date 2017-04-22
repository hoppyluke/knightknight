package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.supergreenowl.tunnel.model.SoldierType;

/**
 * Loads and provides textures for the soldier select screen.
 * @author luke
 *
 */
public class SoldierSelectImages implements Disposable {

	private static final String FILE_NAME = "soldierselect.png";
	
	private static final int ATLAS_HEIGHT = 576;
	
	private static final int H = 96;
	
	private static final int W1 = 128;
	private static final int W2 = 256;
	private static final int W3 = 384;
	private static final int W4 = 512;
	
	private static final int W00 = 96;
	private static final int W05 = W00 + W1;
	
	private static final int X_00 = 0;
	private static final int X_05 = W1;
	private static final int X_10 = W2;
	private static final int X_15 = W3;
	private static final int X_20 = W4;
	private static final int X_25 = W4 + W1;
	private static final int X_27 = W00 * 7;
	private static final int X_30 = W4 + W2;
	
	private static final int Y_0 = ATLAS_HEIGHT - H;
	private static final int Y_1 = ATLAS_HEIGHT - H - H;
	private static final int Y_2 = ATLAS_HEIGHT - H * 3;
	private static final int Y_3 = ATLAS_HEIGHT - H * 4;
	private static final int Y_4 = ATLAS_HEIGHT - H * 5;
	private static final int Y_5 = ATLAS_HEIGHT - H * 6;
	
	public static final int[] NUMBER_WIDTH = new int[] { 33, 22, 31, 30, 29, 30, 32, 30, 30, 32 };
	public static final int NUMBER_HEIGHT = H;
	private static final int NUMBER_X_START = X_20;
	private static final int[] NUMBER_X = calculateNumberX();
	
	private final Texture image;
	
	public final TextureRegion brick, brickActive, brickSmall, brickActiveSmall, brickSelectedSmall, brickLarge;
	public final TextureRegion[] digits;
	public final TextureRegion[] shields, disabledShields;
	public final TextureRegion p1, p2, title;
	public final TextureRegion buy, buyDisabled, sell, sellDisabled, wealth;
	public final TextureRegion back, forward, forwardDisabled;
	public final TextureRegion auto, autoDisabled;
	
	public SoldierSelectImages() {
		image = new Texture(Gdx.files.internal(FILE_NAME));
		
		brick = new TextureRegion(image, X_00, Y_0, W2, H);
		brickActive = new TextureRegion(image, X_10, Y_0, W2, H);
		brickSmall = new TextureRegion(image, X_20, Y_0, W1, H);
		brickActiveSmall = new TextureRegion(image, X_25, Y_0, W1, H);
		brickSelectedSmall = new TextureRegion(image, X_30, Y_0, W1, H);
		brickLarge = new TextureRegion(image, X_00, Y_1, W4, H);
		
		digits = new TextureRegion[10];
		for(int i = 0; i < 10; i++) {
			digits[i] = new TextureRegion(image, NUMBER_X[i], Y_1, NUMBER_WIDTH[i], H);
		}
		
		shields = new TextureRegion[SoldierType.COUNT];
		disabledShields = new TextureRegion[SoldierType.COUNT];
		for(int i = 0; i < SoldierType.COUNT; i++) {
			shields[i] = new TextureRegion(image, W00 * i, Y_2, W00, H);
			disabledShields[i] = new TextureRegion(image, W00 * i, Y_3, W00, H);
		}
		
		p1 = new TextureRegion(image, X_00, Y_4, W1, H);
		p2 = new TextureRegion(image, X_20, Y_4, W1, H);
		title = new TextureRegion(image, X_05, Y_4, W3, H);
		
		back = new TextureRegion(image, X_25, Y_4, W1, H);
		forward = new TextureRegion(image, X_30, Y_4, W1, H);
		forwardDisabled = new TextureRegion(image, X_30, Y_5, W1, H);
		
		buy = new TextureRegion(image, X_00, Y_5, W1, H);
		sell = new TextureRegion(image, X_05, Y_5, W1, H);
		buyDisabled = new TextureRegion(image, X_10, Y_5, W1, H);
		sellDisabled = new TextureRegion(image, X_15, Y_5, W1, H);
		wealth = new TextureRegion(image, X_20, Y_5, W2, H);
		
		auto = new TextureRegion(image, X_27, Y_2, W05, H);
		autoDisabled = new TextureRegion(image, X_27, Y_3, W05, H);
	}

	@Override
	public void dispose() {
		image.dispose();
	}

	/**
	 * Calculates the x coordinates within the texture atlas for the number glyphs based on their widths.
	 * @return x coordinates of each number glyph.
	 */
	private static int[] calculateNumberX() {
		int[] xs = new int[10];
		
		int x = NUMBER_X_START;
		
		for(int i = 0; i < 10; i++) {
			xs[i] = x;
			x += NUMBER_WIDTH[i];
		}
		
		return xs;
	}
}
