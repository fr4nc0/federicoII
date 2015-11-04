package it.federicoII.indice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.lang.model.element.ExecutableElement;

public class ParserRDF {

	public static ArrayList<RDFtriplet> getRDFtripletFromFile(String filename) {

		ArrayList<RDFtriplet> list = new ArrayList<RDFtriplet>();

		File name = new File(filename);
		
		if (name.isFile()) {
			try {
				
				BufferedReader input = new BufferedReader(new FileReader(name));
				//StringBuffer buffer = new StringBuffer();
				String text;
				
				while ((text = input.readLine()) != null) {
				
					if ( ! text.toLowerCase().contains("pre-condition") ) {
						String delim = ",";
						StringTokenizer tok = new StringTokenizer(text, delim, true);
				 		
						String requisiste = "";
						//String requisiste = tok.nextToken().trim();
						//tok.nextToken();
						
						String id		  = tok.nextToken().trim();
						tok.nextToken();
						
						//System.out.println(id);
						
						String subject = tok.nextToken().trim();
						tok.nextToken();
						
						String predicate = tok.nextToken().trim();
						tok.nextToken();
						
						String object = tok.nextToken().trim();
						
						if ( ! object.toLowerCase().contains("t0") ) {
						
							RDFtriplet rdf = new RDFtriplet(requisiste, id, subject, predicate, object);
							
							list.add(rdf);
						}
					}    					
				}
					
				input.close();

			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}


		return list;
	}
}
