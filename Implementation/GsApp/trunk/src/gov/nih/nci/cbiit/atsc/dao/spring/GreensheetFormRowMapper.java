package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.GreensheetForm;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GreensheetFormRowMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		GreensheetForm greensheetForm = new GreensheetForm();
		
		greensheetForm.setId(rs.getInt("FRM_ID"));
		greensheetForm.setFtmId(rs.getInt("FTM_ID"));
		greensheetForm.setFormRoleCode(rs.getString("FORM_ROLE_CODE"));
		greensheetForm.setFormStatus(rs.getString("FORM_STATUS"));
		greensheetForm.setPoc(rs.getString("POC"));
		greensheetForm.setSubmittedUserId(rs.getString("SUBMITTED_USER_ID"));
		greensheetForm.setCreateUserId(rs.getString("CREATE_USER_ID"));
		greensheetForm.setCreateDate(rs.getDate("CREATE_DATE"));
		greensheetForm.setLastChangeUserId(rs.getString("LAST_CHANGE_USER_ID"));
		greensheetForm.setUpdateStamp(rs.getInt("UPDATE_STAMP"));
		greensheetForm.setSubmittedDate(rs.getDate("SUBMITTED_DATE"));

		return greensheetForm;
	}
}
