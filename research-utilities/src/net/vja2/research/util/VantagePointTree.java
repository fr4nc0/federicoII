/* $Id: VantagePointTree.java 23 2006-04-18 21:28:18Z vja2 $ */
package net.vja2.research.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import net.vja2.research.distancemetrics.*;

/**
 * VantagePointTree is a naive implementation of the Vantage Point Tree data structure. It can work with a variety of distance metrics.
 * Additionally, differing vantage point selection methods can be chosen.
 * 
 * @author vja2
 * @see DIDistanceMetric@see VantagePointTree.VantagePointSelector
 */
public class VantagePointTree<E> {
	
	/**
	 * This is the interface for Vantage Point selection algorithms.
	 * @author vja2
	 *
	 */
	public interface VantagePointSelector<E> {
		/**
		 * 
		 * @param data the set of data that the vantage point should be selected from.
		 * @return the vantage point.
		 */
		E select(ArrayList<E> data);
	}
	
	/**
     * This constructor for the VantagePointTree results in a valid vantage point tree based on the given dataset with the given distance metric and selection algorithm.
     * 
     * @param selector An object that implements the {@link VantagePointSelector} interface, for choosing vantage points.
     * @param dm An implementation of the {@link DIDistanceMetric interface for computing the distances between objects for tree construction and querying.
     * @param dataset The dataset the tree will be constructed from.
     */
	public VantagePointTree(VantagePointSelector<E> selector, IDistanceMetric<E> dm, ArrayList<E> dataset)
	{
		this.selector = selector;
		this.distanceMetric = dm;
		this.dataset = dataset;
		this.median = 0;
		this.left = null;
		this.right = null;
		buildTree();
	}
	
	/**
     * This constructor for the VantagePointTree results in a VP-Tree based on the given dataset, distance metric, and random vantage point selection.
     * 
     * @param dm dm An implementation of the {@link IDistanceMetric} interface for computing the distances between objects for tree construction and querying.
     * @param dataset dataset The dataset the tree will be constructed from.
     */
	public VantagePointTree(IDistanceMetric<E> dm, ArrayList<E> dataset)
	{
		this(new RandomVantagePointSelector<E>(), dm, dataset);
	}
	
	/**
	 * 
	 * @return this tree node's vantage point. 
	 */
	public E vantagePoint() { return this.vantagePoint; }
	
	/**
	 * 
	 * @return the median distance for this vantage point.
	 */
	public double median() { return this.median; }
	
	/**
	 * 
	 * @return this node's left subtree.
	 */
	public VantagePointTree<E> left() { return this.left; }
	
	/**
	 * 
	 * @return this node's right subtree.
	 */
	public VantagePointTree<E> right() { return this.right; }
	
	/**
	 * Search finds the nearest neighbor to our object within radius tau.
	 * @param q the query that we want to find a nearest neighbor for.
	 * @param tau search radius.
	 * @return the nearest neighbor to q.
	 */
	public E search(E q, double tau)
	{
		return this.search(q, tau, 1).get(0);
	}

	/**
	 * Search finds the k nearest neighbors to our object within the specified radius.
	 * @param q the query that we want to find a nearest neighbor for.
	 * @param tau search radius.
	 * @param k the number of nearest neighbors to find.
	 * @return a vector with the k closest neighbors within radius tau in descending order (the nearest neighbor is the <b>last</b> element in the vector.
	 */
	public ArrayList<E> search(E q, double tau, int k)
	{
		return this.search(q, new QueryResultQueue<E>(tau, k)).results();
	}
	
	/**
	 * finds all the neighbors within radius tau of the query.
	 * @param q the query.
	 * @param tau the search radius.
	 * @return an ArrayList composed of the neighbors.
	 */
	public ArrayList<E> range(E q, double tau)
	{
		return this.search(q, tau, this.dataset.size());
	}
	
	/**
	 * Tail-recursive implementation of height().
	 * @return returns the height of the current node (has this number of nodes between it and 
	 * a null node on the longest path)
	 */	
	public int height() {
		if (this.left == null && this.right == null)
			return 0;
			
		if (this.left == null)
			return this.right.height() + 1;
		else if (this.right == null)
			return this.left.height() + 1;
		
		return Math.max(this.left.height(), this.right.height()) + 1;
	}
	
