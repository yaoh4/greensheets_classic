/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

import gov.nih.nci.iscs.numsix.greensheets.application.GreensheetUserSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * The GsUser class represents the application user. It contains information
 * related to user identity and role within the application.
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GsUser implements Serializable {

	private static final long serialVersionUID = -1251126806314707178L;

	private GsUserRole role;
	
	private GsUserRole formBuilderRole;

	private String remoteUserName;

	private String oracleId;

	private List cancerActivities = null;

	private List myPortfolioIds = null;

	private NciPerson nciPerson;

	private boolean canEdit;

	private ArrayList dmChecklistUserPermissions = new ArrayList();	// Could use a better data type.
	
	private Map<String, String> userPreferences;
	private String userName;

	public GsUser() {
	}

	public GsUser(NciPerson np) {
		this.nciPerson = np;
	}

	public NciPerson getNciPerson() {
		// return this.getNciPerson();
		return this.nciPerson;
	}

	public void setNciPerson(NciPerson nciPerson) {
		this.nciPerson = nciPerson;
	}

	public String getDisplayUserName() {

		return nciPerson.getFirstName() + " " + nciPerson.getLastName();
	}

	/**
	 * Returns the remoteUserName.
	 * 
	 * @return String
	 */
	public String getRemoteUserName() {
		return remoteUserName;
	}

	/**
	 * Returns the role.
	 * 
	 * @return GsUserRole
	 */
	public gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole getRole() {
		return role;
	}

	/**
	 * Returns the role.
	 * 
	 * @return String
	 */
	public String getRoleAsString() {
		return role.getName();
	}

	/**
	 * Sets the remoteUserName.
	 * 
	 * @param remoteUserName
	 *            The remoteUserName to set
	 */
	public void setRemoteUserName(String remoteUserName) {
		this.remoteUserName = remoteUserName;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            The role to set
	 */
	public void setRole(
			gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole role) {
		this.role = role;
	}

	/**
	 * Returns the oracleId.
	 * 
	 * @return String
	 */
	public String getOracleId() {
		return oracleId;
	}

	/**
	 * Sets the oracleId.
	 * 
	 * @param oracleId
	 *            The oracleId to set
	 */
	public void setOracleId(String oracleId) {
		this.oracleId = oracleId;
	}

	public void setMyPortfolioIds(String ids) {
		Object o = null;
		myPortfolioIds = new ArrayList();
		if (ids != null) {
			ids.toUpperCase();

			// a comma separated list of cancer activities
			StringTokenizer tokenizer = new StringTokenizer(ids, ",");
			while (tokenizer.hasMoreElements()) {
				o = tokenizer.nextElement();
				if (!myPortfolioIds.contains(o)) {
					String val = StringUtils.strip((String) o, "'");
					myPortfolioIds.add(val);
				}
			}
		}

	}

	public List getMyPortfolioIds() {
		return this.myPortfolioIds;
	}

	public void setCancerActivities(String theCancerActivities) {

		Object o = null;
		cancerActivities = new ArrayList();
		if (theCancerActivities != null) {
			theCancerActivities.toUpperCase();

			// a comma separated list of cancer activities
			StringTokenizer tokenizer = new StringTokenizer(
					theCancerActivities, ",");
			while (tokenizer.hasMoreElements()) {
				o = tokenizer.nextElement();
				if (!cancerActivities.contains(o)) {
					String val = StringUtils.strip((String) o, "'");
					cancerActivities.add(val);
				}
			}
		}
	}

	/**
	 * Returns the cancerActivities.
	 * 
	 * @return ArrayList
	 */
	public List getCancerActivities() {
		return cancerActivities;
	}

	/**
	 * Returns the canEdit.
	 * 
	 * @return boolean
	 */
	public boolean isCanEdit() {
		return canEdit;
	}

	/**
	 * Sets the canEdit.
	 * 
	 * @param canEdit
	 *            The canEdit to set
	 */
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public void setDmChecklistUserPermissions(GreensheetUserSession gus, DMChecklistUserPermission userPermissions) {
		int ndx = 0;
		
		synchronized (gus.getUser().dmChecklistUserPermissions) {
			// Search thru' the list by APPL_ID and GRANT_ID
			while (ndx < this.dmChecklistUserPermissions.size()) {
				DMChecklistUserPermission userPermissionsTmp = (DMChecklistUserPermission) this.dmChecklistUserPermissions.get(ndx);
				String applId;
				String fullGrantNumber;
				applId = userPermissionsTmp.getApplId();
				fullGrantNumber = userPermissionsTmp.getFullGrantNumber();			
				if ((applId != null & applId.equalsIgnoreCase(userPermissions.getApplId())) && (fullGrantNumber != null && fullGrantNumber.equalsIgnoreCase(userPermissionsTmp.getFullGrantNumber()))) {
					this.dmChecklistUserPermissions.remove(ndx);
					break;
				}
				ndx++;
			}
			this.dmChecklistUserPermissions.add(userPermissions);			
		}
	}
	
	public DMChecklistUserPermission getDmChecklistUserPermissions(GreensheetUserSession gus, String applId, String fullGrantNumber) {
		int ndx = 0;
		
		synchronized (gus.getUser().dmChecklistUserPermissions) {
			// Search thru' the list by APPL_ID and GRANT_ID
			while (ndx < this.dmChecklistUserPermissions.size()) {
				DMChecklistUserPermission userPermissionsTmp = (DMChecklistUserPermission) this.dmChecklistUserPermissions.get(ndx);
				String applIdTmp;
				String fullGrantNumberTmp;
				applIdTmp = userPermissionsTmp.getApplId();
				fullGrantNumberTmp = userPermissionsTmp.getFullGrantNumber();			
				if ((applIdTmp != null & applIdTmp.equalsIgnoreCase(applId)) && (fullGrantNumberTmp != null && fullGrantNumberTmp.equalsIgnoreCase(fullGrantNumber))) {
					return (DMChecklistUserPermission) this.dmChecklistUserPermissions.get(ndx);
				}
				ndx++;
			}		
			return null;			
		}
	}

	public Map<String, String> getUserPreferences() {
		return userPreferences;
	}

	public void setUserPreferences(Map<String, String> userPreferences) {
		this.userPreferences = userPreferences;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userID) {
		this.userName = userID;
	}
	
	public String toString() {
		return "user " + this.remoteUserName + " [" + this.userName + "/" + this.oracleId + ": " +
			this.getRoleAsString() + "]";
	}
	
	public GsUserRole getFormBuilderRole() {
		return formBuilderRole;
	}

	public void setFormBuilderRole(GsUserRole formBuilderRole) {
		this.formBuilderRole = formBuilderRole;
	}
}
