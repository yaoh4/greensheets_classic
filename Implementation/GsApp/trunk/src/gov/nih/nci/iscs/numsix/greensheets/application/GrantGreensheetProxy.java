/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class wraps information from a grant and greensheet for presentation in the
 * grants list table
 *
 *
 * @author kpuscas, Number Six Software
 */
public class GrantGreensheetProxy {

	private GsGrant grant;

	private GsUser user;

	private String userOracleId;

	private static final Logger logger = Logger
			.getLogger(GrantGreensheetProxy.class);

	public GrantGreensheetProxy(GsGrant grant, GsUser user) {
		this.grant = grant;
		this.user = user;
		this.userOracleId = user.getOracleId();
	}

	private String getOnControlFlagCharacter() {
		String onControlFlag = "";
		if (grant.isGrantOnControl()
				&& (user.getRole().equals(GsUserRole.PGM_ANL) || user.getRole()
						.equals(GsUserRole.PGM_DIR))) {
			onControlFlag = "<img src=\"images/IconInControl.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets In Control\">";
		}

		return onControlFlag;
	}

	/**
	 * Returns the grantNumber.
	 *
	 * @return String
	 */
	public String getGrantNumber() {
		return grant.getFullGrantNumber();// + getOnControlFlagCharacter();

	}

	public String getGrantNumberSort() {

		String url = ((Properties) AppConfigProperties.getInstance()
				.getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES))
				.getProperty("url.grantdetailviewer");

		String value = "<a href=\"javascript:getGrantDetail('"
				+ getGrantNumber() + "'," + "'" + grant.getApplId() + "','"
				+ url + "');\">" + getGrantNumber() + "</a>";

