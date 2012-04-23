package org.blastall.insert.util;

import org.apache.log4j.Logger;
import org.blastall.insert.objects.Protein;
import org.blastall.insert.sql.remove.RemoveFailedProteins;
import org.blastall.insert.sql.remove.RemoveFailedRequests;

public class Cleanup {

	private static final Logger log = Logger.getLogger(Cleanup.class);

	public void clean(String proteins) throws Throwable{

		/* --- Some things we need --- */
		
		RemoveFailedRequests rfr = null;
		RemoveFailedProteins rfp = null;
		StringParser sp = null;
		
		/* *************************** */
		
		try {

			/* ---- Instantiate them ----- */
			
			rfr = new RemoveFailedRequests();
			rfp = new RemoveFailedProteins();
			sp = new StringParser();
			
			/* *************************** */
			
			
			/* ------------ Try to clean up the database ------------------------ */
			
			if(log.isDebugEnabled()){ log.debug("Trying to cleanup database."); }
						
			for (Protein p: sp.stringToList(proteins)){
				rfr.remove(p.getProteinId());
				rfp.remove(p.getProteinId());
			}
			
			if(log.isDebugEnabled()){ log.debug("Done cleaning up database."); }
			
			/* ****************************************************************** */


		}catch (Throwable t){

			/* --------------- Log the error and email the admin ------------------------- */

			log.error("WARNING!!! Could not cleanup database. Please do so manually.", t);
			throw t;

			/* *************************************************************************** */


		}	

	}

}
