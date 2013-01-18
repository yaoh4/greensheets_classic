package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.GreensheetQuestionsDAO;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.OracleLobHandler;

public class GreensheetQuestionsDAOImpl extends JdbcDaoSupport implements GreensheetQuestionsDAO {
	public Document getGreensheetQuestions(String templateIDOrFileKey, boolean readFromDB) {
		Document greensheetQuestionsDoc = null;
		String sql = "SELECT ft.template_xml FROM form_templates_t ft WHERE ft.id = ?";

		if (readFromDB) {
			// Read the Greensheet template from the Database
			long formTemplateID = 0;
			formTemplateID = Long.parseLong(templateIDOrFileKey);
			if (formTemplateID > 0) {
				greensheetQuestionsDoc = (Document) getJdbcTemplate().queryForObject(sql,
											new Object[] { Long.valueOf(formTemplateID) },
											new RowMapper() {
												public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
													OracleLobHandler lobHandler = new OracleLobHandler();
													String greensheetQuestionsAsXML = null;
													Document greensheetQuestionsDoc = null;
			
													greensheetQuestionsAsXML = lobHandler.getClobAsString(rs, 1); // 1 is the column index 
			
													greensheetQuestionsAsXML = greensheetQuestionsAsXML.substring(0, greensheetQuestionsAsXML.lastIndexOf("</GreensheetForm>") + "</GreensheetForm>".length());

													try {
														greensheetQuestionsDoc = DocumentHelper.parseText(greensheetQuestionsAsXML);
													} catch (DocumentException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
			
														return null;
													}
													
													return greensheetQuestionsDoc;
												}
											});
			}
		} else {
			// Read the Greensheet template from the server file system
			File questionsFile = (File) AppConfigProperties.getInstance().getProperty(templateIDOrFileKey);

			SAXReader reader = new SAXReader();
			try {
				greensheetQuestionsDoc = reader.read(questionsFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return greensheetQuestionsDoc;
	}
}
