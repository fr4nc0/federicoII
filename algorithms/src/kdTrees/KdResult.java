package kdTrees;

import java.util.PriorityQueue;

public class KdResult {

	private int 					limit;
	private PriorityQueue<Point> 	queue;
	private Distance 				distance;

	
	@Override
	public String toString() {
		return String.valueOf( queue );
	}

	public KdResult(Point queryPoint, int limit, Distance distance) {

		this.limit = limit;
		KdResultComparator comparator = new KdResultComparator(queryPoint, distance);
		this.queue = new PriorityQueue<Point>(1, comparator);
	}
	
	public void add (Point[] values) {
		for ( int i = 0; i < values.length; i++ ) {
			add(values[i]);
		}
		
	}
	
	public void add (Point value) {
		this.queue.add(value);
		if (this.queue.size() > limit) {
			//Retrieves and removes the head of this queue, or returns null if this queue is empty.
			this.queue.poll();
		}			
	}

	public Point getFarthest()  {
		//Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
		return this.queue.peek();
	}
	
	public Point[] getPoints () {
		return this.queue.toArray(new Point[0]);
	}
	
	public boolean isFull() {
		if ( queue.size() == limit ) {
			return true;
		} else {
			return false;
		}
	}
}
