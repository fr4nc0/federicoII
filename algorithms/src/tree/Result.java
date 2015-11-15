package tree;

import java.util.List;
import java.util.PriorityQueue;

public class Result<T extends Comparable<T>> {

	private int 					limit;
	private PriorityQueue<T> 		queue;
	private boolean 				upFromLeft;
	private boolean 				upFromRight;
	private Node<T>					endingNode;

	
	@Override
	public String toString() {
		return String.valueOf( queue );
	}

	public Result(T queryPoint, int limit, Distance<T> distance) {

		this.limit 					= limit;
		ResultComparator<T> comparator = new ResultComparator<T>(queryPoint, distance);
		this.queue 					= new PriorityQueue<T>(1, comparator);
		this.upFromLeft				= false;
		this.upFromRight 			= false;
		this.endingNode				= null;
	}
	
	public void add (List<T> values) {
		for ( int i = 0; i < values.size(); i++ ) {
			add(values.get(i));
		}
	}
	
	public void add (T value) {
		this.queue.add(value);
		if (this.queue.size() > limit) {
			//Retrieves and removes the head of this queue, or returns null if this queue is empty.
			this.queue.poll();
		}			
	}

	public T getFarthest()  {
		//Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
		return this.queue.peek();
	}
	
	@SuppressWarnings("unchecked")
	public Object[] getPoints () {
		//return this.queue.toArray(new Integer[0]);
		//return this.queue.toArray( (T[]) new Object[0]);
		Object[] tmp = this.queue.toArray( new Object[0]);
		
		//T[] res = (T[]) tmp;
		//return res;
		return tmp;
	}
	
	public boolean isFull() {
		if ( queue.size() == limit ) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isUpFromLeft() {
		return upFromLeft;
	}

	public void setUpFromLeft(boolean upFromLeft) {
		this.upFromLeft = upFromLeft;
	}

	public boolean isUpFromRight() {
		return upFromRight;
	}

	public void setUpFromRight(boolean upFromRight) {
		this.upFromRight = upFromRight;
	}

	public Node<T> getEndingNode() {
		return endingNode;
	}

	public void setEndingNode(Node<T> endingNode) {
		this.endingNode = endingNode;
	}
	
}
