/*
 * AbstractLinkage.java
 *
 * Created on April 19, 2006, 11:51 AM
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
public abstract class AbstractLinkage<E> implements ILinkage<E> {
    
    /** Creates a new instance of AbstractLinkage */
    public AbstractLinkage(IDistanceMetric<E> dist) {
        this.dm = dist;
    }
    
    public abstract double distance(Cluster<E> a, Cluster<E> b);
    
    protected IDistanceMetric<E> dm;
}
