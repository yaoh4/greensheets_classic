package gov.nih.nci.cbiit.atsc.dao.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;
import gov.nih.nci.cbiit.atsc.dao.GreensheetForm;
import gov.nih.nci.cbiit.atsc.dao.GreensheetFormDAO;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsNoTemplateDefException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetStatus;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;

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
		String formGrantMatrixSql = "SELECT ftm_id FROM FORM_GRANT_MATRIX_T" + " WHERE form_role_code = :formRoleCode"
				+ " AND appl_type_code = :grantType" + " AND activity_code = :grantMech";

		if ("SPEC".equalsIgnoreCase(formtype.trim())) {
			formRoleCode = "SPEC";
		} else if ("PGM".equalsIgnoreCase(formtype.trim())) {
			formRoleCode = "PGM";
		} else if ("DM".equalsIgnoreCase(formtype.trim())) {
			formRoleCode = "DM";
		}  else if (Constants.REVISION_TYPE.equalsIgnoreCase(formtype.trim())) {
			formRoleCode = Constants.REVISION_TYPE;			
		}

		sqlParms.addValue("formRoleCode", formRoleCode);
		sqlParms.addValue("grantType", grant.getApplTypeCode()); // Type
		sqlParms.addValue("grantMech", grant.getActivityCode()); // Mech

		logger.debug("SQL: " + formGrantMatrixSql);
		logger.debug("fromRoleCode: " + formRoleCode + ", grantType: " + grant.getApplTypeCode() + ", grantMech: " + grant.getActivityCode());
		int formTemplateId = 0;
		try {
			logger.debug("About to retrieve formTemplateId");
			formTemplateId = this.namedParameterJdbcTemplate.queryForObject(formGrantMatrixSql, sqlParms,
					Integer.class);
			logger.debug("formTemplateId = " + formTemplateId);
		} catch (EmptyResultDataAccessException e) {
			GsNoTemplateDefException noTmplExcp = new GsNoTemplateDefException(
					"Greensheet questionnaires " + "for this kind of grant have not been defined.");
			noTmplExcp.initCause(e);
			logger.error("Greensheet questionnaire for type: " + grant.getApplTypeCode() + " and mech: " + grant.getActivityCode() + " is not found.", e);
			throw noTmplExcp;
		}
		logger.debug("The Greensheet Form Template ID read from the DB is: " + formTemplateId);

		String greensheetFormSql = "SELECT" + " aft.frm_id AS FRM_ID" + ", ft.ftm_id AS FTM_ID"
				+ ", ft.form_role_code AS FORM_ROLE_CODE" + ", ft.form_status AS FORM_STATUS" + ", ft.poc AS POC"
				+ ", ft.submitted_user_id AS SUBMITTED_USER_ID" + ", ft.create_user_id AS CREATE_USER_ID"
				+ ", ft.create_date AS CREATE_DATE" + ", ft.last_change_user_id AS LAST_CHANGE_USER_ID"
				+ ", ft.last_change_date AS LAST_CHANGE_DATE" + ", ft.update_stamp AS UPDATE_STAMP"
				+ ", ft.submitted_date AS SUBMITTED_DATE" + " FROM APPL_FORMS_T aft" + ", FORMS_T ft"
				+ " WHERE aft.frm_id = ft.id" + " AND ft.form_role_code = :formRoleCode";		

		if (grant.isDummy()) {
			greensheetFormSql += " AND aft.control_full_grant_num = :fullGrantNum";
			sqlParms.addValue("fullGrantNum", grant.getFullGrantNum());
		} else {
			greensheetFormSql += " AND aft.appl_id = :applId";
			sqlParms.addValue("applId", grant.getApplId());
		}
			
		if(formRoleCode.equals(Constants.REVISION_TYPE)) {
			greensheetFormSql += "  AND aft.agt_id = :actionId";
			sqlParms.addValue("actionId", grant.getActionId());
		}
		
		logger.debug("SQL to read the Greensheet Form: " + greensheetFormSql);
		logger.debug("fullGrantNum: " + grant.getFullGrantNum() + ", applId" + grant.getApplId() + ", actionId" + grant.getActionId());
		List greensheetForms = this.namedParameterJdbcTemplate.query(greensheetFormSql, sqlParms, new GreensheetFormRowMapper());

		if (greensheetForms != null && greensheetForms.size() == 1) {
			greensheetForm = (GreensheetForm) greensheetForms.get(0);
		} else {
			greensheetForm = new GreensheetForm();

			greensheetForm.setFtmId(formTemplateId); // The defaults are
														// required? Existing
														// code works with
														// these.
			greensheetForm.setFormRoleCode(formRoleCode);
			greensheetForm.setFormStatus("NOT_STARTED");
		}

		// If the form status is either SUBMITTED or FROZEN, use the FTM_ID read
		// by the query immediately above.
		// If the status is other than the above two, use the FTM_ID read from
		// the FORM_GRANT_MATRIX_T table.
		// Task to do: Debate if this stays here or to be moved to the
		// GreensheetFormProxy

		if (greensheetForm.getFormStatus() != null
				&& !(greensheetForm.getFormStatus().equalsIgnoreCase(GreensheetStatus.SUBMITTED.getName()))
				&& !(greensheetForm.getFormStatus().equalsIgnoreCase(GreensheetStatus.FROZEN.getName()))) {
			greensheetForm.setFtmId(formTemplateId);
		} else {
			greensheetForm.setFtmId(greensheetForm.getFtmId());
		}

		// Task to do: The following needs to be worked on.
		// GreensheetFormDataHelper dh = new GreensheetFormDataHelper();
		// GreensheetFormProxy form = dh.getGreensheetFormForGrant(grant, type);

		return greensheetForm;
	}

	public boolean checkActionStatusByApplId(String applId) throws GreensheetBaseException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int nonCancelledOrClosedActionsCount = 0;

		try {
			conn = DbConnectionHelper.getInstance().getConnection();
			logger.debug("Using connection: " + conn);
			String sql = "select gascv.code from appl_gm_actions_t agat, appl_gm_action_statuses_t agast, gm_action_status_codes_vw gascv where agat.action_type=? and agast.active_flag='Y' and agat.id = agast.agt_id and gascv.id=agast.gst_id and agat.appl_id=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "AWARD");
			pstmt.setString(2, applId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String statusCode = rs.getString(1);
				if (!(statusCode.equalsIgnoreCase("CANCELLED")) && !(statusCode.equalsIgnoreCase("CLOSED"))) {
					nonCancelledOrClosedActionsCount++;
				}
			}
		} catch (SQLException se) {
			logger.error(" * *  DB problem checking action statuses when extra FORM_GRANT_VW rows are determined", se);
			throw new GreensheetBaseException("error.greensheetform", se);
		} finally {
			try {
				if (rs != null)
					rs.close();

				if (pstmt != null)
					pstmt.close();

			} catch (SQLException se) {
				logger.error(" * *  DB problem cleaning up after checking action statuses when extra FORM_GRANT_VW "
						+ "rows are determined", se);
				throw new GreensheetBaseException("error.greensheetform", se);
			}

			DbConnectionHelper.getInstance().freeConnection(conn);
		}
		return (nonCancelledOrClosedActionsCount > 1);
	}

	public boolean checkActionStatusByGrantId(String grantId) throws GreensheetBaseException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int nonCancelledOrClosedActionsCount = 0;

		try {
			conn = DbConnectionHelper.getInstance().getConnection();
			logger.debug("Using connection: " + conn);
			String sql = "select gascv.code from appl_gm_actions_t agat, appl_gm_action_statuses_t agast, gm_action_status_codes_vw gascv where agat.action_type=? and agat.id = agast.agt_id and agast.active_flag='Y' and gascv.id=agast.gst_id and agat.expected_grant_num= ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "AWARD");
			pstmt.setString(2, grantId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String statusCode = rs.getString(1);
				System.out.println("######### the statusCode is " + statusCode);
				if (!(statusCode.equalsIgnoreCase("CANCELLED")) && !(statusCode.equalsIgnoreCase("CLOSED"))) {
					nonCancelledOrClosedActionsCount++;
				}
			}
		} catch (SQLException se) {
			logger.error(" * *  DB problem checking action statuses when extra FORM_GRANT_VW rows are determined", se);
			throw new GreensheetBaseException("error.greensheetform", se);

		} finally {
			try {
				if (rs != null)
					rs.close();

				if (pstmt != null)
					pstmt.close();

			} catch (SQLException se) {
				logger.error(" * *  DB problem cleaning up after checking action statuses when extra FORM_GRANT_VW "
						+ "rows are determined", se);
				throw new GreensheetBaseException("error.greensheetform", se);
			}

			DbConnectionHelper.getInstance().freeConnection(conn);
		}

		return (nonCancelledOrClosedActionsCount > 1);

	}

	/**
	 * GREENSHEET-495
	 * 
	 * Is a given APPL_ID a Type 6 Grant with Award Type 1, 2, 4, 5,8 9 in same
	 * FY?
	 */
	public boolean isValidGrantType(String applId, StringBuffer gn) throws GreensheetBaseException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean isValid = true;

		try {
			conn = DbConnectionHelper.getInstance().getConnection();
			logger.debug("Using connection: " + conn);
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT p.appl_id,p.FULL_GRANT_NUM ");
			sql.append("FROM pv_grant_pi_t1 p ");
			sql.append("WHERE p.appl_type_code = '6' ");
			sql.append("AND EXISTS ");
			sql.append("     (SELECT 1 ");
			sql.append("        FROM pv_grant_pi_t1 awd ");
			sql.append("       WHERE awd.serial_num = p.serial_num ");
			sql.append("         AND awd.fy = p.fy ");
			sql.append("         AND awd.admin_phs_org_code = p.admin_phs_org_code  ");
			sql.append("         AND awd.appl_type_code IN ('1','2','4','5','8','9')  ");
			sql.append("         AND awd.appl_status_group_code = 'A')  ");
			sql.append("AND p.appl_id = ? ");

			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, applId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String applIdResult = rs.getString(1);
				gn.append(rs.getString(2));
				logger.info("applIdResult=" + applIdResult);
				isValid = false;
			}
		} catch (SQLException se) {
			logger.error(" * *  DB problem checking action statuses when extra FORM_GRANT_VW rows are determined", se);
			throw new GreensheetBaseException("error.greensheetform", se);

		} finally {
			try {
				if (rs != null)
					rs.close();

				if (pstmt != null)
					pstmt.close();

			} catch (SQLException se) {
				logger.error(" * *  DB problem cleaning up after checking action statuses when extra FORM_GRANT_VW "
						+ "rows are determined", se);
				throw new GreensheetBaseException("error.greensheetform", se);
			}

			DbConnectionHelper.getInstance().freeConnection(conn);
		}

		return isValid;
	}

}
