/* $Id: FastaProtein.java 10 2005-12-25 01:55:01Z vja2 $ */
package net.vja2.research.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is a representation of a protein in FASTA format.
 * @author vja2
 * @see <a href="http://en.wikipedia.org/wiki/FASTA">Wikipedia entry on FASTA</a>
 * @see <a href="http://en.wikipedia.org/wiki/FASTA_format">Wikipedia entry on the FASTA file format</a>
 * @see <a href="http://www.ncbi.nlm.nih.gov/BLAST/fasta.shtml">NCBI description of the FASTA file format</a>
 */
public class FastaProtein {
	/**
	 * 
	 * @param name the name/unique identifier of this protein
	 * @param protein protein sequence information
	 */
	public FastaProtein(String name, String protein)
	{
		this.name = name;
		this.protein = protein;
	}
	
	/**
	 * 
	 * @return the name (a unique identifier) for this protein.
	 */
	public String getName() { return this.name; }
	
	/**
	 * 
	 * @return the protein sequence information for this protein.
	 */
	public String getProteinDef() { return this.protein; }
	
	/**
	 * We have overridden {@link java.lang.Object#toString()} to facilitate comparisons between proteins.
	 * @return the protein sequence information for this protein.
	 * {@inheritDoc}
	 */
	public String toString() { return this.getProteinDef(); }
	
	/**
	 * 
	 * @param file {@link java.io.File} object that contains a dataset of proteins in FASTA format.
	 * @return a {@link java.util.Vector} containing all the proteins from the file.
	 * @throws IOException
	 */
	public static ArrayList<FastaProtein> readFastaFile(File file) throws IOException
	{
		ArrayList<FastaProtein> dataset = new ArrayList<FastaProtein>();

		BufferedReader in = new BufferedReader(new FileReader(file));
		String name = null, protein = new String(""), line;
		
		while(in.ready())
		{
			line = in.readLine();
			
			if(line.startsWith(">"))
			{
				if(name != null)
				{
					dataset.add(new FastaProtein(name, protein));
					protein = new String("");
				}
				name = line.substring(1);
			}
			else
				protein = protein.concat(line);
		}
		dataset.add(new FastaProtein(name, protein));
		in.close();
		
		return dataset;
	}
	
	/**
	 * 
	 * @param filename a string representing a path to a file containing proteins in FASTA format.
	 * @return a {@link java.util.Vector} containing all the proteins found at filename.
	 * @throws IOException
	 */
	public static ArrayList<FastaProtein> readFastaFile(String filename) throws IOException
	{
		return readFastaFile(new File(filename));
	}
	
	private String name;
	private String protein;
}
