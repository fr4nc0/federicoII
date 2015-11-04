/*
 * AgglomerativeCluster.java
 *
 * Created on April 18, 2006, 3:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.clustering;

import java.lang.Double;
import java.util.ArrayList;

/**
 *
 * @author vja2
 */
public class AgglomerativeCluster<E> extends Cluster<E> {
    
    public AgglomerativeCluster(E a)
    {
        this.items = new ArrayList<E>();
        this.items.add(a);
    }
    /**
     * Creates a new instance of AgglomerativeCluster
     */
    public AgglomerativeCluster(AgglomerativeCluster<E> a, AgglomerativeCluster<E> b) {
        this.parentA = a;
        this.parentB = b;
        
        this.items = new ArrayList<E>(a.size() + b.size());
        this.items.addAll(a.items);
        this.items.addAll(b.items);
    }
    
    public static <E> ArrayList<ArrayList<AgglomerativeCluster<E>>> cluster(ArrayList<E> data, ILinkage<E> linkMeasure)
    {
        ArrayList<ArrayList<AgglomerativeCluster<E>>> tree = new ArrayList<ArrayList<AgglomerativeCluster<E>>>(data.size());
        
        for(int i = 1; i <= data.size(); i++)
            tree.add(new ArrayList<AgglomerativeCluster<E>>(i));
        
        ArrayList<AgglomerativeCluster<E>> clusters = tree.get(tree.size() - 1);
        for(E i : data)
            clusters.add(new AgglomerativeCluster(i));
        
        for(int i = tree.size() - 1; i > 0; i--)
        {
            ArrayList<AgglomerativeCluster<E>> newClusters = (ArrayList<AgglomerativeCluster<E>>) clusters.clone();
            AgglomerativeCluster<E>[] closest = findClosest(newClusters, linkMeasure);
            
            for(AgglomerativeCluster<E> j : closest)
                newClusters.remove(j);
            
            newClusters.add(new AgglomerativeCluster<E>(closest[0], closest[1]));
            
            tree.get(i-1).addAll(newClusters);
            clusters = newClusters;
            tree.add(clusters);
        }
        
        return tree;
    }
    
    public static <E> AgglomerativeCluster<E>[] findClosest(ArrayList<AgglomerativeCluster<E>> clusters, ILinkage<E> linkMeasure)
    {
        double min = Double.MAX_VALUE, dist = 0;
        AgglomerativeCluster<E> a = null, b = null;
        
        for(int i = 0; i < clusters.size(); i++)
        {
            for(int j = i + 1; j < clusters.size(); j++)
            {
                dist = linkMeasure.distance(clusters.get(i),clusters.get(j));
                if(dist < min)
                {
                    min = dist;
                    a = clusters.get(i);
                    b = clusters.get(j);
                }
            }
        }
        
        AgglomerativeCluster[] closest = {a, b};
        return closest;
    }
    public AgglomerativeCluster<E> parentA;
    public AgglomerativeCluster<E> parentB;
}
