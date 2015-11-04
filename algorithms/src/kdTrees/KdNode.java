package kdTrees;

import graphics.Node;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.omg.PortableServer.POA;

public class KdNode implements Node{

	private KdNode 		left;
	private KdNode 		right;
	private KdNode 		parent;
	private Double 		splitValue;
	private Point[]		bucket;
	private int 		index;
	private boolean 	leaf;
	private int 		height;
	private String 		status;
	private boolean		root;
	private Double 		minValue;
	private Double		maxValue;
	private Side		side; 
	private int			splitCoordinate;
	
	private Long		ID;  
	static private Long nextID;

	public enum Side {LEFT, RIGHT, NONE};

	public KdNode(Point point, int bucketSize, List<KdNode> allNodes) {

		this.ID				= getNewID();
		this.leaf 			 = true;
		this.bucket 		 = new Point[bucketSize];
		this.index 			 = 0;
		this.bucket[index] 	 = point;	
		this.height 		 = 0;
		this.minValue 		 = point.get(splitCoordinate);
		this.maxValue 		 = point.get(splitCoordinate);
		this.side			 = Side.NONE;

		allNodes.add(this);
	}

	public KdNode(int bucketSize, List<KdNode> allNodes) {

		this.ID			= getNewID();
		this.leaf 		= true;
		this.bucket 	= new Point[bucketSize];
		this.index 		= -1;
		this.height 	= 0;
		this.minValue	= Double.MAX_VALUE;
		this.maxValue	= Double.MIN_VALUE;

		allNodes.add(this);
	}

	public String getLabelNode() {
		String label = "";
		if ( this.isLeaf() ) {
			label = "ID:" + ID + " coord:" + splitCoordinate + getBucketAsString() 
					+ "parent: " + getParent().getID() 
					+ " min:" + minValue + " max:" + maxValue;
		} else {
			if ( getParent() != null ) {
				label = "ID:" + ID + " coord:" + splitCoordinate + " value:" + splitValue 
						+ "parent: " + getParent().getID()
						+ " min:" + minValue + " max:" + maxValue;	
			} else {
				label = "ID:" + ID + " coord:" + splitCoordinate + " value:" + splitValue 
						+ "parent:" + "NULL"
						+ " min:" + minValue + " max:" + maxValue;
			}
			
		}
		return label ;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Point[] getBucket() {
		/*
		 * note: a copy of the elements must be returned in order to freely modify it without changes 
		 * to the bucket  
		 */
		int numPoints = this.index + 1;

		Point[] res = new Point[numPoints];
		for (int i = 0; i < numPoints; i++) {
			res[i] = this.bucket[i];
		}
		return res;
	}

	@Override
	public String toString() {
		return getLabelNode();
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

	public KdNode getLeft() {
		return left;
	}

	public void setLeft(KdNode left) {
		this.left = left;
	}

	public KdNode getRight() {
		return right;
	}

	public void setRight(KdNode right) {
		this.right = right;
	}

	public KdNode getParent() {
		return parent;
	}

	public void setParent(KdNode parent) {
		this.parent = parent;
	}

	public Double getSplitValue() {
		
		if ( ! this.isLeaf() ) {
			
			return splitValue;
		
		} else {
			// the split value of a left child leaf is the minimum in 
			// the bucket (with respect to the split coordinate)
			// otherwise it is the maximum value in the bucket
			
			
			//Point[] tmp = new Point[index + 1];
			//System.arraycopy( this.bucket, 0, tmp, 0, index + 1 );
			//Arrays.sort(tmp);
			
			int parentSplitCoord = this.getParent().getSplitCoordinate();
			
			if ( this.bucket[index].get(parentSplitCoord) < this.getParent().getSplitValue() ) {
				// it's a left child
				//return tmp[0].get(splitCoordinate);
				return minValue;
				
			} else {
				//it's a right child
				//return tmp[index].get(splitCoordinate);
				return maxValue;
			}
		}
	}

	public boolean isLeaf() {
		return this.leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void add(Point newPoint, int bucketSize, 
			List<KdNode> allNodes, 
			List<KdNode> leftSideNodes,
			List<KdNode> rightSideNodes) {

		if ( index < bucket.length - 1 ) {

			index++;
			bucket[index] = newPoint;

			if ( newPoint.get(splitCoordinate) > maxValue ) {
				maxValue = newPoint.get(splitCoordinate);
			}
			if ( newPoint.get(splitCoordinate) < minValue ) {
				minValue = newPoint.get(splitCoordinate);
			}

		} else {

			/*
			 * bucket full: creates two new nodes
			 * NOTE: until now, split coordinate is NULL
			 */

			KdNode newLeft = new KdNode(bucketSize, allNodes);
			KdNode newRight = new KdNode(bucketSize, allNodes);
			newLeft.setParent(this);
			newRight.setParent(this);

			assignSide(this, newLeft, newRight, leftSideNodes, rightSideNodes);

			Point[] tmp = new Point[bucketSize + 1];
			System.arraycopy( this.bucket, 0, tmp, 0, this.bucket.length );
			tmp[tmp.length-1] = newPoint;
			
			this.splitCoordinate = selectSplitCoordinate(newPoint.getNumberCoords(), this.height);
			
			Arrays.sort(tmp, new Comparator<Point>() {

				@Override
				public int compare(Point p1, Point p2) {
					return p1.get(splitCoordinate).compareTo(p2.get(splitCoordinate));
				}
			});

			int half = (int) Math.ceil(tmp.length/2);
			this.splitValue = tmp[half].get(this.splitCoordinate);

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

	static public int selectSplitCoordinate(int dimensions, int height) {
		
		// Select coordinate based on height so that coordinates cycles through 
		// all valid values
	    return height % dimensions;
	}

	private void assignSide(KdNode parentNode, KdNode leftNode,
			KdNode rightNode, List<KdNode> leftSideNodes, List<KdNode> rightSideNodes) {

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

	public Double getMinValue() {
		return minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}
	
	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public int getSplitCoordinate() {
		return splitCoordinate;
	}

	public void setSplitCoordinate(int splitCoordinate) {
		this.splitCoordinate = splitCoordinate;
	}

	public void setSplitValue(Double splitValue) {
		this.splitValue = splitValue;
	}

	
	public Long getID() {
		return ID;
	}

	static private Long getNewID() {
		if ( nextID == null ) {
			nextID = new Long(0);
		}
		return nextID++;
	}
	
}
