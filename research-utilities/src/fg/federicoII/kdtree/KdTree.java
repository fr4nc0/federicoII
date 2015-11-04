package fg.federicoII.kdtree;
/**
 * Copyright 2009 Rednaxela
 * 
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 *    1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 
 *    2. This notice may not be removed or altered from any source
 *    distribution.
 */
  
/*
 * http://robowiki.net/wiki/User:Rednaxela/kD-Tree
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import javax.print.attribute.standard.MediaSize.Other;
 
/**
 * An efficient well-optimized kd-tree
 * 
 * @author Rednaxela
 */
public abstract class KdTree<T> {
	
    // Static variables
    private static final int           bucketSize = 2000; //24;
    
    // All types
    private final int                  dimensions;
    private final KdTree<T>            parent;
 
    // Root only
    private final LinkedList<double[]> locationStack;
    private final Integer              sizeLimit;
 
    // Leaf only
    private double[][]                 locations;
    private Object[]                   data;
    private int                        locationCount;
 
    // Stem only
    private KdTree<T>                  left, right;
    private int                        splitDimension;
    private double                     splitValue;
 
    // Bounds
    private double[]                   minLimit, maxLimit;
    private boolean                    singularity;
 
    // Temporary
    private Status                     status;
    
    // etichetta del nodo
    private long						NodeLabel;
 
    /**
     * Construct a KdTree with a given number of dimensions and a limit on
     * maxiumum size (after which it throws away old points)
     */
    private KdTree(int dimensions, Integer sizeLimit) {
        this.dimensions = dimensions;
 
        // Init as leaf
        this.locations = new double[bucketSize][];
        this.data = new Object[bucketSize];
        this.locationCount = 0;
        this.singularity = true;
 
        // Init as root
        this.parent = null;
        this.sizeLimit = sizeLimit;
        if (sizeLimit != null) {
            this.locationStack = new LinkedList<double[]>();
        }
        else {
            this.locationStack = null;
        }
    }
 
    /**
     * Constructor for child nodes. Internal use only.
     */
    public KdTree(KdTree<T> parent, boolean right) {
        this.dimensions = parent.dimensions;
 
        // Init as leaf
        this.locations = new double[Math.max(bucketSize, parent.locationCount)][];
        this.data = new Object[Math.max(bucketSize, parent.locationCount)];
        this.locationCount = 0;
        this.singularity = true;
 
        // Init as non-root
        this.parent = parent;
        this.locationStack = null;
        this.sizeLimit = null;
    }
 
    /**
     * Get the number of points in the tree
     */
    public int size() {
        return locationCount;
    }
    
    
    /* 
     * INIZIO MODIFICHE F.GARGIULO
     */
    
    /** 
     * Serializzazione per Metis
     * @param filename 
     */

    public void CreateMetisFile(String filename) {
    	try {
    		FileOutputStream file = new FileOutputStream(filename);
    		PrintStream output = new PrintStream(file);
    		
    		Serialize4Metis(this, output);
    		//LabelInLeftOrder(this, output);
    		//LabelInLevelsOrder(this, output);
    		//LeftOrderList(this, output);
    		//LevelsOrderList(this, output);
    		
    	} catch (IOException e) {
    		System.out.println("Errore: " + e);
    		System.exit(1);
    	}
    }
  
	/**
	 * Visita l'albero in Left Order First
	 * @param kt
	 * @param output
	 */
	private void LeftOrderList(KdTree<T> kt, PrintStream output) {
		String riga = "";
		
		System.out.println("\nElaborazione del nodo: " + kt.NodeLabel);
		
		printNodeContent (kt, output);
		
		if (kt.right != null){
			System.out.println("\t Figlio sinistro di " + kt.NodeLabel + " => " + kt.left.NodeLabel);
			LeftOrderList(kt.left, output);
		}
		if (kt.left  != null){
			System.out.println("\t Figlio destro di " + kt.NodeLabel + " => " + kt.right.NodeLabel);
			LeftOrderList(kt.right, output );
		}
	}
	
