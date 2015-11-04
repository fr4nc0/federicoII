package fg.federicoII.esco;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.text.AbstractDocument.Content;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import net.vja2.research.util.FastMap;
import net.vja2.research.util.FastMap.DataMapping;
import net.vja2.research.util.FastMap.MapPoint;

public class ESCOMapping {


	public static void main(String[] args) {

		String filename;

		try {

			/*
			 * tassonomia e nodi da mappare
			 */

			DefaultTreeModel taxonomy = 
					EscoParser.getTaxonomy("C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/4eb7cab7-57db-42a0-adcf-8d30a104dca6.xml");

			ArrayList<EscoNode> EscoTaxonomyNodes = EscoParser.getNodeList();

			/*
			 * la distanza
			 */
			ESCODistanceImpl escoDistance = new ESCODistanceImpl(taxonomy);

			/* 
			 * mapping
			 */
			FastMap<EscoNode> mapper 	= new FastMap<EscoNode>(EscoTaxonomyNodes, escoDistance);


			/*
			 * numero delle dimensioni dello spazio vettoriale
			 */

			Integer maxNumDimensioni = 4;

			for ( Integer numeroDimensioni = 3; numeroDimensioni <= maxNumDimensioni; numeroDimensioni++ ) {
				DataMapping<EscoNode> map = mapper.map(numeroDimensioni);

				HashMap<String, Integer> riscontro = new HashMap<String, Integer>();
				File outputFile = new File("C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/MappedPoints " + numeroDimensioni + " dim.txt");
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

				for(MapPoint<EscoNode> mp : map.mappeddata) {

					/*
					 * cicla sui punti mappati
					 */
					String line = ((EscoNode) mp.data()).getPreferredTerm(); 
					String coords = "";
					for ( int i = 0; i < numeroDimensioni; i++ ) {
						coords = coords + " x[" + i + "]: " + mp.map[i];
						line = line + ";" + mp.map[i];
					}

					//System.out.println( ((EscoNode) mp.data()).getPreferredTerm() + coords );
					//writer.write ( ((EscoNode) mp.data()).getPreferredTerm() + ":" + coords );
					
					writer.write ( line );
					//writer.newLine();
					writer.newLine();

					Integer conteggio = riscontro.get(coords);
					if ( conteggio == null ) {
						conteggio = 1;
					} else {
						conteggio++;
					}

					riscontro.put(coords, conteggio);
				}

				writer.close();

				File ducplicatedFile = new File("C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/Duplicated " + numeroDimensioni + " dim.txt");
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
			}






			System.out.println("ESCOMapping.main() - end");

		} catch(Exception e) {

			e.printStackTrace();

		}

	}
}
