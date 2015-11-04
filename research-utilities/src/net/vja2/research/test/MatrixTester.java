/*
 * MatrixTester.java
 *
 * Created on February 23, 2006, 12:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import net.vja2.research.util.*;
import net.vja2.research.util.FastMap;
import net.vja2.research.distancemetrics.CachedDistanceMetric;
import net.vja2.research.distancemetrics.IDistanceMetric;

/**
 *
 * @author vja2
 */
public class MatrixTester {
    
    public static void main(String args[])
    {
        String filename = new String("/home/vja2/data/gcc-bb2/bb.b.dbi");
        String outputfile = new String("gcc_bb2.b.map");
        try {
        AbstractDissimilarityMatrix m = new DissimilarityMatrix(new File(filename));
        Random rng = new Random();

        System.out.printf("matrix size: %d\n", m.size());
        
        int successes = 0;
        for(int i = 0; i < 500; i++)
        {
            int j = rng.nextInt(m.size()),
                    k = rng.nextInt(m.size());
            
            if(m.get(j,k) == m.get(k,j))
                ++successes;
        }
        
        System.out.printf("Success rate: %g", ((double) successes / 500.0)*100.0);
        System.out.println("%");
        
        ArrayList<Integer> profiles = new ArrayList(m.size());
        
        for(int i = 0; i < m.size(); i++)
            profiles.add(i);
        
        CachedDistanceMetric cdm = new CachedDistanceMetric<Integer>(profiles, m);
        FastMap<Integer> mapper = new FastMap(profiles, cdm);
        FastMap.DataMapping<Integer> map = mapper.map(3);
        
        PrintWriter out = new PrintWriter(new FileOutputStream(outputfile));
        
        for(FastMap.MapPoint<Integer> mp : map.mappeddata)
            out.printf("%e\t%e\t%e\n", mp.map[0], mp.map[1], mp.map[2]);
        
        out.close();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Creates a new instance of MatrixTester */
    public MatrixTester() {
    }
    
}
