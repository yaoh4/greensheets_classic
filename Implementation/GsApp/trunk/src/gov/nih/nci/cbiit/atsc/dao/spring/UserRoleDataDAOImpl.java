package gov.nih.nci.cbiit.atsc.dao.spring;

import gov.nih.nci.cbiit.atsc.dao.UserRoleDataDAO;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

public class UserRoleDataDAOImpl implements UserRoleDataDAO {

	private static final Logger logger = Logger
	.getLogger(UserRoleDataDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall procUserRolesActivities;

	private SimpleJdbcCall procUserPortfolioIDs;

	private SimpleJdbcCall procUserRolesGsFormBuilderActivities;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcTemplate.setResultsMapCaseInsensitive(true);
		//String GsFormBuilderUserRole = null;
		this.procUserRolesActivities = new SimpleJdbcCall(jdbcTemplate)
		.withProcedureName("get_role_param_list")
		.withCatalogName("notifications_pkg")
		.withoutProcedureColumnMetaDataAccess()
		.declareParameters(new SqlParameter("p_userid", Types.VARCHAR),
				new SqlParameter("p_flag", Types.VARCHAR),
				new SqlOutParameter("p_role", Types.VARCHAR),
				new SqlOutParameter("p_npe_id_or_cc", Types.VARCHAR),
				new SqlOutParameter("p_role_usage_code", Types.VARCHAR));

		this.procUserPortfolioIDs = new SimpleJdbcCall(jdbcTemplate)
		.withProcedureName("get_user_pd_ca_details").withCatalogName("notifications_pkg")
		.withoutProcedureColumnMetaDataAccess().declareParameters(
				new SqlParameter("p_userid", Types.VARCHAR),
				new SqlOutParameter("p_pd_flag", Types.VARCHAR),
				new SqlOutParameter("p_ca_flag", Types.VARCHAR),
				new SqlOutParameter("p_npe_ids", Types.VARCHAR),
				new SqlOutParameter("p_ru_codes", Types.VARCHAR),
				new SqlOutParameter("p_ca_codes", Types.VARCHAR));
		// this.procUserRolesGsFormBuilderActivities = new SimpleJdbcCall(jdbcTemplate1).withFunctionName("get_company_name");
		this.procUserRolesGsFormBuilderActivities = new SimpleJdbcCall(jdbcTemplate)
		.withProcedureName("get_greensheets_user_role")
		.withCatalogName("forms_pkg")
		.withoutProcedureColumnMetaDataAccess()
		.declareParameters(
				new SqlParameter("p_userid", Types.VARCHAR),
				new SqlOutParameter("p_role_code", Types.VARCHAR));

	}

	public GsUser getUserRolesActivitiesNPEIDs(String nciOracleID) {
		String roles = null;
		String formBuilderRole = null;
		String cancerActivities = null;
		//		String roleUsageCodes = null; //Unused
		String userPortfolioIDs = null;

		MapSqlParameterSource inParams = new MapSqlParameterSource();
		//MapSqlParameterSource inParams1 = new MapSqlParameterSource();
		inParams.addValue("p_userid", nciOracleID);
		inParams.addValue("p_flag", "CA"); // CA is hard-coded.

		Map userRolesActivitiesMap = procUserRolesActivities.execute(inParams);
		roles = (String) userRolesActivitiesMap.get("p_role");
		cancerActivities = (String) userRolesActivitiesMap.get("p_npe_id_or_cc");
		//		roleUsageCodes = (String) userRolesActivitiesMap.get("p_role_usage_code");

		GsUser user = new GsUser();
		MapSqlParameterSource in = new MapSqlParameterSource();
		in.addValue("p_userid", nciOracleID); 
		// inParams1.addValue("p_userid", nciOracleID);
		Map gsFormBuilderRolesActivitiesMap=procUserRolesGsFormBuilderActivities.execute(in);
		//GsFormBuilderUserRole= (String) GsFormBuilderUserRoleMap.get("p_role");
		formBuilderRole = (String) gsFormBuilderRolesActivitiesMap.get("p_role_code");
		if ("PD".equalsIgnoreCase(roles)) {
			user.setRole(GsUserRole.PGM_DIR);
			user.setCancerActivities(cancerActivities);
		} else if ("PA".equalsIgnoreCase(roles)) {
			user.setRole(GsUserRole.PGM_ANL);
			user.setCancerActivities(cancerActivities);
		} else if ("GMSPEC".equalsIgnoreCase(roles)) {
			user.setRole(GsUserRole.SPEC);
		} else if ("GSDV".equalsIgnoreCase(roles)) {
			user.setRole(GsUserRole.GS_DV);
		} else if ("GSDA".equalsIgnoreCase(roles)) {
			user.setRole(GsUserRole.GS_DA);
		} else {
			user.setRole(GsUserRole.GS_GUEST);
		}

		if ("GSDV".equalsIgnoreCase(formBuilderRole)) {
			user.setFormBuilderRole(GsUserRole.GS_DV);
		} else if ("GSDA".equalsIgnoreCase(formBuilderRole)) {
			user.setFormBuilderRole(GsUserRole.GS_DA);
		} else {
			user.setFormBuilderRole(GsUserRole.GS_GUEST);
		}
		MapSqlParameterSource inParams2 = new MapSqlParameterSource();
		inParams2.addValue("p_userid", nciOracleID);

		Map userPortfolioIDsMap = procUserPortfolioIDs.execute(inParams2);

		userPortfolioIDs = (String) userPortfolioIDsMap.get("p_npe_ids");

		if (userPortfolioIDs != null) {
			user.setMyPortfolioIds(userPortfolioIDs);
		} else {
			user.setMyPortfolioIds("0");
		}

		return user;
	}
}
