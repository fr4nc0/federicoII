package it.federicoII.indice;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.WuAndPalmer;
import net.vja2.research.distancemetrics.IDistanceMetric;
import it.federicoII.indice.*;

public class DistanceImpl implements IDistanceMetric<RDFtriplet> {

	@Override
	public double distance(RDFtriplet a, RDFtriplet b) {

		double alfa  = 0.6;
		double beta  = 0.2;
		double gamma = 0.2;

		double d = (alfa  * innerDistance(a.getSubject(), 	b.getSubject(), 	"n") +
					beta  * innerDistance(a.getPredicate(), b.getPredicate(), 	"v") + 
					gamma * innerDistance(a.getObject(), 	b.getObject(), 		"n") );
		
		//System.out.println("distance " + a.toString() + " " + b.toString() + " = " + d);
		return d;
	}

	private double innerDistance(String concept1, String concept2, String pos) {

		WuAndPalmer wep = WuPalmerSingleton.getInstance();

		NearestNeighborInWordnet nnword1 = WordNetExtensions.getNearestInWordNet(concept1, pos, wep.getDict());
		NearestNeighborInWordnet nnword2 = WordNetExtensions.getNearestInWordNet(concept2, pos, wep.getDict());
		
		
		double d = wep.wupExtented(nnword1.getWord(), 1, nnword2.getWord(), 1, pos, 
				nnword1.getExtension(), nnword2.getExtension()); 

		return (1 - d);	//perchè WEP è una misura di similarità non una distanza!

	}
}
