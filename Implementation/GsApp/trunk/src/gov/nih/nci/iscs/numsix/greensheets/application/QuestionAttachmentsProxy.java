/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionAttachment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This class serves as a temporary holder of question attachments for a
 * particular question. Any actions regarding question attachments are not
 * finalized until the form is saved. Therefore the proxy holds the
 * questionAttachment objetcts whose actions are not yet finalized. The proxy is
 * initialized with any attachmets that already exist for the greenesheet
 * question as represeneted by the responseDefId
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class QuestionAttachmentsProxy {

	private static final Logger logger = Logger
			.getLogger(QuestionAttachmentsAction.class);

	private String responseDefid;

	private HashMap qaMap = new HashMap();

	public QuestionAttachmentsProxy() {
	}

	public QuestionAttachmentsProxy(String responseDefId) {
		this.responseDefid = responseDefId;
	}

	/**
	 * Method getQuestionDefId.
	 * 
	 * @return String
	 */
	public String getQuestionDefId() {
		return StringUtils.substringBeforeLast(responseDefid, "_RD_");
	}

	/**
	 * Returns the attachment map.
	 * 
	 * @return String
	 */
	public HashMap getAttachmentMap() {
		return this.qaMap;
	}

	/**
	 * Returns the attachment map.
	 * 
	 * @return String
	 */
	public String getValidFileNames() {
		logger.debug("In Method QuestionAttachmentsProxy:getValidFileName");
		logger.debug("Length of map = " + this.qaMap.size());
		String fileNames = "$";
		Iterator iter = this.qaMap.values().iterator();
		while (iter.hasNext()) {
			QuestionAttachment qa = (QuestionAttachment) iter.next();
			logger.debug("Filename, Status = " + qa.getFilename() + ", "
					+ qa.getStatus());
			if (!qa.isToBeDeleted()) {
				fileNames += qa.getFilename() + "$";
			}
		}
		if (fileNames.length() > 1)
			fileNames = fileNames.substring(1, fileNames.length() - 1);

		return fileNames;
	}

	/**
	 * Returns the names of the valid files delimited by a "$" symbol
	 * 
	 * @return String
	 */
	public Collection getAttachmentCollection() {
		return this.qaMap.values();
	}

	/**
	 * Returns the attachment .
	 * 
	 * @param fileMemoryId
	 * @return QuestionAttachment
	 */
	public QuestionAttachment getAttachment(String fileMemoryId) {
		return (QuestionAttachment) this.qaMap.get(fileMemoryId);
	}

	/**
	 * Returns the responseDefid.
	 * 
	 * @return String
	 */
	public String getResponseDefid() {
		return responseDefid;
	}

	/**
	 * Sets the responseDefid.
	 * 
	 * @param responseDefid
	 *            The responseDefid to set
	 */
	public void setResponseDefid(String responseDefid) {
		this.responseDefid = responseDefid;
	}

	public void addQuestionAttachment(QuestionAttachment qa) {
		this.qaMap.put(qa.getFileMemoryId(), qa);
	}

	public int getAttachmentCount() {
		return this.qaMap.size();
	}

	public void removeAttachment(String memId) {
		logger.debug("In Method QuestionAttachmentsProxy:removeAttachment()");

		QuestionAttachment qa = (QuestionAttachment) this.qaMap.get(memId);
		if (qa != null) {
			if (qa.isToBeCreated()) {
				// New attachment, just remove it off the map.
				this.qaMap.remove(memId);
				logger.debug("NEW Attachment - Remove from map.");
			} else if (qa.isExisting()) {
				qa.setAttachmentStatusToDeleted();
				logger.debug("EXISTING attachment - Set status to DELETED.");
			}
		}
	}

	public void initWithExistingQuestionAttachments(Object[] attachments) {
		logger.debug("Number of existing attachments = " + attachments.length);
		for (int i = 0; i < attachments.length; i++) {
			QuestionAttachment qa = (QuestionAttachment) attachments[i];
			QuestionAttachment newQA = qa.createCopy();
			this.qaMap.put(newQA.getFileMemoryId(), newQA);
		}
	}
}
