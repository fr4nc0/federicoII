package binaryTrees;

import static org.junit.Assert.*;
import binaryTrees.BucketNode.Side;

import java.util.Arrays;

import org.junit.Test;

public class JunitTest_RandomNeighborNearest2 {

	private final String none 			= "none";
	
	@Test
	public void test() {
		int bucketSize = 0;
		int numPunti   = 0;
		
		for ( bucketSize = 3; bucketSize < 4; bucketSize ++) {
			for ( numPunti = 32; numPunti < 33; numPunti++ ) {

				BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);	
				}

				int k = 3;
				int queryPoint   = 0;
				
				for ( queryPoint = 0; queryPoint < numPunti; queryPoint++) {

					Result result = tree.nearestQuery(queryPoint, k);
					Integer[] tmp  = result.getPoints();
					Arrays.sort(tmp);
					
					for ( BucketNode node : tree.getAllNodes() ) {
						
						Result result2 = new Result(queryPoint, k);
						tree.randomNearestNeighbor(node, queryPoint, k, result2, none);
						Integer[] tmp2 = result2.getPoints();
						Arrays.sort(tmp2);
						assertArrayEquals(tmp, tmp2);	
					}
				}
			}					
		}	
	}

}
