package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Abstract class to represent the Graph ADT. It is assumed that every vertex
 * contains some data of type T, which serves as the identity of that node and
 * provides access to it.
 * 
 * @author Nate Chenette
 *
 * @param <T>
 */
public abstract class Graph<T> {

	/**
	 * Returns the number of vertices in the graph.
	 * 
	 * @return
	 */
	public abstract int size();

	/**
	 * Returns the number of edges in the graph.
	 * 
	 * @return
	 */
	public abstract int numEdges();

	/**
	 * Adds a directed edge from the vertex containing "from" to the vertex
	 * containing "to".
	 * 
	 * @param from
	 * @param to
	 * @return true if the add is successful, false if the edge is already in the
	 *         graph.
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public abstract boolean addEdge(T from, T to);

	/**
	 * Determines whether a vertex is in the graph.
	 * 
	 * @param key
	 * @return true if the graph has a vertex containing key.
	 */
	public abstract boolean hasVertex(T key);

	/**
	 * Determines whether an edge is in the graph.
	 * 
	 * @param from
	 * @param to
	 * @return true if the directed edge (from, to) is in the graph, otherwise
	 *         false.
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public abstract boolean hasEdge(T from, T to) throws NoSuchElementException;

	/**
	 * Removes an edge from the graph.
	 * 
	 * @param from
	 * @param to
	 * @return true if the remove is successful, false if the edge is not in the
	 *         graph.
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public abstract boolean removeEdge(T from, T to) throws NoSuchElementException;

	/**
	 * Computes out-degree of a vertex.
	 * 
	 * @param key
	 * @return the number of successors of the vertex containing key
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract int outDegree(T key) throws NoSuchElementException;

	/**
	 * Computes in-degree of a vertex.
	 * 
	 * @param key
	 * @return the number of predecessors of the vertex containing key
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract int inDegree(T key) throws NoSuchElementException;

	/**
	 * Returns the Set of vertex keys in the graph.
	 * 
	 * @return
	 */
	public abstract Set<T> keySet();

	/**
	 * Returns a Set of keys that are successors of the given key.
	 * 
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Set<T> successorSet(T key) throws NoSuchElementException;

	/**
	 * Returns a Set of keys that are predecessors of the given key.
	 * 
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Set<T> predecessorSet(T key) throws NoSuchElementException;

	/**
	 * Returns an Iterator that traverses the keys who are successors of the given
	 * key.
	 * 
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Iterator<T> successorIterator(T key) throws NoSuchElementException;

	/**
	 * Returns an Iterator that traverses the keys who are successors of the given
	 * key.
	 * 
	 * @param key
	 * @return
	 * @throws NoSuchElementException if the key is not found in the graph
	 */
	public abstract Iterator<T> predecessorIterator(T key) throws NoSuchElementException;

	/**
	 * Finds the strongly-connected component of the provided key.
	 * 
	 * @param key
	 * @return a set containing all data in the strongly connected component of the
	 *         vertex containing key
	 * @throws NoSuchElementException if the key is not found in the graph
	 * 
	 *                                store succ and pred as sets!!! also still need
	 *                                visited
	 */
	public Set<T> stronglyConnectedComponent(T key) throws NoSuchElementException {
		Set<T> successors = new HashSet<T>();
		Set<T> predecessors = new HashSet<>();
		Stack<T> searchForward = new Stack<T>();
		Stack<T> searchBackward = new Stack<T>();
		HashSet<T> visited = new HashSet<T>();

		searchForward.push(key);
		searchBackward.push(key);
		while (!searchForward.isEmpty()) {
			T forward = searchForward.pop();
			visited.add(forward);
			for (T fKey : successorSet(forward)) {
				if (!visited.contains(fKey)) {
					searchForward.push(fKey);
				}
			}

			successors.add(forward);
		}
		visited.clear();
		while (!searchBackward.isEmpty()) {
			T backward = searchBackward.pop();
			visited.add(backward);

			for (T bKey : predecessorSet(backward)) {
				if (!visited.contains(bKey)) {
					searchBackward.push(bKey);
				}
			}
			predecessors.add(backward);

		}
		successors.retainAll(predecessors);
		return successors;
	}

	/**
	 * Searches for the shortest path between start and end points in the graph.
	 * 
	 * @param start
	 * @param end
	 * @return a list of data, starting with start and ending with end, that gives
	 *         the path through the graph, or null if no such path is found.
	 * @throws NoSuchElementException if either key is not found in the graph
	 */
	public List<T> shortestPath(T startLabel, T endLabel) throws NoSuchElementException {
		if (!hasVertex(startLabel) || !hasVertex(endLabel)) {
			throw new NoSuchElementException();
		}
		LinkedList<LinkedList<T>> paths = new LinkedList<LinkedList<T>>();
		LinkedList<T> best = new LinkedList<T>();
		Set<T> visited = new HashSet<T>();
		int bestSize = -1;

		LinkedList<T> list = new LinkedList<T>();
		list.add(startLabel);
		paths.add(list);

		if (startLabel.equals(endLabel)) {
			return list;
		}

		while (!paths.isEmpty()) {
			LinkedList<T> next = paths.remove();

			if (next.size() > bestSize && bestSize != -1) {
				continue;
			}

			for (T nKey : successorSet(next.getLast())) {
				if (!visited.contains(nKey)) {
					visited.add(nKey);
					LinkedList<T> newList = new LinkedList<T>(next);
					newList.add(nKey);
					paths.add(newList);

					if (nKey.equals(endLabel)) {
						if (bestSize == -1) {
							bestSize = newList.size();
							best = newList;
						} else if (newList.size() < bestSize) {
							bestSize = newList.size();
							best = newList;
						}
					}
				}
			}
		}
		if (best.isEmpty()) {
			return null;
		}
		return best;
	}

	public List<T> slPath() {
		List<T> longest = new LinkedList<T>();
		Set<T> keys = keySet();
		LinkedList<T> keysAsList = new LinkedList<T>(keys);

		int outDeg = 0;
		T highestOutDeg = null;

		for (T key : keys) {
			if (outDegree(key) > outDeg) {
				highestOutDeg = key;
			}
		}

				
		int n = 25;
		Thread[] threads = new Thread[n];
		for (int i = 0; i < n; i++) {
			LinkedList<T> sublist = new LinkedList<T>(keysAsList.subList(i * (size() / n), (i + 1) * (size() / n)));
			RunThread thread = new RunThread(sublist, highestOutDeg);
			threads[i] = thread;
			thread.start();
		}
		
		for (int i = 0; i < n; i++) {
			System.out.println("trying to join threads");
			try {
				threads[i].join(10000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return longest;
	}

	class RunThread extends Thread {
		LinkedList<T> keys;
		List<T> longest = new LinkedList<T>();
		T from;
		public RunThread(LinkedList<T> keys, T from) {
			this.keys = keys;
			this.from = from;
		}

		@Override
		public void run() {
			System.out.println("running thread with size: " + keys.size());
			
			int t = keys.size();
			for (int i = 0; i < t; i++) {
				T k = keys.remove();
				List<T> path = shortestPath(from, k);
				if (path == null || path.size() <= 1) {
					continue;
				} else {
					if (path.size() > longest.size()) {
						longest = path;
						System.out.println(longest.size() + " from: " + from + " to: " + k);
					}
				}
			}
			
		}
	}
}