	/**
	 * Esamina col contenuto del nodo
	 * @param kt
	 * @param output
	 */
	private void printNodeContent(KdTree<T> kt, PrintStream output) {
		System.out.println("\nElaborazione del nodo: " + kt.NodeLabel 
				+ " locationCount: " + kt.locationCount
				+ " singularity: " + kt.singularity 
				+ " splitDimension: " + kt.splitDimension
				+ " splitValue: " + kt.splitValue);
		

		if (kt.locations != null  ) {
			System.out.println("\t inzio contenuto ====================");
			if (kt.singularity) {
				writeFile(kt.locations[0], kt.data[0], output);

			} else {
				System.out.println("\n\t\t inzio bucket:----------------");
				for (int i = 0; i < kt.locationCount; i++) {
					writeFile(kt.locations[i], kt.data[i], output);
				}
				System.out.println("\t\t fine bucket----------------");
			}
			System.out.println("\t fine contenuto ====================");
		}
	}


	/**
	 * Etichetta i nodi in LeftOrderList
     * @param kt
     * @param output
     */
	private void LabelInLeftOrder(KdTree<T> kt, PrintStream output) {

		// mette l'etichetta
		//label++;
		//kt.NodeLabel = label;

		if (kt.right != null){
			LabelInLeftOrder(kt.left, output);
		}
		if (kt.left  != null){
			LabelInLeftOrder(kt.right, output);
		}
	}

	/**
	 * Etichetta i nodi dell'albero con una visita per livelli
	 * @param kt
	 * @param output
	 */
	private long LabelInLevelsOrder(KdTree<T> kt, PrintStream output) {
		long 				NumeroArchi = 0;
		long				label = 0;	// serve per la visita per livello
		Queue<KdTree<T>> 	coda = new LinkedList();
		
		coda.add(kt);

		while (!coda.isEmpty()) {
			KdTree<T> k = coda.remove();

			if (k != null) {
				label++;
				k.NodeLabel = label;

				if (k.left != null) {
					NumeroArchi++;
					coda.add(k.left );
				}
				if (k.right != null) {
					NumeroArchi++;
					coda.add(k.right);
				}
			}
		}
		return NumeroArchi;
	}

	/**
	 * Visita l'albero per livelli e scrive il file Metis 
	 * @param kt
	 * @param output
	 */
	private void LevelsOrderList(KdTree<T> kt, PrintStream output) {
		Queue<KdTree<T>> coda = new LinkedList();
		coda.add(kt);

		while (!coda.isEmpty()) {
			KdTree<T> k = coda.remove();
			
			if (k != null) {
				//String riga = "Elaborazione del nodo: " + kt.NodeLabel;
				System.out.println("\nElaborazione del nodo: " + k.NodeLabel);
				printNodeContent(k, output);
				
				if (k.left != null) {
					System.out.println("\t Figlio sinistro di " + k.NodeLabel + " => " + k.left.NodeLabel);
					coda.add(k.left );
					//riga = riga + " figlio SX: " + k.left.NodeLabel;
				}
				if (k.right != null) {
					System.out.println("\t Figlio destro di " + k.NodeLabel + " => " + k.right.NodeLabel);
					coda.add(k.right);
					//riga = riga + " figlio DX: " + k.right.NodeLabel;
				}
				
				//output.println(riga);
			}
		}
	}

	/**
	 * Crea il file .txt nel formato per Metis
	 * @param kt
	 * @param output
	 */
	private void Serialize4Metis(KdTree<T> kt, PrintStream output) {
		
		/*
		 * Questo metodo esegue una visita per livelli dell'albero. Per ogni nodo visitato:
		 * 1. legge le info necessarie: figli, num. di oggetti nel bucket, ecc
		 * 2. scrive una riga nel file
		 */
		
		/*
		 * scrive la riga di intestazione nel file
		 * per questo è necessario contare il numero di archi 
		 * viene eseguita una visita per livelli in cui i nodi sono anche numerati
		 */
		long 	numeroArchi = LabelInLevelsOrder(kt, output);
		int 	numeroVertici = kt.size();
		output.println(numeroVertici + " " + numeroArchi);
		
		Queue<KdTree<T>> coda = new LinkedList();
		coda.add(kt);

		while (!coda.isEmpty()) 
		{
			KdTree<T> k = coda.remove();
			
			if (k != null) 
			{	
				String riga = ""; 
				printNodeContent(k, output);
				
				if (k.left != null) 
				{
					System.out.println("\t Figlio sinistro di " + k.NodeLabel + " => " + k.left.NodeLabel);
					coda.add(k.left );
					riga = Long.toString(k.left.NodeLabel);
				}
				
				if (k.right != null) 
				{
					System.out.println("\t Figlio destro di " + k.NodeLabel + " => " + k.right.NodeLabel);
					coda.add(k.right);
					if (riga.equals(""))
					{
						riga = Long.toString(k.right.NodeLabel);
					} else {
						riga = riga + " " + Long.toString(k.right.NodeLabel);
					}
					
				}

				if (!riga.equals(""))
				{
					output.println(riga);
				}
			}
		}
	}

