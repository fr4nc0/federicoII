package graphics;


import static org.junit.Assert.assertArrayEquals;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import avlTrees.AvlNode;
import avlTrees.AvlTree;
import binaryTrees.BucketBinaryTree;
import binaryTrees.BucketNode;
import binaryTrees.Result;
import binaryTrees.TestResult;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.peer.SystemTrayPeer;

import kdTrees.EuclideanDistance;
import kdTrees.KdResult;
import kdTrees.KdTree;
import kdTrees.Point;

public class DisplayTree extends JPanel implements TreeSelectionListener {

	private JEditorPane htmlPane;
	private JTree tree;
	private URL helpURL;
	private static boolean DEBUG = false;

	//Optionally play with line styles.  Possible values are
	//"Angled" (the default), "Horizontal", and "None".
	private static boolean playWithLineStyle = false;
	private static String lineStyle = "Horizontal";

	//Optionally set the look and feel.
	private static boolean useSystemLookAndFeel = false;

	public DisplayTree(Tree theTree) {
		super(new GridLayout(1,0));

		Node TheRoot = theTree.getRoot();

		DefaultMutableTreeNode root =
				new DefaultMutableTreeNode( TheRoot.getLabelNode()  );

		addNodes(root, TheRoot);

		//Create a tree that allows one selection at a time.
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		tree.addTreeSelectionListener(this);

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			tree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it. 
		JScrollPane treeView = new JScrollPane(tree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);

		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		//htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(700); 
		splitPane.setPreferredSize(new Dimension(500, 700));

		//Add the split pane to this panel.
		add(splitPane);
	}

	public DisplayTree(AvlTree avlTree) {
		super(new GridLayout(1,0));

		AvlNode avlRoot = avlTree.getRoot();

		DefaultMutableTreeNode root =
				new DefaultMutableTreeNode( avlRoot.getLabelNode()  );

		addNodesAVL(root, avlRoot);

		//Create a tree that allows one selection at a time.
		tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode
		(TreeSelectionModel.SINGLE_TREE_SELECTION);

		//Listen for when the selection changes.
		tree.addTreeSelectionListener(this);

		if (playWithLineStyle) {
			System.out.println("line style = " + lineStyle);
			tree.putClientProperty("JTree.lineStyle", lineStyle);
		}

		//Create the scroll pane and add the tree to it. 
		JScrollPane treeView = new JScrollPane(tree);

		//Create the HTML viewing pane.
		htmlPane = new JEditorPane();
		htmlPane.setEditable(false);

		JScrollPane htmlView = new JScrollPane(htmlPane);

		//Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(htmlView);

		Dimension minimumSize = new Dimension(100, 50);
		//htmlView.setMinimumSize(minimumSize);
		treeView.setMinimumSize(minimumSize);
		splitPane.setDividerLocation(400); 
		splitPane.setPreferredSize(new Dimension(500, 300));

		//Add the split pane to this panel.
		add(splitPane);
	}

	private void addNodes(DefaultMutableTreeNode node, Node bucketNode) {

		Node bucketLeftChild = bucketNode.getLeft(); 
		if ( bucketLeftChild != null ) {

			DefaultMutableTreeNode leftChild = new DefaultMutableTreeNode(bucketLeftChild.getLabelNode());
			node.add(leftChild);
			addNodes(leftChild, bucketLeftChild);	
		}

		Node bucketRightChild = bucketNode.getRight(); 
		if ( bucketRightChild != null ) {

			DefaultMutableTreeNode rightChild = new DefaultMutableTreeNode(bucketRightChild.getLabelNode());
			node.add(rightChild);
			addNodes(rightChild, bucketRightChild);	
		}
	}

	private void addNodesAVL(DefaultMutableTreeNode node, AvlNode avlNode) {

		AvlNode avlLeftChild = avlNode.getLeft(); 
		if ( avlLeftChild != null ) {

			DefaultMutableTreeNode leftChild = new DefaultMutableTreeNode(avlLeftChild.getLabelNode());
			node.add(leftChild);
			addNodesAVL(leftChild, avlLeftChild);	
		}

		AvlNode avlRightChild = avlNode.getRight(); 
		if ( avlRightChild != null ) {

			DefaultMutableTreeNode rightChild = new DefaultMutableTreeNode(avlRightChild.getLabelNode());
			node.add(rightChild);
			addNodesAVL(rightChild, avlRightChild);	
		}
	}

