/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Production Implementation of GreensheetPreferencesMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr
 * 
 * @author kkanchinadam, Number Six Software
 */
public class GreensheetPreferencesMgrImpl implements GreensheetPreferencesMgr {

	// The Greensheets User object. Used in class initialization
	public GsUser gsUser;

	// User & Application Preference Maps
	public Map userPrefsMap;

	public Map applPrefsMap;

	// logger
	private static final Logger logger = Logger
			.getLogger(GreensheetPreferencesMgrImpl.class);

	/** constructor */
	public GreensheetPreferencesMgrImpl(GsUser gsUser) {
		init(gsUser);
	}

	/** init */
	private void init(GsUser gsUser) {
		setApplPrefsMap();
		setUserPrefsMap(gsUser);
	}
	
	/**
	 * restorePreferences restores preferences for a user
	 * 
	 * @arg GsUser
	 * @return applPrefsMap
	 */
	public Map restoreUserPreferences() throws GreensheetBaseException {

		// combine application preferences with any user saved preferences
		// user preferences will replace any application default preferences
		// in this operation
		applPrefsMap.putAll(userPrefsMap);

		return applPrefsMap;
	}

	/**
	 * restorePreferences restores application default preferences
	 * 
	 * @return applPrefsMap
	 */
	public Map restoreApplicationPreferences()
			throws GreensheetBaseException {
		return applPrefsMap;
	}

	/**
	 * savePreferences saves preferences for a user
	 * 
	 * @arg GsUser
	 */
	public void savePreferences(GsUser user, HashMap prefsMap)
			throws GreensheetBaseException {
		// TODO Auto-generated method stub
	}	

	/** setApplPrefsMap */
	private void setApplPrefsMap() {
		Properties prefs = (Properties) AppConfigProperties.getInstance()
				.getProperty(GreensheetsKeys.KEY_USER_PREFERENCES);
		
		applPrefsMap = new HashMap();

		addPropToPrefMap(applPrefsMap,prefs,Constants.PREFERENCES_GRANT_SOURCE_KEY);
		addPropToPrefMap(applPrefsMap,prefs,Constants.PREFERENCES_GRANT_TYPE_KEY);
		addPropToPrefMap(applPrefsMap,prefs,Constants.PREFERENCES_GRANT_MECHANISM_KEY);
		addPropToPrefMap(applPrefsMap,prefs,Constants.PREFERENCES_GRANT_PAYLINE_KEY);
		addPropToPrefMap(applPrefsMap,prefs,Constants.PREFERENCES_GRANT_NUMBER_KEY);
		addPropToPrefMap(applPrefsMap,prefs,Constants.PREFERENCES_GRANT_PI_KEY);

	}

	/** addPropToPrefMap */
	private void addPropToPrefMap(Map map, Properties props, String key) {
		try {
			String value = new String(props.getProperty(key));
			map.put(key, value);
		} catch (Exception e) {
			// do nothing, no value found
		}
	}

	/** setUserPrefsMap */
	private void setUserPrefsMap(GsUser gsUser) {
		
		userPrefsMap = new HashMap();

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = DbConnectionHelper.getInstance().getConnection();
			stmt = conn.createStatement();

			String userName = gsUser.getNciPerson().getCommonName()
					.toUpperCase();

			String sql = "SELECT PREFERENCE_NAME, PREFERENCE_VALUE FROM "
					+ "USER_PREFERENCES_T WHERE APPLICATION_NAME='GREENSHEETS' AND USERNAME='"
					+ userName + "'";

			logger.debug("Retrieving user preferences, sql is =" + sql);

			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String prefName = rs.getString("PREFERENCE_NAME");
				String prefValue = rs.getString("PREFERENCE_VALUE");
				userPrefsMap.put(prefName, prefValue);
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
		} finally {
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
			} catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}

	}

}