	private void writeFile(double[] currLocation, Object data, PrintStream output){ 
		String riga = "\t\t\t punto: " + currLocation[0] + " - "  + currLocation[1]  + " data: " + data.toString();
		System.out.println(riga);
		//output.println(riga);
	}
	
	/*
	 * FINE MODIFICHE F.GARGIULO
	 */

		
	
	/**
     * Add a point and associated value to the tree
     */
    public void addPoint(double[] location, T value) {
        KdTree<T> cursor = this;
  
        while (cursor.locations == null || cursor.locationCount >= cursor.locations.length) {
            if (cursor.locations != null) {
                cursor.splitDimension = cursor.findWidestAxis();
                cursor.splitValue = (cursor.minLimit[cursor.splitDimension] + cursor.maxLimit[cursor.splitDimension]) * 0.5;
 
                // Never split on infinity or NaN
                if (cursor.splitValue == Double.POSITIVE_INFINITY) {
                    cursor.splitValue = Double.MAX_VALUE;
                }
                else if (cursor.splitValue == Double.NEGATIVE_INFINITY) {
                    cursor.splitValue = -Double.MAX_VALUE;
                }
                else if (Double.isNaN(cursor.splitValue)) {
                    cursor.splitValue = 0;
                }
 
                // Don't split node if it has no width in any axis. Double the
                // bucket size instead
                if (cursor.minLimit[cursor.splitDimension] == cursor.maxLimit[cursor.splitDimension]) {
                    double[][] newLocations = new double[cursor.locations.length * 2][];
                    System.arraycopy(cursor.locations, 0, newLocations, 0, cursor.locationCount);
                    cursor.locations = newLocations;
                    Object[] newData = new Object[newLocations.length];
                    System.arraycopy(cursor.data, 0, newData, 0, cursor.locationCount);
                    cursor.data = newData;
                    break;
                }
 
                // Don't let the split value be the same as the upper value as
                // can happen due to rounding errors!
                if (cursor.splitValue == cursor.maxLimit[cursor.splitDimension]) {
                    cursor.splitValue = cursor.minLimit[cursor.splitDimension];
                }
 
                // Create child leaves
                KdTree<T> left = new ChildNode(cursor, false);
                KdTree<T> right = new ChildNode(cursor, true);
 
                // Move locations into children
                for (int i = 0; i < cursor.locationCount; i++) {
                    double[] oldLocation = cursor.locations[i];
                    Object oldData = cursor.data[i];
                    if (oldLocation[cursor.splitDimension] > cursor.splitValue) {
                        // Right
                        right.locations[right.locationCount] = oldLocation;
                        right.data[right.locationCount] = oldData;
                        right.locationCount++;
                        right.extendBounds(oldLocation);
                    }
                    else {
                        // Left
                        left.locations[left.locationCount] = oldLocation;
                        left.data[left.locationCount] = oldData;
                        left.locationCount++;
                        left.extendBounds(oldLocation);
                    }
                }
 
                // Make into stem
                cursor.left = left;
                cursor.right = right;
                cursor.locations = null;
                cursor.data = null;
            }
 
            cursor.locationCount++;
            cursor.extendBounds(location);
 
            if (location[cursor.splitDimension] > cursor.splitValue) {
                cursor = cursor.right;
            }
            else {
                cursor = cursor.left;
            }
        }
 
        cursor.locations[cursor.locationCount] = location;
        cursor.data[cursor.locationCount] = value;
        cursor.locationCount++;
        cursor.extendBounds(location);
 
        if (this.sizeLimit != null) {
            this.locationStack.add(location);
            if (this.locationCount > this.sizeLimit) {
                this.removeOld();
            }
        }
    }
 
