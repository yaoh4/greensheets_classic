package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

public class DMChecklistUserPermission {
	private String applId;
	private String fullGrantNumber = "";
	private String editable = "";
	private String submittable = "";
	
	public String getApplId() {
		return applId;
	}
	public void setApplId(String applId) {
		this.applId = applId;
	}
	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}
	public String getSubmittable() {
		return submittable;
	}
	public void setSubmittable(String submittable) {
		this.submittable = submittable;
	}
	public String getFullGrantNumber() {
		return fullGrantNumber;
	}
	public void setFullGrantNumber(String fullGrantNumber) {
		this.fullGrantNumber = fullGrantNumber;
	}
}
