package kdTrees;

import graphics.Node;
import graphics.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import binaryTrees.TestResult;
import kdTrees.KdNode.Side;


public class KdTree implements Tree {

	/*
	 * it is used only for tests
	 */
	public List<KdNode> allNodes;
	public List<KdNode> leftSideNodes;
	public List<KdNode> rightSideNodes;


	private KdNode 		root;
	private int 		bucketSize;
	private Distance 	distance;

	private final String none 			= "none";
	private final String leftVisited 	= "leftVisited";
	private final String rightVisited 	= "rightVisited";
	//private final String allVisited 	= "allVisited";

	private QueryStartCondition currentQueryCondition;




	/*
	 * constructor
	 */
	public KdTree(int bucketSize, Distance distance) {

		this.bucketSize 	= bucketSize;
		this.distance		= distance;
	}



	/*
	 * insertions
	 */
	public void bulkLoad(Point[] points, int bucketSize) {

		allNodes 		= new ArrayList<KdNode>();
		leftSideNodes 	= new ArrayList<KdNode>();
		rightSideNodes 	= new ArrayList<KdNode>();

		root = load(points, 0, bucketSize, null);
		root.setRoot(true);
	}

	public void insert(Point newPoint) {

		if ( root == null) {

			allNodes 		= new ArrayList<KdNode>();
			leftSideNodes   = new ArrayList<KdNode>();
			rightSideNodes  = new ArrayList<KdNode>();

			root = new KdNode(newPoint, bucketSize, allNodes);
			root.setRoot(true);

		} else {

			root.setRoot(false);
			root = insert(newPoint, root);
			root.setRoot(true);
		}
	}

	private KdNode load(Point[] points, int dept, int bucketSize, Side side) {

		// Select axis based on depth so that axis cycles through all valid values: returns dept mod k;
		int coord = KdNode.selectSplitCoordinate(points[0].getNumberCoords(), dept); 

		if ( points.length <= bucketSize ) {

			// creates, adds points and sets data of the new leaf node
			KdNode node = new KdNode(bucketSize, allNodes);
			Double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;

			for ( int i = 0; i < points.length; i++) {

				node.add(points[i], bucketSize, allNodes, leftSideNodes, rightSideNodes);
				if ( points[i].get(coord) < min ) {
					min = points[i].get(coord);
				}
				if ( points[i].get(coord) > max ) {
					max = points[i].get(coord);
				}
			}

			// set the side and adds the node to the correct list
			node.setSide(side);
			if ( side.equals(side.LEFT) ) {
				leftSideNodes.add(node);
			} else {
				rightSideNodes.add(node);
			}

			node.setLeaf(true);
			node.setHeight(0);
			node.setSplitCoordinate(coord);
			node.setMinValue(min);
			node.setMaxValue(max);

			return node; 

		} else {

			// choose median as pivot element: select median by axis from pointList;
			int k = (int) points.length/2;
			Selection selection = selectKth(points, coord, k);
			Point median = selection.getPoint();
			Integer index = selection.getIndex();

			// Create node and construct subtrees
			KdNode node = new KdNode(bucketSize, allNodes);
			node.setLeaf(false);

			//node.location := median;
			node.setSplitCoordinate(coord);
			node.setSplitValue(median.get(coord));

			Point[] leftPoints  = new Point[index + 1];
			Point[] rightPoints = new Point[points.length - index - 1];
			for ( int i = 0; i < index + 1; i++) {
				leftPoints[i] = points[i];
			}
			int j = 0;
			for ( int i = index + 1; i < points.length ; i++) {
				rightPoints[j++] = points[i];
			}

			KdNode leftChild  = null;
			KdNode rightChild = null;
			if ( side == null ) {
				leftChild  = load(leftPoints,  dept + 1, bucketSize, Side.LEFT);
				rightChild = load(rightPoints, dept + 1, bucketSize, Side.RIGHT);
			} else {
				leftChild  = load(leftPoints,  dept + 1, bucketSize, side);
				rightChild = load(rightPoints, dept + 1, bucketSize, side);
			}

			node.setLeft (leftChild); 
			node.setRight(rightChild);


			node.setHeight( Math.max(leftChild.getHeight(), rightChild.getHeight()) + 1 );

			// set the side and adds the node to the correct list
			if ( side != null ) {
				node.setSide(side);

				if ( side.equals(side.LEFT) ) {
					leftSideNodes.add(node);
				} else {
					rightSideNodes.add(node);
				}	
			} else {
				System.err.println("Side = NULL");
			}

			node.getRight().setParent(node);
			node.getLeft().setParent(node);

			setMinMax(node);
			return node;
		}
	}

