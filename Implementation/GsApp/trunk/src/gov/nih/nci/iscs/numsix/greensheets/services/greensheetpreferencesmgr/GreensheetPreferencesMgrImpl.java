/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Production Implementation of GreensheetPreferencesMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr
 * 
 * @author kkanchinadam, Number Six Software
 */
public class GreensheetPreferencesMgrImpl implements GreensheetPreferencesMgr {

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
		setApplPrefsMap(gsUser);
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
	public Map restoreApplicationPreferences() throws GreensheetBaseException {
		return applPrefsMap;
	}

	/**
	 * savePreferences saves preferences for a user
	 * 
	 * @arg GsUser
	 */
	public void savePreferences(GsUser gsUser, Map userPrefsMap)
			throws GreensheetBaseException {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			// delete old preferences (if any)
			conn = DbConnectionHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			String userName = gsUser.getNciPerson().getCommonName()
					.toUpperCase();
			String del = "DELETE FROM USER_PREFERENCES_T WHERE APPLICATION_NAME='GREENSHEETS' AND USERNAME='"
					+ userName + "'";
			rs = stmt.executeQuery(del);
			// save new preferences
			Set keys = userPrefsMap.keySet();
			Iterator i = keys.iterator();
			while (i.hasNext()) {
				String key = (String) i.next();
				String value = (String) userPrefsMap.get(key);
				String ins = "INSERT INTO USER_PREFERENCES_T (APPLICATION_NAME, USERNAME, PREFERENCE_NAME, PREFERENCE_VALUE)"
						+ " VALUES ('GREENSHEETS', '"
						+ userName
						+ "', '"
						+ key
						+ "', '" + value + "')";
				rs = stmt.executeQuery(ins);
			}
			// commit
			conn.commit();
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

	/** setApplPrefsMap */
	private void setApplPrefsMap(GsUser user) {

		Properties prefs = (Properties) AppConfigProperties.getInstance()
				.getProperty(GreensheetsKeys.KEY_USER_PREFERENCES);

		applPrefsMap = new HashMap();

		if (user.getRole().equals(GsUserRole.PGM_DIR)) {
			addPropToPrefMap(applPrefsMap, prefs,
					Constants.PREFERENCES_GRANT_SOURCE_PD_KEY,
					Constants.PREFERENCES_GRANT_SOURCE_KEY);
		} else if (user.getRole().equals(GsUserRole.PGM_ANL)) {
			addPropToPrefMap(applPrefsMap, prefs,
					Constants.PREFERENCES_GRANT_SOURCE_PA_KEY,
					Constants.PREFERENCES_GRANT_SOURCE_KEY);
		}

		addPropToPrefMap(applPrefsMap, prefs,
				Constants.PREFERENCES_GRANT_TYPE_KEY);
		addPropToPrefMap(applPrefsMap, prefs,
				Constants.PREFERENCES_GRANT_MECHANISM_KEY);
		addPropToPrefMap(applPrefsMap, prefs,
				Constants.PREFERENCES_GRANT_PAYLINE_KEY);
		addPropToPrefMap(applPrefsMap, prefs,
				Constants.PREFERENCES_GRANT_NUMBER_KEY);
//		 Bug#4204 Abdul: Commented out this for the new fields lastName and firstName		
//		addPropToPrefMap(applPrefsMap, prefs,
//				Constants.PREFERENCES_GRANT_PI_KEY);
		addPropToPrefMap(applPrefsMap, prefs, Constants.PREFERENCES_GRANT_PI_LAST_NAME_KEY);	// Bug#4204 Abdul: Added the new field lastName
		addPropToPrefMap(applPrefsMap, prefs, Constants.PREFERENCES_GRANT_PI_FIRST_NAME_KEY);	// Bug#4204 Abdul: Added the new field firstName
	}

	/** addPropToPrefMap */
	private void addPropToPrefMap(Map map, Properties props, String fromKey) {
		try {
			String value = new String(props.getProperty(fromKey));
			map.put(fromKey, value);
		} catch (Exception e) {
			// do nothing, no value found
		}
	}

	/** addPropToPrefMap */
	private void addPropToPrefMap(Map map, Properties props, String fromKey,
			String toKey) {
		try {
			String value = new String(props.getProperty(fromKey));
			map.put(toKey, value);
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
