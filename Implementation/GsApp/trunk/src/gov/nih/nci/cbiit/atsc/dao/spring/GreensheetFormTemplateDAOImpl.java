package gov.nih.nci.cbiit.atsc.dao.spring;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.OracleLobHandler;

import gov.nih.nci.cbiit.atsc.dao.GreensheetFormTemplateDAO;

public class GreensheetFormTemplateDAOImpl extends JdbcDaoSupport implements GreensheetFormTemplateDAO {

	private static final Logger logger = Logger
	.getLogger(GreensheetFormTemplateDAOImpl.class);

	public String getGreensheetFormTemplate(String templateId, boolean frozen) {
		
		String[] split = templateId.split(",");
		templateId = split[0];
		String applTypeCode = split[1];
		String activityCode = split[2];
		
		String greensheetFormTemplate = null;
		String sqlToExecute = null;
		// System.out.println("Inside getGreensheetFormTemplate templateId 1 is " +templateId);
		if (templateId.startsWith("-")){
			frozen = false;
		}
		if (frozen) {
			sqlToExecute = "SELECT ft.id, ft.template_html FROM FORM_TEMPLATES_T ft WHERE ft.id = ?";
		} else {
			if (templateId.startsWith("-")){
				sqlToExecute = "SELECT ft.id, ft.template_html FROM FORM_TEMPLATES_DRAFT_T ft where exists (select 1 from FORM_GRANT_MATRIX_DRAFT_T fgm WHERE ft.id = fgm.ftm_id) AND ft.id = ?";
			}else{
				sqlToExecute = "SELECT ft.id, ft.template_html FROM FORM_TEMPLATES_T ft where exists (select 1 from FORM_GRANT_MATRIX_T fgm WHERE ft.id = fgm.ftm_id) AND ft.id = ?";
			}
		}
		if (templateId.startsWith("-")){
			String templateNewId = templateId.substring(1);
			//System.out.println("Inside getGreensheetFormTemplate templateNewId is " +templateNewId);
			templateId = templateNewId; 
			// System.out.println("Inside getGreensheetFormTemplate templateID is " +templateId);
		}

		//System.out.println("Inside getGreensheetFormTemplate templateID FINAL is " +templateId);
		// Read the Greensheet form template from the Database
		if (Integer.parseInt(templateId) > 0) {
			greensheetFormTemplate = (String) getJdbcTemplate().queryForObject(sqlToExecute,
					new Object[] { Long.valueOf(templateId) },
					new RowMapper() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					OracleLobHandler lobHandler = new OracleLobHandler();
					String template = null;

					template = lobHandler.getClobAsString(rs, 2);
					template = template.substring(0, template.lastIndexOf("</html>") + "</html>".length());
					//System.out.println("THE gunjan SHRIVASTAVA template is " +template);
					return template;
				}
			});
		}
		
		if(activityCode.equals("000")){
			greensheetFormTemplate = greensheetFormTemplate.replaceFirst("Greensheet Type: 0 &amp; Mech: 000", "Greensheet");
		} else {
			greensheetFormTemplate = greensheetFormTemplate.replaceFirst("Greensheet Type: [0-9]", "Greensheet Type: " + applTypeCode);
			greensheetFormTemplate = greensheetFormTemplate.replaceFirst("Mech: [A-Z0-9][A-Z0-9][A-Z0-9]", "Mech: " + activityCode);
		}
		
		//Template caching is not implemented right now.
		return greensheetFormTemplate;
	}

	public String getGreensheetFormDraftTemplate(String templateId, boolean frozen) {

		logger.info("!!!!!!!!!!!!!!!!!!!.................. this is in draft table");
		
		String[] split = templateId.split(",");
		templateId = split[0];
		String applTypeCode = split[1];
		String activityCode = split[2];
		String greensheetFormTemplate = null;
		String sqlToExecute = null;

		if (frozen) {
			sqlToExecute = "SELECT ft.id, ft.template_html FROM FORM_TEMPLATES_DRAFT_T ft WHERE ft.id = ?";
		} else {
			//  sqlToExecute = "SELECT ft.id, fgm.activity_code, fgm.appl_type_code, ft.template_html FROM FORM_TEMPLATES_T ft, FORM_GRANT_MATRIX_T fgm WHERE ft.id = fgm.ftm_id AND ft.id = ?";
			sqlToExecute = "SELECT ft.id, ft.template_html FROM FORM_TEMPLATES_DRAFT_T ft, FORM_GRANT_MATRIX_DRAFT_T fgm WHERE ft.id = fgm.ftm_id AND ft.id = ?";
		}

		// Read the Greensheet form template from the Database
		if (Integer.parseInt(templateId) > 0) {
			greensheetFormTemplate = (String) getJdbcTemplate().queryForObject(sqlToExecute,
					new Object[] { Long.valueOf(templateId) },
					new RowMapper() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					OracleLobHandler lobHandler = new OracleLobHandler();
					String template = null;

					template = lobHandler.getClobAsString(rs, 2);
					template = template.substring(0, template.lastIndexOf("</html>") + "</html>".length());

					return template;
				}
			});
		}
		
		if(activityCode.equals("000")){
			greensheetFormTemplate = greensheetFormTemplate.replaceFirst("Greensheet Type: 0 &amp; Mech: 000", "Greensheet");
		} else {
			greensheetFormTemplate = greensheetFormTemplate.replaceFirst("Greensheet Type: [0-9]", "Greensheet Type: " + applTypeCode);
			greensheetFormTemplate = greensheetFormTemplate.replaceFirst("Mech: [A-Z0-9][A-Z0-9][A-Z0-9]", "Mech: " + activityCode);
		}
		
		//Template caching is not implemented right now.
		return greensheetFormTemplate;
	}
}
