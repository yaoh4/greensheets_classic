package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.GrantDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class GrantDAOImpl implements GrantDAO {
    private static final Logger logger = Logger.getLogger(GrantDAOImpl.class);
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    // Following is the result set the SPECIALIST gets by default when logging
    // in.
    public List findGrants(String nciOracleId, Calendar latestBudgetStartDate,
            Calendar latestBudgetEndDate, boolean onControlFlag,
            boolean restrictedToOpenForms, boolean restrictedToLoggedinUser) {

        MapSqlParameterSource sqlParms = new MapSqlParameterSource();

        String baseQuery = "SELECT " + GrantDAOImpl.FORM_GRANT_VW_SELECT_COLS
                + " FROM " + GrantDAOImpl.SOURCE_DB_OBJECTS;

        String incrementalQuery = baseQuery + " WHERE ";

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        String budgetStartDate = dateFormatter.format(latestBudgetStartDate.getTime());
        /* latestBudgetStartDate
            .get(Calendar.DAY_OF_MONTH)
            + "-"
            + latestBudgetStartDate.get(Calendar.MONTH)
            + "-"
            + latestBudgetStartDate.get(Calendar.YEAR); */// Replaced by Anatoli

        String budgetEndDate = dateFormatter.format(latestBudgetEndDate.getTime());
        /* latestBudgetEndDate.get(Calendar.DAY_OF_MONTH)
            + "-" + latestBudgetEndDate.get(Calendar.MONTH) + "-"
            + latestBudgetEndDate.get(Calendar.YEAR); */// Replaced by Anatoli

        // Should be the first condition because the AND keyword is not prefixed
        // to the predicate.
        if (latestBudgetStartDate != null && latestBudgetEndDate != null) {
            incrementalQuery += "(LATEST_BUDGET_START_DATE BETWEEN TO_DATE(:latestBudgetStartDate, 'DD-MON-YYYY') AND TO_DATE(:latestBudgetEndDate, 'DD-MON-YYYY'))";
            sqlParms.addValue("latestBudgetStartDate", budgetStartDate);
            sqlParms.addValue("latestBudgetEndDate", budgetEndDate);
        }

        if (onControlFlag) {
            incrementalQuery += GrantDAOImpl.BLANK_SPACE
                    + "AND ON_CONTROL_FLAG = :onControlFlag";
            sqlParms.addValue("onControlFlag", "Y");
        }

        if (restrictedToOpenForms) {
            incrementalQuery += GrantDAOImpl.BLANK_SPACE
                    + "AND SPEC_FORM_STATUS NOT IN (:specFormStatusSubmitted, :specFormStatusFrozen)";
            sqlParms.addValue("specFormStatusSubmitted", "SUBMITTED");
            sqlParms.addValue("specFormStatusFrozen", "FROZEN");
        }

        if (restrictedToLoggedinUser) {
            incrementalQuery += GrantDAOImpl.BLANK_SPACE
                    + "AND UPPER(ALL_GMS_USERIDS) LIKE :nciOracleId";
            sqlParms.addValue("nciOracleId", "%" + nciOracleId.trim().toUpperCase() + "%");
        }

        String sortOrder = "LATEST_BUDGET_START_DATE ASC";

        String completeQuery = incrementalQuery + " ORDER BY " + sortOrder;

        logger.debug("SQL to read the Form Grant: " + completeQuery);
        if (logger.getEffectiveLevel().equals(Level.DEBUG)) {
            SpringDAOUtil.writeQueryParamsToLog(sqlParms);
        }

        List formGrantsList = this.namedParameterJdbcTemplate.query(
                completeQuery, sqlParms, new FormGrantRowMapper());

        return formGrantsList;
    }

    public List findGrantsByGrantNum(String fullGrantNum) {
        String baseQuery = "SELECT " + GrantDAOImpl.FORM_GRANT_VW_SELECT_COLS
                + " FROM " + GrantDAOImpl.SOURCE_DB_OBJECTS;
        MapSqlParameterSource sqlParms = new MapSqlParameterSource();
        String incrementalQuery = baseQuery + " WHERE ";

        if (fullGrantNum == null || "".equals(fullGrantNum.trim())) {
            return null;
        }
        incrementalQuery += "(UPPER(FULL_GRANT_NUM) LIKE :fullGrantNum";
        sqlParms.addValue("fullGrantNum", "%"
                + fullGrantNum.trim().toUpperCase() + "%");

        if (fullGrantNum.contains("-")) {
            String suffixFullGrantNum = "";
            String[] splits = fullGrantNum.split("-");
            if (splits.length > 1) {
                if (splits[1].toUpperCase().contains("S")) {
                    suffixFullGrantNum = replaceLast(fullGrantNum, "S", "W");
                } else if (splits[1].toUpperCase().contains("W")) {
                    suffixFullGrantNum = replaceLast(fullGrantNum, "W", "S");

                }
            }
            if (suffixFullGrantNum.length() > 0) {
                incrementalQuery += GrantDAOImpl.BLANK_SPACE
                        + "OR UPPER(FULL_GRANT_NUM) LIKE :suffixFullGrantNum";
                sqlParms.addValue("suffixFullGrantNum", "%"
                        + suffixFullGrantNum.trim().toUpperCase() + "%");
            }
        }

        incrementalQuery += ")";
        
        String sortOrder = "LATEST_BUDGET_START_DATE ASC";

        String completeQuery = incrementalQuery + " ORDER BY " + sortOrder;

        logger.debug("SQL: " + completeQuery);
        List formGrantsList = this.namedParameterJdbcTemplate.query(
                completeQuery, sqlParms, new FormGrantRowMapper());

        return formGrantsList;
    }

    public String replaceLast(String original, String target, String replacement) {

        int index = original.lastIndexOf(target);

        if (index == -1) {
            return original;
        }

        return original.substring(0, index) + replacement + original.substring(index + target.length());
    }

    public List retrieveGrantsByFullGrantNum(String fullGrantNum) {
        String baseQuery = "SELECT " + GrantDAOImpl.FORM_GRANT_VW_SELECT_COLS
                + " FROM " + GrantDAOImpl.SOURCE_DB_OBJECTS;
        MapSqlParameterSource sqlParms = new MapSqlParameterSource();
        String incrementalQuery = baseQuery + " WHERE ";

        if (fullGrantNum == null || "".equals(fullGrantNum.trim())) {
            return null;
        }
        incrementalQuery += "UPPER(FULL_GRANT_NUM) = :fullGrantNum";
        sqlParms.addValue("fullGrantNum", fullGrantNum.trim().toUpperCase());


        String sortOrder = "LATEST_BUDGET_START_DATE ASC";

        String completeQuery = incrementalQuery + " ORDER BY " + sortOrder;

        logger.debug("SQL: " + completeQuery);
        List formGrantsList = this.namedParameterJdbcTemplate.query(
                completeQuery, sqlParms, new FormGrantRowMapper());

        return formGrantsList;
    }

    public List findGrantsByApplId(long applId) { // Task to do: Check which is
                                                  // more appropriate: long or
                                                  // Long. -- Abdul Latheef
        String baseQuery = "SELECT " + GrantDAOImpl.FORM_GRANT_VW_SELECT_COLS
                + " FROM " + GrantDAOImpl.SOURCE_DB_OBJECTS;
        MapSqlParameterSource sqlParms = new MapSqlParameterSource();
        String incrementalQuery = baseQuery + " WHERE ";

        if (applId <= 0) {
            return null;
        }
        incrementalQuery += "APPL_ID = :applId";
        sqlParms.addValue("applId", applId);

        String sortOrder = "LATEST_BUDGET_START_DATE ASC";

        String completeQuery = incrementalQuery + " ORDER BY " + sortOrder;

        logger.debug("SQL: " + completeQuery);
        List formGrantsList = this.namedParameterJdbcTemplate.query(
                completeQuery, sqlParms, new FormGrantRowMapper());

        return formGrantsList;
    }

    public List findGrantsByPiLastName(String piLastName, String adminPhsOrgCode) {
        // All grants whose PI Last names start with the user's search text.
        String baseQuery = "SELECT " + GrantDAOImpl.FORM_GRANT_VW_SELECT_COLS
                + " FROM " + GrantDAOImpl.SOURCE_DB_OBJECTS;
        MapSqlParameterSource sqlParms = new MapSqlParameterSource();
        String incrementalQuery = baseQuery + " WHERE ";

        if (piLastName == null || "".equals(piLastName.trim())) {
            return null;
        } else {
            if (adminPhsOrgCode != null && !("".equals(adminPhsOrgCode.trim()))) {
                incrementalQuery += "ADMIN_PHS_ORG_CODE = :adminPhsOrgCode";
                sqlParms.addValue("adminPhsOrgCode", adminPhsOrgCode.trim());
            }

            incrementalQuery += "UPPER(LAST_NAME) LIKE :piLastName";
            sqlParms.addValue("piLastName", piLastName.trim().toUpperCase());
        }

        String sortOrder = "LATEST_BUDGET_START_DATE ASC";

        String completeQuery = incrementalQuery + " ORDER BY " + sortOrder;

        logger.debug("SQL: " + completeQuery);
        List formGrantsList = this.namedParameterJdbcTemplate.query(
                completeQuery, sqlParms, new FormGrantRowMapper());
        // See if you can use Generics with this API method.

        return formGrantsList;
    }

    // PD query.
    public List findGrants(Calendar latestBudgetStartDate,
            Calendar latestBudgetEndDate, boolean restrictedToOpenForms,
            String applStatusGroupCode, boolean minorityGrantsIncluded,
            List userPortfolioIds, List userCancerActivities, String grantType,
            boolean includeOnlyGrantsWithinPayline, String grantMechanism,
            String fullGrantNum, String piLastName, String piFirstName) {

        MapSqlParameterSource sqlParms = new MapSqlParameterSource();

        String baseQuery = "SELECT " + GrantDAOImpl.FORM_GRANT_VW_SELECT_COLS
                + " FROM " + GrantDAOImpl.SOURCE_DB_OBJECTS;

        String incrementalQuery = baseQuery + " WHERE ";
        // boolean firstCondition = true;

        // Should be the first condition because the AND keyword is not prefixed
        // to the predicate.

        incrementalQuery += "LATEST_BUDGET_START_DATE >= TO_DATE('01/06/2004','DD-MM-YYYY')";

        if ((fullGrantNum == null && piLastName == null && piFirstName == null)
                || (fullGrantNum.trim().length() == 0 && piLastName.trim().length() == 0 && piFirstName.trim().length() == 0)) {

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
            /*
            String budgetStartDate = latestBudgetStartDate
                    .get(Calendar.DAY_OF_MONTH)
                    + "-"
                    + latestBudgetStartDate.get(Calendar.MONTH)
                    + "-"
                    + latestBudgetStartDate.get(Calendar.YEAR);
            */
            String budgetStartDate = dateFormatter.format(latestBudgetStartDate.getTime());
            // ^ Replaced by Anatoli Sep. 11, 2012

            /*
            String budgetEndDate = latestBudgetEndDate.get(Calendar.DAY_OF_MONTH)
                    + "-" + latestBudgetEndDate.get(Calendar.MONTH) + "-"
                    + latestBudgetEndDate.get(Calendar.YEAR);
            */
            String budgetEndDate = dateFormatter.format(latestBudgetEndDate.getTime());
            // ^ Replaced by Anatoli Sep. 11, 2012

            if (latestBudgetStartDate != null && latestBudgetEndDate != null) {

                incrementalQuery += GrantDAOImpl.BLANK_SPACE
                        + "AND (LATEST_BUDGET_START_DATE BETWEEN TO_DATE(:latestBudgetStartDate, 'DD-MON-YYYY') AND TO_DATE(:latestBudgetEndDate, 'DD-MON-YYYY'))";
                sqlParms.addValue("latestBudgetStartDate", budgetStartDate);
                sqlParms.addValue("latestBudgetEndDate", budgetEndDate);
            }

            if (restrictedToOpenForms) {
                incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND PGM_FORM_STATUS NOT IN (:pgmFormStatusSubmitted, :pgmFormStatusFrozen)";
                sqlParms.addValue("pgmFormStatusSubmitted", "SUBMITTED");
                sqlParms.addValue("pgmFormStatusFrozen", "FROZEN");
            }

            incrementalQuery += GrantDAOImpl.BLANK_SPACE
                    + "AND (APPL_STATUS_GROUP_CODE IN ('PA', 'TP') OR (APPL_STATUS_GROUP_CODE = 'PC' AND APPL_STATUS_CODE != '25')" +
                    		" OR DUMMY_FLAG = 'Y')";

        }

        if (minorityGrantsIncluded) {

            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND MB_MINORITY_FLAG = :mbMinorityFlag";
            sqlParms.addValue("mbMinorityFlag", "Y");
        }

        if (userPortfolioIds != null) {

            Set userPortfoliosSet = new HashSet();

            for (int index = 0; index < userPortfolioIds.size(); index++) {

                userPortfoliosSet.add(String.valueOf(userPortfolioIds.get(index)));
            }
            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND PD_NPE_ID IN (:pdNpeId)";
            sqlParms.addValue("pdNpeId", userPortfoliosSet);

        }

        if (userCancerActivities != null) {

            Set userCancerActivityCodesSet = new HashSet();

            for (int index = 0; index < userCancerActivities.size(); index++) {

                userCancerActivityCodesSet.add(String.valueOf(userCancerActivities.get(index)));
            }

            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND CAY_CODE IN (:cayCodes)";
            sqlParms.addValue("cayCodes", userCancerActivityCodesSet);

        }

        if (grantType != null) {
            if ("competinggrants".equalsIgnoreCase(grantType.trim())) {
                // Remove the dependency on Constants.PREFERENCES_COMPETINGGRANTS.Task to do: Introduce Enums later on.
                incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND (NOT(COUNCIL_MEETING_DATE LIKE :councilMeetingDate))";
                sqlParms.addValue("councilMeetingDate", "%00");
            } else if ("noncompetinggrants".equalsIgnoreCase(grantType.trim())) {
                incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND COUNCIL_MEETING_DATE LIKE :councilMeetingDate";
                sqlParms.addValue("councilMeetingDate", "%00");
            } else if ("both".equalsIgnoreCase(grantType.trim())) {
                // As of writing this code, in the TEST environment,
                // DISTINCT COUNCIL_MEETING_DATE values are (null), 00, and lot
                // more in the format of YYYYMM.
                // Current code makes two SQL requests - one to read Competing
                // Grants and another to read non-Competing Grants.
                // But, there are only 4 rows with null values in
                // COUNCIL_MEETING_DATE column
                // and 4 rows with 00. Basically these are undetermined.
                // Let's ignore the condition altogether and include these FEW
                // undetermined grants as well in the results.
                // This avoids the need to make the double requests to the DB or
                // making the SQL unnecessarily complicated.

                if (includeOnlyGrantsWithinPayline) {
                    incrementalQuery += GrantDAOImpl.BLANK_SPACE
                            + "AND ((( NOT(COUNCIL_MEETING_DATE LIKE :councilMeetingDate)) AND WITHIN_PAYLINE_FLAG = 'Y') OR (COUNCIL_MEETING_DATE LIKE :councilMeetingDate))";
                    sqlParms.addValue("councilMeetingDate", "%00");
                } else if (!includeOnlyGrantsWithinPayline) {
                    /*
                    incrementalQuery += GrantDAOImpl.BLANK_SPACE
                            + "AND (((NOT(COUNCIL_MEETING_DATE LIKE :councilMeetingDate))) OR (COUNCIL_MEETING_DATE LIKE :councilMeetingDate))";
                    sqlParms.addValue("councilMeetingDate", "%00");
                    */
                    /* Commented out by Anatoli on Sep. 11, 2012: the condition was always true, so not needed  */
                }
            }
        }

        // Ignore the condition if the Grant type is null (same as before) or if
        // it is for Non-Competing

        if (includeOnlyGrantsWithinPayline && grantType != null
                && ("competinggrants".equalsIgnoreCase(grantType.trim()))) {
            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND WITHIN_PAYLINE_FLAG = :withinPaylineFlag";
            sqlParms.addValue("withinPaylineFlag", "Y");
        } else if (!includeOnlyGrantsWithinPayline && grantType != null
                && ("competinggrants".equalsIgnoreCase(grantType.trim()))) {
            // For now, we treat null as N for withinPaylineFlag
            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND WITHIN_PAYLINE_FLAG != :withinPaylineFlag";
            sqlParms.addValue("withinPaylineFlag", "Y");
        }

        if (grantMechanism != null && !("".equals(grantMechanism.trim()))) {
            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND UPPER(ACTIVITY_CODE) = UPPER(:grantMechanism)";
            sqlParms.addValue("grantMechanism", grantMechanism.trim());
        }

        if (fullGrantNum != null && !("".equals(fullGrantNum.trim()))) {
            fullGrantNum = fullGrantNum.toUpperCase();
            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND (UPPER(FULL_GRANT_NUM) LIKE UPPER(:fullGrantNum)";
            sqlParms.addValue("fullGrantNum", "%" + fullGrantNum.trim() + "%");

            if (fullGrantNum.contains("-")) {
                String suffixFullGrantNum = "";
                String[] splits = fullGrantNum.split("-");
                if (splits.length > 1) {
                    if (splits[1].toUpperCase().contains("S")) {
                        suffixFullGrantNum = replaceLast(fullGrantNum, "S", "W");
                    } else if (splits[1].toUpperCase().contains("W")) {
                        suffixFullGrantNum = replaceLast(fullGrantNum, "W", "S");

                    }
                    if (suffixFullGrantNum.length() > 0) {
                        incrementalQuery += GrantDAOImpl.BLANK_SPACE
                                + "OR UPPER(FULL_GRANT_NUM) LIKE UPPER(:suffixFullGrantNum)";
                        sqlParms.addValue("suffixFullGrantNum", "%"
                                + suffixFullGrantNum.trim().toUpperCase() + "%");
                    }
                }
            }
            
            incrementalQuery += ")";
        }

        if (piLastName != null && !("".equals(piLastName.trim()))) {
            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND UPPER(LAST_NAME) LIKE UPPER(:piLastName)";
            sqlParms.addValue("piLastName", piLastName.trim() + "%");
        }

        if (piFirstName != null && !("".equals(piFirstName.trim()))) {
            incrementalQuery += GrantDAOImpl.BLANK_SPACE + "AND UPPER(FIRST_NAME) LIKE UPPER(:piFirstName)";
            sqlParms.addValue("piFirstName", piFirstName.trim() + "%");
        }

        String sortOrder = "LATEST_BUDGET_START_DATE ASC";

        String completeQuery = incrementalQuery + " ORDER BY " + sortOrder;

        logger.debug("SQL: " + completeQuery);
        if (logger.getEffectiveLevel().equals(Level.DEBUG)) {
            SpringDAOUtil.writeQueryParamsToLog(sqlParms);
        }

        List formGrantsList = this.namedParameterJdbcTemplate.query(
                completeQuery, sqlParms, new FormGrantRowMapper());
        // See if you can use Generics with this API method.

        return formGrantsList;
    }

    private static final String FORM_GRANT_VW_SELECT_COLS = "DUMMY_FLAG"
            + ", ON_CONTROL_FLAG" + ", ELECTRONIC_SUBMISSION_FLAG"
            + ", APPL_ID" + ", FULL_GRANT_NUM" + ", RFA_PA_NUMBER"
            + ", COUNCIL_MEETING_DATE" + ", FIRST_NAME" + ", MI_NAME"
            + ", LAST_NAME" + ", PI_NAME" + ", IRG_PERCENTILE_NUM"
            + ", PRIORITY_SCORE_NUM" + ", APPL_TYPE_CODE"
            + ", ADMIN_PHS_ORG_CODE" + ", ACTIVITY_CODE" + ", SERIAL_NUM"
            + ", SUPPORT_YEAR" + ", SUFFIX_CODE" + ", APPL_STATUS_CODE"
            + ", APPL_STATUS_GROUP_CODE" + ", FORMER_NUM"
            + ", BUDGET_START_DATE" + ", LATEST_BUDGET_START_DATE" + ", FY"
            + ", IPF" + ", ORG_NAME" + ", WITHIN_PAYLINE_FLAG" + ", CAY_CODE"
            + ", ROLE_USAGE_CODE" + ", PD_NPE_ID" + ", PD_NPN_ID"
            + ", PD_LAST_NAME" + ", PD_FIRST_NAME" + ", PD_MI_NAME"
            + ", PD_USER_ID" + ", GMS_CODE" + ", GMS_NPE_ID" + ", GMS_NPN_ID"
            + ", GMS_LAST_NAME" + ", GMS_FIRST_NAME" + ", GMS_MI_NAME"
            + ", GMS_USER_ID" + ", BKUP_GMS_CODE" + ", BKUP_GMS_NPE_ID"
            + ", BKUP_GMS_NPN_ID" + ", BKUP_GMS_LAST_NAME"
            + ", BKUP_GMS_FIRST_NAME" + ", BKUP_GMS_MI_NAME"
            + ", BKUP_GMS_USER_ID" + ", ALL_GMS_USERIDS" + ", PGM_FORM_STATUS"
            + ", PGM_FORM_SUBMITTED_DATE" + ", SPEC_FORM_STATUS"
            + ", SPEC_FORM_SUBMITTED_DATE" + ", DM_FORM_STATUS"
            + ", MB_MINORITY_FLAG";

    private static final String SOURCE_DB_OBJECTS = "FORM_GRANT_VW";
    private static final String BLANK_SPACE = " ";
}
