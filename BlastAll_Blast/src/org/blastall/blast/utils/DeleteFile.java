package org.blastall.blast.utils;

import java.io.File;

import org.apache.log4j.Logger;

public class DeleteFile {

	private static Logger log = Logger.getLogger(DeleteFile.class);
	
	// Method so that I dont have to write this several times.
	public void delete( String fileName ) {

		// We will be needing a file.
		File file = null;
		
		try {
			
			// Try to instantiate the file.
			file = new File(fileName);
			
			// Attempt to delete the file.
			boolean isDeleted = file.delete();

			// If the file was not deleted, then log the error
			if (!isDeleted) {

				log.error("Could not remove unused files.");

			// If it was deleted, then log that it was successful (if the debugging is enabled)
			}else {

				if(log.isDebugEnabled()){ log.debug("Unused file: " + fileName + " was removed."); }

			}
		} catch (Throwable t) {
			// Only need to log the failure. it is not grounds for failing the the job.
			log.warn("Could not delete the file: " + fileName, t);
		} 
	}
	
}
