/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * Action retrieves a list of grants assigned to the user
 * 
 * @author kpuscas, Number Six Software
 */
public class RetrieveUsersGrantsAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(RetrieveUsersGrantsAction.class);

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

        // GrantMgr gMgr = GreensheetMgrFactory //Abdul Latheef: Used new
        // FormGrantProxy instead of the old GsGrant.GsGrant;
        // .createGrantMgr(GreensheetMgrFactory.PROD);
        String grantList = null;

        // Check for session time out
        if (req.getSession().isNew()) {
            // set forward name
            grantList = "sessionTimeOut";   // Why??? It should be still legit for users to request the
            	// URL mapped to this action directly, as the very first request of their session...
        } else {
            HttpSession httpSession = req.getSession();
            DynaActionForm form = (DynaActionForm) aForm;
            if (form != null) {
                if (form.get("newUserName") != null) {
                    String newUserName = (String) form.get("newUserName");
                    req.setAttribute(GreensheetsKeys.NEW_USER_ID, newUserName.trim());
                }
            }
        	
            req.getSession().removeAttribute(GreensheetsKeys.USER_NOT_FOUND);

            GreensheetUserSession gus = GreensheetActionHelper
                    .getGreensheetUserSession(req);
            if (gus == null) {
                req.getSession().setAttribute(GreensheetsKeys.USER_NOT_FOUND, "User Not Found");
                return mapping.findForward("changeUser");
            }

            GsUser gsUser = gus.getUser();
            GsUserRole gsUserRole = gsUser.getRole();

            // If "role" = "Guest"
            if (gsUserRole.equals((GsUserRole.GS_GUEST))) {
                // set forward name
                grantList = "guestUserView";
            }

            // If "role" = "Specialist"
            if (gsUserRole.equals(GsUserRole.SPEC)) {
                GreensheetActionHelper.setPaylineOption(req, gus);
                GreensheetActionHelper.setMyPortfolioOption(req, gus);

                List formGrants = greensheetsFormGrantsService.findGrants(
                        gsUser.getOracleId(), true, true, true);
                List<FormGrantProxy> list = GreensheetActionHelper
                        .getFormGrantProxyList(formGrants, gus.getUser());
                req.getSession().setAttribute("GRANT_LIST", list);
                if (gus.getUser().getMyPortfolioIds() != null) {
                    req.setAttribute("MY_PORTFOLIO_OPT", "true");
                }
                // set forward name
                grantList = "specialistGrantsList";
            }

            // If "role" = "Program" or "Analyst"
            if (gsUserRole.equals(GsUserRole.PGM_DIR)
                    || gsUserRole.equals(GsUserRole.PGM_ANL)) {
                // set forward name
                grantList = "programGrantsList";
            }
        }
        return mapping.findForward(grantList);
    }
}
