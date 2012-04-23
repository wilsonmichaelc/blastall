package org.blastall.insert.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.blastall.insert.sql.update.UpdateBlastRequestFinishTime;
import org.blastall.xmlparser.objects.Iteration;

public class XMLProcessor {

	private static final Logger log = Logger.getLogger(XMLProcessor.class);

	public void process(List<Iteration> iterations, String proteins) throws Throwable{

		// Some things we will need
		InsertIteration iteration = null;
		UpdateBlastRequestFinishTime updateFinishTime = null;
		
		try{
			// Instantite
			iteration = new InsertIteration();
			updateFinishTime = new UpdateBlastRequestFinishTime();
			
			if(log.isDebugEnabled()){ log.debug("Inserting hits..."); }
			
			// Insert each iteration and update the finishDate
			for ( Iteration iter: iterations ) { 

				iteration.insert(iter);
				updateFinishTime.update(iter.getIteration_query_def());

			}

			if(log.isDebugEnabled()){ log.debug("Done inserting hits."); }
			
		} catch(Throwable t){
			
			// if anything goes wrong, then clean up.
			Cleanup cleanup = null;
			try{
				cleanup = new Cleanup();
				cleanup.clean(proteins);
			} catch(Throwable u){
				log.error("Failed to cleanup failed insertions.");
				throw u;
			}
			log.error("Failed to process xml.");
			throw t;
			
		}
	}

}
