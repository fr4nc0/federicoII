package binaryTrees;

import graphics.Node;

import java.util.Arrays;
import java.util.List;

import org.omg.PortableServer.POA;

public class BucketNode implements Node {

	private BucketNode 	left;
	private BucketNode 	right;
	private BucketNode 	parent;
	private int 		splitValue;
	private int[] 		bucket;
	private int 		index;
	private boolean 	leaf;
	private int 		height;
	private String 		status;
	private boolean		root;
	private int 		minValue;
	private int			maxValue;
	private Side		side; 

	public enum Side { LEFT, RIGHT, NONE, LEFT_LEFT, LEFT_RIGHT, RIGHT_LEFT, RIGTH_RIGHT};

	public BucketNode(int value, int bucketSize, List<BucketNode> allNodes) {

		this.leaf 			= true;
		this.bucket 		= new int[bucketSize];
		this.index 			= 0;
		this.bucket[index] 	= value;	
		this.height 		= 0;
		this.minValue 		= value;
		this.maxValue 		= value;
		this.side			= Side.NONE;

		allNodes.add(this);
	}

	public BucketNode(int bucketSize, List<BucketNode> allNodes) {

		this.leaf 		= true;
		this.bucket 	= new int[bucketSize];
		this.index 		= -1;
		this.height 	= 0;
		this.minValue	= Integer.MAX_VALUE;
		this.maxValue	= Integer.MIN_VALUE;

		allNodes.add(this);
	}

