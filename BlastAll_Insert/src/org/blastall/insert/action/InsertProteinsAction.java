package org.blastall.insert.action;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import org.blastall.insert.util.Cleanup;
import org.blastall.insert.util.XMLParser;
import org.blastall.insert.util.XMLProcessor;

/**
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 15, 2012
 */
public class InsertProteinsAction extends Action {

	private static final Logger log = Logger.getLogger(InsertProteinsAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)  
	{

		/* ----------Some things that we will need. -------------*/
		
		Writer writer = null;
		InsertProteinsForm form = null;
		FormFile xmlFormFile = null;
		String passfail = null;
		String proteins = null;
		
		
		/* ---------- Try to capture the request and process the xml string -------------*/
		
		try{
			
			/*---------------- Get a response writer so we can response to the request --------------*/

			writer = response.getWriter();
			
			if(log.isDebugEnabled()){ log.debug("Processing request from: " + request.getRemoteAddr()); }

			/* ************************************************************************************* */	

			
			/*--------------------------- Get the request form data ---------------------------------*/
			
			form = (InsertProteinsForm) actionForm;
			xmlFormFile = form.getFile();
			passfail = form.getPassfail();
			proteins = form.getProteins();
			
			/* ************************************************************************************* */	

			
			/*------------- If we got what was expected, then write success, else write fail -----------*/
			
			if(xmlFormFile != null && passfail != null && proteins != null){
				writer.write("success");
				log.debug("Got XMLFile of size: " + xmlFormFile.getFileSize() + "\nPassfail: " + passfail + "\nProtein List of size: " + proteins.length());
			} else {
				writer.write("fail");
				log.debug("XMLFile size: " + xmlFormFile.getFileSize() + "\nPassfail: " + passfail + "\nProtein List of size: " + proteins.length());
			}
			
			/* ************************************************************************************* */	

			try{

				/*--------------------------- Get a parser and a processor ------------------------------*/
				
				XMLParser xmlparser = null;
				XMLProcessor xmlprocessor = null;

				/* ************************************************************************************* */	

				
				/*-------- Check to make sure that the request we got was not a failed BLAST ------------*/
				
				if( (passfail.equals("pass")) && (proteins != null) ){

					xmlparser = new XMLParser();
					xmlprocessor = new XMLProcessor();
					xmlprocessor.process(xmlparser.parse(xmlFormFile.getInputStream(), proteins), proteins);

				/* ************************************************************************************* */	
					
				/*---------------- If the BLAST did fail, then go to cleanup mode -----------------------*/
					
				} else {

					if(proteins != null){
						Cleanup cleanup = null;
						try{
							cleanup = new Cleanup();
							cleanup.clean(proteins);
						} catch(Throwable u){
							log.error("Failed to cleanup failed insertions.");
							throw u;
						}
					} else {
						log.error("Check database manually for incomplete requests. I recommend: delete from blast_request where finishDate is null;");
					}

				}
				
				/* ************************************************************************************* */	

			} catch(Throwable t){

				log.error("Failed to process xml.", t);

			}


		}catch(Throwable a){

			log.error("Failed to get request.", a);

		} finally {

			/*-----------------------Don't forget to close the writer--------------------------------*/
			
			try {

				if(writer != null){
					writer.close();
				}

			} catch (Throwable t) {

				log.warn("The Writer was not closed.", t);
			}
			
			/* ************************************************************************************* */	


		}

		return null;
	}

}
