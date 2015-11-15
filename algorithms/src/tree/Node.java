package tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import binaryTrees.BucketNode;


public class Node<T extends Comparable<T>> {

	private Node<T> 		left;
	private Node<T> 		right;
	private Node<T>			parent;
	private T 				splitValue;
	private List<T>			bucket;
	private int 			bucketSize;
	private boolean 		leaf;
	private int 			height;
	private String 			status;
	private boolean			root;
	private T				minValue;
	private T				maxValue;
	private Side			side; 


	public Node(T value, int bucketSize, List<Node<T>> allNodes) {

		this.leaf 			= true;
		this.bucket 		= new ArrayList<T>();
		this.bucketSize 	= bucketSize;	
		this.height 		= 0;
		this.minValue 		= value;
		this.maxValue 		= value;
		this.side			= Side.NONE;
		this.splitValue		= value;
		this.bucket.add(value);

		allNodes.add(this);
	}

	public Node(int bucketSize, List<Node<T>> allNodes) {

		this.leaf 		= true;
		this.bucket 	= new ArrayList<T>();
		this.bucketSize	= bucketSize;	
		this.height 	= 0;
		this.minValue	= null;	
		this.maxValue	= null; 

		allNodes.add(this);
	}


	public void add(T newValue, int bucketSize, 
			List<Node<T>> allNodes, 
			List<Node<T>> leftSideNodes,
			List<Node<T>> rightSideNodes) {

		if ( bucketSize > bucket.size() ) {

			bucket.add(newValue);

			//if ( newValue > maxValue ) {
			if (maxValue == null || newValue.compareTo(maxValue) > 0) {
				maxValue = newValue;
			}
			//if ( newValue < minValue ) {
			if (minValue == null || newValue.compareTo(minValue) < 0) {
				minValue = newValue;
			}

		} else {

			/*
			 * bucket full: creates two new nodes
			 */

			Node<T> newLeft = new Node<T>(bucketSize, allNodes);
			Node<T> newRight = new Node<T>(bucketSize, allNodes);
			newLeft.setParent(this);
			newRight.setParent(this);

			assignSide(this, newLeft, newRight, leftSideNodes, rightSideNodes);

			List<T> tmp 	= new ArrayList<T>(bucket);
			tmp.add(newValue);
			Collections.sort(tmp, new Comparator<T>() {
				@Override
				public int compare(T  t1, T  t2) {
					return  t1.compareTo(t2);
				}
			});

			int half = (int) Math.ceil(tmp.size()/2);
			this.splitValue = tmp.get(half);

			for (int i = 0; i < half; i++ ) {
				newLeft.add(tmp.get(i), bucketSize, allNodes, leftSideNodes, rightSideNodes);
			}

			for (int i = half; i < tmp.size(); i++ ) {
				newRight.add(tmp.get(i), bucketSize, allNodes, leftSideNodes, rightSideNodes);
			}

			this.left 		= newLeft;
			this.right 		= newRight;
			this.leaf 		= false;
			this.bucket 	= null;
			this.height 	= 1;
			this.minValue 	= newLeft.getMinValue();
			this.maxValue 	= newRight.getMaxValue();
		}
	}


	public String getLabelNode() {
		String label = "";
		if ( this.isLeaf() ) {
			label = this.getBucketAsString() + "min:" + this.minValue.toString() + " max:" + this.maxValue.toString() + " split:" + this.splitValue ;
		} else {
			label = this.splitValue.toString() + " min:" + this.minValue.toString() + " max:" + this.maxValue.toString();
		}
		return label ;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<T> getBucket() {
		return bucket;
	}

	@Override
	public String toString() {
		if (this.isLeaf()) {
			return "bucket=" + getBucketAsString() + " " + status;
		} else {
			return splitValue.toString()  + " " + status;	
		}
	}

	public String getBucketAsString() {
		String s = "";
		if ( this.bucket != null ) {
			s = "[";
			for (int i = 0; i < bucket.size(); i++) {
				s = s + " " + this.bucket.get(i).toString() + " ";
			}
			s = s+ "]";	
		}

		return s;
	}

	public Node<T> getLeft() {
		return left;
	}

	public void setLeft(Node<T> left) {
		this.left = left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setRight(Node<T> right) {
		this.right = right;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public T getSplitValue() {
		if ( ! this.isLeaf() ) {

			return splitValue;

		} else {
			// the split value of a left child leaf is the minimum in the bucket
			// otherwise it is the maximum value in the bucket

			List<T> tmp 	= new ArrayList<T>(bucket);
			Collections.sort(tmp, new Comparator<T>() {
				@Override
				public int compare(T  t1, T  t2) {
					return  t1.compareTo(t2);
				}
			});
			
			//if ( this.bucket.get(0) < this.getParent().getSplitValue() ) {
			if ( this.bucket.get(0).compareTo(this.getParent().getSplitValue()) < 0 ) {
				// it's a left child
				return tmp.get(0);

			} else {
				//it's a right child
				return tmp.get(tmp.size() - 1);
			}
		}
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
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

	public T getMinValue() {
		return minValue;
	}

	public T getMaxValue() {
		return maxValue;
	}

	public void setMinValue(T minValue) {
		this.minValue = minValue;
	}

	public void setMaxValue(T maxValue) {
		this.maxValue = maxValue;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}



	private void assignSide(Node<T> parentNode, Node<T> leftNode,
			Node<T> rightNode, List<Node<T>> leftSideNodes, List<Node<T>> rightSideNodes) {

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

}
