/*
 * SingleLink.java
 *
 * Created on April 18, 2006, 5:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.clustering;

import java.lang.Double;
import java.util.Iterator;
import net.vja2.research.distancemetrics.*;
/**
 *
 * @author vja2
 */
public class SingleLink<E> extends AbstractLinkage<E> {
    
    /** Creates a new instance of SingleLink */
    public SingleLink(IDistanceMetric<E> dist) { super(dist); }
    
    public double distance(Cluster<E> a, Cluster<E> b)
    {
        double min = Double.MAX_VALUE, dist = 0;
        
        for(E i : a.items())
        {
            for(E j : b.items())
            {
                dist = dm.distance(i,j);
                if(dist < min)
                    min = dist;
            }
        }
        return min;
    }
}
