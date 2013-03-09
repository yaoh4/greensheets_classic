package gov.nih.nci.iscs.numsix.greensheets.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import org.apache.log4j.*;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;

public class ChangeCurrFYAction extends GsBaseAction {

	private Logger logger = Logger.getLogger(ChangeCurrFYAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		DynaActionForm dynaForm = null;
		Object newFYobj = null;
		Integer newFY = null;
		
		Object buttonClickedObj = null;
		String buttonClicked = "";
		
		if (form instanceof DynaActionForm) {
			dynaForm = (DynaActionForm) form;
			newFYobj = dynaForm.get("newFY");
			if (newFYobj!=null && newFYobj instanceof Integer) {
				newFY = (Integer) newFYobj;
				logger.debug(" * New FY: " + newFY);
				buttonClickedObj = dynaForm.get("okButton");
				if (buttonClickedObj!=null && buttonClickedObj instanceof String) {
					buttonClicked = (String) buttonClickedObj;
					if (buttonClicked.equals("OK")) {
						// Only when the button the user clicked to submit the form was OK
						// will we proceed with "overriding" the actual current FY with what 
						// the user entered.  Else (i.e. if they clicked "Cancel") we will 
						// still return to the home page, but we won't touch the FY.
						logger.debug(" * Proceeding to store FY_TO_ASSUME in current user's preferences, " +
								"for duration of the session only...");
				        						
						GreensheetUserSession userObjInSession = GreensheetActionHelper.getGreensheetUserSession(request);
						if (userObjInSession!=null) {
							userObjInSession.getUser().getUserPreferences().put("FY_TO_ASSUME", newFY.toString());
						}
					}
				}
			}
		}
		
		return mapping.findForward("retrievegrants");
	}

}
