package com.supergreenowl.tunnel.view;

import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.controller.GameConfig;
import com.supergreenowl.tunnel.ui.ColourFactory;
import com.supergreenowl.tunnel.ui.grid.ContainerItem;
import com.supergreenowl.tunnel.ui.grid.GridItem;
import com.supergreenowl.tunnel.ui.grid.GridScreen;
import com.supergreenowl.tunnel.ui.grid.Switch;
import com.supergreenowl.tunnel.ui.grid.SwitchListener;
import com.supergreenowl.tunnel.ui.grid.TouchListener;

/**
 * {@link GridScreen} shown immediately before the game screen in a 2 player game.
 * Instructs players on how to position the device.
 * @author luke
 *
 */
public class PositionScreen extends GridScreen implements SwitchListener {

	private GameConfig config;
	private PositionImages images;
	private Switch p1, p2;
	
	public PositionScreen(Game game, GameConfig config) {
		super(game, 5);
		this.config = config;
		
		images = new PositionImages();
		manage(images);
		
		this.music = game.music.menu;
		this.setBackgroundColour(ColourFactory.MENU_BACKGROUND);
		
		// Build grid
		add(images.brickSmall);
		add(new ContainerItem(images.brickSmall, images.arrowUp));
		add(new ContainerItem(images.brick, images.p2));
		add(new ContainerItem(images.brickSmall, images.arrowUp));
		add(images.brickSmall);
		nextRow();
		
		add(images.brick);
		p2 = new Switch(images.brickActive, images.readyUpYes, images.readyUpNo, false);
		p2.setSwitchListener(this);
		add(p2);
		add(images.brick);
		nextRow();
		
		ContainerItem back = new ContainerItem(images.brickActiveSmall, images.back);
		back.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				back();
			}
		});
		add(back);
		add(images.position);
		add(images.brickSmall);
		nextRow();
		
		add(images.brick);
		p1 = new Switch(images.brickActive, images.readyDownYes, images.readyDownNo, false);
		p1.setSwitchListener(this);
		add(p1);
		add(images.brick);
		nextRow();
		
		add(images.brickSmall);
		add(new ContainerItem(images.brickSmall, images.arrowDown));
		add(new ContainerItem(images.brick, images.p1));
		add(new ContainerItem(images.brickSmall, images.arrowDown));
		add(images.brickSmall);
	}

	@Override
	public void onSwitch(Switch sender) {
		if(p1.isOn && p2.isOn) {
			GameScreen gs = new GameScreen(game, config);
			game.setScreen(gs);
		}
	}
	
	private void back() {
		// Since this screen is only shown in a 2P game, I know east is human
		game.setScreen(new SoldierSelectScreen(game, config, config.east));
	}

}
