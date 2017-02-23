package gov.nih.nci.iscs.numsix.greensheets.services;

public interface GreensheetFormTemplateService {
	public String loadGreensheetFormTemplate(String templateId, boolean frozen);
	public String loadGreensheetFormDraftTemplate(String templateId, boolean frozen);
}
