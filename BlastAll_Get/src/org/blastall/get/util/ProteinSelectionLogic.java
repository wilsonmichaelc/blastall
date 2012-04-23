package org.blastall.get.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.blastall.get.objects.Protein;
import org.blastall.get.sql.InsertBlastRequest;
import org.blastall.get.sql.SelectFromHits;
import org.blastall.get.sql.SelectRandom;

public class ProteinSelectionLogic {

	private static final Logger log = Logger.getLogger(ProteinSelectionLogic.class);

	public static synchronized String getProteinBlock(){

		// Some things we will need.
		StringParser parser = null;
		SelectRandom selectRandom = null;
		SelectFromHits selectFromHits = null;
		InsertBlastRequest insertBlastRequest = null;
		List<Protein> proteins = null;
		
		try{
			
			// Instantiate
			insertBlastRequest = new InsertBlastRequest();
			selectFromHits = new SelectFromHits();
			selectRandom = new SelectRandom();
			parser = new StringParser();

			// Get the list of proteins
			proteins = selectFromHits.getResults();

			if(log.isDebugEnabled()){ log.debug("Checking for proteins to blast from Hits db"); }

			// if the list is not empty, then process it
			if(!proteins.isEmpty()){

				for (Protein p: proteins){
					insertBlastRequest.insert(p);
				}
				// return the proteins as delimited a string
				return parser.listToString(proteins);

			} 
			else {

				// Get random proteins
				if(log.isDebugEnabled()){ log.debug("Checking for proteins to blast from proteinsToBlast db"); }
				proteins = selectRandom.getResults();

				// if the list is not empty, then process it
				if (!proteins.isEmpty()) {

					for (Protein p: proteins){
						insertBlastRequest.insert(p);
					}
					// return the proteins as delimited a string
					return parser.listToString(proteins);

				}
				// Make sure to log when we run out of proteins to blast
				else {

					log.error("No proteins were elligible for blast");

				}
			}
		} catch (Throwable t){

			log.error("Failed to select block of proteins.", t);

		}

		// if we really dont have proteins to blast, then return stop, so that the client will turn off.
		return "stop";
	}

}
