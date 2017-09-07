/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormService;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetsFormGrantsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

/**
 * Action changes the lock on a greensheet thereby changing the state. Requires the following values to be passed in. <li>grantId: this is the full
 * grant number</> <li>groupType: this the grouptype of the greensheet to be unlocked. Usually either PGM or SPEC</li> <li>userId: Oracle userId of
 * the user</li>
 * 
 * @author kpuscas, Number Six Software
 */
public class ChangeGreensheetLockAction extends GsBaseAction {

    private static final Logger logger = Logger.getLogger(ChangeGreensheetLockAction.class);
   
    static GreensheetsFormGrantsService greensheetsFormGrantsService;
    static GreensheetFormService greensheetFormService;

    public GreensheetsFormGrantsService getGreensheetsFormGrantsService() {
        return greensheetsFormGrantsService;
    }

    public void setGreensheetsFormGrantsService(
            GreensheetsFormGrantsService greensheetsFormGrantsService) {
        this.greensheetsFormGrantsService = greensheetsFormGrantsService;
    }

    public GreensheetFormService getGreensheetFormService() {
        return greensheetFormService;
    }

    public void setGreensheetFormService(
            GreensheetFormService greensheetFormService) {
        this.greensheetFormService = greensheetFormService;
    }

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm aForm,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	
    	String forward = null;
        String groupType = (String) req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
        resp.setStatus(Constants.BAD_REQUEST_STATUS); 
        
        if(groupType != null && groupType.equals(Constants.REVISION_TYPE)) {
        	boolean status = changeLockExternally(req);

        	if(status) resp.setStatus(Constants.OK_STATUS); 
        }
        else {
        	forward = changeLockInternally(req, aForm);
        	resp.setStatus(Constants.OK_STATUS);
        }
        
        return mapping.findForward(forward);
    }
    
    private boolean changeLockExternally(HttpServletRequest req) {
    	FormGrantProxy grant = null;
    	boolean status = false;

    	try {
    		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
    		GsUser user = gus.getUser();

    		if (user.getRole().equals(GsUserRole.SPEC)) {
    			String groupType = (String) req.getParameter(GreensheetsKeys.KEY_GS_GROUP_TYPE);
    			String actionId = (String) req.getParameter(GreensheetsKeys.KEY_AGT_ID);

    			if(actionId != null && groupType != null && !actionId.isEmpty()) {
    				grant = greensheetsFormGrantsService.findGSGrantInfo(Long.parseLong(actionId), null, groupType, gus.getUser());

    				GreensheetFormProxy gForm = greensheetFormService.getGreensheetForm(grant, groupType);
    				if(gForm != null) {
    					status = new GreensheetFormMgrImpl().changeLock(gForm, user);
    				}
    				else {
    					logger.error("There is no greensheet form for Grant: " + grant + ", Group: " + groupType);	    			
    				}
    			}
    		}
    	}
    	catch(Exception e) {
    		logger.error("There is a problem with lock/unlock functionality", e);
    	}

    	return status;
    }
    
    private String changeLockInternally(HttpServletRequest req, ActionForm aForm) throws Exception {
    	String forward = null;

    	if (req.getSession().isNew()) {
    		forward = "sessionTimeOut";

    	} else {
    		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
    		GsUser user = gus.getUser();

    		if (user.getRole().equals(GsUserRole.SPEC)) {
    			DynaActionForm form = (DynaActionForm) aForm;
    			String groupType = (String) form.get("groupType");
    			String applId = (String) form.get("applId");

    			FormGrantProxy grant = null;

    			if (applId != null && !applId.isEmpty() && groupType != null && !groupType.isEmpty()) {
    				grant = greensheetsFormGrantsService.findGSGrantInfo(null, Long.parseLong(applId), groupType, gus.getUser());	

    				GreensheetFormProxy gForm = greensheetFormService.getGreensheetForm(grant, groupType);
    				if(gForm != null) {
    					new GreensheetFormMgrImpl().changeLock(gForm, user);  
    					forward = "retrievegrants";
    				}
    				else {
    					logger.error("There is no greensheet form for Grant: " + grant + ", Group: " + groupType);
    	    			throw new GreensheetBaseException("There is no greensheet form for Grant: " + grant + ", Group: " + groupType);
    				}
    			}
    		} else {            	 
    			logger.error("The user " + user.getDisplayUserName() + " does not have permission to unlock this greensheet");                
    			throw new GreensheetBaseException("The user " + user.getDisplayUserName() + " does not have permission to unlock this greensheet");
    		}
    	}

    	return forward;
    }

}
