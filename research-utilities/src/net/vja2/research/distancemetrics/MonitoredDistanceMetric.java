/*
 * MonitoredDistanceMetric.java
 *
 * Created on April 5, 2006, 10:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.distancemetrics;

/**
 *
 * @author vja2
 */
public class MonitoredDistanceMetric<E> implements IDistanceMetric<E> {
    
    /** Creates a new instance of MonitoredDistanceMetric */
    public MonitoredDistanceMetric(IDistanceMetric<E> dm) {
        this.dm = dm;
    }
    
    public double distance(E a, E b) {
        ++MonitoredDistanceMetric.count;
        return dm.distance(a,b);
    }
    
    public static int count() { return MonitoredDistanceMetric.count; }
    
    public static void reset() { count = 0; }
    
    IDistanceMetric<E> dm;
    private static int count = 0;
}
