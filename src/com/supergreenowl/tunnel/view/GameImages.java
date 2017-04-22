package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.supergreenowl.tunnel.model.CombatEngine;
import com.supergreenowl.tunnel.model.Soldier;
import com.supergreenowl.tunnel.model.SoldierState;
import com.supergreenowl.tunnel.model.SoldierType;

/**
 * Provides in game images.
 * @author luke
 *
 */
public class GameImages implements Disposable {

	/*
	 * Since LibGDX and Inkscape address the Y axis in different directions
	 * all Y co-ordinates need to be inverted as:
	 * 
	 * libgdx y = atlas height - image height - inkscape y
	 */

	private static final String TUNNEL = "tunnel.png";
	private static final String ATLAS_SPRITES = "sprites.png";
	private static final String ATLAS_TEXT = "endgametext.png";
	private static final String ATLAS_UI = "uielements.png";
	
	private static final int TEXT_ATLAS_HEIGHT = 192;
	private static final int SPRITE_ATLAS_HEIGHT = 672;
	
	private static final int UI_ATLAS_HEIGHT = 224;
	
	public static final int TUNNEL_WIDTH = 800;
	public static final int TUNNEL_HEIGHT = 192;
	
	/*
	 * Number of pixels above the base of the image where the floor is in the tunnel.
	 */
	public static final float TUNNEL_FLOOR_OFFSET = 32f;
	
	public static final int SOLDIER_WIDTH = 96;
	public static final int SOLDIER_HEIGHT = 96;
	public static final float SOLDIER_WIDTH_F = 96f;
	public static final float SOLDIER_HEIGHT_F = 96f;
	
	// Animation durations (for whole animation) (s)
	private static final float WALK_DURATION = 0.8f;
	private static final float HIT_DURATION = CombatEngine.HIT_DURATION;
	private static final float DEATH_DURATION = CombatEngine.DYING_DURATION;
	
	/** Duration of the auto-dispatch warning animation (s). */
	public static final float WARNING_DURATION = 3f;
	
	private static final int ANIMATION_FRAMES = 4;
	private static final int WARNING_FRAMES = 3;
	
	private static final int SOLDIER_REST_X = 0;
	private static final int SOLDIER_WALK_X = SOLDIER_WIDTH;
	private static final int SOLDIER_HIT_X = SOLDIER_WIDTH * (ANIMATION_FRAMES + 1);
	private static final int SOLDIER_DEATH_X = SOLDIER_WIDTH * (ANIMATION_FRAMES * 2 + 2);
	
	private static final int[] SOLDIER_Y = new int[] {
		SPRITE_ATLAS_HEIGHT - SOLDIER_HEIGHT, // w
		SPRITE_ATLAS_HEIGHT - (SOLDIER_HEIGHT * 2), // r
		SPRITE_ATLAS_HEIGHT - (SOLDIER_HEIGHT * 3), // g
		SPRITE_ATLAS_HEIGHT - (SOLDIER_HEIGHT * 4), // b
		SPRITE_ATLAS_HEIGHT - (SOLDIER_HEIGHT * 5), // c
		SPRITE_ATLAS_HEIGHT - (SOLDIER_HEIGHT * 6), // m
		SPRITE_ATLAS_HEIGHT - (SOLDIER_HEIGHT * 7) // y
	};
	
	public static final int BUTTON_HEIGHT = 64;
	public static final int BUTTON_WIDTH = 64;
	public static final float BUTTON_SIZE_F = 64f;
	
	private static final int BUTTON_Y = UI_ATLAS_HEIGHT - BUTTON_HEIGHT;
	private static final int BUTTON_DISABLED_Y = UI_ATLAS_HEIGHT - (BUTTON_HEIGHT * 2);
	private static final int BUTTON_W_X = 0;
	private static final int BUTTON_R_X = BUTTON_WIDTH;
	private static final int BUTTON_G_X = BUTTON_WIDTH * 2;
	private static final int BUTTON_B_X = BUTTON_WIDTH * 3;
	private static final int BUTTON_C_X = BUTTON_WIDTH * 4;
	private static final int BUTTON_M_X = BUTTON_WIDTH * 5;
	private static final int BUTTON_Y_X = BUTTON_WIDTH * 6;
	
