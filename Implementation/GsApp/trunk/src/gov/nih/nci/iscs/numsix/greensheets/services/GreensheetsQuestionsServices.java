package gov.nih.nci.iscs.numsix.greensheets.services;

import org.dom4j.Document;

public interface GreensheetsQuestionsServices {
	public Document getGreensheetQuestions(String templateIDOrFileKey, boolean readFromDB);
}