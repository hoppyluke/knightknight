package com.supergreenowl.tunnel;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.supergreenowl.tunnel.audio.MusicPlayer;
import com.supergreenowl.tunnel.audio.SoundEffects;
import com.supergreenowl.tunnel.controller.Settings;
import com.supergreenowl.tunnel.ui.TextWriter;
import com.supergreenowl.tunnel.view.HomeScreen;

public class Game extends com.badlogic.gdx.Game {

	private boolean isDisposing;
	
	public MusicPlayer music;
	public TextWriter text;
	public SoundEffects sound;
	public Settings settings;
	
	@Override
	public void create() {
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG); // TEMP
		
		isDisposing = false;
		
		// Load common resources
		text = new TextWriter();
		settings = new Settings();
		settings.load();
		
		music = new MusicPlayer(settings.isSilent());
		sound = new SoundEffects(settings.isSilent());
		
		// Show home screen
		HomeScreen home = new HomeScreen(this);
		setScreen(home);
		
	}

	@Override
	public void dispose() {		
		// Set is disposing flag to allow screens to automatically dispose themselves (which the framework does not do)
		isDisposing = true;
		
		// Dispose of common resources
		music.dispose();
		sound.dispose();
		text.dispose();
		
		// Call framework dispose event
		super.dispose();
	}
	
	/**
	 * Determines if the game is currently being disposed by the framework.
	 * @return
	 */
	public boolean isDisposing() {
		return isDisposing;
	}

	/**
	 * Overridden set screen to dispose of the previous screen after screen change.
	 */
	@Override
	public void setScreen(Screen screen) {
		Screen oldScreen = getScreen();
		super.setScreen(screen);
		if(oldScreen != null) oldScreen.dispose();
	}
	
	
	
	
}
