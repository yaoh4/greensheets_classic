package gov.nih.nci.cbiit.atsc.dao.spring;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;

public class FormGrantRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		FormGrant formGrant = new FormGrant();

		formGrant.setDummy(rs.getString("DUMMY_FLAG"));
		formGrant.setOnControl(rs.getString("ON_CONTROL_FLAG"));
		formGrant.setElectronicallySubmitted(rs.getString("ELECTRONIC_SUBMISSION_FLAG"));
		formGrant.setApplId(rs.getLong("APPL_ID"));
		formGrant.setFullGrantNum(rs.getString("FULL_GRANT_NUM"));
		formGrant.setRfaPaNumber(rs.getString("RFA_PA_NUMBER"));
		formGrant.setCouncilMeetingDate(rs.getString("COUNCIL_MEETING_DATE"));
		formGrant.setPiFirstName(rs.getString("FIRST_NAME"));
		formGrant.setPiMiddleName(rs.getString("MI_NAME"));
		formGrant.setPiLastName(rs.getString("LAST_NAME"));
		formGrant.setPiName(rs.getString("PI_NAME"));
		formGrant.setIrgPercentileNum(rs.getInt("IRG_PERCENTILE_NUM"));
		formGrant.setPriorityScoreNum(rs.getInt("PRIORITY_SCORE_NUM"));
		formGrant.setApplTypeCode(rs.getString("APPL_TYPE_CODE"));
		formGrant.setAdminPhsOrgCode(rs.getString("ADMIN_PHS_ORG_CODE"));
		formGrant.setActivityCode(rs.getString("ACTIVITY_CODE"));
		formGrant.setSerialNum(rs.getInt("SERIAL_NUM"));
		formGrant.setSupportYear(rs.getInt("SUPPORT_YEAR"));
		formGrant.setSuffixCode(rs.getString("SUFFIX_CODE"));
		formGrant.setApplStatusCode(rs.getString("APPL_STATUS_CODE"));
		formGrant.setApplStatusGroupCode(rs.getString("APPL_STATUS_GROUP_CODE"));
		formGrant.setFormerNum(rs.getString("FORMER_NUM"));
		formGrant.setBudgetStartDate(rs.getDate("BUDGET_START_DATE"));
		formGrant.setLatestBudgetStartDate(rs.getDate("LATEST_BUDGET_START_DATE"));
		formGrant.setFy(rs.getInt("FY"));
		formGrant.setIpf(rs.getInt("IPF"));
		formGrant.setOrgName(rs.getString("ORG_NAME"));
		formGrant.setWithinPaylineFlag(rs.getString("WITHIN_PAYLINE_FLAG"));
		formGrant.setCayCode(rs.getString("CAY_CODE"));
		formGrant.setRoleUsageCode(rs.getString("ROLE_USAGE_CODE"));
		formGrant.setPdNpeId(rs.getLong("PD_NPE_ID"));
		formGrant.setPdNpnId(rs.getLong("PD_NPN_ID"));
		formGrant.setPdLastName(rs.getString("PD_LAST_NAME"));
		formGrant.setPdFirstName(rs.getString("PD_FIRST_NAME"));
		formGrant.setPdMiddleName(rs.getString("PD_MI_NAME"));
		formGrant.setPdUserId(rs.getString("PD_USER_ID"));
		formGrant.setGmsCode(rs.getString("GMS_CODE"));
		formGrant.setGmsNpeId(rs.getLong("GMS_NPE_ID"));
		formGrant.setGmsNpnId(rs.getLong("GMS_NPN_ID"));
		formGrant.setGmsLastName(rs.getString("GMS_LAST_NAME"));
		formGrant.setGmsFirstName(rs.getString("GMS_FIRST_NAME"));
		formGrant.setGmsMiddleName(rs.getString("GMS_MI_NAME"));
		formGrant.setGmsUserId(rs.getString("GMS_USER_ID"));
		formGrant.setBkupGmsCode(rs.getString("BKUP_GMS_CODE"));
		formGrant.setBkupGmsNpeId(rs.getLong("BKUP_GMS_NPE_ID"));
		formGrant.setBkupGmsNpnId(rs.getLong("BKUP_GMS_NPN_ID"));
		formGrant.setBkupGmsLastName(rs.getString("BKUP_GMS_LAST_NAME"));
		formGrant.setBkupGmsFirstName(rs.getString("BKUP_GMS_FIRST_NAME"));
		formGrant.setBkupGmsMiddleName(rs.getString("BKUP_GMS_MI_NAME"));
		formGrant.setBkupGmsUserId(rs.getString("BKUP_GMS_USER_ID"));
		formGrant.setAllGmsUserIds(rs.getString("ALL_GMS_USERIDS"));
		formGrant.setPgmFormStatus(rs.getString("PGM_FORM_STATUS"));
		formGrant.setPgmFormSubmittedDate(rs.getDate("PGM_FORM_SUBMITTED_DATE"));
		formGrant.setSpecFormStatus(rs.getString("SPEC_FORM_STATUS"));
		formGrant.setSpecFormSubmittedDate(rs.getDate("SPEC_FORM_SUBMITTED_DATE"));
		formGrant.setDmFormStatus(rs.getString("DM_FORM_STATUS"));
		formGrant.setMinority(rs.getString("MB_MINORITY_FLAG"));
		formGrant.setPgmGsReadyFlag(rs.getString("PGM_GS_READY_FLAG"));
		int GpmatsDayCountNum = rs.getInt("GPMATS_DAY_COUNT_NUM");

		if (rs.wasNull()) {
			formGrant.setGpmatsDayCountNum(null);
		} else {
			formGrant.setGpmatsDayCountNum(GpmatsDayCountNum + "");
		}

		return formGrant;
	}
}
