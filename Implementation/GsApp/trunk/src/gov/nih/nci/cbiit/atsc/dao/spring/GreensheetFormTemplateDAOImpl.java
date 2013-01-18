package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.GreensheetFormTemplateDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.OracleLobHandler;

public class GreensheetFormTemplateDAOImpl extends JdbcDaoSupport implements GreensheetFormTemplateDAO {

    private static final Logger logger = Logger
            .getLogger(GreensheetFormTemplateDAOImpl.class);

    public String getGreensheetFormTemplate(String templateId, boolean frozen) {
        String greensheetFormTemplate = null;
        String sqlToExecute = null;

        if (frozen) {
            sqlToExecute = "SELECT ft.id, ft.template_html FROM FORM_TEMPLATES_T ft WHERE ft.id = ?";
        } else {
            //	sqlToExecute = "SELECT ft.id, fgm.activity_code, fgm.appl_type_code, ft.template_html FROM FORM_TEMPLATES_T ft, FORM_GRANT_MATRIX_T fgm WHERE ft.id = fgm.ftm_id AND ft.id = ?";
            sqlToExecute = "SELECT ft.id, ft.template_html FROM FORM_TEMPLATES_T ft, FORM_GRANT_MATRIX_T fgm WHERE ft.id = fgm.ftm_id AND ft.id = ?";
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
        //Template caching is not implemented right now.
        return greensheetFormTemplate;
    }
}
