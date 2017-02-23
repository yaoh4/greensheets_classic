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
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

public class RejectDraftGreensheetsAction extends GsBaseAction {

	private static final Logger logger = Logger
	.getLogger(RejectDraftGreensheetsAction.class);

	static ProcessNewQuestionDefsService processNewQuestionDefsService;

	private static EmailNotification emailHelper; // must be static or else, even though Spring injects it
    private static DraftModuleService draftModuleService;

	// at web app startup, by the time execute() runs it is null because the instance of this Action 
	// class when execute() runs is a different one, not the one that was created by Spring at startup...
	// WHICH IS BECAUSE WE DON'T HAVE STRUTS1-SPRING INTEGRATION SET UP AT ALL!

	public ProcessNewQuestionDefsService getProcessNewQuestionDefsService() {
		return processNewQuestionDefsService;
	}

	public void setProcessNewQuestionDefsService(
			ProcessNewQuestionDefsService processNewQuestionDefsService) {
		this.processNewQuestionDefsService = processNewQuestionDefsService;
	}

	public void setEmailHelper(EmailNotification emailHelper) {
		this.emailHelper = emailHelper;
	}

	public EmailNotification getEmailHelper() {
		return emailHelper;
	}

    public void setDraftModuleService(DraftModuleService draftModuleService) {
        this.draftModuleService = draftModuleService;
    }

    public DraftModuleService getDraftModuleService() {
        return draftModuleService;
    }

	/**
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String forward = "rejectDraft";
		logger.debug(".......Reject Draft Greensheets questions");
		ReviewDraftGreensheetsForm rvwDraftForm = (ReviewDraftGreensheetsForm) form;
		ActionErrors errors = new ActionErrors();
		req.getSession().removeAttribute(GreensheetsKeys.USER_NOT_FOUND);
		ReviewDraftGreensheetsAction rd = new ReviewDraftGreensheetsAction();
		String moduleName = rvwDraftForm.getModuleName();
		String selectedModuleName = rvwDraftForm.getSelectedModuleName();
		if (moduleName==null){
			if (selectedModuleName!=null){
				moduleName = selectedModuleName;
			}
		}
		
		List<FormModulesDraft> fmds = draftModuleService.findByUniqueId((Class<FormModulesDraft>) FormModulesDraft.class, "name", moduleName);
		if(fmds != null && !fmds.isEmpty()) {
			draftModuleService.deleteModule(fmds.get(0), fmds.get(0).getId());
			
			if (emailHelper != null) {
			    String subject = "Rejection of the Draft Greensheets";
			    String content = "";
			    String action = "rejectDraft";
			    emailHelper.sendPostProcessEmail(subject, content, "", moduleName, action);
			}
		}
    	logger.info("......Delete table for module " + moduleName);
    	    	
		errors.add("Reject", new ActionError("rvwDraftForm.message.RejectModule", moduleName));
		saveErrors(req, errors);
		rvwDraftForm.setDisplayReviewModuleStatusButton(true);
		rvwDraftForm.setDisplayPromoteButton(false);
		rvwDraftForm.setDisplayViewGreensheetButton(true);
		rvwDraftForm.setDisplayRejectButton(false);
		rd.generateDynamicMechDropdownsPromReject(req,rvwDraftForm);

		return (mapping.findForward(forward));
	}

}
