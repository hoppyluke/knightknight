package com.supergreenowl.tunnel.audio;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Loads and provides sound effects for the game.
 * @author luke
 *
 */
public class SoundEffects implements Disposable {

	private static final String DEATH_1 = "effects/death1.ogg";
	private static final String DEATH_2 = "effects/death2.ogg";
	private static final String DEATH_3 = "effects/death3.ogg";
	
	private static final String HIT_1 = "effects/hit1.ogg";
	private static final String HIT_2 = "effects/hit2.ogg";
	private static final String HIT_3 = "effects/hit3.ogg";
	
	private static final float SOUND_VOLUME = 1f;
	
	private static Random generator = new Random();
	
	/**
	 * Whether this sound effects player is silent or not.
	 */
	public boolean isSilent;
	
	private final Sound[] death;
	private final Sound[] hit;
	
	public SoundEffects() {
		this(false);
	}
	
	public SoundEffects(boolean isSilent) {
		
		this.isSilent = isSilent;
		
		death = new Sound[] {
				Gdx.audio.newSound(Gdx.files.internal(DEATH_1)),
				Gdx.audio.newSound(Gdx.files.internal(DEATH_2)),
				Gdx.audio.newSound(Gdx.files.internal(DEATH_3))
		};
		
		hit = new Sound[] {
				Gdx.audio.newSound(Gdx.files.internal(HIT_1)),
				Gdx.audio.newSound(Gdx.files.internal(HIT_2)),
				Gdx.audio.newSound(Gdx.files.internal(HIT_3))
		};
	}
	
	/**
	 * Plays a hit sound effect selected at random.
	 */
	public void hit() {
		if(!isSilent) {
			hit[generator.nextInt(hit.length)].play(SOUND_VOLUME);
		}
	}
	
	/**
	 * Plays a death sound effect selected at random.
	 */
	public void death() {
		if(!isSilent) {
			death[generator.nextInt(death.length)].play(SOUND_VOLUME);
		}
	}
	
	@Override
	public void dispose() {
		int len = death.length;
		for(int i = 0; i < len; i++) {
			if(death[i] != null) death[i].dispose();
		}
		
		len = hit.length;
		for(int i = 0; i < len; i++) {
			if(hit[i] != null) hit[i].dispose();
		}
	}

}
