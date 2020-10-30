package Maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Maze {

	private static Random random = new Random();
	private int width, height; 
	private int[][] wallIndex;
	private boolean[][] wallToRight;
	private boolean[][] wallDown;
	private int [][] horizontalWeights;
	private int [][] verticalWeights;
	
	public Maze(int mazeHeight, int mazeWidth, int sparcity) {
		this(mazeHeight, mazeWidth, sparcity, random.nextLong());
	}
	
	public Maze(int mazeHeight, int mazeWidth, int sparcity, long randomSeed) {
		random = new Random(randomSeed);
		width = mazeWidth + 1;
		height = mazeHeight + 1;
		int currWall = 1;
		horizontalWeights = new int[height - 2][width - 1];
		verticalWeights = new int[height - 1][width - 2];
		for (int i = 0; i < height - 2; i++) {
			for (int j = 0; j < width - 1; j++) {
				horizontalWeights[i][j] = random.nextInt(9) + 1;
			}
		}
		for (int i = 0; i < height - 1; i++) {
			for (int j = 0; j < width - 2; j++) {
				verticalWeights[i][j] = random.nextInt(9) + 1;

			}
		}
		wallIndex = new int[height][width];
		for (int i = 0; i < width; i++) {
			wallIndex[0][i] = currWall;
			wallIndex[height - 1][i] = currWall;
		}
		for (int i = 0; i < height; i++) {
			wallIndex[i][0] = currWall;
			wallIndex[i][width - 1] = currWall;
		}
		wallToRight = new boolean[height][width];
		wallDown = new boolean[height][width];
		for (int i = 0; i < width - 1; i++) {
			wallToRight[0][i] = true;
			wallToRight[height - 1][i] = true;
		}
		for (int i = 0; i < height - 1; i++) {
			wallDown[i][0] = true;
			wallDown[i][width - 1] = true;
		}

		ArrayList<Juncture> points = new ArrayList<>();
		for (int x = 1; x < width; x++) {
			for (int y = 1; y < height; y++) {
				points.add(new Juncture(x, y));
			}
		}
		Collections.shuffle(points);
		
		while (points.size() > 0) {
			Juncture p = points.remove(0);
			int row = p.getY();
			int col = p.getX();
			if (wallIndex[row][col] == 0) {
				currWall++;
				wallIndex[row][col] = currWall;
				drawFrom(row, col, currWall);
			}
		}
		
		if (sparcity > 0) {
			for (int i = 0; i < width * height * sparcity/50.0; i++) {
				int r = random.nextInt(height - 2) + 1;
				int c = random.nextInt(width - 2) + 1;
				if (random.nextBoolean() == false) {
					wallDown[r][c] = false;
				} else {
					wallToRight[r][c] = false;
				}
			}
		}
	}
	
	private void drawFrom(int row, int col, int currWall) {
		if (possibleToExtend(row, col, currWall)) {
			while(true) {
				int directionToTry = random.nextInt(4);
				if (directionToTry == 0) {  // to right
					int wallFound = wallIndex[row][col + 1];
					if (wallFound != currWall) {
						wallIndex[row][col + 1] = currWall;
						wallToRight[row][col] = true;
						if (wallFound == 0) {
							drawFrom(row, col + 1, currWall);
						}
						return;
					}
				} else if (directionToTry == 1) {  // to left
					int wallFound = wallIndex[row][col - 1];
					if (wallFound != currWall) {
						wallIndex[row][col - 1] = currWall;
						wallToRight[row][col - 1] = true;
						if (wallFound == 0) {
							drawFrom(row, col - 1, currWall);
						}
						return;
					}
				} else if (directionToTry == 2) {  // up
					int wallFound = wallIndex[row - 1][col];
					if (wallFound != currWall) {
						wallIndex[row - 1][col] = currWall;
						wallDown[row - 1][col] = true;
						if (wallFound == 0) {
							drawFrom(row - 1, col, currWall);
						}
						return;
					}
				} else { 
					int wallFound = wallIndex[row + 1][col];
					if (wallFound != currWall) {
						wallIndex[row + 1][col] = currWall;
						wallDown[row][col] = true;
						if (wallFound == 0) {
							drawFrom(row + 1, col, currWall);
						}
						return;
					}
				}
			}
		} else { 
			while(true) {
				int r = random.nextInt(height);
				int c = random.nextInt(width);
				if (wallIndex[r][c] == currWall && possibleToExtend(r, c, currWall)) {
					drawFrom(r, c, currWall);
					return;
				}
			}
		}
	}
	
	private boolean possibleToExtend(int row, int col, int currWall) {
		if (wallIndex[row][col + 1] != currWall) {
			return true;
		}
		if (wallIndex[row][col - 1] != currWall) {
			return true;
		}
		if (wallIndex[row + 1][col] != currWall) {
			return true;
		}
		if (wallIndex[row - 1][col] != currWall) {
			return true;
		}
		return false;
	}

	public int getMazeWidth() {
		return width - 1;
	}

	public int getMazeHeight() {
		return height - 1;
	}
	
	public boolean isWallAbove(Juncture juncture) {
		return wallToRight[juncture.getY()][juncture.getX()];
	}

	public boolean isWallBelow(Juncture juncture) {
		return wallToRight[juncture.getY()+ 1][juncture.getX()];
	}

	public boolean isWallToLeft(Juncture juncture) {
		return wallDown[juncture.getY()][juncture.getX()];
	}

	public boolean isWallToRight(Juncture juncture) {
		return wallDown[juncture.getY()][juncture.getX() + 1];
	}

	public int getWeightAbove(Juncture juncture) {
		return horizontalWeights[juncture.getY() - 1][juncture.getX()];
	}

	public int getWeightBelow(Juncture juncture) {
		return horizontalWeights[juncture.getY()][juncture.getX()];
	}

	public int getWeightToLeft(Juncture juncture) {
		return verticalWeights[juncture.getY()][juncture.getX() - 1];
	}
	
	public int getWeightToRight(Juncture juncture) {
		return	verticalWeights[juncture.getY()][juncture.getX()];
	}
}
