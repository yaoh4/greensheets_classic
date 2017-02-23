/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormModulesDraft;
import gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate.FormQuestionsDraft;
import gov.nih.nci.cbiit.scimgmt.gs.service.DraftModuleService;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;

public class ProcessNewQuestionDefsAction extends GsBaseAction {

    private static final Logger logger = Logger.getLogger(ProcessNewQuestionDefsAction.class);

    static ProcessNewQuestionDefsService processNewQuestionDefsService;
    static boolean processXMLOn = false;

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

    /**
     * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		String forward = "success";
		int status = Constants.OK_STATUS;
		logger.debug(".......Begin process XML questions");

        final String comments = req.getParameter("comments");
        final String module = req.getParameter("module");
        
		if (module == null || module.isEmpty()) {
			status = Constants.BAD_REQUEST_STATUS;
			resp.setStatus(status);
			return (mapping.findForward(forward));
		}

		// Determine role_code from the given module
		String roleCode = "";
		if ("PC".equals(module) || "PNC".equals(module)) {
			roleCode = "PGM";
		} else if ("SNC".equals(module) || "SC".equals(module)) {
			roleCode = "SPEC";
		} else {
			logger.error("Wrong module parameter: " + module);
			status = Constants.BAD_REQUEST_STATUS;
			resp.setStatus(status);
			return (mapping.findForward(forward));
		}

		// Importing form module from the XML file
		if (processNewQuestionDefsService.validateXml(module)) {
			status = Constants.OK_STATUS;
			Map<String, FormQuestionsDraft> mapQuestions = new HashMap<String, FormQuestionsDraft>();
			
			FormModulesDraft moduleObj = draftModuleService.importModuleFromXML(module, roleCode, mapQuestions);
					
			// Truncate draft tables for a given module unique ID
			List<FormModulesDraft> fmds = draftModuleService.findByUniqueId(
					(Class<FormModulesDraft>) FormModulesDraft.class, "name", moduleObj.getName());		
			if(fmds != null && !fmds.isEmpty()) {
				draftModuleService.deleteModule(fmds.get(0), fmds.get(0).getId());
			}
			
			// Populate draft tables 
			draftModuleService.saveModule(moduleObj, mapQuestions);

			// Set the modified flag for FORM_GRANT_MATRIX_DRAFT_T table
			processNewQuestionDefsService.checkDraftModifedFlag();
			
			logger.info(">>>>>>>>after checkDraftModifedFlag....");

			if (emailHelper != null) {
				String moduleName = "";
				if (module.trim().equalsIgnoreCase("PC"))
	                moduleName = "Program Competing";
	            if (module.trim().equalsIgnoreCase("PNC"))
	                moduleName = "Program Non Competing";
	            if (module.trim().equalsIgnoreCase("SC"))
	                moduleName = "Specialist Competing";
	            if (module.trim().equalsIgnoreCase("SNC"))
	                moduleName = "Specialist Non Competing";
	            
				String subject = "Confirmation of Successful Import of Draft Greensheets";
				String content = "Thank you for your request. The "
						+ moduleName
						+ " Draft Greensheets are available for you to review. Please log into the Greensheets Application using the URL given below, and click Promote Module button to accept the Draft Greensheets and promote them to Production. Alternatively, they are not ready to be promoted, you may select Reject Module and submit the updated request at a later time.";
				String action = "processDraft";
				emailHelper.sendPostProcessEmail(subject, content, comments, module, action);
			}

			 logger.info(">>>>>>>>Email notification sent....");
		
		} else {
			status = Constants.VALIDATION_FAILED_STATUS; // invalid XML
			if (emailHelper != null) {
				String subject = "Unsuccessful Import of the DRAFT Greensheets";
				String content = "Thank you for your request. The request did not go through and it resulted in errors. Please contact the System administrator for assistance.";
				String action = "importDraft";
				emailHelper.sendPostProcessEmail(subject, content, "", module, action);
			}
		}

		resp.setStatus(status);
		logger.info("@@@status:" + status);

		return (mapping.findForward(forward));

	}

    public void setDraftModuleService(DraftModuleService draftModuleService) {
        this.draftModuleService = draftModuleService;
    }

    public DraftModuleService getDraftModuleService() {
        return draftModuleService;
    }
}
