package gov.nih.nci.cbiit.atsc.dao;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.QuestionResponseData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GreensheetForm {
	private int id;
	private int ftmId;
	private String formRoleCode;
	private String formStatus;
	private String poc;
	private String submittedUserId;
	private String createUserId;
	private Date createDate;
	private String lastChangeUserId;
	private Date lastChangeDate;
	private int updateStamp;
	private Date submittedDate;
	
	private int applId;
	private Map questionResponsDataMap = new HashMap();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFtmId() {
		return ftmId;
	}

	public void setFtmId(int ftmId) {
		this.ftmId = ftmId;
	}

	public String getFormRoleCode() {
		return formRoleCode;
	}

	public void setFormRoleCode(String formRoleCode) {
		this.formRoleCode = formRoleCode;
	}

	public String getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public String getSubmittedUserId() {
		return submittedUserId;
	}

	public void setSubmittedUserId(String submittedUserId) {
		this.submittedUserId = submittedUserId;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLastChangeUserId() {
		return lastChangeUserId;
	}

	public void setLastChangeUserId(String lastChangeUserId) {
		this.lastChangeUserId = lastChangeUserId;
	}

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public int getUpdateStamp() {
		return updateStamp;
	}

	public void setUpdateStamp(int updateStamp) {
		this.updateStamp = updateStamp;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	
	public int getApplId() {
		return applId;
	}

	public void setApplId(int applId) {
		this.applId = applId;
	}	
	
	public Map getQuestionResponsDataMap() {
		return questionResponsDataMap;
	}

	public void resetQuestionResponsDataMap() {
		this.questionResponsDataMap = new HashMap();
	}
	
	public void setQuestionResponsDataMap(Map questionResponsesMap) {
		this.questionResponsDataMap.putAll(questionResponsesMap);
	}

	public void addQuestionResposeData(String respDefId,
			QuestionResponseData qrd) {
		this.questionResponsDataMap.put(respDefId, qrd);
	}

	public QuestionResponseData getQuestionResponseDataByRespId(String respId) {
		return (QuestionResponseData) this.questionResponsDataMap.get(respId);
	}	
}
