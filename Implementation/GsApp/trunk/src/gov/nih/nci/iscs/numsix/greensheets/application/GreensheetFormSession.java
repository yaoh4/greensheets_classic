/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */
package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetForm;
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
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetFormSession {

	private GreensheetForm form;

	private GsGrant grant;

	private Map attachmentProxies = new HashMap();

	private static final Logger logger = Logger
			.getLogger(GreensheetFormSession.class);

	/**
	 * Constructor for GreensheetFormSession.
	 */
	public GreensheetFormSession(GreensheetForm form, GsGrant grant) {
		this.form = form;
		this.grant = grant;
	}

	public String getFormTitle() {
		String grp = null;
		if (form.getGroupType().equals(GreensheetGroupType.PGM)) {
			grp = "Program";
		} else {
			grp = "Specialist";
		}

		return grp + " Type " + grant.getType() + "  Mech " + grant.getMech();

	}

	/**
	 * Returns the form.
	 * 
	 * @return GreensheetForm
	 */
	public GreensheetForm getForm() {
		return form;
	}

	/**
	 * Returns the grant.
	 * 
	 * @return GsGrant
	 */
	public GsGrant getGrant() {
		return grant;
	}

	/**
	 * Sets the form.
	 * 
	 * @param form
	 *            The form to set
	 */
	public void setForm(GreensheetForm form) {
		this.form = form;
	}

	/**
	 * Sets the grant.
	 * 
	 * @param grant
	 *            The grant to set
	 */
	public void setGrant(GsGrant grant) {
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
}
