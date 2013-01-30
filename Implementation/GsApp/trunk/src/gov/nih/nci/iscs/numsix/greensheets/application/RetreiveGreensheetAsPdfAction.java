/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author kpuscas, Number Six Software
 */
public class RetreiveGreensheetAsPdfAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(RetreiveGreensheetAsPdfAction.class);

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req
                    .setAttribute(
                            "ACTION_CONFIRM_MESSAGE",
                            "Your user session has time out. Please close this window and the Greensheet Window and Refresh your grants list");
            return mapping.findForward(forward);
        } else {

            String commentOption = "SHOW_WITH_QUESTION";
            String formUid = req.getParameter(GreensheetsKeys.KEY_FORM_UID);
            if (req.getParameter("commentsDisplayOption") != null) {
                commentOption = req.getParameter("commentsDisplayOption")
                        .trim();
            }
            String generateAllQuestions = req.getParameter(
                    "generateAllQuestions").trim();

            String commentOptionSepPage = "NO";

            if (commentOption.equalsIgnoreCase("SHOW_WITH_QUESTION")) {
                commentOption = "YES";
                commentOptionSepPage = "NO";
            } else if (commentOption.equalsIgnoreCase("SHOW_SEPARATE")) {
                commentOptionSepPage = "YES";
                commentOption = "YES";
            }

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);

            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            if (gfs == null) {
                return mapping.findForward("sessionTimeOut");
            }
            GreensheetFormProxy form = gfs.getForm();

            //			GsGrant grant = gfs.getGrant(); //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            FormGrantProxy grant = gfs.getGrant();

            //			GreensheetFormMgr mgr = GreensheetMgrFactory	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
            //					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
            GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); //For time being -- Abdul Latheef

            byte[] pdfdoc = mgr.getGreensheetFormAsPdf(form, grant,
                    commentOption, commentOptionSepPage, generateAllQuestions);

            // Prepare response
            resp.setContentType("application/pdf");
            resp.setContentLength(pdfdoc.length);

            // Send content to Browser
            resp.getOutputStream().write(pdfdoc);
            resp.getOutputStream().flush();

            return null;
        }
    }

}
