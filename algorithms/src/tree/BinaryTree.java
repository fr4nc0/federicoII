package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tree.Side;



public class BinaryTree<T extends Comparable<T>> {

	/*
	 * used only for tests
	 */

	public List<Node<T>> allNodes;
	public List<Node<T>> leftSideNodes;
	public List<Node<T>> rightSideNodes;

	@SuppressWarnings("rawtypes")
	public List<Node> left_leftSideNodes;
	@SuppressWarnings("rawtypes")
	public List<Node> left_rightSideNodes;
	@SuppressWarnings("rawtypes")
	public List<Node> right_leftSideNodes;
	@SuppressWarnings("rawtypes")
	public List<Node> right_rightSideNodes;

	private Node<T> root;
	private int bucketSize;

	private final String none 			= "none";
	private final String leftVisited 	= "leftVisited";
	private final String rightVisited 	= "rightVisited";

	private Distance<T> distance;


	public Node<T> getRoot() {
		return root;
	}

	public void setRoot(Node<T> root) {
		this.root = root;
	}


	/*
	 * constructor
	 */
	public BinaryTree(int bucketSize, Distance<T> distance) {
		this.bucketSize = bucketSize;
		this.distance 	= distance;
	}




	/*
	 * inserts
	 */

	public void insert(T newValue) {

		if ( root == null) {

			allNodes = new ArrayList<Node<T>>();
			leftSideNodes = new ArrayList<Node<T>>();
			rightSideNodes = new ArrayList<Node<T>>();

			root = new Node<T>(newValue, bucketSize, allNodes);
			root.setRoot(true);

		} else {

			root.setRoot(false);
			root = insert(newValue, root);
			root.setRoot(true);
		}
	}

	
	
	
	private Node<T> insert(T newValue, Node<T> node) {

		if ( node.isLeaf() ) {

			node.add(newValue, this.bucketSize, allNodes, leftSideNodes, rightSideNodes);

		} else {

			//if ( newValue < node.getSplitValue() ) {
			if ( newValue.compareTo((T) node.getSplitValue()) < 0 ) {

				node.setLeft( insert(newValue, node.getLeft()) );
				node.setHeight( Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);

				node.setMaxValue(node.getRight().getMaxValue());
				node.setMinValue(node.getLeft().getMinValue());

				if( node.getLeft().getHeight() - node.getRight().getHeight() == 2 ) {

					//if( newValue < node.getLeft().getSplitValue() ) {
					if( newValue.compareTo((T) node.getLeft().getSplitValue()) < 0 ) {

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
					//if( newValue > node.getRight().getSplitValue() ) {
					if( newValue.compareTo((T) node.getRight().getSplitValue()) > 0 ) {

						node = rotateWithRightChild( node );

					} else {

						node = doubleWithRightChild( node );
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
	private Node<T> rotateWithLeftChild( Node<T> k2 ){
		/*
		 * 		   k2            k1
		 * 		  /	 \          /  \
		 *       k1   A   =>   B   k2
		 *      /  \              /  \
		 *     B    C            C    A
		 */
		Node<T> k1 = k2.getLeft();
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

	/**
	 * Rotate binary tree node with right child.
	 * For AVL trees, this is a single rotation for case 4.
	 * Update heights, then return new root.
	 */
	private Node<T> rotateWithRightChild( Node<T> k1 ) {
		/*
		 * 		   k2            k1
		 * 		  /	 \          /  \
		 *       k1   A   <=   B   k2
		 *      /  \              /  \
		 *     B    C            C    A
		 */
		Node<T> k2 = k1.getRight();
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

	private void changeSideToSubtree(Node<T> node, Side newSide) {
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

	/**
	 * Double rotate binary tree node: first left child
	 * with its right child; then node k3 with new left child.
	 * For AVL trees, this is a double rotation for case 2.
	 * Update heights, then return new root.
	 */
	private Node<T> doubleWithLeftChild( Node<T> k3 )
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
	private Node<T> doubleWithRightChild( Node<T> k1 )
	{
		k1.setRight( rotateWithLeftChild( k1.getRight()) );
		return rotateWithRightChild( k1 );
	}

	




	/*
	 * nearest neighbor query
	 */

	public Result<T> nearestQuery(T queryPoint, int k) {

		
		Result<T> result = new Result<T>(queryPoint, k, distance);
		nearestNeighbor(root, queryPoint, k, result, none);

		return result;
	}

	private void nearestNeighbor(Node<T> v, T p, int k, Result<T> result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		NN(v, p, k, result);
	}

	private void NN(Node<T> v, T p, int k, Result<T> result) {

		//System.out.println(v + " result:" + result);
		
		if ( v.isLeaf() ) {

			result.add (v.getBucket());
			return; 

		} else {

			if ( v.getStatus().equals(none) ) {
				//if ( p < v.getSplitValue()) {
				if ( p.compareTo((T) v.getSplitValue()) < 0 ) {
					
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

					nearestNeighbor (v.getLeft(), p, k, result, none);
				}
			} 

			if ( v.getStatus().equals(leftVisited) ) {

				if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, p, result) ) {

					nearestNeighbor (v.getRight(), p, k, result, none);
				}
			}
		}

		v.setStatus(null);
	}

	private boolean secondChildMustBeVisited(Node<T> v, T p, Result<T> result) {

		/*
		 * if the result contains less than k point the second child must be always visited
		 */
		if ( ! result.isFull() ) {

			return true;

		} else {
			//if ( Distance.oneDimension(p, v.getSplitValue() ) < Distance.oneDimension(p, result.getFarthest()) ) {
			if ( distance.distance(p, (T) v.getSplitValue() ) < distance.distance(p, (T) result.getFarthest()) ) {

				return true; 	//Descend other sibling also
			}

			else {
				return false; 	//Do not descend 
			}
		}
	}




	/*
	 * nearest neighbor query with ending node
	 */

	public Result<T> nearestQueryWithEndingNode(T queryPoint, int k) {

		Result<T> result = new Result<T>(queryPoint, k, distance);
		result.setEndingNode(root);

		nearestNeighborWithEndingNode(root, queryPoint, k, result, none);

		return result;
	}

	private void nearestNeighborWithEndingNode(Node<T> v, T p, int k, Result<T> result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		NN_WithEndingNode(v, p, k, result);
	}

	private void NN_WithEndingNode(Node<T> v, T p, int k, Result<T> result) {

		if ( v.isLeaf() ) {

			result.add (v.getBucket());
			//return; 

		} else {

			if ( v.getStatus().equals(none) ) {
				//if ( p < v.getSplitValue()) {
				if ( p.compareTo((T) v.getSplitValue()) < 0 ) {

					v.setStatus(leftVisited); 
					nearestNeighborWithEndingNode (v.getLeft(), p, k, result, none);

				} else {

					v.setStatus(rightVisited); 
					nearestNeighborWithEndingNode (v.getRight(), p, k, result, none);
				}
			} 

			if ( ! (result.isUpFromLeft() && result.isUpFromRight()) ) {

				/*
				 * potrebbe entrare in rightVisited oppure in leftVisited dopo essere uscito dall'if precedente
				 */
				if ( v.getStatus().equals(rightVisited) ) {

					if ( ! (v.getLeft() == null) && secondChildMustBeVisited(v, p, result) ) {

						nearestNeighborWithEndingNode (v.getLeft(), p, k, result, none);

					} else {

						result.setUpFromLeft(true);
					}
				} 

				if ( v.getStatus().equals(leftVisited) ) {

					if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, p, result) ) {

						nearestNeighborWithEndingNode (v.getRight(), p, k, result, none);

					} else {

						result.setUpFromRight(true);
					}
				}

			} else {

				if ( result.getEndingNode().equals(root.getSplitValue()) ) {
					result.setEndingNode(v);
				}
			}
		}

		v.setStatus(null);
	}




	/*
	 * random nearest neighbor query without ending node
	 */

	public Result<T> randomNearestQueryWithoutEndingNode(T queryPoint, int k) {

		int randomNodeIndex = 0;
		Node<T> randomNode = null;

		// side
		//if ( queryPoint < root.getSplitValue() ) {
		if ( queryPoint.compareTo((T) root.getSplitValue()) < 0 ) {
			// choose a node in left subtree
			randomNodeIndex = randInt( 0, (leftSideNodes.size() - 1) );
			randomNode = leftSideNodes.get(randomNodeIndex);

		} else {
			// choose a node in right subtree
			randomNodeIndex = randInt( 0, (rightSideNodes.size() - 1) );
			randomNode = rightSideNodes.get(randomNodeIndex);
		}

		Node<T> startNode = findStartingNode(queryPoint, randomNode);

		Result<T> result = new Result<T>(queryPoint, k, distance);
		randomNearestNeighborWithoutEndingNode(startNode, queryPoint, k, result, none);

		return result;
	}

	private void randomNearestNeighborWithoutEndingNode(Node<T> v, T p, int k, Result<T> result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		randomNN_withoutEndingNode(v, p, k, result);
	}

	private void randomNN_withoutEndingNode(Node<T> v, T p, int k, Result<T> result) {

		if ( v.isLeaf() ) {

			result.add (v.getBucket());

			if ( mustBeSetParentStatus(v) )  {

				randomNearestNeighborWithoutEndingNode(v.getParent(), p, k, result, null);
			}

		} else {

			if ( v.getStatus().equals(none) ) {
				//if ( p < v.getSplitValue()) {
				if ( p.compareTo((T) v.getSplitValue()) < 0 ) {

					v.setStatus(leftVisited); 
					randomNearestNeighborWithoutEndingNode (v.getLeft(), p, k, result, none);

				} else {

					v.setStatus(rightVisited); 
					randomNearestNeighborWithoutEndingNode (v.getRight(), p, k, result, none);
				}
			} 
			/*
			 * potrebbe entrare in rightVisited oppure in leftVisited dopo essere uscito dall'if precedente
			 */
			if ( v.getStatus().equals(rightVisited) ) {

				if ( ! (v.getLeft() == null) && secondChildMustBeVisited(v, p, result) ) {

					randomNearestNeighborWithoutEndingNode (v.getLeft(), p, k, result, none);
				} 

				// do the following even if the left child is not visited 
				if ( mustBeSetParentStatus(v) )  {

					randomNearestNeighborWithoutEndingNode(v.getParent(), p, k, result, null);
				}
			} 

			if ( v.getStatus().equals(leftVisited) ) {

				if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, p, result) ) {

					randomNearestNeighborWithoutEndingNode (v.getRight(), p, k, result, none);
				}

				// do the following even if the right child is not visited 
				if ( mustBeSetParentStatus(v) )  {

					randomNearestNeighborWithoutEndingNode(v.getParent(), p, k, result, null);
				}
			}
		}
		v.setStatus(null);
	}




	/*
	 * random nearest neighbor query with ending node
	 */

	public Result<T> randomNearestQuery(T queryPoint, int k) {

		int randomNodeIndex = 0;
		Node<T> randomNode = null;

		// side
		//if ( queryPoint < root.getSplitValue() ) {
		if ( queryPoint.compareTo((T) root.getSplitValue()) < 0 ) {
			// choose a node in left subtree
			randomNodeIndex = randInt( 0, (leftSideNodes.size() - 1) );
			randomNode = leftSideNodes.get(randomNodeIndex);

		} else {
			// choose a node in right subtree
			randomNodeIndex = randInt( 0, (rightSideNodes.size() - 1) );
			randomNode = rightSideNodes.get(randomNodeIndex);
		}

		Node<T> startNode = findStartingNode(queryPoint, randomNode);

		Result<T> result = new Result<T>(queryPoint, k, distance);
		result.setEndingNode(root);

		//System.out.println("random node: " + randomNode);
		//System.out.println("starting node: " + startNode);
		//System.out.println("end node: " + result.getEndingNode());
		
		randomNearestNeighbor(startNode, queryPoint, k, result, none);

		return result;
	}

	private void randomNearestNeighbor(Node<T> v, T p, int k, Result<T> result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		randomNN(v, p, k, result);
	}

	private void randomNN(Node<T> v, T p, int k, Result<T> result) {

		//System.out.println(v + " result:" + result + 
		//		" upLeft: " + result.isUpFromLeft() + 
		//		" upRight: " + result.isUpFromRight());
		
		if ( v.isLeaf() ) {

			result.add (v.getBucket());

			if ( mustBeSetParentStatus(v) )  {

				randomNearestNeighbor(v.getParent(), p, k, result, null);
			}

		} else {

			if ( v.getStatus().equals(none) ) {
				//if ( p < v.getSplitValue()) {
				if ( p.compareTo((T) v.getSplitValue()) < 0) {

					v.setStatus(leftVisited); 
					randomNearestNeighbor (v.getLeft(), p, k, result, none);

				} else {

					v.setStatus(rightVisited); 
					randomNearestNeighbor (v.getRight(), p, k, result, none);
				}
			} 

			if ( ! (result.isUpFromLeft() && result.isUpFromRight()) ) {
				
				//System.out.println(v + " condition FALSE result:" + result + 
				//		" upLeft: " + result.isUpFromLeft() + 
				//		" upRight: " + result.isUpFromRight());
				
				/*
				 * potrebbe entrare in rightVisited oppure in leftVisited dopo essere uscito dall'if precedente
				 */
				if ( v.getStatus().equals(rightVisited) ) {

					if ( ! (v.getLeft() == null) && secondChildMustBeVisited(v, p, result) ) {

						randomNearestNeighbor (v.getLeft(), p, k, result, none);
						
					} else {
						
						result.setUpFromLeft(true);
					}
					
					if ( mustBeSetParentStatus(v) )  {

						randomNearestNeighbor(v.getParent(), p, k, result, null);
					}
									} 

				if ( v.getStatus().equals(leftVisited) ) {

					if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, p, result) ) {

						randomNearestNeighbor (v.getRight(), p, k, result, none);
											
					} else {
						
						result.setUpFromRight(true);
					}
					
					if ( mustBeSetParentStatus(v) )  {

						randomNearestNeighbor(v.getParent(), p, k, result, null);
					}
				}
				
			} else {
				
				//System.out.println(v + " condition TRUE result:" + result + 
				//		" upLeft: " + result.isUpFromLeft() + 
				//		" upRight: " + result.isUpFromRight());
				
				if ( result.getEndingNode().equals(root.getSplitValue()) ) {
					result.setEndingNode(v);
				}
			}
		}
		v.setStatus(null);
	}





	public static void main( String [ ] args ){

		Distance<Integer> integerDistance = new IntegerDistance();
		BinaryTree<Integer> tree = new BinaryTree<Integer>(2, integerDistance);
		for (int i = 0; i < 256; i++) {
			tree.insert(i);
		}
		
		//tree.executeFindingStartingNodeTests();
		//tree.executeFindindEndingNodeTests();
		/*
		int queryPoint = 5;
		int k = 3;
		Result<Integer> result = tree.randomNearestQuery(queryPoint, k);
		System.out.println(result);
		*/
	}








	/*
	 * finding starting node
	 */

	public Node<T> findStartingNode(T queryPoint, Node<T> n) {

		if (n.isRoot()) {
			// ROOT; 
			return n;

		} else {
			if (n.isLeaf()) {
				// LEAF;
				// Di una foglia di possono calcolare min e max
				//if ( n.getMinValue() <= p && p <= n.getMaxValue() ) {
				if ( (n.getMinValue().compareTo(queryPoint) <= 0)  && (queryPoint.compareTo((T) n.getMaxValue()) ) <= 0 ) {
					return n; 
				} else { 
					return findStartingNode(queryPoint, n.getParent());
				}

			} else {
				if ( n.getLeft() != null && n.getRight() != null ) {

					//INTERNAL_WITH_BOTH_CHILDREN;
					T leftSplitValue = (T) n.getLeft().getSplitValue();
					T rightSplitValue = (T) n.getRight().getSplitValue();

					if ( leftSplitValue == null || rightSplitValue == null ) {
						System.err.println();
					}
					//if ( leftSplitValue <= p && p <= rightSplitValue ) {
					if ( (leftSplitValue.compareTo(queryPoint) <= 0) && (queryPoint.compareTo(rightSplitValue) <= 0) ) {
						return n;
					} else { 
						return findStartingNode(queryPoint, n.getParent());
					}
				} else {
					if ( n.getRight() != null ) {
						//INTERNAL_WITH_ONLY_RIGHT_CHILD;
						return findStartingNode(queryPoint, n.getParent());
					} else {
						//INTERNAL_WITH_ONLY_LEFT_CHILD;
						return findStartingNode(queryPoint, n.getParent());
					}
				}
			}
		}
	}

	public Node<T> findStartingNodeMinMax(T p, Node<T> n) {

		if (n.isRoot()) {
			// ROOT; 
			return n;

		} else {
			if (n.isLeaf()) {
				// LEAF;
				// Di una foglia di possono calcolare min e max
				//if ( n.getMinValue() <= p && p <= n.getMaxValue() ) { 
				if ( (n.getMinValue().compareTo(p) <= 0)  && (p.compareTo((T) n.getMaxValue()) ) <= 0 ) {
					return n; 
				} else { 
					return findStartingNode(p, n.getParent());
				}

			} else {
				if ( n.getLeft() != null && n.getRight() != null ) {
					//INTERNAL_WITH_BOTH_CHILDREN;
					//if ( n.getMinValue() <= p && p <= n.getMaxValue() ) {
					if ( (n.getMinValue().compareTo(p) <= 0) && (p.compareTo((T) n.getMaxValue()) <= 0) ) {
						return n;
					} else { 
						return findStartingNode(p, n.getParent());
					}
				} else {
					if ( n.getRight() != null ) {
						//INTERNAL_WITH_ONLY_RIGHT_CHILD;
						//if ( n.getRight().getMinValue() <= p && p <= n.getRight().getMaxValue() ) {
						if ( (n.getRight().getMinValue().compareTo(p) <= 0) && (p.compareTo((T) n.getRight().getMaxValue()) <= 0) )  {
							return n.getRight();
						} else {
							return findStartingNode(p, n.getParent());
						}

					} else {
						//INTERNAL_WITH_ONLY_LEFT_CHILD;
						//if ( n.getLeft().getMinValue() <= p && p <= n.getLeft().getMaxValue() ) {
						if ( (n.getLeft().getMinValue().compareTo(p) <= 0) && (p.compareTo((T) n.getLeft().getMaxValue()) <= 0) ) {
							return n.getLeft();
						} else {
							return findStartingNode(p, n.getParent());
						}
					}
				}
			}
		}
	}

	private boolean mustBeSetParentStatus(Node<T> v) {

		// if v.parent.status = NUL then the researh never visited v.parent and then 
		// v.parent.status will be set to the correct value
		// returns true if the status of the parent of v was null  
		if (v.getParent() != null) {

			if (v.getParent().getStatus() == null) {

				//if (v.getSplitValue() < v.getParent().getSplitValue()) {
				if ( v.getSplitValue().compareTo(v.getParent().getSplitValue()) < 0 ) {

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

	public List<Node<T>> getAllNodes() {
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

	public void buildListSide2() {

		/*
		 * questo metodo è necessario perchè ci sta un piccolo errore 
		 * nella costruzione delle liste durante il caricamento dell'albero.
		 * Manca sempre e solo un nodo dalle due liste right 
		 */

		left_leftSideNodes 		= new ArrayList<Node>();
		left_rightSideNodes 	= new ArrayList<Node>();
		right_leftSideNodes 	= new ArrayList<Node>();
		right_rightSideNodes 	= new ArrayList<Node>();

		if ( root.getLeft() != null ) {
			addSubreeToList( root.getLeft().getLeft(),  left_leftSideNodes, Side.LEFT_LEFT);	
			addSubreeToList( root.getLeft().getRight(), left_rightSideNodes, Side.LEFT_RIGHT);
		}

		if ( root.getRight() != null ) {
			addSubreeToList( root.getRight().getLeft(), right_leftSideNodes, Side.RIGHT_LEFT);
			addSubreeToList( root.getRight().getRight(), right_rightSideNodes, Side.RIGTH_RIGHT);	
		}
	}

	private void addSubreeToList(Node<T> node,
			List<Node> right_rightSideNodes2, Side newSide) {

		if ( node != null ){

			node.setSide(newSide);
			right_rightSideNodes2.add(node);

			if ( node.getLeft() != null) {
				addSubreeToList(node.getLeft(), right_rightSideNodes2, newSide);
			}

			if ( node.getRight() != null) {
				addSubreeToList(node.getRight(), right_rightSideNodes2, newSide);
			}
		}
	}




	/*
	 * test functions for ending node
	 */
	private static void executeFindindEndingNodeTests() {

		/*
		 * al variare della dimensione del bucket
		 * al variare del numero di punti (da minNumPunti a maxNumPunti) 
		 * 
		 * calcola le percentuali:
		 * per ogni punto 
		 * 		esegue la nearest query con k che varia da kMin a kMax  
		 */

		int[] bucketSizes 			= {5, 10, 20, 30, 40};
		int minNumPunti				= 512;
		int maxNumPunti				= 1024;	//8.192, 16.384, 32.768
		int kMin						= 3;
		int queryPoint  			= 0;
		int kMax					= 100;
		double numProve				= 0;
		double endingNodeRoot 		= 0;
		double endingNodeNoRoot 	= 0;

		for (int b = 0; b < bucketSizes.length; b++ ) {

			System.out.println("\n\nbucket size: " + bucketSizes [b]);
			Distance<Integer> distance = new IntegerDistance();
			
			for ( int numPunti = minNumPunti; numPunti <= maxNumPunti; ){

				BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSizes [b], distance);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);
				}

				Node<Integer> root =  tree.getRoot();

				System.out.println("\tn = " + numPunti + " root: " + root.getSplitValue());

				for ( queryPoint = 0; queryPoint < numPunti; queryPoint++ ) {

					for ( int k = kMin; k < kMax; k++ ) {
						numProve++;

						Result<Integer> result = tree.nearestQueryWithEndingNode(queryPoint, k);

						if ( result.getEndingNode().equals(root.getSplitValue()) ) {
							endingNodeRoot++;
						} else {
							endingNodeNoRoot++;
						}
						//System.out.println(result.getEndingNode());
					}
				}

				double mediaPercEndingRoot   = (endingNodeRoot/numProve) * 100;
				double mediaPercEndingNoRoot = (endingNodeNoRoot/numProve) * 100;
				System.out.println("\t\t%ending-root: " + Math.round(mediaPercEndingRoot) + " %ending-noRoot: " + Math.round(mediaPercEndingNoRoot) + "\n");

				numPunti = numPunti * 2;
			}
		}
	}




	/*
	 * test functions for starting node
	 */

	private void executeFindingStartingNodeTests() {

		/*
		 * al variare della dimensione del bucket
		 * al variare del numero di punti (da minNumPunti a maxNumPunti) 
		 * 
		 * calcola le percentuali:
		 * per ogni punto 
		 * 		per ogni nodo 
		 * 			cerca lo starting node
		 */

		int[] bucketSizes 			= {5, 10, 20, 30, 40};
		int minNumPunti				= 512;
		int maxNumPunti				= 1024;	//8.192, 16.384, 32.768
		int queryPoint  			= 0;

		for (int b = 0; b < bucketSizes.length; b++ ) {

			System.out.println("\n\nbucket size: " + bucketSizes [b]);

			Distance<Integer> distance = new IntegerDistance();
			for ( int numPunti = minNumPunti; numPunti <= maxNumPunti; ){

				BinaryTree<Integer> tree = new BinaryTree<Integer>(bucketSizes [b], distance);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);
				}
				//tree.buildListSide2();	// da usare per i test SIDE2

				Node<Integer> root = tree.getRoot();
				System.out.println("\tn = " + numPunti + " root: " + root.getSplitValue());

				double sommaPercRoot = 0, sommaPercNoRoot = 0;

				for ( queryPoint = 0; queryPoint < numPunti; queryPoint++ ) {

					//TestResult res = tree.testFSN(tree, queryPoint);
					//TestResult res = tree.testFSNMinMax(tree, queryPoint);
					//TestResult res = tree.testFSNSide(tree, queryPoint);
					TestResult res = tree.testFSNMinMaxSide(tree, queryPoint);

					//TestResult res = tree.testFSNSide2(tree, queryPoint);			//insert2() + buildListSide2()
					//TestResult res = tree.testFSNMinMaxSide2(tree, queryPoint);		//insert2() + buildListSide2()

					sommaPercNoRoot = sommaPercNoRoot + res.percNumNoRoot;
					sommaPercRoot	= sommaPercRoot + res.percNumRoot;
				}

				double mediaPercRoot   = sommaPercRoot/numPunti;
				double mediaPercNoRoot = sommaPercNoRoot/numPunti;
				System.out.println("\t\t%Root: " + Math.round(mediaPercRoot)+ " %noRoot: " + Math.round(mediaPercNoRoot) + "\n");

				numPunti = numPunti * 2;
			}
		}
	}

	public TestResult testFSN(BinaryTree<T> tree, T queryPoint) {

		TestResult r = new TestResult();

		for ( Object n : tree.getAllNodes() ) {
			Node<T>  node = (Node<T>)n; 
			if ( ! node.isRoot() ) {
				Node<T> start = tree.findStartingNode(queryPoint, node);
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

	public TestResult testFSNSide(BinaryTree<T> tree, T queryPoint) {

		TestResult r = new TestResult();
		//if ( queryPoint < root.getSplitValue() ) {
		if ( queryPoint.compareTo(root.getSplitValue()) < 0 ) {
			for ( Object n : tree.leftSideNodes ) {
				Node<T> node = (Node<T>) n;
				if ( ! node.isRoot() ) {
					Node<T> start = tree.findStartingNode(queryPoint, node);
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
			for ( Object n : tree.rightSideNodes ) {
				Node<T> node = (Node<T>) n;
				if ( ! node.isRoot() ) {
					Node<T> start = tree.findStartingNode(queryPoint, node);
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

	public TestResult testFSNMinMax(BinaryTree<T> tree, T queryPoint) {

		TestResult r = new TestResult();

		for ( Object n : tree.getAllNodes() ) {
			Node<T> node = (Node)n;
			if ( ! node.isRoot() ) {
				Node<T> start = tree.findStartingNodeMinMax(queryPoint, node);
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

	public TestResult testFSNMinMaxSide(BinaryTree<T> tree, T queryPoint) {

		TestResult r = new TestResult();
		double numProve = 0;

		//if ( queryPoint < root.getSplitValue() ) {
		if ( queryPoint.compareTo(root.getSplitValue()) < 0 ) {
			for ( Object n : tree.leftSideNodes ) {
				Node<T> node = (Node<T>)n;
				if ( ! node.isRoot() ) {
					Node<T> start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}
			}

		} else {
			for ( Object n : tree.rightSideNodes ) {
				Node<T> node = (Node<T>)n;
				if ( ! node.isRoot() ) {
					Node<T> start = tree.findStartingNodeMinMax(queryPoint, node);
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

	public TestResult testFSNSide2(BinaryTree<T> tree, T queryPoint) {

		TestResult r = new TestResult();
		double numProve = 0;

		//if ( queryPoint < root.getSplitValue() ) {
		if ( queryPoint.compareTo(root.getSplitValue()) < 0 ) {
			// go to left child 
			//if ( queryPoint < root.getLeft().getSplitValue() ) {
			if ( queryPoint.compareTo(root.getLeft().getSplitValue()) < 0 ) {
				for ( Object n : tree.left_leftSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNode(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}

			} else {
				for ( Object n : tree.left_rightSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNode(queryPoint, node);
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
			//if ( queryPoint < root.getRight().getSplitValue() ) {
			if ( queryPoint.compareTo(root.getRight().getSplitValue()) < 0 ) {
				for ( Object n : tree.right_leftSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNode(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}	

			} else {
				for ( Object n : tree.right_rightSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNode(queryPoint, node);
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

	public TestResult testFSNMinMaxSide2(BinaryTree<T> tree, T queryPoint) {

		TestResult r = new TestResult();
		double numProve = 0;

		//if ( queryPoint < root.getSplitValue() ) {
		if ( queryPoint.compareTo(root.getSplitValue()) < 0 ) {
			// go to left child 
			//if ( queryPoint < root.getLeft().getSplitValue() ) {
			if ( queryPoint.compareTo(root.getLeft().getSplitValue()) < 0 ) {

				//System.out.println("left-left");

				for ( Object n : tree.left_leftSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}

			} else {

				//System.out.println("left-right");

				for ( Object n : tree.left_rightSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNodeMinMax(queryPoint, node);
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
			//if ( queryPoint < root.getRight().getSplitValue() ) {
			if ( queryPoint.compareTo(root.getRight().getSplitValue()) < 0 ) {

				//System.out.println("right-left");

				for ( Object n : tree.right_leftSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNodeMinMax(queryPoint, node);
					numProve++;
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				}	

			} else {

				//System.out.println("right-right");

				for ( Object n : tree.right_rightSideNodes ) {
					Node<T> node = (Node<T>)n;
					Node<T> start = tree.findStartingNodeMinMax(queryPoint, node);
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
