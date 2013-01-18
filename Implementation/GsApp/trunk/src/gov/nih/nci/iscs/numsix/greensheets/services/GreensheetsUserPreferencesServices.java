package gov.nih.nci.iscs.numsix.greensheets.services;

import java.util.Map;

public interface GreensheetsUserPreferencesServices {
	public Map<String, String> readUserPrefernces(String userName, String userRole); 
	public void storeUserPrefernces(String userName, Map<String, String>userPreferences);
}
