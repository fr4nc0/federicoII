package binaryTrees;

import java.util.Comparator;

public class ResultComparator implements Comparator<Integer> {

	private int queryPoint;
	
	
	public ResultComparator(int queryPoint) {
		super();
		this.queryPoint = queryPoint;
	}


	@Override
	public int compare(Integer first, Integer second) {
		/*
		 *  This method returns zero if the objects are equal. 
		 *  It returns a positive value if obj1 is greater than obj2. 
		 *  Otherwise, a negative value is returned
		 */
		/*
		int firstDist  = Distance.oneDimension(first,  this.queryPoint);
		int secondDist = Distance.oneDimension(second, this.queryPoint);
		return (firstDist - secondDist);
		*/
		return Distance.oneDimension(first, this.queryPoint) < Distance.oneDimension(second, this.queryPoint) ? 1 : -1;
	}
}