	private void setMinMax(KdNode node) {

		if ( node.getLeft().getLeft() != null ) {
			node.setMinValue( node.getLeft().getLeft().getMinValue() );
		} else {
			Point[] points = node.getLeft().getBucket();
			Double min = Double.MAX_VALUE;
			for (Point p : points) {
				if ( p.get(node.getSplitCoordinate()) < min ) {
					min = p.get(node.getSplitCoordinate());
				}
			}
			node.setMinValue(min);
		}

		if ( node.getRight().getRight() != null ) {
			node.setMaxValue( node.getRight().getRight().getMaxValue() );
		} else {
			Point[] points = node.getRight().getBucket();
			Double max = Double.MIN_VALUE;
			for (Point p : points) {
				if ( p.get(node.getSplitCoordinate()) > max ) {
					max = p.get(node.getSplitCoordinate());
				}
			}
			node.setMaxValue(max);
		}

	}

	private static Selection selectKth(Point[] arr, int coord, int k) {

		if (arr == null || arr.length <= k)
			throw new Error();

		int from = 0, to = arr.length - 1;

		// if from == to we reached the kth element
		while (from < to) {
			int r = from, w = to;
			Double mid = arr[(r + w) / 2].get(coord);

			// stop if the reader and writer meets
			while (r < w) {

				if (arr[r].get(coord) >= mid) { // put the large values at the end
					Point tmp = arr[w];
					arr[w] = arr[r];
					arr[r] = tmp;
					w--;
				} else { // the value is smaller than the pivot, skip
					r++;
				}
			}

			// if we stepped up (r++) we need to step one down
			if (arr[r].get(coord) > mid)
				r--;

			// the r pointer is on the end of the first k elements
			if (k <= r) {
				to = r;
			} else {
				from = r + 1;
			}
		}

		Selection selection = new Selection();
		selection.setPoint(arr[k]);
		selection.setIndex(k);
		//return arr[k];
		return selection;
	}

	private KdNode insert(Point newPoint, KdNode node) {

		System.err.println(node);
		if ( node.isLeaf() ) {

			node.add(newPoint, this.bucketSize, allNodes, leftSideNodes, rightSideNodes);

		} else {

			if ( newPoint.get(node.getSplitCoordinate()) < node.getSplitValue() ) {

				node.setLeft( insert(newPoint, node.getLeft()) );
				node.setHeight( Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);

				node.setMaxValue(node.getRight().getMaxValue());
				node.setMinValue(node.getLeft().getMinValue());

			} else {

				node.setRight( insert(newPoint, node.getRight()) );
				node.setHeight( Math.max(node.getLeft().getHeight(), node.getRight().getHeight()) + 1);

				node.setMaxValue(node.getRight().getMaxValue());
				node.setMinValue(node.getLeft().getMinValue());

			}	 
		}
		return node;
	}



	/*
	 * classic queries
	 */

	private void nearestNeighbor(KdNode v, Point queryPoint, int k, KdResult result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		//System.out.println("nearestNeighbor: " + v + " v.status:" + v.getStatus() + " status:" + status);

		NN(v, queryPoint, k, result);
	}

