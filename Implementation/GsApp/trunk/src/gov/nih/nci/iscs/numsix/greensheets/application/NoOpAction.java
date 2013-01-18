/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * No-Op action needed for sorting of grants list
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class NoOpAction extends GsBaseAction {

	private static final Logger logger = Logger.getLogger(NoOpAction.class);

	/**
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm arg1,
			HttpServletRequest req, HttpServletResponse arg3) throws Exception {

		String forward = null;

		if (req.getSession().isNew()) {
			forward = "sessionTimeOut";

		} else {

			GreensheetUserSession gus = (GreensheetUserSession) GreensheetActionHelper
					.getGreensheetUserSession(req);

			GreensheetActionHelper.setPaylineOption(req, gus);
			GreensheetActionHelper.setMyPortfolioOption(req, gus);

			if (gus.getUser().getMyPortfolioIds() != null) {
				req.setAttribute("MY_PORTFOLIO_OPT", "true");
			}

			if (gus.getUser().getRole().equals(GsUserRole.PGM_DIR)
					|| gus.getUser().getRole().equals(GsUserRole.PGM_ANL)) {
				forward = "programGrantsList";
			} else if (gus.getUser().getRole().equals(GsUserRole.SPEC)) {
				forward = "specialistGrantsList";
			}
		}

		return mapping.findForward(forward);
	}

}
