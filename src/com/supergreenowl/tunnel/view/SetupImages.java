package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Loads and provides images used to render the {@link SetupScreen}.
 * @author luke
 *
 */
public class SetupImages implements Disposable {

	private static final String FILE_NAME = "setup.png";
	
	private static final int ATLAS_HEIGHT = 640;
	
	private static final int W1 = 128;
	private static final int W2 = 256;
	private static final int W3 = 384;
	private static final int W4 = 512;
	private static final int H2 = 64;
	private static final int H3 = 96;
	
	private static final int BRICK_Y1 = ATLAS_HEIGHT - H3;
	private static final int BRICK_Y2 = ATLAS_HEIGHT - H3 - H3;
	private static final int BRICK_Y3 = ATLAS_HEIGHT - 4 * H3 - 4 * H2;
	
	private static final int TEXT_Y1 = ATLAS_HEIGHT - 2 * H3 - H2;
	private static final int TEXT_Y2 = ATLAS_HEIGHT - 2 * H3 - 2 * H2;
	private static final int TEXT_Y3 = ATLAS_HEIGHT - 2 * H3 - 3 * H2;
	private static final int TEXT_Y4 = ATLAS_HEIGHT - 2 * H3 - 4 * H2;
	
	private static final int SHIELD_Y = ATLAS_HEIGHT - 3 * H3 - 4 * H2;
	
	private static final int X_0 = 0;
	private static final int X_10 = W2;
	private static final int X_15 = W2 + W1;
	private static final int X_20 = 2 * W2;
	private static final int X_25 = 2 * W2 + W1;
	
	public final TextureRegion title, wealth, knights, players, difficulty;
	public final TextureRegion low, medium, high, easy, hard, p1, p2;
	public final TextureRegion shieldsRGB, shieldsW, shieldsCMY;
	public final TextureRegion back, forward, na;
	public final TextureRegion brick, brickSmall, brickActive, brickActiveSmall, brickLarge, brickHuge;
	
	
	private final Texture image;
	
	public SetupImages() {
		image = new Texture(Gdx.files.internal(FILE_NAME));
		
		easy = new TextureRegion(image, X_0, TEXT_Y1, W2, H2);
		medium = new TextureRegion(image, X_10, TEXT_Y1, W2, H2);
		hard = new TextureRegion(image, X_20, TEXT_Y1, W2, H2);
		
		low = new TextureRegion(image, X_0, TEXT_Y2, W2, H2);
		high = new TextureRegion(image, X_10, TEXT_Y2, W2, H2);
		p1 = new TextureRegion(image, X_20, TEXT_Y2, W1, H2);
		p2 = new TextureRegion(image, X_25, TEXT_Y2, W1, H2);
		
		knights = new TextureRegion(image, X_0, TEXT_Y3, W4, H2);
		wealth = new TextureRegion(image, X_20, TEXT_Y3, W2, H2);
		
		difficulty = new TextureRegion(image, X_0, TEXT_Y4, W3, H2);
		players = new TextureRegion(image, X_20, TEXT_Y4, W2, H2);
		na = new TextureRegion(image, X_15, TEXT_Y4, W1, H2);
		
		shieldsRGB = new TextureRegion(image, X_0, SHIELD_Y, W2, H3);
		shieldsW = new TextureRegion(image, X_10, SHIELD_Y, W2, H3);
		shieldsCMY = new TextureRegion(image, X_20, SHIELD_Y, W2, H3);
		
		back = new TextureRegion(image, X_20, BRICK_Y2, W1, H3);
		forward = new TextureRegion(image, X_25, BRICK_Y2, W1, H3);
		
		brick = new TextureRegion(image, X_0, BRICK_Y1, W2, H3);
		brickActive = new TextureRegion(image, X_10, BRICK_Y1, W2, H3);
		brickSmall = new TextureRegion(image, X_20, BRICK_Y1, W1, H3);
		brickActiveSmall = new TextureRegion(image, X_25, BRICK_Y1, W1, H3);
		
		brickLarge = new TextureRegion(image, X_0, BRICK_Y2, W3, H3);
		brickHuge = new TextureRegion(image, X_0, BRICK_Y3, W4, H3);
		
		title = new TextureRegion(image, X_20, BRICK_Y3, W2, H3);
	}
	
	@Override
	public void dispose() {
		image.dispose();
	}

}
