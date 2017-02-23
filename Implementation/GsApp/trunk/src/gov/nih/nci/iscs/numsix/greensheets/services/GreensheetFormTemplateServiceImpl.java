package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.cbiit.atsc.dao.GreensheetFormTemplateDAO;

public class GreensheetFormTemplateServiceImpl implements
		GreensheetFormTemplateService {
	private GreensheetFormTemplateDAO greensheetFormTemplateDAO;
	
//	public GreensheetFormTemplateDAO getGreensheetFormTemplateDAO() {
//		return this.greensheetFormTemplateDAO;
//	}	

	public void setGreensheetFormTemplateDAO(GreensheetFormTemplateDAO greensheetFormTemplateDAO) {
		this.greensheetFormTemplateDAO = greensheetFormTemplateDAO;
	}

	public String loadGreensheetFormTemplate(String templateId, boolean frozen) {
		return greensheetFormTemplateDAO.getGreensheetFormTemplate(templateId, frozen);
	}
	
	public String loadGreensheetFormDraftTemplate(String templateId, boolean frozen) {
        return greensheetFormTemplateDAO.getGreensheetFormDraftTemplate(templateId, frozen);
    }
	
}
