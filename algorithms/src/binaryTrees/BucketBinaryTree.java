package binaryTrees;

import graphics.Node;
import graphics.Tree;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import binaryTrees.BucketNode.Side;



public class BucketBinaryTree implements Tree{

	/*
	 * it is used only for tests
	 */
	public static List<BucketNode> allNodes;
	public static List<BucketNode> leftSideNodes;
	public static List<BucketNode> rightSideNodes;


	private BucketNode root;
	private int bucketSize;

	private final String none 			= "none";
	private final String leftVisited 	= "leftVisited";
	private final String rightVisited 	= "rightVisited";
	//private final String allVisited 	= "allVisited";

	public Node getRoot() {
		return root;
	}

	public void setRoot(BucketNode root) {
		this.root = root;
	}

	public BucketBinaryTree(int bucketSize) {
		this.bucketSize = bucketSize;
	}

	public void insert(int newValue) {

		if ( root == null) {

			allNodes = new ArrayList<BucketNode>();
			leftSideNodes = new ArrayList<BucketNode>();
			rightSideNodes = new ArrayList<BucketNode>();

			root = new BucketNode(newValue, bucketSize, allNodes);
			root.setRoot(true);

		} else {

			root.setRoot(false);
			root = insert(newValue, root);
			root.setRoot(true);
		}
	}

	private BucketNode insert(int newValue, BucketNode node) {

		if ( node.isLeaf() ) {

			node.add(newValue, this.bucketSize, allNodes, leftSideNodes, rightSideNodes);

		} else {

			if ( newValue < node.getSplitValue() ) {

				node.setLeft( insert(newValue, node.getLeft()) );
				node.setHeight( Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);

				node.setMaxValue(node.getRight().getMaxValue());
				node.setMinValue(node.getLeft().getMinValue());

				if( node.getLeft().getHeight() - node.getRight().getHeight() == 2 ) {

					if( newValue < node.getLeft().getSplitValue() ) {

						node = rotateWithLeftChild( node );

					} else {

						node = doubleWithLeftChild( node );
					}
				}
			} else {

				node.setRight( insert(newValue, node.getRight()) );
				node.setHeight( Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);

				node.setMaxValue(node.getRight().getMaxValue());
				node.setMinValue(node.getLeft().getMinValue());

				if( node.getRight().getHeight() - node.getLeft().getHeight() == 2 ) {
					if( newValue > node.getRight().getSplitValue() ) {

						node = rotateWithRightChild( node );

					} else {

						node = doubleWithRightChild( node );
					}
				}
			}	 
		}
		return node;
	}

	/**
	 * Rotate binary tree node with left child.
	 * For AVL trees, this is a single rotation for case 1.
	 * Update heights, then return new root.
	 */
	private static BucketNode rotateWithLeftChild( BucketNode k2 ){
		/*
		 * 		   k2            k1
		 * 		  /	 \          /  \
		 *       k1   A   =>   B   k2
		 *      /  \              /  \
		 *     B    C            C    A
		 */
		BucketNode k1 = k2.getLeft();
		k1.setParent(k2.getParent());

		// some nodes change side if and only if k2 is the root
		if ( k2.getParent() == null ) {

			// k2 is the root
			k1.setSide(Side.NONE);
			leftSideNodes.remove(k1);			// k1 was a left side node

			k2.setSide(Side.RIGHT);
			rightSideNodes.add(k2);				// now, k2 is a right side node

			changeSideToSubtree(k1.getRight(), Side.RIGHT); // node "C" changed its side
		}

		k2.setLeft(k1.getRight());
		k2.getLeft().setParent(k2);

		k1.setRight(k2);
		k2.setParent(k1);



		k2.setMinValue(k2.getRight().getMinValue());
		k1.setMaxValue(k1.getLeft().getMaxValue());

		k2.setHeight( Math.max(k2.getLeft().getHeight(), k2.getRight().getHeight()) +1 );
		k1.setHeight( Math.max(k1.getLeft().getHeight(), k2.getHeight()));
		return k1;
	}

	/**
	 * Rotate binary tree node with right child.
	 * For AVL trees, this is a single rotation for case 4.
	 * Update heights, then return new root.
	 */
	private static BucketNode rotateWithRightChild( BucketNode k1 ) {
		/*
		 * 		   k2            k1
		 * 		  /	 \          /  \
		 *       k1   A   <=   B   k2
		 *      /  \              /  \
		 *     B    C            C    A
		 */
		BucketNode k2 = k1.getRight();
		k2.setParent(k1.getParent());

		// some nodes change side if and only if k1 is the root
		if ( k1.getParent() == null ) {

			// k1 is the root
			k2.setSide(Side.NONE);
			rightSideNodes.remove(k2);	// it was a right side nodes

			k1.setSide(Side.LEFT);
			leftSideNodes.add(k1);

			changeSideToSubtree(k2.getLeft(), Side.LEFT); // node "C" changed its side
		}

		k1.setRight(k2.getLeft()); 
		k1.getRight().setParent(k1);

		k2.setLeft(k1);
		k1.setParent(k2);

		k1.setMaxValue(k1.getRight().getMaxValue());
		k2.setMinValue(k2.getLeft().getMinValue());

		k1.setHeight( Math.max(k1.getLeft().getHeight(), k1.getRight().getHeight())  + 1); 
		k2.setHeight( Math.max(k2.getRight().getHeight(), k1.getHeight()) + 1); 
		return k2;
	}

