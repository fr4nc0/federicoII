/*
 * SwProfileTest.java
 *
 * Created on April 4, 2006, 10:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.test;

import java.io.File;
import java.lang.String;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;
import net.vja2.research.distancemetrics.*;
import net.vja2.research.util.*;


/**
 *
 * @author vja2
 */
public class SwProfileTest {
    
    public static void main(String[] args)
    {
        boolean verbose = false;
        if(args.length < 10)
        {
            System.err.println("Usage: java SwProfileTest datafile resultsfile matrixfile <tree size> <errors in tree> <successes in queries> <errors in queries> <k> <required failures> <tau> [-verbose]");
            System.exit(1);
        }
        else if(args.length == 11 && args[10].equals("-verbose"))
            verbose = true;
        
        String datafile = args[0],
                resultfile = args[1],
                matrixfile = args[2];
        
        int treeSize = Integer.parseInt(args[3]),
                errorsInTraining = Integer.parseInt(args[4]),
                successesInTest = Integer.parseInt(args[5]),
                errorsInTest = Integer.parseInt(args[6]),
                k = Integer.parseInt(args[7]),
                requiredFailures = Integer.parseInt(args[8]);       // number of failures in the nearest neighbor
                                                                    // set needed to mark a query as failed.
        double tau = Double.parseDouble(args[9]);
        
        int constructionCost = 0,                                   // cost of constructing the tree
            totalSuccesses = 0,                                     // number of successful runs correctly marked.
            totalFailures = 0;                                      // number of failures correctly marked.
        
        if(k < requiredFailures)
            requiredFailures = k;
        
        int successesInTraining = treeSize - errorsInTraining;
        int testSetSize = successesInTest + errorsInTest;
        
        if(verbose)
        {
            System.out.printf("Data: %s\nDissimilarity Matrix: %s\n", datafile, matrixfile);
            System.out.printf("\nTree Size: %d (%d errors)\nNumber of Queries: %d (%d errors)\nk-value: %d\nrequired successful neighbors: %d\n",
                           treeSize, errorsInTraining, testSetSize, errorsInTest, k, requiredFailures);
        }
        
        try {
            ArrayList<SoftwareProfile> profiles = read(new File(datafile), new File(resultfile));
            DissimilarityMatrix matrix = new DissimilarityMatrix(new File(matrixfile));
            
            Random rng = new Random();
            ArrayList<SoftwareProfile> trainingSet,
                                        testSet = new ArrayList<SoftwareProfile>(testSetSize);
            
            // create two disjoint subsets, one for building the tree (training) and one for testing.
            trainingSet = createDisjointSubset(profiles, testSet, successesInTraining, errorsInTraining, rng);
            testSet = createDisjointSubset(profiles, trainingSet, successesInTest, errorsInTest, rng);
            
            // initialize the distance metric, using the disimilarity matrix. Monitored Distance Metric is used
            // to keep track of the number of times .distance() is called.
            MonitoredDistanceMetric<SoftwareProfile> mdm =
                        new MonitoredDistanceMetric(new CachedDistanceMetric(profiles, matrix));
            
            VantagePointTree<SoftwareProfile> vptree = new VantagePointTree<SoftwareProfile>(mdm, trainingSet);
            constructionCost = MonitoredDistanceMetric.count();
            
            if(verbose)
            {
                System.out.printf("\nTree Construction Cost: %d distance computations\n", constructionCost);
                System.out.printf("Tree Height: %d\n", vptree.height());

                System.out.println("\nstarting queries...");
            }

            for(SoftwareProfile q : testSet)
            {
                ArrayList<SoftwareProfile> results = vptree.search(q, tau, k);
                
                boolean queryPassed = true;
                int numFailures = 0;
                
                // loop through all the results and keep a tally of how many failures there are.
                // if the number of failures is >= requiredFailures, then mark this profile a failure.
                for(SoftwareProfile r : results)
                {
                    if(!r.status())
                        numFailures++;
                    if(numFailures >= requiredFailures)
                    {
                        queryPassed = false;
                        break;
                    }
                }
                
                // if this was a positive match, increment totalSuccesses or total failures as needed.
                if(queryPassed == q.status())
                {
                    if(q.status())
                        totalSuccesses++;
                    else
                        totalFailures++;
                }
                
            }
            
            double passSuccess = (double) totalSuccesses / (double) successesInTest;
            double failSuccess = (double) totalFailures / (double) errorsInTest;
            
            double cost = (double) (MonitoredDistanceMetric.count() - constructionCost) / (double) testSet.size();
            
            if(verbose)
            {
                System.out.printf("%f distance computations / query!\n", cost);
                System.out.printf("%f success rate for good runs\n", passSuccess);
                System.out.printf("%f success rate for bad runs\n", failSuccess);
                
            }
            else
                System.out.printf("%d,%d,%d,%d,%d,%d,%f,%d,%f,%f,%f\n",
                                    treeSize,errorsInTraining,successesInTest,errorsInTest,k,
                                    requiredFailures,tau,constructionCost,cost,passSuccess,failSuccess);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * reads in profile names and results, and returns an ArrayList of SoftwareProfiles.
     * @param file object referring to the list of profiles.
     * @param file object referring to the test results corresponding to the profiles in profiles.
     * @return an ArrayList of software profiles.
     * @throws IOException if one of the files can't be read.
     */
    public static ArrayList<SoftwareProfile> read(File profiles, File states) throws IOException {
        BufferedReader pInput = new BufferedReader(new FileReader(profiles)),
                        sInput = new BufferedReader(new FileReader(states));
        
        ArrayList<SoftwareProfile> data = new ArrayList<SoftwareProfile>();
        
        while(pInput.ready() && sInput.ready())
            data.add(new SoftwareProfile(pInput.readLine(), Integer.valueOf(sInput.readLine()) == 0));
        pInput.close();
        sInput.close();
        
        return data;
    }
    
    /**
     * this creates a subset of set that is disjoint from subset. This can probably be heavily optimized.
     * @param set the set from which the new subset will be created.
     * @param subset the set which our new set must be disjoint from.
     * @param size the size of the new set.
     * @param numErrors the number of failed profiles in the new set.
     * @param rng random number generator for randomly selecting elements.
     * @return a new set with the specified # of errors, which is a subset of set and disjoint from subset.
     */
    public static ArrayList<SoftwareProfile> createDisjointSubset(ArrayList<SoftwareProfile> set, ArrayList<SoftwareProfile> subset, int numSuccesses, int numErrors, Random rng)
    {
        int size = numSuccesses + numErrors, errorsSoFar = 0, successesSoFar = 0;
        ArrayList<SoftwareProfile> newset = new ArrayList<SoftwareProfile>(size);
        
        while(newset.size() < size)
        {
            int i = rng.nextInt(set.size());
            SoftwareProfile obj = set.get(rng.nextInt(set.size()));
            
            if(!newset.contains(obj) && !subset.contains(obj))
            {
                if(obj.status())
                {
                    if(successesSoFar < numSuccesses)
                    {
                        successesSoFar++;
                        newset.add(obj);
                    }
                }
                else
                {
                    if(errorsSoFar < numErrors)
                    {
                        errorsSoFar++;
                        newset.add(obj);
                    }
                }
            }
        }

        return newset;
    }
    
    public static class SoftwareProfile {

        /** Creates a new instance of SoftwareProfile */
        public SoftwareProfile(String name, boolean status) {
            this.name = name;
            this.status = status;
        }

        public boolean status() { return this.status; }

        public String name() { return this.name; }

        public String toString() { return this.name; }

        private String name;
        private boolean status;
    }
    
    public static class TestResult
    {
        public TestResult(int trainingSetSize, int errorsInTraining, int testSetSize, int errorsInTest, int k, double tau)
        {
            this.trainingSetSize = trainingSetSize;
            this.errorsInTraining = errorsInTraining;
            this.testSetSize = testSetSize;
            this.errorsInTest = errorsInTest;
            this.k = k;
            this.tau = tau;
            
            this.constructionCost = 0;
            this.totalCost = 0;
            this.successes = 0;
        }
        
        public String toString()
        {
//            StringBuilder buf = new StringBuilder();
            
            StringWriter buf = new StringWriter();
            PrintWriter output = new PrintWriter(buf);
            output.printf("%d,%d,%d,%d,%d,%f,%d,%f,%f",
                            trainingSetSize, errorsInTraining, testSetSize, errorsInTest, k, tau, constructionCost,
                            (double) (totalCost - constructionCost) / testSetSize, (double) successes / (double) testSetSize);
            
            return buf.toString();
        }
        // test input parameters
        public int trainingSetSize;
        public int errorsInTraining;
        public int testSetSize;
        public int errorsInTest;
        public int k;
        public double tau;
        
        // test results
        public int constructionCost;
        public double totalCost;
        public int successes;
    }
}
