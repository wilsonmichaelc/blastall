package org.blastall.blast.utils;

import java.io.File;

import org.apache.log4j.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;

public class SendProteins {
	
	private static Logger log = Logger.getLogger(SendProteins.class);
	private static final String PASSFAIL = "passfail";
	private static final String PROTEINS = "proteins";
	private static final String FILE = "file";
	
	public void send(String post_xml, File xmlFileName, String passfail, String proteins) throws Throwable{
		
		/* ---------- Some things that we will need. -------------*/

		HttpClient httpclient = null;
		HttpPost post = null;
		HttpResponse response = null;
		MultipartEntity requestEntity = null;
		HttpEntity responseEntity = null;
		
		FileBody fileBody = null;
		StringBody passfailBody = null;
		StringBody proteinsBody = null;
		
		/* ***************************************************** */

		
        try {
        	
    		/* ------------------- Instantiate ----------------------*/

        	httpclient = new DefaultHttpClient();
            post = new HttpPost(post_xml);
            fileBody = new FileBody(xmlFileName);
            passfailBody = new StringBody(passfail);
            proteinsBody = new StringBody(proteins);
            
    		/* ***************************************************** */

            
    		/* ------- Create the multipart and add the parts -------*/

            requestEntity = new MultipartEntity();
            requestEntity.addPart(FILE, fileBody);
            requestEntity.addPart(PASSFAIL, passfailBody);
            requestEntity.addPart(PROTEINS, proteinsBody);
            
    		/* ***************************************************** */


    		/* ----------- Setup the post and execute it -------------*/
            
            post.setEntity(requestEntity);
            response = httpclient.execute(post);
            responseEntity = response.getEntity();

            if(log.isDebugEnabled()){ log.debug(response.getStatusLine()); }
            
    		/* ***************************************************** */

            
    		/* ---------- Check the lenght of the response ----------*/
            
            if (responseEntity != null) {
                log.debug("Response content length: " + responseEntity.getContentLength());
            }
            
    		/* ***************************************************** */

            
    		/* ---------- consume the entity so it's GC'd -----------*/

            EntityUtils.consume(responseEntity);
            
    		/* ***************************************************** */

            
        } finally {
        	
    		/* --------------- Close the connection -----------------*/
        	
            try { httpclient.getConnectionManager().shutdown(); } catch (Exception ignore) {}
            
    		/* ***************************************************** */

        }
		
		
	}

}
