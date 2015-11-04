/* $Id: LevenshteinDistanceMetric.java 23 2006-04-18 21:28:18Z vja2 $ */
package net.vja2.research.distancemetrics;

import org.apache.commons.lang.StringUtils;

/**
 * A wrapper for the Levenshtein Distance Metric implementation provided by {@link org.apache.commons.lang.StringUtils}.
 * @author vja2
 *
 */
public class LevenshteinDistanceMetric<E> implements IDistanceMetric<E> {

	/**
	 * {@inheritDoc}
	 * this returns the distance between the strings provided by {@link java.lang.Object#toString()}.
	 */
	public double distance(E a, E b) {
		return StringUtils.getLevenshteinDistance(a.toString(),b.toString());
	}

}
