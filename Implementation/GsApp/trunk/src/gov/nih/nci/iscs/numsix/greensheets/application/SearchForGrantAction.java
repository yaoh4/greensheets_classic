package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class SearchForGrantAction extends GsBaseAction {

	private static final Logger logger = Logger
			.getLogger(SearchForGrantAction.class);

	/**
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
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

			GrantMgr mgr = GreensheetMgrFactory
					.createGrantMgr(GreensheetMgrFactory.PROD);

			DynaActionForm form = (DynaActionForm) aForm;

			String searchType = (String) form.get("searchType");
			String searchText = (String) form.get("searchText");

			Map map = mgr.searchForGrant(searchType, searchText, gus.getUser());

			gus.setGrants(map);

			List list = GreensheetActionHelper.getGrantGreensheetProxyList(map,
					gus.getUser());

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
