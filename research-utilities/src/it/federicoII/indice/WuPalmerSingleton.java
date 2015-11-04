package it.federicoII.indice;

import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.WuAndPalmer;

public class WuPalmerSingleton {
	
	
	private static JWS 			localWs;
	private static WuAndPalmer	localWEP;
	
	public static WuAndPalmer getInstance() {
		
		if (localWEP == null) {
			String dir = "C:\\Program Files (x86)\\WordNet";
			localWs = new JWS(dir, "2.1");
			localWEP = localWs.getWuAndPalmer();
		}
		
		return localWEP;
		
	}
	
	
	
}
