package binaryTrees;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class JunitTest_RandomNeighborNearest {

	@Test
	public void test() {
		int bucketSize = 0;
		int numPunti   = 0;
		
		for ( bucketSize = 3; bucketSize < 4; bucketSize ++) {
			for ( numPunti = 512; numPunti < 1024; numPunti++ ) {

				BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);	
				}

				int k = 3;
				int queryPoint   = 0;
				
				for ( queryPoint = 0; queryPoint < numPunti; queryPoint++) {

					Result result = tree.nearestQuery(queryPoint, k);
					Result result2 = tree.randomNearestQuery(queryPoint, k);
					Integer[] tmp  = result.getPoints();
					Integer[] tmp2 = result2.getPoints();
					Arrays.sort(tmp);
					Arrays.sort(tmp2);
					assertArrayEquals(tmp, tmp2);
				}
			}					
		}	
	}

}
