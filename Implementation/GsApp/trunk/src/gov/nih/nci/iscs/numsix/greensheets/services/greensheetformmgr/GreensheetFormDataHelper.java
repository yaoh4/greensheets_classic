/**
 * Copyright(c) 2003, Number Six Software, Inc. Licensed under the Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbUtils;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

/**
 * Class handles database access operations for the GreensheetFormMgr
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetFormDataHelper {

    private static final Logger logger = Logger
            .getLogger(GreensheetFormDataHelper.class);

    //	GreensheetFormDataHelper() { //Changed to public from default access level - Abdul Latheef until the DAO implementation is complete for Form Services.
    public GreensheetFormDataHelper() {
    }

    //	GreensheetFormProxy getGreensheetFormForGrant(GsGrant grant, //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    GreensheetFormProxy getGreensheetFormForGrant(FormGrantProxy grant,
            GreensheetGroupType type) throws GreensheetBaseException {

        logger.debug("getGreensheetFormForGrant() Begin");
        Connection conn = null;
        //		Statement stmt = null;			// Rewrite the SQL to improve the performance in the Greensheets application
        PreparedStatement pstmt = null; // Rewrite the SQL to improve the performance in the Greensheets application
        ResultSet rs = null;

        GreensheetFormProxy form = new GreensheetFormProxy();
        form.setGroupType(type);
        form.setStatus(GreensheetStatus.NOT_STARTED);

        try {

            conn = DbConnectionHelper.getInstance().getConnection();
            //			stmt = conn.createStatement();	// Rewrite the SQL to improve the performance in the Greensheets application

            String form_role_code = null;

            if (type.equals(GreensheetGroupType.SPEC)) {
                form_role_code = "SPEC";
            } else if (type.equals(GreensheetGroupType.PGM)) {
                form_role_code = "PGM";
            } else if (type.equals(GreensheetGroupType.DM)) { //Abdul Latheef: Added the condition for GPMATS
                form_role_code = "DM";
            }

            int templateId = 0;
            //			 Commented out the following SQL: Rewrite the SQL to improve the performance in the Greensheets application
            //			String sqlTemplateId = "select ftm_id from form_grant_matrix_t "
            //					+ "WHERE form_role_code='" + form_role_code + "' AND "
            //					+ "appl_type_code='" + grant.getType()
            //					+ "' AND activity_code='" + grant.getMech() + "'";
            String sqlTemplateId = "select ftm_id from form_grant_matrix_t "
                    + "WHERE form_role_code = ? AND "
                    + "appl_type_code = ? AND "
                    + "activity_code = ?";
            //			logger.debug("sqlTemplateId " + sqlTemplateId);

            //			stmt = conn.createStatement();	// Rewrite the SQL to improve the performance in the Greensheets application
            pstmt = conn.prepareStatement(sqlTemplateId);
            logger.debug("Within getGreensheetFormForGrant(), form_role_code = " + form_role_code);
            logger.debug("Within getGreensheetFormForGrant(), grant.getType() = " + grant.getType());
            logger.debug("Within getGreensheetFormForGrant(), grant.getMech() = " + grant.getMech());
            pstmt.setString(1, form_role_code);
            pstmt.setString(2, grant.getType());
            pstmt.setString(3, grant.getMech());

            logger.debug("sqlTemplateId " + sqlTemplateId);
            //			rs = stmt.executeQuery(sqlTemplateId);	// Rewrite the SQL to improve the performance in the Greensheets application
            rs = pstmt.executeQuery(); // Rewrite the SQL to improve the performance in the Greensheets application

            while (rs.next()) {
                templateId = rs.getInt(1);
                logger.debug("Template ID read from the table form_grant_matrix_t is " + templateId); // Rewrite the SQL to improve the performance in the Greensheets application
            }

            form.setTemplateId(templateId);
            //			 Commented out the following SQL: Rewrite the SQL to improve the performance in the Greensheets application
            //			String sql = "select aft.frm_id, ft.form_status, ft.poc,ft.submitted_user_id, ft.last_change_user_id, ft.ftm_id, ft.submitted_date from appl_forms_t aft, forms_t ft where aft.frm_id=ft.id and ft.form_role_code= '"
            //					+ form_role_code + "'and ";
            //			if (grant.isDummyGrant()) {
            //			sql = sql + "aft.control_full_grant_num='"
            //					+ grant.getFullGrantNumber() + "'";
            //
            //		} else {
            //			sql = sql + "aft.appl_id=" + grant.getApplId();
            //
            //		}
            rs.close();
            pstmt.close();

            String sql = null;
            if (grant.isDummyGrant()) {
                sql = "select aft.frm_id, ft.form_status, ft.poc,ft.submitted_user_id, "
                        + "ft.last_change_user_id, ft.ftm_id, ft.submitted_date "
                        + "from appl_forms_t aft, forms_t ft "
                        + "where aft.frm_id = ft.id and "
                        + "ft.form_role_code = ? and "
                        + "aft.control_full_grant_num = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, form_role_code);
                pstmt.setString(2, grant.getFullGrantNumber());
            } else {
                sql = "select aft.frm_id, ft.form_status, ft.poc,ft.submitted_user_id, "
                        + "ft.last_change_user_id, ft.ftm_id, ft.submitted_date "
                        + "from appl_forms_t aft, forms_t ft "
                        + "where aft.frm_id = ft.id and "
                        + "ft.form_role_code = ? and "
                        + "aft.appl_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, form_role_code);
                pstmt.setInt(2, Integer.parseInt(grant.getApplID())); //Abdul Latheef: Use getApplID() instead of getApplId() for time being. 			
            }
            //			rs.close();
            //			stmt.close();

            // Note that the latest template is used unless the greensheet is
            // frozen. Then user
            // the one that is saved with the form.

            logger.debug("sql " + sql);

            //			stmt = conn.createStatement();	// Rewrite the SQL to improve the performance in the Greensheets application
            //			rs = stmt.executeQuery(sql);
            rs = pstmt.executeQuery(); // Rewrite the SQL to improve the performance in the Greensheets application
            while (rs.next()) {
                form.setFormId(rs.getInt(1));
                String status = rs.getString(2);
                if (status.equalsIgnoreCase(GreensheetStatus.SAVED.getName())) {
                    form.setStatus(GreensheetStatus.SAVED);

                } else if (status.equalsIgnoreCase(GreensheetStatus.SUBMITTED
                        .getName())) {
                    form.setStatus(GreensheetStatus.SUBMITTED);
                    form.setTemplateId(rs.getInt(6));

                } else if (status.equalsIgnoreCase(GreensheetStatus.UNSUBMITTED
                        .getName())) {
                    form.setStatus(GreensheetStatus.UNSUBMITTED);

                } else if (status.equalsIgnoreCase(GreensheetStatus.FROZEN
                        .getName())) {
                    form.setStatus(GreensheetStatus.FROZEN);
                    form.setTemplateId(rs.getInt(6));
                }

                form.setPOC(rs.getString(3));

                form.setSubmittedBy(rs.getString(4));

                form.setChangedBy(rs.getString(5));

                form.setSubmittedDate(rs.getDate(7));

            }

            logger.debug("TEMPL ID " + form.getTemplateId());

            rs.close();
            //			stmt.close();	// Rewrite the SQL to improve the performance in the Greensheets application
            pstmt.close(); // Rewrite the SQL to improve the performance in the Greensheets application

            logger.debug("status " + form.getStatusAsString());

            if (form.getFormId() != 0) {
                this.getGreensheetFormAnswers(form);
            }

        } catch (SQLException se) {
            throw new GreensheetBaseException("error.greensheetform", se);

        } finally {
            try {
                if (rs != null)
                    rs.close();

                if (pstmt != null)
                    pstmt.close();

            } catch (SQLException se) {
                throw new GreensheetBaseException("error.greensheetform", se);
            }

            DbConnectionHelper.getInstance().freeConnection(conn);
        }

        logger.debug("getGreensheetFormForGrant() End");
        return form;

    }

    //	void saveGreensheetFormData(GreensheetFormProxy form, GsGrant grant, GsUser user) 
    void saveGreensheetFormData(GreensheetFormProxy form, FormGrantProxy grant, GsUser user)
            throws GreensheetBaseException {
        logger.debug("saveGreensheetFormData() Begin");
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int formId = 0;
        try {

            conn = DbConnectionHelper.getInstance().getConnection(
                    user.getOracleId());
            boolean autoCommitStatus = conn.getAutoCommit();
            conn.setAutoCommit(false);  //  ***** TRANSACTION BEGINS ****

            // Insert or Update the Forms_T table and Appl_Forms_T

            if (form.getFormId() == 0) {

                checkForDuplicateNewForms(form, grant, conn);

                stmt = conn.createStatement();

                String sqlFrmId = "select frm_seq.nextval from dual";
                rs = stmt.executeQuery(sqlFrmId);
                if (rs.next()) {
                    formId = rs.getInt(1);
                }

                rs.close();
                stmt.close();

                String formSql = "insert into forms_t (id,ftm_id,form_role_code,form_status,poc) values(?,?,?,?,?)";
                pstmt = conn.prepareStatement(formSql);
                pstmt.setInt(1, formId);
                pstmt.setInt(2, form.getTemplateId());
                pstmt.setString(3, form.getGroupTypeAsString());
                pstmt.setString(4, GreensheetStatus.SAVED.getName());
                pstmt.setString(5, StringEscapeUtils.escapeSql(form.getPOC()));

                pstmt.execute();

                logger.debug("insertFormSql " + formSql);

                pstmt.close();

                stmt = conn.createStatement();
                String sqlAplFrmId = "select afr_seq.nextval from dual";
                int afrId = 0;
                rs = stmt.executeQuery(sqlAplFrmId);
                if (rs.next()) {
                    afrId = rs.getInt(1);
                }
                rs.close();
                stmt.close();

                String applSql = null;
                if (grant.isDummyGrant()) {
                    applSql = "insert into appl_forms_t (id,frm_id,control_full_grant_num) values (?,?,?)";

                    pstmt = conn.prepareStatement(applSql);
                    pstmt.setInt(1, afrId);
                    pstmt.setInt(2, formId);
                    pstmt.setString(3, grant.getFullGrantNumber());
                    logger.debug("grant.isDummyGrant() returns true." + "Full grant number passed to the SQL=" + grant.getFullGrantNumber()); // Abdul Latheef: For GPMATS
                } else {
                    applSql = "insert into appl_forms_t (id,frm_id,appl_id) values (?,?,?)";

                    pstmt = conn.prepareStatement(applSql);
                    pstmt.setInt(1, afrId);
                    pstmt.setInt(2, formId);
                    pstmt.setString(3, grant.getApplID()); //Abdul Latheef: Use getApplID() for time being instead of getApplId() 
                    logger.debug("grant.isDummyGrant() returns false." + "APPL ID passed to the SQL=" + grant.getApplId()); // Abdul Latheef: For GPMATS
                }

                logger.debug("applSql " + applSql);

                pstmt.execute();
                pstmt.close();
                form.setFormId(formId);
                this.saveQuestionData(form, user, grant, conn);

                form.setStatus(GreensheetStatus.SAVED);
                //form = new GreensheetFormProxy();
            } else {
                formId = form.getFormId();

                String formSql = "update forms_t set form_status = ?, ftm_id = ?, poc = ? where id = ?";

                pstmt = conn.prepareStatement(formSql);
                // Abdul Latheef: For GPMATS enhancements
                // When the user saves the DM Checklist when the form is in UNSUBMITTED state, put it in SAVED status.
                // Restrict the change only to DM Checklist as PGM, SPEC greensheet users do not have such issue. 
                // Note: Use caution. Though changeGreensheetFormStatus() appears to be generic, its code is used for form SUBMISSION only.
                if (form.getGroupType().equals(GreensheetGroupType.DM)) {
                    logger.debug("Putting the DM Checklist in " + GreensheetStatus.SAVED.getName() + " status.");
                    pstmt.setString(1, GreensheetStatus.SAVED.getName());
                    form.setStatus(GreensheetStatus.SAVED); // Is this necessary? The form will anyway be re-read from the DB?
                    logger.debug("Changing the status of the in-memory DM Checklist also to " + GreensheetStatus.SAVED.getName() + " status.");
                } else {
                    pstmt.setString(1, form.getStatusAsString());
                }
                pstmt.setInt(2, form.getTemplateId());
                pstmt.setString(3, StringEscapeUtils.escapeSql(form.getPOC()));
                pstmt.setInt(4, formId);

                logger.debug("updateFormSql" + formSql);
                pstmt.execute();
                pstmt.close();

                this.saveQuestionData(form, user, grant, conn);
            }

            conn.commit(); //  ***** TRANSACTION ENDS ***** 
            conn.setAutoCommit(autoCommitStatus);
            // ^ Added by Anatoli Mar. 24, 2013: only one commit at the very end of saving the form.

        } catch (SQLException se) {
            throw new GreensheetBaseException("Error saving Greensheet values", se);
        } finally {
            try {
                if (rs != null)
                    rs.close();

                if (stmt != null)
                    stmt.close();

                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se) {
                throw new GreensheetBaseException("error.greensheetform", se);
            }

            DbConnectionHelper.getInstance().freeConnection(conn);
        }
        logger.debug("saveGreensheetFormData() End");
    }

    void changeGreensheetFormStatus(GreensheetFormProxy form,
            GreensheetStatus newStatus, GsUser user)
            throws GreensheetBaseException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnectionHelper.getInstance().getConnection(
                    user.getOracleId());
            //String updateSql = "update forms_t set form_status = ?, submitted_user_id = ?, submitted_date = sysdate where id = ?";
            String updateSql = "update forms_t set form_status = ?, submitted_user_id = ?, submitted_date = ? where id = ?";
            logger.debug("changeStatusSql and setSubmitterSql " + updateSql);

            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, newStatus.getName());
            pstmt.setString(2, user.getOracleId());
            if (newStatus.getName().equals(GreensheetStatus.UNSUBMITTED.getName())) {
                pstmt.setDate(3, null);
            } else {
                java.util.Date utilDate = new Date();
                pstmt.setDate(3, new java.sql.Date(utilDate.getTime()));
            }
            pstmt.setInt(4, form.getFormId());
            pstmt.executeUpdate();
        } catch (SQLException se) {
            throw new GreensheetBaseException("Problem changing Greensheet Status", se);
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se) {
                throw new GreensheetBaseException("error.greensheetform", se);
            }

            DbConnectionHelper.getInstance().freeConnection(conn);
        }
    }

    /*
     * ghh commented out the following method so as to combine it with the
     * preceding method 3/3/06 void setGreensheetFormSubmitter(GreensheetFormProxy
     * form, GsUser user) throws GreensheetBaseException { Connection conn =
     * null; PreparedStatement pstmt = null; try { conn =
     * DbConnectionHelper.getInstance().getConnection(user.getOracleId());
     * String updateSql = "update forms_t set submitted_user_id =?,
     * submitted_date=sysdate where id=?"; logger.debug("setSubmitterSql " +
     * updateSql); pstmt = conn.prepareStatement(updateSql); pstmt.setString(1,
     * user.getOracleId()); pstmt.setInt(2, form.getFormId());
     * pstmt.executeUpdate();
     *  } catch (SQLException se) { throw new GreensheetBaseException("Problem
     * changing Greensheet Status"); } finally { try {
     *
     * if (pstmt != null) pstmt.close();
     *  } catch (SQLException se) { throw new
     * GreensheetBaseException("error.greensheetform", se); }
     *
     * DbConnectionHelper.getInstance().freeConnection(conn); } }
     */
    //	private void getGreensheetFormAnswers(GreensheetFormProxy form) //Changed to public from private - Abdul Latheef until the DAO implementation is complete for Form Services.
    public void getGreensheetFormAnswers(GreensheetFormProxy form)
            throws GreensheetBaseException {
        logger.debug("getGreensheetFormAnswers() Begin");

        Properties p = (Properties) AppConfigProperties.getInstance()
                .getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        String repositoryPath = p.getProperty("attachemts.repository.path");
        Connection conn = null;
        ResultSet rs = null;
        Statement aStmt = null;
        ResultSet rsA = null;
        PreparedStatement pstmt = null; // Rewrite the SQL to improve the performance in the Greensheets application
        PreparedStatement pstmtA = null; // Rewrite the SQL to improve the performance in the Greensheets application		

        try {
            conn = DbConnectionHelper.getInstance().getConnection();
            //			stmt = conn.createStatement(); //Rewrite the SQL to improve the performance in the Greensheets application
            //			 Commented out the following SQL: Rewrite the SQL to improve the performance in the Greensheets application
            //			String sql = "select * from form_question_answers_t where frm_id ="
            //					+ form.getFormId();

            String sql = "select * from form_question_answers_t where frm_id = ?";
            pstmt = conn.prepareStatement(sql); // Rewrite the SQL to improve the performance in the Greensheets application
            pstmt.setInt(1, form.getFormId()); // Rewrite the SQL to improve the performance in the Greensheets application
            //			rs = stmt.executeQuery(sql);		// Rewrite the SQL to improve the performance in the Greensheets application
            logger.debug("Rewritten SQL submitted in getGreensheetFormAnswers() is " + sql);
            rs = pstmt.executeQuery(); // Rewrite the SQL to improve the performance in the Greensheets application

            QuestionResponseData qrd = new QuestionResponseData();

            while (rs.next()) {
                int fqaId = rs.getInt("id");
                String respDefId = rs.getString("extrnl_resp_def_id");

                if (respDefId.indexOf("_RD_") > -1) {

                    String questionDefId = StringUtils.substringBeforeLast(
                            respDefId, "_RD_");

                    if (respDefId.indexOf("_DD_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = rs.getString("extrnl_selc_def_id");
                        qrd.setSelectResponseData(questionDefId, respDefId,
                                QuestionResponseData.DROP_DOWN, value);

                    } else if (respDefId.indexOf("_RB_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = rs.getString("extrnl_selc_def_id");
                        qrd.setSelectResponseData(questionDefId, respDefId,
                                QuestionResponseData.RADIO, value);

                    } else if (respDefId.indexOf("_TX_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = rs.getString("text_value");
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.TEXT, DbUtils
                                        .removeDupQuotes(value));

                    } else if (respDefId.indexOf("_NU_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = Integer.toString(rs
                                .getInt("number_value"));
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.NUMBER, value);

                    } else if (respDefId.indexOf("_ST_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = rs.getString("string_value");
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.STRING, DbUtils
                                        .removeDupQuotes(value));

                    } else if (respDefId.indexOf("_DT_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = DateFormatUtils.format(rs
                                .getDate("date_value"), "MM/dd/yyyy");
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.DATE, value);

                    } else if (respDefId.indexOf("_FL_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        //						 Commented out: Rewrite the SQL to improve the performance in the Greensheets application
                        //						String sqlAttachments = "select * from form_answer_attachments_t where fqa_id = "
                        //								+ fqaId;
                        //						aStmt = conn.createStatement();
                        //						rsA = aStmt.executeQuery(sqlAttachments);

                        String sqlAttachments = "select * from form_answer_attachments_t where fqa_id = ?";
                        logger.debug("Another Rewritten SQL submitted in getGreensheetFormAnswers() is " + sqlAttachments);
                        pstmtA = conn.prepareStatement(sqlAttachments);
                        pstmtA.setInt(1, fqaId);
                        rsA = pstmtA.executeQuery();
                        while (rsA.next()) {
                            int id = rsA.getInt("id");
                            String fileName = rsA.getString("name");
                            //String filePath = rsA.getString("file_location");
                            String filePath = repositoryPath + File.separator + rsA.getString("file_location");
                            QuestionAttachment qa = QuestionAttachment.createExistingAttachment(fileName, filePath, id);
                            qrd.setFileResponseData(questionDefId, respDefId, QuestionResponseData.FILE, qa);
                        }
                        if (rsA != null)
                            rsA.close();
                        if (pstmtA != null)
                            pstmtA.close();

                    } else if (respDefId.indexOf("_CM_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = rs.getString("comment_value");
                        qrd.setInputResponseData(questionDefId, respDefId, QuestionResponseData.COMMENT, DbUtils.removeDupQuotes(value));
                    }
                } else {
                    throw new GreensheetBaseException(
                            "error retreiveing form values. response def "
                                    + respDefId + " not found");
                }
                //logger.debug("QuestionResponseDataMap" + form.getQuestionResponsDataMap());
                form.addQuestionResposeData(respDefId, qrd);

            }

            // Rewrite the SQL to improve the performance in the Greensheets application
            // Close the DB objects outside the while loop that reads the result set.
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
            //			Commented out: Rewrite the SQL to improve the performance in the Greensheets application
            //			String sqlCbx = "select * from form_question_answers_t where (frm_id = "
            //					+ form.getFormId()
            //					+ ") and (extrnl_resp_def_id like '%_CB_%') order by extrnl_resp_def_id";

            String sqlCbx = "select * from form_question_answers_t "
                    + "where (frm_id = ?) and (extrnl_resp_def_id like '%_CB_%') "
                    + "order by extrnl_resp_def_id";

            logger.debug("sqlCbx " + sqlCbx);

            //			rs = stmt.executeQuery(sqlCbx);			// Rewrite the SQL to improve the performance in the Greensheets application
            pstmtA = conn.prepareStatement(sqlCbx); // Rewrite the SQL to improve the performance in the Greensheets application
            pstmtA.setInt(1, form.getFormId()); // Rewrite the SQL to improve the performance in the Greensheets application
            rs = pstmtA.executeQuery(); // Rewrite the SQL to improve the performance in the Greensheets application

            String cbxValue = ",";
            String cbxQuestionDefId = null;
            String cbxRespDefId = null;
            Map tmpMap = new HashMap();

            while (rs.next()) {
                cbxRespDefId = rs.getString("extrnl_resp_def_id");

                cbxValue = rs.getString("extrnl_selc_def_id");
                cbxQuestionDefId = StringUtils.substringBeforeLast(
                        cbxRespDefId, "_RD_");

                if (tmpMap.containsKey(cbxRespDefId)) {

                    QuestionResponseData q = (QuestionResponseData) tmpMap
                            .get(cbxRespDefId);
                    String s = q.getSelectionDefId();
                    s = s + cbxValue + ",";
                    q.setSelectionDefId(s);

                } else {
                    QuestionResponseData q = new QuestionResponseData();
                    tmpMap.put(cbxRespDefId, q);
                    q.setSelectResponseData(cbxQuestionDefId, cbxRespDefId,
                            QuestionResponseData.CHECK_BOX, "," + cbxValue
                                    + ",");
                }

            }

            // Rewrite the SQL to improve the performance in the Greensheets application
            // Close the DB objects outside the while loop that reads the result set.
            if (rs != null)
                rs.close();
            //			if (stmt != null)// Rewrite the SQL to improve the performance in the Greensheets application
            //				stmt.close();			
            if (pstmtA != null)// Rewrite the SQL to improve the performance in the Greensheets application
                pstmtA.close();

            Iterator iter = tmpMap.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry e = (Map.Entry) iter.next();
                String key = (String) e.getKey();
                QuestionResponseData q = (QuestionResponseData) e.getValue();

                form.addQuestionResposeData(key, q);
            }
        } catch (SQLException se) {
            throw new GreensheetBaseException("error retreiveing form values", se);
        } finally {
            try {
                if (rs != null)
                    rs.close();

                //				if (stmt != null) //Rewrite the SQL to improve the performance in the Greensheets application
                //					stmt.close();

                if (rsA != null)
                    rsA.close();

                if (aStmt != null)
                    aStmt.close();

                if (pstmtA != null) // Rewrite the SQL to improve the performance in the Greensheets application
                    pstmtA.close();
            } catch (SQLException se) {
                throw new GreensheetBaseException("error.greensheetform", se);
            }

            DbConnectionHelper.getInstance().freeConnection(conn);
        }
        logger.debug("getGreensheetFormAnswers() End");
    }

    private void checkBoxHandler(QuestionResponseData qrd, GreensheetFormProxy form,
            Connection conn) throws SQLException {
        //		Statement stmt = null; 			// Rewrite the SQL to improve the performance in the Greensheets application
        PreparedStatement pstmt = null; // Rewrite the SQL to improve the performance in the Greensheets application

        try {
            //			 Commented out the following SQL: Rewrite the SQL to improve the performance in the Greensheets application
            //			String sqlclearExisting = "delete from form_question_answers_t where extrnl_resp_def_id = '"
            //					+ qrd.getResponseDefId()
            //					+ "' and frm_id="
            //					+ form.getFormId();

            String sqlclearExisting = "delete from form_question_answers_t where extrnl_resp_def_id = ? and frm_id = ?";

            //			stmt = conn.createStatement();	// Rewrite the SQL to improve the performance in the Greensheets application
            //			stmt.execute(sqlclearExisting);	// Rewrite the SQL to improve the performance in the Greensheets application
            pstmt = conn.prepareStatement(sqlclearExisting);

            pstmt.setString(1, qrd.getResponseDefId());
            pstmt.setInt(2, form.getFormId());
            logger.debug("Checkbox clearExisting " + sqlclearExisting);

            pstmt.execute();
            //			stmt.close();	// Rewrite the SQL to improve the performance in the Greensheets application
            pstmt.close(); // Rewrite the SQL to improve the performance in the Greensheets application

            if (!qrd.getSelectionDefId().equalsIgnoreCase("")) {
                String s1 = StringUtils.stripEnd(qrd.getSelectionDefId(), ",");
                String s2 = StringUtils.stripStart(s1, ",");
                String[] vals = StringUtils.split(s2, ",");
                for (int i = 0; i < vals.length; i++) {
                    int fqaId = DbUtils.getNewRowId(conn, "fqa_seq.nextval");
                    //					 Rewrite the SQL to improve the performance in the Greensheets application
                    //					String sqlInsert = "insert into form_question_answers_t "
                    //							+ "(id, frm_id, extrnl_question_id, extrnl_resp_def_id, extrnl_selc_def_id)"
                    //							+ " values(" + fqaId + "," + form.getFormId()
                    //							+ ",'" + qrd.getQuestionDefId() + "','"
                    //							+ qrd.getResponseDefId() + "','" + vals[i] + "')";
                    String sqlInsert = "insert into form_question_answers_t "
                            + "(id, "
                            + "frm_id, "
                            + "extrnl_question_id, "
                            + "extrnl_resp_def_id, "
                            + "extrnl_selc_def_id) "
                            + "values(?, ?, ?, ?, ?)";
                    logger.debug("Checkbox sqlInsert " + sqlInsert);
                    //					stmt = conn.createStatement();
                    //					stmt.execute(sqlInsert);
                    //					stmt.close();
                    pstmt = conn.prepareStatement(sqlInsert);
                    pstmt.setInt(1, fqaId);
                    pstmt.setInt(2, form.getFormId());
                    pstmt.setString(3, qrd.getQuestionDefId());
                    pstmt.setString(4, qrd.getResponseDefId());
                    pstmt.setString(5, vals[i]);
                    pstmt.execute();
                }
                if (pstmt != null)
                    pstmt.close(); //Moved out of the for loop: Rewrite the SQL to improve the performance in the Greensheets application
            }
        } catch (SQLException se) {
            throw se;
        } finally {
            try {
                if (pstmt != null) // Rewrite the SQL to improve the performance in the Greensheets application
                    pstmt.close();
            } catch (SQLException se) {
                throw se;
            }
        }
    }

    private void saveQuestionData(GreensheetFormProxy form, GsUser user,
            FormGrantProxy grant, Connection conn) throws GreensheetBaseException {
        //			GsGrant grant) throws GreensheetBaseException { //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.

        logger.debug("saveQuestionData() Begin");

        // Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtToDel = null; // Rewrite the SQL to improve the performance in the Greensheets application
        try {

            /*
        	conn = DbConnectionHelper.getInstance().getConnection(
                    user.getOracleId());
            conn.setAutoCommit(false);
            */

            // Here we delete all existing answers associated with this form ID here - not just specific 
            // question/answer IDs. This, in absence of better mechanisms like unique constraints on FORM_ID+question_id+
            // response_def_id, should avoid saving duplicate and sometimes conflicting answers to the same questions under
            // the same form's ID number if two users are editing the same form simultaneously, or even the same user in 
            // two windows. But we don't want to reupload the file attachments, so a smarter solution is needed...
            
        	String delPreviousAnswers = "DELETE FROM form_question_answers_t WHERE FRM_ID = ? " +
        			"AND extrnl_resp_def_id NOT LIKE '%_RD_FL_%'";
        	// Leaving the file-attachment answers alone and dealing with them later...
        	pstmtToDel = conn.prepareStatement(delPreviousAnswers);
        	pstmtToDel.setInt(1, form.getFormId());
        	int oldAnswersDeletedCount = pstmtToDel.executeUpdate();
        	logger.debug(" * To delete previously-saved attachments, just ran this SQL with frm_id=" + form.getFormId()
        			+ ": " + delPreviousAnswers + "\n, and it affected " + oldAnswersDeletedCount + " rows.");
        	
            Collection qrdList = form.getQuestionResponsDataMap().values();
            Iterator iter = qrdList.iterator();

            HashMap qaMap = null;

            while (iter.hasNext()) {

                String extrnl_selc_def_id = null;
                String string_value = null;
                String text_value = null;
                double number_value = 0;
                java.sql.Date date_value = null;
                String comment_value = null;
                stmt = conn.createStatement();

                QuestionResponseData qrd = (QuestionResponseData) iter.next();
                String respDefType = qrd.getResponseDefType();

                logger.debug("\n\tRESPDEFTYPE :::: " + respDefType
                        + "\n\tRESPDEFID :::: " + qrd.getResponseDefId()
                        + "\n\tSELECTVAL :::: " + qrd.getSelectionDefId()
                        + "\n\tINPUTVAL :::: " + qrd.getInputValue());

                boolean isRDCheckBox = respDefType
                        .equalsIgnoreCase(QuestionResponseData.CHECK_BOX);
                boolean isRDFile = respDefType
                        .equalsIgnoreCase(QuestionResponseData.FILE);
                boolean isRDRadio = respDefType
                        .equalsIgnoreCase(QuestionResponseData.RADIO);
                boolean isRDDropDown = respDefType
                        .equalsIgnoreCase(QuestionResponseData.DROP_DOWN);
                boolean isRDComment = respDefType
                        .equalsIgnoreCase(QuestionResponseData.COMMENT);
                boolean isRDString = respDefType
                        .equalsIgnoreCase(QuestionResponseData.STRING);
                boolean isRDNumber = respDefType
                        .equalsIgnoreCase(QuestionResponseData.NUMBER);
                boolean isRDDate = respDefType
                        .equalsIgnoreCase(QuestionResponseData.DATE);
                boolean isRDText = respDefType
                        .equalsIgnoreCase(QuestionResponseData.TEXT);

                // Delete all question answers, except for file attachments.
                // Create new answers for the data.
                /*
                 * This code to delete one answer at a time was commented out by Anatoli on Mar. 23, 2013 because he added a different
                 * statement to delete all answers pertaining to this form above, outside of the loop.
                if (!isRDFile) {
                    // Commented out the following SQL: Rewrite the SQL to improve the performance in the Greensheets application
                    //					String sql = "delete from form_question_answers_t where id="
                    //							+ qrd.getId();
                    //					stmt = conn.createStatement();
                    String sql = "delete from form_question_answers_t where id = ?";
                    pstmtToDel = conn.prepareStatement(sql);// Rewrite the SQL to improve the performance in the Greensheets application
                    pstmtToDel.setInt(1, qrd.getId());// Rewrite the SQL to improve the performance in the Greensheets application
                    pstmtToDel.execute(); // Rewrite the SQL to improve the performance in the Greensheets application
                    //					stmt.close();// Rewrite the SQL to improve the performance in the Greensheets application
                }
                */

                // If File attachments, do nothing. handle later
                if (isRDFile) {
                    if (qaMap == null) {
                        qaMap = new HashMap();
                    }
                    //					logger.debug("File qrd =" + qrd.getResponseDefId()+ "Size"+qrd.getQuestionAttachments().size() + " : " +qrd);
                    logger.debug("File for RESP DEF ID = " + qrd.getResponseDefId());
                    qaMap.put(qrd.getResponseDefId() + "", qrd);
                }

                // Special handling for Check Boxes
                if (isRDCheckBox) {
                    this.checkBoxHandler(qrd, form, conn);
                }

                boolean bCreateRecord = true;
                if (isRDRadio || isRDDropDown) {
                    extrnl_selc_def_id = qrd.getSelectionDefId();
                    // Value could be the default SEL_X. Donot save.
                    if (extrnl_selc_def_id.equalsIgnoreCase("")
                            || (extrnl_selc_def_id.indexOf("SEL_X") > -1)) {
                        bCreateRecord = false;
                    }
                }
                // else if RD is COMMENT, STRING, TEXT, NUMBER, DATE
                else if (isRDComment || isRDString || isRDNumber || isRDDate
                        || isRDText) {
                    // Get the Input value
                    String inputValue = qrd.getInputValue();
                    if ((inputValue != null)
                            && (!inputValue.equalsIgnoreCase(""))) {
                        String escapedSqlValue = StringEscapeUtils
                                .escapeSql(inputValue);

                        if (isRDComment) {
                            //							System.out.println("Comment Val " + inputValue);
                            //							System.out.println("comment length " + inputValue.length() );
                            comment_value = inputValue;//escapedSqlValue;
                        }

                        if (isRDString) {
                            string_value = escapedSqlValue;
                        }

                        if (isRDText) {
                            text_value = escapedSqlValue;
                        }

                        if (isRDNumber) {
                            number_value = Double.parseDouble(inputValue);
                        }

                        if (isRDDate) {
                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "MM/dd/yyyy");
                            java.util.Date date = sdf.parse(inputValue);
                            java.sql.Date sDate = new java.sql.Date(date
                                    .getTime());
                            date_value = sDate;
                        }

                    } else if ((inputValue != null)
                            && (inputValue.equalsIgnoreCase(""))) {
                        bCreateRecord = false;
                    }
                }

                // Save the response if not a file or checkbox AND if
                // bCreateRecord is true.
                if (!isRDFile && !isRDCheckBox && bCreateRecord) {
                    int fqaId = DbUtils.getNewRowId(conn, "fqa_seq.nextval");
                    String sqlInsert = "insert into form_question_answers_t "
                            + "(id, frm_id, extrnl_question_id, extrnl_resp_def_id,"
                            + "extrnl_selc_def_id,string_value,text_value,number_value,date_value,comment_value)"
                            + " values(?,?,?,?,?,?,?,?,?,?)";

                    pstmt = conn.prepareStatement(sqlInsert);
                    // pstmt.setEscapeProcessing(true);

                    pstmt.setInt(1, fqaId);
                    pstmt.setInt(2, form.getFormId());
                    pstmt.setString(3, qrd.getQuestionDefId());
                    pstmt.setString(4, qrd.getResponseDefId());
                    pstmt.setString(5, extrnl_selc_def_id);
                    pstmt.setString(6, string_value);
                    pstmt.setString(7, text_value);
                    if (number_value == 0) {
                        number_value = Types.NULL;
                    }
                    pstmt.setDouble(8, number_value);
                    pstmt.setDate(9, date_value);
                    pstmt.setString(10, comment_value);
                    pstmt.execute();

                    logger.debug("INSERTED RECORD into FQA table- FQA_ID =  "
                            + fqaId + "  FormID = " + form.getFormId());
                }
            }

            // Rewrite the SQL to improve the performance in the Greensheets application
            if (pstmtToDel != null)
                pstmtToDel.close();

            // conn.commit();  Commented out by Anatoli Mar. 23, 2013: we need only 1 commit at the
               // end of saving the whole form!

            logger.debug("SAVING/DELETING THE ATTACHMENTS NOW.......");

            if (qaMap != null) { // attachments exist
            	
            	AttachmentHelper ah = new AttachmentHelper();
            	ah.deleteOrphanAttachments(conn, form.getFormId(), qaMap);
            	
                // Handle each file attachment separately
                logger.debug("Number of QRD with Files = " + qaMap.size());

                Iterator iterAttachments = qaMap.values().iterator();
                int count = 1;
                while (iterAttachments.hasNext()) {
                    logger.debug("QRD #" + count);
                    QuestionResponseData qrd = (QuestionResponseData) iterAttachments
                            .next();
                    /* AttachmentHelper ah = new AttachmentHelper(); */
                    ah.saveAttachments(qrd, grant, form, conn);

                    count++;
                }
            }

            logger.debug(" DONE --- SAVING/DELETING THE ATTACHMENTS NOW.......");
            logger.debug("Lets clean the QA MAP");
            form.resetQuestionResponsDataMap();
        } catch (Exception se) {
            if (conn != null) {
                try {
                    logger.debug("ALERT - Transaction being Rolled Back.");
                    conn.rollback();
                    throw new GreensheetBaseException("errorSavingData", se);
                } catch (SQLException excep) {
                    logger.debug("SQLException: ");
                    logger.debug(excep.getMessage());
                    throw new GreensheetBaseException("errorSavingData", se);
                }
            }

        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (pstmt != null)
                    pstmt.close();
                // Rewrite the SQL to improve the performance in the Greensheets application
                if (pstmtToDel != null)
                    pstmtToDel.close();
            } catch (SQLException se) {
                throw new GreensheetBaseException("errorSavingData", se);
            }
            /*
            if (conn != null) {
                DbConnectionHelper.getInstance().freeConnection(conn);
            }
            */
        }
        logger.debug("saveQuestionData() End");
    }

    //	private void checkForDuplicateNewForms(GreensheetFormProxy form, GsGrant g) //Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    private void checkForDuplicateNewForms(GreensheetFormProxy form, FormGrantProxy g, Connection conn)
            throws GreensheetBaseException {
        // Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // conn = DbConnectionHelper.getInstance().getConnection();

            String type = form.getGroupTypeAsString();
            int applId = Integer.parseInt(g.getApplID()); //Abdul Latheef: Use getApplID() for time being instead of getApplId() 
            String grantNum = g.getFullGrantNumber();
            String sql = null;
            if (g.isDummyGrant()) {
                sql = "select count(*) from appl_forms_t aft, forms_t ft where aft.frm_id=ft.id and aft.control_full_grant_num='"
                        + grantNum + "' and FORM_ROLE_CODE='" + type + "'";
            } else {
                sql = "select count(*) from appl_forms_t aft, forms_t ft where aft.frm_id=ft.id and appl_id='"
                        + applId + "' and FORM_ROLE_CODE='" + type + "'";
            }

            stmt = conn.createStatement();
            logger.debug("checkForDuplicateNewForms sql " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int returned = rs.getInt(1);
                if (returned > 0) {
                    throw new GreensheetBaseException(
                            "Error Saving Duplicate Greensheet Forms. Only one new Greensheetform for a grant can be created");
                }
            }
        } catch (SQLException se) {
            throw new GreensheetBaseException("Error Saving Duplicate Greensheet Forms. Only one new Greensheetform for a grant can be created", se);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                throw new GreensheetBaseException("Database Connection Error", se);
            }
            // DbConnectionHelper.getInstance().freeConnection(conn);
        }
    }
}