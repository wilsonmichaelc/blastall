package org.blastall.xmlparser.main;

import java.util.List;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.blastall.xmlparser.objects.Iteration;

public class XMLStreamComboParser {

	private static Logger log = Logger.getLogger(XMLStreamComboParser.class);

	public List<Iteration> parse(InputStream xmlStream) throws Throwable{

		List<Iteration> iterations = new ArrayList<Iteration>();
		Iteration iteration = null;
		XMLInputFactory xif = null;
		XMLStreamReader xsr = null;
		boolean isStart_BlastOutput_iterations;

		try {

			xif = XMLInputFactory.newInstance();
			xsr = xif.createXMLStreamReader(xmlStream, "ISO-8859-1");
			isStart_BlastOutput_iterations = false;

			log.debug("Processing XML...");

			while( !isStart_BlastOutput_iterations ){		
				xsr.next();		
				if(xsr.isStartElement() && xsr.getLocalName().equals("BlastOutput_iterations")){			
					isStart_BlastOutput_iterations = true;		
				}	
			}

			JAXBContext jc = JAXBContext.newInstance(Iteration.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			while(xsr.nextTag() == XMLStreamConstants.START_ELEMENT) {
				iteration = (Iteration) unmarshaller.unmarshal(xsr);
				iterations.add(iteration);
			}
			
			log.debug("Done parsing xml...");
			
		} catch(Throwable t){

			log.error("Failed to parser xml.", t);
			throw t;
		}
		return iterations;
	}

}
