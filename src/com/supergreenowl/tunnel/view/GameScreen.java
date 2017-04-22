package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.controller.CombatListener;
import com.supergreenowl.tunnel.controller.Engine;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.model.Soldier;
import com.supergreenowl.tunnel.model.TunnelState;
import com.supergreenowl.tunnel.ui.DispatchButton;
import com.supergreenowl.tunnel.ui.HorizontalAlign;
import com.supergreenowl.tunnel.ui.Screen;
import com.supergreenowl.tunnel.ui.VerticalAlign;

public class GameScreen extends Screen implements CombatListener {

	private Engine engine;
	
	private static final float PIXELS_PER_M = 16f;
	
	private static final float TUNNEL_Y = (TARGET_HEIGHT - GameImages.TUNNEL_HEIGHT) / 2f;
	private static final float SOLDIER_Y = TUNNEL_Y + GameImages.TUNNEL_FLOOR_OFFSET;
	private static final float SOLDIER_X_OFFSET = GameImages.SOLDIER_WIDTH_F / 2f;
	private static final float HEALTH_BAR_Y = SOLDIER_Y + GameImages.SOLDIER_HEIGHT;
	private static final float HEALTH_BAR_X_OFFSET = GameImages.HEALTH_BAR_WIDTH / 2f;
	
	private static final float P1_BUTTONS_Y = (TUNNEL_Y - GameImages.BUTTON_SIZE_F) / 2f;
	private static final float P2_BUTTONS_Y = TARGET_HEIGHT - P1_BUTTONS_Y - GameImages.BUTTON_SIZE_F;
	
	private static final float VICTORY_WEST_X = (TARGET_WIDTH - GameImages.VICTORY_WEST_WIDTH) / 2f;
	private static final float VICTORY_TEXT_Y = (TARGET_HEIGHT - GameImages.VICTORY_TEXT_HEIGHT) / 2f;
	private static final float VICTORY_EAST_X = (TARGET_WIDTH - GameImages.VICTORY_EAST_WIDTH) / 2f;
	private static final float VICTORY_DRAW_X = (TARGET_WIDTH - GameImages.VICTORY_DRAW_WIDTH) / 2f;
		
	private static final float WARNING_WEST_Y = TUNNEL_Y + (GameImages.TUNNEL_HEIGHT - GameImages.WARNING_HEIGHT) / 2f;
	private static final float WARNING_WEST_X = 32f;
	private static final float WARNING_EAST_Y = WARNING_WEST_Y + GameImages.WARNING_HEIGHT;
	private static final float WARNING_EAST_X = TARGET_WIDTH - WARNING_WEST_X;
	
	private static final float COUNTDOWN_X = (TARGET_WIDTH - GameImages.WARNING_WIDTH) / 2f;
	private static final float COUNTDOWN_Y = (TARGET_HEIGHT - GameImages.WARNING_HEIGHT) / 2f;
	
//	private static final float SCORE_Y_OFFSET = 16f;
//	private static final float SCORE_X = 124f;
//	private static final float SCORE_Y = TUNNEL_Y + SCORE_Y_OFFSET;
	
	private GameImages images;
	
	private boolean isGameOver;
	private GameConfig config;
	private DispatchButton[] p1Buttons, p2Buttons;
	
	public GameScreen(Game g, GameConfig config) {
		super(g, true);
		this.config = config;
		images = new GameImages(); // Load textures
		manage(images);
		
		isGameOver = false;
		
		this.engine = new Engine(config.west, config.east);
		this.engine.listener = this;
		layoutButtons();
		
		this.music = game.music.inGame;
		
		// High scores text is right, middle aligned 
		game.text.setDefaultAlignment(HorizontalAlign.Right, VerticalAlign.Middle);
	}

	@Override
	public void onHit() {
		game.sound.hit();
	}

	@Override
	public void onDeath() {
		game.sound.death();
	}

