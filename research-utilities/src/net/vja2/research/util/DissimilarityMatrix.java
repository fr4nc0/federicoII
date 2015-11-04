/*
 * DissimilarityMatrix.java
 *
 * Created on February 22, 2006, 12:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

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
 *
 * @author vja2
 */
public class DissimilarityMatrix extends AbstractDissimilarityMatrix {
    
    public DissimilarityMatrix(File file, int size) throws IOException
    {
        super(file, size);
    }
    
    /** Creates a new instance of DissimilarityMatrix */
    public DissimilarityMatrix(File file) throws IOException
    {
        super(file);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.size = this.buffer.getInt(0);
    }
    
    /** creates a new MappedByteBuffer */
    protected void createBuffer(File file, int size) throws IOException
    {
        if(!file.exists())
        {
            FileChannel out = (new RandomAccessFile(file, "rw")).getChannel();
            this.buffer = out.map(FileChannel.MapMode.READ_WRITE, 0, size * size + 16);
            out.close();
            
            this.buffer.putInt(this.size).putInt(0).putInt(this.size).putInt(this.size);
            this.buffer = this.buffer.load();
        }
        else
            throw new IOException("File exists!");
    }
    
    /** */
    protected int getIndexOf(int i, int j) throws IndexOutOfBoundsException
    {
        if(i >= this.size || j >= this.size)
            throw new IndexOutOfBoundsException();
        
        return (i * this.size + j) * 8 + 16;
    }
}