		return value;
	}

	public String getApplId() {
		return grant.getApplId();
	}

	public String getPrincipalInvestigator() {
		return grant.getPi();
	}

	public Date getBudgetStartDate() {
		return grant.getBudgetStartDate();
	}

	public Date getLatestBudgetStartDate() {
		return grant.getLatestBudgetStartDate();
	}

	public Date getPgmFormSubmittedDate() {
		return grant.getPgmFormSubmittedDate();
	}

	public String getCouncilMeetingDate() {
		return grant.getCouncilMeetingDate();
	}

	public String getPd() {
		return grant.getPdName();
	}

	public String getPi() {
		return grant.getPi();
	}

	public String getCancerActivity() {
		return grant.getCancerActivity();
	}

	public String getPriorityScore() {
		return grant.getPriorityScore();
	}

	public Double getPercentileScore() throws Exception {
		if (grant.getPercentileNumber().equalsIgnoreCase("")) {
			return null;
		} else {
			Double d = Double.valueOf(grant.getPercentileNumber());

			return d;
		}
	}

	public String getSpecialist() {
		return grant.getPrimarySpecialist();
	}

	public String getBackUpSpecialist() {
		return grant.getBackupSpecialist();
	}

	public String getSpecialistGreensheet() {
		if (grant.getSpecialistFormStatus() != null) {
			return this.setGreensheetString(GreensheetGroupType.SPEC.getName(), grant.getSpecialistFormStatus())
					+ getOnControlFlagCharacter();
		} else {
			return null;
		}
	}

	public String getSpecialistStatus() {
		if (grant.getSpecialistFormStatus() != null) {

			return grant.getSpecialistFormStatus();
		} else {
			return null;
		}
	}

	public String getProgramGreensheet() {
		if (grant.getProgramFormStatus() != null) {
			return this.setGreensheetString(GreensheetGroupType.PGM.getName(),grant.getProgramFormStatus())
					+ getOnControlFlagCharacter();
		} else {
			return null;
		}

	}

	public String getProgramLockIcon() {
		String lockIcon = null;
		String value = null;	//Bug#4303 Abdul: Don't show the hyperlink for inappropriate action.

		if (grant.getProgramFormStatus().equalsIgnoreCase(
				GreensheetStatus.UNSUBMITTED.getName())) {
			lockIcon = "IconUnlocked.gif";
		} else if (grant.getProgramFormStatus().equalsIgnoreCase(
				GreensheetStatus.SUBMITTED.getName())) {
			lockIcon = "IconLocked.gif";
		} else {
			lockIcon = "IconNoLocking.gif";
		}

		if (lockIcon == null || lockIcon.equalsIgnoreCase("IconNoLocking.gif")) {
			value = "<img src=\"images/"
				+ lockIcon
				+ "\" width=\"15\" height=\"18\" border=\"0\" alt=\"ChangeLock\">";
		} else {
			value = "<a href=\"javascript:changeLock('"
					+ grant.getFullGrantNumber()
					+ "','"
					+ this.getApplId()
					+ "','"
					+ GreensheetGroupType.PGM.getName()
					+ "');\">"
					+ "<img src=\"images/"
					+ lockIcon
					+ "\" width=\"15\" height=\"18\" border=\"0\" alt=\"ChangeLock\"></a>";
		}
		return value;
	}

	public String getSpecialistLockIcon() {
		String lockIcon = null;
		String value = null;	//Bug#4303 Abdul: Don't show the hyperlink for inappropriate action.

		if (grant.getSpecialistFormStatus().equalsIgnoreCase(
				GreensheetStatus.UNSUBMITTED.getName())) {
			lockIcon = "IconUnlocked.gif";
		} else if (grant.getSpecialistFormStatus().equalsIgnoreCase(
				GreensheetStatus.SUBMITTED.getName())) {
			lockIcon = "IconLocked.gif";
		} else {
			lockIcon = "IconNoLocking.gif";
		}

		if (lockIcon == null || lockIcon.equalsIgnoreCase("IconNoLocking.gif")) {
			value = "<img src=\"images/"
				+ lockIcon
				+ "\" width=\"15\" height=\"18\" border=\"0\" alt=\"ChangeLock\">";
		} else {
			value = "<a href=\"javascript:changeLock('"
				+ grant.getFullGrantNumber()
				+ "','"
				+ this.getApplId()
				+ "','"
				+ GreensheetGroupType.SPEC.getName()
				+ "');\">"
				+ "<img src=\"images/"
				+ lockIcon
				+ "\" width=\"15\" height=\"18\" border=\"0\" alt=\"ChangeLock\"></a>";
		}
		return value;
	}

	public String getProgramStatus() {
		if (grant.getProgramFormStatus() != null) {
			return grant.getProgramFormStatus();
		} else {
			return null;
		}

	}

	/**
	 * Returns the userOracleId.
	 *
	 * @return String
	 */
	public String getUserOracleId() {
		return userOracleId;
	}

	/**
	 * Sets the userOracleId.
	 *
	 * @param userOracleId
	 *            The userOracleId to set
	 */
	public void setUserOracleId(String userOracleId) {
		this.userOracleId = userOracleId;
	}
	/**
	 * Returns the URL for opening the greensheet and sets the ordering sequence of the greensheet
	 * which is 
	 * NotStarted IN Control = A
	 * Saved IN Control = B
	 * UnSubmitted = C
	 * Saved NOT IN Control =D
	 * Submitted = E
	 * NOT Started Not In Control = F
	 * Frozen = G
	 *
	 * @return String
	 */
	private String setGreensheetString(String group , String Status ) {
		String attname = "";
		   String greensheetIcon = "";

			if (Status != null) {
				if ( Status.equalsIgnoreCase("SUBMITTED")){
					attname ="E";
					greensheetIcon = "<img src=\"images/IconLocked.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Submitted\">";
				} else if ( Status.equalsIgnoreCase("UNSUBMITTED")){
					attname = "C";
					greensheetIcon = "<img src=\"images/IconUnlocked.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets UnSubmitted\">";
				} else if (Status.equalsIgnoreCase("SAVED")){
					if (grant.isGrantOnControl()
							&& (user.getRole().equals(GsUserRole.PGM_ANL) || user.getRole()
									.equals(GsUserRole.PGM_DIR))) {
					attname = "B"; } else {
						attname = "D";
					}
					greensheetIcon = "<img src=\"images/IconSaved.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Saved\">";
				} else if (Status.equalsIgnoreCase("FROZEN")){
					attname = "G";
					greensheetIcon = "<img src=\"images/IconFrozen.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Frozen\">";
				} else {
					if (grant.isGrantOnControl()
							&& (user.getRole().equals(GsUserRole.PGM_ANL) || user.getRole()
									.equals(GsUserRole.PGM_DIR))) {
					attname = "A"; } else {
						attname = "F";
					}
					greensheetIcon ="<img src=\"images/Spacer.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Not Started\">";
				}
			}
			
			return buildURL(attname, group,greensheetIcon);


	}
	
	private String buildURL (String Attribute, String group, String greenSheetSatusIcon) {
		
		return "<a name =\""+ Attribute.trim() +"\" href=\"javascript:retreieveGreensheet('"
		+ grant.getFullGrantNumber()
		+ "','"
		+ this.getApplId()
		+ "','"
		+ group
		+ "','"
		+ this.getUserOracleId()
		+ "');\">" 
		+ "<img src=\"images/IconGreensheets.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets\">"  
		+ "</a>"
		+ greenSheetSatusIcon ;

		
	}

}
