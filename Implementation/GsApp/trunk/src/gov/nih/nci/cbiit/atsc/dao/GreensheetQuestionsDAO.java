package gov.nih.nci.cbiit.atsc.dao;

import org.dom4j.Document;

public interface GreensheetQuestionsDAO {
	public Document getGreensheetQuestions(String templateIDOrFileName, boolean readFromDB); 
}