	public static final int HEALTH_BAR_WIDTH = 96;
	public static final int HEALTH_BAR_HEIGHT = 32;
	private static final int HEALTH_BAR_0_X = 192;
	private static final int HEALTH_BAR_1_X = HEALTH_BAR_0_X + HEALTH_BAR_WIDTH;
	private static final int HEALTH_BAR_2_X = HEALTH_BAR_0_X;
	private static final int HEALTH_BAR_3_X = HEALTH_BAR_0_X + HEALTH_BAR_WIDTH;
	private static final int HEALTH_BAR_0_Y = UI_ATLAS_HEIGHT - (2 * BUTTON_HEIGHT) - HEALTH_BAR_HEIGHT;
	private static final int HEALTH_BAR_1_Y = UI_ATLAS_HEIGHT - (2 * BUTTON_HEIGHT) - HEALTH_BAR_HEIGHT;
	private static final int HEALTH_BAR_2_Y = UI_ATLAS_HEIGHT - (2 * BUTTON_HEIGHT) - (2 * HEALTH_BAR_HEIGHT);
	private static final int HEALTH_BAR_3_Y = UI_ATLAS_HEIGHT - (2 * BUTTON_HEIGHT) - (2 * HEALTH_BAR_HEIGHT);
	private static final float ONE_THIRD = 1f / 3f;
	private static final float TWO_THIRDS = 2f / 3f;
	
	public static final int VICTORY_TEXT_HEIGHT = 64;
	public static final int VICTORY_WEST_WIDTH = 560;
	private static final int VICTORY_WEST_X = 0;
	private static final int VICTORY_WEST_Y = TEXT_ATLAS_HEIGHT - VICTORY_TEXT_HEIGHT;
	
	public static final int VICTORY_EAST_WIDTH = 742;
	private static final int VICTORY_EAST_X = 0;
	private static final int VICTORY_EAST_Y = TEXT_ATLAS_HEIGHT - (VICTORY_TEXT_HEIGHT * 2);
	
	public static final int VICTORY_DRAW_WIDTH = 510;
	private static final int VICTORY_DRAW_X = 0;
	private static final int VICTORY_DRAW_Y = TEXT_ATLAS_HEIGHT - (VICTORY_TEXT_HEIGHT * 3);
	
	public static final int WARNING_WIDTH = 64;
	public static final int WARNING_HEIGHT = 96;
	private static final int WARNING_X = 0;
	private static final int WARNING_Y = UI_ATLAS_HEIGHT - (2 * BUTTON_HEIGHT) - WARNING_HEIGHT;
	
	private final Texture tunnelTexture, sprites, textAtlas, uiElements;
	
	/**
	 * Texture region representing the tunnel (game background).
	 */
	public final TextureRegion tunnel;
	public final TextureRegion victoryWest, victoryEast, victoryDraw;
	public final Animation warning;
	
	private final TextureRegion[] soldier;
	private final Animation[] walk, hit, death;
	
	private final TextureRegion buttonWhite, buttonRed, buttonGreen, buttonBlue, buttonCyan, buttonMagenta, buttonYellow;
	private final TextureRegion buttonDisabledWhite, buttonDisabledRed, buttonDisabledGreen, buttonDisabledBlue, buttonDisabledCyan, buttonDisabledMagenta, buttonDisabledYellow;
	
	private final TextureRegion healthBar0, healthBar1, healthBar2, healthBar3;

