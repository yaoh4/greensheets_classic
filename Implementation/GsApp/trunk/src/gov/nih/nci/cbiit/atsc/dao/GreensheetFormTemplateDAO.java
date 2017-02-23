package gov.nih.nci.cbiit.atsc.dao;

public interface GreensheetFormTemplateDAO {
	public String getGreensheetFormTemplate(String templateId, boolean frozen); 
	public String getGreensheetFormDraftTemplate(String templateId, boolean frozen); 
	
}
