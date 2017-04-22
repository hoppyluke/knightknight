package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

/**
 * Loads and provides images for the position device screen.
 * @author luke
 *
 */
public class PositionImages implements Disposable {

	private static final String FILE_NAME = "position.png";
	
	private static final int ATLAS_HEIGHT = 384;
	
	private static final int H = 96;
	
	private static final int W1 = 128;
	private static final int W2 = 256;
	private static final int W4 = 512;
	
	private static final int X_00 = 0;
	private static final int X_10 = W2;
	private static final int X_20 = W4;
	private static final int X_25 = W4 + W1;
		
	private static final int Y_0 = ATLAS_HEIGHT - H;
	private static final int Y_1 = ATLAS_HEIGHT - (H * 2);
	private static final int Y_2 = ATLAS_HEIGHT - (H * 3);
	private static final int Y_3 = ATLAS_HEIGHT - (H * 4);
	
	private final Texture images;
	public final TextureRegion arrowUp, arrowDown, back;
	public final TextureRegion readyDownNo, readyDownYes;
	public final TextureRegion readyUpNo, readyUpYes;
	public final TextureRegion p1, p2;
	public final TextureRegion brick, brickSmall, brickActive, brickActiveSmall;
	public final TextureRegion position;
	
	public PositionImages() {
		images = new Texture(Gdx.files.internal(FILE_NAME));
		
		arrowDown = new TextureRegion(images, X_25, Y_1, W1, H);
		arrowUp = new TextureRegion(images, X_25, Y_1, W1, H);
		arrowUp.flip(false, true);
		back = new TextureRegion(images, X_20, Y_1, W1, H);
		
		readyDownNo = new TextureRegion(images, X_00, Y_2, W2, H);
		readyDownYes = new TextureRegion(images, X_10, Y_2, W2, H);
		p1 = new TextureRegion(images, X_20, Y_2, W2, H);
		
		readyUpNo = new TextureRegion(images, X_00, Y_3, W2, H);
		readyUpYes = new TextureRegion(images, X_10, Y_3, W2, H);
		p2 = new TextureRegion(images, X_20, Y_3, W2, H);
		
		brick = new TextureRegion(images, X_00, Y_0, W2, H);
		brickActive = new TextureRegion(images, X_10, Y_0, W2, H);
		brickSmall = new TextureRegion(images, X_20, Y_0, W1, H);
		brickActiveSmall = new TextureRegion(images, X_25, Y_0, W1, H);
		
		position = new TextureRegion(images, X_00, Y_1, W4, H);
	}
	
	@Override
	public void dispose() {
		images.dispose();
	}

}
