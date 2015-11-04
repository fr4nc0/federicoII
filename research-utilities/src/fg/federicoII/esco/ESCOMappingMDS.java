package fg.federicoII.esco;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;

import mdsj.MDSJ;

public class ESCOMappingMDS {

	static DefaultTreeModel taxonomy;
	static ArrayList<EscoNode> escoTaxonomyNodes;

	static ESCODistanceImpl escoDistance = new ESCODistanceImpl(taxonomy);

	
	public static void main(String[] args) {

		/*
		double[][] input={        // input dissimilarity matrix
		    {0.00,2.04,1.92,2.35,2.06,2.12,2.27,2.34,2.57,2.43,1.90,2.41},
		    {2.04,0.00,2.10,2.00,2.23,2.04,2.38,2.36,2.23,2.36,2.57,2.34},
		    {1.92,2.10,0.00,1.95,2.21,2.23,2.32,2.46,1.87,1.88,2.41,1.97},
		    {2.35,2.00,1.95,0.00,2.05,1.78,2.08,2.27,2.14,2.14,2.38,2.17},
		    {2.06,2.23,2.21,2.05,0.00,2.35,2.23,2.18,2.30,1.98,1.74,2.06},
		    {2.12,2.04,2.23,1.78,2.35,0.00,2.21,2.12,2.21,2.12,2.17,2.23},
		    {2.27,2.38,2.32,2.08,2.23,2.21,0.00,2.04,2.44,2.19,1.74,2.13},
		    {2.34,2.36,2.46,2.27,2.18,2.12,2.04,0.00,2.19,2.09,1.71,2.17},
		    {2.57,2.23,1.87,2.14,2.30,2.21,2.44,2.19,0.00,1.81,2.53,1.98},
		    {2.43,2.36,1.88,2.14,1.98,2.12,2.19,2.09,1.81,0.00,2.00,1.52},
		    {1.90,2.57,2.41,2.38,1.74,2.17,1.74,1.71,2.53,2.00,0.00,2.33},
		    {2.41,2.34,1.97,2.17,2.06,2.23,2.13,2.17,1.98,1.52,2.33,0.00}
	        };

		 */

		taxonomy = 
				EscoParser.getTaxonomy("C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/4eb7cab7-57db-42a0-adcf-8d30a104dca6.xml");
		escoTaxonomyNodes = EscoParser.getNodeList();

		ESCODistanceImpl escoDistance = new ESCODistanceImpl(taxonomy);

		
		double[][] input = getMatrix();

		int n=input[0].length;    // number of data objects

		int maxNumDimensioni = 3;

		for (int numeroDimensioni = 3; numeroDimensioni <= maxNumDimensioni; numeroDimensioni++) {

			double[][] output=MDSJ.classicalScaling(input, numeroDimensioni); // apply MDS

			HashMap<String, Integer> riscontro = new HashMap<String, Integer>();
			File outputFile = new File("C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/MDS-MappedPoints " + numeroDimensioni + " dim.txt");
			File outputFileSivestro = new File("C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/MDS-MappedPoints Silvestro " + numeroDimensioni + " dim.txt");
			
			try {

				BufferedWriter writerSilvestro = new BufferedWriter(new FileWriter(outputFileSivestro));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
				for(int i = 0; i < n; i++) {  // output all coordinates

					/* silvestro
					 * T5304 <Azienda, Cerca, Gestore di ristorante>:0.01414323750661031;0.008926657276812432;0.003512906512548959 
					 */
					
					String line = escoTaxonomyNodes.get(i).getPreferredTerm();
					String silvestroLine = "T5304 <Azienda, Cerca, " + escoTaxonomyNodes.get(i).getPreferredTerm() + ">:";
					
					String coords = "";
					for ( int j = 0;  j < numeroDimensioni; j++ ) {
						coords = coords + " x[" + j + "]: " + output[j][i];
						line = line + ";" + output[j][i] ;
						silvestroLine = silvestroLine  + output[j][i] + ";" ;
					}
					silvestroLine = silvestroLine.substring(0, silvestroLine.length()-1);
					
					//System.out.println(coords);

					//writer.write (  ":" + coords );
					
					writer.write ( line );
					writerSilvestro.write ( silvestroLine );
					
					//writer.newLine();
					writer.newLine();
					writerSilvestro.newLine();

					Integer conteggio = riscontro.get(coords);
					if ( conteggio == null ) {
						conteggio = 1;
					} else {
						conteggio++;
					}

					riscontro.put(coords, conteggio);
				}
				writer.close();
				writerSilvestro.close();

				File ducplicatedFile = new File("C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/MDS-Duplicated " + numeroDimensioni + " dim.txt");
				BufferedWriter writer2 = new BufferedWriter(new FileWriter(ducplicatedFile));

				Iterator it = (Iterator) riscontro.entrySet().iterator();

				int maxDuplicati = 0;

				while ( it.hasNext() ) {

					Map.Entry pair = (Map.Entry)it.next();

					int duplicati = (Integer) pair.getValue();

					if (maxDuplicati < duplicati ) {
						maxDuplicati = duplicati;
					}
					String row = duplicati + " " + (String) pair.getKey();
					//System.out.println(row );
					writer2.write ( row );
					writer2.newLine();
					writer2.newLine();
				}

				writer2.write("Max duplicati:  " + maxDuplicati );
				System.out.println(numeroDimensioni + "  " + maxDuplicati );
				writer2.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("ESCOMappingMDS.main() - end");

	}

	private static double[][] getMatrix() {


		int numNodes = escoTaxonomyNodes.size();
		double[][] input = new  double[numNodes][numNodes];

		for ( int i = 0; i < numNodes; i++ ) {
			System.out.println();
			for ( int j = 0; j < numNodes; j++ ) {
				input[i][j] = escoDistance.distance(escoTaxonomyNodes.get(i), escoTaxonomyNodes.get(j));
			}

		}

		return input;
	}
}
