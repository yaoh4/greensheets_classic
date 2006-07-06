/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetUserMgrImpl implements GreensheetUserMgr {

	private static final Logger logger = Logger
			.getLogger(GreensheetUserMgrImpl.class);

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr#findUserByRemoteUserName(String)
	 */
	public GsUser findUserByUserName(String userName)
			throws GreensheetBaseException {
		logger.debug("Find By username " + userName);

		NciPersonBuilder builder = new NciPersonBuilder();
		NciPerson np = builder.getPersonByUserName(userName);
		GsUser user = new GsUser(np);
		user.setRemoteUserName(userName);
		if (np.getOracleId() != null) {
			user.setOracleId(np.getOracleId());
			this.getUserDBData(user);
		} else {
			user.setRole(GsUserRole.GS_GUEST);
		}
		return user;
	}

	private void getUserDBData(GsUser user) throws GreensheetBaseException {

		logger.debug(user.getOracleId());
		Connection conn = null;
		CallableStatement cs = null;
		try {
			conn = DbConnectionHelper.getInstance().getConnection(
					user.getOracleId());
			cs = conn
					.prepareCall("{call notifications_pkg.get_role_param_list(?,?,?,?,?)}");

			// set the IN parameters
			if (user.getOracleId() == null) {
				throw new GreensheetBaseException("error.userid");
			} else {
				cs.setString(1, user.getOracleId());
			}
			cs.setString(2, "CA");

			// Set the OUT parameters to register
			cs.registerOutParameter(3, java.sql.Types.VARCHAR); // roles
			cs.registerOutParameter(4, java.sql.Types.VARCHAR); // cancer
																// activites
			cs.registerOutParameter(5, java.sql.Types.VARCHAR); // role_usage
																// codes. Not
																// Used

			// execute the stored procedure
			cs.execute();

			// get the data returned from the procedure
			String roles = cs.getString(3);
			String ca = cs.getString(4);
			logger.debug("roles:: " + roles + "    ca:: " + ca);
			conn.commit();

			// determine if role is PD ro PA otherwise will get role from Ldap

			if (roles.equalsIgnoreCase("PD")) {
				user.setRole(GsUserRole.PGM_DIR);
				user.setCancerActivities(ca);
			} else if (roles.equalsIgnoreCase("PA")) {
				user.setRole(GsUserRole.PGM_ANL);
				user.setCancerActivities(ca);
			} else if (roles.equalsIgnoreCase("GMSPEC")) {
				user.setRole(GsUserRole.SPEC);
			} else {
				user.setRole(GsUserRole.GS_GUEST);
			}

			logger.debug(user.getRoleAsString());

			// Now need to get the NpeId list for myportfolio

			cs = conn
					.prepareCall("{call notifications_pkg.get_user_pd_ca_details(?,?,?,?,?,?)}");
			cs.setString(1, user.getOracleId());

			// Set the OUT parameters to register
			cs.registerOutParameter(2, java.sql.Types.VARCHAR); // p_pd_flag.
																// Not Used
			cs.registerOutParameter(3, java.sql.Types.VARCHAR); // p_ca_flag.
																// Not Used
			cs.registerOutParameter(4, java.sql.Types.VARCHAR); // P_npe_ids.
			cs.registerOutParameter(5, java.sql.Types.VARCHAR); // p_ru_codes.
																// Not Used
			cs.registerOutParameter(6, java.sql.Types.VARCHAR); // p_ca_codes
																// codes. Not
																// Used

			cs.execute();
			String npeids = cs.getString(4);
			logger.debug("---------------> \n " + npeids);

			if (npeids != null) {
				user.setMyPortfolioIds(npeids);
			}

			conn.commit();

		} catch (SQLException e) {
			throw new GreensheetBaseException("error.userdatabase", e);
		} finally {
			try {
				if (cs != null) {
					cs.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				throw new GreensheetBaseException("error.userdatabase", e);
			}
		}
	}

}
