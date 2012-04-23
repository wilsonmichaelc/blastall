package org.blastall.get.action;

import org.apache.struts.action.ActionForm;

/**
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class GetProteinsForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String msg;
	
	/**
	 * @return the flag
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
