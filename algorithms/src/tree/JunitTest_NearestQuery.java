package tree;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.Test;

public class JunitTest_NearestQuery {

	/*
	 * these tests check the correctness of the 'classic' nearest neighbor implementation 
	 */
	
	private Distance<Integer> distance = new IntegerDistance();
	
	@Test
	public void test1() {
		
		int numPoints 	= 150;
		int bucketSize 	= 3;
		int queryPoint 	= 2;
		int k 			= 5;
		Integer[] expected = new Integer[]{0, 1, 2, 3, 4};
		
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

	@Test
	public void test2() {
		
		int numPoints 	= 1500;
		int bucketSize 	= 4;
		int queryPoint 	= 50;
		int k 			= 5;
		Integer[] expected = new Integer[]{48, 49, 50, 51, 52};
		
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

	@Test
	public void test3() {
		
		int numPoints 	= 3000;
		int bucketSize 	= 8;
		int queryPoint 	= 22;
		int k 			= 7;
		Integer[] expected = new Integer[]{19, 20, 21, 22, 23, 24, 25};
		
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

	@Test
	public void test4() {
		
		int numPoints 	= 150;
		int bucketSize 	= 3;
		int queryPoint 	= 8;
		int k 			= 5;
		Integer[] expected = new Integer[]{6, 7, 8, 9, 10};
		
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

	@Test
	public void test5() {
		
		int numPoints 	= 15;
		int bucketSize 	= 3;
		int queryPoint 	= 9;
		int k 			= 18;
		Integer[] expected = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
		
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

	@Test
	public void test6() {
		
		int numPoints 	= 5000;
		int bucketSize 	= 30;
		int queryPoint 	= 9;
		int k 			= 1;
		Integer[] expected = new Integer[]{9};
		
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

	@Test
	public void test7() {
		
		int numPoints 	= 5000;
		int bucketSize 	= 1;
		int queryPoint 	= 20;
		int k 			= 1;
		Integer[] expected = new Integer[]{20};
		
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

	@Test
	public void test8() {
		
		int numPoints 	= 5000;
		int bucketSize 	= 5;
		int queryPoint 	= 20;
		int k 			= 11;
		Integer[] expected = new Integer[]{15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
										   
		BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSize, distance);

		for (int i = 0; i < numPoints; i++) {
			tree.insert(i);	
		}
		
		Result<Integer> result = tree.nearestQuery(queryPoint, k);
		Object[] tmp = result.getPoints();
		Integer[] actual = new Integer[tmp.length];
		for(int i = 0; i < actual.length; i++){
			actual[i] = (Integer) tmp[i];
		}
		Arrays.sort( actual );
		
		assertArrayEquals(expected, actual);
		tree = null;
	}

}
