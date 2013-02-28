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
                
                if(formGrants.size()>1){
                    return mapping.findForward("duplicateGreensheetsError");
                }
                formGrantsProxies = GreensheetActionHelper
                        .getFormGrantProxyList(formGrants, gus.getUser());
            }
            if (formGrantsProxies != null && formGrantsProxies.size() > 0) {
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

}