	/**
	 * Creates a new image provider and loads textures.
	 */
	public GameImages() {
		
		// Load textures from assets directory
		tunnelTexture = new Texture(Gdx.files.internal(TUNNEL));
		textAtlas = new Texture(Gdx.files.internal(ATLAS_TEXT));
		sprites = new Texture(Gdx.files.internal(ATLAS_SPRITES));
		uiElements = new Texture(Gdx.files.internal(ATLAS_UI));
		
		// Load background tunnel image
		tunnel = new TextureRegion(tunnelTexture, TUNNEL_WIDTH, TUNNEL_HEIGHT);

		// Load sprites
		soldier = new TextureRegion[SoldierType.COUNT];
		hit = new Animation[SoldierType.COUNT];
		walk = new Animation[SoldierType.COUNT];
		death = new Animation[SoldierType.COUNT];
		boolean[] doubleFrames = new boolean[] { false, false, true, false };
		for(int i = 0; i < SoldierType.COUNT; i++) {
			soldier[i] = new TextureRegion(sprites, SOLDIER_REST_X, SOLDIER_Y[i], SOLDIER_WIDTH, SOLDIER_HEIGHT);
			walk[i] = loadAnimation(sprites, WALK_DURATION, ANIMATION_FRAMES, SOLDIER_WIDTH, SOLDIER_HEIGHT, SOLDIER_WALK_X, SOLDIER_Y[i]);
			hit[i] = loadAnimation(sprites, HIT_DURATION, ANIMATION_FRAMES, SOLDIER_WIDTH, SOLDIER_HEIGHT, SOLDIER_HIT_X, SOLDIER_Y[i], ANIMATION_FRAMES, doubleFrames);
			death[i] = loadAnimation(sprites, DEATH_DURATION, 3, SOLDIER_WIDTH, SOLDIER_HEIGHT, SOLDIER_DEATH_X, SOLDIER_Y[i]);
		}
		
		// Load buttons - enabled buttons
		buttonWhite = new TextureRegion(uiElements, BUTTON_W_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonRed = new TextureRegion(uiElements, BUTTON_R_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonGreen = new TextureRegion(uiElements, BUTTON_G_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonBlue = new TextureRegion(uiElements, BUTTON_B_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonCyan = new TextureRegion(uiElements, BUTTON_C_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonMagenta = new TextureRegion(uiElements, BUTTON_M_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonYellow = new TextureRegion(uiElements, BUTTON_Y_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		
		// Load buttons - disabled buttons
		buttonDisabledWhite = new TextureRegion(uiElements, BUTTON_W_X, BUTTON_DISABLED_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonDisabledRed = new TextureRegion(uiElements, BUTTON_R_X, BUTTON_DISABLED_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonDisabledGreen = new TextureRegion(uiElements, BUTTON_G_X, BUTTON_DISABLED_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonDisabledBlue = new TextureRegion(uiElements, BUTTON_B_X, BUTTON_DISABLED_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonDisabledCyan = new TextureRegion(uiElements, BUTTON_C_X, BUTTON_DISABLED_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonDisabledMagenta = new TextureRegion(uiElements, BUTTON_M_X, BUTTON_DISABLED_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttonDisabledYellow = new TextureRegion(uiElements, BUTTON_Y_X, BUTTON_DISABLED_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		
		// Load health bars
		healthBar0 = new TextureRegion(uiElements, HEALTH_BAR_0_X, HEALTH_BAR_0_Y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		healthBar1 = new TextureRegion(uiElements, HEALTH_BAR_1_X, HEALTH_BAR_1_Y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		healthBar2 = new TextureRegion(uiElements, HEALTH_BAR_2_X, HEALTH_BAR_2_Y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		healthBar3 = new TextureRegion(uiElements, HEALTH_BAR_3_X, HEALTH_BAR_3_Y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		
		// Load end of game text
		victoryWest = new TextureRegion(textAtlas, VICTORY_WEST_X, VICTORY_WEST_Y, VICTORY_WEST_WIDTH, VICTORY_TEXT_HEIGHT);
		victoryEast = new TextureRegion(textAtlas, VICTORY_EAST_X, VICTORY_EAST_Y, VICTORY_EAST_WIDTH, VICTORY_TEXT_HEIGHT);
		victoryDraw = new TextureRegion(textAtlas, VICTORY_DRAW_X, VICTORY_DRAW_Y, VICTORY_DRAW_WIDTH, VICTORY_TEXT_HEIGHT);
		
		// Load auto-dispatch warning animation
		warning = loadAnimation(uiElements, WARNING_DURATION, WARNING_FRAMES, WARNING_WIDTH, WARNING_HEIGHT, WARNING_X, WARNING_Y);
	}
	
	/**
	 * Gets the texture region representing a soldier as determined by the current state of the soldier.
	 * @param s Soldier to draw.
	 * @return Texture region for soldier.
	 */
	public TextureRegion soldier(Soldier s) {
		
		SoldierState state = s.state;
		int type = s.type;
		
		if(state == SoldierState.Walking) return walk[type].getKeyFrame(s.stateTime, true);
		else if(state == SoldierState.Hitting) return hit[type].getKeyFrame(s.stateTime, true);
		else if(state == SoldierState.Dying) return death[type].getKeyFrame(s.stateTime, true);
		else /*if(state == SoldierState.Stopped)*/ return soldier[type];
	}
	
	/**
	 * Gets an image representing a health bar for a soldier.
	 * @param hp Current HP of the soldier.
	 * @param maxHp Max HP of the soldier.
	 * @return
	 */
	public TextureRegion healthBar(int hp, int maxHp) {
		
		// Convert hp into a fraction
		float hpRemaining = hp / (float)maxHp;
		
		// Have 3 health bar images so use 1/3 as comparison
		if(hpRemaining < ONE_THIRD) return healthBar0;
		else if(hpRemaining >= ONE_THIRD && hpRemaining < TWO_THIRDS) return healthBar1;
		else if(hpRemaining >= TWO_THIRDS && hpRemaining < 1f) return healthBar2;
		else return healthBar3;
		
	}
	
	/**
	 * Gets a texture region representing a dispatch soldier button.
	 * @param type Type of soldier to be dispatched.
	 * @param isEnabled Flag indicating if the button is enabled or not.
	 * @return Button image.
	 */
	public TextureRegion button(int type, boolean isEnabled) {
		
		if(!isEnabled) {
			switch(type) {
				case SoldierType.WHITE: return buttonDisabledWhite;
				case SoldierType.RED: return buttonDisabledRed;
				case SoldierType.GREEN: return buttonDisabledGreen;
				case SoldierType.BLUE: return buttonDisabledBlue;
				case SoldierType.CYAN: return buttonDisabledCyan;
				case SoldierType.MAGENTA: return buttonDisabledMagenta;
				case SoldierType.YELLOW: return buttonDisabledYellow;
			}
		}
		else {
			switch(type) {
			case SoldierType.WHITE: return buttonWhite;
			case SoldierType.RED: return buttonRed;
			case SoldierType.GREEN: return buttonGreen;
			case SoldierType.BLUE: return buttonBlue;
			case SoldierType.CYAN: return buttonCyan;
			case SoldierType.MAGENTA: return buttonMagenta;
			case SoldierType.YELLOW: return buttonYellow;
		}
		}
		
		return null;
	}
	
	@Override
	public void dispose() {
		sprites.dispose();
		textAtlas.dispose();
		tunnelTexture.dispose();
	}
	
	/**
	 * Loads an animation from a texture. The key frames are expected to be laid out in one row, left to right.
	 * @param texture Texture containing frames.
	 * @param duration Total duration of the animation.
	 * @param frames Number of frames in the animation.
	 * @param frameWidth Width of each key frame.
	 * @param frameHeight Height of each key frame.
	 * @param x x-coordinate of location within the texture for the first frame.
	 * @param y y-coordinate of each frame within the texture.
	 * @return Animation instance for the frames.
	 */
	private Animation loadAnimation(Texture texture, float duration, int frames, int frameWidth, int frameHeight, int x, int y) {
		return loadAnimation(texture, duration, frames, frameWidth, frameHeight, x, y, frames, null);
	}
	
	/**
	 * Loads an animation from a texture. The key frames are expected to be laid out left to right, bottom to top.
	 * @param texture Sprite sheet.
	 * @param duration Total duration of the animation.
	 * @param frames Number of frames in the animation.
	 * @param frameWidth Width of each key frame.
	 * @param frameHeight Height of each key frame.
	 * @param x x-coordinate of location within the texture for the first frame.
	 * @param y y-coordinate of each frame within the texture.
	 * @param framesPerRow Number of frames in each row in the sprite sheet.
	 * @param isDoubleWidth Indicates which frames are double width sized.
	 * @return Animation instance for the frames.
	 */
	private Animation loadAnimation(Texture texture, float duration, int frames, int frameWidth, int frameHeight, int x, int y, int framesPerRow, boolean[] isDoubleWidth) {
		TextureRegion[] keyframes = new TextureRegion[frames];
		
		int frameX = x;
		
		for(int i = 0; i < frames; i++) {
			int col = i % framesPerRow;
			int row = i / framesPerRow;

			if(col == 0) frameX = x;
			else frameX += frameWidth;
			
			
			int frameY = y + (row * frameHeight);
			
			int width = frameWidth;
			boolean doubleWidth = isDoubleWidth == null ? false : isDoubleWidth[i];
			if(doubleWidth) width *= 2;
			
			keyframes[i] = new TextureRegion(texture, frameX, frameY, width, frameHeight);
			
			if(doubleWidth) frameX += frameWidth;
		}
		
		float frameDuration = duration / (float)frames;
		
		return new Animation(frameDuration, keyframes);
	}
	
}
