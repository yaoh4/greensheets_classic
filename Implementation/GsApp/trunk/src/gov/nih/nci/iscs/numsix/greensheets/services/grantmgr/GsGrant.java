/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.grantmgr;

import gov.nih.nci.iscs.i2e.grantretriever.FormGrant;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * Represents a NCI GsGrant
 * 
 * 
 * @author kpuscas, Number Six Software
 */

public class GsGrant {

	private String pi;

	private String pdName;

	private String pdCode;

	private String primarySpecialist;

	private String primarySpecialistCode;

	private String backupSpecialist;

	private String backupSpecialistCode;

	private String orgName;

	private String fullGrantNumber;

	private String applId;

	private String type;

	private String mech;

	private String budgetStartDate;

	private String latestBudgetStartDate;

	private float percentileNumber;

	private int priorityScore;

	private boolean grantOnControl;

	private String cancerActivity;

	private String programFormStatus;

	private String specialistFormStatus;

	private java.util.Date councilMeetingDate;

	private FormGrant formGrant;

	private static final Logger logger = Logger.getLogger(GsGrant.class);

	/**
	 * Constructor for GsGrant.
	 */
	public GsGrant() {
	}

	// public GsGrant(
	// String pi,
	// String pdName,
	// String pdCode,
	// String primarySpecialist,
	// String primarySpecialistCode,
	// String backupSpecialist,
	// String backupSpecialistCode,
	// String orgName,
	// String fullGrantNumber,
	// String applId,
	// String type,
	// String mech,
	// String budgetStartDate,
	// float percentileNumber,
	// int priorityScore,
	// boolean grantOnControl,
	// String cancerActivity,
	// String programFormStatus,
	// String specialistFormStatus) {
	// this.pi = pi;
	// this.pdName = pdName;
	// this.pdCode = pdCode;
	// this.primarySpecialist = primarySpecialist;
	// this.primarySpecialistCode = primarySpecialistCode;
	// this.backupSpecialist = backupSpecialist;
	// this.backupSpecialistCode = backupSpecialistCode;
	// this.orgName = orgName;
	// this.fullGrantNumber = fullGrantNumber;
	// this.applId = applId;
	// this.type = type;
	// this.mech = mech;
	// this.budgetStartDate = budgetStartDate;
	// this.percentileNumber = percentileNumber;
	// this.priorityScore = priorityScore;
	// this.grantOnControl = grantOnControl;
	// this.cancerActivity = cancerActivity;
	// this.programFormStatus = programFormStatus;
	// this.specialistFormStatus = specialistFormStatus;
	//
	// }

	/**
	 * Returns the backupSpecialist.
	 * 
	 * @return String
	 */
	public String getBackupSpecialist() {
		return formGrant.getBkupGmsUserId();

	}

	/**
	 * Returns the backupSpecialistCode.
	 * 
	 * @return String
	 */
	public String getBackupSpecialistCode() {
		return formGrant.getBkupGmsCode();
	}

	/**
	 * Returns the orgName.
	 * 
	 * @return String
	 */
	public String getOrgName() {
		return formGrant.getOrgName();
	}

	/**
	 * Returns the pdName.
	 * 
	 * @return String
	 */
	public String getPdName() {

		if (formGrant.getPdLastName() == null
				|| formGrant.getPdFirstName() == null) {
			return "";
		} else {
			return formGrant.getPdLastName() + "," + formGrant.getPdFirstName();
		}

	}

	/**
	 * Returns the pi.
	 * 
	 * @return String
	 */
	public String getPi() {

		if (formGrant.getLastName() == null || formGrant.getFirstName() == null) {
			return "";
		} else {
			return formGrant.getLastName() + "," + formGrant.getFirstName();
		}

	}

	/**
	 * Returns the primarySpecialist.
	 * 
	 * @return String
	 */
	public String getPrimarySpecialist() {

		if (formGrant.getGmsLastName() == null
				|| formGrant.getGmsFirstName() == null) {
			return "";
		} else {
			return formGrant.getGmsLastName() + ","
					+ formGrant.getGmsFirstName();
		}
	}

	/**
	 * Returns the primarySpecialistCode.
	 * 
	 * @return String
	 */
	public String getPrimarySpecialistCode() {
		return formGrant.getGmsCode();
	}