	private void NN(KdNode v, Point queryPoint, int k, KdResult result) {

		if ( v.isLeaf() ) {

			result.add (v.getBucket());
			return; 

		} else {

			if ( v.getStatus().equals(none) && !(v.isLeaf()) ) {
				if ( queryPoint.get(v.getSplitCoordinate()) < v.getSplitValue()) {
					v.setStatus(leftVisited); 
					nearestNeighbor (v.getLeft(), queryPoint, k, result, none);
				} else {
					v.setStatus(rightVisited); 
					nearestNeighbor (v.getRight(), queryPoint, k, result, none);
				}
			} 
			/*
			 * potrebbe entrare in rightVisited oppure in leftVisited dopo essere uscito dall'if precedente
			 */
			if ( v.getStatus().equals(rightVisited) ) {

				if ( ! (v.getLeft() == null) && secondChildMustBeVisited(v, queryPoint, result) ) {
					//v.setStatus(allVisited); 
					nearestNeighbor (v.getLeft(), queryPoint, k, result, none);
				}
			} 

			if ( v.getStatus().equals(leftVisited) ) {

				if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, queryPoint, result) ) {
					//v.setStatus(allVisited); 
					nearestNeighbor (v.getRight(), queryPoint, k, result, none);
				}
			}
		}

		// l'elaborazione del nodo corrente è finita
		v.setStatus(null);
	}

	public KdResult nearestQuery(Point queryPoint, int k) {

		KdResult result = new KdResult(queryPoint, k, this.distance);
		nearestNeighbor(root, queryPoint, k, result, none);

		return result;
	}



	/*
	 * random queries
	 */

	private void randomNearestNeighbor(KdNode v, Point queryPoint, int k, KdResult result, String status) {

		if ( !(status == null) ) {
			v.setStatus(status);
		}

		//System.out.println("randomNearestNeighbor: " + v + " v.status:" + v.getStatus() + " status:" + status);

		randomNN(v, queryPoint, k, result);
	}

	private void randomNN(KdNode v, Point queryPoint, int k, KdResult result) {

		//System.out.println(v + " " + v.getStatus());

		if ( v.isLeaf() ) {

			result.add (v.getBucket());

			if ( mustBeSetParentStatus(v) )  {

				randomNearestNeighbor(v.getParent(), queryPoint, k, result, null);
			}
			//return; 

		} else {

			if ( v.getStatus().equals(none) && !(v.isLeaf()) ) {
				if ( queryPoint.get(v.getSplitCoordinate()) < v.getSplitValue()) {
					v.setStatus(leftVisited); 
					randomNearestNeighbor (v.getLeft(), queryPoint, k, result, none);
				} else {
					v.setStatus(rightVisited); 
					randomNearestNeighbor (v.getRight(), queryPoint, k, result, none);
				}
			} 
			/*
			 * potrebbe entrare in rightVisited oppure in leftVisited dopo essere uscito dall'if precedente
			 */
			if ( v.getStatus().equals(rightVisited) ) {

				if ( ! (v.getLeft() == null) && secondChildMustBeVisited(v, queryPoint, result) ) {
					randomNearestNeighbor (v.getLeft(), queryPoint, k, result, none);
				} 

				// do the following even if the left child is not visited 
				if ( mustBeSetParentStatus(v) )  {

					randomNearestNeighbor(v.getParent(), queryPoint, k, result, null);
				}
			} 

			if ( v.getStatus() == null ) {
				System.out.println(" v.status è NULL");
			}
			
			if ( v.getStatus().equals(leftVisited) ) { //null?

				if ( ! (v.getRight() == null) && secondChildMustBeVisited(v, queryPoint, result) ) {

					randomNearestNeighbor (v.getRight(), queryPoint, k, result, none);
				}

				// do the following even if the right child is not visited 
				if ( mustBeSetParentStatus(v) )  {

					randomNearestNeighbor(v.getParent(), queryPoint, k, result, null);
				}
			}
		}
		v.setStatus(null);
	}

	public KdResult randomNearestQuery(Point queryPoint, int k, 
			boolean useMinMax, boolean chooseBySide) {

		KdNode randomNode = null;

		if ( chooseBySide ) {
			
			randomNode = chooseRandomNodeBySide(queryPoint);			
		
		} else {
		
			randomNode = chooseRandomNode();
		}
		
		KdResult result = null;

		if ( randomNode != null ) {
			result = startQueryFromRandomNode(randomNode, queryPoint, k, useMinMax);	
		} else {
			// opss...
			System.err.println(" random node NULL");
		}

		return result;
	}

	private KdNode chooseRandomNode() {

		// choose a random node among all nodes

		int randomNodeIndex = 0;
		KdNode randomNode = null;

		randomNodeIndex = randInt( 0, (allNodes.size() - 1) );
		randomNode = allNodes.get(randomNodeIndex);
		while (randomNode.isRoot()) {
			randomNode = allNodes.get(randInt(0, allNodes.size()));
		}

		return randomNode;
	}

	private KdResult startQueryFromRandomNode(KdNode randomNode,
			Point queryPoint, int k, boolean useMinMax) {

		//System.out.println(randomNodeIndex);
		//System.out.println("random: " + randomNode);

		KdNode startNode = null;

		if ( useMinMax ) {
			startNode = findStartingNodeMinMax(queryPoint, randomNode);
		} else {
			startNode = findStartingNode(queryPoint, randomNode);
		}

		//System.out.println("start: " + startNode);
		KdResult result = null;

		if ( startNode != null ) {
			result = new KdResult(queryPoint, k, this.distance);
			randomNearestNeighbor(startNode, queryPoint, k, result, none);

		} else {
			//opss...
			System.err.println(" starting node NULL ");
		}

		return result;

	}

	private KdNode chooseRandomNodeBySide(Point queryPoint) {

		// choose a random node in the subtree (side) containing (eventually) the query point

		int randomNodeIndex = 0;
		KdNode randomNode = null;


		if ( queryPoint.get(root.getSplitCoordinate()) < root.getSplitValue() ) {
			// choose a node in left subtree
			randomNodeIndex = randInt( 0, (leftSideNodes.size() - 1) );
			randomNode = leftSideNodes.get(randomNodeIndex);

		} else {
			// choose a node in right subtree
			randomNodeIndex = randInt( 0, (rightSideNodes.size() - 1) );
			randomNode = rightSideNodes.get(randomNodeIndex);
		}

		return randomNode;
	}

	private boolean mustBeSetParentStatus(KdNode v) {

		// if v.parent.status = NUL then the research never visited v.parent and then 
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




	/*
	 * queries common functions
	 */

	private boolean secondChildMustBeVisited(KdNode v, Point queryPoint, KdResult result) {

		/*
		 * if the result contains less than k point the second child must be always visited
		 */
		if ( ! result.isFull() ) {

			return true;

		} else {
			if ( Math.abs( queryPoint.get(v.getSplitCoordinate()) - v.getSplitValue() ) 
					< distance.get(queryPoint, result.getFarthest()) ) {

				return true; 	//Descend other sibling also
			}

			else {
				return false; 	//Do not descend 
			}
		}
	}




	/*
	 * find starting node for query
	 */

	public KdNode findStartingNode(Point queryPoint, KdNode n) {

		if ( currentQueryCondition == null ) {
			currentQueryCondition = new QueryStartCondition(queryPoint.getNumberCoords());	
		}

		if (n.isRoot()) {
			// ROOT; 
			return n;

		} else {
			if (n.isLeaf()) {
				// LEAF;

				// By construction, a leaf always has min e max values 
				if ( n.getMinValue() <= queryPoint.get(n.getSplitCoordinate()) 
						&& queryPoint.get(n.getSplitCoordinate()) <= n.getMaxValue() ) { 

					// the condition is verified for current coordinate
					currentQueryCondition.setVerifiedCoord(n.getSplitCoordinate());

					if ( currentQueryCondition.isVerified() ) {

						return n;	

					} else {

						return findStartingNode(queryPoint, n.getParent());
					}

				} else { 
					return findStartingNode(queryPoint, n.getParent());
				}

			} else {
				if ( n.getLeft() != null && n.getRight() != null ) {

					//INTERNAL_WITH_BOTH_CHILDREN;
					Double leftSplitValue = n.getLeft().getSplitValue();
					Double rightSplitValue = n.getRight().getSplitValue();

					if ( leftSplitValue <= queryPoint.get(n.getSplitCoordinate()) 
							&& queryPoint.get(n.getSplitCoordinate()) <= rightSplitValue ) { 

						// the condition is verified for current coordinate
						currentQueryCondition.setVerifiedCoord(n.getSplitCoordinate());

						if ( currentQueryCondition.isVerified() ) {

							return n;	

						} else {

							return findStartingNode(queryPoint, n.getParent());
						}

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

	public KdNode findStartingNodeMinMax(Point queryPoint, KdNode n) {

		if (n.isRoot()) {
			// ROOT; 
			return n;

		} else {
			if (n.isLeaf()) {
				// LEAF;
				// Di una foglia di possono calcolare min e max
				if ( n.getMinValue() <= queryPoint.get(n.getSplitCoordinate()) 
						&& queryPoint.get(n.getSplitCoordinate()) <= n.getMaxValue() ) { 
					return n; 
				} else { 
					return findStartingNode(queryPoint, n.getParent());
				}

			} else {
				if ( n.getLeft() != null && n.getRight() != null ) {
					//INTERNAL_WITH_BOTH_CHILDREN;
					if ( n.getMinValue() <= queryPoint.get(n.getSplitCoordinate()) 
							&& queryPoint.get(n.getSplitCoordinate()) <= n.getMaxValue() ) { 
						return n;
					} else { 
						return findStartingNode(queryPoint, n.getParent());
					}
				} else {
					if ( n.getRight() != null ) {
						//INTERNAL_WITH_ONLY_RIGHT_CHILD;
						if ( n.getRight().getMinValue() <= queryPoint.get(n.getSplitCoordinate()) 
								&& queryPoint.get(n.getSplitCoordinate()) <= n.getRight().getMaxValue() ) { 
							return n.getRight();
						} else {
							return findStartingNode(queryPoint, n.getParent());
						}

					} else {
						//INTERNAL_WITH_ONLY_LEFT_CHILD;
						if ( n.getLeft().getMinValue() <= queryPoint.get(n.getSplitCoordinate()) 
								&& queryPoint.get(n.getSplitCoordinate()) <= n.getLeft().getMaxValue() ) { 
							return n.getLeft();
						} else {
							return findStartingNode(queryPoint, n.getParent());
						}
					}
				}
			}
		}
	}





	/*
	 * test functions
	 */

	public TestResult testFindStartingNode(KdTree tree, Point queryPoint) {

		TestResult r = new TestResult();

		for ( KdNode node : tree.getAllNodes() ) {
			r.totElabs++;

			if ( ! node.isRoot() ) {
				KdNode start = tree.findStartingNode(queryPoint, node);

				if ( start.isRoot() ) {
					r.numRoot++;
				} else {
					r.numNoRoot++;
				}	
			} else {
				r.skippedElabs++;
			}
		}

		r.calculatePercentages();
		return r;
	}

	public TestResult testFindStartingNodeSide(KdTree tree, Point queryPoint) {

		TestResult r = new TestResult();
		if ( queryPoint.get(root.getSplitCoordinate()) < root.getSplitValue() ) {

			for ( KdNode node : tree.leftSideNodes ) {
				r.totElabs++;

				if ( ! node.isRoot() ) {
					KdNode start = tree.findStartingNode(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				} else {
					r.skippedElabs++;
				}
			}	

		} else {
			for ( KdNode node : tree.rightSideNodes ) {
				r.totElabs++;

				if ( ! node.isRoot() ) {
					KdNode start = tree.findStartingNode(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				} else {
					r.skippedElabs++;
				}
			}

		}

		r.calculatePercentages();
		return r;
	}

	public TestResult testFindStartingNodeMinMax(KdTree tree, Point queryPoint) {

		TestResult r = new TestResult();

		for ( KdNode node : tree.getAllNodes() ) {
			r.totElabs++;

			if ( ! node.isRoot() ) {
				KdNode start = tree.findStartingNodeMinMax(queryPoint, node);
				if ( start.isRoot() ) {
					r.numRoot++;
				} else {
					r.numNoRoot++;
				}	
			} else {
				r.skippedElabs++;
			}
		}	
		r.calculatePercentages();

		return r;
	}

	public TestResult testFindStartingNodeMinMaxSide(KdTree tree, Point queryPoint) {

		TestResult r = new TestResult();

		if ( queryPoint.get(root.getSplitCoordinate()) < root.getSplitValue() ) {
			for ( KdNode node : tree.leftSideNodes ) {
				r.totElabs++;

				if ( ! node.isRoot() ) {
					KdNode start = tree.findStartingNodeMinMax(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				} else {
					r.skippedElabs++;
				}
			}	

		} else { 
			for ( KdNode node : tree.rightSideNodes ) {
				r.totElabs++;

				if ( ! node.isRoot() ) {
					KdNode start = tree.findStartingNodeMinMax(queryPoint, node);
					if ( start.isRoot() ) {
						r.numRoot++;
					} else {
						r.numNoRoot++;
					}	
				} else {
					r.skippedElabs++;
				}
			}
		}

		r.calculatePercentages();
		return r;
	}

	public static void main( String [ ] args ){

		int bucketSize  = 3;
		int dimensions	= 2;

		KdTree tree = new KdTree(bucketSize, new EuclideanDistance());

		int maxX = 3, maxY = 3;

		for (Double i = (double) 1; i < maxX; i++) {
			for ( Double j = (double) 0; j < maxY; j++) {
				Point p = new Point(dimensions);
				p.set(0, i);
				p.set(1, j);

				tree.insert(p);
			}
		}

		Point queryPoint = new Point(dimensions);
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




	/* 
	 * getters and setters
	 */

	public List<KdNode> getAllNodes() {
		return allNodes;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(KdNode root) {
		this.root = root;
	}


	
	/*
	 * test service methods
	 */
	public static KdTree buildSampleKdTree(int bucketSize, int maxCoordValue) {
		
		/*
		 * kd tree parameters and creation
		 */
		KdTree tree = new KdTree(bucketSize, new EuclideanDistance());

		Point[] points = new Point[maxCoordValue * maxCoordValue];

		for (int i = 0; i < maxCoordValue; i++) {
			for (int j = 0; j < maxCoordValue; j++) {
				Point p = new Point( (double)i, (double)j);
				points[j + maxCoordValue*i] = p;
			}		
		}

		tree.bulkLoad(points, bucketSize);
		
		return tree;		
	}


	/*
	 * service functions
	 */

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
