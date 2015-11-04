/*
 * AverageLink.java
 *
 * Created on April 18, 2006, 5:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.clustering;

import net.vja2.research.distancemetrics.*;
/**
 *
 * @author vja2
 */
public class AverageLink<E> extends AbstractLinkage<E>{
    
    /** Creates a new instance of AverageLink */
    public AverageLink(IDistanceMetric<E> dist) { super(dist); }
    
    public double distance(Cluster<E> a, Cluster<E> b)
    {
        double distanceTotal = 0.0;
        for(E i : a.items())
            for(E j : b.items())
                distanceTotal += dm.distance(i,j);
        
        return distanceTotal / (double) (a.size() * b.size());
    }
}
