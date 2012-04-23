package org.blastall.insert.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class InsertProteinsForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private FormFile file;
	private String passfail;
	private String proteins;
	
	/**
	 * @return the file
	 */
	public FormFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}
	/**
	 * @return the passfail
	 */
	public String getPassfail() {
		return passfail;
	}
	/**
	 * @param passfail the passfail to set
	 */
	public void setPassfail(String passfail) {
		this.passfail = passfail;
	}
	/**
	 * @return the proteins
	 */
	public String getProteins() {
		return proteins;
	}
	/**
	 * @param proteins the proteins to set
	 */
	public void setProteins(String proteins) {
		this.proteins = proteins;
	}
	
	
}
