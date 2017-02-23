/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.cbiit.scimgmt.common.bdo.UserRecord;
import gov.nih.nci.cbiit.scimgmt.common.service.iface.IAuthorizationService;
import gov.nih.nci.cbiit.scimgmt.common.service.iface.IChangeUserService;
import gov.nih.nci.cbiit.scimgmt.common.service.impl.ChangeUserServiceImpl;
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

    IChangeUserService changeUserService;
    IAuthorizationService authorizationService;

    private String firstName;
    private String lastName;
    private String method;
    private String selectedUser;

    private List<UserRecord> foundUsers;

    public GreensheetsFormGrantsService getGreensheetsFormGrantsService() {
        return greensheetsFormGrantsService;
    }

    public void setGreensheetsFormGrantsService(
            GreensheetsFormGrantsService greensheetsFormGrantsService) {
        this.greensheetsFormGrantsService = greensheetsFormGrantsService;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // GrantMgr gMgr = GreensheetMgrFactory //Abdul Latheef: Used new
        // FormGrantProxy instead of the old GsGrant.GsGrant;
        // .createGrantMgr(GreensheetMgrFactory.PROD);
        String grantList = null;

        HttpSession httpSession = req.getSession();

        DynaActionForm form = (DynaActionForm) aForm;

        if (form != null) {
            String method = (String) form.get("method");
            if (method.equalsIgnoreCase("searchUser")) {
                firstName = (String) form.get("firstName");
                lastName = (String) form.get("lastName");
                foundUsers = getChangeUserService().searchUser(firstName.trim(), lastName.trim());
                req.getSession().setAttribute("foundUsers", foundUsers);
                return mapping.findForward("changeUser");
            }

            if (method.equalsIgnoreCase("setUser")) {
                selectedUser = (String) form.get("selectedUser");
                req.setAttribute(GreensheetsKeys.NEW_USER_ID, selectedUser.trim());
                req.getSession().removeAttribute("foundUsers");
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
        if (gsUserRole.equals((GsUserRole.GS_GUEST))) {
            // set forward name
            grantList = "guestUserView";
        }

        // If "role" = "Specialist"
        if (gsUserRole.equals(GsUserRole.SPEC)) {
            GreensheetActionHelper.setPaylineOption(req, gus);
            GreensheetActionHelper.setMyPortfolioOption(req, gus);

            List formGrants = greensheetsFormGrantsService.findGrants(
                        gsUser, true, true, true, true);
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
        /*  this was the end of the block "do this if the session is not new"
            	}
        */
        return mapping.findForward(grantList);
    }

    public List<UserRecord> getFoundUsers() {
        return foundUsers;
    }

    public void setFoundUsers(List<UserRecord> foundUsers) {
        this.foundUsers = foundUsers;
    }

    public IChangeUserService getChangeUserService() {
        ChangeUserServiceImpl changeUserServiceImpl = new ChangeUserServiceImpl();
        return changeUserServiceImpl;
    }

    public void setChangeUserService(IChangeUserService changeUserService) {
        this.changeUserService = changeUserService;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

}
