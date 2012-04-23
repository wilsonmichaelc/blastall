package org.blastall.blast.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;


import org.blastall.blast.objects.Property;
import org.blastall.blast.objects.Protein;
import org.blastall.blast.utils.DeleteFile;
import org.blastall.blast.utils.WriteFile;

public class Blast {

	private static Logger log = Logger.getLogger(Blast.class);
	
	public File process(List<Protein> proteins, Property property) throws Throwable {

		/* ----------Some things that we will need. -------------*/

		WriteFile writeFile = null;
		Date now = null;
		Process process = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedReader bufferedReader = null;
		InputStream inputStream  = null;
		DeleteFile deleteFile  = null;

		String blastCommand = "";
		String fileName = "";
		String stdErr = "";
		String buff = "";
		String xml = "";

		/* ***************************************************** */



		/* --------Write the sequences to a file ---------------*/

		try{
			writeFile = new WriteFile();
			String sequences = "";
			for(Protein p: proteins){
				sequences += ">" + p.getProteinId() + "\n" + p.getProteinSequence() + "\n\n";
			}
			writeFile.write(sequences, property.getTmp_dir());
		} catch(Throwable t){
			log.error("Failed to write file.", t);
			Main.passfail = Main.FAIL;
		}

		/* ***************************************************** */


		/*----------Get System time for unique filename----------*/

		try{
			now = new Date();
			fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
			fileName = property.getTmp_dir() + "/" + fileName + ".xml";
			// log the filename
			log.info("XML Output File Name for this job: " + fileName);
		} catch(Throwable t){
			log.error("Failed to get current date stamp.");
			Main.passfail = Main.FAIL;
		}

		/* ***************************************************** */


		/*--------------------Build the query-------------------*/

		try{
			blastCommand = property.getBlast_dir() + "/blastp" +
					" -query " + property.getTmp_dir() + "/sequenceBlock.fasta" +
					" -db " + property.getDb_dir() + "/" + property.getDatabase() +
					" -outfmt 5" +
					" -out " + fileName +  
					" -num_descriptions " + property.getNum_results();
					
			if(Integer.parseInt( property.getThreads() ) >= 2){
				blastCommand += " -num_threads " + property.getThreads();
			}
			//log the blast command
			if(log.isDebugEnabled()){log.debug( "Blast Command: " + blastCommand);}
		} catch(Throwable t){
			log.error("Failed to build query string.", t);
			Main.passfail = Main.FAIL;
		}

		/* ***************************************************** */



		/*-------------- Execute the blast query ----------------*/

		try{
			process = Runtime.getRuntime().exec(blastCommand);
			process.waitFor();
			
		} catch(Throwable t){
			log.error("Failed to run blast command: " + blastCommand + " \nExit code: " + process.exitValue(), t);
			Main.passfail = Main.FAIL;
		}

		/* ***************************************************** */

		
		/*------------------------------------ Read the standard error --------------------------------------*/
		try {

			inputStream = process.getErrorStream();
			bufferedReader = new BufferedReader(new InputStreamReader( inputStream ));
			while ((buff = bufferedReader.readLine()) != null) { stdErr+=buff+"\n"; }
			if(log.isDebugEnabled()){ log.debug("BLAST Standard error: " + stdErr); }

		} catch (Throwable t) {

			log.error("Could not read standard error stream. getErrorStream() ", t);

			if (inputStream != null && bufferedReader != null) {
				try {
					inputStream.close();
					bufferedReader.close();
				} catch (Throwable th) {
					log.error("Could not close standard error stream. from: p.getErrorStream()", th);
				}
			}
		}
		/* ************************************************************************************************* */
		

		/*--------------Read the xml file ----------------*/

		try{
			byte[] buffer = new byte[(int) new File(fileName).length()];
			try {
				bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName));
				bufferedInputStream.read(buffer);
			} catch(IOException ioe){
				log.error("Failed to read xml file: " + fileName, ioe);
				throw new IOException("Failed to read xml file: " + fileName, ioe);
			} 
			finally {
				if (bufferedInputStream != null) 
					try {
						bufferedInputStream.close(); 
					} catch (IOException xmlreader) {
						log.warn("Possilbe memory leak. BufferedInputStream was not closed.");
					}
			}
			xml = new String(buffer);
			
		} catch(Throwable t){
			log.error("Failed to read xml file from blast output.", t);
			Main.passfail = Main.FAIL;
		}

		/* ***************************************************** */

		
		/*--------------------------------------- Delete unused file ----------------------------------------*/

		try{
			deleteFile = new DeleteFile();
			deleteFile.delete(property.getTmp_dir() + "/sequenceBlock.fasta");
			deleteFile.delete(fileName);
		} catch(Throwable t){
			log.warn("Failed to remove unused files: " + fileName + " and/or " + property.getTmp_dir() + "/sequenceBlock.fasta", t);
		}

		/* ************************************************************************************************* */


		/* ---If the standard error is null/empty and standard out is NOT null/empty, continue to parsing--- */


		if ((!xml.equals(null) && !xml.equals("")) && (stdErr.equals(null) || stdErr.equals(""))){

			//log.debug(xml);
			return new File(fileName);

		}
		else if (!xml.startsWith("Error:")){
			return new File(fileName);
		}
		else {
			log.error(stdErr);
			Main.passfail = Main.FAIL;
			return null;
		}

		/* ************************************************************************************************* */

	}
}
