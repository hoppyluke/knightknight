package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.utils.Array;
import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.ai.AI;
import com.supergreenowl.tunnel.ai.AIFactory;
import com.supergreenowl.tunnel.campaign.Level;
import com.supergreenowl.tunnel.campaign.LevelLoader;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.ui.ColourFactory;
import com.supergreenowl.tunnel.ui.NumberWriter;
import com.supergreenowl.tunnel.ui.grid.Button;
import com.supergreenowl.tunnel.ui.grid.ClickListener;
import com.supergreenowl.tunnel.ui.grid.ContainerItem;
import com.supergreenowl.tunnel.ui.grid.GridItem;
import com.supergreenowl.tunnel.ui.grid.GridScreen;
import com.supergreenowl.tunnel.ui.grid.TouchListener;
import com.supergreenowl.tunnel.ui.grid.TouchNumberItem;

public class LevelSelectScreen extends GridScreen implements ClickListener {

	private static final int COLUMNS = 6;
	private static final int ROWS = 4;
	private static final int LEVELS_PER_PAGE = ROWS * COLUMNS;
	
	// Ratio of the width of a large brick to a small brick used in padding calculations when there are
	// less levels to display on a page than the page size
	private static final int PADDING_BRICK_RATIO = 2;
	
	private Array<Level> levels;
	private LevelSelectImages images;
	
	private ContainerItem back, title;
	private GridItem titleLeft, titleRight;
	private GridItem forward;
	private TouchNumberItem[] levelButtons;
	private Array<GridItem> lastPagePadding;
	
	private boolean hasMultipleScreens;
	private int firstLevelShown;
	private int lastLevelCompleted;
	
