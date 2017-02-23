/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormModulesDraft;
import gov.nih.nci.cbiit.scimgmt.gs.service.DraftModuleService;
import gov.nih.nci.cbiit.scimgmt.gs.service.PromoteModuleService;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class PromoteDraftGreensheetsAction extends GsBaseAction {

    private static final Logger logger = Logger
            .getLogger(PromoteDraftGreensheetsAction.class);
    
    static boolean processOn = false;

    private static DraftModuleService draftModuleService;
    static PromoteModuleService promoteModuleService;


    // at web app startup, by the time execute() runs it is null because the instance of this Action 
    // class when execute() runs is a different one, not the one that was created by Spring at startup...
    // WHICH IS BECAUSE WE DON'T HAVE STRUTS1-SPRING INTEGRATION SET UP AT ALL!

    public void setDraftModuleService(DraftModuleService draftModuleService) {
        this.draftModuleService = draftModuleService;
    }

    public DraftModuleService getDraftModuleService() {
        return draftModuleService;
    }

    public void setPromoteModuleService(PromoteModuleService promoteModuleService) {
        this.promoteModuleService = promoteModuleService;
    }

    public PromoteModuleService getPromoteModuleService() {
        return promoteModuleService;
    }

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	ReviewDraftGreensheetsAction rd = new ReviewDraftGreensheetsAction();
        String forward = "promoted";
        logger.debug(".......Promote XML questions");
        ReviewDraftGreensheetsForm rvwDraftForm = (ReviewDraftGreensheetsForm) form;
        ActionErrors errors = new ActionErrors();
        req.getSession().removeAttribute(GreensheetsKeys.USER_NOT_FOUND);

		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);

		GsUser gsUser = gus.getUser();
		final String userName =gsUser.getUserName();
		
		final String moduleName = rvwDraftForm.getSelectedModuleName();
		        
        if(!processOn){
            processOn = true;
            new Thread(){
            	public void run(){
            		try {
            			promoteModuleService.promoteDraftGreensheets(moduleName);
            			List<FormModulesDraft> fmds = draftModuleService.findByUniqueId((Class<FormModulesDraft>) FormModulesDraft.class, "name", moduleName);
            			if(fmds != null) {
            				draftModuleService.deleteModule(fmds.get(0), fmds.get(0).getId());
            			}
            		} catch (GreensheetBaseException e) {
            			logger.error("There is a problem with promoting module: " + moduleName + "for user: " + userName, e);
            		}
                }
            }.start();
            
            processOn = false;
        }
        errors.add("Promote", new ActionError("rvwDraftForm.message.PromoteModule", moduleName));
	    saveErrors(req, errors);
		    rvwDraftForm.setDisplayReviewModuleStatusButton(true);
			rvwDraftForm.setDisplayPromoteButton(false);
			rvwDraftForm.setDisplayViewGreensheetButton(true);
			rvwDraftForm.setDisplayRejectButton(false);
        rd.generateDynamicMechDropdownsPromReject(req,rvwDraftForm);
        return (mapping.findForward(forward));
    }

}
