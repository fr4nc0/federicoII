/* $Id: DissimilarityMatrix.java 12 2006-02-15 17:40:37Z vja2 $ */
package net.vja2.research.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.IndexOutOfBoundsException;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import java.util.AbstractList;

/**
 * The dissimilarity matrix class handles reading and writing dissimilarity matrices to disk.
 * @author vja2
 *
 */
public class CompactDissimilarityMatrix extends AbstractDissimilarityMatrix {

    /**
     *
     */
    public CompactDissimilarityMatrix(File file, int size) throws IOException
    {
        super(file, size);
    }
    
    /**
     * 
     * @param file the file to either write the data to or read the data from. If the file exists, we will read from it.
     * @throws IOException if the file cannot be read.
     */
    public CompactDissimilarityMatrix(File file) throws IOException
    {	
        // load the buffer into physical memory. This seems to result in a speed increase for larger matrices.
        super(file);
        
        this.size = this.buffer.getInt();
        this.buffer.order(this.buffer.getInt() == 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
    }


    /**
     * This function creates the an array that contains the starting array indices for each row of the matrix.
     *
     */
    private void createIndex()
    {
        index = new int[this.size];
        index[0] = 0;
        for(int i = 1; i < this.size; i++)
            index[i] = index[i - 1] + this.size - i;
    }

    /**
     * creates the initial BDM (Binary Dissimilarity Matrix) file.
     * @param file the file to be created.
     * @throws IOException if the given file cannot be written to.
     */
    protected void createBuffer(File file, int size) throws IOException
    {
        // create the buffer given file
        FileChannel out = (new RandomAccessFile(file, "rw")).getChannel();
        this.buffer = out.map(FileChannel.MapMode.READ_WRITE, 0, getIndexOf(size-2, size-1));
        out.close();
        // put header information: consists of
        //	1. number of rows/columns in the matrix.
        //	2. the byte ordering. 0 indicates Big Endian; 1 indicates Little Endian.
        this.buffer.putInt(this.size);
        this.buffer.putInt(buffer.order() == ByteOrder.LITTLE_ENDIAN ? 0 : 1);

        this.buffer.force();
    }

    /**
     * 
     * @param i an index to an object.
     * @param j an index to another object.
     * @return the index in the matrix that contains the distance between the two objects refered to by these indices.
     * @throws IndexOutOfBoundsException if either index is out of bounds.
     */
    protected int getIndexOf(int i, int j) throws IndexOutOfBoundsException
    {
            if(i >= this.size || j >= this.size)
                    throw new IndexOutOfBoundsException();
            return ((i < j ? index[i] + j - i : index[j] + i - j) - 1) * 8 + 8;
    }

    private int[] index;
}

