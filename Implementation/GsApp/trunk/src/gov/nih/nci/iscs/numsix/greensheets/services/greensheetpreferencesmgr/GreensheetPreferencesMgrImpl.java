/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr;

import gov.nih.nci.iscs.numsix.greensheets.application.GreensheetUserSession;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.NciPerson;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.*;
import org.apache.log4j.*;
import java.sql.*;

/**
 * Production Implementation of GreensheetPreferencesMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr
 * 
 *  @author kkanchinadam, Number Six Software
 */
public class GreensheetPreferencesMgrImpl implements GreensheetPreferencesMgr {

	// The User object.
	GsUser gsUser;
	
	
	// User Preferences in a Map.
	private HashMap userPrefs;
	
	Properties props;
	
	// Key names, defined in the property file.
	String grantSourceName;
	String grantTypeName;
	String grantMechanismName;
	String grantPaylineName;
	
	// grant source
	String grantSourceDefault;
	String grantSourcePortfolio;
	String grantSourceMyActivities;
	String grantSourceAll;
	
	// grant type
	String grantTypeDefault;
	String grantTypeCompeting;
	String grantTypeNonCompeting;
	String grantTypeBoth;
	
	// grant mechanism
	String grantMechanismDefault;
	
	// grant payline
	String grantPaylineDefault;
	String grantPaylineYes;
	String grantPaylineNo;
	
	private static final Logger logger =
		Logger.getLogger(GreensheetPreferencesMgrImpl.class);

	public GreensheetPreferencesMgrImpl(GsUser user) {		
		this.initialize(user);
    }
	
