/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetForm;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Action manages the Save, Submit, and Close operations for the greensheet
 * form.
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class SaveSubmitCloseAction extends DispatchAction {

	private static final Logger logger = Logger
			.getLogger(SaveSubmitCloseAction.class);

	/**
	 * Method save.
	 * 
	 * @param mapping
	 * @param aForm
	 * @param req
	 * @param resp
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward save(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String forward = null;

		if (req.getSession().isNew()) {
			forward = "actionConfirm";
			req
					.setAttribute(
							"ACTION_CONFIRM_MESSAGE",
							"Your user session has time out. Please close this window and Refresh your grants list");

		} else {

			String id = req.getParameter(GreensheetsKeys.KEY_FORM_UID);
			String poc = req.getParameter("POC");

			GreensheetUserSession gus = GreensheetActionHelper
					.getGreensheetUserSession(req);
			GreensheetFormSession gfs = gus.getGreensheetFormSession(id);
			GsUser user = gus.getUser();
			GreensheetForm form = gfs.getForm();
			form.setPOC(poc);
			GsGrant grant = gfs.getGrant();

			GreensheetFormMgr mgr = GreensheetMgrFactory
					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);

			mgr.saveForm(form, req.getParameterMap(), user, grant);

			GreensheetActionHelper.setFormDisplayInfo(req, id);

			req.setAttribute(GreensheetsKeys.KEY_GRANT_ID, grant
					.getFullGrantNumber());
			req.setAttribute(GreensheetsKeys.KEY_GS_GROUP_TYPE, form
					.getGroupTypeAsString());

			req.setAttribute("TEMPLATE_ID", Integer.toString(form
					.getTemplateId()));
			req.setAttribute(GreensheetsKeys.KEY_FORM_UID, id);

			forward = "retrievegreensheet";
		}
		return mapping.findForward(forward);

	}

	/**
	 * Method submit.
	 * 
	 * @param mapping
	 * @param aForm
	 * @param req
	 * @param resp
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward submit(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		if (req.getSession().isNew()) {

			req
					.setAttribute(
							"ACTION_CONFIRM_MESSAGE",
							"Your user session has time out. Please close this window and Refresh your grants list");

		} else {

			String id = req.getParameter(GreensheetsKeys.KEY_FORM_UID);
			String poc = req.getParameter("POC");
			GreensheetUserSession gus = GreensheetActionHelper
					.getGreensheetUserSession(req);
			GreensheetFormSession gfs = gus.getGreensheetFormSession(id);
			GsUser user = gus.getUser();
			GreensheetForm form = gfs.getForm();
			form.setPOC(poc);

			GsGrant grant = gfs.getGrant();

			GreensheetFormMgr mgr = GreensheetMgrFactory
					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);

			mgr.submitForm(form, req.getParameterMap(), user, grant);

			req.setAttribute(GreensheetsKeys.KEY_ACTION_CONFIRM_MESSAGE,
					"The Greensheet Form for Grant "
							+ gus.getFormSessionGrant(id).getFullGrantNumber()
							+ " has been submitted");

			gus.removeGreensheetFormSession(id);
		}
		return (mapping.findForward("actionConfirm"));

	}

	/**
	 * Method close.
	 * 
	 * @param mapping
	 * @param aForm
	 * @param req
	 * @param resp
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward close(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String forward = null;

		if (req.getSession().isNew()) {
			forward = "actionConfirm";
			req
					.setAttribute(
							"ACTION_CONFIRM_MESSAGE",
							"Your user session has time out. Please close this window and Refresh your grants list");

		} else {

			String id = req.getParameter(GreensheetsKeys.KEY_FORM_UID);

			GreensheetUserSession gus = GreensheetActionHelper
					.getGreensheetUserSession(req);
			gus.removeGreensheetFormSession(id);

			forward = "closeDialog";
		}
		return mapping.findForward(forward);

	}

}
