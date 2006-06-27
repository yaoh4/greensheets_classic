/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.reports;

import java.util.Date;

/**
 * ValueObject for the Specialist Status Report
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class SpecialistStatusVO {

	private String fullGrantNum;

	private String applId;

	private String submitterId;

	private Date submittedDate;

	public SpecialistStatusVO(String fullGrantNum, String applId,
			String userId, Date submittedDate) {
		this.fullGrantNum = fullGrantNum;
		this.submitterId = userId;
		this.submittedDate = submittedDate;
		this.applId = applId;

	}

	/**
	 * Returns the fullGrantNum.
	 * 
	 * @return String
	 */
	public String getFullGrantNum() {
		return fullGrantNum;
	}

	/**
	 * Returns the submitterId.
	 * 
	 * @return String
	 */
	public String getSubmitterId() {
		return submitterId;
	}

	/**
	 * Returns the submittedDate.
	 * 
	 * @return Date
	 */
	public Date getSubmittedDate() {
		return submittedDate;
	}

	/**
	 * Returns the applId.
	 * 
	 * @return String
	 */
	public String getApplId() {
		if (applId.equals("0")) {
			return null;
		} else {
			return applId;
		}
	}

}
