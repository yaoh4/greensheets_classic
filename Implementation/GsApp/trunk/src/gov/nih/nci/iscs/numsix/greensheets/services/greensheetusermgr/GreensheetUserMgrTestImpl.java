/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

/**
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetUserMgrTestImpl implements GreensheetUserMgr {

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr#findUserByOracleId(String)
	 */
	public GsUser findUserByOracleId(String oracleId)
			throws GreensheetBaseException {
		GsUser user = new GsUser();
		user.setOracleId(oracleId);
		user.setRole(GsUserRole.PGM_DIR);
		user.setCancerActivities("'BP'");
		return user;
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr#findUserByRemoteUserName(String)
	 */
	public GsUser findUserByUserName(String remoteUserName)
			throws GreensheetBaseException {

		NciPerson p = new NciPerson();
		p.setFirstName("Test");
		p.setLastName("User");
		GsUser user = new GsUser(p);
		user.setOracleId(remoteUserName);
		user.setRole(GsUserRole.PGM_DIR);
		user.setCancerActivities("'BP'");

		return user;
	}

}
