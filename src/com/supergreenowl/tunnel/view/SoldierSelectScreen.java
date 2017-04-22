package com.supergreenowl.tunnel.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.ai.BalancedSelectionStrategy;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.controller.Player;
import com.supergreenowl.tunnel.model.Direction;
import com.supergreenowl.tunnel.model.SoldierType;
import com.supergreenowl.tunnel.ui.ColourFactory;
import com.supergreenowl.tunnel.ui.NumberWriter;
import com.supergreenowl.tunnel.ui.grid.Button;
import com.supergreenowl.tunnel.ui.grid.ClickListener;
import com.supergreenowl.tunnel.ui.grid.ContainerItem;
import com.supergreenowl.tunnel.ui.grid.GridItem;
import com.supergreenowl.tunnel.ui.grid.GridScreen;
import com.supergreenowl.tunnel.ui.grid.NumberItem;
import com.supergreenowl.tunnel.ui.grid.Panel;
import com.supergreenowl.tunnel.ui.grid.SelectItem;
import com.supergreenowl.tunnel.ui.grid.SelectionListener;
import com.supergreenowl.tunnel.ui.grid.TouchListener;

public class SoldierSelectScreen extends GridScreen implements SelectionListener {

	private SoldierSelectImages images;
	private GameConfig config;
	private Player player;
	private BalancedSelectionStrategy balancedSelection;
	
	private SelectItem[] soldiers;
	private NumberItem[] soldierCounts;
	private Button buy, sell, forward, autobuy;
	private NumberItem wealth;
	
	private int selectedType;
	
