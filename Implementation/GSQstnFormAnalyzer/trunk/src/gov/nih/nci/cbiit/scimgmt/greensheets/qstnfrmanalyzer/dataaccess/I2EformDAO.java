package gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer.dataaccess;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class I2EformDAO extends JdbcDaoSupport {

	public static final String GET_FORM_COUNT_SQL = 
			" SELECT count(DISTINCT ft.id) " +
					" FROM forms_t ft  " +
					"   JOIN ( " +
					"     SELECT aft.frm_id, pgi.appl_type_code, pgi.activity_code " +
					"       FROM appl_forms_t aft JOIN pv_grant_pi_t1 pgi ON aft.appl_id = pgi.appl_id " +
					"     UNION  " +
					"     SELECT aft.frm_id, agt.appl_type_code, agt.activity_code " +
					"       FROM appl_forms_t aft JOIN appl_gm_actions_t agt ON aft.control_full_grant_num " +
					"         = agt.expected_grant_num " +
					"       WHERE agt.action_type = 'AWARD' " +
					"       ) gf " +
					"     ON ft.ID = gf.frm_id " +
					" WHERE " +
					"   ft.create_date >= to_date('01-OCT-2008') " +
					"   AND ft.form_role_code = ? " +
					"   AND gf.appl_type_code = ? " +
					"   AND gf.activity_code = ? ";
	
	public long determineCount(String formStaffRole, String typeMech) {
		String type = typeMech.substring(0, 1);
		String mech = typeMech.substring(1);
		Object[] args = new Object[3];
		args[0] = formStaffRole; args[1] = type; args[2] = mech;
		Long queryresult = this.getJdbcTemplate().queryForObject(GET_FORM_COUNT_SQL, args, Long.class);
		return queryresult.longValue();
	}

}
