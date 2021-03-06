/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.cbiit.atsc.dao.GreensheetForm;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseAction;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsNoTemplateDefException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.ProcessNewQuestionDefsService;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;
import gov.nih.nci.iscs.numsix.greensheets.utils.EmailNotification;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;


public class PreviewDraftGreensheetsAction extends GsBaseAction {

	private static final Logger logger = Logger
	.getLogger(PreviewDraftGreensheetsAction.class);

	static ProcessNewQuestionDefsService processNewQuestionDefsService;

	private static EmailNotification emailHelper; // must be static or else, even though Spring injects it

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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String forward = "vmid";

		String type = req.getParameter("TYPE");
		String mech = req.getParameter("MECH");
		String module = req.getParameter("MODULE_NAME");

		String moduleName = null;
		if (module.equalsIgnoreCase("Program Competing")){
			moduleName = "PGM";
		} else if (module.equalsIgnoreCase("Program Non Competing")){
			moduleName = "PGM";
		} else if (module.equalsIgnoreCase("Specialist Competing")){
			moduleName = "SPEC";
		} else if (module.equalsIgnoreCase("Specialist Non Competing")){
			moduleName = "SPEC";
		} else if (module.equalsIgnoreCase("Revision")){
			moduleName = Constants.REVISION_TYPE;
		}
		