	public SoldierSelectScreen(Game game, GameConfig config, Player player) {
		super(game, 5);
		
		balancedSelection = new BalancedSelectionStrategy(false);
		
		this.music = game.music.menu;
		setBackgroundColour(ColourFactory.MENU_BACKGROUND);
		
		this.config = config;
		this.player = player;
		
		images = new SoldierSelectImages();
		manage(images);
		
		selectedType = Player.NONE_SELECTED;
		
		NumberWriter number = new NumberWriter(SoldierSelectImages.NUMBER_WIDTH, SoldierSelectImages.NUMBER_HEIGHT, images.digits, 2);
		
		// Create soldier selection buttons
		soldiers = new SelectItem[SoldierType.COUNT];
		soldierCounts = new NumberItem[SoldierType.COUNT];
		for(int i = 0; i < SoldierType.COUNT; i++) {
			soldiers[i] = soldierIcon(i);
			soldiers[i].isDisabled = !config.available.allows(i);
			soldiers[i].setSelectionListener(this);
			
			soldierCounts[i] = new NumberItem(images.brickSmall, number);
			if(player.selectedSoldiers[i] > 0) soldierCounts[i].number = player.selectedSoldiers[i];
		}
		
		// Build grid
		ContainerItem back = new ContainerItem(images.brickActiveSmall, images.back);
		back.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				back();
			}
		});
		add(back);
		
		TextureRegion playerTexture = player.direction == Direction.West ? images.p1 : images.p2;
		add(new Panel(images.brickLarge, playerTexture, images.title));
		
		forward = new Button(images.brickActiveSmall, images.forward, images.forwardDisabled);
		forward.setClickListener(new ClickListener() {
			@Override
			public void onClick(GridItem sender) {
				forward();				
			}
		});
		add(forward);
		nextRow();
		
		add(soldiers[SoldierType.RED]);
		add(soldierCounts[SoldierType.RED]);
		add(soldiers[SoldierType.CYAN]);
		add(soldierCounts[SoldierType.CYAN]);
		add(soldiers[SoldierType.WHITE]);
		add(soldierCounts[SoldierType.WHITE]);
		nextRow();
		
		add(soldiers[SoldierType.GREEN]);
		add(soldierCounts[SoldierType.GREEN]);
		add(soldiers[SoldierType.MAGENTA]);
		add(soldierCounts[SoldierType.MAGENTA]);
		add(images.brick);
		nextRow();
		
		add(soldiers[SoldierType.BLUE]);
		add(soldierCounts[SoldierType.BLUE]);
		add(soldiers[SoldierType.YELLOW]);
		add(soldierCounts[SoldierType.YELLOW]);
		autobuy = new Button(images.brickActive, images.auto, images.autoDisabled);
		autobuy.setClickListener(new ClickListener() {
			@Override
			public void onClick(GridItem sender) {
				autoBuy();
			}
		});
		add(autobuy);
		nextRow();
		
		buy = new Button(images.brickActiveSmall, images.buy, images.buyDisabled);
		buy.setClickListener(new ClickListener() {
			@Override
			public void onClick(GridItem sender) {
				buy();
			}
		});
		add(buy);
		sell = new Button(images.brickActiveSmall, images.sell, images.sellDisabled);
		sell.setClickListener(new ClickListener() {
			@Override
			public void onClick(GridItem sender) {
				sell();
			}
		});
		add(sell);
		add(new ContainerItem(images.brick, images.wealth));
		wealth = new NumberItem(images.brick, number);
		add(wealth);
		
		updateView();
	}
	
	@Override
	public void onSelected(SelectItem sender) {
		// Work out soldier type that was selected
		for(int i = 0; i < SoldierType.COUNT; i++) {
			SelectItem item = soldiers[i];
			if(item != sender && item.isSelected) {
				item.isSelected = false;
			}
			else if(item == sender) {
				this.selectedType = i;
			}
		}
		
		updateView();
	}

	@Override
	public void onUnselected(SelectItem sender) {
		this.selectedType = Player.NONE_SELECTED;
		
		updateView();
	}

	private void back() {
		if(player.direction == Direction.East) {
			// Move back to P1 setup if this is P2
			game.setScreen(new SoldierSelectScreen(game, config, config.west));
		}
		else {
			if(config.type == GameConfig.Type.Campaign) game.setScreen(new LevelSelectScreen(game));
			else game.setScreen(new SetupScreen(game, config));
		}
	}
	
	private void forward() {
		if(player.direction == Direction.West && config.east.isHuman) {
			// Move on to P2 setup if this is P1 and P2 is human
			game.setScreen(new SoldierSelectScreen(game, config, config.east));
		}
		else if(player.direction == Direction.East && config.type == GameConfig.Type.Multiplayer) {
			// Show 2 player position device instructions
			game.setScreen(new PositionScreen(game, config));
		}
		else {
			// Start the game!
			game.setScreen(new GameScreen(game, config));
		}
	}
	
	/**
	 * Buys a soldier if the player can afford a soldier of the selected type.
	 */
	private void buy() {
		if(selectedType != Player.NONE_SELECTED && config.available.allows(selectedType)) {
			int remaining = config.coins - player.totalCost();
			int cost = SoldierType.COST[selectedType];
			if(cost <= remaining) {
				if(player.selectedSoldiers[selectedType] == Player.NONE_SELECTED) player.selectedSoldiers[selectedType] = 0;
				player.selectedSoldiers[selectedType]++;
			}
		}
		
		updateView();
	}
	
	/**
	 * Automatically spends remaining coins for the player.
	 */
	private void autoBuy() {
		// TODO this currently ignores what the player has already selected:
		// - could modify BalancedSelectionStrategy to take into account the existing values in the selection array
		// - or leave it as if player selects some and then auto-buys this retains their selection as separate from the auto selection??
		
		// Use a balanced selection with the remaining coins
		int remaining = config.coins - player.totalCost();
		balancedSelection.select(player, remaining, config.available);
		updateView();
	}
	
	/**
	 * Sells a soldier of the selected type if the player has one to sell.
	 */
	private void sell() {
		if(selectedType != Player.NONE_SELECTED && player.selectedSoldiers[selectedType] > 0) {
			player.selectedSoldiers[selectedType]--;
			if(player.selectedSoldiers[selectedType] == 0) player.selectedSoldiers[selectedType] = Player.NONE_SELECTED;
		}
		
		updateView();
	}
	
	private void updateView() {
		
		// Update remaining number of coins display
		int remaining = config.coins - player.totalCost();
		wealth.number = remaining;
		
		// Enable/disable forward button - must spend all coins to proceed
		forward.isDisabled = remaining > 0;
		
		// Enable/disabled auto buy button - must have enough coins to afford a soldier
		int minCost = config.available.allows(SoldierType.WHITE) ? SoldierType.COST[SoldierType.WHITE] : SoldierType.COST[SoldierType.RED];
		autobuy.isDisabled = remaining < minCost;
		
		// Enable/disable buy and sell buttons - must have a soldier type selected and be able to afford it/have some to sell
		if(selectedType == Player.NONE_SELECTED) {
			buy.isDisabled = true;
			sell.isDisabled = true;
		}
		else {
			buy.isDisabled = remaining < SoldierType.COST[selectedType];
			sell.isDisabled = player.selectedSoldiers[selectedType] < 1;
		}
		
		// Update number selected displays
		for(int i = 0; i < SoldierType.COUNT; i++) {
			int count = player.selectedSoldiers[i];
			if(count == Player.NONE_SELECTED) count = 0;
			soldierCounts[i].number = count;
		}
	}
	
	/**
	 * Creates a select item representing the specified soldier type.
	 * @param soldierType Soldier type to create item for.
	 * @return New {@code SelectItem} for the specified type.
	 */
	private SelectItem soldierIcon(int soldierType) {
		return new SelectItem(images.brickActiveSmall, images.brickSelectedSmall, images.shields[soldierType], images.disabledShields[soldierType]);
	}
}
