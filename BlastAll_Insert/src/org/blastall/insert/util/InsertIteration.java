package org.blastall.insert.util;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.insert.InsertBlastHit;
import org.blastall.insert.sql.insert.InsertBlastStat;
import org.blastall.xmlparser.objects.Hit;
import org.blastall.xmlparser.objects.Iteration;

/**
 * This class handles the insertion of iterations. 
 * In the event that multiple sequence are submitted via the PDR BLAST Search, 
 * this class will handlde inserting the results of each query for that submission.
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class InsertIteration {

	private static Logger log = Logger.getLogger(InsertIteration.class);

	/**
	 * This method takes an iteration ( which is the result of a blast of a single sequence )
	 *  and inserts its Hits into the database.
	 *  
	 * @param iteration
	 * @param blastRequestId
	 * @throws Throwable
	 */
	public void insert(Iteration iteration) throws Throwable {

		// Some things we need
		InsertBlastHit insertHit = null;
		InsertBlastStat insertStat = null;

		// Insert the hits.
		try {
			
			// Instantiate
			insertHit = new InsertBlastHit();
			insertStat = new InsertBlastStat();
			
			// If there are Hits, iterate over them and insert the data into the pdr database
			if (iteration.getIteration_hits() != null) {
				for (Hit hit: iteration.getIteration_hits()) {
					
					insertHit.insert(iteration.getIteration_query_def(), hit);
					insertStat.insert(iteration.getIteration_query_def(), iteration.getIteration_stat().getStatistics());

				}

				//if(log.isDebugEnabled()){ log.debug("Num Hits for iteration#" + iteration.getIterationDefinition() + ": " + iteration.getHits().size()); }
			} else {

				// If there were no hits, update the status field accordingly.
				if(log.isDebugEnabled()){ log.debug("There were no hits to insert for request#: " + iteration.getIteration_query_def()); }

			}
		} catch (Throwable t) {

			// If something goes wrong, log the event and re-throw the exception.
			log.error("There was an error while inserting hits.", t);
			throw t;

		} 

	}
}