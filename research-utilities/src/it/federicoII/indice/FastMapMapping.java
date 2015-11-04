package it.federicoII.indice;

import java.util.ArrayList;

import net.vja2.research.util.FastMap;
import net.vja2.research.util.FastMap.DataMapping;
import net.vja2.research.util.FastMap.MapPoint;

public class FastMapMapping {

	public static void main(String[] args) {
		
		/*
		String dir = "C:\\Program Files (x86)\\WordNet";
		JWS	ws = new JWS(dir, "2.1");
		WuAndPalmer wep = ws.getWuAndPalmer();
		double d = wep.wup("software", 1, "pen", 1, "n");
		double de = wep.wupExtented("software", 1, "pen", 1, "n", 1.0, 0.0);
		System.out.println("d: " + d + "de: " + de);
		*/

		try {


			/*
			 * numero delle dimensioni dello spazio vettoriale
			 */
			Integer NumeroDimensioni = 5;
			
			/*
			 * le triple da mappare: le legge dal file
			 */
			//ArrayList<RDFtriplet> RDFtriplets = ParserRDF.getRDFtripletFromFile("C:\\Users\\gafr423\\Desktop\\Gabriella\\Triple HLR_0d.txt");
			ArrayList<RDFtriplet> RDFtriplets = 
					ParserRDF.getRDFtripletFromFile("C:\\Users\\gafr423\\Desktop\\Gabriella\\triple\\13.11.2014\\Requirement set triplet - ver.2.0 .txt");

			/*
			 * la nostra distanza
			 */
			DistanceImpl distanza = new DistanceImpl();

			/* 
			 * mapping
			 */
			FastMap<RDFtriplet> mapper 	= new FastMap<RDFtriplet>(RDFtriplets, distanza);
			DataMapping<RDFtriplet> map = mapper.map(NumeroDimensioni);


			for(MapPoint<RDFtriplet> mp : map.mappeddata) {

				/*
				 * cicla sui punti mappati
				 */
			 	System.out.println(mp.data().getId() + 
			 			" <" + mp.data().getSubject() + ", " + mp.data().getPredicate() + ", " + mp.data().getObject() + "> " + 
			 			" x: " + mp.map[0] + " y: " + mp.map[1] + " z: " + mp.map[2] + " t: " + mp.map[3] + " k: " + mp.map[4]); 
			}


		} catch(Exception e) {

			e.printStackTrace();

		}

	}

	/*
	private static ArrayList<RDFtriplet> getRDFtripletFromFile(String filename) {

		ArrayList<RDFtriplet> list = new ArrayList<RDFtriplet>();

		File name = new File(filename);
		
		if (name.isFile()) {
			try {
				
				BufferedReader input = new BufferedReader(new FileReader(name));
				//StringBuffer buffer = new StringBuffer();
				String text;
				
				while ((text = input.readLine()) != null) {
				
					String delim = ",";
					StringTokenizer tok = new StringTokenizer(text, delim, true);
			 		
					String requisiste = tok.nextToken().trim();
					tok.nextToken();
					
					String id		  = tok.nextToken().trim();
					tok.nextToken();
					
					String subject = tok.nextToken().trim();
					tok.nextToken();
					
					String predicate = tok.nextToken().trim();
					tok.nextToken();
					
					String object = tok.nextToken().trim();
					
					RDFtriplet rdf = new RDFtriplet(requisiste, id, subject, predicate, object);
					
					list.add(rdf);
					    					
				}
					
				input.close();

			} catch (IOException ioException) {
			}
		}


		return list;
	}
	*/

}
