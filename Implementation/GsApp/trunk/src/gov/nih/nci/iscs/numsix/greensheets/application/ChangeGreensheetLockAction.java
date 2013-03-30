/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * Action changes the lock on a greensheet thereby changing the state. Requires the following values to be passed in. <li>grantId: this is the full
 * grant number</> <li>groupType: this the grouptype of the greensheet to be unlocked. Usually either PGM or SPEC</li> <li>userId: Oracle userId of
 * the user</li>
 * 
 * @author kpuscas, Number Six Software
 */
public class ChangeGreensheetLockAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(ChangeGreensheetLockAction.class);

    private static EmailNotification emailHelper; // must be static or else, even though Spring injects it
	// at web app startup, by the time execute() runs it is null because the instance of this Action 
	// class when execute() runs is a different one, not the one that was created by Spring at startup...
    // WHICH IS BECAUSE WE DON'T HAVE STRUTS1-SPRING INTEGRATION SET UP AT ALL!
    
    static GreensheetsFormGrantsService greensheetsFormGrantsService;

    public GreensheetsFormGrantsService getGreensheetsFormGrantsService() {
        return greensheetsFormGrantsService;
    }

    public void setGreensheetsFormGrantsService(
            GreensheetsFormGrantsService greensheetsFormGrantsService) {
        this.greensheetsFormGrantsService = greensheetsFormGrantsService;
    }

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "sessionTimeOut";

        } else {

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            GsUser user = gus.getUser();

            DynaActionForm form = (DynaActionForm) aForm;
            String grantId = (String) form.get("grantId");
            String groupType = (String) form.get("groupType");
            FormGrantProxy grant = null;

            // GsGrant grant = gus.getGrantByGrantNumber(grantId); //Abdul
            // Latheef: Used new FormGrantProxy instead of the old GsGrant.
            // GsGrant grant = gus.getGrantByGrantNumber(grantId);
            // Abdul Latheef: Go to the DB until some workaround is devised or
            // the DAO
            // or the DAO is rewritten to return a Map instead of List.
            // ApplicationContext context = new
            // ClassPathXmlApplicationContext("applicationContext.xml");
            // GreensheetsFormGrantsService greensheetsFormGrantsService =
            // (GreensheetsFormGrantsService)
            // context.getBean("greensheetsFormGrantsService");
            List formGrants = null;
            List formGrantsProxies = null;

            if (grantId != null && (!"".equals(grantId.trim()))) {
                formGrants = greensheetsFormGrantsService.retrieveGrantsByFullGrantNum(grantId.trim());
                
//                if(formGrants.size()>1){
//                    return mapping.findForward("duplicateGreensheetsError"); TODO: remove that mapping and JSP?
//                }
                formGrantsProxies = GreensheetActionHelper
                        .getFormGrantProxyList(formGrants, gus.getUser());
            }
            if (formGrantsProxies!=null && formGrantsProxies.size() > 1) {
    			logger.error("\n\t!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    			StringBuffer msgText = new StringBuffer();
    			msgText.append("In 'changelock' action, method execute(), ").append(
    					"with parameters \n\tfull grant number ");
    			msgText.append(grantId).append(
    					", \n\tgreensheet type = " + groupType + ", more than one grant met the criteria, likely ")
    					.append("meaning there are two GPMATS actions with the same EXPECTED_GRANT_NUM.");
    			msgText.append("\n\tThis is not normal, and OGA probably should be contacted to delete the ")
    				.append("extra action(s). However, the user of Greensheets is probably able to continue.");
    			logger.error("\t" + msgText);
    			logger.error("\n\t!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    			if (emailHelper!=null) {
    				emailHelper.sendTextToSupportEmail(msgText);
    			}
    			grant = (FormGrantProxy) formGrantsProxies.get(0); // TODO: BAD!!! - some time in the future 
    				// we should change the data model to support multiple GPMATS actions per the same 
    				// full grant number.  But that's not an especially quick undertaking.            	
            }
            else if (formGrantsProxies != null && formGrantsProxies.size() == 1) {
                grant = (FormGrantProxy) formGrantsProxies.get(0);
            } else {
                grant = null;
            }

            if (user.getRole().equals(GsUserRole.SPEC)) {

                // GreensheetFormMgr mgr = GreensheetMgrFactory //Abdul Latheef:
                // Used new FormGrantProxy instead of the old GsGrant.
                // .createGreensheetFormMgr(GreensheetMgrFactory.PROD);
                GreensheetFormMgr mgr = new GreensheetFormMgrImpl(); // For time
                                                                     // being
                                                                     // --
                                                                     // Abdul
                                                                     // Latheef

                GreensheetFormProxy gForm = mgr.findGreensheetForGrant(grant,
                        GreensheetGroupType.getGreensheetGroupType(groupType));

                mgr.changeLock(gForm, user);

                forward = "retrievegrants";
            } else {
                throw new GreensheetBaseException("The user "
                        + user.getDisplayUserName()
                        + " does not have permission to unlock this greensheet");
            }

        }

        return mapping.findForward(forward);

    }

	public static EmailNotification getEmailHelper() {
		return emailHelper;
	}

	public void setEmailHelper(EmailNotification emailHelper) {
		this.emailHelper = emailHelper;
	}

}
