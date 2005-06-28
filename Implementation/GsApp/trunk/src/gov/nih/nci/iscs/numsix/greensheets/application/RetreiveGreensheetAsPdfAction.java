/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;
/**
 *
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class RetreiveGreensheetAsPdfAction extends GsBaseAction {

    private static final Logger logger = Logger.getLogger(RetreiveGreensheetAsPdfAction.class);
    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and the Greensheet Window and Refresh your grants list");
            return mapping.findForward(forward);
        } else {

            String formUid = req.getParameter(GreensheetsKeys.KEY_FORM_UID);
            String commentOption = req.getParameter("commentsDisplayOption").trim();
            String generateAllQuestions = req.getParameter("generateAllQuestions").trim();

            String commentOptionSepPage = "NO";

            if (commentOption.equalsIgnoreCase("SHOW_WITH_QUESTION")) {
                commentOption = "YES";
                commentOptionSepPage = "NO";
            } else if (commentOption.equalsIgnoreCase("SHOW_SEPARATE")) {
                commentOptionSepPage = "YES";
                commentOption = "YES";
            }

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
            GreensheetFormSession gfs = gus.getGreensheetFormSession(formUid);

            GreensheetForm form = gfs.getForm();

            GsGrant grant = gfs.getGrant();

            GreensheetFormMgr mgr = GreensheetMgrFactory.createGreensheetFormMgr(GreensheetMgrFactory.PROD);

            byte[] pdfdoc = mgr.getGreensheetFormAsPdf(form, grant, commentOption, commentOptionSepPage, generateAllQuestions);

            //Prepare response
            resp.setContentType("application/pdf");
            resp.setContentLength(pdfdoc.length);

            //Send content to Browser
            resp.getOutputStream().write(pdfdoc);
            resp.getOutputStream().flush();

            return null;
        }
    }

}
