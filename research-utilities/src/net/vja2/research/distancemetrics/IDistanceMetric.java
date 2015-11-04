/* $Id: DistanceMetric.java 6 2005-12-18 22:35:52Z vja2 $ */
package net.vja2.research.distancemetrics;

/**
 * IDistanceMetric implements the Strategy Design Pattern.
 * This will allow us to implement a number of Distance Metric methods and drop them into our implementation
 * 
 * @author vja2
 */
public interface IDistanceMetric<E> {
	/**
	 * 
	 * @param a an object.
	 * @param b another object.
	 * @return the distance between <i>a</i> and <i>b</i>.
	 */
	double distance(E a, E b);
}