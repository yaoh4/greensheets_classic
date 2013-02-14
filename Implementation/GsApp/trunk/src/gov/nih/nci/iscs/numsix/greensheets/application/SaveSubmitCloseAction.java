/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
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
 * Action manages the Save, Submit, and Close operations for the greensheet form.
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

        logger.debug("save() Begin");
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
            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }
            GsUser user = gus.getUser();
            GreensheetFormProxy form = gfs.getForm();
            form.setPOC(poc);
            //			GsGrant grant = gfs.getGrant(); //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            FormGrantProxy grant = gfs.getGrant();

            //			GreensheetFormMgr mgr = GreensheetMgrFactory	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            //					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
            GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); //For time being -- Abdul Latheef 
            mgr.saveForm(form, req.getParameterMap(), user, grant);

            try {
                GreensheetActionHelper.setFormDisplayInfo(req, id);
            } catch (GreensheetBaseException e) {
                if (e.getMessage().contains("sessionTimeOut"))
                    return mapping.findForward("sessionTimeOut");
            }

            req.setAttribute(GreensheetsKeys.KEY_GRANT_ID, grant
                    .getFullGrantNumber());
            req.setAttribute(GreensheetsKeys.KEY_GS_GROUP_TYPE, form
                    .getGroupTypeAsString());

            req.setAttribute("TEMPLATE_ID", Integer.toString(form
                    .getTemplateId()));
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, id);

            forward = "retrievegreensheet";
        }
        logger.debug("save() End");
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

        logger.debug("submit() Begin");
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
            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }
            GsUser user = gus.getUser();
            GreensheetFormProxy form = gfs.getForm();
            form.setPOC(poc);

            //			GsGrant grant = gfs.getGrant(); //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            FormGrantProxy grant = gfs.getGrant();

            //			GreensheetFormMgr mgr = GreensheetMgrFactory	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            //					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
            GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); //For time being -- Abdul Latheef

            mgr.submitForm(form, req.getParameterMap(), user, grant);

            try {
                req.setAttribute(GreensheetsKeys.KEY_ACTION_CONFIRM_MESSAGE,
                        "The Greensheet Form for Grant "
                                + gus.getFormSessionGrant(id).getFullGrantNumber()
                                + " has been submitted");
            } catch (NullPointerException e) {

                return mapping.findForward("sessionTimeOut");
            }

            gus.removeGreensheetFormSession(id);
        }
        logger.debug("submit() End");
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

        logger.debug("close() Begin");
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
        logger.debug("close() End");
        return mapping.findForward(forward);

    }

}
