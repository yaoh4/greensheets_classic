package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author kpuscas, Number Six Software
 */
public class SearchForGrantAction extends GsBaseAction {

    static GreensheetsFormGrantsService greensheetsFormGrantsService;

    public GreensheetsFormGrantsService getGreensheetsFormGrantsService() {
        return greensheetsFormGrantsService;
    }

    public void setGreensheetsFormGrantsService(
            GreensheetsFormGrantsService greensheetsFormGrantsService) {
        this.greensheetsFormGrantsService = greensheetsFormGrantsService;
    }

    private static final Logger logger = Logger
            .getLogger(SearchForGrantAction.class);

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

            GreensheetActionHelper.setPaylineOption(req, gus);

            // GrantMgr mgr = GreensheetMgrFactory //Abdul Latheef: Used new
            // FormGrantProxy instead of the old GsGrant.GsGrant;
            // .createGrantMgr(GreensheetMgrFactory.PROD);

            DynaActionForm form = (DynaActionForm) aForm;

            String searchType = (String) form.get("searchType");
            String searchText = (String) form.get("searchText");

            GsUser gsUser = gus.getUser();
            List formGrants = new ArrayList(); // See if this is the right way
            // of initializing the list.
            // ApplicationContext context = new
            // ClassPathXmlApplicationContext("applicationContext.xml");
            // GreensheetsFormGrantsService greensheetsFormGrantsService =
            // (GreensheetsFormGrantsService)
            // context.getBean("greensheetsFormGrantsService");
            if (searchType.trim().equalsIgnoreCase(
                    GreensheetsKeys.SEARCH_GS_NUMBER)) {
                formGrants = greensheetsFormGrantsService
                        .findGrantsByGrantNum(searchText);
            } else if (searchType.trim().equalsIgnoreCase(
                    GreensheetsKeys.SEARCH_GS_NAME)) {
                if (gsUser.getRole().equals(GsUserRole.PGM_DIR)
                        || gsUser.getRole().equals(GsUserRole.PGM_ANL)) {
                    formGrants = greensheetsFormGrantsService
                            .findGrantsByPiLastName(searchText, "CA");
                } else {
                    formGrants = greensheetsFormGrantsService
                            .findGrantsByPiLastName(searchText, null);
                }
            }

            List<FormGrantProxy> list = GreensheetActionHelper
                    .getFormGrantProxyList(formGrants, gsUser);

            // Map map = mgr.searchForGrant(searchType, searchText,
            // gus.getUser());
            //
            // gus.setGrants(map);
            //
            // List list =
            // GreensheetActionHelper.getGrantGreensheetProxyList(map,
            // gus.getUser());

            req.getSession().setAttribute("GRANT_LIST", list);

            logger.debug(gus.getUser().getRoleAsString());

            // Logic removed 25.05.2006, see #4016, A.Angelo
            // if (gus.getUser().getRole().equals(GsUserRole.PGM_DIR)
            // || gus.getUser().getRole().equals(GsUserRole.PGM_ANL)) {
            // req.setAttribute("SEARCH_RESULTS", "true");
            // forward = "programGrantsList";
            // }

            if (gus.getUser().getRole().equals(GsUserRole.SPEC)) {
                req.setAttribute("SEARCH_RESULTS", "true");
                forward = "specialistGrantsList";
            } else if (gus.getUser().getRole().equals(GsUserRole.GS_GUEST)) {
                req.setAttribute("SEARCH_RESULTS", "true");
                forward = "guestUserGrantsList";
            }
        }

        logger.debug(forward);
        return mapping.findForward(forward);
    }

}
