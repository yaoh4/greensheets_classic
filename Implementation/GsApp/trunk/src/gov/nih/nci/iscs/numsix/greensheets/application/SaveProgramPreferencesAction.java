package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.GreensheetPreferencesMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.GreensheetPreferencesMgrImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SaveProgramPreferencesAction extends GsBaseAction {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		ProgramPreferencesForm prefs = (ProgramPreferencesForm) form;

		// get the user session
		GreensheetUserSession gus = GreensheetActionHelper
				.getGreensheetUserSession(req);

		// save preferences form values
		GreensheetPreferencesMgr pm = (GreensheetPreferencesMgr) new GreensheetPreferencesMgrImpl(
				gus.getUser());
		Map map = new HashMap();
		map.put(Constants.PREFERENCES_GRANT_SOURCE_KEY, prefs.getGrantSource());
		map.put(Constants.PREFERENCES_GRANT_TYPE_KEY, prefs.getGrantType());

		if (prefs.getOnlyGrantsWithinPayline() == null) {
			map.put(Constants.PREFERENCES_GRANT_PAYLINE_KEY,
					Constants.PREFERENCES_NO);
		} else {
			map.put(Constants.PREFERENCES_GRANT_PAYLINE_KEY, prefs
					.getOnlyGrantsWithinPayline());
		}

		if (prefs.getMechanism().length() != 0) {
			map.put(Constants.PREFERENCES_GRANT_MECHANISM_KEY, prefs
					.getMechanism());
		}
		pm.savePreferences(gus.getUser(), map);
		
		// Bug#4157 Abdul: Collect the user's cancer activities.
		List cancerActivities = gus.getUser().getCancerActivities();
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

		// set grant source field
		if (prefs.getGrantSource().equals(Constants.PREFERENCES_MYPORTFOLIO)) {
			req.setAttribute(Constants.GRANT_SOURCE_KEY, "My Portfolio");
		} else if (prefs.getGrantSource().equals(
				Constants.PREFERENCES_MYCANCERACTIVITY)) {
//			Bug#4157 Abdul: Append the user's cancer activities to the choice 'My Cancer Activity'.
//			req.setAttribute(Constants.GRANT_SOURCE_KEY, "My Cancer Activity");
			req.setAttribute(Constants.GRANT_SOURCE_KEY, "My Cancer Activity" + userCancerActivities);
		} else if (prefs.getGrantSource().equals(
				Constants.PREFERENCES_ALLNCIGRANTS)) {
			req.setAttribute(Constants.GRANT_SOURCE_KEY, "All NCI Grants");
		}

		// set grant type field
		if (prefs.getGrantType().equals(Constants.PREFERENCES_COMPETINGGRANTS)) {
			req.setAttribute(Constants.GRANT_TYPE_KEY, "Competing Grants");
		} else if (prefs.getGrantType().equals(
				Constants.PREFERENCES_NONCOMPETINGGRANTS)) {
			req.setAttribute(Constants.GRANT_TYPE_KEY, "Non-Competing Grants");
		} else if (prefs.getGrantType().equals(Constants.PREFERENCES_BOTH)) {
			req.setAttribute(Constants.GRANT_TYPE_KEY,
					"Both Competing & Non-Competing Grants");
		}

		// set mechanism field
		req.setAttribute(Constants.GRANT_MECHANISM_KEY, prefs.getMechanism());

		// set grants within payline check-box (values y or null)
		if (prefs.getOnlyGrantsWithinPayline() == null) {
			req.setAttribute(Constants.GRANTS_WITHIN_PAYLINE_CHOICE, "No");
		} else {
			if (prefs.getOnlyGrantsWithinPayline().equals(
					Constants.PREFERENCES_YES)) {
				req.setAttribute(Constants.GRANTS_WITHIN_PAYLINE_CHOICE, "Yes");
			}
		}
		return mapping.findForward(Constants.SUCCESS_KEY);
	}

}