	private static void showTree(Tree tree) {

		if (useSystemLookAndFeel) {
			try {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.err.println("Couldn't use system look and feel.");
			}
		}

		//Create and set up the window.
		JFrame frame = new JFrame("Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new DisplayTree(tree));

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	private static void showTreeAVL(AvlTree tree) {

		if (useSystemLookAndFeel) {
			try {
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.err.println("Couldn't use system look and feel.");
			}
		}

		//Create and set up the window.
		JFrame frame = new JFrame("Tree");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new DisplayTree(tree));

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				//executeCorrectnessTests();
				
				//testStartingNodeKdTree();
				//preliminaryTestRandomQueriesCorrectness();

				drawTree();
				//executeTests2();
				//drawTree();
				//executeTests();
			}

			private void drawTree() {

				int bucketSize = 3;
				int numPunti   = 8;
				BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

				for (int i = 0; i < numPunti; i++) {
					//tree.insert(i);
					tree.insert2(i);
				}

				/*
				System.out.println("All: " + tree.allNodes.size() + 
						" L: " + tree.leftSideNodes.size() + 
						" R: " + tree.rightSideNodes.size());
				*/
				
				System.out.println("All: " + tree.allNodes.size() + 
						" LL: " + tree.left_leftSideNodes.size() +
						" LR: " + tree.left_rightSideNodes.size() + 
						" RL: " + tree.right_leftSideNodes.size() + 
						" RR: " + tree.right_rightSideNodes.size());
				
				showTree(tree);

				/*
				System.out.println("Right nodes");
				for (BucketNode n : tree.rightSideNodes) {
					System.out.println(n);
				}
				System.out.println("Left nodes");
				for (BucketNode n : tree.leftSideNodes) {
					System.out.println(n);
				}
				 */
			}

			private void testStartingNodeKdTree() {

				int bucketSize 		= 2;
				int maxCoordValue   = 5;

				KdTree tree = KdTree.buildSampleKdTree(bucketSize, maxCoordValue);

				/*
				 * display the kd tree
				 */
				showTree(tree);

				/*
				 * query info
				 */
				Point queryPoint = new Point(2, 1);
				int k = 5;

				/*
				 * test the starting query node selection using 4 different ways
				 */
				TestResult tr = null;
				tr = tree.testFindStartingNode(tree, queryPoint);
				tr.printResults("BASE:");

				tr = tree.testFindStartingNodeSide(tree, queryPoint);
				tr.printResults("SIDE:");

				tr = tree.testFindStartingNodeMinMax(tree, queryPoint);
				tr.printResults("MINMAX");

				tr = tree.testFindStartingNodeMinMaxSide(tree, queryPoint);
				tr.printResults("SIDE-MINMAX");
			}

			private void preliminaryTestRandomQueriesCorrectness() {

				int bucketSize 		= 2;
				int maxCoordValue   = 5;

				KdTree tree = KdTree.buildSampleKdTree(bucketSize, maxCoordValue);

				/*
				 * display the kd tree
				 */
				showTree(tree);

				/*
				 * query info
				 */
				Point queryPoint = new Point(2, 1);
				int k = 5;


				KdResult result = tree.nearestQuery(queryPoint, k);
				System.out.println(result);

				KdResult resultRandom = tree.randomNearestQuery
						(queryPoint, k, true, false);
				System.out.println(resultRandom);



			}

			private void executeCorrectnessTests() {
				/*
				 * parametri del kd tree
				 */
				int bucketSize = 0;
				int maxCoordValue   = 5;

				for ( bucketSize = 2; bucketSize < 3; bucketSize ++) {
					for ( maxCoordValue = 5; maxCoordValue < 6; maxCoordValue++ ) {

						/*
						 * creazione kd tree
						 */
						KdTree tree = KdTree.buildSampleKdTree(bucketSize, maxCoordValue);


						/*
						 * parametri della query
						 */
						int maxK = 5;
						int minCoordValue = maxCoordValue -1;
						int XqueryPointCood = 0, YQueryPointCoord = 0;

						for ( XqueryPointCood = minCoordValue; XqueryPointCood < maxCoordValue; XqueryPointCood++) {
							for ( YQueryPointCoord = minCoordValue; YQueryPointCoord < maxCoordValue; YQueryPointCoord++) {

								/*
								 * costruzione della query
								 */
								Point queryPoint = new Point(XqueryPointCood, YQueryPointCoord);


								/*
								 * manca: ciclo su tutti i nodi come random node
								 */
								for ( int k = 1; k <= maxK; k++) {

									System.out.println(" query: k=" + k + " query point=" + queryPoint);
									/*
									 * esecuzione delle query
									 */
									KdResult result = tree.nearestQuery(queryPoint, k);
									Point[] sortedResults = getSortedArray(result);
									System.out.println("OK " + result);
									
									// NO MinMax - NO Side
									KdResult resultRandomNOMinMaxNOSide = 
											tree.randomNearestQuery(queryPoint, k, false, false);
									Point[] sortedResultNOMinMaxNOSide = 
											getSortedArray(resultRandomNOMinMaxNOSide);
									System.out.println("OK " + resultRandomNOMinMaxNOSide);
									
									
									// MinMax - NO Side 
									KdResult resultRandomMinMaxNOSide = 
											tree.randomNearestQuery(queryPoint, k, true, false);
									Point[] sortedResultMinMaxNOSide = 
											getSortedArray(resultRandomMinMaxNOSide);
									System.out.println("OK" + resultRandomMinMaxNOSide);
									
									
									// NO MinMax - Side
									KdResult resultRandomNOMinMaxSide = 
											tree.randomNearestQuery(queryPoint, k, false, true);
									Point[] sortedResultNOMinMaxSide = 
											getSortedArray(resultRandomNOMinMaxSide);
									System.out.println("OK" + resultRandomNOMinMaxSide);
									
									// MinMax - Side
									KdResult resultRandomMinMaxSide = 
											tree.randomNearestQuery(queryPoint, k, true, true);
									Point[] sortedResultMinMaxSide = 
											getSortedArray(resultRandomMinMaxSide);
									System.out.println("OK" + resultRandomMinMaxSide);


									if ( ! isEqual(sortedResults, sortedResultNOMinMaxNOSide)) {
										System.err.println(" errore: no MinMax no side");
									}
									if ( ! isEqual(sortedResults, sortedResultMinMaxNOSide)) {
										System.err.println(" errore: MinMax no side");
									}
									if ( ! isEqual(sortedResults, sortedResultNOMinMaxSide)) {
										System.err.println(" errore: no MinMax side");
									}
									if ( ! isEqual(sortedResults, sortedResultMinMaxSide)) {
										System.err.println(" errore: MinMax side");
									}

								}
							}
						}
					}
				}
			}

			private boolean isEqual(Point[] a1,	Point[] a2) {

				if ( a1.length != a2.length ) {
					return false;
				}
				
				for ( int i = 0; i < a1.length; i++) {
					if ( ! a1[i].equals(a2[i]) ) {
						return false;
					}
				}
				
				return true;
			}

			private Point[] getSortedArray(KdResult result) {

				Point[] res = result.getPoints();

				Arrays.sort(res, new Comparator<Point>() {

					public int compare(Point point1, Point point2) {
						Double x1 = point1.get(0);
						Double x2 = point2.get(0);

						if ( ! x1.equals(x2) ) {

							// sort by x-coord only
							return x1.compareTo(x2);

						} else {

							// x-coord are equals the sort by y-coord
							Double y1 = point1.get(1);
							Double y2 = point2.get(1);

							return y1.compareTo(y2);
						}
					}
				});

				return res;
			}	


			private void executeTests2() {

				int bucketSize = 0;
				int numPunti   = 0;

				for ( bucketSize = 3; bucketSize < 4; bucketSize ++) {
					for ( numPunti = 16; numPunti < 17; numPunti++ ) {

						BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

						for (int i = 0; i < numPunti; i++) {
							tree.insert(i);	
						}

						int k = 3;
						int queryPoint   = 0;

						for ( queryPoint = 2; queryPoint < 3; queryPoint++) {

							Result result = tree.nearestQuery(queryPoint, k);
							Result result2 = tree.randomNearestQuery(queryPoint, k);

							Integer[] tmp  = result.getPoints();
							Integer[] tmp2 = result2.getPoints();

							Arrays.sort(tmp);
							Arrays.sort(tmp2);

							System.out.println();
						}
					}					
				}				
			}

			private void executeTests() {

				int bucketSize = 0;
				int numPunti   = 0;

				for ( bucketSize = 3; bucketSize < 5; bucketSize ++) {
					for ( numPunti = 128; numPunti < 150; numPunti++ ) {

						BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

						for (int i = 0; i < numPunti; i++) {
							tree.insert(i);	
						}

						int queryPoint   = 0;
						Integer numTests = 0;
						Double totPercRoot         = (double) 0, totPercRootMinMax         = (double) 0;
						Double totPercNoRoot       = (double) 0, totPercNoRootMinMax       = (double) 0;
						Double kAvgPercRoot        = (double) 0, kAvgPercRootMinMax        = (double) 0;
						Double kAvgPercNoRoot  	   = (double) 0, kAvgPercNoRootMinMax  	   = (double) 0;
						Double totPercRootSide     = (double) 0, totPercRootMinMaxSide     = (double) 0;
						Double totPercNoRootSide   = (double) 0, totPercNoRootMinMaxSide   = (double) 0;
						Double kAvgPercRootSide    = (double) 0, kAvgPercRootMinMaxSide    = (double) 0;
						Double kAvgPercNoRootSide  = (double) 0, kAvgPercNoRootMinMaxSide  = (double) 0;


						for ( queryPoint = 0; queryPoint < numPunti; queryPoint++) {

							TestResult r1 = tree.testFSN(tree, queryPoint);
							TestResult r2 = tree.testFSNMinMax(tree, queryPoint);
							TestResult r3 = tree.testFSNSide(tree, queryPoint);
							TestResult r4 = tree.testFSNMinMaxSide(tree, queryPoint);

							numTests++;

							totPercRoot			= totPercRoot         + r1.percNumRoot;
							totPercNoRoot		= totPercNoRoot       + r1.percNumNoRoot;
							totPercRootMinMax	= totPercRootMinMax   + r2.percNumRoot;
							totPercNoRootMinMax	= totPercNoRootMinMax + r2.percNumNoRoot;

							totPercRootSide			= totPercRootSide         + r3.percNumRoot;
							totPercNoRootSide		= totPercNoRootSide       + r3.percNumNoRoot;
							totPercRootMinMaxSide	= totPercRootMinMaxSide   + r4.percNumRoot;
							totPercNoRootMinMaxSide	= totPercNoRootMinMaxSide + r4.percNumNoRoot;
						}

						kAvgPercRoot 		 = totPercRoot 		   / numTests;
						kAvgPercNoRoot       = totPercNoRoot 	   / numTests;
						kAvgPercRootMinMax   = totPercRootMinMax   / numTests;
						kAvgPercNoRootMinMax = totPercNoRootMinMax / numTests;

						kAvgPercRootSide 		 = totPercRootSide 		   / numTests;
						kAvgPercNoRootSide       = totPercNoRootSide 	   / numTests;
						kAvgPercRootMinMaxSide   = totPercRootMinMaxSide   / numTests;
						kAvgPercNoRootMinMaxSide = totPercNoRootMinMaxSide / numTests;

						System.out.println(bucketSize + ";" + kAvgPercNoRoot + ";" + kAvgPercNoRootMinMax );
						System.out.println(bucketSize + ";" + kAvgPercNoRootSide + ";" + kAvgPercNoRootMinMaxSide );
						System.out.println();

					}					
				}				
			}
		});
	}


	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}
}