	public String getLabelNode() {
		String label = "";
		if ( this.isLeaf() ) {
			label = this.getBucketAsString() + " " + this.side + " " + this.minValue + " " + this.maxValue + " h:" + this.getHeight(); // + " " + this.status ;
		} else {
			label = String.valueOf( this.splitValue ) + " " + this.side + " " + this.minValue + " " + this.maxValue + " h:" + this.getHeight(); // + " " + this.status;
		}
		return label ;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int[] getBucket() {
		/*
		 * note: a copy of the elements must be returned (even if not is not an object, it is a good practice)  
		 */
		int numPoints = this.index + 1;

		int[] res = new int[numPoints];
		for (int i = 0; i < numPoints; i++) {
			res[i] = this.bucket[i];
		}
		return res;
	}



	@Override
	public String toString() {
		if (this.isLeaf()) {
			return "bucket=" + getBucketAsString() + " " + status + side;
		} else {
			return String.valueOf(splitValue)  + " " + status + side;	
		}

	}

	public String getBucketAsString() {
		String s = "";
		if ( this.bucket != null ) {
			s = "[";
			for (int i = 0; i <=index; i++) {
				s = s + " " + String.valueOf(this.bucket[i]) + " ";
			}
			s = s+ "]";	
		}

		return s;
	}

	public BucketNode getLeft() {
		return left;
	}

	public void setLeft(BucketNode left) {
		this.left = left;
	}

	public BucketNode getRight() {
		return right;
	}

	public void setRight(BucketNode right) {
		this.right = right;
	}

	public BucketNode getParent() {
		return parent;
	}

	public void setParent(BucketNode parent) {
		this.parent = parent;
	}

	public int getSplitValue() {
		
		if ( ! this.isLeaf() ) {
			
			return splitValue;
		
		} else {
			// the split value of a left child leaf is the minimum in the bucket
			// otherwise it is the maximum value in the bucket
			
			int[] tmp = new int[index + 1];
			System.arraycopy( this.bucket, 0, tmp, 0, index + 1 );
			Arrays.sort(tmp);
			
			if ( this.bucket[index] < this.getParent().getSplitValue() ) {
				// it's a left child
				return tmp[0];
				
			} else {
				//it's a right child
				return tmp[index];
			}
		}
			
			
	}

	public boolean isLeaf() {
		return this.leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void add(int newValue, int bucketSize, 
			List<BucketNode> allNodes, 
			List<BucketNode> leftSideNodes,
			List<BucketNode> rightSideNodes) {

		if ( index < bucket.length - 1 ) {

			index++;
			bucket[index] = newValue;

			if ( newValue > maxValue ) {
				maxValue = newValue;
			}
			if ( newValue < minValue ) {
				minValue = newValue;
			}

		} else {

			/*
			 * bucket full: creates two new nodes
			 */

			BucketNode newLeft = new BucketNode(bucketSize, allNodes);
			BucketNode newRight = new BucketNode(bucketSize, allNodes);
			newLeft.setParent(this);
			newRight.setParent(this);

			assignSide(this, newLeft, newRight, leftSideNodes, rightSideNodes);

			int[] tmp = new int[bucketSize + 1];
			System.arraycopy( this.bucket, 0, tmp, 0, this.bucket.length );
			tmp[tmp.length-1] = newValue;
			Arrays.sort(tmp);

			int half = (int) Math.ceil(tmp.length/2);
			this.splitValue = tmp[half];

			for (int i = 0; i < half; i++ ) {
				newLeft.add(tmp[i], bucketSize, allNodes, leftSideNodes, rightSideNodes);
			}

			for (int i = half; i < tmp.length; i++ ) {
				newRight.add(tmp[i], bucketSize, allNodes, leftSideNodes, rightSideNodes);
			}

			this.index 		= 0;
			this.left 		= newLeft;
			this.right 		= newRight;
			this.leaf 		= false;
			this.bucket 	= null;
			this.height 	= 1;
			this.minValue 	= newLeft.getMinValue();
			this.maxValue 	= newRight.getMaxValue();


		}
	}

	public void add2(int newValue, int bucketSize, 
			List<BucketNode> allNodes, 
			List<BucketNode> left_leftSideNodes,
			List<BucketNode> left_rightSideNodes, 
			List<BucketNode> right_leftSideNodes, 
			List<BucketNode> right_rightSideNodes) {

		if ( index < bucket.length - 1 ) {

			index++;
			bucket[index] = newValue;

			if ( newValue > maxValue ) {
				maxValue = newValue;
			}
			if ( newValue < minValue ) {
				minValue = newValue;
			}

		} else {

			/*
			 * bucket full: creates two new nodes
			 */

			BucketNode newLeft = new BucketNode(bucketSize, allNodes);
			BucketNode newRight = new BucketNode(bucketSize, allNodes);
			newLeft.setParent(this);
			newRight.setParent(this);

			assignSide2(this, newLeft, newRight, left_leftSideNodes, left_rightSideNodes,
					right_leftSideNodes, right_rightSideNodes);

			int[] tmp = new int[bucketSize + 1];
			System.arraycopy( this.bucket, 0, tmp, 0, this.bucket.length );
			tmp[tmp.length-1] = newValue;
			Arrays.sort(tmp);

			int half = (int) Math.ceil(tmp.length/2);
			this.splitValue = tmp[half];

			for (int i = 0; i < half; i++ ) {
				newLeft.add2(tmp[i], bucketSize, allNodes, 
						left_leftSideNodes, left_rightSideNodes,
						right_leftSideNodes, right_rightSideNodes);
			}

			for (int i = half; i < tmp.length; i++ ) {
				newRight.add2(tmp[i], bucketSize, allNodes, 
						left_leftSideNodes, left_rightSideNodes,
						right_leftSideNodes, right_rightSideNodes);
			}

			this.index 		= 0;
			this.left 		= newLeft;
			this.right 		= newRight;
			this.leaf 		= false;
			this.bucket 	= null;
			this.height 	= 1;
			this.minValue 	= newLeft.getMinValue();
			this.maxValue 	= newRight.getMaxValue();
		}
	}

	private void assignSide(BucketNode parentNode, BucketNode leftNode,
			BucketNode rightNode, List<BucketNode> leftSideNodes, List<BucketNode> rightSideNodes) {

		if (  parentNode.getParent() != null ) { //checks if it is the root

			leftNode.setSide(parentNode.getSide());
			rightNode.setSide(parentNode.getSide());
			if ( parentNode.getSide().equals(Side.LEFT) ) {
				leftSideNodes.add(leftNode);
				leftSideNodes.add(rightNode);
			}
			if ( parentNode.getSide().equals(Side.RIGHT) ) {
				rightSideNodes.add(leftNode);
				rightSideNodes.add(rightNode);
			}
			
		} else {

			// parent is the root 
			leftNode.setSide(Side.LEFT);
			leftSideNodes.add(leftNode);
			
			rightNode.setSide(Side.RIGHT);
			rightSideNodes.add(rightNode);
		}
	}

	private void assignSide2(BucketNode parentNode, BucketNode leftNode,
			BucketNode rightNode, 
			List<BucketNode> left_leftSideNodes, 
			List<BucketNode> left_rightSideNodes, 
			List<BucketNode> right_leftSideNodes, 
			List<BucketNode> right_rightSideNodes) {

		if (  parentNode.getParent() != null ) { //checks if it is the root

			// the parent is not the root
			
			
			if ( parentNode.getSide().equals(Side.LEFT)) {  
				// the parent is the left child of the root
				
				leftNode.setSide(Side.LEFT_LEFT);
				left_leftSideNodes.add(leftNode);
				rightNode.setSide(Side.LEFT_RIGHT);
				left_rightSideNodes.add(rightNode);
				
			} else if ( parentNode.getSide().equals(Side.RIGHT)) { 
				// the parent is the right child of the root
				
				leftNode.setSide(Side.RIGHT_LEFT);
				right_leftSideNodes.add(leftNode);
				rightNode.setSide(Side.RIGTH_RIGHT);
				right_rightSideNodes.add(rightNode);
				
				
			} else {
				
				// the parent is a common node
				leftNode.setSide(parentNode.side);
				rightNode.setSide(parentNode.side);
				
				if ( parentNode.getSide().equals(Side.LEFT_LEFT) ) {
					
					left_leftSideNodes.add(leftNode);
					left_leftSideNodes.add(rightNode);
				
				} else if ( parentNode.getSide().equals(Side.LEFT_RIGHT) ) {
					
					left_rightSideNodes.add(leftNode);
					left_rightSideNodes.add(rightNode);
				
					
				} else if ( parentNode.getSide().equals(Side.RIGHT_LEFT) ) {
					
					right_leftSideNodes.add(leftNode);
					right_leftSideNodes.add(rightNode);
				
				} else if ( parentNode.getSide().equals(Side.RIGTH_RIGHT) ) {
					
					right_rightSideNodes.add(leftNode);
					right_rightSideNodes.add(rightNode);
				}
			}

		} else {

			// parent is the root 
			leftNode.setSide(Side.LEFT);
			rightNode.setSide(Side.RIGHT);
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public int getMinValue() {
		return minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}




}