	private void initialize(GsUser user) {
        gsUser = user;
		props = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_USER_PREFERENCES);		
		userPrefs = this.getUserPreferences();
		this.loadPreferences();
	}
	
	private void loadPreferences() {
		grantSourceName			=	props.getProperty("preferences.grant.source.name");
		grantTypeName			=	props.getProperty("preferences.grant.type.name");
		grantMechanismName		=	props.getProperty("preferences.grant.mechanism.name");
		grantPaylineName		=	props.getProperty("preferences.grant.payline.name");
		
		// grant source
		grantSourceDefault		=	props.getProperty("preferences.grant.source.default");
		grantSourcePortfolio	=	props.getProperty("preferences.grant.source.portfolio");
		grantSourceMyActivities	=	props.getProperty("preferences.grant.source.myactivities");
		grantSourceAll			=	props.getProperty("preferences.grant.source.all");
		
		// grant type
		grantTypeDefault		=	props.getProperty("preferences.grant.type.default");
		grantTypeCompeting		=	props.getProperty("preferences.grant.type.competing");
		grantTypeNonCompeting	=	props.getProperty("preferences.grant.type.noncompeting");
		grantTypeBoth			=	props.getProperty("preferences.grant.type.both");
		
		// grant mechanism
		grantMechanismDefault	=	props.getProperty("preferences.grant.mechanism.default");
		
		// grant payline
		grantPaylineDefault		=	props.getProperty("preferences.grant.payline.default");
		grantPaylineYes			=	props.getProperty("preferences.grant.payline.yes");
		grantPaylineNo			=	props.getProperty("preferences.grant.payline.no");
	}
	
	private String isOptionSelected(boolean flag, boolean isDropdown) {
		String retVal = "";
		if(flag){
			if (isDropdown) {
				retVal = "selected='selected'";
			}
			else {
				retVal = "checked='checked'";
			}
		}
		
		return retVal;
	}
	   
	private HashMap getUserPreferences()  {
		HashMap userPrefs = new HashMap();
		 
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbConnectionHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			
			String userName = this.getUserLdapLoginId();
			
			String sql = "SELECT PREFERENCE_NAME, PREFERENCE_VALUE FROM USER_PREFERENCES_T WHERE APPLICATION_NAME='GREENSHEETS' AND USERNAME='" + userName + "'";
			logger.debug("Retrieving user preferences, sql is =" + sql);
			
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String prefName = rs.getString("PREFERENCE_NAME");
				String prefValue = rs.getString("PREFERENCE_VALUE");
				userPrefs.put(prefName, prefValue);				
			}
		}
		catch (Exception e) {
			logger.debug(e.getMessage());
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				
				DbConnectionHelper.getInstance().freeConnection(conn);
			}
			catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
				
		return userPrefs;
	}
	
	public void saveUserPreferences(HashMap userPrefs) throws GreensheetBaseException {
		
	}
	
	public HashMap getUserPrefs() {
		return userPrefs;
	}
	
	public GsUser getUser() {
		return gsUser;
	}
	
	public NciPerson getNciPerson() {
		return gsUser.getNciPerson();
	}

	private String getUserLdapLoginId() {
		return this.getNciPerson().getCommonName().toUpperCase();
	}
	
	
	public String getGrantSource() {
		// Get the default value for the source. 
		String grantSource = this.getGrantSourceDefault();
		
		// Retrieve the key name
		String grantSourceName = this.getGrantSourceName();
		
		// If key present in the user preferences, it implies user has saved this preference. Retrieve the value. 
		if (userPrefs.containsKey(grantSourceName)) {
			grantSource = (String) userPrefs.get(grantSourceName);
		}
		
		return grantSource;
	}
	
	public String getGrantType() {
		String grantType = this.getGrantTypeDefault();
		String grantTypeName = this.getGrantTypeName();
		if (userPrefs.containsKey(grantTypeName)) {
			grantType = (String) userPrefs.get(grantTypeName);
		}
		
		return grantType;
	}
	
	public String getGrantMechanism() {
		String grantMechanism = this.getGrantMechanismDefault();
		String grantMechanismName = this.getGrantMechanismName();
		if (userPrefs.containsKey(grantMechanismName)) {
			grantMechanism = (String) userPrefs.get(grantMechanismName);
		}
		
		return grantMechanism;
	}
	
	public String getGrantPayline() {
		String grantPayline = this.getGrantPaylineDefault();
		String grantPaylineName = this.getGrantPaylineName();
		if (userPrefs.containsKey(grantPaylineName)) {
			grantPayline = (String) userPrefs.get(grantPaylineName);
		}
		
		return grantPayline;
	}
	
	
	public String isGrantSourceOptionSelected(String optionValue, boolean isDropdown) {
		String retVal = isOptionSelected(getGrantSource().equalsIgnoreCase(optionValue), isDropdown);
		return retVal;
	}

	public String isGrantTypeOptionSelected(String optionValue, boolean isDropdown) {
		String retVal = isOptionSelected(getGrantType().equalsIgnoreCase(optionValue), isDropdown);
		return retVal;
	}

	public String isGrantPaylineOptionSelected(String optionValue, boolean isDropdown) {
		String retVal = isOptionSelected(getGrantPayline().equalsIgnoreCase(optionValue), isDropdown);
		return retVal;
	}

	public boolean isGrantSourcePortfolioSelected() {
		return getGrantSource().equalsIgnoreCase(getGrantSourcePortfolio());
	}

	public boolean isGrantSourceMyActivitiesSelected() {
		return getGrantSource().equalsIgnoreCase(getGrantSourceMyActivities());
	}

	public boolean isGrantSourceAllSelected() {
		return getGrantSource().equalsIgnoreCase(getGrantSourceAll());
	}
	
	
	public boolean isGrantTypeBothSelected() {
		return getGrantType().equalsIgnoreCase(getGrantTypeBoth());
	}
	
	public boolean isGrantTypeCompetingSelected() {
		return getGrantType().equalsIgnoreCase(getGrantTypeCompeting());
	}
	
	public boolean isGrantTypeNonCompetingSelected() {
		return getGrantType().equalsIgnoreCase(getGrantTypeNonCompeting());
	}
	
	
	public boolean isGrantYesSelected() {
		return getGrantPayline().equalsIgnoreCase(getGrantPaylineYes());
	}
	
	public boolean isGrantNoSelected() {
		return getGrantPayline().equalsIgnoreCase(getGrantPaylineNo());
	}
	
	
	public String getGrantMechanismDefault() {
		return grantMechanismDefault;
	}

	public String getGrantMechanismName() {
		return grantMechanismName;
	}

	public String getGrantPaylineDefault() {
		return grantPaylineDefault;
	}

	public String getGrantPaylineName() {
		return grantPaylineName;
	}

	public String getGrantPaylineNo() {
		return grantPaylineNo;
	}

	public String getGrantPaylineYes() {
		return grantPaylineYes;
	}

	public String getGrantSourceAll() {
		return grantSourceAll;
	}

	public String getGrantSourceDefault() {
		return grantSourceDefault;
	}

	public String getGrantSourceMyActivities() {
		return grantSourceMyActivities;
	}

	public String getGrantSourceName() {
		return grantSourceName;
	}

	public String getGrantSourcePortfolio() {
		return grantSourcePortfolio;
	}

	public String getGrantTypeBoth() {
		return grantTypeBoth;
	}

	public String getGrantTypeCompeting() {
		return grantTypeCompeting;
	}

	public String getGrantTypeDefault() {
		return grantTypeDefault;
	}

	public String getGrantTypeName() {
		return grantTypeName;
	}

	public String getGrantTypeNonCompeting() {
		return grantTypeNonCompeting;
	}
}
