package kdTrees;

import static org.junit.Assert.*;

import org.junit.Test;

public class JUnit_Test_KdNearest {

	@Test
	public void test() {

		/*
		 *  10
		 *  9
		 *  8
		 *  7
		 *  6
		 *  5
		 *  4
		 *  3  x  x  x
		 *  2  x  q  x
		 *  1  x  x  x
		 *  0  1  2  3  4  5  6  7  8  9  10  
		 */
		int bucketSize = 3;
		int numPunti   = 10;
		KdTree tree = new KdTree(bucketSize, new EuclideanDistance());

		Point[] points = new Point[numPunti * numPunti];

		for (int i = 0; i < numPunti; i++) {
			for (int j = 0; j < numPunti; j++) {
				Point p = new Point( (double)i, (double)j);
				points[j + numPunti*i] = p;
			}		
		}

		tree.bulkLoad(points, bucketSize);
		Point queryPoint = new Point(2, 2);
		int k = 9;
		KdResult result = tree.nearestQuery(queryPoint, k);
		
		System.out.println();
	}

}
