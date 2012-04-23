package org.blastall.load.utils;

/**
 * This class is a BLAST Request
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class Request {
	
	private String sequence;
	private String header;
	
	/**
	 * @return
	 */
	public String getSequence() {
		return sequence;
	}
	
	/**
	 * @param sequence
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	/**
	 * @return
	 */
	public String getHeader() {
		return header;
	}
	
	/**
	 * @param header
	 */
	public void setHeader(String header) {
		this.header = header;
	}

}
