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
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr.*;

import java.util.*;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;

/**
 * Action retrieves a list of grants assigned to the user
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class RetrieveUsersGrantsAction extends GsBaseAction {

	private static final Logger logger = Logger
			.getLogger(RetrieveUsersGrantsAction.class);

	/**
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */

	public ActionForward execute(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		GrantMgr gMgr = GreensheetMgrFactory
				.createGrantMgr(GreensheetMgrFactory.PROD);
		String grantList = null;

		// Check for session time out
		if (req.getSession().isNew()) {
			// set forward name
			grantList = "sessionTimeOut";
		} else {
			HttpSession httpSession = req.getSession();
			GreensheetUserSession gus = GreensheetActionHelper
					.getGreensheetUserSession(req);
			GsUser gsUser = gus.getUser();
			GsUserRole gsUserRole = gsUser.getRole();

			//	If "role" = "Guest"
			if (gsUserRole.equals((GsUserRole.GS_GUEST))) {
				//	set forward name
				grantList = "guestUserView";
			}

			//	If "role" = "Specialist"
			if (gsUserRole.equals(GsUserRole.SPEC)) {
				GreensheetActionHelper.setPaylineOption(req, gus);
				GreensheetActionHelper.setMyPortfolioOption(req, gus);
				Map map = gMgr.getGrantsListForUser(gus.getUser(), gus
						.isPaylineOnly(), gus.isMyPortfolio());
				gus.setGrants(map);
				List list = GreensheetActionHelper.getGrantGreensheetProxyList(
						map, gus.getUser());
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
