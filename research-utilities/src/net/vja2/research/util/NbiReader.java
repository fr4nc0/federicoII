/*
 * NbiReader.java
 *
 * Created on April 25, 2006, 9:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.PrintWriter;
import java.lang.Math;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author vja2
 */
public class NbiReader {
    
    /** Creates a new instance of NbiReader */
    public NbiReader() {
    }
    
    public static void main(String args[]) {
        
        try {
            File file = new File(args[0]);
            File csv = new File(args[1]);
            //File file = new File("/Users/vja2/data/jtidy/fn.nbi");
            //File csv = new File("/Users/vja2/data/jtidy/fn.txt");
            
            int numComputations = 100;
            FileChannel out = (new RandomAccessFile(file, "r")).getChannel();
            MappedByteBuffer buffer = out.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            
            
            buffer = buffer.load();
            
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            
            int totalRows = buffer.getInt(0);
            int rowFrom = buffer.getInt(4);
            int rowTo = buffer.getInt(8);
            int columns = buffer.getInt(12);
            
            System.out.printf("row-from:%d\nrow-to:%d\ncolumns:%d\n", rowFrom, rowTo, columns);
            int numProfiles = rowTo - rowFrom;
            
            if(numProfiles < totalRows)
                System.out.println("number of profiles is less than the total.");
            else if(numProfiles == totalRows)
                System.out.printf("file contains %d profiles with %d features each.\n", totalRows, columns);
            
            System.out.print("reading... ");
            double data[][] = new double[numProfiles][columns];
            
            long time = System.currentTimeMillis();
            for(int i = rowFrom; i < rowTo; i++)
            {
                for(int j = 0; j < columns; j++)
                {
                    data[i][j] = buffer.getDouble((i * columns + j) * 8 + 16);
                    //if(data[i][j] != 0)
                    //    System.out.printf("(%d,%d) is greater than zero(%f)\n", i, j, data[i][j]);
                }
            }
            System.out.printf("%d profiles(%d ms).\n", numProfiles, System.currentTimeMillis() - time);
            
            out.close();
            
            PrintWriter pw = new PrintWriter(csv);
            for(int i = 0; i < numProfiles; i++)
            {
                for(int j = 0; j < columns; j++)
                {
                    pw.print(data[i][j]);
                    if(j < columns-1)
                        pw.print(' ');
                    else
                        pw.println();
                }
            }
            pw.close();
            
            
            /*double matrix[][] = new double[numProfiles][numProfiles];
            
            System.out.print("constructing matrix... ");
            time = System.currentTimeMillis();
            for(int i = 0; i < numComputations; i++)
            {
                for(int j = 0; j < numComputations; j++)
                    matrix[i][j] = dist(data, i, j);
            }
            time = System.currentTimeMillis() - time;
            int num = numComputations * numComputations;
            double avg = ((double) time / num);
            System.out.printf("%d distances calculated in %d ms (%f ms / calculation).\n", num, time, avg);
            System.out.printf("total matrix should take %f ms to compute.\n", avg * (numProfiles * numProfiles));*/
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private static double dist(double[][] data, int i, int j) {
        double sum = 0.0;
        for(int pos = 0; pos < data[i].length; pos++)
            sum += Math.pow(data[i][pos], 2) - Math.pow(data[j][pos], 2);
        return Math.sqrt(sum);
    }
}