	/**
	 * Returns the applId.
	 * 
	 * @return String
	 */
	public String getApplId() {
		return formGrant.getApplId().toString();
	}

	/**
	 * Returns the fullGrantNumber.
	 * 
	 * @return String
	 */
	public String getFullGrantNumber() {
		return formGrant.getFullGrantNum();
	}

	/**
	 * Returns the mech.
	 * 
	 * @return String
	 */
	public String getMech() {
		return formGrant.getActivityCode();
	}

	/**
	 * Returns the type.
	 * 
	 * @return String
	 */
	public String getType() {
		return formGrant.getApplTypeCode();

	}

	/**
	 * Returns the grantOnControl.
	 * 
	 * @return boolean
	 */
	public boolean isGrantOnControl() {
		String flag = formGrant.getOnControlFlag();
		if (flag.equalsIgnoreCase("Y")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the pdCode.
	 * 
	 * @return String
	 */
	public String getPdCode() {

		return formGrant.getRoleUsageCode();
	}

	/**
	 * Returns the percentileNumber.
	 * 
	 * @return float
	 */
	public String getPercentileNumber() {
		if (formGrant.getIrgPercentileNum() == null) {
			return "";
		} else {
			return formGrant.getIrgPercentileNum().toString();
		}

	}

	/**
	 * Returns the priorityScore.
	 * 
	 * @return int
	 */
	public String getPriorityScore() {
		if (formGrant.getPriorityScoreNum() == null) {
			return "";
		} else {
			return formGrant.getPriorityScoreNum().toString();
		}
	}

	/**
	 * Returns the programFormStatus.
	 * 
	 * @return String
	 */
	public String getProgramFormStatus() {
		return formGrant.getPgmFormStatus();

	}

	/**
	 * Returns the specialistFormStatus.
	 * 
	 * @return String
	 */
	public String getSpecialistFormStatus() {
		return formGrant.getSpecFormStatus();
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * Returns the budgetStartDate.
	 * 
	 * @return Date
	 */
	public java.util.Date getBudgetStartDate() {

		if (formGrant.getBudgetStartDate() == null) {
			return null;
		} else {

			return formGrant.getBudgetStartDate();
		}
	}

	/**
	 * Returns the latestBudgetStartDate.
	 * 
	 * @return Date
	 */
	public java.util.Date getLatestBudgetStartDate() {

		if (formGrant.getLatestBudgetStartDate() == null) {
			return null;
		} else {

			return formGrant.getLatestBudgetStartDate();
		}
	}

	/**
	 * Returns the getPgmFormSubmittedDate.
	 * 
	 * @return Date
	 */
	public java.util.Date getPgmFormSubmittedDate() {

		if (formGrant.getPgmFormSubmittedDate() == null) {
			return null;
		} else {

			return formGrant.getPgmFormSubmittedDate();
		}
	}

	public String getCouncilMeetingDate() {
		String councilMeetingDate = formGrant.getCouncilMeetingDate();
		String dateString = null;
		if (councilMeetingDate == null || councilMeetingDate.trim().equals("")) {
			return null;
		} else {

			// the raw data is in the form YYYYMM, switch it to MM/YYYY
			String month = councilMeetingDate.substring(4, councilMeetingDate
					.length());
			String year = councilMeetingDate.substring(0, 4);
			if (month.equalsIgnoreCase("00")) {
				dateString = "";
			} else {

				dateString = year + "/" + month;
			}

			return dateString;
		}
	}

	/**
	 * Returns the cancerActivity.
	 * 
	 * @return String
	 */
	public String getCancerActivity() {
		return formGrant.getCayCode();
	}

	public boolean isDummyGrant() {
		String flag = formGrant.getDummyFlag();
		if (flag.equalsIgnoreCase("Y")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the formGrant.
	 * 
	 * @return FormGrant
	 */
	public FormGrant getFormGrant() {
		return formGrant;
	}

	/**
	 * Sets the formGrant.
	 * 
	 * @param formGrant
	 *            The formGrant to set
	 */
	public void setFormGrant(FormGrant formGrant) {
		this.formGrant = formGrant;
	}

}