	@Override
	protected void onTouch(float x, float y) {
		
		if(isGameOver) { // let player know this will happen on UI??
			if(config.type == GameConfig.Type.Campaign) {
				game.setScreen(new LevelSelectScreen(game));
			}
			else game.setScreen(new SetupScreen(game, config));
		}
		
		if(engine.isPaused) return; // No touch events during pause
		
		int len = 0;
		
		// See if one of P1's dispatch soldier buttons was touched
		if(p1Buttons != null) {
			len = p1Buttons.length;
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p1Buttons[i];
				if(btn.bounds.contains(x, y)) {
					engine.dispatchSoldier(btn.direction, btn.soldierType);
					return;
				}
			}
		}
		
		// See if one of P2's dispatch soldier buttons was touched (if touch not already handled)
		if(p2Buttons != null) {
			len = p2Buttons.length;
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p2Buttons[i];
				if(btn.bounds.contains(x, y)) {
					engine.dispatchSoldier(btn.direction, btn.soldierType);
					return;
				}
			}
		}
		
	}

	@Override
	protected void update(float delta) {
		// Update game engine
		engine.update(delta);
		
		// Update buttons displays
		if(p1Buttons != null) {
			int len = p1Buttons.length;
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p1Buttons[i];
				if(engine.west.soldiers[btn.soldierType] <= 0) btn.isEnabled = false;
			}
		}
		
		if(p2Buttons != null) {
			int len = p2Buttons.length;
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p2Buttons[i];
				if(engine.east.soldiers[btn.soldierType] <= 0) btn.isEnabled = false;
			}
		}
		
		// If the west player has won and this is a campaign game then update highest level completed setting
		if(isGameOver && config.levelId != GameConfig.NO_LEVEL && engine.tunnel.state == TunnelState.West) {
			if(config.levelId > game.settings.getLastLevelCompleted()) {
				game.settings.setLastLevelCompleted(config.levelId);
				game.settings.save();
			}
		}
	}

	@Override
	protected void draw(float delta, SpriteBatch batch) {
		
		// Don't need blending for the tunnel background - LibGDX docs tell me this will help performance
		batch.disableBlending();
		batch.draw(images.tunnel, 0f, TUNNEL_Y); // draw the tunnel
		batch.enableBlending();
		
		// Draw pre-game countdown
		if(engine.isPaused) {
			TextureRegion countdown = images.warning.getKeyFrame(engine.pausedTime, false);
			batch.draw(countdown, COUNTDOWN_X, COUNTDOWN_Y);
		}
		
		// Draw west soldiers
		int len = engine.tunnel.west.size;
		for(int i = 0; i < len; i++) {
			Soldier s = engine.tunnel.west.get(i);
			TextureRegion tex = images.soldier(s);
			batch.draw(tex, (s.position * PIXELS_PER_M) - SOLDIER_X_OFFSET, SOLDIER_Y);
		}
		
		// Draw east soldiers
		len = engine.tunnel.east.size;
		for(int i = 0; i < len; i++) {
			Soldier s = engine.tunnel.east.get(i);
			TextureRegion tex = images.soldier(s);
			// Flip soldier with -ve width so it faces west
			batch.draw(tex, (s.position * PIXELS_PER_M) + SOLDIER_X_OFFSET, SOLDIER_Y, -1f * tex.getRegionWidth(), GameImages.SOLDIER_HEIGHT_F);
		}
		
		// Draw west soldiers' health
		len = engine.tunnel.west.size;
		for(int i = 0; i < len; i++) {
			Soldier s = engine.tunnel.west.get(i);
			TextureRegion health = images.healthBar(s.hp, s.maxHp);
			batch.draw(health, (s.position * PIXELS_PER_M) - HEALTH_BAR_X_OFFSET, HEALTH_BAR_Y);
		}
		
		// Draw east soldiers' health
		len = engine.tunnel.east.size;
		for(int i = 0; i < len; i++) {
			Soldier s = engine.tunnel.east.get(i);
			TextureRegion health = images.healthBar(s.hp, s.maxHp);
			batch.draw(health, (s.position * PIXELS_PER_M) - HEALTH_BAR_X_OFFSET, HEALTH_BAR_Y);
		}
		
		// Work out if warning animations are needed for auto-dispatch
		float warningStart = Engine.AUTO_DISPATCH_TIME - GameImages.WARNING_DURATION;
		if(engine.gameTime >= warningStart && engine.gameTime <= Engine.AUTO_DISPATCH_TIME) {
			float warningTime = engine.gameTime - warningStart;
			TextureRegion warning = images.warning.getKeyFrame(warningTime);
			if(!engine.isDispatchedWest && engine.west.isHuman) {
				batch.draw(warning, WARNING_WEST_X, WARNING_WEST_Y);
			}
			if(!engine.isDispatchedEast && engine.east.isHuman) {
				// Rotate the warning for east so it reads the right way up for them
				batch.draw(warning, WARNING_EAST_X, WARNING_EAST_Y, 0f, 0f, GameImages.WARNING_WIDTH, GameImages.WARNING_HEIGHT, 1f, 1f, 180f);
			}
		}
		
		// Draw buttons
		if(p1Buttons != null) {
			len = p1Buttons.length;
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p1Buttons[i];
				TextureRegion tex = images.button(btn.soldierType, btn.isEnabled && !engine.isPaused);
				batch.draw(tex, btn.bounds.x, btn.bounds.y, btn.bounds.width, btn.bounds.height);
			}
		}
		
		if(p2Buttons != null) {
			len = p2Buttons.length;
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p2Buttons[i];
				TextureRegion tex = images.button(btn.soldierType, btn.isEnabled && !engine.isPaused);
				batch.draw(tex, btn.bounds.x, btn.bounds.y + GameImages.BUTTON_SIZE_F, btn.bounds.width, -1f * btn.bounds.height);
			}
		}
		
		// Draw game over text
		if(engine.tunnel.state == TunnelState.West) {
			batch.draw(images.victoryWest, VICTORY_WEST_X, VICTORY_TEXT_Y);
			isGameOver = true;
		}
		else if(engine.tunnel.state == TunnelState.East) {
			batch.draw(images.victoryEast, VICTORY_EAST_X, VICTORY_TEXT_Y);
			isGameOver = true;
		}
		else if(engine.tunnel.state == TunnelState.Draw) {
			batch.draw(images.victoryDraw, VICTORY_DRAW_X, VICTORY_TEXT_Y);
			isGameOver = true;
		}
		
