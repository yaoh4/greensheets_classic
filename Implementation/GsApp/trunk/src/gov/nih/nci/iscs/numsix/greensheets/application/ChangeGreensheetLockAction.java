/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;

import javax.servlet.http.*;
import org.apache.log4j.*;
import org.apache.struts.action.*;

/**
 *  Action changes the lock on a greensheet thereby changing the state. 
 *  Requires the following values to be passed in. 
 *  <li>grantId: this is the full grant number</>
 *  <li>groupType: this the grouptype of the greensheet to be unlocked. Usually either
 *      PGM or SPEC</li>
 *  <li>userId: Oracle userId of the user</li>
 *  
 * 
 *  @author kpuscas, Number Six Software
 */
public class ChangeGreensheetLockAction extends GsBaseAction {

    private static final Logger logger = Logger.getLogger(ChangeGreensheetLockAction.class);

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
            GsUser user = gus.getUser();

            DynaActionForm form = (DynaActionForm) aForm;
            String grantId = (String) form.get("grantId");
            String groupType = (String) form.get("groupType");

            GsGrant grant = gus.getGrantByGrantNumber(grantId);

            if (user.getRole().equals(GsUserRole.SPEC)) {

                GreensheetFormMgr mgr = GreensheetMgrFactory.createGreensheetFormMgr(GreensheetMgrFactory.PROD);

                GreensheetForm gForm = mgr.findGreensheetForGrant(grant, GreensheetGroupType.getGreensheetGroupType(groupType));

                mgr.changeLock(gForm, user);

                forward = "retrievegrants";
            } else {
                throw new GreensheetBaseException(
                    "The user " + user.getDisplayUserName() + " does not have permission to unlock this greensheet");
            }

        }

        return mapping.findForward(forward);

    }

}
