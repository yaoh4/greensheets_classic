package gov.nih.nci.iscs.numsix.greensheets.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import gov.nih.nci.cbiit.atsc.dao.GrantDAO;
import gov.nih.nci.cbiit.atsc.dao.GreensheetFormDAO;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class GreensheetsFormGrantsServiceImpl implements GreensheetsFormGrantsService {

    private static final Logger logger = Logger.getLogger(GreensheetsFormGrantsServiceImpl.class);

    private GreensheetsMiscServices greensheetsMiscServices = null;
    private GrantDAO grantDAO;
    private GreensheetFormDAO greensheetFormDAO;

    public void setGreensheetFormDAO(GreensheetFormDAO greensheetFormDAO) {
        this.greensheetFormDAO = greensheetFormDAO;
    }

    public void setGrantDAO(GrantDAO grantDAO) {
        this.grantDAO = grantDAO;
    }

    public GreensheetsMiscServices getGreensheetsMiscServices() {
        return greensheetsMiscServices;
    }

    public void setGreensheetsMiscServices(
            GreensheetsMiscServices greensheetsMiscServices) {
        this.greensheetsMiscServices = greensheetsMiscServices;
    }

    public List findGrants(GsUser user, boolean onControlFlag,
            boolean restrictedToOpenForms, boolean restrictedToLoggedinUser, boolean filterCancelled) {
        
    	String nciOracleId = user.getOracleId();
    	
    	ArrayList<Calendar> budgetDates = this.determineBudgetDates(user);

        List formGrants = grantDAO.findGrants(nciOracleId, budgetDates.get(0),
                budgetDates.get(1), onControlFlag, restrictedToOpenForms,
                restrictedToLoggedinUser,filterCancelled);

        return formGrants;
    }

    public List findGrants(GsUser user, String grantsRange, String grantType,
            boolean includeOnlyGrantsWithinPayline, String grantMechanism,
            String fullGrantNum, String piLastName, String piFirstName, boolean filterCancelled) {

        String applStatusGroupCode = "A"; // Default
        boolean minoritySupplIncluded = false;
        List userPortfolioIds = null;
        List userCancerActivities = null;

        // Check if the minority flag has to be checked based on logged in
        // user's nciOracleID.
        Properties p = (Properties) AppConfigProperties.getInstance()
                .getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        String minoritySupplPgmmUsers = p
                .getProperty("minoritysupplements.userids"); // Minority
        // Supplements
        // Program Users
        if (StringUtils.contains(minoritySupplPgmmUsers, user.getOracleId())) {
            minoritySupplIncluded = true;
        }

        // Determine the range of grants - User's Portfolio (myportfolio),
        // User's Cancer Activities (mycanceractivity), or All NCI Grants
        // (allncigrants).
        if (grantsRange != null && !grantsRange.trim().equals("")) {
            if (grantsRange.trim().equalsIgnoreCase(
                    Constants.PREFERENCES_MYPORTFOLIO)) {
                userPortfolioIds = user.getMyPortfolioIds();
            } else if (grantsRange.trim().equalsIgnoreCase(
                    Constants.PREFERENCES_MYCANCERACTIVITY)) {
                userCancerActivities = user.getCancerActivities();
            }
        }
        boolean restrictedToOpenForms = true; // Restrict the forms that are
        // open.

        ArrayList<Calendar> budgetDates = this.determineBudgetDates(user);

        List formGrants = grantDAO.findGrants(budgetDates.get(0),
                budgetDates.get(1), restrictedToOpenForms, applStatusGroupCode,
                minoritySupplIncluded, userPortfolioIds, userCancerActivities,
                grantType, includeOnlyGrantsWithinPayline, grantMechanism,
                fullGrantNum, piLastName, piFirstName, filterCancelled);

        return formGrants;
    }

    public List findGrantsByGrantNum(String fullGrantNum) {
        List formGrants = grantDAO.findGrantsByGrantNum(fullGrantNum);

        return formGrants;
    }
    
    public List retrieveGrantsByFullGrantNum(String fullGrantNum) {
        List formGrants = grantDAO.retrieveGrantsByFullGrantNum(fullGrantNum);

        return formGrants;
    }

    public List findGrantsByPiLastName(String piLastName, String adminPhsOrgCode) {
        List formGrants = grantDAO.findGrantsByPiLastName(piLastName,
                adminPhsOrgCode);

        return formGrants;
    }

    public List findGrantsByApplId(long applId) {
        List formGrants = grantDAO.findGrantsByApplId(applId);

        return formGrants;
    }
    
    public FormGrantProxy findGSGrantInfo(Long actionId, Long applId, String grantNum, GsUser userId) {
    	FormGrantProxy formGrantProxy = grantDAO.findGSGrantInfo(actionId, applId, grantNum, userId);

        return formGrantProxy;
   }

    private ArrayList<Calendar> determineBudgetDates(GsUser user) {
    	
    	String currentFYtoAssume = "";
    	if (user!=null) {
    		Map<String, String> userPreferences = user.getUserPreferences();
    		if (userPreferences!=null) {
    			currentFYtoAssume = userPreferences.get("FY_TO_ASSUME");
    		}
    	}
    	if (currentFYtoAssume==null)  { currentFYtoAssume = ""; }
    	
        Calendar currentTimestamp = Calendar.getInstance();

        Calendar startDate = Calendar.getInstance(), endDate = Calendar
                .getInstance();
        ArrayList<Calendar> datesRange = new ArrayList<Calendar>();

        currentTimestamp.setTime((Date) greensheetsMiscServices
                .getCurrentTimsestamp());
        int currentYear = currentTimestamp.get(Calendar.YEAR);
        
        /* The overriding of system-date-based current FY with what the testing user may 
         * have specified instead:  */
        if (!"".equals(currentFYtoAssume)) {
        	currentYear = Integer.parseInt(currentFYtoAssume);
        	currentTimestamp.set(GregorianCalendar.YEAR, currentYear);
        }
        
        int currentMonth = currentTimestamp.get(Calendar.MONTH);
        int fiscalStartYear = -99, fiscalEndYear = -99; // Should be set to a
        // valid values.

        Properties appProperties = (Properties) AppConfigProperties
                .getInstance().getProperty(
                        GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        Calendar cutOverTimestamp = Calendar.getInstance();
        String fiscalYearCutOverDate = appProperties
                .getProperty("fiscalYearCutOverDate");
        boolean fiscalYearCutOverDateOverrided = false;
        int cutOverDate = 8;
        int cutOverMonth = 10;

        if (fiscalYearCutOverDate == null
                || "".equals(fiscalYearCutOverDate.trim())) {
            // Continue with the current procedure.
            fiscalYearCutOverDateOverrided = false;
        } else {
            // Interpret the overrided date in the MM-DD format.
            fiscalYearCutOverDate = fiscalYearCutOverDate.trim();
            int dashPosition = fiscalYearCutOverDate.indexOf("-");
            if (dashPosition > -1) {
                String tmpCutOverDate = fiscalYearCutOverDate
                        .substring(dashPosition + 1);
                String tmpCutOverMonth = fiscalYearCutOverDate.substring(0,
                        dashPosition);

                try {
                    cutOverDate = Integer.parseInt(tmpCutOverDate);
                    cutOverMonth = Integer.parseInt(tmpCutOverMonth);
                    cutOverTimestamp.set(currentYear, cutOverMonth - 1,
                            cutOverDate); // if this goes thru', the numbers set
                    // in the properties file are Ok.

                    int dateComparison = currentTimestamp.getTime().compareTo(
                            cutOverTimestamp.getTime());

                    if (dateComparison < 0) { // Current date is before the
                        // cutoff date
                        fiscalStartYear = currentYear - 1;
                        fiscalEndYear = currentYear;
                    } else {
                        fiscalStartYear = currentYear;
                        fiscalEndYear = currentYear + 1;
                    }
                    fiscalYearCutOverDateOverrided = true;
                } catch (NumberFormatException nfe) {
                    fiscalYearCutOverDateOverrided = false;
                }
            }
        }

        if (!fiscalYearCutOverDateOverrided) {
            if (currentMonth > 9) {
                fiscalStartYear = currentYear;
                fiscalEndYear = currentYear + 1;
            } else {
                fiscalStartYear = currentYear - 1;
                fiscalEndYear = currentYear;
            }
        }

        startDate.set(fiscalStartYear, cutOverMonth-1, cutOverDate); // This used to ignore that Java months 
        endDate.set(fiscalEndYear, cutOverMonth-1, cutOverDate - 1); // are 0-based, and cause bugs

        datesRange.add(startDate);
        datesRange.add(endDate);

        return datesRange;
    }
    
    public boolean checkActionStatusByApplId(String applId) throws GreensheetBaseException{
        return greensheetFormDAO.checkActionStatusByApplId(applId);
    }
    
    public boolean checkActionStatusByGrantId(String grantId) throws GreensheetBaseException{
        
        return greensheetFormDAO.checkActionStatusByGrantId(grantId);
        
    }

    /**
     * GREENSHEET-495
     * 
     * Is a given APPL_ID a Type 6 Grant with Award Type 1, 2, 4, 5,8 9 in same FY?
     */
	public boolean isValidGrantType(String applId, StringBuffer gn) throws GreensheetBaseException{
		return greensheetFormDAO.isValidGrantType(applId, gn);
	}
	

}
