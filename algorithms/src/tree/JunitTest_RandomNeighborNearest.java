package tree;

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
		int numPuntiMax   = 1024;
		
		Distance<Integer> distance = new IntegerDistance();
		
		for ( int bucketSize = bucketSizeMin; bucketSize < bucketSizeMax; bucketSize ++) {
			for ( int numPunti = numPuntiMin; numPunti < numPuntiMax; numPunti++ ) {

				BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);	
				}

				int k = 5;
				
				for ( int queryPoint = 0; queryPoint < numPunti; queryPoint++) {

					System.err.println("----------- num. punti:" + numPunti + " q:" + queryPoint + " bucket size: " + bucketSize +" ----------");
					
					Result<Integer> result = tree.nearestQuery(queryPoint, k);
					//Result<Integer> result2 = tree.nearestQueryWithEndingNode(queryPoint, k);
					Result<Integer> result2 = tree.randomNearestQuery(queryPoint, k);
					//Result<Integer> result2 = tree.randomNearestQueryWithoutEndingNode(queryPoint, k);
					
					Object[] tmp = result.getPoints();
					Integer[] firstResult = new Integer[tmp.length];
					for(int i = 0; i < firstResult.length; i++){
						firstResult[i] = (Integer) tmp[i];
					}
					Arrays.sort( firstResult );
					
					Object[] tmp2 = result2.getPoints();
					Integer[] secondResult = new Integer[tmp2.length];
					for(int i = 0; i < secondResult.length; i++){
						secondResult[i] = (Integer) tmp2[i];
					}
					
					Arrays.sort( secondResult );
					
					assertArrayEquals(firstResult, secondResult);
				}
			}					
		}	
	}

}
