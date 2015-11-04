package fg.federicoII.kdtree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import edu.wlu.cs.levy.CG.KDTree;
import fg.federicoII.esco.EscoNode;

public class testMapping {

	public static void main(String[] args) {

		//KDTree<String> kd = new KDTree<String>(3);

		int dimensioni = 3;
		int maxNumNodes = 10000;
		KdTree<String> kd = new KdTree.SqrEuclid<String>(dimensioni, maxNumNodes);
		//KdTree<String> kd = new KdTree.WeightedSqrEuclid<String>(dimensioni, maxNumNodes);
		//KdTree<String> kd = new KdTree.Manhattan<String>(dimensioni, maxNumNodes);


		double[] query = new double[3];

		BufferedReader reader;
		try {
			//String filename = "C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/MappedPoints 3 dim.txt";
			String filename = "C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/MDS-MappedPoints 3 dim.txt";
			//String filename = "C:/Users/gafr423/Desktop/ESCO/occupazioni-xml/Fastmap_point3d.txt";
			reader = new BufferedReader(new FileReader(filename));

			String line = reader.readLine();

			while(line!=null) {
				//System.out.println(line);

				StringTokenizer st2 = new StringTokenizer(line, ";");
				String value = (String) st2.nextElement();

				double[] key = new double[3];
				String x1 = (String) st2.nextElement();
				String x2 = (String) st2.nextElement();
				String x3 = (String) st2.nextElement();

				key[0] = Double.parseDouble(x1);
				key[1] = Double.parseDouble(x2);
				key[2] = Double.parseDouble(x3);

				if (value.equalsIgnoreCase("Forze armate") ) {
				//if (value.equalsIgnoreCase("http://ec.europa.eu/esco/occupation/560_Forze armate") ) {	
					System.out.println("=============================================");
					System.out.println("query: forze armate");

					query[0] = key[0];
					System.out.println(key[0]);
					query[1] = key[1];
					System.out.println(key[1]);
					query[2] = key[2];
					System.out.println(key[2]);
				}

				kd.addPoint(key, value);	 

				line = reader.readLine();
			}

			List<KdTree.Entry<String>> res = kd.nearestNeighbor(query, 30, true);


			Iterator<fg.federicoII.kdtree.KdTree.Entry<String>> it = res.iterator();
			while (it.hasNext()){
				fg.federicoII.kdtree.KdTree.Entry<String> val = it.next();

				System.out.println("res: " + val.value);
			}


		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