	private static void changeSideToSubtree(BucketNode node, Side newSide) {
		if ( node != null )  {
			node.setSide(newSide);
			if ( newSide.equals(Side.LEFT) ) {
				leftSideNodes.add(node);
				rightSideNodes.remove(node);
			} else {
				rightSideNodes.add(node);
				leftSideNodes.remove(node);
			}

			changeSideToSubtree(node.getRight(), newSide);
			changeSideToSubtree(node.getLeft(), newSide);
		}
	}

	/**
	 * Double rotate binary tree node: first left child
	 * with its right child; then node k3 with new left child.
	 * For AVL trees, this is a double rotation for case 2.
	 * Update heights, then return new root.
	 */
	private static BucketNode doubleWithLeftChild( BucketNode k3 )
	{
		k3.setLeft( rotateWithRightChild(k3.getLeft()) );
		return rotateWithLeftChild( k3 );
	}

	/**
	 * Double rotate binary tree node: first right child
	 * with its left child; then node k1 with new right child.
	 * For AVL trees, this is a double rotation for case 3.
	 * Update heights, then return new root.
	 */
	private static BucketNode doubleWithRightChild( BucketNode k1 )
	{
		k1.setRight( rotateWithLeftChild( k1.getRight()) );
		return rotateWithRightChild( k1 );
	}

	private void nearestNeighbor(BucketNode v, int p, int k, Result result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		//System.out.println("nearestNeighbor: " + v + " v.status:" + v.getStatus() + " status:" + status);

		NN(v, p, k, result);
	}

	public void randomNearestNeighbor(BucketNode v, int p, int k, Result result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		//System.out.println("randomNearestNeighbor: " + v + " v.status:" + v.getStatus() + " status:" + status);

		randomNN(v, p, k, result);
	}

