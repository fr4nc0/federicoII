package binaryTrees;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class JunitTest_RandomNeighborNearest {

	/*
	 * these tests compare the results of the 'classic' (nearestQuery)
	 * 	vs. 'classic' nearest neighbor queries with ending node (nearestQueryWithEndingNode)
	 *  vs. 'random' nearest neighbor queries without ending node (randomNearestQueryWithoutEndingNode)
	 *  vs. 'random' nearest neighbor queries with ending node (randomNearestQuery)
	 */
	
	@Test
	public void test() {
		
		int bucketSizeMin = 2;
		int bucketSizeMax = 4;
		int numPuntiMin   = 512;
		int numPuntiMax   = 2048;
		
		for ( int bucketSize = bucketSizeMin; bucketSize < bucketSizeMax; bucketSize ++) {
			for ( int numPunti = numPuntiMin; numPunti < numPuntiMax; numPunti++ ) {

				BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);	
				}

				int k = 5;
				
				for ( int queryPoint = 0; queryPoint < numPunti; queryPoint++) {

					//System.err.println("----------- num. punti:" + numPunti + " q:" + queryPoint + " bucket size: " + bucketSize +" ----------");
					
					Result result = tree.nearestQuery(queryPoint, k);
					//Result result2 = tree.nearestQueryWithEndingNode(queryPoint, k);
					//Result result2 = tree.randomNearestQuery(queryPoint, k);
					Result result2 = tree.randomNearestQueryWithoutEndingNode(queryPoint, k);
					
					Integer[] tmp  = result.getPoints();
					Integer[] tmp2 = result2.getPoints();
					Arrays.sort(tmp);
					Arrays.sort(tmp2);
					
					//System.out.println(result);
					//System.out.println(result2);
					
					assertArrayEquals(tmp, tmp2);
				}
			}					
		}	
	}

}
