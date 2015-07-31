package kdTrees;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class JUnit_Test_KdFindStartingNode {

	@Test
	public void test() {
		int bucketSize = 3;
		int numPunti   = 5;
		KdTree tree = new KdTree(bucketSize, new EuclideanDistance());

		Point[] points = new Point[numPunti * numPunti];

		for (int i = 0; i < numPunti; i++) {
			for (int j = 0; j < numPunti; j++) {
				Point p = new Point( (double)i, (double)j);
				points[j + numPunti*i] = p;
			}		
		}

		tree.bulkLoad(points, bucketSize);
		
		Point queryPoint = new Point(1, 1);
		
		KdNode randomNode = getNodeByID((long)4, tree.getAllNodes());
		
		KdNode start = tree.findStartingNode(queryPoint, randomNode);
		System.out.println();
	}

	private KdNode getNodeByID(Long i, List<KdNode> allNodes) {

		for ( KdNode n : allNodes ) {
			if ( n.getID().equals(i) ) {
				return n;
			}
		}
		return null;
	}

}
