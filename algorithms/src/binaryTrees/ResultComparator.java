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
		 *  note: if the distances are equal the second one wins
		 */
		return Distance.oneDimension(first, this.queryPoint) < Distance.oneDimension(second, this.queryPoint) ? 1 : -1;
	}
}
