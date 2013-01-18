package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.cbiit.atsc.dao.GreensheetQuestionsDAO;

import org.dom4j.Document;

public class GreensheetsQuestionsServicesImpl implements GreensheetsQuestionsServices {
	private GreensheetQuestionsDAO greensheetQuestionsDAO;

	public void setGreensheetQuestionsDAO(GreensheetQuestionsDAO greensheetQuestionsDAO) {
		this.greensheetQuestionsDAO = greensheetQuestionsDAO;
	}

	public Document getGreensheetQuestions(String templateIDOrFileKey, boolean readFromDB) {
		Document greensheetQuestionsDoc = null;

		greensheetQuestionsDoc = greensheetQuestionsDAO.getGreensheetQuestions(templateIDOrFileKey, readFromDB);

		return greensheetQuestionsDoc;
	}
}
