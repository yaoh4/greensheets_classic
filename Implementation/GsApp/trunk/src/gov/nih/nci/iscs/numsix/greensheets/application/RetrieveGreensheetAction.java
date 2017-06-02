/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsNoTemplateDefException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormService;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;
import gov.nih.nci.iscs.numsix.greensheets.utils.RedundantEmailPreventer;

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

    private static EmailNotification emailHelper; // must be static or else, even though Spring injects it

    // at web app startup, by the time execute() runs it is null because the instance of this Action 
    // class when execute() runs is a different one, not the one that was created by Spring at startup...
    // WHICH IS BECAUSE WE DON'T HAVE STRUTS1-SPRING INTEGRATION SET UP AT ALL!

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

    public void setEmailHelper(EmailNotification emailHelper) {
        this.emailHelper = emailHelper;
    }

    public EmailNotification getEmailHelper() {
        return emailHelper;
    }

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String forward = null;
        logger.debug("execute() Begin");
        
        
        /**
         * GREENSHEET-495
         * 
         * Is a given APPL_ID a Type 6 Grant with Award Type 1, 2, 4, 5,8 9 in same FY?
         */
        StringBuffer gn = new StringBuffer();
        if( ! greensheetsFormGrantsService.isValidGrantType(req.getParameter(GreensheetsKeys.KEY_APPL_ID), gn) ){
        	req.setAttribute("gnFull", gn);
        	return mapping.findForward("grantTypeError");
        }
        
        GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
        FormGrantProxy grant = this.getGrant(req);

        GreensheetFormProxy gsform = null;
        try {
        	if(grant == null) {
        		grant = new FormGrantProxy(gus.getUser());
        	}        	
            gsform = this.getForm(req, grant);
        } catch (GsNoTemplateDefException noTmplE) {
            req.setAttribute("MISSING_TYPE", grant.getApplTypeCode());
            req.setAttribute("MISSING_MECH", grant.getActivityCode());
            return mapping.findForward("templatenotfound");
        }

        if (gsform == null) {
            return mapping.findForward("error");
        }

        GreensheetFormSession gfs = new GreensheetFormSession(gsform, grant);

        String id = gus.addGreensheetFormSession(gfs);

        if (gsform.getFtmId() <= 0) {
            // This branch of code no longer runs after converting from plain JDBC
            // database access to one based on the Spring JDBC template, because
            // instead of returning 0 items an exception is thrown, so we are now 
            // doing the same by caching the GsNoTemplateDefException exception, above 
            req.setAttribute("MISSING_TYPE", grant.getApplTypeCode());
            req.setAttribute("MISSING_MECH", grant.getActivityCode());
            forward = "templatenotfound";
        } else {
            if (gsform.getStatus() == null) {
                String invalidFormStatusMsg = "Invalid form status detected on greensheet for grant " + grant.getFullGrantNum() + " (appl_id "
                            + grant.getApplID() + ", form id " + gsform.getFormId() + "). Please contact support to resolve.";
                req.getSession().setAttribute("invalidFormStatusMsg", invalidFormStatusMsg);
                return mapping.findForward("invalidFormStatus");
            }
            if (gsform.getStatus().equals(GreensheetStatus.FROZEN) || gsform.getStatus().equals(GreensheetStatus.SUBMITTED)) {
                if (gsform.getQuestionResponsDataMap().size() == 0) { // blank submitted/frozen GS
                    System.out.println("<<<< This SUBMITTED/FROZEN greensheet is empty");
                    return (mapping.findForward("imcompletedGS"));
                } else if (grant.getApplID() != null) {
                    if ((grant.getApplId() == 8974582 || grant.getApplId() == 8735619) && gsform.getGroupType().getName().equalsIgnoreCase("PGM")) {// sepcial case for 3R01CA150980-04S1 appl_id 8974582 / 5U01CA155309-04 appl_id 8735619   
                        System.out.println("<<<< This SUBMITTED/FROZEN greensheet is imcompleted");
                        return (mapping.findForward("imcompletedGS"));
                    } else if ((grant.getApplId() == 8760841 || grant.getApplId() == 8641323 || grant.getApplId() == 8530972)
                            && gsform.getGroupType().getName().equalsIgnoreCase("SPEC")) {// sepcial case for 1R01CA181368-01A1 appl_id 8760841 / 5T32CA009599-26 appl_id 8641323 /5K07CA137140-05 appl_id 8530972    
                        System.out.println("<<<< This SUBMITTED/FROZEN greensheet is imcompleted");
                        return (mapping.findForward("imcompletedGS"));
                    }
                }
                logger.debug("FROZEN GS");
                req.setAttribute("TEMPLATE_ID",
                            "F" + Integer.toString(gsform.getFtmId()));
                req.setAttribute("ACTIVITY_CODE", grant.getActivityCode());
                req.setAttribute("APPL_TYPE_CODE", grant.getApplTypeCode());

            } else {
                req.setAttribute("TEMPLATE_ID",
                            "" + Integer.toString(gsform.getFtmId()));
                req.setAttribute("ACTIVITY_CODE", grant.getActivityCode());
                req.setAttribute("APPL_TYPE_CODE", grant.getApplTypeCode());
            }
            req.setAttribute(GreensheetsKeys.KEY_FORM_UID, id);
            // needed to force validation on parent questions that don't
            // have saved answers

            QuestionResponseData dummy = new QuestionResponseData();
            dummy.setSelectionDefId("VALIDATE_TRUE");

            req.getSession().setAttribute("VALIDATE_TRUE", dummy);

            try {
                GreensheetActionHelper.setFormDisplayInfo(req, id);
            } catch (GreensheetBaseException e) {
                if (e.getMessage().contains("sessionTimeOut"))
                    return mapping.findForward("sessionTimeOut");
            }
            forward = "vmid";
        }

        logger.debug("execute() End");

        return (mapping.findForward(forward));
    }

    private FormGrantProxy getGrant(HttpServletRequest req) throws Exception {

        // TODO: after the various combinations of request parameters and sources 
        // of requests were accounted for, this method ended up having a decent 
        // amount of copy-pasted repetitive code.  It could well use some 
        // refactoring for reuse and better clarity.  - Anatoli, April 2013

        logger.debug("getGrant() Begin");
        boolean checkActionStatus = true;

        GreensheetUserSession gus = (GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
        String grantIdp = (String) req.getParameter(GreensheetsKeys.KEY_GRANT_ID);
        String grantIda = (String) req.getAttribute(GreensheetsKeys.KEY_GRANT_ID);
        String applId = (String) req.getParameter(GreensheetsKeys.KEY_APPL_ID);
        String actionId = (String) req.getParameter(GreensheetsKeys.KEY_AGT_ID);
        
        if (applId == null || "".equals(applId)) {
            Object applIdObj = req.getAttribute(GreensheetsKeys.KEY_APPL_ID);
            if (applIdObj != null && applIdObj instanceof Long) {
                applId = ((Long) applIdObj).toString();
            } else if (applIdObj != null && applIdObj instanceof String) {
                applId = (String) applIdObj;
            }
        }
        
        String grantId = null;
        FormGrantProxy grant = null;

        // the grant Id can be on a parameter or an attribute check to see which
        if (grantIdp == null && grantIda == null && applId == null && actionId == null) {
            return null;
        } else if (grantIdp != null) {
            grantId = grantIdp;
        } else if (grantIda != null) {
            grantId = grantIda;
        }
        
        if (actionId == null || "".equals(actionId)) {
            Object actionIdObj = req.getAttribute(GreensheetsKeys.KEY_AGT_ID);
            if (actionIdObj != null && actionIdObj instanceof Long) {
                actionId = ((Long) actionIdObj).toString();
            } else if (actionIdObj != null && actionIdObj instanceof String) {
                actionId = (String) actionIdObj;
            }
            if(actionId != null) grantId = null;
        }
        
        List<FormGrantProxy> formGrants = null;
        List formGrantsProxies = null;

        String group = req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
        if (group == null || "".equals(group)) {
        	group = (String) req.getAttribute(GreensheetsKeys.KEY_GS_GROUP_TYPE);
        }
        if (group != null) {
        	group = group.trim();
        }
        if (group == null || "".equals(group)) {
        	GreensheetBaseException newException = new GreensheetBaseException("The request to retrieve " +
        			"a greensheet that you submitted does not specify what kind of greensheet (e.g., " +
        			"Program or Specialist or OGA Document Management), and therefore it cannot be completed.");
        	throw newException;
        } else if (group.equalsIgnoreCase(GreensheetGroupType.DM.getName())) {
        	/* *** Dealing with a request for a "DM checklist" greensheet *** */
        	if (grantId != null && !"".equals(grantId)) {
        		// Either (a) GPMATS determined that this is a dummy grant, formed the URL with grant 
        		// number rather than appl_id, or (b) the request is from eGrants
        		formGrants = greensheetsFormGrantsService.retrieveGrantsByFullGrantNum(grantId);
        		formGrantsProxies = GreensheetActionHelper.getFormGrantProxyList(formGrants, gus.getUser());
        		if (formGrants != null && formGrants.size() > 1) {
        			checkActionStatus = greensheetsFormGrantsService.checkActionStatusByGrantId(grantId);
        		}                    
        	} else if (applId != null && !"".equals(applId)) {
        		// GPMATS determined that this is a real, non-dummy, grant and formed the URL with 
        		// appl_id rather than grant number
        		formGrants = greensheetsFormGrantsService.findGrantsByApplId(Long.parseLong(applId));

        		if (formGrants != null && formGrants.size() > 0) {
        			List nonDummyFormGrants = new ArrayList();
        			for (Object oneGrant : formGrants) {
        				if (((FormGrant) oneGrant).isDummy()) {
        					logger.info("Grant with appl_id " + applId + " should be a \"real\" grant \n\t" +
        							"per the URL formed by GPMATS, but there is also a dummy grant with \n\t" +
        							"the same appl_id in the database. So we will NOT ADD it to the list \n\t" +
        							"of non-dummy grants that we will use as the utimate list.");
        				} else {
        					nonDummyFormGrants.add(oneGrant);
        				}
        			}
        			formGrants = nonDummyFormGrants; // replacing the original list, which includes all 
        			// entries with the same appl_id, with the "filtered" list that should only have 
        			// one, non-dummy, grant. 
        			if (formGrants != null && formGrants.size() > 1) {
        				checkActionStatus = greensheetsFormGrantsService.checkActionStatusByApplId(applId);
        			}
        			nonDummyFormGrants = null;
        		}
        		formGrantsProxies = GreensheetActionHelper.getFormGrantProxyList(formGrants, gus.getUser());
        	} else {
        		GreensheetBaseException newException = new GreensheetBaseException("Your request to " +
        				"retrieve a DM checklist provides invalid parameters and cannot be completed. " +
        				"Please contact support.");
        		throw newException;
        	}
        } else if (group.equalsIgnoreCase(GreensheetGroupType.PGM.getName()) ||
        		group.equalsIgnoreCase(GreensheetGroupType.SPEC.getName()) ||
        		group.equalsIgnoreCase(GreensheetGroupType.REV.getName())) {
        	/* Request parameters include BOTH appl_id and grant number when request URLs are composed
                   by Greensheets itself or by eGrants; because a query by appl_id might return two or even 
                   more grants (real plus dummy(ies)), we should always get the grant by the full grant number 
                   and disregard the appl_id when we can.
                   However, as it turns out, request URLs generated by YourGrants include appl_id only, so 
                   to deal with the possibility of multiple grants per appl_id we should probably limit the 
                   list to real grants and suppress the dummies.
        	 */
        	if (actionId != null && !actionId.isEmpty()) {
        		grant = greensheetsFormGrantsService.findGSGrantInfo(Long.parseLong(actionId), null, group, gus.getUser());	
        		req.getSession().setAttribute(GreensheetsKeys.KEY_AGT_ID, actionId);
        	} else if (applId != null && !applId.isEmpty()) {
        		// requested from YourGrants
        		grant = greensheetsFormGrantsService.findGSGrantInfo(null, Long.parseLong(applId), group, gus.getUser());	        		
        	} else {
        		GreensheetBaseException newException = new GreensheetBaseException("Invalid request to retrieve a greensheet. "
        				+ "The greensheet cannot be retrieved without an appl ID or action ID. (grant ID:" + grantId + ")");
            	throw newException;
        	}
        } else {
        	GreensheetBaseException newException = new GreensheetBaseException("The request to retrieve " +
        			"a greensheet that you submitted specifies an invalid type of greensheet (it should be " +
        			"Program or Specialist or OGA Document Management), and therefore it cannot be completed.");
        	throw newException;
        }

        if (formGrants != null && formGrants.size() > 1) {
        	logger.error("\n\t!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        	StringBuffer msgText = new StringBuffer();
        	msgText.append("In 'retrievegreensheet' action, method getGrant(), ").append(
        			"with parameters \n\tappl_id = ").append(applId).append(", full grant number ");
        	msgText.append(grantId).append(
        			", \n\tgreensheet type = " + group + ", more than one grant met the criteria, likely ")
        	.append("meaning there are two GPMATS actions with the same EXPECTED_GRANT_NUM \n\tor ")
        	.append("there are dupliate greensheets caused by an unreconciled award.");
        	msgText.append("\n\tThis is not normal, and OGA probably should be contacted to delete the ")
        	.append("extra action(s) \n\tor the duplicate greensheets should be de-duplicated. " +
        			"However, the user of Greensheets is probably able to continue.");
        	logger.error("\t" + msgText);
        	logger.error("\n\t!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        	RedundantEmailPreventer redundantEmailPreventer = (RedundantEmailPreventer) req.getSession()
        			.getServletContext().getAttribute(GreensheetsKeys.KEY_DUPLGPMATSACTION_REDUND_EMAIL_PREVENTER);
        	if (redundantEmailPreventer != null &&
        			!redundantEmailPreventer.grantNumberNotificationAlreadySent(grantId) &&
        			!redundantEmailPreventer.applIdNotificationAlreadySent(applId)) {
        		if (emailHelper != null && checkActionStatus) {
        			emailHelper.sendTextToSupportEmail(msgText);
        			logger.info("############ Email sent.");
        			redundantEmailPreventer.recordTheSending((FormGrantProxy) formGrantsProxies.get(0));
        		}
        	}
        	grant = (FormGrantProxy) formGrantsProxies.get(0); // TODO: BAD!!! - some time in the future 
        	// we should change the data model to support multiple GPMATS actions per the same 
        	// full grant number.  But that's not an especially quick undertaking.
        } else if (formGrants != null && formGrants.size() == 1) {
        	grant = (FormGrantProxy) formGrantsProxies.get(0);
        }
        
        return grant;
    }

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

        logger.debug("About to retrieve form");
        form = greensheetFormService.getGreensheetForm(grant, group);
        logger.debug("form: " + form);
        logger.debug("getForm() End");

        return form;
    }
}
