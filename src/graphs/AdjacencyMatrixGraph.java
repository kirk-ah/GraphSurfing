package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class AdjacencyMatrixGraph<T> extends Graph<T> {
	Map<T, Integer> keyToIndex;
	List<T> indexToKey;
	int[][] matrix;

	AdjacencyMatrixGraph(Set<T> keys) {
		int size = keys.size();
		this.keyToIndex = new HashMap<T, Integer>();
		this.indexToKey = new ArrayList<T>();
		this.matrix = new int[size][size];
		// need to populate keyToIndex and indexToKey with info from keys
		int index = 0;
		for (T key : keys) {
			this.keyToIndex.put(key, index);
			this.indexToKey.add(key);
			index++;
		}
	}

	@Override
	public int size() {
		return this.matrix.length;
	}

	@Override
	public int numEdges() {
		int edges = 0;

		for (int i = 0; i < this.matrix.length; i++) {
			for (int j = 0; j < this.matrix[i].length; j++) {
				if (this.matrix[i][j] == 1) {
					edges++;
				}
			}
		}
		return edges;
	}

	@Override
	public boolean addEdge(T from, T to) throws NoSuchElementException {
		if (this.keyToIndex.get(from) == null || this.keyToIndex.get(to) == null) {
			throw new NoSuchElementException();
		}
		if (this.matrix[this.keyToIndex.get(from)][this.keyToIndex.get(to)] == 1) {
			return false;
		}
		this.matrix[this.keyToIndex.get(from)][this.keyToIndex.get(to)] = 1;
		return true;
	}

	@Override
	public boolean hasVertex(T key) {
		return this.keyToIndex.get(key) != null;
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		if (this.keyToIndex.get(from) == null || this.keyToIndex.get(to) == null) {
			throw new NoSuchElementException();
		}
		return this.matrix[this.keyToIndex.get(from)][this.keyToIndex.get(to)] == 1;
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		if (this.keyToIndex.get(from) == null || this.keyToIndex.get(to) == null) {
			throw new NoSuchElementException();
		}
		if (this.matrix[this.keyToIndex.get(from)][this.keyToIndex.get(to)] == 0) {
			return false;
		}
		this.matrix[this.keyToIndex.get(from)][this.keyToIndex.get(to)] = 0;
		return true;
	}

	@Override
	public int outDegree(T key) {
		int outDegree = 0;

		if (this.keyToIndex.get(key) == null) {
			throw new NoSuchElementException();
		}
		int row = this.keyToIndex.get(key);
		for (int i = 0; i < this.matrix[row].length; i++) {
			if (this.matrix[row][i] == 1) {
				outDegree++;
			}
		}
		return outDegree;
	}

	@Override
	public int inDegree(T key) {
		int inDegree = 0;

		if (this.keyToIndex.get(key) == null) {
			throw new NoSuchElementException();
		}
		for (int i = 0; i < this.matrix[this.keyToIndex.get(key)].length; i++) {
			if (this.matrix[i][this.keyToIndex.get(key)] == 1) {
				inDegree++;
			}
		}
		return inDegree;
	}

	@Override
	public Set<T> keySet() {
		return keyToIndex.keySet();
	}

	@Override
	public Set<T> successorSet(T key) {

		Set<T> set = new HashSet<T>();

		if (this.keyToIndex.get(key) == null) {
			throw new NoSuchElementException();
		}

		for (int i = 0; i < this.matrix[this.keyToIndex.get(key)].length; i++) {
			if (this.matrix[this.keyToIndex.get(key)][i] == 1) {
				set.add(indexToKey.get(i));
			}
		}

		return set;
	}

	@Override
	public Set<T> predecessorSet(T key) {
		Set<T> set = new HashSet<T>();

		if (this.keyToIndex.get(key) == null) {
			throw new NoSuchElementException();
		}

		for (int i = 0; i < this.matrix[this.keyToIndex.get(key)].length; i++) {
			if (this.matrix[i][this.keyToIndex.get(key)] == 1) {
				set.add(indexToKey.get(i));
			}
		}
		return set;
	}

	@Override
	public Iterator<T> successorIterator(T key) {
		// TODO Auto-generated method stub
		if (this.keyToIndex.get(key) == null) {
			throw new NoSuchElementException();
		}
		return new SuccessorIterator(this, key);
	}

	class SuccessorIterator implements Iterator<T> {
		AdjacencyMatrixGraph<T> matrixGraph;
		T key;
		int[] row;
		int lastSuccessorIndex;

		public SuccessorIterator(AdjacencyMatrixGraph<T> adjacencyMatrixGraph, T key) {
			this.matrixGraph = adjacencyMatrixGraph;
			this.key = key;
			this.row = matrix[matrixGraph.keyToIndex.get(key)];
			this.lastSuccessorIndex = -1;

		}

		@Override
		public boolean hasNext() {
			for (int i = lastSuccessorIndex + 1; i < this.row.length; i++) {
				if (this.row[i] == 1) {
					return true;
				}
			}
			return false;
		}

		@Override
		public T next() { // Track where in hashset you are.

			for (int i = 0; i < this.row.length; i++) {
				if (this.row[i] == 1 && i > lastSuccessorIndex) {
					this.lastSuccessorIndex = i;
					return this.matrixGraph.indexToKey.get(i);
				}
			}

			throw new NoSuchElementException();
		}

	}

	@Override
	public Iterator<T> predecessorIterator(T key) {
		// TODO Auto-generated method stub
		if (this.keyToIndex.get(key) == null) {
			throw new NoSuchElementException();
		}
		return new PredecessorIterator(this, key);
	}

	class PredecessorIterator implements Iterator<T> {
		AdjacencyMatrixGraph<T> matrixGraph;
		T key;
		int[] column;
		int lastPredecessorIndex;

		public PredecessorIterator(AdjacencyMatrixGraph<T> adjacencyMatrixGraph, T key) {
			this.matrixGraph = adjacencyMatrixGraph;
			this.key = key;
			this.column = matrix[matrixGraph.keyToIndex.get(key)];
			this.lastPredecessorIndex = -1;

		}

		@Override
		public boolean hasNext() {
			for (int i = lastPredecessorIndex + 1; i < this.column.length; i++) {
				if (this.matrixGraph.matrix[i][matrixGraph.keyToIndex.get(key)] == 1) {
					return true;
				}
			}
			return false;
		}

		@Override
		public T next() {
			for (int i = 0; i < this.column.length; i++) {
				if (this.matrixGraph.matrix[i][matrixGraph.keyToIndex.get(key)] == 1 && i > lastPredecessorIndex) {
					this.lastPredecessorIndex = i;
					return this.matrixGraph.indexToKey.get(i);
				}
			}

			throw new NoSuchElementException();
		}

	}
}