    /**
     * Extends the bounds of this node do include a new location
     */
    private final void extendBounds(double[] location) {
        if (minLimit == null) {
            minLimit = new double[dimensions];
            System.arraycopy(location, 0, minLimit, 0, dimensions);
            maxLimit = new double[dimensions];
            System.arraycopy(location, 0, maxLimit, 0, dimensions);
            return;
        }
 
        for (int i = 0; i < dimensions; i++) {
            if (Double.isNaN(location[i])) {
                minLimit[i] = Double.NaN;
                maxLimit[i] = Double.NaN;
                singularity = false;
            }
            else if (minLimit[i] > location[i]) {
                minLimit[i] = location[i];
                singularity = false;
            }
            else if (maxLimit[i] < location[i]) {
                maxLimit[i] = location[i];
                singularity = false;
            }
        }
    }
 
    /**
     * Find the widest axis of the bounds of this node
     */
    private final int findWidestAxis() {
        int widest = 0;
        double width = (maxLimit[0] - minLimit[0]) * getAxisWeightHint(0);
        if (Double.isNaN(width)) width = 0;
        for (int i = 1; i < dimensions; i++) {
            double nwidth = (maxLimit[i] - minLimit[i]) * getAxisWeightHint(i);
            if (Double.isNaN(nwidth)) nwidth = 0;
            if (nwidth > width) {
                widest = i;
                width = nwidth;
            }
        }
        return widest;
    }
 
    /**
     * Remove the oldest value from the tree. Note: This cannot trim the bounds
     * of nodes, nor empty nodes, and thus you can't expect it to perfectly
     * preserve the speed of the tree as you keep adding.
     */
    private void removeOld() {
        double[] location = this.locationStack.removeFirst();
        KdTree<T> cursor = this;
 
        // Find the node where the point is
        while (cursor.locations == null) {
            if (location[cursor.splitDimension] > cursor.splitValue) {
                cursor = cursor.right;
            }
            else {
                cursor = cursor.left;
            }
        }
 
        for (int i = 0; i < cursor.locationCount; i++) {
            if (cursor.locations[i] == location) {
                System.arraycopy(cursor.locations, i + 1, cursor.locations, i, cursor.locationCount - i - 1);
                cursor.locations[cursor.locationCount-1] = null;
                System.arraycopy(cursor.data, i + 1, cursor.data, i, cursor.locationCount - i - 1);
                cursor.data[cursor.locationCount-1] = null;
                do {
                    cursor.locationCount--;
                    cursor = cursor.parent;
                } while (cursor.parent != null);
                return;
            }
        }
        // If we got here... we couldn't find the value to remove. Weird...
    }
 
    /**
     * Enumeration representing the status of a node during the running
     */
    private static enum Status {
        NONE, LEFTVISITED, RIGHTVISITED, ALLVISITED
    }
 
    /**
     * Stores a distance and value to output
     */
    public static class Entry<T> {
        public final double distance;
        public final T      value;
 
        private Entry(double distance, T value) {
            this.distance = distance;
            this.value = value;
        }
    }
 
