/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * GreensheetForm encapsulates information related to a specific Greensheet.
 * This also includes answers to questions.
 * 
 * @author kpuscas, Number Six Software
 * 
 */
public class GreensheetForm {

	private Date dateChanged;

	private GreensheetStatus status;

	private String POC = "";

	private String changedBy = "";

	private GreensheetGroupType group;

	private String submittedBy = "";

	private Date submittedDate;

	private int formId;

	private int applId;

	private int templateId;

	private Map questionResponsDataMap = new HashMap();

	/**
	 * Constructor for GreensheetForm.
	 */
	public GreensheetForm() {
		super();
	}

	/**
	 * Returns the applId.
	 * 
	 * @return int
	 */
	public int getApplId() {
		return applId;
	}

	/**
	 * Returns the changedBy.
	 * 
	 * @return String
	 */
	public String getChangedBy() {
		return changedBy;
	}

	/**
	 * Returns the dateChanged.
	 * 
	 * @return Date
	 */
	public Date getDateChanged() {
		return dateChanged;
	}

	/**
	 * Returns the formId.
	 * 
	 * @return int
	 */
	public int getFormId() {
		return formId;
	}

	/**
	 * Returns the group.
	 * 
	 * @return GreensheetGroupType
	 */
	public GreensheetGroupType getGroupType() {
		return group;
	}

	/**
	 * Returns the group.
	 * 
	 * @return GreensheetGroupType
	 */
	public String getGroupTypeAsString() {
		return group.getName();
	}

	/**
	 * Returns the pOC.
	 * 
	 * @return String
	 */
	public String getPOC() {
		return POC;
	}

	/**
	 * Returns the questionResponsDataMap.
	 * 
	 * @return Map
	 */
	public Map getQuestionResponsDataMap() {
		return questionResponsDataMap;
	}
	
	public void resetQuestionResponsDataMap(){
		this.questionResponsDataMap = new HashMap();
	}
	/**
	 * Returns the status.
	 * 
	 * @return GreensheetStatus
	 */
	public GreensheetStatus getStatus() {
		return status;
	}

	/**
	 * Returns the status.
	 * 
	 * @return GreensheetStatus
	 */
	public String getStatusAsString() {
		return status.getName();
	}

	/**
	 * Returns the submittedBy.
	 * 
	 * @return String
	 */
	public String getSubmittedBy() {
		return submittedBy;
	}

	/**
	 * Returns the templateId.
	 * 
	 * @return String
	 */
	public int getTemplateId() {
		return templateId;
	}

	/**
	 * Sets the applId.
	 * 
	 * @param applId
	 *            The applId to set
	 */
	public void setApplId(int applId) {
		this.applId = applId;
	}

	/**
	 * Sets the changedBy.
	 * 
	 * @param changedBy
	 *            The changedBy to set
	 */
	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	/**
	 * Sets the dateChanged.
	 * 
	 * @param dateChanged
	 *            The dateChanged to set
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	/**
	 * Sets the formId.
	 * 
	 * @param formId
	 *            The formId to set
	 */
	public void setFormId(int formId) {
		this.formId = formId;
	}

	/**
	 * Sets the group.
	 * 
	 * @param group
	 *            The group to set
	 */
	public void setGroupType(GreensheetGroupType group) {
		this.group = group;
	}

	/**
	 * Sets the pOC.
	 * 
	 * @param pOC
	 *            The pOC to set
	 */
	public void setPOC(String pOC) {
		POC = pOC;
	}

	/**
	 * Sets the questionResponsDataMap.
	 * 
	 * @param questionResponsDataMap
	 *            The questionResponsDataMap to set
	 */
	public void setQuestionResponsDataMap(Map questionResponsesMap) {
		this.questionResponsDataMap.putAll(questionResponsesMap);
	}

	/**
	 * Method addQuestionResposeData.
	 * 
	 * @param respDefId
	 * @param qrd
	 */
	public void addQuestionResposeData(String respDefId,
			QuestionResponseData qrd) {
		this.questionResponsDataMap.put(respDefId, qrd);
	}

	/**
	 * Method getQuestionResponseDataByRespId.
	 * 
	 * @param respId
	 * @return QuestionResponseData
	 */
	public QuestionResponseData getQuestionResponseDataByRespId(String respId) {
		return (QuestionResponseData) this.questionResponsDataMap.get(respId);
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            The status to set
	 */
	public void setStatus(GreensheetStatus status) {
		this.status = status;
	}

	/**
	 * Sets the submittedBy.
	 * 
	 * @param submittedBy
	 *            The submittedBy to set
	 */
	public void setSubmittedBy(String submittedBy) {
		this.submittedBy = submittedBy;
	}

	/**
	 * Sets the templateId.
	 * 
	 * @param templateId
	 *            The templateId to set
	 */
	public void setTemplateId(int templateAtSubmit) {
		this.templateId = templateAtSubmit;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * Returns the submittedDate.
	 * 
	 * @return Date
	 */
	public Date getSubmittedDate() {
		return submittedDate;
	}

	/**
	 * Returns the submittedDate.
	 * 
	 * @return Date
	 */
	public String getSubmittedDateAsString() {

		if (this.submittedDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return formatter.format((Date) this.submittedDate);
		} else {
			return null;
		}
	}

	/**
	 * Sets the submittedDate.
	 * 
	 * @param submittedDate
	 *            The submittedDate to set
	 */
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

}
