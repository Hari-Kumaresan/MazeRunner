package Graph;

import java.util.List;

public interface GraphAlgorithmObserver<V> {
	
	public void notifyDFSHasBegun();

	public void notifyBFSHasBegun();
	
	public void notifyVisit(V vertexBeingVisited);
	
	public void notifySearchIsOver();
	
	public void notifyDijkstraHasBegun();
	
	public void notifyDijkstraVertexFinished(V vertexAddedToFinishedSet, Integer costOfPath);
	
	public void notifyDijkstraIsOver(List<V> path);
}
