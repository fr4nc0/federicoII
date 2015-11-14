package binaryTrees;

import java.util.PriorityQueue;

public class Result {

	private int 					limit;
	private PriorityQueue<Integer> 	queue;
	private boolean 				upFromLeft;
	private boolean 				upFromRight;
	private Integer					endingNode;

	
	@Override
	public String toString() {
		return String.valueOf( queue );
	}

	public Result(int queryPoint, int limit) {

		this.limit 					= limit;
		ResultComparator comparator = new ResultComparator(queryPoint);
		this.queue = new PriorityQueue<Integer>(1, comparator);
		this.upFromLeft				= false;
		this.upFromRight 			= false;
		this.endingNode				= null;
	}
	
	public void add (int[] values) {
		for ( int i = 0; i < values.length; i++ ) {
			add(values[i]);
		}
		
	}
	
	public void add (int value) {
		this.queue.add(value);
		if (this.queue.size() > limit) {
			//Retrieves and removes the head of this queue, or returns null if this queue is empty.
			this.queue.poll();
		}			
	}

	public int getFarthest()  {
		//Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
		return this.queue.peek();
	}
	
	public Integer[] getPoints () {
		return this.queue.toArray(new Integer[0]);
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

	public Integer getEndingNode() {
		return endingNode;
	}

	public void setEndingNode(Integer endingNode) {
		this.endingNode = endingNode;
	}
	
	
	
}
