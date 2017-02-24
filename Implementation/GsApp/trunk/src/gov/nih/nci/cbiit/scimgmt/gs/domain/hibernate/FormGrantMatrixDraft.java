package gov.nih.nci.cbiit.scimgmt.gs.domain.hibernate;
// Generated Oct 18, 2016 9:54:23 AM by Hibernate Tools 5.2.0.Beta1

/**
 * FormGrantMatrixDraft generated by hbm2java
 */
@SuppressWarnings("serial")
public class FormGrantMatrixDraft implements java.io.Serializable {

	private Integer id;
	private FormTemplatesDraft formTemplatesDraft;
	private String formRoleCode;
	private String applTypeCode;
	private String majorActivityCode;
	private String activityCode;	
	private Integer updateStamp;
	private String otherCriteria;
	private String module;
	private Boolean modifiedFlag;

	public FormGrantMatrixDraft() {
	}

	public FormGrantMatrixDraft(Integer id, FormTemplatesDraft formTemplatesDraft, String formRoleCode, String applTypeCode,
			String majorActivityCode, String activityCode) {
		this.id = id;
		this.formTemplatesDraft = formTemplatesDraft;
		this.formRoleCode = formRoleCode;
		this.applTypeCode = applTypeCode;
		this.majorActivityCode = majorActivityCode;
		this.activityCode = activityCode;
	}

	public FormGrantMatrixDraft(Integer id, FormTemplatesDraft formTemplatesDraft, String formRoleCode, String applTypeCode,
			String majorActivityCode, String activityCode, Integer updateStamp, String otherCriteria, String module,
			Boolean modifiedFlag) {
		this.id = id;
		this.formTemplatesDraft = formTemplatesDraft;
		this.formRoleCode = formRoleCode;
		this.applTypeCode = applTypeCode;
		this.majorActivityCode = majorActivityCode;
		this.activityCode = activityCode;
		this.updateStamp = updateStamp;
		this.otherCriteria = otherCriteria;
		this.module = module;
		this.modifiedFlag = modifiedFlag;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FormTemplatesDraft getFormTemplatesDraft() {
		return formTemplatesDraft;
	}

	public void setFormTemplatesDraft(FormTemplatesDraft formTemplatesDraft) {
		this.formTemplatesDraft = formTemplatesDraft;
	}
	
	public String getFormRoleCode() {
		return this.formRoleCode;
	}

	public void setFormRoleCode(String formRoleCode) {
		this.formRoleCode = formRoleCode;
	}

	public String getApplTypeCode() {
		return this.applTypeCode;
	}

	public void setApplTypeCode(String applTypeCode) {
		this.applTypeCode = applTypeCode;
	}

	public String getMajorActivityCode() {
		return this.majorActivityCode;
	}

	public void setMajorActivityCode(String majorActivityCode) {
		this.majorActivityCode = majorActivityCode;
	}

	public String getActivityCode() {
		return this.activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public Integer getUpdateStamp() {
		return this.updateStamp;
	}

	public void setUpdateStamp(Integer updateStamp) {
		this.updateStamp = updateStamp;
	}

	public String getOtherCriteria() {
		return this.otherCriteria;
	}

	public void setOtherCriteria(String otherCriteria) {
		this.otherCriteria = otherCriteria;
	}

	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Boolean getModifiedFlag() {
		return this.modifiedFlag;
	}

	public void setModifiedFlag(Boolean modifiedFlag) {
		this.modifiedFlag = modifiedFlag;
	}

	public FormGrantMatrix toFormGrantMatrix() {
		FormGrantMatrix fgm = new FormGrantMatrix();
		fgm.setFormRoleCode(this.formRoleCode);
		fgm.setApplTypeCode(this.applTypeCode);
		fgm.setActivityCode(this.activityCode);
		fgm.setMajorActivityCode(this.majorActivityCode);
		fgm.setOtherCriteria(this.otherCriteria);

		return fgm;
	}
	
}