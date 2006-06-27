/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetForm;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * Action changes the lock on a greensheet thereby changing the state. Requires
 * the following values to be passed in.
 * <li>grantId: this is the full grant number</>
 * <li>groupType: this the grouptype of the greensheet to be unlocked. Usually
 * either PGM or SPEC</li>
 * <li>userId: Oracle userId of the user</li>
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class ChangeGreensheetLockAction extends GsBaseAction {

	private static final Logger logger = Logger
			.getLogger(ChangeGreensheetLockAction.class);

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
			GsUser user = gus.getUser();

			DynaActionForm form = (DynaActionForm) aForm;
			String grantId = (String) form.get("grantId");
			String groupType = (String) form.get("groupType");

			GsGrant grant = gus.getGrantByGrantNumber(grantId);

			if (user.getRole().equals(GsUserRole.SPEC)) {

				GreensheetFormMgr mgr = GreensheetMgrFactory
						.createGreensheetFormMgr(GreensheetMgrFactory.PROD);

				GreensheetForm gForm = mgr.findGreensheetForGrant(grant,
						GreensheetGroupType.getGreensheetGroupType(groupType));

				mgr.changeLock(gForm, user);

				forward = "retrievegrants";
			} else {
				throw new GreensheetBaseException("The user "
						+ user.getDisplayUserName()
						+ " does not have permission to unlock this greensheet");
			}

		}

		return mapping.findForward(forward);

	}

}
