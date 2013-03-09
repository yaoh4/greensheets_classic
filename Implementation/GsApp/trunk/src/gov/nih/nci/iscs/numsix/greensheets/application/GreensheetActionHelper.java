/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsUserPreferencesServices;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsUserServices;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.DMChecklistUserPermission;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Utility methods used by the action classes.
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetActionHelper {

    private static final Logger logger = Logger
            .getLogger(GreensheetActionHelper.class);

    /**
     * Constructor for GreensheetActionHelper.
     */
    private GreensheetActionHelper() {
    }

    public static void setPaylineOption(HttpServletRequest req,
            GreensheetUserSession gus) {

        // Check if the payLineOnly option has been flagged and caused this
        // request
        String changePayLineOption = req.getParameter("paylineOpt");

        if (gus.isNewSession()) {
            gus.setPaylineOnly(true);
        } else if (changePayLineOption != null
                && changePayLineOption.equalsIgnoreCase("YES")) {
            gus.setPaylineOnly(true);
        } else if (changePayLineOption != null
                && changePayLineOption.equalsIgnoreCase("NO")) {

            gus.setPaylineOnly(false);
        }

        if (gus.isPaylineOnly()) {
            req.setAttribute("payLineOnlyChecked", "checked='true'");
        } else {
            req.setAttribute("payLineOnlyChecked", "");
        }

    }

    public static void setMyPortfolioOption(HttpServletRequest req,
            GreensheetUserSession gus) {

        // Check if the myportfolio option has been flagged and caused this
        // request
        String changeMyPortFolioOption = req.getParameter("myPortfolio");

        if (gus.isNewSession()) {
            gus.setMyPortfolio(false);
        } else if (changeMyPortFolioOption != null
                && changeMyPortFolioOption.equalsIgnoreCase("YES")) {
            gus.setMyPortfolio(true);
        } else if (changeMyPortFolioOption != null
                && changeMyPortFolioOption.equalsIgnoreCase("NO")) {
            gus.setMyPortfolio(false);
        }

        if (gus.isMyPortfolio()) {
            req.setAttribute("myPortfolioChecked", "checked='true'");
        } else {
            req.setAttribute("myPortfolioChecked", "");
        }
    }

    /**
     * Method getGreensheetUserSession. Returns a GreensheetUserSession if one exists or creates a new one if it dosen't
     * 
     * @param req
     * @return GreensheetUserSession
     * @throws GreensheetBaseException
     */
    public static GreensheetUserSession getGreensheetUserSession(
            HttpServletRequest req) throws GreensheetBaseException {

        GreensheetUserSession gus = (GreensheetUserSession) req.getSession()
                .getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
        // String remoteUserName = req.getRemoteUser();
        String remoteUserName = null;

        /** FY to assume for retrieving grants list in DEV and TEST environments with
         *  old data - change-able via UI only by super-users and stored in GreensheetUserSessions */
        String currentFYtoAssume = "";
        if (gus!=null) {
        	currentFYtoAssume = gus.getUser().getUserPreferences().get("FY_TO_ASSUME");
        	if (currentFYtoAssume==null)  { currentFYtoAssume = ""; }
        }
        
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        GreensheetsUserServices greensheetsUserServices = (GreensheetsUserServices) context
                .getBean("greensheetsUserServices");

        String newUserName = (String) req.getAttribute(GreensheetsKeys.NEW_USER_ID);

        if (req.getSession().getAttribute(GreensheetsKeys.ACTUAL_USER_SESSION) == null) {
            req.getSession().setAttribute(GreensheetsKeys.ACTUAL_USER_SESSION,(GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION));
        }

        if (newUserName != null) {
            boolean isSuperUser = ((GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.ACTUAL_USER_SESSION)).getUser()
                    .getNciPerson().isSuperUser();
            if (isSuperUser) {
                logger.debug("The "
                        + ((GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.ACTUAL_USER_SESSION)).getUser().getNciPerson()
                                .getFullName() + " is Supert User.");
                if (greensheetsUserServices.getUserByUserName(newUserName) != null) {
                    if (gus != null) {
                        if (gus.getUser() != null)
                            logger.debug("User " + gus.getUser().getUserName() + " changed login to " + newUserName.trim());
                    }
                    remoteUserName = newUserName.trim();
                    gus = null;
                } else {
                    return null;
                }
            }
        } else {

            remoteUserName = req.getHeader("SM_USER"); // SM_USER is the header that will start being set by SiteMinder / iTrust in 2011: Jira issue 319
            if (StringUtils.isNotEmpty(remoteUserName)) {
                logger.debug("The value of http request header SM_USER, which is " + remoteUserName + ", will be used to determine who the user is.");
            }
            else {
                remoteUserName = req.getRemoteUser();
                logger.debug("HTTP request header SM_USER does not appear to be set, so traditional retRemoteUser() "
                        + "will be used to determine who the user is - and it is \n\t" + remoteUserName);
            } // end of implementing Jira

            if (StringUtils.isEmpty(remoteUserName)) {

                String authUser = req.getHeader("Authorization");

                if (StringUtils.isNotBlank(authUser)) {
                    // BASE64Decoder decoder = new BASE64Decoder();
                    // authUser = new String(decoder.decodeBuffer(authUser.substring(6)));
                    authUser = new String(Base64.decodeBase64(authUser.substring(6)));
                    remoteUserName = authUser.substring(0, authUser.indexOf(":"));

                    logger.debug("The value of http request header Authorization, which is " + remoteUserName
                            + ", will be used to determine who the user is.");
                }

            }

        }

        req.setAttribute(GreensheetsKeys.NEW_USER_ID, null);

        String userId = req.getParameter(GreensheetsKeys.KEY_USER_ID);
        String testUser = req.getParameter(GreensheetsKeys.KEY_TEST_USER);
        // GreensheetUserMgr gsUserMgr = null; //Abdul Latheef
        GsUser user = null;
        String loggedInUserID = null;

        // Abdul: This needs to be redone.
        if (testUser != null) {

            logger.debug("USING TEST USER " + testUser);

            // gsUserMgr = GreensheetMgrFactory
            // .createGreensheetUserMgr(GreensheetMgrFactory.TEST);
            userId = testUser;
        } else {
            // gsUserMgr = GreensheetMgrFactory
            // .createGreensheetUserMgr(GreensheetMgrFactory.PROD);
        }

        if (gus == null) {

            if (remoteUserName != null) {
                // user = gsUserMgr.findUserByUserName(remoteUserName);
                loggedInUserID = remoteUserName;
                user = greensheetsUserServices
                        .getUserByUserName(remoteUserName);
            } else if (userId != null) {
                // user = gsUserMgr.findUserByUserName(userId);
                loggedInUserID = userId;
                user = greensheetsUserServices.getUserByUserName(userId);
            } else {
                throw new GreensheetBaseException("error.userid");
            }

            // Abdul: Read the user preferences.
            // Abdul: Need to integrate the user preferences functionality into
            // the GreensheetsUserServices.

            // user must be a valid object by now.

            if (user.getRole().equals(GsUserRole.PGM_DIR)
                    || user.getRole().equals(GsUserRole.PGM_ANL)) {
                GreensheetsUserPreferencesServices greensheetsUserPreferencesServices = (GreensheetsUserPreferencesServices) context
                        .getBean("greensheetsUserPreferencesServices");
                Map<String, String> userPreferences;
                userPreferences = greensheetsUserPreferencesServices
                        .readUserPrefernces(user.getUserName(),
                                user.getRoleAsString());
                user.setUserPreferences(userPreferences);
            }
            Map<String, String> userPreferences = user.getUserPreferences();
            if (userPreferences==null) {
            	userPreferences = new HashMap<String, String>();
            }
            if (currentFYtoAssume!=null && !"".equals(currentFYtoAssume)) {
            	userPreferences.put("FY_TO_ASSUME", currentFYtoAssume);
            	// ^ Only super-users can change the 'current FY' to something other than the actual FY.
            	// The above ensures that even after super-user performed the "change user" action and 
            	// started acting as a regular, non-super, user, the change of FY will still be in effect. 
            }
            user.setUserPreferences(userPreferences);

            if (loggedInUserID.indexOf(",") > -1) {
                loggedInUserID = loggedInUserID.substring(0,
                        loggedInUserID.indexOf(",")); // Returns a new string
                                                      // that is a substring
                                                      // of this string. The
                                                      // substring begins at
                                                      // the specified
                                                      // beginIndex and
                                                      // extends to the
                                                      // character at index
                                                      // endIndex - 1.
                // user.setUserID(loggedInUserID.toUpperCase()); // Play the
                // same dirty game (of using the uppercase letters) as the
                // predecessor.
            }

            // GreensheetsUserPreferencesDAO greensheetsUserPreferencesManager =
            // (GreensheetsUserPreferencesDAO)
            // context.getBean("greensheetsUserPreferencesDAO");
            // user.setUserPreferences(greensheetsUserPreferencesManager.readUserPrefernces(user.getUserID(),
            // user.getRoleAsString())); // enum is a feature available in v5
            // and later.

            gus = new GreensheetUserSession(user);

            logger.debug("Set Current User Session");

            req.getSession().setAttribute(
                    GreensheetsKeys.KEY_CURRENT_USER_SESSION, gus);

        } else {
            gus.setNewSession(false);

        }
        return gus;
    }

    /**
     * Method getGrantGreensheetProxyList. Returns a list of GrantGreensheetProxy objects. These are used for display of the users grants list table.
     * 
     * @param gMap
     * @param oracleId
     * @return List
     */
    public static List getGrantGreensheetProxyList(Map gMap, GsUser user) {

        Set entries = gMap.entrySet();
        List list = new ArrayList();

        Iterator iter = entries.iterator();
        while (iter.hasNext()) {
            Map.Entry me = (Map.Entry) iter.next();
            // GsGrant g = (GsGrant) me.getValue(); //Abdul Latheef: Used new
            // FormGrantProxy instead of the old GsGrant.
            FormGrantProxy g = (FormGrantProxy) me.getValue();
            // GrantGreensheetProxy gp = new GrantGreensheetProxy(g, user);
            // //Abdul Latheef: Used new FormGrantProxy instead of the old
            // GsGrant.
            // list.add(gp);
            list.add(g);
        }

        return list;

    }

    /**
     * Method setFormDisplayInfo. This method sets various display properties for the greensheet. These display properties are used by the Velocity
     * template engine.
     * 
     * @param req
     * @param formUid
     * @throws Exception
     */
    public static void setFormDisplayInfo(HttpServletRequest req, String formUid)
            throws Exception {

        GreensheetUserSession gus = (GreensheetUserSession) req.getSession()
                .getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);

        GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);
        GsUser user = gus.getUser();
        if (gfs == null) {
            throw new GreensheetBaseException("sessionTimeOut");
        }
        GreensheetFormProxy form = gfs.getForm();
        // GsGrant grant = gfs.getGrant(); //Abdul Latheef: Used new FormGrant
        // instead of the old GsGrant.
        FormGrantProxy grant = gfs.getGrant();

        GreensheetGroupType type = form.getGroupType();
        GsUserRole role = user.getRole();

        String grantDetailUrl = ((Properties) AppConfigProperties.getInstance()
                .getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES))
                .getProperty("url.grantdetailviewer");

        boolean displaySubmit = false;
        boolean displaySave = false;
        boolean displayRmcLetterOption = false;
        boolean displayGmsAndPdCode = false;
        boolean displayPoc = false;
        boolean disablePoc = false;
        boolean disabled = true;
        String displayTypeString = null;
        boolean displayElectronicSubmissionFlag = false;
        DMChecklistUserPermission dmChecklistUserPermissions = null;

        setQuestionResponsesInRequest(form, req);

        // Set if the greensheet questions are disabled
        switch (role.getValue()) {
        case GsUserRole.PGM_ANL_VALUE:
            if (type.equals(GreensheetGroupType.PGM)
                    && !form.getStatus().equals(GreensheetStatus.SUBMITTED)
                    && !form.getStatus().equals(GreensheetStatus.FROZEN)
                    && !restrictByCancerActivities(user, grant)) {

                disabled = false;
            }
            break;
        case GsUserRole.PGM_DIR_VALUE:
            if (type.equals(GreensheetGroupType.PGM)
                    && !form.getStatus().equals(GreensheetStatus.SUBMITTED)
                    && !form.getStatus().equals(GreensheetStatus.FROZEN)
                    && !restrictByCancerActivities(user, grant)) {

                disabled = false;
            }
            break;

        case GsUserRole.SPEC_VALUE:
            if (type.equals(GreensheetGroupType.SPEC)
                    && !form.getStatus().equals(GreensheetStatus.SUBMITTED)
                    && !form.getStatus().equals(GreensheetStatus.FROZEN)) {

                disabled = false;
            }
            break;
        case GsUserRole.GS_GUEST_VALUE:
            disabled = true;
            break;
        }

        // Set the greensheet type string
        if (type.equals(GreensheetGroupType.PGM)) {
            displayTypeString = "PROGRAM";
        } else if (type.equals(GreensheetGroupType.SPEC)) {
            displayTypeString = "SPECIALIST";
        } else if (type.equals(GreensheetGroupType.DM)) { // Abdul Latheef (for
                                                          // GPMATS) : Expand
                                                          // the check for the
                                                          // DM Check list
            displayTypeString = "DOCUMENT MANAGEMENT";
        }

        // Set the submit and save button
        if (!disabled) {

            if (type.equals(GreensheetGroupType.PGM)
                    && user.getRole().equals(GsUserRole.PGM_DIR)) {
                displaySave = true;
                if (grant.isGrantOnControl()
                        && !restrictByCancerActivities(user, grant)) {
                    displaySubmit = true;
                }
            }
            if (type.equals(GreensheetGroupType.PGM)
                    && user.getRole().equals(GsUserRole.PGM_ANL)) {
                displaySave = true;
            }
            if (type.equals(GreensheetGroupType.SPEC)
                    && user.getRole().equals(GsUserRole.SPEC)) {
                displaySave = true;
                if (grant.isGrantOnControl()) {
                    displaySubmit = true;
                }
            }
        }

        // Abdul Latheef: Display the Save, Submit buttons appropriately for the
        // DM Check list. The user could be accessing the DM Check list
        // externally, which is totally controlled by the request parameters,
        // not the role
        // information retrieved from the Database.
        // switch (role.getValue()) {
        // case GsUserRole.GS_GUEST_VALUE:
        if (type.equals(GreensheetGroupType.DM)) { // Save the URL parameters in
                                                   // user's session if it is
                                                   // DM Checklist call
            dmChecklistUserPermissions = user.getDmChecklistUserPermissions(
                    gus, grant.getApplID(), grant.getFullGrantNumber()); // Abdul
                                                                         // Latheef:
                                                                         // getApplID()
                                                                         // is
                                                                         // used
                                                                         // instead
                                                                         // of
                                                                         // getApplId(),
                                                                         // for
                                                                         // time
                                                                         // being.
            if (dmChecklistUserPermissions == null) {
                dmChecklistUserPermissions = new DMChecklistUserPermission();
                dmChecklistUserPermissions.setApplId(grant.getApplID()); // Abdul
                                                                         // Latheef:
                                                                         // getApplID()
                                                                         // is
                                                                         // used
                                                                         // instead
                                                                         // of
                                                                         // getApplId(),
                                                                         // for
                                                                         // time
                                                                         // being.
                dmChecklistUserPermissions.setFullGrantNumber(grant
                        .getFullGrantNumber());
            }

            // See if the user can save the DM Check list
            // The request parameters exist when the user invokes the DM
            // Checklist externally.
            // They are lost when the first HTTP request completes and the DM
            // Checklist form/greensheet is returned after the save.
            String editableReqParam = req
                    .getParameter(GreensheetsKeys.KEY_EDITABLE);

            if (editableReqParam != null) {
                if (editableReqParam.trim().equalsIgnoreCase("Y")) {
                    if (!form.getStatus().equals(GreensheetStatus.SUBMITTED)
                            && !form.getStatus()
                                    .equals(GreensheetStatus.FROZEN)) { // Actually,
                                                                        // DM
                                                                        // Checklist
                                                                        // should
                                                                        // not
                                                                        // have
                                                                        // FROZEN
                                                                        // status.
                        displaySave = true;
                        disabled = false; // Because disabled is true by default
                    }
                }
                if ((editableReqParam.trim().equalsIgnoreCase("Y"))
                        || (editableReqParam.trim().equalsIgnoreCase("N"))) {
                    dmChecklistUserPermissions.setEditable(editableReqParam
                            .trim().toUpperCase());
                }
            } else {
                if (dmChecklistUserPermissions != null) {
                    String editableSessionVar = dmChecklistUserPermissions
                            .getEditable();
                    if ((editableSessionVar != null)
                            && (editableSessionVar.equalsIgnoreCase("Y"))) {
                        if (!form.getStatus()
                                .equals(GreensheetStatus.SUBMITTED)
                                && !form.getStatus().equals(
                                        GreensheetStatus.FROZEN)) {
                            displaySave = true;
                            disabled = false; // Because disabled is true by
                                              // default
                        }
                    } // By default, the save button is hidden and the form is
                      // frozen. Test it anyway.
                }
            }

            // See if the user can submit the DM Check list
            String submittableReqParam = req
                    .getParameter(GreensheetsKeys.KEY_SUBMITTABLE);

            if (submittableReqParam != null) {
                if (submittableReqParam.trim().equalsIgnoreCase("Y")) {
                    if (!form.getStatus().equals(GreensheetStatus.SUBMITTED)) {
                        displaySubmit = true;
                        disabled = false; // Because disabled is true by default
                    }
                }
                if ((submittableReqParam.trim().equalsIgnoreCase("Y"))
                        || (submittableReqParam.trim().equalsIgnoreCase("N"))) {
                    dmChecklistUserPermissions
                            .setSubmittable(submittableReqParam.trim()
                                    .toUpperCase());
                }
            } else {
                if (dmChecklistUserPermissions != null) {
                    String submittableSessionVar = dmChecklistUserPermissions
                            .getSubmittable();
                    if ((submittableSessionVar != null)
                            && (submittableSessionVar.equalsIgnoreCase("Y"))) {
                        if (!form.getStatus()
                                .equals(GreensheetStatus.SUBMITTED)
                                && !form.getStatus().equals(
                                        GreensheetStatus.FROZEN)) {
                            displaySubmit = true;
                            disabled = false; // Because disabled is true by
                                              // default
                        }
                    } // By default, the submit button is hidden and the form is
                      // frozen. Test it anyway.
                }
            }
            user.setDmChecklistUserPermissions(gus, dmChecklistUserPermissions);
        }
        // break;
        // }

        // Set if the Point of contact is displayed
        if (form.getGroupType().equals(GreensheetGroupType.PGM)) {
            displayPoc = true;
            if (disabled) {
                disablePoc = true;
            }
        }

        if (form.getGroupType().equals(GreensheetGroupType.SPEC)) {
            displayGmsAndPdCode = true;
        }

        // Abdul Latheef (for GPMATS). Set the flag to indicate if the
        // application was electronically submitted.
        if (form.getGroupType().equals(GreensheetGroupType.DM)) {
            displayElectronicSubmissionFlag = true;
        }

        user.setCanEdit(disabled);

        req.setAttribute("grantDetailUrl", grantDetailUrl);
        req.setAttribute("displaySubmit", new Boolean(displaySubmit));
        req.setAttribute("displaySave", new Boolean(displaySave));
        req.setAttribute("displayRmcLetterOption", new Boolean(
                displayRmcLetterOption));
        req.setAttribute("displayGmsAndPdCode",
                new Boolean(displayGmsAndPdCode));
        req.setAttribute("displayPoc", new Boolean(displayPoc));
        req.setAttribute("displayElectronicSubmissionFlag", new Boolean(
                displayElectronicSubmissionFlag)); // Abdul Latheef (for GPMATS)

        if (disablePoc) {

            req.setAttribute("disablePoc", "disabled");
        }

        req.setAttribute("gsType", displayTypeString);

        if (disabled) {
            user.setCanEdit(false);
            req.setAttribute("disabled", "disabled");
        } else {
            user.setCanEdit(true);
        }

    }

    // private static boolean restrictByCancerActivities(GsUser user, GsGrant
    // grant) { //Abdul Latheef: Used new FormGrant instead of the old GsGrant.
    private static boolean restrictByCancerActivities(GsUser user,
            FormGrantProxy grant) {

        Properties p = (Properties) AppConfigProperties.getInstance()
                .getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);

        String minNames = p.getProperty("minoritysupplements.userids");

        if (p.getProperty("cancer.grants.only.restriction").equalsIgnoreCase(
                "YES")) {

            if (user.getCancerActivities().contains(grant.getCancerActivity())
                    || StringUtils.contains(minNames, user.getOracleId())) {

                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }

    }

    private static void setQuestionResponsesInRequest(GreensheetFormProxy form,
            HttpServletRequest req) {

        Iterator iter = form.getQuestionResponsDataMap().keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            QuestionResponseData qr = (QuestionResponseData) form
                    .getQuestionResponsDataMap().get(key);
            req.setAttribute(key, qr);
        }

    }

    // Abdul Latheef: New method.
    public static List<FormGrantProxy> getFormGrantProxyList(
            List formGrantsList, GsUser user) {
        List<FormGrantProxy> grantProxyList = new ArrayList<FormGrantProxy>();

        if (formGrantsList == null) {
            return grantProxyList;
        }

        for (int i = 0; i < formGrantsList.size(); i++) {
            FormGrant formGrant = (FormGrant) formGrantsList.get(i);
            FormGrantProxy grantProxy = new FormGrantProxy(user); // Task to do:
                                                                  // See if
                                                                  // this can
                                                                  // be done
                                                                  // in a
                                                                  // better
                                                                  // way.
                                                                  // --Abdul
                                                                  // Latheef

            grantProxy.setDummy(formGrant.isDummy() ? "Y" : "N");
            grantProxy.setOnControl(formGrant.isOnControl() ? "Y" : "N");
            grantProxy.setElectronicallySubmitted(formGrant
                    .isElectronicallySubmitted() ? "Y" : "N");
            grantProxy.setApplId(formGrant.getApplId());
            grantProxy.setFullGrantNum(formGrant.getFullGrantNum());
            grantProxy.setRfaPaNumber(formGrant.getRfaPaNumber());
            grantProxy.setCouncilMeetingDate(formGrant.getCouncilMeetingDate());
            grantProxy.setPiFirstName(formGrant.getPiFirstName());
            grantProxy.setPiMiddleName(formGrant.getPiMiddleName());
            grantProxy.setPiLastName(formGrant.getPiLastName());
            grantProxy.setPiName(formGrant.getPiName());
            grantProxy.setIrgPercentileNum(formGrant.getIrgPercentileNum());
            grantProxy.setPriorityScoreNum(formGrant.getPriorityScoreNum());
            grantProxy.setApplTypeCode(formGrant.getApplTypeCode());
            grantProxy.setAdminPhsOrgCode(formGrant.getAdminPhsOrgCode());
            grantProxy.setActivityCode(formGrant.getActivityCode());
            grantProxy.setSerialNum(formGrant.getSerialNum());
            grantProxy.setSupportYear(formGrant.getSupportYear());
            grantProxy.setSuffixCode(formGrant.getSuffixCode());
            grantProxy.setApplStatusCode(formGrant.getApplStatusCode());
            grantProxy.setApplStatusGroupCode(formGrant
                    .getApplStatusGroupCode());
            grantProxy.setFormerNum(formGrant.getFormerNum());
            grantProxy.setBudgetStartDate(formGrant.getBudgetStartDate());
            grantProxy.setLatestBudgetStartDate(formGrant
                    .getLatestBudgetStartDate());
            grantProxy.setFy(formGrant.getFy());
            grantProxy.setIpf(formGrant.getIpf());
            grantProxy.setOrgName(formGrant.getOrgName());
            grantProxy.setWithinPaylineFlag(formGrant.getWithinPaylineFlag());
            grantProxy.setCayCode(formGrant.getCayCode());
            grantProxy.setRoleUsageCode(formGrant.getRoleUsageCode());
            grantProxy.setPdNpeId(formGrant.getPdNpeId());
            grantProxy.setPdNpnId(formGrant.getPdNpnId());
            grantProxy.setPdLastName(formGrant.getPdLastName());
            grantProxy.setPdFirstName(formGrant.getPdFirstName());
            grantProxy.setPdMiddleName(formGrant.getPdMiddleName());
            grantProxy.setPdUserId(formGrant.getPdUserId());
            grantProxy.setGmsCode(formGrant.getGmsCode());
            grantProxy.setGmsNpeId(formGrant.getGmsNpeId());
            grantProxy.setGmsNpnId(formGrant.getGmsNpnId());
            grantProxy.setGmsLastName(formGrant.getGmsLastName());
            grantProxy.setGmsFirstName(formGrant.getGmsFirstName());
            grantProxy.setGmsMiddleName(formGrant.getGmsMiddleName());
            grantProxy.setGmsUserId(formGrant.getGmsUserId());
            grantProxy.setBkupGmsCode(formGrant.getBkupGmsCode());
            grantProxy.setBkupGmsNpeId(formGrant.getBkupGmsNpeId());
            grantProxy.setBkupGmsNpnId(formGrant.getBkupGmsNpnId());
            grantProxy.setBkupGmsLastName(formGrant.getBkupGmsLastName());
            grantProxy.setBkupGmsFirstName(formGrant.getBkupGmsFirstName());
            grantProxy.setBkupGmsMiddleName(formGrant.getBkupGmsMiddleName());
            grantProxy.setBkupGmsUserId(formGrant.getBkupGmsUserId());
            grantProxy.setAllGmsUserIds(formGrant.getAllGmsUserIds());
            grantProxy.setPgmFormStatus(formGrant.getPgmFormStatus());
            grantProxy.setPgmFormSubmittedDate(formGrant
                    .getPgmFormSubmittedDate());
            grantProxy.setSpecFormStatus(formGrant.getSpecFormStatus());
            grantProxy.setSpecFormSubmittedDate(formGrant
                    .getSpecFormSubmittedDate());
            grantProxy.setDmFormStatus(formGrant.getDmFormStatus());
            grantProxy.setMinority(formGrant.isMinority() ? "Y" : "N");

            grantProxyList.add(grantProxy);
        }

        return grantProxyList;
    }
}
