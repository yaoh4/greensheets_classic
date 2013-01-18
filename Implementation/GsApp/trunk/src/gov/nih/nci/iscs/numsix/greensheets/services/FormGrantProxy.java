package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.Properties;

import org.apache.commons.lang.builder.ToStringBuilder;

public class FormGrantProxy extends FormGrant {
	private GsUser user;

	public FormGrantProxy(GsUser user) {
		this.user = user;
	}

	public String getGrantNumberSort() {
		String grantDetailViewerURL = ((Properties) AppConfigProperties.getInstance()
				.getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES))
				.getProperty("url.grantdetailviewer");

		String grantDetailsURL = "<a href=\"javascript:getGrantDetail('"
									+ this.getFullGrantNum() + 
									"'," + 
									"'" + 
									this.getApplId() +
									"','" +
									grantDetailViewerURL +
									"');\">" + 
									this.getFullGrantNum() + "</a>";
		return grantDetailsURL;
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getCayCode()}
	 */
	@Deprecated		
	public String getCancerActivity() {
		return this.getCayCode();
	}	
	
	public String getSpecialist() {
		if (this.getGmsLastName() == null || this.getGmsFirstName() == null) {
			return "";
		} else {
			return this.getGmsLastName() + "," + this.getGmsFirstName();
		}
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getBkupGmsUserId()}
	 */
	@Deprecated		
	public String getBackupSpecialist() { //Task to do: See if this could be renamed getBackUpSpecialist(), if it is right. Check GsPdfRenderer class.
		return this.getBkupGmsUserId(); 
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getProgramFormStatus()}
	 */
	@Deprecated		
	public String getProgramStatus() {
		return this.getProgramFormStatus();
	}	

	public String getProgramGreensheet() {
		if (this.getPgmFormStatus() != null) {
			return this.setGreensheetString(GreensheetGroupType.PGM.getName(), this.getPgmFormStatus()) + getOnControlFlagCharacter();
		} else {
			return null;
		}
	}	
	
	public String getProgramLockIcon() {
		String lockIcon = null;
		String value = null; // Bug#4303 Abdul: Don't show the hyperlink for
								// inappropriate action.

		if (this.getPgmFormStatus().equalsIgnoreCase(GreensheetStatus.UNSUBMITTED.getName())) {
			lockIcon = "IconUnlocked.gif";
		} else if (this.getPgmFormStatus().equalsIgnoreCase(GreensheetStatus.SUBMITTED.getName())) {
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
					+ this.getFullGrantNum()
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
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getSpecialistFormStatus()}
	 */
	@Deprecated	
	public String getSpecialistStatus() {
		return this.getSpecialistFormStatus();
	}
	
	public String getSpecialistGreensheet() {
		if (this.getSpecFormStatus() != null) {
			return this.setGreensheetString(GreensheetGroupType.SPEC.getName(), this.getSpecFormStatus()) + getOnControlFlagCharacter();
		} else {
			return null;
		}
	}	
	
	public String getSpecialistLockIcon() {
		String lockIcon = null;
		String value = null;	//Bug#4303 Abdul: Don't show the hyperlink for inappropriate action.

		if (this.getSpecFormStatus().equalsIgnoreCase(GreensheetStatus.UNSUBMITTED.getName())) {
			lockIcon = "IconUnlocked.gif";
		} else if (this.getSpecFormStatus().equalsIgnoreCase(
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
				+ this.getFullGrantNum()
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
	
	private String getOnControlFlagCharacter() {
		String onControlFlag = "";
		if (this.isOnControl() && 
			(user.getRole().equals(GsUserRole.PGM_ANL) || user.getRole().equals(GsUserRole.PGM_DIR))) {
			onControlFlag = "<img src=\"images/IconInControl.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets In Control\">";
		}

		return onControlFlag;
	}

	//Task to do: Rewrite the following comments.
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
					attname ="E" + this.getLatestBudgetStartDate();
					greensheetIcon = "<img src=\"images/IconLocked.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Submitted\">";
				} else if ( Status.equalsIgnoreCase("UNSUBMITTED")){
					attname = "C" + this.getLatestBudgetStartDate();
					greensheetIcon = "<img src=\"images/IconUnlocked.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets UnSubmitted\">";
				} else if (Status.equalsIgnoreCase("SAVED")){
					if (this.isOnControl()
							&& (user.getRole().equals(GsUserRole.PGM_ANL) || user.getRole()
									.equals(GsUserRole.PGM_DIR))) {
					attname = "B" + this.getLatestBudgetStartDate(); } else {
						attname = "D" + this.getLatestBudgetStartDate();
					}
					greensheetIcon = "<img src=\"images/IconSaved.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Saved\">";
				} else if (Status.equalsIgnoreCase("FROZEN")){
					attname = "G" + this.getLatestBudgetStartDate();
					greensheetIcon = "<img src=\"images/IconFrozen.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Frozen\">";
				} else {
					if (this.isOnControl()
							&& (user.getRole().equals(GsUserRole.PGM_ANL) || user.getRole()
									.equals(GsUserRole.PGM_DIR))) {
					attname = "A"+ this.getLatestBudgetStartDate(); } else {
						attname = "F" + this.getLatestBudgetStartDate();
					}
					greensheetIcon ="<img src=\"images/Spacer.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets Not Started\">";
				}
			}
			
			return buildURL(attname, group,greensheetIcon);


	}
	
	private String buildURL(String Attribute, String group, String greenSheetSatusIcon) {
		return "<a name =\" "+ Attribute.trim() +"\" href=\"javascript:retreieveGreensheet('"
		+ this.getFullGrantNum()
		+ "','"
		+ this.getApplId()
		+ "','"
		+ group
		+ "','"
		+ this.user.getOracleId()
		+ "');\">" 
		+ "<img src=\"images/IconGreensheets.gif\" width=\"15\" height=\"18\" border=\"0\" alt=\"Greensheets\">"  
		+ "</a>"
		+ greenSheetSatusIcon ;
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getPriorityScoreNum()}
	 */
	public String getPriorityScore() {
		return String.valueOf(this.getPriorityScoreNum());
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getIrgPercentileNum()}
	 */	
	public Double getPercentileScore() {
		return Double.valueOf(this.getIrgPercentileNum());
	}

	@Override
	public String getCouncilMeetingDate() {
		String councilMeetingDate = super.getCouncilMeetingDate();
		String dateString = null;
		if (councilMeetingDate == null || councilMeetingDate.trim().equals("")) {
			return null;
		} else {
			// The DB data is in the form YYYYMM, switch it to MM/YYYY.
			//Abdul: GREENSHEETS-289 - Fix starts below --------
			// The Database has 00 for a very few rows in the column of council_meeting_date
			if (councilMeetingDate.length() < 6) {
				// In the above case, the data is not in the expected format (YYYYMM).
				dateString = "";
				return dateString;
			}
			//Abdul: GREENSHEETS-289 - Fix ends above --------
			String month = councilMeetingDate.substring(4, councilMeetingDate.length());
			String year = councilMeetingDate.substring(0, 4);
			if (month.equalsIgnoreCase("00")) {
				dateString = "";
			} else {
				dateString = year + "/" + month;
			}

			return dateString;
		}
	}
	
	public String getPdName() {
		if (this.getPdLastName() == null || this.getPdFirstName() == null) {
			return "";
		} else {
			return this.getPdLastName() + "," + this.getPdFirstName();
		}
	}
	
	public String getPrimarySpecialist() {
		if (this.getGmsLastName() == null || this.getGmsFirstName() == null) {
			return "";
		} else {
			return this.getGmsLastName() + "," + this.getGmsFirstName();
		}
	}

	/**
	 * @deprecated As of release v2.5, replaced by {@link #getBkupGmsCode()}
	 */
	@Deprecated
	public String getBackupSpecialistCode() {
		return this.getBkupGmsCode();
	}

	public String getPd() { // See if this.getPiName() would do good.
		if (this.getPdLastName() == null || this.getPdFirstName() == null) {
			return "";
		} else {
			return this.getPdLastName() + "," + this.getPdFirstName();
		}
	}	

	/**
	 * @deprecated As of release v2.5, replaced by {@link #getPiName()}
	 */
	@Deprecated
	public String getPi() {
		if (this.getPiLastName() == null || this.getPiFirstName() == null) {
			return "";
		} else {
			return this.getPiLastName() + "," + this.getPiFirstName();
		}
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getGmsCode()}
	 */
	@Deprecated
	public String getPrimarySpecialistCode() {
		return this.getGmsCode();
	}
	
	//GsGrant has getApplId() with the following String return type. See how this can be reconciled or its use could be eliminated.
	public String getApplID() { // This is essentially same as getApplId(). Since I can't override getApplID(), I renamed this as though this is a different method.
		return Long.toString(super.getApplId());
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getFullGrantNum()}
	 */
	@Deprecated
	public String getFullGrantNumber() {
		return this.getFullGrantNum();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getActivityCode()}
	 */
	public String getMech() {
		return this.getActivityCode();
	}

	/**
	 * @deprecated As of release v2.5, replaced by {@link #getApplTypeCode()}
	 */
	public String getType() {
		return this.getApplTypeCode();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #isOnControl()}
	 */
	@Deprecated
	public boolean isGrantOnControl() {
		return this.isOnControl();
	}
	
	//Check if this is correct. Copied over from the old GsGrant.
	public String getPdCode() {
		return this.getRoleUsageCode();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getIrgPercentileNum()}
	 */
	@Deprecated	
	public String getPercentileNumber() {
			return Integer.toString(this.getIrgPercentileNum());
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getPgmFormStatus()}
	 */
	@Deprecated		
	public String getProgramFormStatus() {
		if (this.getPgmFormStatus() != null) {
			return this.getPgmFormStatus();
		} else {
			return null;
		}
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getSpecFormStatus()}
	 */
	@Deprecated		
	public String getSpecialistFormStatus() {
		if (this.getSpecFormStatus() != null) {
			return this.getSpecFormStatus();
		} else {
			return null;
		}
	}
	
	// Task to do: See if we can continue the old style.
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #isDummy()}
	 */
	@Deprecated		
	public boolean isDummyGrant() {
		return this.isDummy();
	}	
}
