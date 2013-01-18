package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.cbiit.atsc.dao.GreensheetsUserPreferencesDAO;

import java.util.Map;

public class GreensheetsUserPreferencesServicesImpl implements
        GreensheetsUserPreferencesServices {
    private GreensheetsUserPreferencesDAO greensheetsUserPreferencesDAO;

    public GreensheetsUserPreferencesDAO getGreensheetsUserPreferencesDAO() {
        return greensheetsUserPreferencesDAO;
    }

    public void setGreensheetsUserPreferencesDAO(
            GreensheetsUserPreferencesDAO greensheetsUserPreferencesDAO) {
        this.greensheetsUserPreferencesDAO = greensheetsUserPreferencesDAO;
    }

    public Map<String, String> readUserPrefernces(String userName,
            String userRole) {
        return greensheetsUserPreferencesDAO.readUserPrefernces(userName,
                userRole);
    }

    public void storeUserPrefernces(String userName,
            Map<String, String> userPreferences) {
        greensheetsUserPreferencesDAO.storeUserPrefernces(userName,
                userPreferences);
    }
}
