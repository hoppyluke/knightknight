package com.supergreenowl.tunnel.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;

/**
 * Allows streaming of music tracks. 
 * @author luke
 *
 */
public class MusicPlayer implements Disposable {

	private static final String IN_GAME = "music/SederuntPrincipes.ogg";
	private static final String MENU = "music/AveRegina.ogg";
	
	private static final float FADE_DURATION = 1f;
	private static final float MUSIC_VOLUME = 0.2f;
	
	/** Music to play on the game screen. */
	public final Music inGame;
	
	/** Music to play on the menu screens. */
	public final Music menu;
	
	private Music track, nextTrack;
	private float volume;
	
	private float fadeEndVolume;
	private float fadeRate;
	private boolean isFading;
	private boolean isSilent;
	
	public MusicPlayer() {
		this(false);
	}
	
	public MusicPlayer(boolean isSilent) {
		inGame = Gdx.audio.newMusic(Gdx.files.internal(IN_GAME));
		menu = Gdx.audio.newMusic(Gdx.files.internal(MENU));
		
		inGame.setLooping(true);
		menu.setLooping(true);
		
		isFading = false;
		this.isSilent = isSilent;
	}
	
	/**
	 * Queues a track to be played next (if it is not already playing).
	 * Track will start playing on next update call and after any current fade completes.
	 * @param track Track to play next.
	 */
	public void play(Music track) {
		// If the requested track is already playing do nothing
		if(this.track != track) {
			if(this.track != null) fadeOut(); // fade out current music
			this.nextTrack = track;
		}
	}
	
	/**
	 * Stops music play back.
	 */
	public void stop() {
		if(track != null) {
			track.stop();
			track = null;
		}
		
		if(nextTrack != null) {
			nextTrack = null;
		}
	}

	/**
	 * Fades the volume for the current track.
	 * @param fadeTo Volume to fade to - must be in the interval [0, 1].
	 * @param duration Duration of fade in seconds.
	 */
	public void fade(float fadeTo, float duration) {
		this.fadeEndVolume = fadeTo;
		fadeRate = (fadeTo - volume) / duration;
		isFading = true;
	}
	
	/**
	 * Fades the volume of the current track to silent.
	 */
	public void fadeOut() {
		fade(0f, FADE_DURATION);
	}
	
	/**
	 * Turns silent mode on or off. If silence mode is turned on, any playing music is stopped and any queued music is
	 * removed from the queue.
	 * @param isSilent Whether silence is being turned on or off.
	 */
	public void setSilence(boolean isSilent) {
		this.isSilent = isSilent;
		
		if(isSilent) {
			if(track != null) {
				track.stop();
				track = null;
			}
			
			if(nextTrack != null) nextTrack = null;
		}
	}
	
	/**
	 * Determines if this player is set to silent mode.
	 * @return
	 */
	public boolean isSilent() {
		return isSilent;
	}
	
	/**
	 * Determines if this player is currently fading a track.
	 * @return
	 */
	public boolean isFading() {
		return isFading;
	}
	
	/**
	 * Updates the player state.
	 * @param delta Time in seconds since last update call.
	 */
	public void update(float delta) {
	
		// Apply volume fade
		if(track != null && isFading) {
			
			volume += fadeRate * delta;
			
			// Check for fade completion
			if(fadeRate > 0f && volume >= fadeEndVolume) {
				volume = fadeEndVolume;
				isFading = false;
			}
			else if(fadeRate < 0f && volume <= fadeEndVolume) {
				volume = fadeEndVolume;
				isFading = false;
			}
			
			track.setVolume(volume);
		}
		
		// Switch to next track unless the current track is mid-fade
		if(nextTrack != null && !isFading && !isSilent) {
			if(track != null) {
				track.stop(); // stop current track
			}
			track = nextTrack; // switch to next track
			volume = MUSIC_VOLUME; // reset volume for new track
			track.setVolume(volume);
			track.play(); // start playing new track
			nextTrack = null; // clear next track
		}
	}
	
	@Override
	public void dispose() {
		inGame.dispose();
		menu.dispose();
	}
	
	
}
