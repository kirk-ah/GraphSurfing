package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

public class AdjacencyListGraph<T> extends Graph<T> {
	Map<T, Vertex> keyToVertex;

	private class Vertex {
		T key;
		List<Vertex> successors;
		List<Vertex> predecessors;

		Vertex(T key) {
			this.key = key;
			this.successors = new ArrayList<Vertex>();
			this.predecessors = new ArrayList<Vertex>();
		}
	}

	AdjacencyListGraph(Set<T> keys) {
		this.keyToVertex = new HashMap<T, Vertex>();
		for (T key : keys) {
			Vertex v = new Vertex(key);
			this.keyToVertex.put(key, v);
		}
	}

	@Override
	public int size() {
		return keyToVertex.size();
	}

	@Override
	public int numEdges() {
		int edges = 0;
		for (T key : this.keyToVertex.keySet()) {
			edges += this.keyToVertex.get(key).successors.size();
		}
		return edges;
	}

	@Override
	public boolean addEdge(T from, T to) {
		if (this.keyToVertex.get(from) == null || this.keyToVertex.get(to) == null) {
			throw new NoSuchElementException();
		}

		if (this.keyToVertex.get(from).successors.contains(keyToVertex.get(to))
				|| this.keyToVertex.get(to).predecessors.contains(keyToVertex.get(from))) {
			return false;
		}

		this.keyToVertex.get(from).successors.add(keyToVertex.get(to));
		this.keyToVertex.get(to).predecessors.add(keyToVertex.get(from));

		return true;
	}

	@Override
	public boolean hasVertex(T key) {
		return this.keyToVertex.containsKey(key);
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		if (this.keyToVertex.get(from) == null || this.keyToVertex.get(to) == null) {
			throw new NoSuchElementException();
		}

		return this.keyToVertex.get(from).successors.contains(keyToVertex.get(to))
				&& this.keyToVertex.get(to).predecessors.contains(keyToVertex.get(from));
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		if (this.keyToVertex.get(from) == null || this.keyToVertex.get(to) == null) {
			throw new NoSuchElementException();
		}

		if (!this.keyToVertex.get(from).successors.contains(keyToVertex.get(to))
				&& !this.keyToVertex.get(to).predecessors.contains(keyToVertex.get(from))) {
			return false;
		}

		this.keyToVertex.get(from).successors.remove(keyToVertex.get(to));
		this.keyToVertex.get(to).predecessors.remove(keyToVertex.get(from));
		return true;
	}

	@Override
	public int outDegree(T key) {
		if (this.keyToVertex.get(key) == null) {
			throw new NoSuchElementException();
		}

		return this.keyToVertex.get(key).successors.size();
	}

	@Override
	public int inDegree(T key) {
		if (this.keyToVertex.get(key) == null) {
			throw new NoSuchElementException();
		}

		return this.keyToVertex.get(key).predecessors.size();
	}

	@Override
	public Set<T> keySet() {
		return this.keyToVertex.keySet();
	}

	@Override
	public Set<T> successorSet(T key) {
		Set<T> set = new HashSet<T>();
		if (this.keyToVertex.get(key) == null) {
			throw new NoSuchElementException();
		}
		
		int size = this.keyToVertex.get(key).successors.size();
		List<AdjacencyListGraph<T>.Vertex> successors = this.keyToVertex.get(key).successors;

		for (int i = 0; i < size; i++) {
			set.add(successors.get(i).key);
		}
		return set;
	}

	@Override
	public Set<T> predecessorSet(T key) {
		Set<T> set = new HashSet<T>();
		if (this.keyToVertex.get(key) == null) {
			throw new NoSuchElementException();
		}

		for (int i = 0; i < this.keyToVertex.get(key).predecessors.size(); i++) {
			set.add(this.keyToVertex.get(key).predecessors.get(i).key);
		}
		return set;
	}

	@Override
	public Iterator<T> successorIterator(T key) {
		if (this.keyToVertex.get(key) == null) {
			throw new NoSuchElementException();
		}
		return new SuccessorIterator(this, key);
	}

	class SuccessorIterator implements Iterator<T> {
		AdjacencyListGraph<T> graph;
		T key;
		int successorsSize, numSuccessorsFound;
		int lastSuccessorIndex;

		public SuccessorIterator(AdjacencyListGraph<T> adjacencyListGraph, T key) {
			this.graph = adjacencyListGraph;
			this.key = key;
			this.successorsSize = this.graph.keyToVertex.get(key).successors.size();
			this.numSuccessorsFound = 0;
			this.lastSuccessorIndex = -1;
		}

		@Override
		public boolean hasNext() {
			return numSuccessorsFound < successorsSize;
		}

		@Override
		public T next() {
			this.lastSuccessorIndex++;
			this.numSuccessorsFound++;
			return this.graph.keyToVertex.get(key).successors.get(lastSuccessorIndex).key;
		}
	}

	@Override
	public Iterator<T> predecessorIterator(T key) {
		if (this.keyToVertex.get(key) == null) {
			throw new NoSuchElementException();
		}
		return new PredecessorIterator(this, key);
	}

	class PredecessorIterator implements Iterator<T> {
		AdjacencyListGraph<T> graph;
		T key;
		int predecessorsSize, numPredecessorsFound;
		int lastPredecessorIndex;

		public PredecessorIterator(AdjacencyListGraph<T> adjacencyListGraph, T key) {
			this.graph = adjacencyListGraph;
			this.key = key;
			this.predecessorsSize = this.graph.keyToVertex.get(key).predecessors.size();
			this.numPredecessorsFound = 0;
			this.lastPredecessorIndex = -1;
		}

		@Override
		public boolean hasNext() {
			return numPredecessorsFound < predecessorsSize;
		}

		@Override
		public T next() {
			this.lastPredecessorIndex++;
			this.numPredecessorsFound++;
			return this.graph.keyToVertex.get(key).predecessors.get(lastPredecessorIndex).key;
		}
	}
}
