package com.supergreenowl.tunnel.campaign;

import java.io.BufferedReader;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Loads levels from disk.
 * @author luke
 *
 */
public class LevelLoader {

	private static final String FILE_NAME = "levels.txt";
	
	private static final String DELIMITER = ";";
	private static final int EXPECTED_PARTS = 6;
	
	private static final int ID = 0;
	private static final int DIFFICULTY = 1;
	private static final int PLAYER = 2;
	private static final int AI = 3;
	private static final int COINS = 4;
	private static final int AVAILABILITY = 5;
		
	private FileHandle file;
	
	/**
	 * Creates a new level loader.
	 */
	public LevelLoader() {
		file = Gdx.files.internal(FILE_NAME);
	}
	
	/**
	 * Gets all the levels.
	 * @return
	 */
	public Array<Level> load() {
		Array<Level> levels = new Array<Level>();
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(file.reader());
			String line = null;
			
			while((line = reader.readLine()) != null) {
				levels.add(loadLevel(line));
			}
			
		}
		catch(Exception e) {
			/* I'm just going to return as many levels as I have loaded. */
		}
		finally {
			try {
				if(reader != null) reader.close();
			}
			catch(IOException e) { /* Silent fail. */ }
		}
		
		return levels;
	}
	
	private Level loadLevel(String s) {
		String[] components = s.split(DELIMITER);
		if(components.length != EXPECTED_PARTS) throw new IllegalArgumentException(s);
		
		int levelId = Integer.parseInt(components[ID]);
		char difficulty = components[DIFFICULTY].charAt(0);
		int coins = Integer.parseInt(components[COINS]);
		char availability = components[AVAILABILITY].charAt(0);
		
		String player = components[PLAYER] == null || components[PLAYER].length() == 0 ? null : components[PLAYER];
		String ai = components[AI] == null || components[AI].length() == 0 ? null : components[AI];
		
		return new Level(levelId, difficulty, player, ai, coins, availability);
	}
}
