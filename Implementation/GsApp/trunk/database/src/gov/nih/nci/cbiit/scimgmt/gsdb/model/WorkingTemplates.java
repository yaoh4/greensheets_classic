package gov.nih.nci.cbiit.scimgmt.gsdb.model;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.SqlResultSetMapping;
import java.io.Serializable;
import java.util.Date;

public class WorkingTemplates implements Serializable {

    private Integer templateId;
    private String formRoleCode;
    private String applTypeCode;
    private String activityCode;
    private Date createDate;
    private boolean currentFlag;

    public WorkingTemplates(Integer templateId, String formRoleCode, String applTypeCode, String activityCode, Date createDate, boolean currentFlag) {
        this.templateId = templateId;
        this.formRoleCode = formRoleCode;
        this.applTypeCode = applTypeCode;
        this.activityCode = activityCode;
        this.createDate = createDate;
        this.currentFlag = currentFlag;
    }

    public WorkingTemplates(Integer templateId, String formRoleCode, String applTypeCode, String activityCode, Date createDate) {
        this(templateId, formRoleCode, applTypeCode, activityCode, createDate, false);
    }

    public WorkingTemplates() {};

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getFormRoleCode() {
        return formRoleCode;
    }

    public void setFormRoleCode(String formRoleCode) {
        this.formRoleCode = formRoleCode;
    }

    public String getApplTypeCode() {
        return applTypeCode;
    }

    public void setApplTypeCode(String applTypeCode) {
        this.applTypeCode = applTypeCode;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(boolean currentFlag) {
        this.currentFlag = currentFlag;
    }
}
