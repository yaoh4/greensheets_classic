/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.grantmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.Map;

/**
 * 
 * Test Implementation of GrantMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr
 * 
 * @author kpuscas, Number Six Software
 */
public class GrantMgrTestImpl implements GrantMgr {

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#findGrantByApplId(String)
	 */
	public GsGrant findGrantByApplId(String applId)
			throws GreensheetBaseException {
		return null;
	}

	public GsGrant findGrantByGrantNumber(String grantNumber)
			throws GreensheetBaseException {
		return null;
	}	
	
	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#getGrantsListForUser(GsUser,
	 *      boolean)
	 */
	public Map getGrantsListForUser(GsUser user, boolean paylineOnly,
			boolean myPortfolio) throws GreensheetBaseException {
		return null;
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#searchForGrant(String,
	 *      String, GsUser)
	 */
	public Map searchForGrant(String searchType, String searchText, GsUser user)
			throws GreensheetBaseException {
		return null;
	}

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#findGrantById(String)
	 */
	public GsGrant findGrantById(String applId, String grantNumber)
			throws GreensheetBaseException {
		return null;
	}

//	 Bug#4204 Abdul: Commented out this for the two new fields lastName and firstName	
//	public Map getGrantsListForProgramUser(GsUser user, String grantSource,
//			String grantType, String mechanism, String onlyGrantsWithinPayline,
//			String grantNumber, String piName) throws GreensheetBaseException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public Map getGrantsListForProgramUser(GsUser user, String grantSource,
			String grantType, String mechanism, String onlyGrantsWithinPayline,
			String grantNumber, String lastName, String firstName) throws GreensheetBaseException {
		// TODO Auto-generated method stub
		return null;
	}
	// Bug # 4366 Anand 
	public Map searchForProgramUserGrantList(GsUser user, String grantSource,
			String grantType, String mechanism, String onlyGrantsWithinPayline,
			String grantNumber, String lastName, String firstName) throws GreensheetBaseException {
		return null;
	}
	
}
