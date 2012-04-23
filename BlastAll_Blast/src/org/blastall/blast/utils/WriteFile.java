package org.blastall.blast.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.log4j.Logger;

public class WriteFile {

	private static Logger log = Logger.getLogger(WriteFile.class);
	
	public void write(String sequences, String directory) throws Exception{

		FileWriter fw = null;
		BufferedWriter br = null;
		
		try {

			// Write the query string to a fasta file
			File f = new File(directory);
			boolean exists = f.exists();
			if (exists) {

				fw = new FileWriter(directory + "/sequenceBlock.fasta");
				br = new BufferedWriter(fw);
				br.write(sequences);
				if(log.isDebugEnabled()){ log.debug("Writing query to " + directory + "/sequenceBlock.fasta"); }

			} else {
				// Do not continue because the file could not be written.
				log.error("Directory does not exist: " + directory) ;
			}

		} catch (Throwable t) {

			// Do not continue because the file could not be written.
			String msg = "Could not write query string to file. Check: " + directory + " exists and has write access.";
			log.error(msg, t);
			throw new Exception(msg, t);

		} finally {
			try {
				// Make sure to close the file.
				if (br != null) {
					br.close();
				}	
				if (fw != null){
					fw.close();
				}
			}
			catch (Throwable t) {
				// Continue, but warn that closing the file failed.
				log.error("A writer was not closed.");
			}
		}
	}
}
