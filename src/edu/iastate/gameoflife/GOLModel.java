package edu.iastate.gameoflife;

public class GOLModel {
	public interface ModelChangeListener {
		void onChange();
		void onCellChange(int x, int y);
	}
	
	private final int width;
	private final int height;
	private boolean[][] board;
	private boolean[][] temp;

	public GOLModel(int width, int height) {
		this.width = width;
		this.height = height;
		this.board = new boolean[height][width];
		this.temp = new boolean[height][width];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean getCell(int x, int y) {
		if (x < 0 || x >= width)
			return false;
		if (y < 0 || y >= height)
			return false;

		return board[y][x];
	}

	public void setCell(int x, int y, boolean state) {
		board[y][x] = state;
	}
	
	public void toggleCell(int x, int y) {
		setCell(x, y, !getCell(x, y));
	}
	
	public void nextGeneration() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int livingNeighbors = countLivingNeighbors(x, y);

				if (getCell(x, y)) {

					// Overcrowding death
					if (livingNeighbors >= 4)
						temp[y][x] = false;

					// Loneliness death
					else if (livingNeighbors <= 1)
						temp[y][x] = false;

					// Still alive
					else
						temp[y][x] = true;

				} else {

					// Born
					if (livingNeighbors == 3)
						temp[y][x] = true;

					// Still dead
					else
						temp[y][x] = false;
				}
			}
		}
		
		boolean[][] swap = board;
		board = temp;
		temp = swap;
	}

	private int countLivingNeighbors(int x, int y) {
		int count = 0;

		count += getCell(x + 0, y - 1) ? 1 : 0; // N
		count += getCell(x + 1, y - 1) ? 1 : 0; // NE
		count += getCell(x + 1, y + 0) ? 1 : 0; // E
		count += getCell(x + 1, y + 1) ? 1 : 0; // SE

		count += getCell(x + 0, y + 1) ? 1 : 0; // S
		count += getCell(x - 1, y + 1) ? 1 : 0; // SW
		count += getCell(x - 1, y + 0) ? 1 : 0; // W
		count += getCell(x - 1, y - 1) ? 1 : 0; // NW

		return count;
	}
}
