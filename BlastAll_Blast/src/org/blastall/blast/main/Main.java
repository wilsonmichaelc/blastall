package org.blastall.blast.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.blastall.blast.objects.Protein;
import org.blastall.blast.utils.GetProteins;
import org.blastall.blast.utils.ReadProperties;
import org.blastall.blast.utils.SendProteins;
import org.blastall.blast.utils.StringParser;

public class Main {

	private static Logger log = Logger.getLogger(Main.class);
	public static final String PASS = "pass";
	public static final String FAIL = "fail";
	public static String passfail = PASS;

	public String run() throws Throwable {

		/* ----------Some things that we will need. -------------*/

		GetProteins getProteins = null;
		ReadProperties read = null;
		StringParser parser = null;
		Blast blast = null;
		SendProteins sendProteins = null;
		List<Protein> list = null;
		String response = "";
		File xmlFilename = null;

		/* ***************************************************** */
		
		try {
			
			/* --------------Instantiate some stuff-----------------*/
			
			getProteins = new GetProteins();
			read = new ReadProperties();
			sendProteins = new SendProteins();
			parser = new StringParser();
			list = new ArrayList<Protein>();
			blast = new Blast();
			
			/* ***************************************************** */

			
			/* -----Capture the response from getProteins request-----*/
			
			try{
				
				if(log.isDebugEnabled()){ log.debug("Reading protein list."); }
				response = getProteins.get(read.properties().getPost_proteins());
				//log the response text
				//if(log.isDebugEnabled()){ log.debug(response); }
				
			} catch(Throwable t){

				log.error("Failed to read response.");
				passfail = FAIL;

			}
			
			/* ***************************************************** */
			

			/* ---------Make sure the we stop when requested.--------*/
			
			if(response.equals("stop")){ return "stop"; }
			
			/* ***************************************************** */

			/* ----------------------Continue if stop command is not received----------------------*/

			if( (!response.equals("stop")) && (!response.equals("")) ){
				
				/* --------Try to covert the . delimited string to a list of proteins------------*/

				try{
					list = parser.stringToList(response);
					if(log.isDebugEnabled()){ log.debug("Read in " + list.size() + " proteins."); }
				} catch(Throwable t){
					log.error("Problem parsing protein string.", t);
					passfail = FAIL;
				}
				
				/* ***************************************************************************** */

				/* --------Run BLAST, and get the filename that it has written out---------------*/

				try{
					if(log.isDebugEnabled()){ log.debug("Running BLAST"); }
					xmlFilename = blast.process( list, read.properties());
					if(log.isDebugEnabled()){ log.debug("Received XML from BLAST."); }
				} catch(Throwable t){
					log.error("Problem getting XML from BLAST.", t);
					passfail = FAIL;
				}
				
				/* ***************************************************************************** */

				
				/* ---------------Send the xml string back to the web service -------------------*/

				try{
					if(log.isDebugEnabled()){ log.debug("Sending XML to Web Service for DB Insertion."); }
					
					String proteins = "";
					for(Protein p: list){
						proteins += p.getProteinId() + "." + "-" + ".";
					}

					sendProteins.send(read.properties().getPost_xml(), xmlFilename, passfail, proteins);
					log.debug("Sending xml, " + passfail + ", proteins");
				} catch(Throwable t){
					log.error("Problem sending XML for DB Insertion.", t);
				}
				
				/* ***************************************************************************** */
				
			}
			
			/* ************************************************************************************** */


		} catch (Throwable t) {

			log.error("General Error in " + Main.class, t);

		} 
		
		return "run";

	}
}
