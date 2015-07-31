package kdTrees;

import java.util.Comparator;

public class KdResultComparator implements Comparator<Point> {

	private Point 		queryPoint;
	private Distance	distance;
	
	
	public KdResultComparator(Point queryPoint, Distance distance) {
		this.distance = distance;
		this.queryPoint = queryPoint;
	}


	@Override
	public int compare(Point first, Point second) {
		/*
		 *  note: if the distances are equal the second one wins
		 */
		return distance.get(first, this.queryPoint) < distance.get(second, this.queryPoint) ? 1 : -1;
	}
}
