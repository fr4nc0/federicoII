package kdTrees;

import static org.junit.Assert.*;
import binaryTrees.BucketNode.Side;

import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

public class JunitTest_RandomQueryCorrectenss {

	private final String none 			= "none";

	@Test
	public void test() {
		
		/*
		 * parametri del kd tree
		 */
		int bucketSize = 0;
		int maxCoordValue   = 5;

		for ( bucketSize = 2; bucketSize < 3; bucketSize ++) {
			for ( maxCoordValue = 5; maxCoordValue < 6; maxCoordValue++ ) {

				/*
				 * creazione kd tree
				 */
				KdTree tree = KdTree.buildSampleKdTree(bucketSize, maxCoordValue);

				
				/*
				 * parametri della query
				 */
				int maxK = 5;
				int minCoordValue = maxCoordValue -1;
				int XqueryPointCood = 0, YQueryPointCoord = 0;
				
				for ( XqueryPointCood = minCoordValue; XqueryPointCood < maxCoordValue; XqueryPointCood++) {
					for ( YQueryPointCoord = minCoordValue; YQueryPointCoord < maxCoordValue; YQueryPointCoord++) {

						/*
						 * costruzione della query
						 */
						Point queryPoint = new Point(XqueryPointCood, YQueryPointCoord);
						

						/*
						 * manca: ciclo su tutti i nodi come random node
						 */
						for ( int k = 1; k <= maxK; k++) {
							
							System.out.println(" query: k=" + k + " query point=" + queryPoint);
							/*
							 * esecuzione delle query
							 */
							KdResult result = tree.nearestQuery(queryPoint, k);
							Point[] sortedResults = getSortedArray(result);

							// NO MinMax - NO Side
							KdResult resultRandomNOMinMaxNOSide = 
									tree.randomNearestQuery(queryPoint, k, false, false);
							Point[] sortedResultNOMinMaxNOSide = 
									getSortedArray(resultRandomNOMinMaxNOSide);

							// MinMax - NO Side <--
							KdResult resultRandomMinMaxNOSide = 
									tree.randomNearestQuery(queryPoint, k, true, false);
							Point[] sortedResultMinMaxNOSide = 
									getSortedArray(resultRandomMinMaxNOSide);

							// NO MinMax - Side
							KdResult resultRandomNOMinMaxSide = 
									tree.randomNearestQuery(queryPoint, k, false, true);
							Point[] sortedResultNOMinMaxSide = 
									getSortedArray(resultRandomNOMinMaxSide);

							// MinMax - Side
							KdResult resultRandomMinMaxSide = 
									tree.randomNearestQuery(queryPoint, k, true, true);
							Point[] sortedResultMinMaxSide = 
									getSortedArray(resultRandomMinMaxSide);
							
							
							System.out.println(result);
							System.out.println(resultRandomNOMinMaxNOSide);
							System.out.println(resultRandomMinMaxNOSide);
							System.out.println(resultRandomNOMinMaxSide);
							System.out.println(resultRandomMinMaxSide);
							
							
							assertArrayEquals(sortedResults, sortedResultNOMinMaxNOSide);
							assertArrayEquals(sortedResults, sortedResultMinMaxNOSide);
							assertArrayEquals(sortedResults, sortedResultNOMinMaxSide);
							assertArrayEquals(sortedResults, sortedResultMinMaxSide);
	
						}
					}
				}
			}
		}
	}

	private Point[] getSortedArray(KdResult result) {

		Point[] res = result.getPoints();

		Arrays.sort(res, new Comparator<Point>() {

			public int compare(Point point1, Point point2) {
				Double x1 = point1.get(0);
				Double x2 = point2.get(0);

				if ( ! x1.equals(x2) ) {

					// sort by x-coord only
					return x1.compareTo(x2);

				} else {

					// x-coord are equals the sort by y-coord
					Double y1 = point1.get(1);
					Double y2 = point2.get(1);

					return y1.compareTo(y2);
				}
			}
		});

		return res;
	}
}
