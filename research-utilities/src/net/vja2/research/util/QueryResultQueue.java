/* $Id: QueryResultQueue.java 18 2006-02-24 23:44:55Z vja2 $ */
package net.vja2.research.util;

import java.util.ArrayList;
import org.apache.commons.collections.buffer.PriorityBuffer;

public class QueryResultQueue<E> {
	public QueryResultQueue(double tau, int numAnswers)
	{
		this.tau = tau;
		this.numAnswers = numAnswers;
		queue = new PriorityBuffer(numAnswers, false);		
	}
	
	public void addPossibleResult(QueryResult<E> q)
	{
		if(queue.size() < numAnswers)
		{
			queue.add(q);
			if(queue.size() == numAnswers)
				this.tau = ((QueryResult) queue.get()).tau;
		}
		else if(q.compareTo(queue.get()) < 0)
		{
			queue.remove();
			queue.add(q);
			tau = ((QueryResult) queue.get()).tau;
		}
	}
	
	public double tau() { return this.tau; }
	
	public ArrayList<E> results()
	{
		ArrayList<E> results = new ArrayList<E>(queue.size());
		while(!queue.isEmpty())
			results.add(((QueryResult<E>) queue.remove()).neighbor);
		return results;
	}
	
	private double tau;
	private int numAnswers;
	private PriorityBuffer queue;
}
