/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;
import java.text.*;

import org.apache.commons.lang.*;
import org.apache.commons.lang.time.*;
import org.apache.log4j.*;

/**
 * Class handles database access operations for the GreensheetFormMgr
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetFormDataHelper {

    private static final Logger logger = Logger.getLogger(GreensheetFormDataHelper.class);

    GreensheetFormDataHelper() {
    }

    GreensheetForm getGreensheetFormForGrant(GsGrant grant, GreensheetGroupType type)
            throws GreensheetBaseException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        GreensheetForm form = new GreensheetForm();
        form.setGroupType(type);
        form.setStatus(GreensheetStatus.NOT_STARTED);

        try {

            conn = DbConnectionHelper.getInstance().getConnection();
            stmt = conn.createStatement();

            String form_role_code = null;

            if (type.equals(GreensheetGroupType.SPEC)) {
                form_role_code = "SPEC";

            } else if (type.equals(GreensheetGroupType.PGM)) {
                form_role_code = "PGM";

            }

            int templateId = 0;

            String sqlTemplateId = "select ftm_id from form_grant_matrix_t "
                    + "WHERE form_role_code='" + form_role_code + "' AND " + "appl_type_code='"
                    + grant.getType() + "' AND activity_code='" + grant.getMech() + "'";

            logger.debug("sqlTemplateId " + sqlTemplateId);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlTemplateId);

            while (rs.next()) {
                templateId = rs.getInt(1);
            }

            form.setTemplateId(templateId);

            String sql = "select aft.frm_id, ft.form_status, ft.poc,ft.submitted_user_id, ft.last_change_user_id, ft.ftm_id, ft.submitted_date from appl_forms_t aft, forms_t ft where aft.frm_id=ft.id and ft.form_role_code= '"
                    + form_role_code + "'and ";

            if (grant.isDummyGrant()) {
                sql = sql + "aft.control_full_grant_num='" + grant.getFullGrantNumber() + "'";

            } else {
                sql = sql + "aft.appl_id=" + grant.getApplId();

            }

            rs.close();
            stmt.close();

            // Note that the latest template is used unless the greensheet is
            // frozen. Then user
            // the one that is saved with the form.

            logger.debug("sql " + sql);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {

                form.setFormId(rs.getInt(1));
                String status = rs.getString(2);
                if (status.equalsIgnoreCase(GreensheetStatus.SAVED.getName())) {
                    form.setStatus(GreensheetStatus.SAVED);

                } else if (status.equalsIgnoreCase(GreensheetStatus.SUBMITTED.getName())) {
                    form.setStatus(GreensheetStatus.SUBMITTED);
                    form.setTemplateId(rs.getInt(6));

                } else if (status.equalsIgnoreCase(GreensheetStatus.UNSUBMITTED.getName())) {
                    form.setStatus(GreensheetStatus.UNSUBMITTED);

                } else if (status.equalsIgnoreCase(GreensheetStatus.FROZEN.getName())) {
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
            stmt.close();

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

                if (stmt != null)
                    stmt.close();

            } catch (SQLException se) {
                throw new GreensheetBaseException("error.greensheetform", se);
            }

            DbConnectionHelper.getInstance().freeConnection(conn);
        }

        return form;

    }

    void saveGreensheetFormData(GreensheetForm form, GsGrant grant, GsUser user)
            throws GreensheetBaseException {

        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int formId = 0;
        try {

            conn = DbConnectionHelper.getInstance().getConnection(user.getOracleId());

            // Insert or Update the Forms_T table and Appl_Forms_T

            if (form.getFormId() == 0) {

                checkForDuplicateNewForms(form, grant);

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

                } else {

                    applSql = "insert into appl_forms_t (id,frm_id,appl_id) values (?,?,?)";

                    pstmt = conn.prepareStatement(applSql);
                    pstmt.setInt(1, afrId);
                    pstmt.setInt(2, formId);
                    pstmt.setString(3, grant.getApplId());

                }

                logger.debug("applSql " + applSql);

                pstmt.execute();
                pstmt.close();
                form.setFormId(formId);
                this.saveQuestionData(form, user, grant);

                form.setStatus(GreensheetStatus.SAVED);

            } else {
                formId = form.getFormId();

                String formSql = "update forms_t set form_status = ?, ftm_id= ?, poc=? where id=?";

                pstmt = conn.prepareStatement(formSql);
                pstmt.setString(1, form.getStatusAsString());
                pstmt.setInt(2, form.getTemplateId());
                pstmt.setString(3, StringEscapeUtils.escapeSql(form.getPOC()));
                pstmt.setInt(4, formId);

                logger.debug("updateFormSql" + formSql);
                pstmt.execute();
                pstmt.close();

                this.saveQuestionData(form, user, grant);
            }

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

    }

    void changeGreensheetFormStatus(GreensheetForm form, GreensheetStatus newStatus, GsUser user)
            throws GreensheetBaseException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnectionHelper.getInstance().getConnection(user.getOracleId());
            String updateSql = "update forms_t set form_status =? where id=?";
            logger.debug("changeStatusSql " + updateSql);
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, newStatus.getName());
            pstmt.setInt(2, form.getFormId());
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

    void setGreensheetFormSubmitter(GreensheetForm form, GsUser user)
            throws GreensheetBaseException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbConnectionHelper.getInstance().getConnection(user.getOracleId());
            String updateSql = "update forms_t set submitted_user_id =?, submitted_date=sysdate where id=?";
            logger.debug("setSubmitterSql " + updateSql);
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, user.getOracleId());
            pstmt.setInt(2, form.getFormId());
            pstmt.executeUpdate();

        } catch (SQLException se) {
            throw new GreensheetBaseException("Problem changing Greensheet Status");
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

    private void getGreensheetFormAnswers(GreensheetForm form) throws GreensheetBaseException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        Statement aStmt = null;
        ResultSet rsA = null;

        try {

            conn = DbConnectionHelper.getInstance().getConnection();
            stmt = conn.createStatement();

            String sql = "select * from form_question_answers_t where frm_id =" + form.getFormId();

            rs = stmt.executeQuery(sql);

            QuestionResponseData qrd = null;

            while (rs.next()) {
                int fqaId = rs.getInt("id");
                String respDefId = rs.getString("extrnl_resp_def_id");

                if (respDefId.indexOf("_RD_") > -1) {

                    String questionDefId = StringUtils.substringBeforeLast(respDefId, "_RD_");

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
                                QuestionResponseData.TEXT, DbUtils.removeDupQuotes(value));

                    } else if (respDefId.indexOf("_NU_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = Integer.toString(rs.getInt("number_value"));
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.NUMBER, value);

                    } else if (respDefId.indexOf("_ST_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = rs.getString("string_value");
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.STRING, DbUtils.removeDupQuotes(value));

                    } else if (respDefId.indexOf("_DT_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = DateFormatUtils.format(rs.getDate("date_value"),
                                "MM/dd/yyyy");
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.DATE, value);

                    } else if (respDefId.indexOf("_FL_") > -1) {
                        qrd = new QuestionResponseData(fqaId);

                        String sqlAttachments = "select * from form_answer_attachments_t where fqa_id = "
                                + fqaId;
                        aStmt = conn.createStatement();
                        rsA = aStmt.executeQuery(sqlAttachments);

                        while (rsA.next()) {

                            int id = rsA.getInt("id");
                            String fileName = rsA.getString("name");
                            String filePath = rsA.getString("file_location");
                            QuestionAttachment qa = QuestionAttachment.createExistingAttachment(
                                    fileName, filePath, id);
                            qrd.setFileResponseData(questionDefId, respDefId,
                                    QuestionResponseData.FILE, qa);
                        }
                        rsA.close();
                        aStmt.close();

                    } else if (respDefId.indexOf("_CM_") > -1) {
                        qrd = new QuestionResponseData(fqaId);
                        String value = rs.getString("comment_value");
                        qrd.setInputResponseData(questionDefId, respDefId,
                                QuestionResponseData.COMMENT, DbUtils.removeDupQuotes(value));
                    }
                } else {

                    throw new GreensheetBaseException(
                            "error retreiveing form values. response def " + respDefId
                                    + " not found");
                }

                form.addQuestionResposeData(respDefId, qrd);

            }

            String sqlCbx = "select * from form_question_answers_t where (frm_id = "
                    + form.getFormId()
                    + ") and (extrnl_resp_def_id like '%_CB_%') order by extrnl_resp_def_id";

            logger.debug("sqlCbx " + sqlCbx);

            rs = stmt.executeQuery(sqlCbx);

            String cbxValue = ",";
            String cbxQuestionDefId = null;
            String cbxRespDefId = null;
            Map tmpMap = new HashMap();

            while (rs.next()) {
                cbxRespDefId = rs.getString("extrnl_resp_def_id");

                cbxValue = rs.getString("extrnl_selc_def_id");
                cbxQuestionDefId = StringUtils.substringBeforeLast(cbxRespDefId, "_RD_");

                if (tmpMap.containsKey(cbxRespDefId)) {

                    QuestionResponseData q = (QuestionResponseData) tmpMap.get(cbxRespDefId);
                    String s = q.getSelectionDefId();
                    s = s + cbxValue + ",";
                    q.setSelectionDefId(s);

                } else {
                    QuestionResponseData q = new QuestionResponseData();
                    tmpMap.put(cbxRespDefId, q);
                    q.setSelectResponseData(cbxQuestionDefId, cbxRespDefId,
                            QuestionResponseData.CHECK_BOX, "," + cbxValue + ",");
                }

            }

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

                if (stmt != null)
                    stmt.close();

                if (rsA != null)
                    rsA.close();

                if (aStmt != null)
                    aStmt.close();

            } catch (SQLException se) {
                throw new GreensheetBaseException("error.greensheetform", se);
            }

            DbConnectionHelper.getInstance().freeConnection(conn);

        }

    }

    private boolean entryExists(int respDefId, int formId, Connection conn) throws SQLException {
        boolean result = false;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = conn.createStatement();
            String sqlExistingAnswer = "select id from form_question_answers_t where id = '"
                    + respDefId + "' and frm_id=" + formId;

            logger.debug("entryExistsSql " + sqlExistingAnswer);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlExistingAnswer);
            if (!rs.next()) {
                result = false;
            } else {
                result = true;
            }

        } catch (SQLException se) {
            throw se;

        } finally {
            try {
                if (rs != null)
                    rs.close();

                if (stmt != null)
                    stmt.close();

            } catch (SQLException se) {
                throw se;
            }

        }
        return result;

    }

    private int createNewFormQuestionAnswer(GreensheetForm form, QuestionResponseData qrd,
            Connection conn) throws SQLException {

        PreparedStatement pstmt = null;
        int fqaId = 0;
        try {

            fqaId = DbUtils.getNewRowId(conn, "fqa_seq.nextval");

            String sqlbase = "insert into form_question_answers_t (id,frm_id,extrnl_question_id,extrnl_resp_def_id) values(?,?,?,?)";

            pstmt = conn.prepareStatement(sqlbase);

            pstmt.setInt(1, fqaId);
            pstmt.setInt(2, form.getFormId());
            pstmt.setString(3, qrd.getQuestionDefId());
            pstmt.setString(4, qrd.getResponseDefId());

            logger.debug("createNewFQAsql " + sqlbase);

            pstmt.execute();

        } catch (SQLException se) {
            throw se;
        } finally {
            try {

                if (pstmt != null)
                    pstmt.close();

            } catch (SQLException se) {
                throw se;
            }

        }
        return fqaId;

    }

    private void checkBoxHandler(QuestionResponseData qrd, GreensheetForm form, Connection conn)
            throws SQLException {
        Statement stmt = null;
        try {

            String sqlclearExisting = "delete from form_question_answers_t where extrnl_resp_def_id = '"
                    + qrd.getResponseDefId() + "' and frm_id=" + form.getFormId();

            logger.debug("Checkbox clearExisting " + sqlclearExisting);
            stmt = conn.createStatement();
            stmt.execute(sqlclearExisting);
            stmt.close();

            if (!qrd.getSelectionDefId().equalsIgnoreCase("")) {
                String s1 = StringUtils.stripEnd(qrd.getSelectionDefId(), ",");
                String s2 = StringUtils.stripStart(s1, ",");
                String[] vals = StringUtils.split(s2, ",");
                for (int i = 0; i < vals.length; i++) {
                    int fqaId = DbUtils.getNewRowId(conn, "fqa_seq.nextval");
                    String sqlInsert = "insert into form_question_answers_t "
                            + "(id, frm_id, extrnl_question_id, extrnl_resp_def_id, extrnl_selc_def_id)"
                            + " values(" + fqaId + "," + form.getFormId() + ",'"
                            + qrd.getQuestionDefId() + "','" + qrd.getResponseDefId() + "','"
                            + vals[i] + "')";
                    logger.debug("Checkbox sqlInsert " + sqlInsert);
                    stmt = conn.createStatement();
                    stmt.execute(sqlInsert);
                    stmt.close();
                }
            }

        } catch (SQLException se) {
            throw se;
        } finally {
            try {

                if (stmt != null)
                    stmt.close();

            } catch (SQLException se) {
                throw se;
            }
        }

    }

    private void saveQuestionData(GreensheetForm form, GsUser user, GsGrant grant)
            throws GreensheetBaseException {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        try {

            Collection qrdList = form.getQuestionResponsDataMap().values();

            Iterator iter = qrdList.iterator();
            conn = DbConnectionHelper.getInstance().getConnection(user.getOracleId());
            conn.setAutoCommit(false);

            while (iter.hasNext()) {

                int formId = 0;
                String extrnl_question_id = null;
                String extrnl_resp_def_id = null;
                String extrnl_selc_def_id = null;
                String string_value = null;
                String text_value = null;
                double number_value = 0;
                java.sql.Date date_value = null;
                String comment_value = null;
                boolean delete = false;
                stmt = conn.createStatement();

                QuestionResponseData qrd = (QuestionResponseData) iter.next();

                String respDefType = qrd.getResponseDefType();

                logger.debug("\n\tRESPDEFTYPE :::: " + respDefType + "\n\tRESPDEFID :::: "
                        + qrd.getResponseDefId() + "\n\tSELECTVAL :::: " + qrd.getSelectionDefId()
                        + "\n\tINPUTVAL :::: " + qrd.getInputValue());

                // Special handling for check box response defs
                if (respDefType.equalsIgnoreCase(QuestionResponseData.CHECK_BOX)) {
                    this.checkBoxHandler(qrd, form, conn);

                }

                // Special handling for file response defs
                if (respDefType.equalsIgnoreCase(QuestionResponseData.FILE)) {
                    logger.debug("SAVING FILES");
                    AttachmentHelper ah = new AttachmentHelper();
                    ah.saveAttachments(qrd, grant, form, conn);
                }

                // Delete all question answers. Will create new ones for data
                // that exists.
                if (!respDefType.equalsIgnoreCase(QuestionResponseData.FILE)) {
                    String sqlDelete2 = "delete from form_question_answers_t where id="
                            + qrd.getId();

                    stmt = conn.createStatement();
                    stmt.execute(sqlDelete2);
                    stmt.close();
                }

                // if response type is radio of drop down
                if (respDefType.equalsIgnoreCase(QuestionResponseData.RADIO)
                        || respDefType.equalsIgnoreCase(QuestionResponseData.DROP_DOWN)) {

                    extrnl_selc_def_id = qrd.getSelectionDefId();

                    if ((qrd.getSelectionDefId().indexOf("SEL_X") > -1)
                            || qrd.getSelectionDefId().equalsIgnoreCase("")) {
                        delete = true;
                    }

                    // if response type is one that requires user input
                    // (comment, text, string, data, number)
                } else if (qrd.getInputValue() != null && !qrd.getInputValue().equalsIgnoreCase("")) {

                    if (respDefType.equalsIgnoreCase(QuestionResponseData.COMMENT)) {

                        comment_value = StringEscapeUtils.escapeSql(qrd.getInputValue());

                    } else if (respDefType.equalsIgnoreCase(QuestionResponseData.STRING)) {

                        string_value = StringEscapeUtils.escapeSql(qrd.getInputValue());

                    } else if (respDefType.equalsIgnoreCase(QuestionResponseData.NUMBER)) {

                        number_value = Double.parseDouble(qrd.getInputValue());

                    } else if (respDefType.equalsIgnoreCase(QuestionResponseData.DATE)) {

                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                        java.util.Date date = sdf.parse(qrd.getInputValue());
                        java.sql.Date sDate = new java.sql.Date(date.getTime());
                        date_value = sDate;

                    } else if (respDefType.equalsIgnoreCase(QuestionResponseData.TEXT)) {

                        text_value = StringEscapeUtils.escapeSql(qrd.getInputValue());
                    }

                } else if (qrd.getInputValue() != null && qrd.getInputValue().equalsIgnoreCase("")) {

                    delete = true;
                }

                if (!respDefType.equalsIgnoreCase(QuestionResponseData.FILE)
                        && !respDefType.equalsIgnoreCase(QuestionResponseData.CHECK_BOX) && !delete) {

                    int fqaId = DbUtils.getNewRowId(conn, "fqa_seq.nextval");
                    String sqlInsert = "insert into form_question_answers_t "
                            + "(id, frm_id, extrnl_question_id, extrnl_resp_def_id,"
                            + "extrnl_selc_def_id,string_value,text_value,number_value,date_value,comment_value)"
                            + " values(?,?,?,?,?,?,?,?,?,?)";
                    pstmt = conn.prepareStatement(sqlInsert);
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
                    logger.debug("Inserted recored into FQA " + fqaId + " " + form.getFormId());
                }

                //                if (!respDefType.equalsIgnoreCase(QuestionResponseData.FILE)
                //                        &&
                // !respDefType.equalsIgnoreCase(QuestionResponseData.COMMENT)
                //                        && delete) {
                //                    logger.debug("\n\n Delete associated files");
                //                    Object[] list =
                // qrd.getQuestionAttachments().values().toArray();
                //                    int size = list.length;
                //                    for (int i = 0; i < size; i++) {
                //                        QuestionAttachment qa = (QuestionAttachment) list[i];
                //                        qa.setToBeDeleted(true);
                //                    }
                //                    AttachmentHelper ah = new AttachmentHelper();
                //                    ah.saveAttachments(qrd, grant, form, conn);
                //                }

            }

            conn.commit();

        } catch (Exception se) {

            if (conn != null) {
                try {
                    System.err.print("Transaction is being ");
                    System.err.println("rolled back");
                    conn.rollback();
                    throw new GreensheetBaseException("errorSavingData", se);
                } catch (SQLException excep) {
                    System.err.print("SQLException: ");
                    System.err.println(excep.getMessage());
                    throw new GreensheetBaseException("errorSavingData", se);
                }
            }

        } finally {
            try {

                if (stmt != null)
                    stmt.close();
                if (pstmt != null)
                    pstmt.close();

            } catch (SQLException se) {
                throw new GreensheetBaseException("errorSavingData", se);
            }
            if (conn != null)
                DbConnectionHelper.getInstance().freeConnection(conn);

        }

    }

    private void checkForDuplicateNewForms(GreensheetForm form, GsGrant g)
            throws GreensheetBaseException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            conn = DbConnectionHelper.getInstance().getConnection();

            String type = form.getGroupTypeAsString();
            int applId = Integer.parseInt(g.getApplId());
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
                if (returned > 0) { throw new GreensheetBaseException(
                        "Error Saving Duplicate Greensheet Forms. Only one new Greensheetform for a grant can be created"); }

            }

        } catch (SQLException se) {
            throw new GreensheetBaseException(
                    "Error Saving Duplicate Greensheet Forms. Only one new Greensheetform for a grant can be created",
                    se);

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                throw new GreensheetBaseException("Database Connection Error", se);
            }
            DbConnectionHelper.getInstance().freeConnection(conn);

        }

    }

}