package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsUserPreferencesServices;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class EditProgramPreferencesAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(EditProgramPreferencesAction.class);

    static GreensheetsUserPreferencesServices greensheetsUserPreferencesServices;

    public GreensheetsUserPreferencesServices getGreensheetsUserPreferencesServices() {
        return greensheetsUserPreferencesServices;
    }

    public void setGreensheetsUserPreferencesServices(
            GreensheetsUserPreferencesServices greensheetsUserPreferencesServices) {
        this.greensheetsUserPreferencesServices = greensheetsUserPreferencesServices;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        ProgramPreferencesForm prefsForm = (ProgramPreferencesForm) form;

        GreensheetUserSession gus = GreensheetActionHelper
                .getGreensheetUserSession(req);

        // restoreProgramPreferencesForm(gus.getUser(), prefsForm);
        readUserPreferences(gus.getUser(), prefsForm);

        // set all LOVs in Form
        setProgramPreferencesFormLOVs(req);

        return mapping.findForward(Constants.SUCCESS_KEY);
    }

    /**
     * setProgramPreferencesFormLOVs
     * 
     * @throws Exception
     */
    private void setProgramPreferencesFormLOVs(HttpServletRequest req)
            throws Exception {
        setGrantsSourcesLOV(req);
        setGrantTypesLOV(req);
    }

    /**
     * setGrantsSourcesLOV
     * 
     * @throws Exception
     */
    private void setGrantsSourcesLOV(HttpServletRequest req) throws Exception {
        // Bug#4157 Abdul: Collect the user's cancer activities.
        // get the user session
        GreensheetUserSession gus = GreensheetActionHelper
                .getGreensheetUserSession(req);
        GsUser loggedOnUser = gus.getUser();
        List cancerActivities = loggedOnUser.getCancerActivities();
        int activityCount = 0;
        StringBuffer userCancerActivities = new StringBuffer("");

        if (cancerActivities != null) {
            activityCount = cancerActivities.size();
            if (activityCount > 0) {
                userCancerActivities.append(" ("
                        + (String) cancerActivities.get(0));
                for (int index = 1; index < activityCount; index++) {
                    userCancerActivities.append(", ").append(
                            cancerActivities.get(index));
                }
                userCancerActivities.append(") ");
            }
        }

        Collection<LabelValueBean> grantSources = new ArrayList<LabelValueBean>();
        grantSources.add(new LabelValueBean("My Portfolio",
                Constants.PREFERENCES_MYPORTFOLIO));
        // Bug#4157 Abdul: Append the user's cancer activities to the choice 'My
        // Cancer Activity'.
        // grantSources.add(new LabelValueBean("My Cancer Activity",
        // Constants.PREFERENCES_MYCANCERACTIVITY));
        grantSources
                .add(new LabelValueBean("My Cancer Activity"
                        + userCancerActivities,
                        Constants.PREFERENCES_MYCANCERACTIVITY));
        grantSources.add(new LabelValueBean("All NCI Grants",
                Constants.PREFERENCES_ALLNCIGRANTS));

        req.setAttribute(Constants.GRANT_SOURCES_KEY, grantSources);
    }

    /** setGrantTypesLOV */
    private void setGrantTypesLOV(HttpServletRequest req) {
        Collection<LabelValueBean> grantTypes = new ArrayList<LabelValueBean>();
        grantTypes.add(new LabelValueBean("Competing Grants",
                Constants.PREFERENCES_COMPETINGGRANTS));
        grantTypes.add(new LabelValueBean("Non-Competing Grants",
                Constants.PREFERENCES_NONCOMPETINGGRANTS));
        grantTypes.add(new LabelValueBean(
                "Both Competing and Non-Competing Grants",
                Constants.PREFERENCES_BOTH));
        req.setAttribute(Constants.GRANT_TYPES_KEY, grantTypes);
    }

    // Abdul Latheef: Commented out the following method
    /** restoreProgramPreferencesForm */
    // private ProgramPreferencesForm restoreProgramPreferencesForm(GsUser
    // gsUser, ProgramPreferencesForm prefs) throws Exception {
    //
    // GreensheetPreferencesMgr pm = (GreensheetPreferencesMgr) new
    // GreensheetPreferencesMgrImpl(gsUser);
    //
    // Map map = pm.restoreUserPreferences();
    // prefs.grantSource = (String) map
    // .get(Constants.PREFERENCES_GRANT_SOURCE_KEY);
    // prefs.grantType = (String) map
    // .get(Constants.PREFERENCES_GRANT_TYPE_KEY);
    // prefs.mechanism = (String) map
    // .get(Constants.PREFERENCES_GRANT_MECHANISM_KEY);
    // prefs.onlyGrantsWithinPayline = (String) map
    // .get(Constants.PREFERENCES_GRANT_PAYLINE_KEY);
    //
    // return prefs;
    // }

    private void readUserPreferences(GsUser gsUser,
            ProgramPreferencesForm prefsForm) throws Exception {
        // ApplicationContext context = new
        // ClassPathXmlApplicationContext("applicationContext.xml");
        // GreensheetsUserPreferencesServices greensheetsUserPreferencesServices
        // = (GreensheetsUserPreferencesServices)
        // context.getBean("greensheetsUserPreferencesServices");
        Map<String, String> userPreferences;

        if (greensheetsUserPreferencesServices != null) {
            logger.info("@@ greensheetsUserPreferencesServices is NOT null");
        } else {
            logger.info("@@ greensheetsUserPreferencesServices is null ");
        }

        userPreferences = greensheetsUserPreferencesServices
                .readUserPrefernces(gsUser.getUserName(),
                        gsUser.getRoleAsString());

        prefsForm.grantSource = userPreferences
                .get(Constants.PREFERENCES_GRANT_SOURCE_KEY);
        prefsForm.grantType = userPreferences
                .get(Constants.PREFERENCES_GRANT_TYPE_KEY);
        prefsForm.mechanism = userPreferences
                .get(Constants.PREFERENCES_GRANT_MECHANISM_KEY);
        prefsForm.onlyGrantsWithinPayline = userPreferences
                .get(Constants.PREFERENCES_GRANT_PAYLINE_KEY);
    }

}
