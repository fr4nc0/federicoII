package index;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.WuAndPalmer;
import java.util.ArrayList;
import java.util.Iterator;
import net.vja2.research.distancemetrics.IDistanceMetric;
;

public class DistanceImpl implements IDistanceMetric<RDFtriplet> {
ArrayList<ArrayList<Double>> distance;
ArrayList<ArrayList<String>> tmp;
        
    public DistanceImpl(ArrayList<ArrayList<Double>> distance, ArrayList<ArrayList<String>> tmp) {
      this.distance=distance;
      this.tmp=tmp;
    }
    
	@Override
	public double distance(RDFtriplet a, RDFtriplet b) {
		
		double gamma = 1;

		double d = (gamma * innerDistance(a.getObject(), b.getObject(),"n") );
		
		//System.out.println("distance " + a.toString() + " " + b.toString() + " = " + d);
		return d;
	}

	private double innerDistance(String concept1, String concept2, String pos) {
		return this.check_distance(concept1,concept2);	
                }

    private double check_distance(String concept1, String concept2) {
        return this.distance.get(this.check_index(concept1)).get(this.check_index(concept2));
 }

    private int check_index(String concept) {
       int index=-1;
       for(int i=0;i<tmp.size();i++){
           if(tmp.get(i).get(0).equals(concept)){
               index=i;
           }
       }
       return index;
    }
}
