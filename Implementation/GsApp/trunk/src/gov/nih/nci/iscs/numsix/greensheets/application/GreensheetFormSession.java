/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */
package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
//import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionAttachment;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * The greensheet form session is a container for the form, grant and question
 * attachments for a particular greensheet form. A user can therefore have
 * multiple GreensheetFormSessions active if they are working/viewing multiple
 * greensheet forms at one time.
 *
 * @author kpuscas, Number Six Software
 */
public class GreensheetFormSession {

	private GreensheetFormProxy form;

//	private GsGrant grant; //Abdul Latheef: Used new FormGrant instead of the old GsGrant.
	private FormGrantProxy grant;

	private Map attachmentProxies = new HashMap();

	private static final Logger logger = Logger
			.getLogger(GreensheetFormSession.class);

	/**
	 * Constructor for GreensheetFormSession.
	 */
//	public GreensheetFormSession(GreensheetFormProxy form, GsGrant grant) {	//Abdul Latheef: Used new FormGrant instead of the old GsGrant.
	public GreensheetFormSession(GreensheetFormProxy form, FormGrantProxy grant) {
		this.form = form;
		this.grant = grant;
	}

	public String getFormTitle() {
		String grp = null;
		if (form.getGroupType().equals(GreensheetGroupType.PGM)) {
			grp = "Program";
		} else if (form.getGroupType().equals(GreensheetGroupType.DM)) {	// Abdul Latheef: Added the condition for the GPMATS enhancements
			grp = "Document Management";
		} else {
			grp = "Specialist";
		}

		return grp + " Type " + grant.getType() + "  Mech " + grant.getMech();

	}

	/**
	 * Returns the form.
	 *
	 * @return GreensheetFormProxy
	 */
	public GreensheetFormProxy getForm() {
		return form;
	}

	/**
	 * Returns the grant.
	 *
	 * @return GsGrant
	 */
//	public GsGrant getGrant() { //Abdul Latheef: Used new FormGrant instead of the old GsGrant.
	public FormGrantProxy getGrant() {
		return grant;
	}

	/**
	 * Sets the form.
	 *
	 * @param form
	 *            The form to set
	 */
	public void setForm(GreensheetFormProxy form) {
		this.form = form;
	}

	/**
	 * Sets the grant.
	 *
	 * @param grant
	 *            The grant to set
	 */
//	public void setGrant(GsGrant grant) { //Abdul Latheef: Used new FormGrant instead of the old GsGrant.
	public void setGrant(FormGrantProxy grant) {
		this.grant = grant;
	}

	/**
	 * Returns the attachmentProxies.
	 *
	 * @return Map
	 */
	public Map getAttachmentProxies() {
		return attachmentProxies;
	}

	/**
	 * Sets the attachmentProxies.
	 *
	 * @param attachmentProxies
	 *            The attachmentProxies to set
	 */
	public void setAttachmentProxies(Map attachmentProxies) {
		this.attachmentProxies = attachmentProxies;
	}

	public void addAttachmentsProxy(QuestionAttachmentsProxy p, String respDefId) {
		this.attachmentProxies.put(respDefId, p);
	}

	public QuestionAttachmentsProxy getQuestionAttachmentProxy(String respDefId) {
		return (QuestionAttachmentsProxy) this.attachmentProxies.get(respDefId);
	}

	public void removeQuestionAttachmentsProxy(String respDefId) {
		this.attachmentProxies.remove(respDefId);
	}

	public void clearQuestionAttachmentsProxies() {
		this.attachmentProxies.clear();
	}

	/**
	 * Method updateQuestionAttachments.
	 *
	 * @param respDefId
	 */
	public void updateQRDQuestionAttachments(String respDefId, HashMap qaMap) {
		// get the proxy for the responseDefId
		logger.debug("updateQRDQuestionAttachments -- Resp Def Id = "
				+ respDefId);
		logger.debug("updateQRDQuestionAttachments -- qaMap Size = "
				+ qaMap.size());

		QuestionAttachmentsProxy qap = this
				.getQuestionAttachmentProxy(respDefId);
		QuestionResponseData qrd = form
				.getQuestionResponseDataByRespId(respDefId);

		if (qrd != null) {
			logger.debug("Updating the QRD Attachment map");
			qrd.setQuestionAttachments(qaMap);
		} else {
			QuestionResponseData newQrd = new QuestionResponseData();
			Iterator iter = qaMap.values().iterator();
			while (iter.hasNext()) {
				QuestionAttachment qa = (QuestionAttachment) iter.next();
				newQrd.setFileResponseData(qap.getQuestionDefId(), respDefId,
						QuestionResponseData.FILE, qa);
				form.addQuestionResposeData(respDefId, newQrd);

				logger
						.debug("Added a new Question Response Data for filename - "
								+ qa.getFilename());
			}
		}
	}
	
	public String toString() {
		return this.form.getFormRoleCode() + " greensheet for " + this.grant.getFullGrantNum() +
				"(" + this.grant.getApplId() + ") [" + this.form.getFormStatus() + "]"; 
	}
}
