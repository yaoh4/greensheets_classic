package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseDispatchAction;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.GreensheetPreferencesMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.GreensheetPreferencesMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class SearchProgramGrantsAction extends GsBaseDispatchAction {

	/** restorePreferences */
	public ActionForward restorePreferences(ActionMapping mapping,
			ActionForm form, HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		ProgramPreferencesForm prefs = (ProgramPreferencesForm) form;

		// get the user session
		GreensheetUserSession gus = GreensheetActionHelper
				.getGreensheetUserSession(req);

		// get the grant manager
		GrantMgr gMgr = GreensheetMgrFactory
				.createGrantMgr(GreensheetMgrFactory.PROD);

		// set program preferences form values
		restoreProgramPreferencesForm(gus.getUser(), prefs);

		// set payline options for user session
		setUserSessionPaylineOption(prefs, gus);

		// set portfolio options
		setUserSessionMyPortfolioOption(prefs, gus);

//		 Bug#4204 Abdul: Commented out this for the new fields lastName and firstName		
		// get list of grants for user
//		Map grantsMap = gMgr.getGrantsListForProgramUser(gus.getUser(), prefs
//				.getGrantSource(), prefs.getGrantType(), prefs.getMechanism(),
//				prefs.getOnlyGrantsWithinPayline(), prefs.getGrantNumber(),
//				prefs.getPiName());
		Map grantsMap = gMgr.getGrantsListForProgramUser(gus.getUser(), prefs
				.getGrantSource(), prefs.getGrantType(), prefs.getMechanism(),
				prefs.getOnlyGrantsWithinPayline(), prefs.getGrantNumber(),
				prefs.getLastName(), prefs.getFirstName());		
		gus.setGrants(grantsMap);

		// transform list of grants into a list of proxy objects each containig
		// a grant and a user. This list of proxy objects is consumed in the JSP
		// displaytag rendered HTML table
		List list = GreensheetActionHelper.getGrantGreensheetProxyList(
				grantsMap, gus.getUser());

		// set grant list in the request
		req.getSession().setAttribute("GRANT_LIST", list);

		// set all LOVs in Form
		setProgramPreferencesFormLOVs(req);

		return mapping.findForward(Constants.SUCCESS_KEY);
	}

	/** search */
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		ProgramPreferencesForm prefs = (ProgramPreferencesForm) form;

		// get the user session
		GreensheetUserSession gus = GreensheetActionHelper
				.getGreensheetUserSession(req);

		// get the grant manager
		GrantMgr gMgr = GreensheetMgrFactory
				.createGrantMgr(GreensheetMgrFactory.PROD);

		// set program preferences form values
		setProgramPreferencesForm(gus.getUser(), prefs);

		// set payline options for user session
		setUserSessionPaylineOption(prefs, gus);

		// set portfolio options
		setUserSessionMyPortfolioOption(prefs, gus);

//		Bug#4204 Abdul: Changed the code to accomodate the 2 new fields  
		// get list of grants for user
//		Map grantsMap = gMgr.getGrantsListForProgramUser(gus.getUser(), prefs
//				.getGrantSource(), prefs.getGrantType(), prefs.getMechanism(),
//				prefs.getOnlyGrantsWithinPayline(), prefs.getGrantNumber(),
//				prefs.getPiName());
		Map grantsMap = gMgr.searchForProgramUserGrantList(gus.getUser(), prefs
				.getGrantSource(), prefs.getGrantType(), prefs.getMechanism(),
				prefs.getOnlyGrantsWithinPayline(), prefs.getGrantNumber(),
				prefs.getLastName(), prefs.getFirstName());
		gus.setGrants(grantsMap);

		// transform list of grants into a list of proxy objects each containig
		// a grant and a user. This list of proxy objects is consumed in the JSP
		// displaytag rendered HTML table
		List list = GreensheetActionHelper.getGrantGreensheetProxyList(
				grantsMap, gus.getUser());

		// set grant list in the request
		req.getSession().setAttribute("GRANT_LIST", list);

		// set all LOVs in Form
		setProgramPreferencesFormLOVs(req);

		return mapping.findForward(Constants.SUCCESS_KEY);
	}

	/** cancel */
	public ActionForward cancel(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse res) throws Exception {

		ProgramPreferencesForm prefs = (ProgramPreferencesForm) form;

		// get the user session
		GreensheetUserSession gus = GreensheetActionHelper
				.getGreensheetUserSession(req);

		// set program preferences form values
		restoreProgramPreferencesForm(gus.getUser(), prefs);

		// set all LOVs in Form
		setProgramPreferencesFormLOVs(req);

		return mapping.findForward(Constants.SUCCESS_KEY);
	}

	/** setProgramPreferencesFormLOVs 
	 * @throws Exception */
	private void setProgramPreferencesFormLOVs(HttpServletRequest req) throws Exception {
		setGrantsSourcesLOV(req);
		setGrantTypesLOV(req);
	}

	/** setGrantsSourcesLOV 
	 * @throws Exception */
	private void setGrantsSourcesLOV(HttpServletRequest req) throws Exception {
		// Bug#4157 Abdul: Collect the user's cancer activities.
		// get the user session
		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
		GsUser loggedOnUser = gus.getUser();
		List cancerActivities = loggedOnUser.getCancerActivities();
		int activityCount = 0;
		StringBuffer userCancerActivities = new StringBuffer("");
		
		if (cancerActivities != null) {
			activityCount = cancerActivities.size();
			if (activityCount > 0) {
				userCancerActivities.append(" (" + (String) cancerActivities.get(0));
				for (int index = 1; index < activityCount; index++) {
					userCancerActivities.append(", ").append(cancerActivities.get(index));
				}
				userCancerActivities.append(") ");
			}
		}
		
		Collection grantSources = new ArrayList();
		grantSources.add(new LabelValueBean("My Portfolio",
				Constants.PREFERENCES_MYPORTFOLIO));
//		Bug#4157 Abdul: Append the user's cancer activities to the choice 'My Cancer Activity'.
//		grantSources.add(new LabelValueBean("My Cancer Activity",
//				Constants.PREFERENCES_MYCANCERACTIVITY));
		grantSources.add(new LabelValueBean("My Cancer Activity" + userCancerActivities,
				Constants.PREFERENCES_MYCANCERACTIVITY));
		grantSources.add(new LabelValueBean("All NCI Grants",
				Constants.PREFERENCES_ALLNCIGRANTS));
		req.setAttribute(Constants.GRANT_SOURCES_KEY, grantSources);
	}

	/** setGrantTypesLOV */
	private void setGrantTypesLOV(HttpServletRequest req) {
		Collection grantTypes = new ArrayList();
		grantTypes.add(new LabelValueBean("Competing Grants",
				Constants.PREFERENCES_COMPETINGGRANTS));
		grantTypes.add(new LabelValueBean("Non-Competing Grants",
				Constants.PREFERENCES_NONCOMPETINGGRANTS));
		grantTypes.add(new LabelValueBean("Both Competing and Non-Competing Grants", Constants.PREFERENCES_BOTH));
		req.setAttribute(Constants.GRANT_TYPES_KEY, grantTypes);
	}

	/** setProgramPreferencesForm */
	private ProgramPreferencesForm setProgramPreferencesForm(GsUser gsUser,
			ProgramPreferencesForm prefs) throws Exception {
		if (prefs.isNull()) {
			return restoreProgramPreferencesForm(gsUser, prefs);
		} else {
			return prefs;
		}
	}

	/** restoreProgramPreferencesForm */
	private ProgramPreferencesForm restoreProgramPreferencesForm(GsUser gsUser,
			ProgramPreferencesForm prefs) throws Exception {

		GreensheetPreferencesMgr pm = (GreensheetPreferencesMgr) new GreensheetPreferencesMgrImpl(
				gsUser);
		Map map = pm.restoreUserPreferences();
		prefs.grantSource = (String) map
				.get(Constants.PREFERENCES_GRANT_SOURCE_KEY);
		prefs.grantType = (String) map
				.get(Constants.PREFERENCES_GRANT_TYPE_KEY);
		prefs.mechanism = (String) map
				.get(Constants.PREFERENCES_GRANT_MECHANISM_KEY);
		prefs.onlyGrantsWithinPayline = (String) map
				.get(Constants.PREFERENCES_GRANT_PAYLINE_KEY);
		prefs.grantNumber = (String) map
				.get(Constants.PREFERENCES_GRANT_NUMBER_KEY);
//		prefs.piName = (String) map.get(Constants.PREFERENCES_GRANT_PI_KEY); // Bug#4204 Abdul: Commented out this for the new fields lastName and firstName
		prefs.lastName = (String) map.get(Constants.PREFERENCES_GRANT_PI_LAST_NAME_KEY); // Bug#4204 Abdul: Added the new field lastName
		prefs.firstName = (String) map.get(Constants.PREFERENCES_GRANT_PI_FIRST_NAME_KEY); // Bug#4204 Abdul: Added the new field firstName
		
		return prefs;
	}

	/** setUserSessionPaylineOption */
	private void setUserSessionPaylineOption(ProgramPreferencesForm prefs,
			GreensheetUserSession gus) {
		if (prefs.getOnlyGrantsWithinPayline() == null) {
			gus.setPaylineOnly(false);
		} else {
			if (prefs.getOnlyGrantsWithinPayline().equals(
					Constants.PREFERENCES_YES)) {
				gus.setPaylineOnly(true);
			}
		}
	}

	/** setUserSessionPaylineOption */
	private void setUserSessionMyPortfolioOption(ProgramPreferencesForm prefs,
			GreensheetUserSession gus) {
		if (prefs.getGrantSource().equals(Constants.PREFERENCES_MYPORTFOLIO)) {
			gus.setMyPortfolio(true);
		} else {
			gus.setMyPortfolio(false);
		}
	}

}
