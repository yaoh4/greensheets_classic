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
 * Action class that retrieves a greensheet form based on the type and mechanism. 
 * The following infomation must be provided. 
 * </br></br>
 * <li>User Id: if the remote username is not available then the parameter ORACLE_ID will be used.</li>
 * <li>GsGrant Number: The full grant number. This is passed as a parameter GRANT_ID</li>
 * <li>GroupType: this is the group that works up the greensheet. This is passed as the parameter GS_GROUP_TYPE</li>
 * 
 * 
 *  @author kpuscas, Number Six Software
 */

public class RetrieveGreensheetAction extends GsBaseAction {

    private static final Logger logger = Logger.getLogger(RetrieveGreensheetAction.class);
    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew() && req.getParameter("EXTERNAL") == null) {
            forward = "actionConfirm";
            req.setAttribute(
                "ACTION_CONFIRM_MESSAGE",
                "Your user session has time out. Please close this window and Refresh your grants list");

        } else {

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
            GsGrant grant = this.getGrant(req);
            GreensheetForm gsform = this.getForm(req, grant);

            GreensheetFormSession gfs = new GreensheetFormSession(gsform, grant);

            String id = gus.addGreensheetFormSession(gfs);

            if (gsform.getTemplateId() <= 0) {
                req.setAttribute("MISSING_TYPE", grant.getType());
                req.setAttribute("MISSING_MECH", grant.getMech());
                forward = "templatenotfound";

            } else {


                if (gsform.getStatus().equals(GreensheetStatus.FROZEN)) {
                    
                    logger.debug("FROZEN GS");
                    
                    req.setAttribute("TEMPLATE_ID", "F" + Integer.toString(gsform.getTemplateId()));
                } else {

                    req.setAttribute("TEMPLATE_ID", "" + Integer.toString(gsform.getTemplateId()));

                }

                req.setAttribute(GreensheetsKeys.KEY_FORM_UID, id);
                //needed to force validation on parent questions that don't have saved answers

                QuestionResponseData dummy = new QuestionResponseData();
                dummy.setSelectionDefId("VALIDATE_TRUE");

                req.getSession().setAttribute("VALIDATE_TRUE", dummy);

                GreensheetActionHelper.setFormDisplayInfo(req, id);
                forward = "vmid";
            }
        }

        return (mapping.findForward(forward));
    }

    private GsGrant getGrant(HttpServletRequest req) throws Exception {

        GreensheetUserSession gus = (GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
        String grantIdp = (String) req.getParameter(GreensheetsKeys.KEY_GRANT_ID);
        String grantIda = (String) req.getAttribute(GreensheetsKeys.KEY_GRANT_ID);
        String applId = (String) req.getParameter(GreensheetsKeys.KEY_APPL_ID);
        String grantId = null;
        GsGrant grant = null;

        // the grant Id can be on a parameter or an attribute check to see which
        if (grantIdp == null && grantIda == null && applId == null) {
            throw new GreensheetBaseException("error.grantid");
        } else if (grantIdp != null) {
            grantId = grantIdp;
        } else if (grantIda != null) {
            grantId = grantIda;
        }


        if (gus != null) {
            GsGrant g = gus.getGrantByGrantNumber(grantId);
            if (g == null) {
                GrantMgr grantMgr = GreensheetMgrFactory.createGrantMgr(GreensheetMgrFactory.PROD);
                if (applId != null) {
                    grant = grantMgr.findGrantById(applId,grantId);
                    gus.addGrant(grant);
                }

                logger.debug("Grant not in user list. Get new one " + grant.getFullGrantNumber());

            } else {
                grant = g;

                logger.debug("Grant in user list " + grant.getFullGrantNumber());
            }
        }
        return grant;
    }

    private GreensheetForm getForm(HttpServletRequest req, GsGrant grant) throws Exception {

        GreensheetForm form = null;

        String groupp = req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
        String groupa = (String) req.getAttribute(GreensheetsKeys.KEY_GS_GROUP_TYPE);
        String group = null;

        // the grant Id can be on a parameter or an attribute check to see which
        if (groupp == null && groupa == null) {
            throw new GreensheetBaseException("error.grouptype");
        } else if (groupp != null) {
            group = groupp;
        } else if (groupa != null) {
            group = groupa;
        }

        GreensheetFormMgr mgr = GreensheetMgrFactory.createGreensheetFormMgr(GreensheetMgrFactory.PROD);

        if (group.equalsIgnoreCase(GreensheetGroupType.PGM.getName())) {
            form = mgr.findGreensheetForGrant(grant, GreensheetGroupType.PGM);

        } else if (group.equalsIgnoreCase(GreensheetGroupType.SPEC.getName())) {
            form = mgr.findGreensheetForGrant(grant, GreensheetGroupType.SPEC);

        } else if (group.equalsIgnoreCase(GreensheetGroupType.RMC.getName())) {
            form = mgr.findGreensheetForGrant(grant, GreensheetGroupType.RMC);

        } else {
            throw new GreensheetBaseException("error.grouptype");
        }

        return form;

    }

}
