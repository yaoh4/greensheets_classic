package gov.nih.nci.cbiit.atsc.dao.spring;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.OracleLobHandler;

public class GreensheetDocumentRowMapper implements RowMapper {
	public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
		OracleLobHandler lobHandler = new OracleLobHandler();
		String greensheetQuestionsAsXML = null;
		Document greensheetQuestionsDoc = null;
		
		greensheetQuestionsAsXML = lobHandler.getClobAsString(rs, 1); // 1 is the column index
		
		greensheetQuestionsAsXML = greensheetQuestionsAsXML.substring(0, greensheetQuestionsAsXML.lastIndexOf("</GreensheetFormProxy>") + "</GreensheetFormProxy>".length());		
		
		try {
			greensheetQuestionsDoc = DocumentHelper.parseText(greensheetQuestionsAsXML);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return greensheetQuestionsDoc;
		}	
}