		GreensheetUserSession gus = GreensheetActionHelper.getGreensheetUserSession(req);
		String templateId = processNewQuestionDefsService.getGreensheetDraftTemplateId(type, mech, moduleName);
		if (templateId == null) {
			forward = "templatenotfound";
		} 
		else {
			templateId = ("-".concat(templateId));
			
			FormGrantProxy grant = this.getGrant(req);
			GreensheetFormProxy gsform = null;
			try {
				gsform = this.getForm(req, grant, type, mech, moduleName, templateId);
			}
			catch (GsNoTemplateDefException noTmplE) {
				req.setAttribute("MISSING_TYPE", grant.getApplTypeCode());
				req.setAttribute("MISSING_MECH", grant.getActivityCode());
				return mapping.findForward("templatenotfound");
			}

			if (gsform == null) {
				logger.error("Inside execute method gsForm is null.");
				return mapping.findForward("error");
			}

			GreensheetFormSession gfs = new GreensheetFormSession(gsform, grant);
			req.getSession().setAttribute(
					GreensheetsKeys.KEY_CURRENT_USER_SESSION, gus);
			String id = gus.addGreensheetFormSession(gfs);
			logger.info("Inside PreviewDraftGreensheetaction id is: " + id);

			logger.debug("execute() End");

			req.setAttribute(GreensheetsKeys.KEY_FORM_UID, id);
			req.getSession().setAttribute("TEMPLATE_ID", templateId);

			QuestionResponseData dummy = new QuestionResponseData();
			dummy.setSelectionDefId("VALIDATE_TRUE");

			req.getSession().setAttribute("VALIDATE_TRUE", dummy);

			try {
				GreensheetActionHelper.setFormDraftDisplayInfo(req, id);
			} catch (GreensheetBaseException e) {
				if (e.getMessage().contains("sessionTimeOut"))
					return mapping.findForward("sessionTimeOut");
			}
		}
		return (mapping.findForward(forward));
	}

	private FormGrantProxy getGrant(HttpServletRequest req) throws Exception {

		// TODO: after the various combinations of request parameters and sources 
		// of requests were accounted for, this method ended up having a decent 
		// amount of copy-pasted repetitive code.  It could well use some 
		// refactoring for reuse and better clarity.  - Anatoli, April 2013

		logger.debug("getGrant() Begin");
		String moduleName = null;
		GreensheetUserSession gus = (GreensheetUserSession) req.getSession().getAttribute(GreensheetsKeys.KEY_CURRENT_USER_SESSION);
		String type = req.getParameter("TYPE");
		String mech = req.getParameter("MECH");
		String module = req.getParameter("MODULE_NAME");
		if (module.equalsIgnoreCase("Program Competing")){
			moduleName = "PGM";
		} else if (module.equalsIgnoreCase("Program Non Competing")){
			moduleName = "PGM";
		} else if (module.equalsIgnoreCase("Specialist Competing")){
			moduleName = "SPEC";
		} else if (module.equalsIgnoreCase("Specialist Non Competing")){
			moduleName = "SPEC";
		} else if (module.equalsIgnoreCase("Revision")){
			moduleName = Constants.REVISION_TYPE;
		}
		FormGrantProxy grant = null;

		List<FormGrant> formGrants = null;
		List<FormGrantProxy> formGrantsProxies = null;

		// request from inside Greensheets or from eGrants
		formGrants = processNewQuestionDefsService.retrieveDraftGrantsByFullGrantNum(type, mech,moduleName);
		formGrantsProxies = GreensheetActionHelper.getFormDraftGrantProxyList(formGrants, gus.getUser());

		if (formGrants!=null && formGrants.size() >= 1) {
			grant = (FormGrantProxy) formGrantsProxies.get(0); // TODO: BAD!!! - some time in the future 
			// we should change the data model to support multiple GPMATS actions per the same 
			// full grant number.  But that's not an especially quick undertaking.
		}

		return grant;
	}


	private GreensheetFormProxy getForm(HttpServletRequest req,
			FormGrantProxy grant, String type, String mech, String moduleName, String templateId) throws Exception {
		
		logger.debug("getForm() Begin");
		
		GreensheetFormProxy form = null;
		String group = moduleName;
		form = this.getGreensheetForm(grant, group, templateId);

		logger.debug("getForm() End");

		return form;
	}
	
	public GreensheetFormProxy getGreensheetForm(FormGrantProxy formGrantProxy, String formRoleCode, String templateId) 
			throws GreensheetBaseException {
		FormGrant formGrant = null;
		String formStatus = "";
		GreensheetForm greensheetForm = null;
		GreensheetFormProxy greensheetFormProxy = null;

		if (formGrantProxy != null) {
			formGrant = formGrantProxy;
		} else {
			return null;
		}

		logger.info("Inside getGreensheetForm method formGrant is: " + formGrant);
		greensheetForm = this.getGreensheetForm(formGrant, formRoleCode, templateId);
		if (greensheetForm != null) {
			greensheetFormProxy = new GreensheetFormProxy();

			greensheetFormProxy.setId(greensheetForm.getId());
			greensheetFormProxy.setFtmId(greensheetForm.getFtmId());
			greensheetFormProxy.setFormRoleCode(greensheetForm.getFormRoleCode());
			greensheetFormProxy.setFormStatus(greensheetForm.getFormStatus());
			greensheetFormProxy.setPoc(greensheetForm.getPoc());
			greensheetFormProxy.setSubmittedUserId(greensheetForm.getSubmittedUserId());
			greensheetFormProxy.setCreateUserId(greensheetForm.getCreateUserId());
			greensheetFormProxy.setCreateDate(greensheetForm.getCreateDate());
			greensheetFormProxy.setLastChangeUserId(greensheetForm.getLastChangeUserId());
			greensheetFormProxy.setLastChangeDate(greensheetForm.getLastChangeDate());
			greensheetFormProxy.setUpdateStamp(greensheetForm.getUpdateStamp());
			greensheetFormProxy.setSubmittedDate(greensheetForm.getSubmittedDate());

			//Set the form Status
			formStatus = greensheetForm.getFormStatus();
			if (formStatus.equalsIgnoreCase(GreensheetStatus.SAVED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.SAVED);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.SUBMITTED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.SUBMITTED);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.UNSUBMITTED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.UNSUBMITTED);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.FROZEN.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.FROZEN);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.NOT_STARTED.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.NOT_STARTED);
			} else if (formStatus.equalsIgnoreCase(GreensheetStatus.DRAFT.getName())) {
				greensheetFormProxy.setStatus(GreensheetStatus.DRAFT);
			}

			//Set the form role code
			formRoleCode = greensheetForm.getFormRoleCode();
			if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.PGM.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.PGM);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.SPEC.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.SPEC);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.RMC.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.RMC);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.DM.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.DM);
			} else if (formRoleCode.equalsIgnoreCase(GreensheetGroupType.REV.getName())) {
				greensheetFormProxy.setGroupType(GreensheetGroupType.REV);	
			}

		} else {
			return null;
		}


		return greensheetFormProxy;
	}
	public GreensheetForm getGreensheetForm(FormGrant grant, String formtype, String templateId) throws GreensheetBaseException {

		logger.info("Inside getGreensheetForm 2 method formType is: " + formtype);
		if ("SPEC".equalsIgnoreCase(formtype.trim())) {
		} else if ("PGM".equalsIgnoreCase(formtype.trim())) {
		} else if ("DM".equalsIgnoreCase(formtype.trim())) {
		}

		GreensheetForm greensheetForm = new GreensheetForm();
		greensheetForm.setId(1);
		greensheetForm.setFtmId(Integer.parseInt(templateId));
		greensheetForm.setFormRoleCode(formtype);
		greensheetForm.setFormStatus("DRAFT");
		greensheetForm.setPoc("");
		greensheetForm.setSubmittedUserId("");
		greensheetForm.setCreateUserId("");

		List<GreensheetForm> greensheetForms = new ArrayList<GreensheetForm>();
		greensheetForms.add(greensheetForm);
		if (greensheetForms != null && greensheetForms.size() == 1) {
			greensheetForm = (GreensheetForm) greensheetForms.get(0);
		} 

		return greensheetForm;
	}
}