	private void randomNN(BucketNode v, int p, int k, Result result) {

		//System.out.println(v + " " + v.getStatus());

		if ( v.isLeaf() ) {

			result.add (v.getBucket());

			if ( mustBeSetParentStatus(v) )  {

				randomNearestNeighbor(v.getParent(), p, k, result, null);
			}
			//return; 

		} else {

			if ( v.getStatus().equals(none) && !(v.isLeaf()) ) {
				if ( p < v.getSplitValue()) {
					v.setStatus(leftVisited); 
					randomNearestNeighbor (v.getLeft(), p, k, result, none);
				} else {
					v.setStatus(rightVisited); 
					randomNearestNeighbor (v.getRight(), p, k, result, none);
				}
			} 
			/*
			 * potrebbe entrare in rightVisited oppure in leftVisited dopo essere uscito dall'if precedente
			 */
			if ( v.getStatus().equals(rightVisited) ) {

				if ( ! (v.getLeft() == null) && secondChildMustBeVisited(v, p, result) ) {
					randomNearestNeighbor (v.getLeft(), p, k, result, none);
				} 

				// do the following even if the left child is not visited 
				if ( mustBeSetParentStatus(v) )  {

					randomNearestNeighbor(v.getParent(), p, k, result, null);
				}
			} 

			if ( v.getStatus().equals(leftVisited) ) {

				if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, p, result) ) {

					randomNearestNeighbor (v.getRight(), p, k, result, none);
				}

				// do the following even if the right child is not visited 
				if ( mustBeSetParentStatus(v) )  {

					randomNearestNeighbor(v.getParent(), p, k, result, null);
				}
			}
		}
		v.setStatus(null);
	}

	private void NN(BucketNode v, int p, int k, Result result) {

		if ( v.isLeaf() ) {

			result.add (v.getBucket());
			return; 

		} else {

			if ( v.getStatus().equals(none) && !(v.isLeaf()) ) {
				if ( p < v.getSplitValue()) {
					v.setStatus(leftVisited); 
					nearestNeighbor (v.getLeft(), p, k, result, none);
				} else {
					v.setStatus(rightVisited); 
					nearestNeighbor (v.getRight(), p, k, result, none);
				}
			} 
			/*
			 * potrebbe entrare in rightVisited oppure in leftVisited dopo essere uscito dall'if precedente
			 */
			if ( v.getStatus().equals(rightVisited) ) {

				if ( ! (v.getLeft() == null) && secondChildMustBeVisited(v, p, result) ) {
					//v.setStatus(allVisited); 
					nearestNeighbor (v.getLeft(), p, k, result, none);
				}
			} 

			if ( v.getStatus().equals(leftVisited) ) {

				if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, p, result) ) {
					//v.setStatus(allVisited); 
					nearestNeighbor (v.getRight(), p, k, result, none);
				}
			}
		}

		// l'elaborazione del nodo corrente è finita
		v.setStatus(null);
	}

	private boolean secondChildMustBeVisited(BucketNode v, int p, Result result) {

		/*
		 * if the result contains less than k point the second child must be always visited
		 */
		if ( ! result.isFull() ) {

			return true;

		} else {
			if ( Distance.oneDimension(p, v.getSplitValue() ) < Distance.oneDimension(p, result.getFarthest()) ) {

				return true; 	//Descend other sibling also
			}

			else {
				return false; 	//Do not descend 
			}
		}
	}

	public static void main( String [ ] args ){

		int bucketSize = 3;
		BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

		for (int i = 1; i < 15; i++) {
			tree.insert(i);	
		}

		int queryPoint = 7;
		int k = 1;

		/*
		Result result = tree.randomNearestQuery(queryPoint, k);
		//Result result = tree.nearestQuery(queryPoint, k);
		Integer[] points = result.getPoints();
		for ( Integer i : points ) {
			System.err.println(i);
		}
		 */
	}

	public Result nearestQuery(int queryPoint, int k) {

		Result result = new Result(queryPoint, k);
		nearestNeighbor(root, queryPoint, k, result, none);

		return result;
	}

	public Result randomNearestQuery(int queryPoint, int k) {

		int randomNodeIndex = 0;
		BucketNode randomNode = null;

		// no side
		//randomNodeIndex = randInt( 0, (allNodes.size() - 1) );
		//randomNode = allNodes.get(randomNodeIndex);
		//while (randomNode.isRoot()) {
		//	randomNode = allNodes.get(randInt(0, allNodes.size()));
		//}

		// side
		if ( queryPoint < root.getSplitValue() ) {
			// choose a node in left subtree
			randomNodeIndex = randInt( 0, (leftSideNodes.size() - 1) );
			randomNode = leftSideNodes.get(randomNodeIndex);

		} else {
			// choose a node in right subtree
			randomNodeIndex = randInt( 0, (rightSideNodes.size() - 1) );
			randomNode = rightSideNodes.get(randomNodeIndex);
		}
		//System.out.println(randomNodeIndex);
		//System.out.println("ramdom: " + randomNode);

		BucketNode startNode = findStartingNode(queryPoint, randomNode);
		//System.out.println("start: " + startNode);
		Result result = new Result(queryPoint, k);
		randomNearestNeighbor(startNode, queryPoint, k, result, none);

		return result;
	}

	public BucketNode findStartingNode(int p, BucketNode n) {

		if (n.isRoot()) {
			// ROOT; 
			return n;

		} else {
			if (n.isLeaf()) {
				// LEAF;
				// Di una foglia di possono calcolare min e max
				if ( n.getMinValue() <= p && p <= n.getMaxValue() ) { 
					return n; 
				} else { 
					return findStartingNode(p, n.getParent());
				}

			} else {
				if ( n.getLeft() != null && n.getRight() != null ) {

					//INTERNAL_WITH_BOTH_CHILDREN;
					int leftSplitValue = n.getLeft().getSplitValue();
					int rightSplitValue = n.getRight().getSplitValue();

					if ( leftSplitValue <= p && p <= rightSplitValue ) { 
						return n;
					} else { 
						return findStartingNode(p, n.getParent());
					}
				} else {
					if ( n.getRight() != null ) {
						//INTERNAL_WITH_ONLY_RIGHT_CHILD;
						return findStartingNode(p, n.getParent());
					} else {
						//INTERNAL_WITH_ONLY_LEFT_CHILD;
						return findStartingNode(p, n.getParent());
					}
				}
			}
		}
	}

	public BucketNode findStartingNodeMinMax(int p, BucketNode n) {

		if (n.isRoot()) {
			// ROOT; 
			return n;

		} else {
			if (n.isLeaf()) {
				// LEAF;
				// Di una foglia di possono calcolare min e max
				if ( n.getMinValue() <= p && p <= n.getMaxValue() ) { 
					return n; 
				} else { 
					return findStartingNode(p, n.getParent());
				}

			} else {
				if ( n.getLeft() != null && n.getRight() != null ) {
					//INTERNAL_WITH_BOTH_CHILDREN;
					if ( n.getMinValue() <= p && p <= n.getMaxValue() ) { 
						return n;
					} else { 
						return findStartingNode(p, n.getParent());
					}
				} else {
					if ( n.getRight() != null ) {
						//INTERNAL_WITH_ONLY_RIGHT_CHILD;
						if ( n.getRight().getMinValue() <= p && p <= n.getRight().getMaxValue() ) { 
							return n.getRight();
						} else {
							return findStartingNode(p, n.getParent());
						}

					} else {
						//INTERNAL_WITH_ONLY_LEFT_CHILD;
						if ( n.getLeft().getMinValue() <= p && p <= n.getLeft().getMaxValue() ) { 
							return n.getLeft();
						} else {
							return findStartingNode(p, n.getParent());
						}
					}
				}
			}
		}
	}

	private boolean mustBeSetParentStatus(BucketNode v) {

		// if v.parent.status = NUL then the researh never visited v.parent and then 
		// v.parent.status will be set to the correct value
		// returns true if the status of the parent of v was null  
		if (v.getParent() != null) {

			if (v.getParent().getStatus() == null) {

				if (v.getSplitValue() < v.getParent().getSplitValue()) {

					// v is a left child of v.parent
					v.getParent().setStatus(leftVisited); 
					return true;

				} else {

					// v is a right child of v.parent
					v.getParent().setStatus(rightVisited);
					return true;
				}
			} else {
				// n.parent.status not is NIL => do nothing
				return false;
			} 
		} else {
			// n is root => do nothing
			return false;
		}
	}

	public List<BucketNode> getAllNodes() {
		return allNodes;
	}

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	/*
	 * test functions
	 */
	public TestResult testFindStartingNode(BucketBinaryTree tree, int queryPoint) {

		TestResult r = new TestResult();

		for ( BucketNode node : tree.getAllNodes() ) {
			if ( ! node.isRoot() ) {
				BucketNode start = tree.findStartingNode(queryPoint, node);
				if ( start.isRoot() ) {
					r.numRoot++;
				} else {
					r.numNoRoot++;
				}	
			}
		}

		r.percNumRoot   = (double) ((r.numRoot * 100)   / allNodes.size());
		r.percNumNoRoot = (double) ((r.numNoRoot * 100) / allNodes.size());

		return r;
	}

	public TestResult testFindStartingNodeSide(BucketBinaryTree tree, int queryPoint) {

		TestResult r = new TestResult();
		if ( queryPoint < root.getSplitValue() ) {
			for ( BucketNode node : tree.leftSideNodes ) {
				if ( ! node.isRoot() ) {
					BucketNode start = tree.findStartingNode(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}	
			r.percNumRoot   = (double) ((r.numRoot * 100)   / leftSideNodes.size());
			r.percNumNoRoot = (double) ((r.numNoRoot  * 100)/ leftSideNodes.size());

		} else {
			for ( BucketNode node : tree.rightSideNodes ) {
				if ( ! node.isRoot() ) {
					BucketNode start = tree.findStartingNode(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}
			r.percNumRoot   = (double) ((r.numRoot * 100)   / rightSideNodes.size());
			r.percNumNoRoot = (double) ((r.numNoRoot * 100) / rightSideNodes.size());

		}
		return r;
	}

	public TestResult testFindStartingNodeMinMax(BucketBinaryTree tree, int queryPoint) {

		TestResult r = new TestResult();

		for ( BucketNode node : tree.getAllNodes() ) {
			if ( ! node.isRoot() ) {
				BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
				if ( start.isRoot() ) {
					r.numRoot++;
				} else {
					r.numNoRoot++;
				}	
			}
		}	
		r.percNumRoot   = (double) ((r.numRoot * 100)    / allNodes.size());
		r.percNumNoRoot = (double) ((r.numNoRoot  * 100) / allNodes.size());

		return r;
	}

	public TestResult testFindStartingNodeMinMaxSide(BucketBinaryTree tree, int queryPoint) {

		TestResult r = new TestResult();

		if ( queryPoint < root.getSplitValue() ) {
			for ( BucketNode node : tree.leftSideNodes ) {
				if ( ! node.isRoot() ) {
					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}	
			r.percNumRoot   = (double) ((r.numRoot * 100)   / leftSideNodes.size());
			r.percNumNoRoot = (double) ((r.numNoRoot * 100) / leftSideNodes.size());

		} else { 
			for ( BucketNode node : tree.rightSideNodes ) {
				if ( ! node.isRoot() ) {
					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}
			r.percNumRoot   = (double) ((r.numRoot * 100)   / rightSideNodes.size());
			r.percNumNoRoot = (double) ((r.numNoRoot * 100) / rightSideNodes.size());

		}
		return r;
	}

}
