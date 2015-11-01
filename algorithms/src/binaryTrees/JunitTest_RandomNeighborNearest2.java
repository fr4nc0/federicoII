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

		for ( numPunti = 16; numPunti <= 1024;  ) {
			numPunti = numPunti * 2;

			for ( bucketSize = 1; bucketSize <= 8; ) {
				bucketSize = bucketSize * 2;

				BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);	
				}

				int kMax = (int) Math.ceil(numPunti/bucketSize);

				for (int k = 1; k <= kMax; ) {
					k = (k * 2) + 1; //dispari

					System.out.println("n = " + numPunti + " b = " + bucketSize + " k = " + k);
					
					int queryPoint   = 0;
					for ( queryPoint = 0; queryPoint < numPunti; queryPoint++) {

						//System.out.println("\tquery point: " + queryPoint);
						
						//Result result = tree.nearestQuery(queryPoint, k);
						//Integer[] tmp  = result.getPoints();
						//Arrays.sort(tmp);

						for ( BucketNode node : tree.getAllNodes() ) {

							//System.out.println("\t\tstart node: " + node.getLabelNode());
							
							Result result2 = new Result(queryPoint, k);
							tree.randomNearestNeighbor(node, queryPoint, k, result2, none);
							//Integer[] tmp2 = result2.getPoints();
							//Arrays.sort(tmp2);
							//assertArrayEquals(tmp, tmp2);	
						}
					}
				}
			}					
		}	
	}

}
