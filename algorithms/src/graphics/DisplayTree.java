package graphics;


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

				drawKdTree();
				//drawTree();
				//executeTests2();
				//drawTree();
				//executeTests();
			}

			private void drawTree() {

				int bucketSize = 3;
				int numPunti   = 256;
				BucketBinaryTree tree = new BucketBinaryTree(bucketSize);

				for (int i = 0; i < numPunti; i++) {
					tree.insert(i);	
				}

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

			private void drawKdTree() {

				int bucketSize = 2;
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
				showTree(tree);
				/*
				Point queryPoint = new Point(0, 1);
				int k = 4;
				KdResult result = tree.nearestQuery(queryPoint, k);
				System.out.println();
				*/
				

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

							TestResult r1 = tree.testFindStartingNode(tree, queryPoint);
							TestResult r2 = tree.testFindStartingNodeMinMax(tree, queryPoint);
							TestResult r3 = tree.testFindStartingNodeSide(tree, queryPoint);
							TestResult r4 = tree.testFindStartingNodeMinMaxSide(tree, queryPoint);

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

