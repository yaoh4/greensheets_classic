package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.GreensheetsUserPreferencesDAO;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class GreensheetsUserPreferencesDAOImpl extends SimpleJdbcDaoSupport implements
		GreensheetsUserPreferencesDAO {

	public Map<String, String> readUserPrefernces(String userName, String userRole) { // Check if userRole is required.
		Map<String, String> storedUserPreferences = new HashMap<String, String>();
		Map<String, String> defaultUserPreferences = new HashMap<String, String>();
		
		// Read the default preferences from the properties file.
		defaultUserPreferences = this.readDefaultUserPrefernces(userName, userRole);
		
		// Read the user's preferences stored in the Database, if any.
		storedUserPreferences = this.readStoredUserPrefernces(userName, userRole);
		
		//Now, put the preferences together. User's stored preferences take precedence over the default ones.
		// Abdul Latheef comments:
		// Need to change the existing functionality.
		// This does not provide the users an option to override the default user preferences.
		// They need to change the preferences read.
		
		// Once the user stores their own preferences, the default preferences should be ignored forever
		// unless they want to restore with the defaults, meaning they want to overwrite their stored preferences with the defaults.		
		defaultUserPreferences.putAll(storedUserPreferences);
		
		return defaultUserPreferences;		
	}

	private Map<String, String> readDefaultUserPrefernces(String userName, String userRole) {
		// Find a better place to store the default Greensheets user preferences.
		Properties defaultUserPreferences = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_USER_PREFERENCES);
		Map<String, String> userPreferences = new HashMap<String, String>();
		
		if ("PGM_DIR".equals(userRole)) {
			userPreferences.put(Constants.PREFERENCES_GRANT_SOURCE_KEY, (String) defaultUserPreferences.getProperty(Constants.PREFERENCES_GRANT_SOURCE_PD_KEY));
		} else if ("PGM_ANL".equals(userRole)) {
			userPreferences.put(Constants.PREFERENCES_GRANT_SOURCE_KEY, (String) defaultUserPreferences.getProperty(Constants.PREFERENCES_GRANT_SOURCE_PA_KEY));
		}		
		
		userPreferences.put(Constants.PREFERENCES_GRANT_TYPE_KEY, (String) defaultUserPreferences.getProperty(Constants.PREFERENCES_GRANT_TYPE_KEY));
		userPreferences.put(Constants.PREFERENCES_GRANT_MECHANISM_KEY, (String) defaultUserPreferences.getProperty(Constants.PREFERENCES_GRANT_MECHANISM_KEY));
		userPreferences.put(Constants.PREFERENCES_GRANT_PAYLINE_KEY, (String) defaultUserPreferences.getProperty(Constants.PREFERENCES_GRANT_PAYLINE_KEY));
		userPreferences.put(Constants.PREFERENCES_GRANT_NUMBER_KEY, (String) defaultUserPreferences.getProperty(Constants.PREFERENCES_GRANT_NUMBER_KEY));
		
		return userPreferences;
	}

	private Map<String, String> readStoredUserPrefernces(String userName, String userRole) {
		String appName = "GREENSHEETS"; 
		String preferencesSQL = "SELECT PREFERENCE_NAME, PREFERENCE_VALUE FROM USER_PREFERENCES_T WHERE APPLICATION_NAME = :applicationName AND USERNAME = :userName";
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("applicationName", appName);
		parameters.put("userName", userName);
		
		Map<String, String> storedUserPreferences = new HashMap<String, String>();
		
		// Read the preferences from the Database, if any.
		List<Map<String, Object>> rows = getSimpleJdbcTemplate().queryForList(preferencesSQL, parameters);
		// use queryForMap()
		
		if (rows.size() > 0) {
			for (Map row : rows) {
				storedUserPreferences.put((String)row.get("PREFERENCE_NAME"), (String)row.get("PREFERENCE_VALUE"));
			}
		}
		
		return storedUserPreferences;
	}
	
	public void storeUserPrefernces(String userName, Map<String, String>userPreferences) {
		String appName = "GREENSHEETS";
		Map<String, String> parameters = new HashMap<String, String>();
		
		String deleteSQL = "DELETE FROM USER_PREFERENCES_T " +
						   "WHERE APPLICATION_NAME = :applicationName "	+ 
						   "AND USERNAME = :userName";

		String insertSQL = "INSERT INTO USER_PREFERENCES_T (APPLICATION_NAME, USERNAME, PREFERENCE_NAME, PREFERENCE_VALUE) " +
		   "VALUES (:applicationName, :userName, :preferenceName, :preferenceValue)";
		
		parameters.put("applicationName", appName);
		parameters.put("userName", userName);	
		
		getSimpleJdbcTemplate().update(deleteSQL, parameters);
		
		Set keys = userPreferences.keySet();
		Iterator i = keys.iterator();
		
		while(i.hasNext()) {
			String key = (String) i.next();
			parameters.put("preferenceName", key);
			parameters.put("preferenceValue", userPreferences.get(key));
			
			getSimpleJdbcTemplate().update(insertSQL, parameters);
		}
	}
}
