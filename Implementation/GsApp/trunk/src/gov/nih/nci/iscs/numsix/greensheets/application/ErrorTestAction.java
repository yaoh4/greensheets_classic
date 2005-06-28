package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;

/**
 * 
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class ErrorTestAction extends Action {

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
     *      HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
            throws Exception {

        try {
            GreensheetUserMgr userMgr = GreensheetMgrFactory.createGreensheetUserMgr(GreensheetMgrFactory.PROD);
            GsUser user = userMgr.findUserByUserName("gs_pd");
            if (user == null) {
                throw new Exception("Error in finding Test User");
            }
            GrantMgr grantMgr = GreensheetMgrFactory.createGrantMgr(GreensheetMgrFactory.PROD);
            Map grantsMap = grantMgr.getGrantsListForUser(user, true, true);
            if (grantsMap.size() <= 0) {
                throw new Exception("Error in Retrieving Users Grants List");
            }
            GsGrant grant = (GsGrant) ((grantsMap.values().toArray())[0]);
            GreensheetFormMgr formMgr = GreensheetMgrFactory.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
            GreensheetForm form = formMgr.findGreensheetForGrant(grant, GreensheetGroupType.PGM);
            if (form == null) {
                throw new Exception("Error in Retrieving Greensheet Form");
            }

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

        resp.sendError(200, "\nUser Test Passed \n Retrieve Grants Test Passed \n Retrieve Greensheet Passed");
        return null;
    }

}