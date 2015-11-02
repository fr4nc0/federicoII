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

	public static List<BucketNode> left_leftSideNodes;
	public static List<BucketNode> left_rightSideNodes;
	public static List<BucketNode> right_leftSideNodes;
	public static List<BucketNode> right_rightSideNodes;

	private BucketNode root;
	private int bucketSize;

	private final String none 			= "none";
	private final String leftVisited 	= "leftVisited";
	private final String rightVisited 	= "rightVisited";



	public Node getRoot() {
		return root;
	}

	public void setRoot(BucketNode root) {
		this.root = root;
	}


	/*
	 * constructor
	 */
	public BucketBinaryTree(int bucketSize) {
		this.bucketSize = bucketSize;
	}




	/*
	 * inserts
	 */

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

	public void insert2(int newValue) {

		if ( root == null) {

			allNodes = new ArrayList<BucketNode>();
			left_leftSideNodes 		= new ArrayList<BucketNode>();
			left_rightSideNodes 	= new ArrayList<BucketNode>();
			right_leftSideNodes 	= new ArrayList<BucketNode>();
			right_rightSideNodes 	= new ArrayList<BucketNode>();

			root = new BucketNode(newValue, bucketSize, allNodes);
			root.setRoot(true);

		} else {

			root.setRoot(false);
			root = insert2(newValue, root);
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

	private BucketNode insert2(int newValue, BucketNode node) {

		/*
		 * this insert methods labels the node as left-left, left-right, right-left and right-right
		 */

		if ( node.isLeaf() ) {

			node.add2(newValue, this.bucketSize, allNodes, 
					left_leftSideNodes, left_rightSideNodes,
					right_leftSideNodes, right_rightSideNodes);

		} else {

			if ( newValue < node.getSplitValue() ) {

				node.setLeft( insert2(newValue, node.getLeft()) );
				node.setHeight( Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);

				node.setMaxValue(node.getRight().getMaxValue());
				node.setMinValue(node.getLeft().getMinValue());

				if( node.getLeft().getHeight() - node.getRight().getHeight() == 2 ) {

					if( newValue < node.getLeft().getSplitValue() ) {

						node = rotateWithLeftChild2( node );

					} else {

						node = doubleWithLeftChild2( node );
					}
				}
			} else {

				node.setRight( insert2(newValue, node.getRight()) );
				node.setHeight( Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);

				node.setMaxValue(node.getRight().getMaxValue());
				node.setMinValue(node.getLeft().getMinValue());

				if( node.getRight().getHeight() - node.getLeft().getHeight() == 2 ) {
					if( newValue > node.getRight().getSplitValue() ) {

						node = rotateWithRightChild2( node );

					} else {

						node = doubleWithRightChild2( node );
					}
				}
			}	 
		}
		return node;
	}




	/*
	 * rotations
	 */

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

		//k2.setMinValue(k2.getRight().getMinValue());
		//k1.setMaxValue(k1.getLeft().getMaxValue());
		k2.setMinValue(k2.getLeft().getMinValue());
		k2.setMaxValue(k2.getRight().getMaxValue());
		k1.setMinValue(k1.getLeft().getMinValue());
		k1.setMaxValue(k1.getRight().getMaxValue());

		k2.setHeight( Math.max(k2.getLeft().getHeight(), k2.getRight().getHeight()) + 1);
		//k1.setHeight( Math.max(k1.getLeft().getHeight(), k2.getHeight()));
		k1.setHeight( Math.max(k1.getLeft().getHeight(), k1.getRight().getHeight()) + 1);
		return k1;
	}

	private static BucketNode rotateWithLeftChild2( BucketNode k2 ){

		/*
		 * 		   k2            k1
		 * 		  /	 \          /  \
		 *       k1   A   =>   B   k2
		 *      /  \              /  \
		 *     B    C            C    A
		 */

		BucketNode k1 = k2.getLeft();

		// changes in structure
		k1.setParent(k2.getParent());
		k2.setParent(k1);
		k2.setLeft(k1.getRight());
		k1.setRight(k2);

		// changes in min max values
		k2.setMinValue(k2.getLeft().getMinValue());
		k1.setMaxValue(k1.getRight().getMaxValue());

		// changes in heights
		k2.setHeight( Math.max(k2.getLeft().getHeight(), k2.getRight().getHeight()) + 1 );
		k1.setHeight( Math.max(k1.getLeft().getHeight(), k1.getRight().getHeight()) + 1 );

		// changes in Sides
		if ( k1.getParent() == null ) {
			/*
			 * 		   k2(n)                  k1(n)
			 * 		  /	 \                 /         \ 
			 *   (L)k1   A(R)    =>  (L)B             k2(R)
			 *      /  \              /  \		     /  \
			 * (LL)B    C(LR)    (LL)C    A(LR) (RL)U   W(RR) 
			 */

			removeNodeFromLists(k1);	// <-- inutile 
			removeNodeFromLists(k1.getLeft());
			removeNodeFromLists(k1.getRight());

			k1.setSide(Side.NONE);
			k1.getLeft().setSide(Side.LEFT);
			k1.getRight().setSide(Side.RIGHT);

			changeSideToSubtree2(k1.getLeft().getLeft(), Side.LEFT_LEFT);
			changeSideToSubtree2(k1.getLeft().getRight(), Side.LEFT_RIGHT);
			changeSideToSubtree2(k1.getRight().getLeft(), Side.RIGHT_LEFT);
			changeSideToSubtree2(k1.getRight().getRight(), Side.RIGTH_RIGHT);

		} else {

			// k2 was not the root. 
			if (k2.getSide().equals(Side.LEFT) ){
				/*
				 * 		   k2(L)               	       k1(L)
				 * 		  /	 \              	  /           \ 
				 *   (LL)k1   A(LR)   =>  	(LL)B              k2(LR)
				 *      /  \            	   /  \           /  \
				 * (LL)B    C(LL)    	  (LL)C    A(LL) (LR)U   W(LR)
				 */

				//removeNodeFromLists(k1);	// <-- inutile 
				removeNodeFromLists(k1.getLeft());
				removeNodeFromLists(k1.getRight());

				k1.setSide(Side.LEFT);
				k1.getLeft().setSide(Side.LEFT_LEFT);
				k1.getRight().setSide(Side.LEFT_RIGHT);

				changeSideToSubtree2(k1.getLeft().getLeft(), Side.LEFT_LEFT);
				changeSideToSubtree2(k1.getLeft().getRight(), Side.LEFT_LEFT);
				changeSideToSubtree2(k1.getRight().getLeft(), Side.LEFT_RIGHT);
				changeSideToSubtree2(k1.getRight().getRight(), Side.LEFT_RIGHT);

			} else if (k2.getSide().equals(Side.RIGHT)) {

				/*
				 * 		   k2(R)               	       k1(R)
				 * 		  /	 \              	  /           \ 
				 *   (RL)k1   A(RR)   =>  	(RL)B              k2(RR)
				 *      /  \            	   /  \           /  \
				 * (RL)B    C(RL)    	  (RL)C    A(RL) (RR)U   W(RR)
				 */

				removeNodeFromLists(k1);	// <-- inutile 
				removeNodeFromLists(k1.getLeft());
				removeNodeFromLists(k1.getRight());

				k1.setSide(Side.RIGHT);
				k1.getLeft().setSide(Side.RIGHT_LEFT);
				k1.getRight().setSide(Side.RIGTH_RIGHT);

				changeSideToSubtree2(k1.getLeft().getLeft(), Side.RIGHT_LEFT);
				changeSideToSubtree2(k1.getLeft().getRight(), Side.RIGHT_LEFT);
				changeSideToSubtree2(k1.getRight().getLeft(), Side.RIGTH_RIGHT);
				changeSideToSubtree2(k1.getRight().getRight(), Side.RIGTH_RIGHT);

			} else {

				/*
				 * 		   k2(xx)             	      k1(xx)
				 * 		  /	 \              	  /           \ 
				 *   (xx)k1   A(xx)   =>  	(xx)B              k2(xx)
				 *      /  \            	   /  \           /  \
				 * (xx)B    C(xx)    	  (xx)C    A(xx) (xx)U   W(xx)
				 */
				// NO changes of side are required
			}
		}

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

		//k1.setMaxValue(k1.getRight().getMaxValue());
		//k2.setMinValue(k2.getLeft().getMinValue());
		k2.setMinValue(k2.getLeft().getMinValue());
		k2.setMaxValue(k2.getRight().getMaxValue());
		k1.setMinValue(k1.getLeft().getMinValue());
		k1.setMaxValue(k1.getRight().getMaxValue());

		k1.setHeight( Math.max(k1.getLeft().getHeight(), k1.getRight().getHeight())  + 1); 
		//k2.setHeight( Math.max(k2.getRight().getHeight(), k1.getHeight()) + 1);
		k2.setHeight( Math.max(k2.getRight().getHeight(), k1.getRight().getHeight()) + 1);
		return k2;
	}

	private static BucketNode rotateWithRightChild2( BucketNode k1 ) {
		/*
		 * 		   k2            k1
		 * 		  /	 \          /  \
		 *       k1   A   <=   B   k2
		 *      /  \              /  \
		 *     B    C            C    A
		 */

		BucketNode k2 = k1.getRight();

		// changes in structure
		k2.setParent(k1.getParent());
		k1.setParent(k2);
		k1.setRight(k2.getLeft());
		k2.setLeft(k1);

		// changes in min max values
		k1.setMinValue(k1.getLeft().getMinValue());
		k2.setMaxValue(k2.getRight().getMaxValue());

		// changes in heights
		k1.setHeight( Math.max(k1.getLeft().getHeight(), k1.getRight().getHeight()) + 1 );
		k2.setHeight( Math.max(k2.getLeft().getHeight(), k2.getRight().getHeight()) + 1 );

		// changes in Sides
		if ( k2.getParent() == null ) {
			//k1 was the root

			/*
			 * 		   k2(n)                  k1(n)
			 * 		  /	 \                 /         \ 
			 *   (L)k1   A(R)    <=  (L)B             k2(R)
			 *      /  \              /  \		     /  \
			 * (LL)B    C(LR)    (LL)C    A(LR) (RL)U   W(RR) 
			 */

			//removeNodeFromLists(k2);	// <-- inutile 
			removeNodeFromLists(k2.getLeft());
			removeNodeFromLists(k2.getRight());

			k2.setSide(Side.NONE);
			k2.getLeft().setSide(Side.LEFT);
			k2.getRight().setSide(Side.RIGHT);

			changeSideToSubtree2(k2.getLeft().getLeft(), Side.LEFT_LEFT);
			changeSideToSubtree2(k2.getLeft().getRight(), Side.LEFT_RIGHT);
			changeSideToSubtree2(k2.getRight().getLeft(), Side.RIGHT_LEFT);
			changeSideToSubtree2(k2.getRight().getRight(), Side.RIGTH_RIGHT);

		} else {

			// k1 was not the root. 
			if (k1.getSide().equals(Side.LEFT) ){
				/*
				 * 		   k2(L)               	       k1(L)
				 * 		  /	 \              	  /           \ 
				 *   (LL)k1   A(LR)   <=  	(LL)B              k2(LR)
				 *      /  \            	   /  \           /  \
				 * (LL)B    C(LL)    	  (LL)C    A(LL) (LR)U   W(LR)
				 */

				removeNodeFromLists(k2);
				removeNodeFromLists(k2.getLeft());
				removeNodeFromLists(k2.getRight());

				k2.setSide(Side.LEFT);
				k2.getLeft().setSide(Side.LEFT_LEFT);
				k2.getRight().setSide(Side.LEFT_RIGHT);

				changeSideToSubtree2(k2.getLeft().getLeft(), Side.LEFT_LEFT);
				changeSideToSubtree2(k2.getLeft().getRight(), Side.LEFT_LEFT);
				changeSideToSubtree2(k2.getRight().getLeft(), Side.LEFT_RIGHT);
				changeSideToSubtree2(k2.getRight().getRight(), Side.LEFT_RIGHT);

			} else if (k1.getSide().equals(Side.RIGHT)) {

				/*
				 * 		   k2(R)               	       k1(R)
				 * 		  /	 \              	  /           \ 
				 *   (RL)k1   A(RR)   =>  	(RL)B              k2(RR)
				 *      /  \            	   /  \           /  \
				 * (RL)B    C(RL)    	  (RL)C    A(RL) (RR)U   W(RR)
				 */

				removeNodeFromLists(k2);
				removeNodeFromLists(k2.getLeft());
				removeNodeFromLists(k2.getRight());

				k2.setSide(Side.RIGHT);
				k2.getLeft().setSide(Side.RIGHT_LEFT);
				k2.getRight().setSide(Side.RIGTH_RIGHT);

				changeSideToSubtree2(k2.getLeft().getLeft(), Side.RIGHT_LEFT);
				changeSideToSubtree2(k2.getLeft().getRight(), Side.RIGHT_LEFT);
				changeSideToSubtree2(k2.getRight().getLeft(), Side.RIGTH_RIGHT);
				changeSideToSubtree2(k2.getRight().getRight(), Side.RIGTH_RIGHT);

			} else {

				/*
				 * 		   k2(xx)             	      k1(xx)
				 * 		  /	 \              	  /           \ 
				 *   (xx)k1   A(xx)   =>  	(xx)B              k2(xx)
				 *      /  \            	   /  \           /  \
				 * (xx)B    C(xx)    	  (xx)C    A(xx) (xx)U   W(xx)
				 */
				// NO changes of side are required
			}
		}

		return k2;
	}

	private static void removeNodeFromLists(BucketNode node) {

		if ( node.getSide().equals(Side.LEFT_LEFT) ) {

			left_leftSideNodes.remove(node);

		} else if ( node.getSide().equals(Side.LEFT_RIGHT) ) {

			left_rightSideNodes.remove(node);


		} else if ( node.getSide().equals(Side.RIGHT_LEFT) ) {

			right_leftSideNodes.remove(node);

		} else if ( node.getSide().equals(Side.RIGTH_RIGHT) ) {

			right_rightSideNodes.remove(node);
		}
	}

	private static void changeSideToSubtree(BucketNode node, Side newSide) {
		/*
		 * change the side to all nodes in subtree on n 
		 * and update the lists
		 */
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

	private static void changeSideToSubtree2(BucketNode node, Side newSide) {
		/*
		 * change the side to all nodes in subtree on n 
		 * and update the lists
		 */
		if ( node != null )  {

			if ( ! node.getSide().equals(newSide) ) {

				removeNodeFromLists(node);
				//Side oldSide = node.getSide(); 
				node.setSide(newSide);

				//add
				if ( newSide.equals(Side.LEFT_LEFT) ) {

					left_leftSideNodes.add(node);

				} else if ( newSide.equals(Side.LEFT_RIGHT) ) {

					left_rightSideNodes.add(node);

				} else if ( newSide.equals(Side.RIGHT_LEFT) ) {

					right_leftSideNodes.add(node);

				} else if ( newSide.equals(Side.RIGTH_RIGHT) ) {

					right_rightSideNodes.add(node);
				}

				// remove
				/*
				if ( oldSide.equals(Side.LEFT_LEFT) ) {

					left_leftSideNodes.remove(node);

				} else if ( oldSide.equals(Side.LEFT_RIGHT) ) {

					left_rightSideNodes.remove(node);

				} else if ( oldSide.equals(Side.RIGHT_LEFT) ) {

					right_leftSideNodes.remove(node);

				} else if ( oldSide.equals(Side.RIGTH_RIGHT) ) {

					right_rightSideNodes.remove(node);
				}
				 */

				changeSideToSubtree2(node.getRight(), newSide);
				changeSideToSubtree2(node.getLeft(), newSide);
			}
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

	private static BucketNode doubleWithLeftChild2( BucketNode k3 )
	{
		k3.setLeft( rotateWithRightChild2(k3.getLeft()) );
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

	private static BucketNode doubleWithRightChild2( BucketNode k1 )
	{
		k1.setRight( rotateWithLeftChild( k1.getRight()) );
		return rotateWithRightChild( k1 );
	}



	/*
	 * nearest neighbor query
	 */
	private void nearestNeighbor(BucketNode v, int p, int k, Result result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		//System.out.println("nearestNeighbor: " + v + " v.status:" + v.getStatus() + " status:" + status);

		NN(v, p, k, result);
	}

	public Result nearestQuery(int queryPoint, int k) {

		Result result = new Result(queryPoint, k);
		nearestNeighbor(root, queryPoint, k, result, none);

		return result;
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




	/*
	 * random nearest neighbor query
	 */

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





	public static void main( String [ ] args ){

		int bucketSize 	= 5;
		int maxNumPunti	= 16384;
		int k			= 3;
		int queryPoint  = 0;

		for ( int numPunti = 32; numPunti <= maxNumPunti; ){

			numPunti = numPunti * 2;

			BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

			for (int i = 0; i < numPunti; i++) {
				//tree.insert(i);
				tree.insert2(i);	
				tree.buildListSide2();
			}

			/*
			System.out.println("LL: " + tree.left_leftSideNodes.size() +
					" LR: " + tree.left_rightSideNodes.size() + 
					" RL: " + tree.right_leftSideNodes.size() + 
					" RR: " + tree.right_rightSideNodes.size());
			 */

			System.out.println("n = " + numPunti);

			double sommaPercRoot = 0, sommaPercNoRoot = 0;

			for ( queryPoint = 0; queryPoint < numPunti; queryPoint++ ) {

				//TestResult res = tree.testFSN(tree, queryPoint);
				//TestResult res = tree.testFSNMinMax(tree, queryPoint);
				//TestResult res = tree.testFSNSide(tree, queryPoint);
				//TestResult res = tree.testFSNMinMaxSide(tree, queryPoint);

				//TestResult res = tree.testFSNSide2(tree, queryPoint);			//insert2() + buildList
				TestResult res = tree.testFSNMinMaxSide2(tree, queryPoint);	//insert2() + buildList

				sommaPercNoRoot = sommaPercNoRoot + res.percNumNoRoot;
				sommaPercRoot	= sommaPercRoot + res.percNumRoot;

				//System.out.println(res.percNumRoot + " --" + res.percNumNoRoot );
				//System.out.println(sommaPercRoot + " --" + sommaPercNoRoot );
			}

			double mediaPercRoot   = sommaPercRoot/numPunti;
			double mediaPercNoRoot = sommaPercNoRoot/numPunti;

			System.out.println("media%Root: " + Math.round(mediaPercRoot) 
					+ " media%noRoot: " + Math.round(mediaPercNoRoot));

		}


		/*
		Result result = tree.randomNearestQuery(queryPoint, k);
		//Result result = tree.nearestQuery(queryPoint, k);
		Integer[] points = result.getPoints();
		for ( Integer i : points ) {
			System.err.println(i);
		}
		 */
	}

	public void buildListSide2() {

		/*
		 * questo metodo è necessario perchè ci sta un piccolo errore 
		 * nella costruzione delle liste durante il caricamento dell'albero.
		 * Manca sempre e solo un nodo dalle due liste right 
		 */

		left_leftSideNodes 		= new ArrayList<BucketNode>();
		left_rightSideNodes 	= new ArrayList<BucketNode>();
		right_leftSideNodes 	= new ArrayList<BucketNode>();
		right_rightSideNodes 	= new ArrayList<BucketNode>();

		if ( root.getLeft() != null ) {
			addSubreeToList((BucketNode) root.getLeft().getLeft(),  left_leftSideNodes, Side.LEFT_LEFT);	
			addSubreeToList((BucketNode) root.getLeft().getRight(), left_rightSideNodes, Side.LEFT_RIGHT);
		}

		if ( root.getRight() != null ) {
			addSubreeToList((BucketNode) root.getRight().getLeft(), right_leftSideNodes, Side.RIGHT_LEFT);
			addSubreeToList((BucketNode) root.getRight().getRight(), right_rightSideNodes, Side.RIGTH_RIGHT);	
		}
	}


	private void addSubreeToList(BucketNode node,
			List<BucketNode> list, Side newSide) {

		if ( node != null ){

			node.setSide(newSide);
			list.add(node);

			if ( node.getLeft() != null) {
				addSubreeToList(node.getLeft(), list, newSide);
			}

			if ( node.getRight() != null) {
				addSubreeToList(node.getRight(), list, newSide);
			}
		}
	}




	/*
	 * finding starting node
	 */
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
	public TestResult testFSN(BucketBinaryTree tree, int queryPoint) {

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

		double denominatore = tree.allNodes.size() - 1;
		double num1 = r.numRoot * 100;
		double num2 = r.numNoRoot * 100;

		r.percNumRoot   = (num1 / denominatore);
		r.percNumNoRoot = (num2 / denominatore);

		return r;
	}

	public TestResult testFSNSide(BucketBinaryTree tree, int queryPoint) {

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

			double denominatore = tree.leftSideNodes.size();
			double num1 = r.numRoot * 100;
			double num2 = r.numNoRoot * 100;

			r.percNumRoot   = (num1 / denominatore);
			r.percNumNoRoot = (num2 / denominatore);

			//r.percNumRoot   = (double) ((r.numRoot * 100)   / leftSideNodes.size());
			//r.percNumNoRoot = (double) ((r.numNoRoot  * 100)/ leftSideNodes.size());

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

			double denominatore = tree.rightSideNodes.size();
			double num1 = r.numRoot * 100;
			double num2 = r.numNoRoot * 100;

			r.percNumRoot   = (num1 / denominatore);
			r.percNumNoRoot = (num2 / denominatore);

			//r.percNumRoot   = (double) ((r.numRoot * 100)   / rightSideNodes.size());
			//r.percNumNoRoot = (double) ((r.numNoRoot * 100) / rightSideNodes.size());

		}
		return r;
	}

	public TestResult testFSNMinMax(BucketBinaryTree tree, int queryPoint) {

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

		double denominatore = tree.allNodes.size() - 1;
		double num1 = r.numRoot * 100;
		double num2 = r.numNoRoot * 100;

		r.percNumRoot   = (num1 / denominatore);
		r.percNumNoRoot = (num2 / denominatore);

		//r.percNumRoot   = (double) ((r.numRoot * 100)    / allNodes.size());
		//r.percNumNoRoot = (double) ((r.numNoRoot  * 100) / allNodes.size());

		return r;
	}

	public TestResult testFSNMinMaxSide(BucketBinaryTree tree, int queryPoint) {

		TestResult r = new TestResult();
		double numProve = 0;

		if ( queryPoint < root.getSplitValue() ) {
			for ( BucketNode node : tree.leftSideNodes ) {
				if ( ! node.isRoot() ) {
					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}

		} else {
			for ( BucketNode node : tree.rightSideNodes ) {
				if ( ! node.isRoot() ) {
					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}
		}

		double num1 = r.numRoot * 100;
		double num2 = r.numNoRoot * 100;
		r.percNumRoot   = (num1 / numProve);
		r.percNumNoRoot = (num2 / numProve);

		return r;
	}

	public TestResult testFSNSide2(BucketBinaryTree tree, int queryPoint) {

		TestResult r = new TestResult();
		double numProve = 0;

		if ( queryPoint < root.getSplitValue() ) {
			// go to left child 
			if ( queryPoint < root.getLeft().getSplitValue() ) {
				for ( BucketNode node : tree.left_leftSideNodes ) {

					BucketNode start = tree.findStartingNode(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
				
			} else {
				for ( BucketNode node : tree.left_rightSideNodes ) {
					BucketNode start = tree.findStartingNode(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}

		} else {
			// go to right child
			if ( queryPoint < root.getRight().getSplitValue() ) {
				for ( BucketNode node : tree.right_leftSideNodes ) {

					BucketNode start = tree.findStartingNode(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}	
				
			} else {
				for ( BucketNode node : tree.right_rightSideNodes ) {

					BucketNode start = tree.findStartingNode(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}
		}

		double num1 = r.numRoot * 100;
		double num2 = r.numNoRoot * 100;

		r.percNumRoot   = (num1 / numProve);
		r.percNumNoRoot = (num2 / numProve);

		return r;
	}

	public TestResult testFSNMinMaxSide2(BucketBinaryTree tree, int queryPoint) {

		TestResult r = new TestResult();
		double numProve = 0;

		if ( queryPoint < root.getSplitValue() ) {
			// go to left child 
			if ( queryPoint < root.getLeft().getSplitValue() ) {

				//System.out.println("left-left");

				for ( BucketNode node : tree.left_leftSideNodes ) {
					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}

			} else {

				//System.out.println("left-right");

				for ( BucketNode node : tree.left_rightSideNodes ) {
					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}

		} else {
			// go to right child
			if ( queryPoint < root.getRight().getSplitValue() ) {

				//System.out.println("right-left");

				for ( BucketNode node : tree.right_leftSideNodes ) {
					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}	

			} else {

				//System.out.println("right-right");

				for ( BucketNode node : tree.right_rightSideNodes ) {

					BucketNode start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}
		}

		double num1 = r.numRoot * 100;
		double num2 = r.numNoRoot * 100;

		r.percNumRoot   = (num1 / numProve);
		r.percNumNoRoot = (num2 / numProve);

		return r;
	}

}
