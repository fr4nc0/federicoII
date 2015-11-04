package it.federicoII.indice;

import java.util.ArrayList;

import edu.mit.jwi.IDictionary;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordSense;
import edu.sussex.nlp.jws.WuAndPalmer;

public class InconsistenceChecker {

	static boolean systemOutPrint = false;
	
	
	private static void SystemOut(String s) {
		if (systemOutPrint) {
			System.out.println(s);
		}
	}
	
	public static void main(String[] args) {
		
		try {
	
			/*
			 * le triple da mappare: le legge dal file
			 */
			//ArrayList<RDFtriplet> RDFtriplets = ParserRDF.getRDFtripletFromFile("C:\\Users\\gafr423\\Desktop\\Gabriella\\Triple HLR_0d.txt");

			ArrayList<RDFtriplet> RDFtriplets = 
					ParserRDF.getRDFtripletFromFile("C:\\Users\\gafr423\\Desktop\\Gabriella\\triple\\04.11.2014\\Requirement set triplet - ver.1.0 .txt");
			/*
			 * la nostra distanza
			 */
			DistanceImpl distanza = new DistanceImpl();

			//distanza.distance(a, b);
			
			/*
			 * checks the Rule n.1 
			 */
			WuAndPalmer wep = WuPalmerSingleton.getInstance();
			
			
			ArrayList<ArrayList<RDFtriplet>> globalInconsistenceList = CheckWeakContraddictions(RDFtriplets, wep );
			
			for (ArrayList<RDFtriplet> list : globalInconsistenceList)  {
				System.out.println("\nWeak Contraddictions between:");
				for (RDFtriplet t: list) {
					System.out.println(t.getId());
				}

			}
			
			System.out.println("finished! ");
		} catch(Exception e) {

			e.printStackTrace();

		}

	}

	private static ArrayList<ArrayList<RDFtriplet>> CheckWeakContraddictions(
			ArrayList<RDFtriplet> RDFtriplets, WuAndPalmer wep) {
		
		/*
		 * Per ogni tripla ne cerca una che abbia:
		 * 	- stesso soggetto e oggetto
		 *  - predicato in conflitto
		 */
		
		ArrayList<ArrayList<RDFtriplet>> globalInconsistenceList = new ArrayList<ArrayList<RDFtriplet>>();
		ArrayList<RDFtriplet> inconsistenceList;
		boolean foundSomething = false;
		
		
		for (RDFtriplet rdf1 : RDFtriplets) {
			SystemOut("T1: " + rdf1.getId());
			
			inconsistenceList = new ArrayList<RDFtriplet>();
			inconsistenceList.add(rdf1);
			foundSomething = false;
			
			for (RDFtriplet rdf2 : RDFtriplets) {
				
				if ( rdf1.getSubject().equals(rdf2.getSubject()) &&
					 rdf1.getObject().equals(rdf2.getObject())	) {
					
					if ( ! rdf1.getPredicate().equals(rdf2.getPredicate())) {
						
						SystemOut("T2: " + rdf2.getId());
						if ( oppositePredicate(rdf1.getPredicate(), rdf2.getPredicate(), wep ) ) {
							
							foundSomething = true;
							inconsistenceList.add(rdf2);
						}	
					}
				}
			}	
			
			if (foundSomething) {
				globalInconsistenceList.add(inconsistenceList);
			}
		}
		
		return globalInconsistenceList;
		
	}

	private static boolean oppositePredicate(String predicate1,
			String predicate2, WuAndPalmer wep) {
		
		boolean esito = false;
		
		SystemOut("Predicate n.1: " + predicate1.toLowerCase());
		SystemOut("Predicate n.2: " + predicate2.toLowerCase());
		
		//NounSynset nounSynset;  
		VerbSynset verbSynset;
		WordSense[] antonyms; 

		System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		
		WordNetDatabase database = WordNetDatabase.getFileInstance(); 
		
		NearestNeighborInWordnet nnword = WordNetExtensions.getNearestInWordNet(predicate1, "v", wep.getDict());
		edu.smu.tspell.wordnet.Synset[] synsets =  database.getSynsets(nnword.getWord(), SynsetType.VERB); 
		
		ArrayList<String> contrariList = new ArrayList<String>();
		
		/*
		 * trova tutti i contrari di predicate1 
		 */
		for (int i = 0; i < synsets.length; i++) { 
			
			verbSynset = (VerbSynset)(synsets[i]); 
		    
		    String words[]  = verbSynset.getWordForms(); 

		    for (String wrd : words) {
		    	antonyms = verbSynset.getAntonyms(wrd);
		    	
		    	for (int j = 0; j < antonyms.length; j++) {
		    		contrariList.add(antonyms[j].getWordForm() );
		    	}
		    }
		}
		
		
		double DistMediaPredicato1Contrari 			= 0.0;
		double DistMediaPredicato2ContrariP1 		= 0.0;
		double DistPredicato1Predicato2 			= 0.0;
		
		double somma 	= 0.0;
		
		
		
		/*
		 * confrontare con predicate2 
		 */
		NearestNeighborInWordnet nnword2 = WordNetExtensions.getNearestInWordNet(predicate2, "v", wep.getDict());
		
		DistPredicato1Predicato2 = wep.wupExtented(nnword.getWord(), 1, nnword2.getWord(), 1, "v", nnword2.getExtension(), nnword2.getExtension());
		DistPredicato1Predicato2 = (1- DistPredicato1Predicato2);
		
		SystemOut("Wu Palmer(" + predicate1.toLowerCase() + ", " + predicate2.toLowerCase() + ") = " + DistPredicato1Predicato2);
		
		
		
		/*
		 * calcola la distanza media tra predicate1 e tutti i suoi contrari 
		 */
		for (String contrario: contrariList) {
			
			SystemOut("  antonym of predicate n.1: " + contrario);
			
			double d = wep.wupExtented(nnword.getWord(), 1, contrario, 1, "v", nnword.getExtension(), 0.0);
			d = (1 - d);
			
			SystemOut("Wu Palmer(" + predicate1.toLowerCase() + ", " + contrario + ") = " + d);
			somma = somma + d; 
		}
		
		
		/*
		 * calcola la distanza media tra Predicate2 e tutti i contrari do Predicate1
		 */
		if (contrariList.size() > 0 ) {
			DistMediaPredicato1Contrari = somma/contrariList.size();	
			
			somma = 0.0;
			for (String contrario: contrariList) {
				
				
				double d = wep.wupExtented(nnword2.getWord(), 1, contrario, 1, "v", nnword2.getExtension(), 0.0);
				d = (1 - d);
				
				SystemOut("Wu Palmer(" + predicate2.toLowerCase() + ", " + contrario + ") = " + d);
				somma = somma + d;
			}
			
			DistMediaPredicato2ContrariP1 = somma / contrariList.size();
			
			SystemOut("Average dist. between predicate n.1 and its antonyms: " + DistMediaPredicato1Contrari );
			SystemOut("Average dist. between predicato n.2 and antonyms of predicate n.1: " + DistMediaPredicato2ContrariP1 );
			
			if ( DistMediaPredicato2ContrariP1 <= DistPredicato1Predicato2 ) {
				
				esito = true;
			}	
		}
		
		SystemOut("Potential conflict:" + esito + "\n\n" );
		return esito;
	}



}
