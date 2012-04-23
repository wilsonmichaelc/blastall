package org.blastall.blast.utils;

import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.blastall.blast.objects.Property;

public class ReadProperties {

	private static final Logger log = Logger.getLogger(ReadProperties.class);
	private static final String PROPERTIES = "blast_config.properties";
	private Property property = null;
	
	public Property properties() throws Throwable{

		/* ---------- Some things that we will need. ------------*/

		ClassLoader thisClassLoader = ReadProperties.class.getClassLoader();
		InputStream prop = null;
		Properties configProps = null;

		/* ***************************************************** */

		
		try {
			
			/* --------------- Make them not null. ------------------*/

    		prop = thisClassLoader.getResourceAsStream(PROPERTIES);
    		configProps = new Properties();
			configProps.load(prop);
			
			/* ***************************************************** */


			/* ------------ Get the properties we need -------------*/

			String blast_dir = configProps.getProperty("blast_dir");
			String tmp_dir = configProps.getProperty("tmp_dir");
			String db_dir = configProps.getProperty("db_dir");
			String post_proteins = configProps.getProperty("post_proteins");
			String database = configProps.getProperty("database");
			String threads = configProps.getProperty("threads");
			String num_results = configProps.getProperty("num_results");
			String post_xml = configProps.getProperty("post_xml");
			
			/* ***************************************************** */

			
			/* --------- Prepare to check for invalid props ---------*/

			boolean isValid = true;
			String invalidProps = "";
			
			/* ***************************************************** */


			/* ---------------------------------- Check for invalid properties -------------------------------------*/

			if (blast_dir == null || blast_dir.isEmpty()) { isValid = false; invalidProps += " " + blast_dir + " "; }
			
			if (tmp_dir == null || tmp_dir.isEmpty()) { isValid = false; invalidProps += " " + tmp_dir + " "; }
			
			if (db_dir == null || db_dir.isEmpty()) { isValid = false; invalidProps += " " + db_dir + " "; }
				
			if (post_proteins == null || post_proteins.isEmpty()) { isValid = false; invalidProps += " " + post_proteins + " "; }
			
			if (database == null || database.isEmpty()) { isValid = false; invalidProps += " " + database + " "; }

			if (num_results == null || num_results.isEmpty()) { isValid = false; invalidProps += " " + num_results + " "; }

			if (post_xml == null || post_xml.isEmpty()) { isValid = false; invalidProps += " " + post_xml + " "; }
			
			if (threads == null || threads.isEmpty()) { threads = "1"; }
			
			/* ***************************************************************************************************** */


			/* ---------- If there were no invalid properties, then return the property object with -------------*/

			if (isValid) {
				
				property = new Property();
				property.setBlast_dir(blast_dir);
				property.setDb_dir(db_dir);
				property.setTmp_dir(tmp_dir);
				property.setPost_proteins(post_proteins);
				property.setDatabase(database);
				property.setThreads(threads);
				property.setNum_results(num_results);
				property.setPost_xml(post_xml);
				return property;
				
			}

			/* *************************************************************************************************** */
			
			
			/* ---------- If an invalid prop was found, throw an exception -------------*/

			else {
				
				// Log the error and DO NOT CONTINUE
				log.error("There were some invalid properties detected: " + invalidProps);
				throw new Exception("Invalid parameters were found: " + invalidProps);
			
			}
			
			/* ************************************************************************* */


        } catch (Throwable t) {

        	log.error("Error reading properties file: " + PROPERTIES, t);
        	throw t;
        	
        } finally {

        	/* ---------- Don't forget to close the properties file -------------*/

        	try {
        		if (prop != null) {
        			prop.close();
        		}
        	} catch (Throwable t){
        		log.error("Error closing properties file " + PROPERTIES, t);
        	}
        	
    		/* ***************************************************************** */

        }
		
	}
	
}
