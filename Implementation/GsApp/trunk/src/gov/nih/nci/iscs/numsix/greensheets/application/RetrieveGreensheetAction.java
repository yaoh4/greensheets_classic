/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormService;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action class that retrieves a greensheet form based on the type and mechanism. The following infomation must be provided. </br></br> <li>User Id:
 * if the remote username is not available then the parameter ORACLE_ID will be used.</li> <li>GsGrant Number: The full grant number. This is passed
 * as a parameter GRANT_ID</li> <li>GroupType: this is the group that works up the greensheet. This is passed as the parameter GS_GROUP_TYPE</li>
 * 
 * @author kpuscas, Number Six Software
 */

public class RetrieveGreensheetAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(RetrieveGreensheetAction.class);

    static GreensheetsFormGrantsService greensheetsFormGrantsService;

    static GreensheetFormService greensheetFormService;

    public GreensheetsFormGrantsService getGreensheetsFormGrantsService() {
        return greensheetsFormGrantsService;
    }

    public void setGreensheetsFormGrantsService(
            GreensheetsFormGrantsService greensheetsFormGrantsService) {
        this.greensheetsFormGrantsService = greensheetsFormGrantsService;
    }

    public GreensheetFormService getGreensheetFormService() {
        return greensheetFormService;
    }

    public void setGreensheetFormService(
            GreensheetFormService greensheetFormService) {
        this.greensheetFormService = greensheetFormService;
    }

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String forward = null;
        logger.debug("execute() Begin");

        if (req.getSession().isNew() && req.getParameter("EXTERNAL") == null) {

            forward = "actionConfirm";
            req.setAttribute(
                    "ACTION_CONFIRM_MESSAGE",
                    "Your user session has timed out. Please close this window and refresh your grants list.");
        } else {
            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            // GsGrant grant = this.getGrant(req); //Abdul Latheef: Used new
            // FormGrant instead of the old GsGrant.
            FormGrantProxy grant = this.getGrant(req);

            if (grant == null) {
                return mapping.findForward("sessionTimeOut");
            }

            GreensheetFormProxy gsform = this.getForm(req, grant);

            if (gsform == null) {
                return mapping.findForward("sessionTimeOut");
            }

            GreensheetFormSession gfs = new GreensheetFormSession(gsform, grant);

            String id = gus.addGreensheetFormSession(gfs);

            if (gsform.getFtmId() <= 0) {
                req.setAttribute("MISSING_TYPE", grant.getApplTypeCode());
                req.setAttribute("MISSING_MECH", grant.getApplTypeCode());
                forward = "templatenotfound";
            } else {
                if (gsform.getStatus().equals(GreensheetStatus.FROZEN) || gsform.getStatus().equals(GreensheetStatus.SUBMITTED)) {
                    logger.debug("FROZEN GS");
                    req.setAttribute("TEMPLATE_ID",
                            "F" + Integer.toString(gsform.getFtmId()));
                } else {
                    req.setAttribute("TEMPLATE_ID",
                            "" + Integer.toString(gsform.getFtmId()));
                }
                req.setAttribute(GreensheetsKeys.KEY_FORM_UID, id);
                // needed to force validation on parent questions that don't
                // have saved answers

                QuestionResponseData dummy = new QuestionResponseData();
                dummy.setSelectionDefId("VALIDATE_TRUE");

                req.getSession().setAttribute("VALIDATE_TRUE", dummy);

                GreensheetActionHelper.setFormDisplayInfo(req, id);

                forward = "vmid";
            }
        }
        logger.debug("execute() End");
        return (mapping.findForward(forward));
    }

    // private GsGrant getGrant(HttpServletRequest req) throws Exception {
    // //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    private FormGrantProxy getGrant(HttpServletRequest req) throws Exception {
        logger.debug("getGrant() Begin");
        GreensheetUserSession gus = (GreensheetUserSession) req.getSession()
                .getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
        String grantIdp = (String) req
                .getParameter(GreensheetsKeys.KEY_GRANT_ID);
        String grantIda = (String) req
                .getAttribute(GreensheetsKeys.KEY_GRANT_ID);
        String applId = (String) req.getParameter(GreensheetsKeys.KEY_APPL_ID);
        String grantId = null;
        // GsGrant grant = null; //Abdul Latheef: Used FormGrant instead of
        // GsGrant.
        FormGrantProxy grant = null;

        // the grant Id can be on a parameter or an attribute check to see which
        if (grantIdp == null && grantIda == null && applId == null) {
            // throw new GreensheetBaseException("error.grantid");
            return null;
        } else if (grantIdp != null) {
            grantId = grantIdp;
        } else if (grantIda != null) {
            grantId = grantIda;
        }

        // ApplicationContext context = new
        // ClassPathXmlApplicationContext("applicationContext.xml");
        // GreensheetsFormGrantsService greensheetsFormGrantsService =
        // (GreensheetsFormGrantsService)
        // context.getBean("greensheetsFormGrantsService");
        List formGrants = null;
        List formGrantsProxies = null;

        if (gus != null) {
            // GsGrant grant = gus.getGrantByGrantNumber(grantId);
            // Abdul Latheef: Go to the DB until some workaround is devised or
            // the DAO
            // or the DAO is rewritten to return a Map instead of List.
        }

        if (grant == null) {
            // Abdul Latheef (For GPMATS): Retrieve the grant by the APPL ID
            if ((applId != null) && !(applId.trim().equalsIgnoreCase(""))) {
                formGrants = greensheetsFormGrantsService
                        .findGrantsByApplId(Long.parseLong(applId));
                formGrantsProxies = GreensheetActionHelper
                        .getFormGrantProxyList(formGrants, gus.getUser());
            }

            // Next, try retrieving the grant by the Grant#
            if (formGrantsProxies == null || formGrantsProxies.size() < 1
                    || formGrantsProxies.size() > 1) {
                if (grantId != null && (!"".equals(grantId.trim()))) {
                    formGrants = greensheetsFormGrantsService
                            .findGrantsByGrantNum(grantId.trim());
                    formGrantsProxies = GreensheetActionHelper
                            .getFormGrantProxyList(formGrants, gus.getUser());
                }
            }

            if (formGrantsProxies != null && formGrantsProxies.size() > 0) {
                grant = (FormGrantProxy) formGrantsProxies.get(0);
            } else {
                grant = null;
            }
        }

        return grant;
    }

    // private GreensheetFormProxy getForm(HttpServletRequest req, GsGrant
    // grant) throws Exception { //Abdul Latheef: Used new FormGrantProxy
    // instead of the old GsGrant.
    private GreensheetFormProxy getForm(HttpServletRequest req,
            FormGrantProxy grant) throws Exception {
        logger.debug("getForm() Begin");
        GreensheetFormProxy form = null;

        String groupp = req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
        String groupa = (String) req
                .getAttribute(GreensheetsKeys.KEY_GS_GROUP_TYPE);
        String group = null;

        // the grant Id can be on a parameter or an attribute check to see which
        if (groupp == null && groupa == null) {
            // throw new GreensheetBaseException("error.grouptype");
            return null;
        } else if (groupp != null) {
            group = groupp;
        } else if (groupa != null) {
            group = groupa;
        }

        // GreensheetFormMgr mgr = GreensheetMgrFactory //Abdul Latheef: Used
        // new FormGrantProxy instead of the old GsGrant.
        // .createGreensheetFormMgr(GreensheetMgrFactory.PROD);
        // GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); //For time being
        // -- Abdul Latheef
        // ApplicationContext context = new
        // ClassPathXmlApplicationContext("applicationContext.xml");
        // GreensheetFormService greensheetFormService = (GreensheetFormService)
        // context.getBean("greensheetFormService");

        form = greensheetFormService.getGreensheetForm(grant, group);

        // if (group.equalsIgnoreCase(GreensheetGroupType.PGM.getName())) {
        // form = mgr.findGreensheetForGrant(grant, GreensheetGroupType.PGM);
        //
        // } else if
        // (group.equalsIgnoreCase(GreensheetGroupType.SPEC.getName())) {
        // form = mgr.findGreensheetForGrant(grant, GreensheetGroupType.SPEC);
        //
        // } else if (group.equalsIgnoreCase(GreensheetGroupType.RMC.getName()))
        // {
        // form = mgr.findGreensheetForGrant(grant, GreensheetGroupType.RMC);
        // // Abdul Latheef (for GPMATS enhancements)
        // } else if (group.equalsIgnoreCase(GreensheetGroupType.DM.getName()))
        // {
        // form = mgr.findGreensheetForGrant(grant, GreensheetGroupType.DM);
        // } else {
        // throw new GreensheetBaseException("error.grouptype");
        // }
        logger.debug("getForm() End");

        return form;
    }
}