    /**
     * Calculates the nearest 'count' points to 'location'
     */
    @SuppressWarnings("unchecked")
    public List<Entry<T>> nearestNeighbor(double[] location, int count, boolean sequentialSorting) {
        KdTree<T> cursor = this;
        cursor.status = Status.NONE;
        double range = Double.POSITIVE_INFINITY;
        ResultHeap resultHeap = new ResultHeap(count);
 
        do {
            if (cursor.status == Status.ALLVISITED) {
                // At a fully visited part. Move up the tree
                cursor = cursor.parent;
                continue;
            }
 
            if (cursor.status == Status.NONE && cursor.locations != null) {
                // At a leaf. Use the data.
                if (cursor.locationCount > 0) {
                    if (cursor.singularity) {
                        double dist = pointDist(cursor.locations[0], location);
                        if (dist <= range) {
                            for (int i = 0; i < cursor.locationCount; i++) {
                                resultHeap.addValue(dist, cursor.data[i]);
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < cursor.locationCount; i++) {
                            double dist = pointDist(cursor.locations[i], location);
                            resultHeap.addValue(dist, cursor.data[i]);
                        }
                    }
                    range = resultHeap.getMaxDist();
                }
 
                if (cursor.parent == null) {
                    break;
                }
                cursor = cursor.parent;
                continue;
            }
 
            // Going to descend
            KdTree<T> nextCursor = null;
            if (cursor.status == Status.NONE) {
                // At a fresh node, descend the most probably useful direction
                if (location[cursor.splitDimension] > cursor.splitValue) {
                    // Descend right
                    nextCursor = cursor.right;
                    cursor.status = Status.RIGHTVISITED;
                }
                else {
                    // Descend left;
                    nextCursor = cursor.left;
                    cursor.status = Status.LEFTVISITED;
                }
            }
            else if (cursor.status == Status.LEFTVISITED) {
                // Left node visited, descend right.
                nextCursor = cursor.right;
                cursor.status = Status.ALLVISITED;
            }
            else if (cursor.status == Status.RIGHTVISITED) {
                // Right node visited, descend left.
                nextCursor = cursor.left;
                cursor.status = Status.ALLVISITED;
            }
 
            // Check if it's worth descending. Assume it is if it's sibling has
            // not been visited yet.
            if (cursor.status == Status.ALLVISITED) {
                if (nextCursor.locationCount == 0
                        || (!nextCursor.singularity && pointRegionDist(location, nextCursor.minLimit,
                                nextCursor.maxLimit) > range)) {
                    continue;
                }
            }
 
            // Descend down the tree
            cursor = nextCursor;
            cursor.status = Status.NONE;
        } while (cursor.parent != null || cursor.status != Status.ALLVISITED);
 
        ArrayList<Entry<T>> results = new ArrayList<Entry<T>>(resultHeap.values);
        if (sequentialSorting) {
            while (resultHeap.values > 0) {
                resultHeap.removeLargest();
                results.add(new Entry<T>(resultHeap.removedDist, (T)resultHeap.removedData));
            }
        }
        else {
            for (int i = 0; i < resultHeap.values; i++) {
                results.add(new Entry<T>(resultHeap.distance[i], (T)resultHeap.data[i]));
            }
        }
 
        return results;
    }
 
    // Override in subclasses
    protected abstract double pointDist(double[] p1, double[] p2);
 
    protected abstract double pointRegionDist(double[] point, double[] min, double[] max);
 
    protected double getAxisWeightHint(int i) {
        return 1.0;
    }
 
    /**
     * Internal class for child nodes
     */
    private class ChildNode extends KdTree<T> {
        private ChildNode(KdTree<T> parent, boolean right) {
            super(parent, right);
        }
 
        // Distance measurements are always called from the root node
        protected double pointDist(double[] p1, double[] p2) {
            throw new IllegalStateException();
        }
 
        protected double pointRegionDist(double[] point, double[] min, double[] max) {
            throw new IllegalStateException();
        }
    }
 
    /**
     * Class for tree with Weighted Squared Euclidean distancing
     */
    public static class WeightedSqrEuclid<T> extends KdTree<T> {
        private double[] weights;
 
        public WeightedSqrEuclid(int dimensions, Integer sizeLimit) {
            super(dimensions, sizeLimit);
            this.weights = new double[dimensions];
            Arrays.fill(this.weights, 1.0);
        }
 
        public void setWeights(double[] weights) {
            this.weights = weights;
        }
 
        protected double getAxisWeightHint(int i) {
            return weights[i];
        }
 
        protected double pointDist(double[] p1, double[] p2) {
            double d = 0;
 
            for (int i = 0; i < p1.length; i++) {
                double diff = (p1[i] - p2[i]) * weights[i];
                if (!Double.isNaN(diff)) {
                    d += diff * diff;
                }
            }
 
            return d;
        }
 
        protected double pointRegionDist(double[] point, double[] min, double[] max) {
            double d = 0;
 
            for (int i = 0; i < point.length; i++) {
                double diff = 0;
                if (point[i] > max[i]) {
                    diff = (point[i] - max[i]) * weights[i];
                }
                else if (point[i] < min[i]) {
                    diff = (point[i] - min[i]) * weights[i];
                }
 
                if (!Double.isNaN(diff)) {
                    d += diff * diff;
                }
            }
 
            return d;
        }
    }
 
    /**
     * Class for tree with Unweighted Squared Euclidean distancing
     */
    public static class SqrEuclid<T> extends KdTree<T> {
        public SqrEuclid(int dimensions, Integer sizeLimit) {
            super(dimensions, sizeLimit);
        }
 
        protected double pointDist(double[] p1, double[] p2) {
            double d = 0;
 
            for (int i = 0; i < p1.length; i++) {
                double diff = (p1[i] - p2[i]);
                if (!Double.isNaN(diff)) {
                    d += diff * diff;
                }
            }
 
            return d;
        }
 
        protected double pointRegionDist(double[] point, double[] min, double[] max) {
            double d = 0;
 
            for (int i = 0; i < point.length; i++) {
                double diff = 0;
                if (point[i] > max[i]) {
                    diff = (point[i] - max[i]);
                }
                else if (point[i] < min[i]) {
                    diff = (point[i] - min[i]);
                }
 
                if (!Double.isNaN(diff)) {
                    d += diff * diff;
                }
            }
 
            return d;
        }
    }
 
    /**
     * Class for tree with Weighted Manhattan distancing
     */
    public static class WeightedManhattan<T> extends KdTree<T> {
        private double[] weights;
 
        public WeightedManhattan(int dimensions, Integer sizeLimit) {
            super(dimensions, sizeLimit);
            this.weights = new double[dimensions];
            Arrays.fill(this.weights, 1.0);
        }
 
        public void setWeights(double[] weights) {
            this.weights = weights;
        }
 
        protected double getAxisWeightHint(int i) {
            return weights[i];
        }
 
        protected double pointDist(double[] p1, double[] p2) {
            double d = 0;
 
            for (int i = 0; i < p1.length; i++) {
                double diff = (p1[i] - p2[i]);
                if (!Double.isNaN(diff)) {
                    d += ((diff < 0) ? -diff : diff) * weights[i];
                }
            }
 
            return d;
        }
 
        protected double pointRegionDist(double[] point, double[] min, double[] max) {
            double d = 0;
 
            for (int i = 0; i < point.length; i++) {
                double diff = 0;
                if (point[i] > max[i]) {
                    diff = (point[i] - max[i]);
                }
                else if (point[i] < min[i]) {
                    diff = (min[i] - point[i]);
                }
 
                if (!Double.isNaN(diff)) {
                    d += diff * weights[i];
                }
            }
 
            return d;
        }
    }
 
    /**
     * Class for tree with Manhattan distancing
     */
    public static class Manhattan<T> extends KdTree<T> {
        public Manhattan(int dimensions, Integer sizeLimit) {
            super(dimensions, sizeLimit);
        }
 
        protected double pointDist(double[] p1, double[] p2) {
            double d = 0;
 
            for (int i = 0; i < p1.length; i++) {
                double diff = (p1[i] - p2[i]);
                if (!Double.isNaN(diff)) {
                    d += (diff < 0) ? -diff : diff;
                }
            }
 
            return d;
        }
 
        protected double pointRegionDist(double[] point, double[] min, double[] max) {
            double d = 0;
 
            for (int i = 0; i < point.length; i++) {
                double diff = 0;
                if (point[i] > max[i]) {
                    diff = (point[i] - max[i]);
                }
                else if (point[i] < min[i]) {
                    diff = (min[i] - point[i]);
                }
 
                if (!Double.isNaN(diff)) {
                    d += diff;
                }
            }
 
            return d;
        }
    }
 
    /**
     * Class for tracking up to 'size' closest values
     */
    private static class ResultHeap {
        private final Object[] data;
        private final double[] distance;
        private final int      size;
        private int            values;
        public Object          removedData;
        public double          removedDist;
 
        public ResultHeap(int size) {
            this.data = new Object[size];
            this.distance = new double[size];
            this.size = size;
            this.values = 0;
        }
 
        public void addValue(double dist, Object value) {
            // If there is still room in the heap
            if (values < size) {
                // Insert new value at the end
                data[values] = value;
                distance[values] = dist;
                upHeapify(values);
                values++;
            }
            // If there is no room left in the heap, and the new entry is lower
            // than the max entry
            else if (dist < distance[0]) {
                // Replace the max entry with the new entry
                data[0] = value;
                distance[0] = dist;
                downHeapify(0);
            }
        }
 
        public void removeLargest() {
            if (values == 0) {
                throw new IllegalStateException();
            }
 
            removedData = data[0];
            removedDist = distance[0];
            values--;
            data[0] = data[values];
            distance[0] = distance[values];
            downHeapify(0);
        }
 
        private void upHeapify(int c) {
            for (int p = (c - 1) / 2; c != 0 && distance[c] > distance[p]; c = p, p = (c - 1) / 2) {
                Object pData = data[p];
                double pDist = distance[p];
                data[p] = data[c];
                distance[p] = distance[c];
                data[c] = pData;
                distance[c] = pDist;
            }
        }
 
        private void downHeapify(int p) {
            for (int c = p * 2 + 1; c < values; p = c, c = p * 2 + 1) {
                if (c + 1 < values && distance[c] < distance[c + 1]) {
                    c++;
                }
                if (distance[p] < distance[c]) {
                    // Swap the points
                    Object pData = data[p];
                    double pDist = distance[p];
                    data[p] = data[c];
                    distance[p] = distance[c];
                    data[c] = pData;
                    distance[c] = pDist;
                }
                else {
                    break;
                }
            }
        }
 
        public double getMaxDist() {
            if (values < size) {
                return Double.POSITIVE_INFINITY;
            }
            return distance[0];
        }
    }
  
    
    public static void main (String[] args)
    {
    	Random r = new Random();

    	int d=1000000;
    	int N = 1000000; //T.length();
    	int k = 1;
    	
		//KdTree <Double> kt = new WeightedManhattan<Double>(k,d);
    	KdTree <Double> kt = new Manhattan<Double>(k,d);
    	
    	
    	double[][] kcoordinate = new double[N][k];
    	
    	
    	//Creo matrice random di coordinate
    	for (int i=0; i<N; i++){
        	for (int j=0; j<k; j++){
        		kcoordinate[i][j] = r.nextInt(100); 
        	}
    	}
    	
    	//Visualizzo la matrice random di coordinate
    	for (int i=0; i<N; i++){
        	for (int j=0; j<k; j++){
        		//System.out.print( kcoordinate[i][j]+ "    ");
        		}
        	//System.out.println();
    	}
    	List<Entry<Double>> nodlist = null;//new List<Entry<Double>> ();
    	double riga[] = new double[k];
    	
    	
    	//prelevo le righe di questa matrice
		

    	for (int i=0; i<N; i++){
    		for (int j=0; j<k; j++){
    		//System.out.print( "kcoordinates[i] = "+kcoordinate[j][i] + " ");
    		riga [j] = kcoordinate[i][j];
    		//System.out.print( "riga di j = "+riga [j]+ "\t");
    		}

    		//System.out.println( "riga di i = "+riga [i]);
        	//System.out.print("riga= "+riga+ " ");
    		kt.addPoint( riga , (double)i); //trasformare il metodo addpoint in modo che usa ArrayList
    		//Ricerca
    		//nodlist = kt.nearestNeighbor(riga, N, true);
    		
    		//System.out.println("Nodo trovato: "+kt.nearestNeighbor(riga, 4, true).get(i).distance+ " "  );
    		//System.out.print( "\nriga che passo = "+riga [i]+ "con i = "+i);
    	}
    	
    	
    	double startTime = System.currentTimeMillis();
    	
    	
    	nodlist = kt.nearestNeighbor(riga, N, true);
    	
		double endTime = System.currentTimeMillis();

		double seconds = (endTime - startTime);

		
    	//for(int i=0; i<nodlist.size(); i++)
	   // System.out.println("Nodo:"+nodlist.get(i).value+ " "  );

	  //  System.out.println("Dimensione albero: "+nodlist.size()  );
		System.out.printf("\nProgramma eseguito in %.5f secondi", seconds/1000);

    	
    	
    	}
}

