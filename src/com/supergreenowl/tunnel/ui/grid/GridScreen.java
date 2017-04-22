package com.supergreenowl.tunnel.ui.grid;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.supergreenowl.tunnel.Game;
import com.supergreenowl.tunnel.ui.Screen;

/**
 * A {@link Screen} that lays out its content in a rectangular grid with a fixed number of rows.
 * The entire grid is centred horizontally and vertically on the screen.
 * Items in the grid are centred vertically relative to each grid row; no horizontal item alignment takes place.
 * 
 * The grid has optional padding between items and rows (which is off by default).
 * @author luke
 *
 */
public abstract class GridScreen extends Screen {
	
	/**
	 * Number of rows in the grid.
	 */
	protected final int rows;
	
	private int currentRow;
	private int[] rowHeights;
	private float paddingX, paddingY;
	
	private boolean needsLayout;
	private Array<Array<GridItem>> contents;
	
	public GridScreen(Game game, int rows) {
		super(game, false); // No continuous rendering on grids
		this.rows = rows;
		
		rowHeights = new int[rows];
		
		contents = new Array<Array<GridItem>>(rows);
		clearGrid(); // clearing the grid initialises the rows
		
		paddingX = paddingY = 0f;
	}
	
	/**
	 * Adds an item to this grid at the end of the current row.
	 * @param item Item to add.
	 * @see #nextRow()
	 */
	public void add(GridItem item) {
		contents.get(currentRow).add(item);
		item.parent = this;
		needsLayout = true;
	}
	
	/**
	 * Adds a new item that displays the specified {@code TextureRegion} at the end of the current row. 
	 * @param texture {@code TextureRegion} to add to the grid.
	 * @see #nextRow()
	 */
	public void add(TextureRegion texture) {
		add(new GridItem(texture));
	}
	
	/**
	 * Signals that any further {@code add} calls should add items to the next row of this grid.
	 * If items are already being added to the last row of the grid then this will have no effect.
	 */
	public void nextRow() {
		if(currentRow < rows - 1) {
			currentRow++;
		}
	}
	
	/**
	 * Sets the padding between items in the grid. Set to zero to clear padding.
	 * @param x Horizontal padding between items in the same row.
	 * @param y Vertical padding between rows.
	 */
	public void setPadding(float x, float y) {
		this.paddingX = x;
		this.paddingY = y;
		needsLayout = true;
	}
	
	/**
	 * Signals that the layout for this grid needs to be recalculated the next time the screen is updated.
	 */
	public void invalidateLayout() {
		needsLayout = true;
	}
	
	/**
	 * Removes all items from this grid.
	 */
	public void clearGrid() {
		contents.clear();
		
		for(int i = 0; i < rows; i++) {
			contents.add(new Array<GridItem>());
		}
		
		currentRow = 0;
		needsLayout = true;
	}

	/**
	 * Sends a touch to whichever item in this grid that was touched.
	 */
	@Override
	protected void onTouch(float x, float y) {
		for(int i = 0; i < rows; i++) {
			Array<GridItem> row = contents.get(i);
			int cells = row.size;
			
			for(int j = 0; j < cells; j++) {
				GridItem item = row.get(j);
				if(item.isTouched(x, y)) {
					item.touch(x, y);
					return;
				}
			}
		}
	}

	/**
	 * Updates the grid layout when required. Derived types should call this in their implementation.
	 */
	@Override
	protected void update(float delta) {
		if(needsLayout) layout();
	}

	/**
	 * Renders all of the items in the grid.
	 */
	@Override
	protected void draw(float delta, SpriteBatch batch) {
		for(int i = 0; i < rows; i++) {
			Array<GridItem> row = contents.get(i);
			int cells = row.size;
			
			for(int j = 0; j < cells; j++) {
				GridItem item = row.get(j);
				item.draw(batch);
			}
		}
	}
	
	/**
	 * Calculates and sets the position for each item in the grid. 
	 */
	private void layout() {
		
		// Calculate grid dimensions
		int width = 0;
		int height = 0;
		
		for(int i = 0; i < rows; i++) {
			Array<GridItem> row = contents.get(i);
			int rowWidth = 0;
			int rowHeight = 0;
			int cells = row.size;
			
			for(int j = 0; j < cells; j++) {
				GridItem item = row.get(j);
				
				rowWidth += item.width; // row width = sum(item width)
				if(j < rows - 1) rowWidth += paddingX; // add horizontal padding into row width calculation
				
				if(item.height > rowHeight) rowHeight = item.height; // row height = max(item height)
			}
			
			rowHeights[i] = rowHeight;
			if(rowWidth > width) width = rowWidth; // grid width = max(row width)
			height += rowHeight; // grid height = sum(row height)
		}
		
		height += paddingY * (rows - 1);
		
		// Layout each item in the grid
		// cells are added top to bottom but y-axis goes up so y calculations are inverted
		
		float offsetX = (TARGET_WIDTH - (float)width) / 2f;
		float offsetY = (TARGET_HEIGHT - (float)height) / 2f;
		
		float y = TARGET_HEIGHT - offsetY;
		
		for(int i = 0; i < rows; i++) {
			int rowHeight = rowHeights[i];
			y -= rowHeight;
			float x = offsetX;
			
			Array<GridItem> row = contents.get(i);
			int cells = row.size;
			
			for(int j = 0; j < cells; j++) {
				GridItem item = row.get(j);
				
				float itemY = y;
				if(item.height < rowHeight) {
					itemY += (float)(rowHeight - item.height) / 2f;
				}
				
				item.place(x, itemY);
				x += item.width + paddingX;
				y -= paddingY;
			}
		}
		
		needsLayout = false;
	}

}
