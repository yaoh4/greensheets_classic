package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.cbiit.atsc.dao.GreensheetForm;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

public class GreensheetFormProxy extends GreensheetForm {

	private GreensheetStatus status;

	private GreensheetGroupType group;

	public GreensheetStatus getStatus() {
		return this.status;
	}

	public String getStatusAsString() {
		return status.getName();
	}
	
	public GreensheetGroupType getGroupType() {
		return group;
	}

	public String getGroupTypeAsString() {
		return group.getName();
	}	

	public void setGroupType(GreensheetGroupType group) {
		this.group = group;
	}

	public void setStatus(GreensheetStatus status) {
		this.status = status;
	}

	public String toString() {
		// return ToStringBuilder.reflectionToString(this);
		// ^ was not really working... - Anatoli
		return this.group.getName() + " greensheet form with id " + this.getId() 
				+ " for appl_id " + this.getApplId() + " [" + status.getName() +"]"; 
	}

	public String getSubmittedDateAsString() {

		if (this.getSubmittedDate() != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return formatter.format((Date) this.getSubmittedDate());
		} else {
			return null;
		}
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getLastChangeUserId()}
	 */
	@Deprecated	
	public String getChangedBy() {
		return this.getLastChangeUserId();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #setLastChangeUserId(String lastChangeUserId)}
	 */
	@Deprecated		
	public void setChangedBy(String changedBy) {
		this.setLastChangeUserId(changedBy);
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getLastChangeDate()}
	 */
	@Deprecated		
	public Date getDateChanged() {
		return this.getLastChangeDate();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #setLastChangeDate(Date lastChangeDate)}
	 */
	@Deprecated		
	public void setDateChanged(Date dateChanged) {
		this.setLastChangeDate(dateChanged);
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getId()}
	 */
	@Deprecated		
	public int getFormId() {
		return this.getId();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #setId(int id)}
	 */
	@Deprecated		
	public void setFormId(int formId) {
		this.setId(formId);
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getPoc()}
	 */
	@Deprecated		
	public String getPOC() {
		return this.getPoc();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #setPoc(String poc)}
	 */
	@Deprecated	
	public void setPOC(String poc) {
		this.setPoc(poc);
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getSubmittedUserId()}
	 */
	@Deprecated		
	public String getSubmittedBy() {
		return this.getSubmittedUserId();
	}
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #setSubmittedUserId(String submittedUserId)}
	 */
	@Deprecated			
	public void setSubmittedBy(String submittedBy) {
		this.setSubmittedUserId(submittedBy);
	}	
	
	/**
	 * @deprecated As of release v2.5, replaced by {@link #getFtmId()}
	 */
	@Deprecated		
	public int getTemplateId() {
		return this.getFtmId();
	}

	/**
	 * @deprecated As of release v2.5, replaced by {@link #setFtmId(int ftmId)}
	 */
	@Deprecated	
	public void setTemplateId(int templateId) {
		this.setFtmId(templateId);
	}	
}
