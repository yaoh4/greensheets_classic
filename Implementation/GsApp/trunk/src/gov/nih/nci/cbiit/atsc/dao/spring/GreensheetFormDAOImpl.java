package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.cbiit.atsc.dao.GreensheetForm;
import gov.nih.nci.cbiit.atsc.dao.GreensheetFormDAO;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsNoTemplateDefException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class GreensheetFormDAOImpl implements GreensheetFormDAO {
    private static final Logger logger = Logger.getLogger(GreensheetFormDAOImpl.class);
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public GreensheetForm getGreensheetForm(FormGrant grant, String formtype) throws GreensheetBaseException {
        MapSqlParameterSource sqlParms = new MapSqlParameterSource();
        String formRoleCode = null;
        GreensheetForm greensheetForm = null;
        String formGrantMatrixSql = "SELECT ftm_id FROM FORM_GRANT_MATRIX_T"
                + " WHERE form_role_code = :formRoleCode"
                + " AND appl_type_code = :grantType"
                + " AND activity_code = :grantMech";

        if ("SPEC".equalsIgnoreCase(formtype.trim())) {
            formRoleCode = "SPEC";
        } else if ("PGM".equalsIgnoreCase(formtype.trim())) {
            formRoleCode = "PGM";
        } else if ("DM".equalsIgnoreCase(formtype.trim())) {
            formRoleCode = "DM";
        }

        sqlParms.addValue("formRoleCode", formRoleCode);
        sqlParms.addValue("grantType", grant.getApplTypeCode()); // Type
        sqlParms.addValue("grantMech", grant.getActivityCode()); //Mech

        logger.debug("SQL: " + formGrantMatrixSql);
        int formTemplateId = 0;
        try {
        	formTemplateId = this.namedParameterJdbcTemplate.queryForInt(formGrantMatrixSql, sqlParms);
        }
        catch(EmptyResultDataAccessException e) {
        	GsNoTemplateDefException noTmplExcp = new GsNoTemplateDefException("Greensheet questionnaires " +
        			"for this kind of grant have not been defined.");
        	noTmplExcp.initCause(e);
        	throw noTmplExcp;
        }
        logger.debug("The Greensheet Form Template ID read from the DB is: " + formTemplateId);

        String greensheetFormSql = "";
        greensheetFormSql = "SELECT"
                + " aft.frm_id AS FRM_ID"
                + ", ft.ftm_id AS FTM_ID"
                + ", ft.form_role_code AS FORM_ROLE_CODE"
                + ", ft.form_status AS FORM_STATUS"
                + ", ft.poc AS POC"
                + ", ft.submitted_user_id AS SUBMITTED_USER_ID"
                + ", ft.create_user_id AS CREATE_USER_ID"
                + ", ft.create_date AS CREATE_DATE"
                + ", ft.last_change_user_id AS LAST_CHANGE_USER_ID"
                + ", ft.last_change_date AS LAST_CHANGE_DATE"
                + ", ft.update_stamp AS UPDATE_STAMP"
                + ", ft.submitted_date AS SUBMITTED_DATE"
                + " FROM APPL_FORMS_T aft"
                + ", FORMS_T ft"
                + " WHERE aft.frm_id = ft.id"
                + " AND ft.form_role_code = :formRoleCode";

        if (grant.isDummy()) {
            greensheetFormSql += " AND aft.control_full_grant_num = :fullGrantNum";
            sqlParms.addValue("fullGrantNum", grant.getFullGrantNum());
        } else {
            greensheetFormSql += " AND aft.appl_id = :applId";
            sqlParms.addValue("applId", grant.getApplId());
        }

        logger.debug("SQL to read the Greensheet Form: " + greensheetFormSql);
        //		greensheetForm = (GreensheetForm) this.namedParameterJdbcTemplate.queryForObject(greensheetFormSql, sqlParms, new GreensheetFormRowMapper());

        List greensheetForms = this.namedParameterJdbcTemplate.query(greensheetFormSql, sqlParms, new GreensheetFormRowMapper());

        if (greensheetForms != null && greensheetForms.size() == 1) {
            greensheetForm = (GreensheetForm) greensheetForms.get(0);
        } else {
            greensheetForm = new GreensheetForm();

            greensheetForm.setFtmId(formTemplateId); // The defaults are required? Existing code works with these.
            greensheetForm.setFormRoleCode(formRoleCode);
            greensheetForm.setFormStatus("NOT_STARTED");
        }

        //If the form status is either SUBMITTED or FROZEN, use the FTM_ID read by the query immediately above. 
        //If the status is other than the above two, use the FTM_ID read from the FORM_GRANT_MATRIX_T table.
        //Task to do: Debate if this stays here or to be moved to the GreensheetFormProxy

        if (greensheetForm.getFormStatus() != null
                && !(greensheetForm.getFormStatus()
                        .equalsIgnoreCase(GreensheetStatus.SUBMITTED.getName()))
                && !(greensheetForm.getFormStatus()
                        .equalsIgnoreCase(GreensheetStatus.FROZEN.getName()))) {
            greensheetForm.setFtmId(formTemplateId);
        } else {
            greensheetForm.setFtmId(greensheetForm.getFtmId());
        }

        //Task to do: The following needs to be worked on.
        //		GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
        //		GreensheetFormProxy form = dh.getGreensheetFormForGrant(grant, type);		

        return greensheetForm;
    }

}
