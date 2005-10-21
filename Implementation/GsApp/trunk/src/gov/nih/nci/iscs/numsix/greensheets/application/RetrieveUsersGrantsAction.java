/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionAttachment;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;
import java.util.*;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;

/**
 * Action retrieves a list of grants assigned to the user
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
public class RetrieveUsersGrantsAction extends GsBaseAction {

    private static final Logger logger = Logger.getLogger(RetrieveUsersGrantsAction.class);
    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm aForm, HttpServletRequest req, HttpServletResponse resp)
        throws Exception {

        GrantMgr gMgr = GreensheetMgrFactory.createGrantMgr(GreensheetMgrFactory.PROD);
        String grantList = null;

        if (req.getSession().isNew()) {
            grantList = "sessionTimeOut";

        } else {

            GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);

            GreensheetActionHelper.setPaylineOption(req, gus);
            GreensheetActionHelper.setMyPortfolioOption(req, gus);
/*
// Starts with prefs
            System.out.println("PREFS START");
            HttpSession session = req.getSession();
            Map userPrefs = (Map) session.getAttribute("preferences"); 
                   
            if(userPrefs != null) {
                System.out.println("Count of Prefs = " + userPrefs.size());
                                                           
                Iterator iter = userPrefs.entrySet().iterator(); 
               	while(iter.hasNext()) {	       	
               	 Map.Entry mapEntry = (Map.Entry) iter.next();
               	 System.out.println(mapEntry.getKey() + "  = " + mapEntry.getValue());
               	}
            } 
            else {
                System.out.println("User Prefs IS NULL"); 
            } 
             
            System.out.println("PREFS END");
// End of prefs            
      */      
             
            
            if (gus.getUser().getRole().equals(GsUserRole.GS_GUEST)) { 
                grantList = "guestUserView";
            } else {
 
                Map map = gMgr.getGrantsListForUser(gus.getUser(), gus.isPaylineOnly(), gus.isMyPortfolio());
 
                gus.setGrants(map);

                List list = GreensheetActionHelper.getGrantGreensheetProxyList(map, gus.getUser());

                req.getSession().setAttribute("GRANT_LIST", list);
                
                if (gus.getUser().getMyPortfolioIds() != null) {
                    req.setAttribute("MY_PORTFOLIO_OPT", "true");
                }

                if (gus.getUser().getRole().equals(GsUserRole.PGM_DIR) || gus.getUser().getRole().equals(GsUserRole.PGM_ANL)) {
                    grantList = "programGrantsList";
                } else if (gus.getUser().getRole().equals(GsUserRole.SPEC)) {
                    grantList = "specialistGrantsList";
                }
            }
        }

        return mapping.findForward(grantList);

    }

}
