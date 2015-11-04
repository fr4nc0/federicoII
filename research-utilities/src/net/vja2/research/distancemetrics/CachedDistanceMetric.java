/*
 * CachedDistanceMetric.java
 *
 * Created on February 23, 2006, 1:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.distancemetrics;

import java.util.AbstractList;
import net.vja2.research.util.AbstractDissimilarityMatrix;

/**
 *
 * @author vja2
 */
public class CachedDistanceMetric<E> implements IDistanceMetric<E> {
    
    /** Creates a new instance of CachedDistanceMetric */
    public CachedDistanceMetric(AbstractList<E> data, AbstractDissimilarityMatrix matrix) throws Exception
    {
        if(data.size() == matrix.size())
        {
            this.data = data;
            this.matrix = matrix;
        }
        else
            throw new Exception("The number of matrix dimensions and the length of the dataset must match!");
    }
    
    public double distance(E a, E b)
    {
        return this.matrix.get(this.data.indexOf(a), this.data.indexOf(b));
    }
    
    private AbstractList<E> data;
    private AbstractDissimilarityMatrix matrix;
}
