package Graph;

import Graph.WeightedGraph;
import Maze.Juncture;
import Maze.Maze;

public class MazeGraph extends WeightedGraph<Juncture> {

	public MazeGraph(Maze maze) {

		for (int w = 0; w < maze.getMazeWidth(); w++) {
			for (int h = 0; h < maze.getMazeHeight(); h++) {
				Juncture tempJuncture = new Juncture(w, h);
				addVertex(tempJuncture);

				if (!maze.isWallToLeft(tempJuncture)) {
					addEdge(tempJuncture, new Juncture(w - 1, h), maze.getWeightToLeft(tempJuncture));
				}
				if (!maze.isWallToRight(tempJuncture)) {
					addEdge(tempJuncture, new Juncture(w + 1, h), maze.getWeightToRight(tempJuncture));
				}
				if (!maze.isWallAbove(tempJuncture)) {
					addEdge(tempJuncture, new Juncture(w, h - 1), maze.getWeightAbove(tempJuncture));
				}
				if (!maze.isWallBelow(tempJuncture)) {
					addEdge(tempJuncture, new Juncture(w, h + 1), maze.getWeightBelow(tempJuncture));
				}
			}
		}
	}
}