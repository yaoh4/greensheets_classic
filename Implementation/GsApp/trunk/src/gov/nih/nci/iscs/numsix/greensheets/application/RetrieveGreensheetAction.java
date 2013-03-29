/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormService;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;

import java.util.ArrayList;
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
    
    private static EmailNotification emailHelper;  // must be static or else, even though Spring injects it
    	// at web app startup, by the time execute() runs it is null because the instance of this Action 
    	// class when execute() runs is a different one, not the one that was created by Spring at startup...

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
                if (gsform.getStatus() == null) {
                    String invalidFormStatusMsg = "Invalid form status detected on greensheet for grant " + grant.getFullGrantNum() + " (appl_id "
                            + grant.getApplID() + ", form id " + gsform.getFormId() + "). Please contact support to resolve.";
                    req.getSession().setAttribute("invalidFormStatusMsg", invalidFormStatusMsg);
                    return mapping.findForward("invalidFormStatus");
                }
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

                try {
                    GreensheetActionHelper.setFormDisplayInfo(req, id);
                } catch (GreensheetBaseException e) {
                    if (e.getMessage().contains("sessionTimeOut"))
                        return mapping.findForward("sessionTimeOut");
                }

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
        if (applId == null || "".equals(applId)) {
        	Object applIdObj = req.getAttribute(GreensheetsKeys.KEY_APPL_ID); 
        	if (applIdObj!=null && applIdObj instanceof Long) {
        		applId = ((Long)applIdObj).toString();
        	}
        	else if(applIdObj!=null && applIdObj instanceof String) {
        		applId = (String)applIdObj;
        	}
        }
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
//            // Abdul Latheef (For GPMATS): Retrieve the grant by the APPL ID
//            if ((applId != null) && !(applId.trim().equalsIgnoreCase(""))) {
//                formGrants = greensheetsFormGrantsService
//                        .findGrantsByApplId(Long.parseLong(applId));
//                formGrantsProxies = GreensheetActionHelper
//                        .getFormGrantProxyList(formGrants, gus.getUser());
//            }
//
//            // Next, try retrieving the grant by the Grant#
//            if (formGrantsProxies == null || formGrantsProxies.size() < 1
//                    || formGrantsProxies.size() > 1) {
//                if (grantId != null && (!"".equals(grantId.trim()))) {
//                    formGrants = greensheetsFormGrantsService
//                            .retrieveGrantsByFullGrantNum(grantId.trim());
//                    formGrantsProxies = GreensheetActionHelper
//                            .getFormGrantProxyList(formGrants, gus.getUser());
//                }
//            }
//
//            if (formGrantsProxies != null && formGrantsProxies.size() > 0) {
//                grant = (FormGrantProxy) formGrantsProxies.get(0);
//            } else {
//                grant = null;
//            }
            String group = req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
            if (group == null || "".equals(group)) {
            	group = (String) req.getAttribute(GreensheetsKeys.KEY_GS_GROUP_TYPE);
            }
            if (group != null)  { group = group.trim(); }
            if (group == null || "".equals(group)) {
            	GreensheetBaseException newException = new GreensheetBaseException("The request to retrieve " +
            			"a greensheet that you submitted does not specify what kind of greensheet (e.g., " +
            			"Program or Specialist or OGA Document Management), and therefore it cannot be completed.");
            	throw newException;
            }
            else if (group.equalsIgnoreCase(GreensheetGroupType.DM.getName())) {
            	if ((applId==null || "".equals(applId)) && grantId!=null && !"".equals(grantId)) {
            		// GPMATS determined that this is a dummy grant, formed the URL with grant number 
            		// rather than appl_id
            		formGrants = greensheetsFormGrantsService.retrieveGrantsByFullGrantNum(grantId);
            		formGrantsProxies = GreensheetActionHelper.getFormGrantProxyList(formGrants, gus.getUser());
            		if (formGrants!=null && formGrants.size() > 0) {
            			for (Object oneGrant : formGrants) {
            				if ( ((FormGrant) oneGrant).isDummy() == false ) {
            					logger.error("Grant " + grantId + " should be dummy per the URL formed by GPMATS, " +
            							"but isn't dummy per the database. Greensheets will \"force\" treating it as dummy.");
            					((FormGrant) oneGrant).setDummy("Y");
            				}
            			}
            		}
            	}
            	else if ((grantId==null || "".equals(grantId)) && applId!=null && !"".equals(applId)) {
            		// GPMATS determined that this is a real, non-dummy, grant and formed the URL 
            		// with appl_id rather than grant number
            		formGrants = greensheetsFormGrantsService.findGrantsByApplId(Long.parseLong(applId));
            		if (formGrants!=null && formGrants.size() > 0) {
            			List nonDummyFormGrants = new ArrayList();
            			for ( Object oneGrant : formGrants ) {
            				if ( ((FormGrant)oneGrant).isDummy() ) {
            					logger.info("Grant with appl_id " + applId + " should be a \"real\" grant \n\t" +
            							"per the URL formed by GPMATS, but there is also a dummy grant with \n\t" +
            							"the same appl_id in the database. So we will NOT ADD it to the list \n\t" +
            							"of non-dummy grants that we will use as the utimate list.");
            				}
            				else {
            					nonDummyFormGrants.add(oneGrant);
            				}
            			}
            			formGrants = nonDummyFormGrants; // replacing the original list, which includes all 
            				// entries with the same appl_id, with the "filtered" list that should only have 
            				// one, non-dummy, grant. 
            			nonDummyFormGrants = null;
            		}
            		formGrantsProxies = GreensheetActionHelper.getFormGrantProxyList(formGrants, gus.getUser());
            	}
            	else {
            		GreensheetBaseException newException = new GreensheetBaseException("Your request to " +
            				"retrieve a DM checklist provides invalid parameters and cannot be completed. " +
            				"Please contact support.");
            		throw newException;
            	}
            }
            else if (group.equalsIgnoreCase(GreensheetGroupType.PGM.getName()) || 
            		group.equalsIgnoreCase(GreensheetGroupType.SPEC.getName())) {
            	// Request parameters apparently always include BOTH appl_id and grant number; because a 
            	// query by appl_id might return two or even more grants (real plus dummy(ies)), we should 
            	// always get the grant by the full grant number and disregard the appl_id
        		formGrants = greensheetsFormGrantsService.retrieveGrantsByFullGrantNum(grantId);
        		formGrantsProxies = GreensheetActionHelper.getFormGrantProxyList(formGrants, gus.getUser());            	
            }
            else {
            	GreensheetBaseException newException = new GreensheetBaseException("The request to retrieve " +
            			"a greensheet that you submitted specifies an invalid type of greensheet (it should be " +
            			"Program or Specialist or OGA Document Management), and therefore it cannot be completed.");
            	throw newException;            	
            }
    		if (formGrants!=null && formGrants.size() > 1) {
    			/*
    			GreensheetBaseException newException = new GreensheetBaseException("Your request " +
    					"to retrieve a " + group + " greensheet for " +
    					grantId + " (appl_id " + applId + ") unexpectedly found more than one grant " +
    					"with this grant number, which is invalid. Please contact support.");
    			throw newException;
    			*/
    			logger.error("\n\t!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    			StringBuffer msgText = new StringBuffer();
    			msgText.append("In 'retrievegreensheet' action, method getGrant(), ").append(
    					"with parameters \n\tappl_id = ").append(applId).append(", full grant number ");
    			msgText.append(grantId).append(
    					", \n\tgreensheet type = " + group + ", more than one grant met the criteria, ")
    					.append("likely meaning there are two GPMATS actions with the same EXPECTED_GRANT_NUM.");
    			msgText.append("\n\tThis is not normal, and OGA probably should be contacted to delete the ")
    				.append("extra actions. However, the user of Greensheets is probably able to continue.");
    			logger.error("\t" + msgText);
    			logger.error("\n\t!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    			if (emailHelper!=null) {
    				emailHelper.sendTextToSupportEmail(msgText);
    			}
    			grant = (FormGrantProxy) formGrantsProxies.get(0); // TODO: BAD!!! - some time in the future 
    				// we should change the data model to support multiple GPMATS actions per the same 
    				// full grant number.  But that's not an especially quick undertaking.
    		}
    		else if (formGrants!=null && formGrants.size() == 1) {
    			grant = (FormGrantProxy) formGrantsProxies.get(0);
    		}
        }
//        String group = req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
//        if (group != null && "DM".equalsIgnoreCase(group.trim())) {
//            if ((applId != null) && !(applId.trim().equalsIgnoreCase(""))) {
//                grant.setDummy("N");
//            }
//        }

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