//		// Draw scores
//		if(engine.west.isHuman) {
//			game.text.write(Integer.toString(engine.west.score), batch, SCORE_X, SCORE_Y);
//		}
//		
//		// Draw east score (upside-down)
//		if(engine.east.isHuman) {
//			
//			/* LibGDX does not provide a way to draw text rotated. To rotate the P2 score, we have to rotate the entire SpriteBatch.
//			 * This causes the batch to flush prior to drawing the P2 score and so this should always be the last thing drawn so that
//			 * the rest of the screen can all be batched together.
//			 */
//			rotate(batch, 180f);
//			game.text.write(Integer.toString(engine.east.score), batch, SCORE_X, SCORE_Y);
//			clearRotation(batch);
//		}
	}

	
	
	/**
	 * Calculates coordinates for all of the buttons to dispatch soldiers (for both players).
	 */
	private void layoutButtons() {
		p1Buttons = DispatchButton.getButtons(engine.west);
		p2Buttons = DispatchButton.getButtons(engine.east);
		
		if(p1Buttons != null) {
			int len = p1Buttons.length;
			float segmentWidth = TARGET_WIDTH / (float)len;
			float segmentOffset = (segmentWidth - GameImages.BUTTON_SIZE_F) / 2f;
						
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p1Buttons[i];
				btn.bounds.set(segmentWidth * i + segmentOffset, P1_BUTTONS_Y, GameImages.BUTTON_SIZE_F, GameImages.BUTTON_SIZE_F);
			}
		}
		
		if(p2Buttons != null) {
			int len = p2Buttons.length;
			float segmentWidth = TARGET_WIDTH / (float)len;
			float segmentOffset = (segmentWidth - GameImages.BUTTON_SIZE_F) / 2f;
			
			for(int i = 0; i < len; i++) {
				DispatchButton btn = p2Buttons[i];
				btn.bounds.set(TARGET_WIDTH - (segmentWidth * i + segmentOffset + GameImages.BUTTON_SIZE_F), P2_BUTTONS_Y, GameImages.BUTTON_SIZE_F, GameImages.BUTTON_SIZE_F);
			}
		}
	}
	
}
