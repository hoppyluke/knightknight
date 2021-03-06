package com.supergreenowl.tunnel.view;

import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.ui.ColourFactory;
import com.supergreenowl.tunnel.ui.grid.GridItem;
import com.supergreenowl.tunnel.ui.grid.GridScreen;
import com.supergreenowl.tunnel.ui.grid.TouchListener;

public class CreditsScreen extends GridScreen {

	private CreditsImages images;
	
	public CreditsScreen(Game game) {
		super(game, 2);
		
		images = new CreditsImages();
		manage(images);
		
		setBackgroundColour(ColourFactory.MENU_BACKGROUND);
		music = game.music.menu;
		
		// Build the grid
		GridItem back = new GridItem(images.back);
		back.setTouchListener(new TouchListener() {
			@Override
			public void onTouch(GridItem sender, float x, float y) {
				back();
			}
		});
		
		add(back);
		add(images.title);
		nextRow();
		
		add(images.credits);		
	}

	private void back() {
		game.setScreen(new HomeScreen(game));
	}
	
}