	public LevelSelectScreen(Game game) {
		super(game, 5);
		
		images = new LevelSelectImages();
		manage(images);
		
		music = game.music.menu;
		setBackgroundColour(ColourFactory.MENU_BACKGROUND);
				
		LevelLoader loader = new LevelLoader();
		levels = loader.load();
		
		if(levels.size <= 0) throw new IllegalStateException("Failed to load levels.");
		
		hasMultipleScreens = levels.size > LEVELS_PER_PAGE;
		
		// Load last level from settings - and ensure this is not > no. of levels
		lastLevelCompleted = Math.min(levels.size, game.settings.getLastLevelCompleted());
		firstLevelShown = ((lastLevelCompleted - 1) / LEVELS_PER_PAGE) * LEVELS_PER_PAGE + 1; // jump to page with last level completed
		
		// Build grid items - build title row
		back = new ContainerItem(images.brickActive, images.back);
		back.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				back();
			}
		});
		titleLeft = new GridItem(images.brickSmall);
		title = new ContainerItem(images.brick, images.title);
		titleRight = new GridItem(images.brickSmall);
		if(hasMultipleScreens) {
			Button btnForward = new Button(images.brickActive, images.forward, images.forwardDisabled);
			btnForward.setClickListener(new ClickListener() {
				@Override
				public void onClick(GridItem sender) {
					forward();
				}
			});
			forward = btnForward;
			toggleForwardEnabled();
		}
		else {
			forward = new GridItem(images.brickSmall);
		}
		
		// Build grid items - build level buttons
		int length = levels.size;
		levelButtons = new TouchNumberItem[length];
		int maxDigits = Integer.toString(length).length();
		NumberWriter enabled = new NumberWriter(LevelSelectImages.NUMBER_WIDTH, LevelSelectImages.NUMBER_HEIGHT, images.digits, maxDigits);
		NumberWriter disabled = new NumberWriter(LevelSelectImages.NUMBER_WIDTH, LevelSelectImages.NUMBER_HEIGHT, images.disabledDigits, maxDigits);
				
		for(int i = 0; i < length; i++) {
			Level l = levels.get(i);
			TouchNumberItem nextLevel = new TouchNumberItem(images.brickActive, enabled, disabled);
			nextLevel.number = l.id;
			nextLevel.isDisabled = l.id > lastLevelCompleted + 1; // disable level if not unlocked
			nextLevel.setClickListener(this);
			levelButtons[i] = nextLevel;
		}
		
		// Build grid items - build padding
		int itemsOnLastPage = length % LEVELS_PER_PAGE;
		
		if(itemsOnLastPage != 0) {
			// Calculate number of bricks required to pad the page -
			// will use 1x1 bricks in last column and 2x1 bricks in all other columns
			int paddingItems = ((LEVELS_PER_PAGE - itemsOnLastPage) + 1) / PADDING_BRICK_RATIO;
			lastPagePadding = new Array<GridItem>(paddingItems);
			
			int nextIndex = itemsOnLastPage + 1;
			while(nextIndex <= LEVELS_PER_PAGE) {
				if(nextIndex % COLUMNS == 0) {
					lastPagePadding.add(new GridItem(images.brickSmall));
					nextIndex++;
				}
				else {
					lastPagePadding.add(new GridItem(images.brick));
					nextIndex += PADDING_BRICK_RATIO;
				}
			}
			
		}
		else lastPagePadding = null; // No padding required, levels fill final page
		
		fillGrid();
	}
	
	@Override
	public void onClick(GridItem sender) {
		if(sender instanceof TouchNumberItem) {
			TouchNumberItem item = (TouchNumberItem)sender;
			int levelId = item.number;
			int levelIndex = levelId - 1; // level IDs are 1-based, indices are 0-based
			
			Level l = levels.get(levelIndex);
			
			GameConfig config = new GameConfig(GameConfig.Type.Campaign, l.difficulty, l.available);
			config.levelId = l.id;
			config.coins = l.coins;
			config.west = new Player(true, Direction.West);
			
			AI ai = AIFactory.create(l.difficulty, Direction.East, l.aiSoldiers);
			ai.selectSoldiers(config.coins, config.available);
			config.east = ai;
			
			if(l.playerSoldiers == null) {
				game.setScreen(new SoldierSelectScreen(game, config, config.west));
			} else {
				l.playerSoldiers.select(config.west.selectedSoldiers);
				game.setScreen(new GameScreen(game, config));	
			}
			
		}
	}
	
	private void back() {
		if(hasMultipleScreens && firstLevelShown > 1) {
			firstLevelShown -= LEVELS_PER_PAGE;
			toggleForwardEnabled();
			
			// Redraw grid for new page
			clearGrid();
			fillGrid();
		}
		else {
			game.setScreen(new HomeScreen(game));
		}
	}
	
	private void forward() {
		firstLevelShown += LEVELS_PER_PAGE;
		toggleForwardEnabled();
		
		// Redraw grid for new page
		clearGrid();
		fillGrid();
	}
	
	/**
	 * Disables the forward button if the last page of levels is currently being shown.
	 * Enables it otherwise. If there is only 1 page of levels, does nothing.
	 */
	private void toggleForwardEnabled() {
		if(forward instanceof Button) {
			Button b = (Button)forward;
			b.isDisabled = firstLevelShown + LEVELS_PER_PAGE - 1 >= levels.size;
		}
	}
	
	private void fillGrid() {
		
		// Add title row items
		add(back);
		add(titleLeft);
		add(title);
		add(titleRight);
		add(forward);
		nextRow();
		
		// Add buttons for the levels on the current page
		int levelsOnPage = Math.min(levels.size - firstLevelShown + 1, LEVELS_PER_PAGE);
		int row = 0;
		int col = 0;
		
		for(int i = firstLevelShown - 1; i < firstLevelShown + levelsOnPage - 1; i++) {
			add(levelButtons[i]);
			
			if(++col == COLUMNS) {
				if(++row == ROWS) break;
				nextRow();
				col = 0;
			}
		}
		
		// If there is space left on this page then fill it with padding bricks
		if(levelsOnPage < LEVELS_PER_PAGE) {
			int n = lastPagePadding.size;
			for(int i = 0; i < n; i++) {
				add(lastPagePadding.get(i));
				
				if(++col == COLUMNS) {
					if(++row == ROWS) break;
					nextRow();
					col = 0;
				}
				else if(++col == COLUMNS) {
					if(++row == ROWS) break;
					nextRow();
					col = 0;
				}
			}
		}
	}

}
