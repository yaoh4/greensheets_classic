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

		// get list of grants for user
		Map grantsMap = gMgr.getGrantsListForProgramUser(gus.getUser(), prefs
				.getGrantSource(), prefs.getGrantType(), prefs.getMechanism(),
				prefs.getOnlyGrantsWithinPayline(), prefs.getGrantNumber(),
				prefs.getPiName());
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

		// get list of grants for user
		Map grantsMap = gMgr.getGrantsListForProgramUser(gus.getUser(), prefs
				.getGrantSource(), prefs.getGrantType(), prefs.getMechanism(),
				prefs.getOnlyGrantsWithinPayline(), prefs.getGrantNumber(),
				prefs.getPiName());
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

	/** setProgramPreferencesFormLOVs */
	private void setProgramPreferencesFormLOVs(HttpServletRequest req) {
		setGrantsSourcesLOV(req);
		setGrantTypesLOV(req);
		setGrantsWithinPaylineChoicesRADIO(req);
	}

	/** setGrantsSourcesLOV */
	private void setGrantsSourcesLOV(HttpServletRequest req) {
		Collection grantSources = new ArrayList();
		grantSources.add(new LabelValueBean("My Portfolio",
				Constants.PREFERENCES_MYPORTFOLIO));
		grantSources.add(new LabelValueBean("My Cancer Activity",
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
		grantTypes.add(new LabelValueBean("Both", Constants.PREFERENCES_BOTH));
		req.setAttribute(Constants.GRANT_TYPES_KEY, grantTypes);
	}

	/** setGrantsWithinPaylineChoicesRADIO */
	private void setGrantsWithinPaylineChoicesRADIO(HttpServletRequest req) {
		Collection grantsWithinPaylineChoices = new ArrayList();
		grantsWithinPaylineChoices.add(new LabelValueBean("Yes",
				Constants.PREFERENCES_YES));
		grantsWithinPaylineChoices.add(new LabelValueBean("No",
				Constants.PREFERENCES_NO));
		req.setAttribute(Constants.GRANTS_WITHIN_PAYLINE_CHOICES,
				grantsWithinPaylineChoices);
	}

	/** setProgramPreferencesForm */
	private ProgramPreferencesForm setProgramPreferencesForm(GsUser gsUser,
			ProgramPreferencesForm prefs) throws Exception {
		if (prefs.isNull()) {
			return restoreProgramPreferencesForm(gsUser,prefs);
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
		prefs.piName = (String) map.get(Constants.PREFERENCES_GRANT_PI_KEY);
		
		return prefs;
	}	

	/** setUserSessionPaylineOption */
	private void setUserSessionPaylineOption(ProgramPreferencesForm prefs,
			GreensheetUserSession gus) {
		if (prefs.getOnlyGrantsWithinPayline()
				.equals(Constants.PREFERENCES_YES)) {
			gus.setPaylineOnly(true);
		} else {
			gus.setPaylineOnly(false);
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
