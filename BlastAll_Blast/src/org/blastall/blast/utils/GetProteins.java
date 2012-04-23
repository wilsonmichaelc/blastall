package org.blastall.blast.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.blastall.blast.main.Main;

public class GetProteins {

	private static Logger log = Logger.getLogger(GetProteins.class);
	private static final String REQUEST_MSG = "msg";
	
	public String get( String post_proteins ) throws Throwable{
		
		
		/* -------------- Some things that we will need. ---------------*/

		HttpClient client = null;
		HttpPost post = null;
		BufferedReader rd = null;
		List<NameValuePair> nameValuePairs = null;
		HttpResponse response = null;
		String line = "";
		
		/* ************************************************************ */

		try {
			
			/* --------- get the client, post, and nameValuePairs -----------*/

			client = new DefaultHttpClient();
			post = new HttpPost(post_proteins);
			nameValuePairs = new ArrayList<NameValuePair>(1);
			
			/* ************************************************************ */

			
			/* ------- Add the request message to the nameValuePairs -------*/
			
			nameValuePairs.add(new BasicNameValuePair(REQUEST_MSG, "Proteins Please!"));
			
			/* ************************************************************ */


			/* --------- Set the nameValuePairs as a post entity -----------*/
			
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			/* ************************************************************ */


			/* -------- Execute the post whilst saving the response --------*/
			
			response = client.execute(post);
			
			/* ************************************************************ */


			/* --------- Log the response if logging is enabled ------------*/
			
			if(log.isDebugEnabled()){ log.debug(response); }
			
			/* ************************************************************ */

			
			/* ------------------- Buffer the response ---------------------*/
			
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			/* ************************************************************ */


			/* --------- While the buffer has lines, read them -------------*/
			
			while ((line = rd.readLine()) != null) {
				
				//if(log.isDebugEnabled()){ log.debug("Response: " + line); }
				return line;

			}
			
			/* ************************************************************ */

			
		} catch (Throwable t) {
			
			/* --------- If we failed at all, then set the passfail to FAIL ---------*/
			
			log.error("Failed to get proteins.", t);
			Main.passfail = Main.FAIL;
			
			/* ********************************************************************* */

			
		} finally { 
			
			/* --------- Don't forget to close the buffered reader -------------*/
			
			if(rd != null) { 
				rd.close(); 			
			} 
		
			/* **************************************************************** */

		}
		
		/* ---------- If we make it here, then return an empty string. ----------- */

		return "";

		/* *********************************************************************** */

	}
	
}
