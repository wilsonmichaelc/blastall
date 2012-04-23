package org.blastall.get.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.blastall.get.objects.Protein;

/** 
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 15, 2012
*/
public class StringParser {

	private static Logger log = Logger.getLogger(StringParser.class);

	// Converts . delimeted string to List<Protein>
	public List<Protein> stringToList(String proteinString) throws Throwable{

		/* ----------Some things that we will need. -------------*/

		List<Protein> proteins = null;
		Protein p = null;
		StringTokenizer token = null;
		
		/* ****************************************************** */

		try {
			/* ---------- Instantiate -------------*/

			proteins = new ArrayList<Protein>();
			token = new StringTokenizer(proteinString, ".");
			
			/* *********************************** */
			
			/* ---------- While we have more tokens, add them -------------*/

			while (token.hasMoreTokens()){
				p = new Protein();
				p.setProteinId(Integer.parseInt(token.nextToken()));
				p.setProteinSequence(token.nextToken());
				proteins.add(p);
			}
			
			/* *********************************************************** */
			
		}catch (Throwable t){

			/* --------- If anything goes wrong, log it -------------*/
			
			log.error("Failed to build list.", t);
			
			/* ***************************************************** */
			
		}

		if(log.isDebugEnabled()){ log.debug("List of proteins has been built."); }
		
		/* ---------- Return the list -------------*/

		return proteins;
		
		/* *************************************** */
	}

	// Converts List<Protein> to . delimited string.
	public String listToString(List<Protein> proteins) throws Throwable{

		/* ---------- Make a string to hold the list -------------*/
		
		String proteinString = "";
		
		/* ****************************************************** */
		
		try{

			/* ---------- For each protein, add the id and sequence to the string -------------*/
			
			for(Protein p: proteins){
				
				proteinString += p.getProteinId() + "." + p.getProteinSequence() + ".";

			}
			
			/* ******************************************************************************* */
			
		}catch(Throwable t){

			/* --------- If anything goes wrong, log it -------------*/
			
			log.error("Failed to build string.", t);
			
			/* ***************************************************** */
		}
		if(log.isDebugEnabled()){ log.debug("String has been built."); }
		
		/* ---------- Finally return the string -------------*/
		
		return proteinString;
		
		/* ************************************************* */
	}

}
