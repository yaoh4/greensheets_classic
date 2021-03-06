package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Sep 23, 2016 12:13:02 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * FormQuestionAnswers generated by hbm2java
 */
@SuppressWarnings("serial")
public class FormQuestionAnswers implements java.io.Serializable {

	private Integer id;
	private Forms forms;
	private String extrnlQuestionId;
	private String extrnlRespDefId;
	private String extrnlSelcDefId;
	private String stringValue;
	private String textValue;
	private Integer numberValue;
	private Date dateValue;
	private String commentValue;
	private Integer updateStamp;
	// No need for hashCode() and equal() methods. This is inside a session and Hibernate guarantees equivalence of persistent identity (database row) and Java identity inside a particular session scope.
	private Set<FormAnswerAttachments> formAnswerAttachments = new HashSet<FormAnswerAttachments>(0);

	public FormQuestionAnswers() {
	}

	public FormQuestionAnswers(Integer id, Forms forms) {
		this.id = id;
		this.forms = forms;
	}

	public FormQuestionAnswers(Integer id, Forms forms, String extrnlQuestionId, String extrnlRespDefId,
			String extrnlSelcDefId, String stringValue, String textValue, Integer numberValue, Date dateValue, String commentValue, Integer updateStamp, Set<FormAnswerAttachments> formAnswerAttachments) {
		this.id = id;
		this.forms = forms;
		this.extrnlQuestionId = extrnlQuestionId;
		this.extrnlRespDefId = extrnlRespDefId;
		this.extrnlSelcDefId = extrnlSelcDefId;
		this.stringValue = stringValue;
		this.textValue = textValue;
		this.numberValue = numberValue;
		this.dateValue = dateValue;
		this.commentValue = commentValue;
		this.updateStamp = updateStamp;
		this.formAnswerAttachments = formAnswerAttachments;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Forms getForms() {
		return this.forms;
	}

	public void setForms(Forms forms) {
		this.forms = forms;
	}

	public String getExtrnlQuestionId() {
		return this.extrnlQuestionId;
	}

	public void setExtrnlQuestionId(String extrnlQuestionId) {
		this.extrnlQuestionId = extrnlQuestionId;
	}

	public String getExtrnlRespDefId() {
		return this.extrnlRespDefId;
	}

	public void setExtrnlRespDefId(String extrnlRespDefId) {
		this.extrnlRespDefId = extrnlRespDefId;
	}

	public String getExtrnlSelcDefId() {
		return this.extrnlSelcDefId;
	}

	public void setExtrnlSelcDefId(String extrnlSelcDefId) {
		this.extrnlSelcDefId = extrnlSelcDefId;
	}

	public String getStringValue() {
		return this.stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getTextValue() {
		return this.textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public Integer getNumberValue() {
		return this.numberValue;
	}

	public void setNumberValue(Integer numberValue) {
		this.numberValue = numberValue;
	}

	public Date getDateValue() {
		return this.dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public String getCommentValue() {
		return this.commentValue;
	}

	public void setCommentValue(String commentValue) {
		this.commentValue = commentValue;
	}

	public Integer getUpdateStamp() {
		return this.updateStamp;
	}

	public void setUpdateStamp(Integer updateStamp) {
		this.updateStamp = updateStamp;
	}

	public Set<FormAnswerAttachments> getFormAnswerAttachments() {
		return this.formAnswerAttachments;
	}

	public void setFormAnswerAttachments(Set<FormAnswerAttachments> formAnswerAttachments) {
		this.formAnswerAttachments = formAnswerAttachments;
	}

}
