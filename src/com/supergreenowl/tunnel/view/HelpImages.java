package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class HelpImages implements Disposable {

	private static final String FILE_NAME = "help.png";
	
	private static final int H = 96;
	private static final int H4 = 384;
	
	private static final int W = 128;
	private static final int W5 = 640;
	private static final int W6 = 768;
	
	private final Texture image;
	
	public final TextureRegion back, title, help;
	
	public HelpImages() {
		image = new Texture(Gdx.files.internal(FILE_NAME));
		
		back = new TextureRegion(image, 0, 0, W, H);
		title = new TextureRegion(image, W, 0, W5, H);
		help = new TextureRegion(image, 0, H, W6, H4);
	}
	
	@Override
	public void dispose() {
		image.dispose();
	}

}
