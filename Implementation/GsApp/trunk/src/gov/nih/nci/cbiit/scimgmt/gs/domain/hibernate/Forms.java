package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Sep 23, 2016 12:13:02 PM by Hibernate Tools 5.2.0.Beta1

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Forms generated by hbm2java
 */
@SuppressWarnings("serial")
public class Forms implements Serializable {

	private Integer id;
	private FormTemplates formTemplates;
	private String formRoleCode;
	private String formStatus;
	private String poc;
	private String submittedUserId;
	private Integer updateStamp;
	private Date submittedDate;
	// No need for hashCode() and equal() methods. This is inside a session and Hibernate guarantees equivalence of persistent identity (database row) and Java identity inside a particular session scope.
	private Set<ApplForms> applForms = new HashSet<ApplForms>(0);
	private Set<FormQuestionAnswers> formQuestionAnswers = new HashSet<FormQuestionAnswers>(0);
	private FormTemplatesMatrix formTemplatesMatrix;

	public Forms() {
	}

	public Forms(Integer id, String formRoleCode, String formStatus) {
		this.id = id;
		this.formRoleCode = formRoleCode;
		this.formStatus = formStatus;
	}

	public Forms(Integer id, FormTemplates formTemplates, String formRoleCode, String formStatus, String poc,
			String submittedUserId, Integer updateStamp, Date submittedDate, Set<ApplForms> applForms, Set<FormQuestionAnswers> formQuestionAnswers) {
		this.id = id;
		this.formTemplates = formTemplates;
		this.formRoleCode = formRoleCode;
		this.formStatus = formStatus;
		this.poc = poc;
		this.submittedUserId = submittedUserId;
		this.updateStamp = updateStamp;
		this.submittedDate = submittedDate;
		this.applForms = applForms;
		this.formQuestionAnswers = formQuestionAnswers;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FormTemplates getFormTemplates() {
		return this.formTemplates;
	}

	public void setFormTemplates(FormTemplates formTemplates) {
		this.formTemplates = formTemplates;
	}

	public String getFormRoleCode() {
		return this.formRoleCode;
	}

	public void setFormRoleCode(String formRoleCode) {
		this.formRoleCode = formRoleCode;
	}

	public String getFormStatus() {
		return this.formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

	public String getPoc() {
		return this.poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	public String getSubmittedUserId() {
		return this.submittedUserId;
	}

	public void setSubmittedUserId(String submittedUserId) {
		this.submittedUserId = submittedUserId;
	}

	public Integer getUpdateStamp() {
		return this.updateStamp;
	}

	public void setUpdateStamp(Integer updateStamp) {
		this.updateStamp = updateStamp;
	}

	public Date getSubmittedDate() {
		return this.submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Set<ApplForms> getApplForms() {
		return this.applForms;
	}

	public void setApplForms(Set<ApplForms> applForms) {
		this.applForms = applForms;
	}

	public Set<FormQuestionAnswers> getFormQuestionAnswers() {
		return this.formQuestionAnswers;
	}

	public void setFormQuestionAnswers(Set<FormQuestionAnswers> formQuestionAnswers) {
		this.formQuestionAnswers = formQuestionAnswers;
	}

	public FormTemplatesMatrix getFormTemplatesMatrix() {
		return formTemplatesMatrix;
	}

	public void setFormTemplatesMatrix(FormTemplatesMatrix formTemplatesMatrix) {
		this.formTemplatesMatrix = formTemplatesMatrix;
	}
}
