package com.supergreenowl.tunnel.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.supergreenowl.tunnel.Game;

/**
 * Base type for all game screen classes. Implements the framework {@link com.badlogic.gdx.Screen} interface and ensures
 * the UI is scaled to fit the actual screen size but maintain the target aspect ratio.
 * @author luke
 *
 */
public abstract class Screen implements com.badlogic.gdx.Screen {

	/**
	 * Target width of a screen.
	 */
	public static final float TARGET_WIDTH = 800f;
	
	/**
	 * Target height of a screen.
	 */
	public static final float TARGET_HEIGHT = 480f;
	
	/**
	 * Main game instance.
	 */
	protected final Game game;
	
	/**
	 * Music to play on this screen. Null if none.
	 */
	protected Music music;
	
	/** Camera for this screen. */
	private OrthographicCamera camera;
	
	/** Sprite batch. */
	private SpriteBatch batch;
	
	/** Coordinates of last touch. */
	private Vector3 touch;
	
	private Color backgroundColour;
	
	private Array<Disposable> managedResources;
	
	private boolean isContinuousRendering;
	
	private int viewportX, viewportY, viewportWidth, viewportHeight;
	
	private Matrix4 rotation;
	private Matrix4 previousTransform;
	private Vector3 translation, inverseTranslation;
	
	/**
	 * Creates a new screen.
	 * @param game Game instance.
	 * @param isContinuousRendering Determines whether continuous or non-continuous rendering should be used when this screen is shown.
	 * Static menu screens etc should pass false to save CPU time.
	 * 
	 */
	public Screen(Game game, boolean isContinuousRendering) {
		this.game = game;
		this.isContinuousRendering = isContinuousRendering;
		
		backgroundColour = Color.BLACK;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, TARGET_WIDTH, TARGET_HEIGHT);
		batch = new SpriteBatch();
		
		touch = new Vector3();
		managedResources = new Array<Disposable>();
		
		rotation = new Matrix4();
		previousTransform = new Matrix4();
		translation = new Vector3(TARGET_WIDTH / 2f, TARGET_HEIGHT / 2f, 0f);
		inverseTranslation = new Vector3(translation);
		inverseTranslation.mul(-1f);
		
		music = null;
	}
	
	@Override
	public void render(float delta) {
		
		// Update camera and set graphics viewport
		camera.update();
		Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
		
		// Touch input handling
		if(Gdx.input.justTouched()) {
			// Get touch coordinates and translate from screen space to viewport space
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0f);
			camera.unproject(touch, viewportX, viewportY, viewportWidth, viewportHeight);
			
			onTouch(touch.x, touch.y);
		}
		
		update(delta); // Update model
		game.music.update(delta); // Update music player
		
		// Clear screen
		Gdx.gl.glClearColor(backgroundColour.r, backgroundColour.g, backgroundColour.b, backgroundColour.a);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		draw(delta, batch);
		batch.end();
		
		// Continuous rendering is required to get the music to fade
		// - even if the screen does not need it otherwise
		if(!isContinuousRendering && game.music.isFading()) {
			Gdx.graphics.requestRendering();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		// Calculate the view port size to scale the target resolution to fit the screen but still maintain the apsect ratio
		Vector2 scaledDimensions = Scaling.fit.apply(TARGET_WIDTH, TARGET_HEIGHT, width, height);
		
		Vector2 padding = new Vector2(width, height);
		padding.sub(scaledDimensions);
		padding.mul(0.5f);
		
		viewportX = (int)padding.x;
		viewportY = (int)padding.y;
		viewportWidth = (int)scaledDimensions.x;
		viewportHeight = (int)scaledDimensions.y;
	}

	@Override
	public void show() {
		// Disable continuous rendering if requested by the screen
		if(!isContinuousRendering && Gdx.graphics.isContinuousRendering()) {
			Gdx.graphics.setContinuousRendering(false);
			Gdx.graphics.requestRendering();
		}
		else if(isContinuousRendering && !Gdx.graphics.isContinuousRendering()) {
			Gdx.graphics.setContinuousRendering(true);
		}
		
		// Play music for this screen (if any)
		game.music.play(music);
	}

	@Override
	public void hide() {
		// LibGDX does not automatically dispose screens when the game is disposed
		// The screen will be hidden instead so if the screen is being hidden because
		// the game is being disposed then it will dispose of itself
		if(game.isDisposing()) {
			dispose();
		}
	}

	@Override
	public void pause() {
		// TODO perchance save game state somehow/somewhere?
	}

	@Override
	public void resume() {
		// TODO check if there is some saved state to collect?
	}

	@Override
	public void dispose() {		
		// Dispose of all my managed resources
		for(Disposable resource : managedResources) {
			resource.dispose();
		}
	}

	/**
	 * Gets the screen to manage a disposable resource so that the resource will be disposed when the screen is.
	 * @param resouce
	 */
	public void manage(Disposable resouce) {
		managedResources.add(resouce);
	}
	
	/**
	 * Applies rotation to a SpriteBatch which will mean any subsequent drawing calls are rotated.
	 * Call {@link #clearRotation} to remove the rotation effect.
	 * Note that this flushes the batch.
	 * @param batch Batch to rotate.
	 * @param angle Angle to rotate by (in degrees).
	 */
	protected void rotate(SpriteBatch batch, float angle) {				
		previousTransform.set(batch.getTransformMatrix());
		
		rotation.idt().translate(translation).rotate(0f, 0f, 1f, angle).translate(inverseTranslation);
		
		batch.setTransformMatrix(rotation);
	}
	
	/**
	 * Clears rotation previous applied to a SpriteBatch. Note that this flushes the batch.
	 * @param batch Batch to clear rotation on.
	 * @see #rotate(SpriteBatch, float)
	 */
	protected void clearRotation(SpriteBatch batch) {
		batch.setTransformMatrix(previousTransform);
	}
	
	/**
	 * Sets the background colour for this screen.
	 * @param c New background colour.
	 */
	protected void setBackgroundColour(Color c) {
		this.backgroundColour = c;
	}
	
	/**
	 * Handles a touch input event.
	 * @param x Touch location in viewport (target screen size) space.
	 * @param y Touch location in viewport (target screen size) space.
	 */
	protected abstract void onTouch(float x, float y);
	
	/**
	 * Updates the model behind the screen.
	 * @param delta Elapsed time in seconds since last update.
	 */
	protected abstract void update(float delta);

	/**
	 * Draws the current game state to the screen.
	 * @param delta Time in seconds since last draw call.
	 * @param batch SpriteBatch instance to draw to.
	 */
	protected abstract void draw(float delta, SpriteBatch batch);
}
