package Graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class WeightedGraph<V> {

	private Map<V, Map<V, Integer>> weightedGraph;

	private Collection<GraphAlgorithmObserver<V>> observerList;

	public WeightedGraph() {
		weightedGraph = new HashMap<V, Map<V, Integer>>();
		observerList = new ArrayList<GraphAlgorithmObserver<V>>();
	}

	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);
	}

	public void addVertex(V vertex) {
		if (!containsVertex(vertex)) {
			weightedGraph.put(vertex, new HashMap<V, Integer>());
		} else {
			throw new IllegalArgumentException();
		}

	}

	public boolean containsVertex(V vertex) {
		return weightedGraph.containsKey(vertex);
	}

	public void addEdge(V from, V to, Integer weight) {
		if (containsVertex(from) || containsVertex(to) || weight > 0) {
			weightedGraph.get(from).put(to, weight);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Integer getWeight(V from, V to) {
		if (containsVertex(from) && containsVertex(to)) {
			return weightedGraph.get(from).get(to);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void DoBFS(V start, V end) {
		Queue<V> toCheck = new LinkedList<V>();
		HashSet<V> visited = new HashSet<V>();
		for (GraphAlgorithmObserver<V> o : observerList) {
			o.notifyBFSHasBegun();
		}
		toCheck.add(start);
		while (!toCheck.isEmpty()) {
			V first = toCheck.poll();
			if (!visited.contains(first)) {
				for (GraphAlgorithmObserver<V> observers : observerList) {
					observers.notifyVisit(first);
				}
				visited.add(first);
				for (V next : weightedGraph.get(first).keySet()) {
					if (!visited.contains(next)) {
						toCheck.add(next);
					}
				}
			}
			if (first.equals(end)) {
				for (GraphAlgorithmObserver<V> observers : observerList) {
					observers.notifySearchIsOver();
				}
				return;
			}
		}

	}

	public void DoDFS(V start, V end) {
		Stack<V> toCheck = new Stack<V>();
		HashSet<V> visited = new HashSet<V>();
		for (GraphAlgorithmObserver<V> o : observerList) {
			o.notifyDFSHasBegun();
		}
		toCheck.push(start);
		while (!toCheck.isEmpty()) {
			V first = toCheck.pop();
			if (!visited.contains(first)) {
				for (GraphAlgorithmObserver<V> o : observerList) {
					o.notifyVisit(first);
				}
				visited.add(first);
				for (V next : weightedGraph.get(first).keySet()) {
					if (!visited.contains(next)) {
						toCheck.push(next);
					}
				}
			}
			if (first.equals(end)) {
				for (GraphAlgorithmObserver<V> o : observerList) {
					o.notifySearchIsOver();
				}
				return;
			}
		}

	}

	public void DoDijsktra(V start, V end) {
		HashSet<V> visited = new HashSet<V>();
		HashMap<V, V> predecessor = new HashMap<V, V>();
		HashMap<V, Integer> costs = new HashMap<V, Integer>();
		V minVertexCost = start;

		for (V vertex : weightedGraph.keySet()) {
			if (vertex.equals(start)) {
				predecessor.put(vertex, vertex);
				costs.put(vertex, 0);
			} else {
				predecessor.put(vertex, null);
				costs.put(vertex, Integer.MAX_VALUE);
			}
		}

		for (GraphAlgorithmObserver<V> o : observerList) {
			o.notifyDijkstraHasBegun();
		}

		while (!visited.containsAll(weightedGraph.keySet())) {
			int min = Integer.MAX_VALUE;
			for (V vertex : costs.keySet()) {
				if (!visited.contains(vertex)) {
					if (costs.get(vertex) < min) {
						min = costs.get(vertex);
						minVertexCost = vertex;
					}
				}
			}
			visited.add(minVertexCost);
			
			for (GraphAlgorithmObserver<V> o : observerList) {
				o.notifyDijkstraVertexFinished(minVertexCost, costs.get(minVertexCost));
			}
			for (V adjacency : weightedGraph.get(minVertexCost).keySet()) {
				if (!visited.contains(adjacency)) {
					int total = costs.get(minVertexCost) + weightedGraph.get(minVertexCost).get(adjacency);
					if (total < costs.get(adjacency)) {
						costs.put(adjacency, total);
						predecessor.put(adjacency, minVertexCost);
					}
				}
			}
		}

		ArrayList<V> minPath = new ArrayList<V>();
		V endVertex = end;
		while (!endVertex.equals(start)) {
			minPath.add(endVertex);
			endVertex = predecessor.get(endVertex);
		}
		minPath.add(start);
		Collections.reverse(minPath);
		for (GraphAlgorithmObserver<V> o : observerList) {
			o.notifyDijkstraIsOver(minPath);
		}

	}

}
