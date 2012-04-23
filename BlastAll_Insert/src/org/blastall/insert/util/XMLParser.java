package org.blastall.insert.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.blastall.xmlparser.main.XMLStreamComboParser;
import org.blastall.xmlparser.objects.Iteration;


public class XMLParser {

	private static final Logger log = Logger.getLogger(XMLParser.class);

	public List<Iteration> parse(InputStream xmlStream, String proteins) throws Throwable{

		// Some things we will need
		XMLStreamComboParser xmlStreamParser = null; 
		List<Iteration> iterations = null;

		try{

			// Instantiate them
			xmlStreamParser = new XMLStreamComboParser();
			iterations = new ArrayList<Iteration>();

			// get the list of iterations by parsing the xml doc
			if(log.isDebugEnabled()){ log.debug("Parsing XML..."); }
			iterations = xmlStreamParser.parse(xmlStream);
			if(log.isDebugEnabled()){ log.debug("Parsed the XML."); }

		} catch (Throwable t){

			// if something goes wrong, make sure to use the list of proteins to clean up the database.
			Cleanup cleanup = null;
			try{
				cleanup = new Cleanup();
				cleanup.clean(proteins);
			} catch(Throwable u){
				log.error("Failed to cleanup failed insertions.");
				throw u;
			}
			log.error("Failed to parse xml.");
			throw t;

		}

		return iterations;
	}

}
