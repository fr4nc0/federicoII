/*
 * ILinkage.java
 *
 * Created on April 18, 2006, 4:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.clustering;

/**
 *
 * @author vja2
 */
public interface ILinkage<E> {
    double distance(Cluster<E> a, Cluster<E> b);
}