	/**
	 * This function is an implementation of the VP search algorithm found in "Data Structures and Algorithms for Nearest Neighbor Search in General Metric Spaces.
	 * @param q the query that we want to find the nearest neighbor for.
	 * @param results a priority queue that holds the k closest neighbors found thus far.
	 * @return a QueryResultQueue object, which has the k nearest neighbors, and their distance to the query.
	 * @see <a href="http://citeseer.csail.mit.edu/yianilos93data.html">Data Structures and Algorithms for Nearest Neighbor Search in General Metric Spaces / CiteSeer</a>
	 */
	private QueryResultQueue<E> search(E q, QueryResultQueue<E> results)
	{
		double x = dist(q);
		if(x <= results.tau())
			results.addPossibleResult(new QueryResult<E>(q, this.vantagePoint, x));
		
		if(this.left != null && (x - results.tau() < this.median))
			results = left.search(q, results);
		if(this.right != null && (x + results.tau() >= this.median))
			results = right.search(q, results);
		
		return results;
	}
	
	/**
	 * 
	 * @param a the point we want to compute the distance to.
	 * @return the distance between the vantage point and object a.
	 */
	private double dist(E a) { return this.distanceMetric.distance(this.vantagePoint, a); }
	
	/**
	 * Recursively builds a vantage point tree from the dataset.
	 *
	 */
	private void buildTree()
	{
		this.vantagePoint = selector.select(dataset);

		if(this.dataset.size() == 1) return;
		
		// initialize variables
		int halfway = (this.dataset.size() - 1) / 2;
		
		// We can assume that inSet and outSet will both be roughly half of the original dataset.
		// Vector will add space as needed. Before returning, we'll use Vector.trimToSize().
		ArrayList<E> inSet = new ArrayList<E>(halfway),
				outSet = new ArrayList<E>(halfway);
		
		// computing the median
		if(this.dataset.size() - 1 > 2)			// if there are more than 2 elements, we must sort everything and then compute the median
		{
			ArrayList<Double> distances = new ArrayList<Double>(this.dataset.size() - 1);
			
			// get all the distances from the vantage point, and then sort in preparation for finding the median.
			for(E i : dataset)
			{
				if(!i.equals(this.vantagePoint))
					distances.add(dist(i));
			}
			Collections.sort(distances);
			
			// Compute the median. If there are an odd number of values, then return the halfway point.
			// Otherwise, return the average of the two middle elements.
			this.median = (distances.size() % 2 > 0)
							? distances.get(halfway)
							: (distances.get(halfway) + distances.get(halfway - 1)) / 2;
		}
		else if(this.dataset.size() - 1 == 2)	// if there are only two elements, we can return their average for the median
			this.median = (dist(this.dataset.get(0)) + dist(this.dataset.get(1))) / 2;
		else									// else, there is only one element and it is the median.
			this.median = dist(this.dataset.get(0));
		
		// partition the points (except the vantage point) around the median -- if less than the median, put in inSet; otherwise outSet
		for(E i : dataset)
		{
			if(!this.vantagePoint.equals(i))
			{
				if(dist(i) < median)
					inSet.add(i);
				else
					outSet.add(i);
			}
		}
		inSet.trimToSize();
		outSet.trimToSize();
		
		// create the left and right subtrees based on inSet and outSet.
		if(!inSet.isEmpty())
			this.left = new VantagePointTree<E>(this.selector, this.distanceMetric, inSet);
		if(!outSet.isEmpty())
			this.right = new VantagePointTree<E>(this.selector, this.distanceMetric, outSet);
	}
	
	private VantagePointSelector<E> selector;
	private IDistanceMetric<E> distanceMetric;
	private ArrayList<E> dataset;
	
	private E vantagePoint;
	private double median;
	
	private VantagePointTree<E> left;
	private VantagePointTree<E> right;
	
	public static class RandomVantagePointSelector<E> implements VantagePointSelector<E> {

		/**
		 * initializes a RandomVantagePointSelector with a new instance of {@link java.util.Random}.
		 *
		 */
		public RandomVantagePointSelector()
		{
			rng = new Random();
		}
		
		/**
		 * {@inheritDoc}
		 * returns a random element from the ArrayList.
		 */
		public E select(ArrayList<E> data) {
			return data.get(rng.nextInt(data.size()));
		}

		private Random rng; 
	}
}

