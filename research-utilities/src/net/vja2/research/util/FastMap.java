/* $Id: FastMap.java 15 2006-02-23 21:59:16Z vja2 $ */
package net.vja2.research.util;

import fg.federicoII.esco.EscoNode;
import it.federicoII.indice.RDFtriplet;

import java.lang.Math;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Random;

import net.vja2.research.distancemetrics.IDistanceMetric;

public class FastMap<E> {
	public FastMap(AbstractList<E> data, IDistanceMetric<E> metric)
	{
		this(data, metric, new DefaultPivotSelector<E>(metric));
	}
	
	public FastMap(AbstractList<E> data, IDistanceMetric<E> metric, PivotSelector<E> selector)
	{
		this.dataset = data;
		this.selector = selector;
	}
	
	/**
	 * The map function returns a DataMapping created using the initial data, the desired number of
	 * dimensions and the PivotSelector. map() is a non-recursive implementation of the FastMap
	 * algorithm described in "FastMap" by Faloutsos & Lin (algorithm #2).
	 * @param dimensions the desired number of dimensions.
	 * @return a DataMapping 
	 */
	public DataMapping<E> map(int dimensions)
	{
		ArrayList<Pivot<E>> pivots = new ArrayList<Pivot<E>>(dimensions);
		ArrayList<MapPoint<E>> mappeddata = new ArrayList<MapPoint<E>>(this.dataset.size());
		Pivot<E> p = null;
		
		for(E data : this.dataset)
			mappeddata.add(new MapPoint<E>(data, dimensions));
		
		for(int i = 0; i < dimensions; i++)
		{
			p = this.selector.select(mappeddata, i);
			pivots.add(p);
			
			/*
			 * lettura dei pivot
			 */
			/*
			System.out.println(" dimension: " + i + 
					" pivot.pointA: " + ((RDFtriplet) p.a().data()).getId() + 
					" pivot.pointB: " + ((RDFtriplet) p.b().data()).getId() );
			*/
			/*
			System.out.println(" dimension: " + i + 
					" pivot.pointA: " + ((EscoNode) p.a().data()).getPreferredTerm() + 
					" pivot.pointB: " + ((EscoNode) p.b().data()).getPreferredTerm() );
			*/
			
			for(MapPoint<E> datapoint : mappeddata)
				datapoint = p.map(datapoint);
		}
		pivots.trimToSize();
		mappeddata.trimToSize();
		
		return new DataMapping<E>(pivots, mappeddata);
	}
		
	public static interface PivotSelector<E>
	{
		Pivot<E> select(ArrayList<MapPoint<E>> dataset, int k);
	}

	public static class MapPoint<E>
	{
		public MapPoint(E i, int k)
		{
			this.a = i;
			this.dimensions = k;
			this.map = new double[this.dimensions];
		}
		
		public E data() { return this.a; }
		public int dimensions() { return this.dimensions; }
		
		private E a;
		private int dimensions;
		public double[] map;
	}
	
	public static class Pivot<E>
	{
		public Pivot(IDistanceMetric<E> m, int k)
		{
			this.pointA = null;
			this.pointB = null;
			this.dm = m;
			this.k = k;
		}
		
		public double distSquared(MapPoint<E> a, MapPoint<E> b)
		{
			double sum = 0;
			for(int i = 0; i < k - 1; i++)
				sum += Math.pow( a.map[i] - b.map[i], 2);
			return Math.pow(dm.distance(a.data(), b.data()), 2) - sum;
		}
		
		public double dist(MapPoint<E> a, MapPoint<E> b)
		{
			return Math.sqrt(this.distSquared(a, b));
		}
		
		public MapPoint<E> map(MapPoint<E> i)
		{
			i.map[this.k] = (distSquared(this.pointA, i) + Math.pow(this.distance, 2)
							- distSquared(this.pointB, i)) / (2.0 * this.distance);
			return i;
		}
		
		public void setA(MapPoint<E> a)
		{
			this.pointA = a;
			if(this.pointA != null && this.pointB != null)
				this.distance = dist(this.pointA, this.pointB);
		}
		public void setB(MapPoint<E> b)
		{
			this.pointB = b;
			if(this.pointA != null && this.pointB != null)
				this.distance = dist(this.pointA, this.pointB);
		}
		
		public MapPoint<E> a() { return this.pointA; }
		public MapPoint<E> b() { return this.pointB; }
		public double distance() { return this.distance; }
		
		private MapPoint<E> pointA;
		private MapPoint<E> pointB;
		private IDistanceMetric<E> dm;
		
		private int k;
		private double distance;
	}

	public static class DataMapping<E>
	{
		public DataMapping(ArrayList<Pivot<E>> pivots, ArrayList<MapPoint<E>> map)
		{
			this.pivots = pivots;
			this.mappeddata = map;
		}
		
		/**
		 * the map function returns a new MapPoint given the inputted data point and this mapping's
		 * pivots.
		 * @param o the object to create a mapping for.
		 * @return a new MapPoint object.
		 */
		public MapPoint<E> map(E o)
		{
			MapPoint<E> point = new MapPoint<E>(o, this.dimensions());
			for(Pivot<E> p : this.pivots)
				point = p.map(point);
			return point;
		}

		/**
		 * Adds the specified MapPoint to the mapped data.
		 * @param datapoint the datapoint to add to this dataset.
		 */
		public void add(MapPoint<E> datapoint) { this.mappeddata.add(datapoint); }
		
		/**
		 * Creates a mapping for this data point, and then adds it to the mapped data.
		 * @param data the data to be mapped and added.
		 */
		public void add(E data) { this.mappeddata.add(this.map(data)); }
		
		/**
		 * 
		 * @return the number of dimensions in this mapping.
		 */
		public int dimensions() { return this.pivots.size(); }
		
		public ArrayList<MapPoint<E>> mappeddata;
		public ArrayList<Pivot<E>> pivots;
	}
	
	public static class DefaultPivotSelector<E> implements PivotSelector<E>
	{
		public DefaultPivotSelector(IDistanceMetric<E> dm)
		{
			this.rng = new Random();
			this.dm = dm;
		}
		
		public Pivot<E> select(ArrayList<MapPoint<E>> dataset, int k)
		{
			Pivot<E> p = new Pivot<E>(this.dm, k);
			MapPoint<E> pivotA = null, pivotB = dataset.get(rng.nextInt(dataset.size()));
			
			double maxDist = 0;
			double dist = 0;
			
			for(MapPoint<E> i : dataset)
			{
				dist = p.dist(pivotB, i);
				if(dist > maxDist)
				{
					maxDist = dist;
					pivotA = i;
				}
			}
			p.setA(pivotA);
			
			for(MapPoint<E> i : dataset)
			{
				dist = p.dist(pivotA, i);
				if(dist > maxDist)
				{
					maxDist = dist;
					pivotB = i;
				}
			}
			p.setB(pivotB);
			
			return p;
		}
		
		private IDistanceMetric<E> dm;
		private Random rng;
	}
	
	private AbstractList<E> dataset;
	private PivotSelector<E> selector;
}
