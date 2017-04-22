package com.supergreenowl.tunnel.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {

	private static final String FILE_NAME = "tunnel.settings";
	
	private boolean isSilent;
	private int lastLevel;
	
	private boolean isDirty;
	private FileHandle file;
	
	public Settings() {
		file = Gdx.files.local(FILE_NAME);
	}
	
	/**
	 * Saves settings to local storage if they have been modified since they were last saved or loaded.
	 */
	public void save() {
		if(isDirty && Gdx.files.isLocalStorageAvailable()) {
			
			BufferedWriter writer = null;
			
			try {
				writer = new BufferedWriter(file.writer(false));
				
				// Save settings in order
				writer.write(Boolean.toString(isSilent));
				writer.newLine();
				writer.write(Integer.toString(lastLevel));
			}
			catch(Exception e) { }
			finally {
				try {
					if(writer != null) writer.close();
				}
				catch(IOException e) { }
			}
		}
	}
	
	/**
	 * Loads settings from storage.
	 */
	public void load() {
		if(Gdx.files.isLocalStorageAvailable()) {
		
			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(file.reader());
				
				// Load settings in order
				isSilent = Boolean.parseBoolean(reader.readLine());
				lastLevel = Integer.parseInt(reader.readLine());
				
			}
			catch(Exception e) {
				loadDefaults();
			}
			finally {
				try {
					if(reader != null) reader.close();
				}
				catch(IOException e) { /* Silent fail. */ }
			}
			
		}
		else {
			loadDefaults();
		}
		
		isDirty = false;
	}
	
	public void setSilence(boolean isSilent) {
		if(isSilent != this.isSilent) {
			this.isSilent = isSilent;
			isDirty = true;
		}
	}
	
	public boolean isSilent() {
		return isSilent;
	}
	
	/**
	 * Gets the ID of the last level completed.
	 * @return Level ID (number).
	 */
	public int getLastLevelCompleted() {
		return lastLevel;
	}
	
	/**
	 * Sets the ID of the last level completed.
	 * @param completedLevel ID (number) of the last level the player has completed/won.
	 */
	public void setLastLevelCompleted(int completedLevel) {
		if(this.lastLevel != completedLevel) {
			this.lastLevel = completedLevel;
			isDirty = true;
		}
	}
	
	/**
	 * Loads the default settings in case the settings file is missing.
	 */
	private void loadDefaults() {
		isSilent = false;
		lastLevel = 0;
	}
	
}
