package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;
import java.util.*;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;

/**
 *
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class SearchForGrantAction extends GsBaseAction {

    private static final Logger logger = Logger.getLogger(SearchForGrantAction.class);
    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        String forward = null;

        if (req.getSession().isNew()) {
            forward = "sessionTimeOut";

        } else {

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
            
            GreensheetActionHelper.setPaylineOption(req, gus);

            GrantMgr mgr = GreensheetMgrFactory.createGrantMgr(GreensheetMgrFactory.PROD);

            DynaActionForm form = (DynaActionForm) aForm;

            String searchType = (String) form.get("searchType");
            String searchText = (String) form.get("searchText");


            Map map = mgr.searchForGrant(searchType, searchText, gus.getUser());

            gus.setGrants(map);

            List list = GreensheetActionHelper.getGrantGreensheetProxyList(map, gus.getUser());

            req.getSession().setAttribute("GRANT_LIST", list);
            
            logger.debug(gus.getUser().getRoleAsString());
            
            if (gus.getUser().getRole().equals(GsUserRole.PGM_DIR) || gus.getUser().getRole().equals(GsUserRole.PGM_ANL)) {
                req.setAttribute("SEARCH_RESULTS","true");
                forward = "programGrantsList";
            } else if (gus.getUser().getRole().equals(GsUserRole.SPEC)) {
                req.setAttribute("SEARCH_RESULTS","true");
                forward = "specialistGrantsList";
            }else if(gus.getUser().getRole().equals(GsUserRole.GS_GUEST)){
                req.setAttribute("SEARCH_RESULTS","true");
                forward = "guestUserGrantsList";
            }
        }
        
        logger.debug(forward);
        return mapping.findForward(forward);
    }

}
