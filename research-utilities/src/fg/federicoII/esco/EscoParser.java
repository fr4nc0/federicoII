package fg.federicoII.esco;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class EscoParser {

	static DefaultTreeModel 					taxonomy;
	static HashMap<String, EscoNode> 			nodeList; 			
	static Map<String, ArrayList<String> >	relationsList;

	public static DefaultTreeModel getTaxonomy(String filename) {

		nodeList 		= new HashMap<String, EscoNode>();
		relationsList 	= new LinkedHashMap< String, ArrayList<String> >();;

		parseXMLEscoTaxonomy(filename);
		DefaultTreeModel taxonomy = buildTaxonomy();

		return taxonomy;
	}

	private static DefaultTreeModel buildTaxonomy() {


		EscoNode root = new EscoNode("root");
		taxonomy = new DefaultTreeModel(root);

		ArrayList<EscoNode> firstLevelNodes = getChildrenOf(root);

		buildTree(root, firstLevelNodes);


		return taxonomy;
	}

	private static ArrayList<EscoNode> getChildrenOf(EscoNode node) {

		/*
		 * se è la radice restituisce quelli senza padre
		 */

		//System.out.println("EscoParser.getChildrenOf() : " + node.getUri());

		ArrayList<EscoNode> children = new ArrayList<EscoNode>();

		if ( node.getUri().equals("root")) {

			Iterator it = (Iterator) nodeList.entrySet().iterator();

			while ( it.hasNext() ) {

				Map.Entry pair = (Map.Entry)it.next();

				EscoNode n = (EscoNode) pair.getValue();
				if ( ! n.isHasParent() ) {

					children.add(n);
				}
			}

		} else {

			ArrayList<String> childUriList = relationsList.get(node.getUri());
			if ( childUriList != null ) {
				for ( String childUri : childUriList ) {
					children.add(nodeList.get(childUri));
				}	
			}
		}

		return children;
	}

	private static void buildTree(EscoNode node,
			ArrayList<EscoNode> childrenNodes) {

		for (EscoNode newChild : childrenNodes ) {

			node.add(newChild);
			buildTree( newChild, getChildrenOf(newChild) );
		}

	}

	private static void parseXMLEscoTaxonomy(String filename) {

		try {

			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(filename);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			TreeNode item = null;

			XMLEvent event;

			while (eventReader.hasNext()) {
				event = eventReader.nextEvent();

				if (event.isStartElement()) {

					StartElement startElement = event.asStartElement();


					if ( startElement.getName().getLocalPart().equals("ThesaurusConcept") ) {

						event = eventReader.nextEvent();

						EscoNode node = null;
						while ( eventReader.hasNext()  ) {

							if (event.isStartElement()) {
								startElement = event.asStartElement();



								if (startElement.getName().getLocalPart().equals("uri") ) {

									event = eventReader.nextEvent();
									//System.out.println(event.toString());
									node = new EscoNode(event.toString());
								}

								if (startElement.getName().getLocalPart().equals("PreferredTerm") ) {

									event = eventReader.nextEvent();
									event = eventReader.nextEvent();
									event = eventReader.nextEvent();
									//System.out.println(event.toString());

									node.setPreferredTerm(event.toString());

									nodeList.put(node.getUri(), node);
									break;
								}
							}

							event = eventReader.nextEvent();
						}
					}

					if ( startElement.getName().getLocalPart().equals("HierarchicalRelationship") ) {


						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						//System.out.println(event.toString());
						String newChildUri = event.toString();

						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						//System.out.println(event.toString());
						String parentUri =	event.toString();

						addRelation(parentUri, newChildUri);

						nodeList.get(newChildUri).setHasParent(true);

					}
				}

				/*
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					System.out.println("end: " + endElement.getName().getLocalPart());
				}
				 */
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public static void addRelation(String parentUri, String newChildUri) {

		ArrayList<String> currentChildList = relationsList.get(parentUri);

		if (currentChildList == null) {
			currentChildList  = new ArrayList<String>();
			relationsList.put(parentUri, currentChildList);
		}
		currentChildList.add(newChildUri);
	}


	public static ArrayList<EscoNode> getNodeList() {

		ArrayList<EscoNode> list = new ArrayList<EscoNode>();

		Iterator it = (Iterator) nodeList.entrySet().iterator();

		while ( it.hasNext() ) {

			Map.Entry pair = (Map.Entry)it.next();
			EscoNode n = (EscoNode) pair.getValue();
			list.add(n);

		}
		return list;
	}





}
