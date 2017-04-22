package com.supergreenowl.tunnel.view;

import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.ui.ColourFactory;
import com.supergreenowl.tunnel.ui.Screen;
import com.supergreenowl.tunnel.ui.grid.ContainerItem;
import com.supergreenowl.tunnel.ui.grid.GridItem;
import com.supergreenowl.tunnel.ui.grid.GridScreen;
import com.supergreenowl.tunnel.ui.grid.Switch;
import com.supergreenowl.tunnel.ui.grid.SwitchListener;
import com.supergreenowl.tunnel.ui.grid.TouchListener;

/**
 * Main menu {@link Screen} that allows the user to select game types and other options (help, credits). Also allows the user to control whether sound
 * is turned on or off.
 * @author luke
 *
 */
public class HomeScreen extends GridScreen {

	private HomeImages images;
	
	public HomeScreen(Game game) {
		super(game, 4);
		
		images = new HomeImages();
		manage(images);
		
		this.music = game.music.menu;
		setBackgroundColour(ColourFactory.MENU_BACKGROUND);
		
		// Build grid
		add(images.title);
		nextRow();
		
		ContainerItem levels = new ContainerItem(images.brickActive, images.levels);
		levels.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				campaign();
			}
		});
		add(levels);
		add(images.brick);
		
		ContainerItem quickPlay = new ContainerItem(images.brickActive, images.quickPlay);
		quickPlay.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				quickPlay();
			}
		});
		add(quickPlay);
		nextRow();
		
		add(images.brick);
		add(images.brickSmall);
		add(images.brickSmall);
		add(images.brick);
		nextRow();
		
		
		Switch sound = new Switch(images.brickActiveSmall, images.soundOn, images.soundOff, !game.settings.isSilent());
		sound.setSwitchListener(new SwitchListener() {			
			@Override
			public void onSwitch(Switch sender) {
				toggleSound();
			}
		});
		add(sound);
		add(images.brickSmall);
		ContainerItem credits = new ContainerItem(images.brickActive, images.credits);
		credits.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				credits();
			}
		});
		add(credits);
		add(images.brickSmall);
		ContainerItem help = new ContainerItem(images.brickActiveSmall, images.help);
		help.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				help();
			}
		});
		add(help);
	}

	/**
	 * Toggles game sounds on/off. Saves this in game settings so it is remembered next time the app is launced.
	 */
	private void toggleSound() {
		
		if(game.settings.isSilent()) {
			// Turn silence off
			game.settings.setSilence(false);
			game.music.setSilence(false);
			game.music.play(music);
			game.sound.isSilent = false;
		}
		else {
			game.settings.setSilence(true);
			game.music.setSilence(true);
			game.sound.isSilent = true;
		}
		
		game.settings.save();
	}
	
	/**
	 * Advances to the {@link SetupScreen} to allow the user to setup a quick play game.
	 */
	private void quickPlay() {
		SetupScreen ss = new SetupScreen(game, new GameConfig(GameConfig.Type.QuickPlay));
		game.setScreen(ss);
	}
	
	private void campaign() {
		LevelSelectScreen ls = new LevelSelectScreen(game);
		game.setScreen(ls);		
	}
	
	private void help() {
		game.setScreen(new HelpScreen(game));
	}
	
	private void credits() {
		game.setScreen(new CreditsScreen(game));
	}
	
}
