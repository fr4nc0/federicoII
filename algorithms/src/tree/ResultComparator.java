package tree;

import java.util.Comparator;

public class ResultComparator<T extends Comparable<T>> implements Comparator<T> {

	private T 				queryPoint;
	private Distance<T> 	distance;
	
	
	public ResultComparator(T queryPoint, Distance<T> distance) {
		super();
		this.queryPoint = queryPoint;
		this.distance	= distance;
	}


	@Override
	public int compare(T first, T second) {

		/*
		 *  This method returns zero if the objects are equal. 
		 *  It returns a positive value if obj1 is greater than obj2. 
		 *  Otherwise, a negative value is returned
		 */
		Double firstDistance 	= distance.distance(first, this.queryPoint);
		Double secondDistance 	= distance.distance(second, this.queryPoint);
		
		// occhio al "-" !!
		return -firstDistance.compareTo(secondDistance);
	}


	/*
	@Override
	public int compare(Integer first, Integer second) {
		return Distance.oneDimension(first, this.queryPoint) < Distance.oneDimension(second, this.queryPoint) ? 1 : -1;
	}
	*/
}
