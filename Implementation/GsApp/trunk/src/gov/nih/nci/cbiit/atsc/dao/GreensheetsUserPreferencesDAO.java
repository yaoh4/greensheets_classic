package gov.nih.nci.cbiit.atsc.dao;

import java.util.Map;

public interface GreensheetsUserPreferencesDAO {
	public Map<String, String> readUserPrefernces(String userName, String userRole);
	public void storeUserPrefernces(String userName, Map<String, String>userPreferences);
}
