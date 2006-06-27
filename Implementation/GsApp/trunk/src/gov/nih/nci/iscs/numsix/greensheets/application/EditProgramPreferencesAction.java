package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.GreensheetPreferencesMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.GreensheetPreferencesMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

public class EditProgramPreferencesAction extends GsBaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		ProgramPreferencesForm prefs = (ProgramPreferencesForm) form;

		GreensheetUserSession gus = GreensheetActionHelper
				.getGreensheetUserSession(req);

		restoreProgramPreferencesForm(gus.getUser(), prefs);

		// set all LOVs in Form
		setProgramPreferencesFormLOVs(req);

		return mapping.findForward(Constants.SUCCESS_KEY);
	}

	/** setProgramPreferencesFormLOVs */
	private void setProgramPreferencesFormLOVs(HttpServletRequest req) {
		setGrantsSourcesLOV(req);
		setGrantTypesLOV(req);
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

		return prefs;
	}

}
