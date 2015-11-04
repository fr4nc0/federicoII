/*
 * AbstractDissimilarityMatrix.java
 *
 * Created on February 21, 2006, 3:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.vja2.research.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.String;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;

/**
 *
 * @author vja2
 */
public abstract class AbstractDissimilarityMatrix {
    
    protected AbstractDissimilarityMatrix() { this.size = 0; }
    
    /** Creates a new instance of AbstractDissimilarityMatrix */
    public AbstractDissimilarityMatrix(File file, int size) throws IOException
    {
        this.size = size;
        this.createBuffer(file, this.size);
    }
    
    public AbstractDissimilarityMatrix(File file) throws IOException
    {
        this.openBuffer(file);
    }
        
    /**
     * gets values from the matrix
     * @param i the row number of the sought-after item.
     * @param j the column number of the sought-after item.
     * @return the value at [i,j] in the matrix.
     */
    public double get(int i, int j) throws IndexOutOfBoundsException
    {
        return this.buffer.getDouble(this.getIndexOf(i, j));
    }
    
    /**
     * puts the specified value into the matrix.
     * @param i the row number to put the value in.
     * @param j the column number to put the value in.
     * @param value the double value to put in [i,j].
     */
    public void put(int i, int j, double value) throws IndexOutOfBoundsException
    {
        this.buffer.putDouble(this.getIndexOf(i, j), value);
    }
    
    /**
     * opens a buffer specified by file. The file must exist.
     * @param file the file object to open.
     */
    protected void openBuffer(File file) throws IOException//, String mode)
    {
        if(file.exists())
        {
            // create the buffer from the given file
            FileChannel out = (new RandomAccessFile(file, "rw")).getChannel();
            this.buffer = out.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            out.close();
            this.buffer = this.buffer.load();
        }
        else
            throw new IOException("File does not exist!");
    }
    
    /**
     * creates a buffer specified by file. The file must not exist.
     * @param file the file to be created.
     */
    protected abstract void createBuffer(File file, int size) throws IOException;
    
    protected abstract int getIndexOf(int i, int j) throws IndexOutOfBoundsException;
    
    /**
     * returns the length on a side of this matrix.
     */
    public int size() { return this.size; }
    
    /** the number of rows & columns for this matrix */
    protected int size;
    
    /** a MappedByteBuffer that references the file holding the matrix */
    protected MappedByteBuffer buffer;
}
