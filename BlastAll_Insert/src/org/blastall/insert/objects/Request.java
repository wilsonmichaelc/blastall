package org.blastall.insert.objects;

/**
 * This class is a BLAST Request
 * 
 * @author Michael Wilson <wilsonmc@u.washington.edu>
 * @version Feb 2, 2012
 */
public class Request {
	
	private String sequence;
	private int proteinId;
	private String submitDate;
	
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public int getProteinId() {
		return proteinId;
	}
	public void setProteinId(int proteinId) {
		this.proteinId = proteinId;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}

}
