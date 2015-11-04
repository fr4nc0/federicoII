/* $Id: QueryResult.java 18 2006-02-24 23:44:55Z vja2 $ */
package net.vja2.research.util;

import java.lang.Comparable;

public class QueryResult<E> implements Comparable {
	public QueryResult(E query, E neighbor, double distance)
	{
		this.query = query;
		this.neighbor = neighbor;
		this.tau = distance;
	}
	
	public int compareTo(Object o)
	{
		if(this.tau == ((QueryResult) o).tau)
			return 0;
		else if(this.tau < ((QueryResult) o).tau)
			return -1;
		else
			return 1;
	}
	
	public E query;
	public E neighbor;
	public double tau;
}
