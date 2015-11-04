/*
 * Cluster.java
 *
 * Created on April 18, 2006, 3:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.clustering;

import java.util.ArrayList;

/**
 *
 * @author vja2
 */
public class Cluster<E> {
    
    /** Creates a new instance of Cluster */
    public Cluster() {

    }
    
    public ArrayList<E> items() { return items; }
    
    public int size() { return items.size(); }
    
    protected ArrayList<E> items;
}
