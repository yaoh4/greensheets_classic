package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetForm;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
	public ActionForward execute(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		try {
			GreensheetUserMgr userMgr = GreensheetMgrFactory
					.createGreensheetUserMgr(GreensheetMgrFactory.PROD);
			GsUser user = userMgr.findUserByUserName("gs_pd");
			if (user == null) {
				throw new Exception("Error in finding Test User");
			}
			GrantMgr grantMgr = GreensheetMgrFactory
					.createGrantMgr(GreensheetMgrFactory.PROD);
			Map grantsMap = grantMgr.getGrantsListForUser(user, true, true);
			if (grantsMap.size() <= 0) {
				throw new Exception("Error in Retrieving Users Grants List");
			}
			GsGrant grant = (GsGrant) ((grantsMap.values().toArray())[0]);
			GreensheetFormMgr formMgr = GreensheetMgrFactory
					.createGreensheetFormMgr(GreensheetMgrFactory.PROD);
			GreensheetForm form = formMgr.findGreensheetForGrant(grant,
					GreensheetGroupType.PGM);
			if (form == null) {
				throw new Exception("Error in Retrieving Greensheet Form");
			}

		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e
					.getMessage());
		}

		resp
				.sendError(
						200,
						"\nUser Test Passed \n Retrieve Grants Test Passed \n Retrieve Greensheet Passed");
		return null;
	}

}