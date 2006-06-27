/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * The GsUser class represents the application user. It contains information
 * related to user identity and role within the application.
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GsUser {

	private GsUserRole role;

	private String remoteUserName;

	private String oracleId;

	private List cancerActivities = null;

	private List myPortfolioIds = null;

	private NciPerson nciPerson;

	private boolean canEdit;

	private static final Logger logger = Logger.getLogger(GsUser.class);

	public GsUser() {
	}

	public GsUser(NciPerson np) {
		this.nciPerson = np;
	}

	public NciPerson getNciPerson() {
		// return this.getNciPerson();
		return this.nciPerson;
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

}
