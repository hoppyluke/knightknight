package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.ai.AI;
import com.supergreenowl.tunnel.ai.AIFactory;
import com.supergreenowl.tunnel.controller.Difficulty;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.ui.ColourFactory;
import com.supergreenowl.tunnel.ui.Screen;
import com.supergreenowl.tunnel.ui.grid.ContainerItem;
import com.supergreenowl.tunnel.ui.grid.GridItem;
import com.supergreenowl.tunnel.ui.grid.GridScreen;
import com.supergreenowl.tunnel.ui.grid.ToggleItem;
import com.supergreenowl.tunnel.ui.grid.TouchListener;
/**
 * {@link Screen} that allows the user to setup a quick play game.
 * @author luke
 *
 */
public class SetupScreen extends GridScreen {

	private GameConfig config;
	private SetupImages images;
	
	private SoldiersToggle soldiers;
	private DifficultyToggle difficulty;
	private SizeToggle armySize;
	private PlayersToggle players;
	
	public SetupScreen(Game game, GameConfig config) {
		super(game, 5);
		
		this.config = config;
		images = new SetupImages();
		manage(images);
		
		this.music = game.music.menu;
		setBackgroundColour(ColourFactory.MENU_BACKGROUND);
		
		// Build the grid
		ContainerItem back = new ContainerItem(images.brickActiveSmall, images.back);
		back.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				back();
				
			}
		});
		add(back);
		
		add(images.brickSmall);
		add(new ContainerItem(images.brick, images.title));
		add(images.brickSmall);
		
		ContainerItem forward = new ContainerItem(images.brickActiveSmall, images.forward);
		forward.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				forward();
			}
		});
		
		add(forward);
		nextRow();
		
		add(new ContainerItem(images.brick, images.players));
		add(images.brick);
		players = new PlayersToggle(images.brickActive, config.type);
		add(players);
		nextRow();
		
		add(new ContainerItem(images.brickLarge, images.difficulty));
		add(images.brickSmall);
		difficulty = new DifficultyToggle(images.brickActive, config.difficulty);
		difficulty.setDisabledStateDisplay(images.na);
		add(difficulty);
		nextRow();
		
		add(new ContainerItem(images.brickHuge, images.knights));
		soldiers = new SoldiersToggle(images.brickActive, config.available);
		add(soldiers);
		nextRow();
		
		add(new ContainerItem(images.brick, images.wealth));
		add(images.brick);
		armySize = new SizeToggle(images.brickActive, config.armySize);
		add(armySize);
	}

	@Override
	protected void update(float delta) {
		super.update(delta);
		
		if(players.state.equals(GameConfig.Type.Multiplayer)) {
			difficulty.isDisabled = true;
		}
		else {
			difficulty.isDisabled = false;
		}
	}

	/**
	 * Navigates back to the {@link HomeScreen}.
	 */
	private void back() {
		game.setScreen(new HomeScreen(game));
	}
	
	/**
	 * Navigates forward to soldier selection.
	 */
	private void forward() {
		
		boolean isWestValid = config.west != null;
		boolean isEastValid = config.east != null;
		
		// Look for config changes and update the configuration
		
		if(!config.armySize.equals(armySize.state)) {
			int coins = armySize.state.toCoins();
			
			// if players have spent more coins than are now availble, they must re-select troops
			if(config.west != null && config.west.totalCost() > coins) isWestValid = false;
			if(config.east != null && config.east.totalCost() > coins) isEastValid = false;
			
			config.coins = coins;
			config.armySize = armySize.state;
		}
		
		if(!config.available.equals(soldiers.state)) {
			
			// If less soldiers are now available then players must re-select soldiers
			if(soldiers.state.compareTo(config.available) < 0) {
				isWestValid = isEastValid = false;
			}
			
			config.available = soldiers.state;
		}
		
		if(!config.difficulty.equals(difficulty.state)) {
			
			// If difficulty changes in a single player game, AI player needs to be re-created
			if(config.east != null && !config.type.equals(GameConfig.Type.Multiplayer)) {
				isEastValid = false;
			}
			config.difficulty = difficulty.state;
		}
		
		if(!config.type.equals(players.state)) {
			// if game type has changed to/from multiplayer then P2 is no longer valid
			isEastValid = false;
			config.type = players.state;
		}
		
		// Re-create players if they are not valid
		if(!isWestValid) {
			Player p1 = new Player(true, Direction.West);
			config.west = p1;
		}
		
		if(!isEastValid) {
			if(config.type == GameConfig.Type.Multiplayer) {
				Player p2 = new Player(true, Direction.East);
				config.east = p2;
			}
			else {
				AI ai = AIFactory.create(config.difficulty, Direction.East);
				config.east = ai;
			}
		}
		
		if(!config.east.isHuman) {
			// Get the AI to reselect each time - this allows AI that uses
			// multiple different selection strategies to change strategy
			AI ai = (AI)config.east;
			ai.selectSoldiers(config.coins, config.available);
		}
		
		game.setScreen(new SoldierSelectScreen(game, config, config.west));
	}
	
	/**
	 * Toggle button for game difficulty.
	 * @author luke
	 *
	 */
	private class DifficultyToggle extends ToggleItem<Difficulty> {

		public DifficultyToggle(TextureRegion background, Difficulty state) {
			super(background, state);
		}

		@Override
		public Difficulty nextState() {
			if(state.equals(Difficulty.Easy)) return Difficulty.Medium;
			else if(state.equals(Difficulty.Medium)) return Difficulty.Hard;
			else return Difficulty.Easy;
		}

		@Override
		public TextureRegion getRegion() {
			if(state.equals(Difficulty.Easy)) return images.easy;
			else if(state.equals(Difficulty.Medium)) return images.medium;
			else return images.hard;
		}
		
	}
	
	/**
	 * Toggle button for available soldiers.
	 * @author luke
	 *
	 */
	private class SoldiersToggle extends ToggleItem<GameConfig.SoldierAvailability> {

		public SoldiersToggle(TextureRegion background, GameConfig.SoldierAvailability state) {
			super(background, state);
		}

		@Override
		public GameConfig.SoldierAvailability nextState() {
			if(state.equals(GameConfig.SoldierAvailability.RGB)) return GameConfig.SoldierAvailability.WRGB;
			else if(state.equals(GameConfig.SoldierAvailability.WRGB)) return GameConfig.SoldierAvailability.WRGBCMY;
			else return GameConfig.SoldierAvailability.RGB;
		}

		@Override
		public TextureRegion getRegion() {
			if(state.equals(GameConfig.SoldierAvailability.RGB)) return images.shieldsRGB;
			else if(state.equals(GameConfig.SoldierAvailability.WRGB)) return images.shieldsW;
			else return images.shieldsCMY;
		}
	}
	
	/**
	 * Toggle button for army size.
	 * @author luke
	 *
	 */
	private class SizeToggle extends ToggleItem<GameConfig.ArmySize> {

		public SizeToggle(TextureRegion background, GameConfig.ArmySize state) {
			super(background, state);
		}

		@Override
		public GameConfig.ArmySize nextState() {
			if(state.equals(GameConfig.ArmySize.Small)) return GameConfig.ArmySize.Medium;
			else if(state.equals(GameConfig.ArmySize.Medium)) return GameConfig.ArmySize.Large;
			else return GameConfig.ArmySize.Small;
		}

		@Override
		public TextureRegion getRegion() {
			if(state.equals(GameConfig.ArmySize.Small)) return images.low;
			else if(state.equals(GameConfig.ArmySize.Medium)) return images.medium;
			else return images.high;
		}
		
	}
	
	private class PlayersToggle extends ToggleItem<GameConfig.Type> {
		public PlayersToggle(TextureRegion background, GameConfig.Type state) {
			super(background, state);
		}

		@Override
		public GameConfig.Type nextState() {
			if(state.equals(GameConfig.Type.QuickPlay)) return GameConfig.Type.Multiplayer;
			else return GameConfig.Type.QuickPlay;
		}

		@Override
		public TextureRegion getRegion() {
			if(state.equals(GameConfig.Type.Multiplayer)) return images.p2;
			else return images.p1;
		}
	}
}
