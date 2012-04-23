package org.blastall.get.action;

import java.io.Writer;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.blastall.get.util.ProteinSelectionLogic;



/**
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 15, 2012
 */
public class GetProteinsAction extends Action {

	private static final Logger log = Logger.getLogger(GetProteinsAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)  
	{

		// Stuff we need
		Writer writer = null;
		GetProteinsForm form = null;
		String msg = null;
		
		try {

			// instantiate and get the form data
			writer = response.getWriter();
			form = (GetProteinsForm) actionForm;
			msg = form.getMsg();
			
			// Log stuff if logging if enabled.
			if(log.isDebugEnabled()){ log.debug("Processing request from: " + request.getRemoteAddr()); }
			if(log.isDebugEnabled()){ log.debug("Request param: " + msg); }
			
			// Respond to the request with a list of proteins.
			writer.write(ProteinSelectionLogic.getProteinBlock());
			

		} catch (Throwable t) {

			// if we failed, don't throw, just log
			log.error("Could not get list of proteins.", t);

		} finally {
			
			// and always close the writer.
			try {
				
				if(writer != null) { writer.close(); }


			} catch (Throwable t) {

				log.warn("The Writer was not closed.", t);
			}
		}

		return null;
	}
}
