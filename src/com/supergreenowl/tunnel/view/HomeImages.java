package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Loads and provides the textures used to render the {@link HomeScreen}.
 * @author luke
 *
 */
public class HomeImages implements Disposable {

	private static final String FILE_NAME = "home.png";
	
	private static final int ATLAS_HEIGHT = 480;
	
	private static final int W1 = 128;
	private static final int W2 = 256;
	private static final int W6 = 768;
	
	//private static final int H2 = 64;
	private static final int H3 = 96;
	private static final int H6 = 192;
	
	private static final int X_00 = 0;
	private static final int X_05 = W1;
	private static final int X_10 = W2;
	private static final int X_15 = W1 + W2;
	private static final int X_20 = W2 + W2;
	private static final int X_25 = W2 + W2 + W1;
	
	private static final int BRICKS_Y = ATLAS_HEIGHT - H3;
	private static final int TITLE_Y = ATLAS_HEIGHT - H3 - H6;
	private static final int BUTTONS_Y = ATLAS_HEIGHT - H3 - H6 - H3;
	private static final int TEXT_Y = ATLAS_HEIGHT - H3 - H6 - H3 - H3;
	
	private final Texture image;
	
	public final TextureRegion brick, brickActive, brickSmall, brickActiveSmall;
	public final TextureRegion levels, quickPlay, credits, help;
	public final TextureRegion soundOn, soundOff;
	public final TextureRegion title;
	
	public HomeImages() {
		image = new Texture(Gdx.files.internal(FILE_NAME));
		
		brick = new TextureRegion(image, X_00, BRICKS_Y, W2, H3);
		brickActive = new TextureRegion(image, X_10, BRICKS_Y, W2, H3);
		brickSmall = new TextureRegion(image, X_20, BRICKS_Y, W1, H3);
		brickActiveSmall = new TextureRegion(image, X_25, BRICKS_Y, W1, H3);
		
		title = new TextureRegion(image, X_00, TITLE_Y, W6, H6);
		
		soundOn = new TextureRegion(image, X_00, BUTTONS_Y, W1, H3);
		soundOff = new TextureRegion(image, X_05, BUTTONS_Y, W1, H3);
		help = new TextureRegion(image, X_10, BUTTONS_Y, W1, H3);
		quickPlay = new TextureRegion(image, X_15, BUTTONS_Y, W2, H3);
		
		levels = new TextureRegion(image, X_00, TEXT_Y,  W2, H3);
		credits = new TextureRegion(image, X_10, TEXT_Y,  W2, H3);
	}
	
	@Override
	public void dispose() {
		image.dispose();
	}

}
