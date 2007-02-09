/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.grantmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Provides services for finding GsGrant information
 * 
 * @author kpuscas, Number Six Software
 */

public interface GrantMgr {

	public GsGrant findGrantById(String applId, String grantNumber)
			throws GreensheetBaseException;

	/**
	 * Method getGrantsListForUser.
	 * 
	 * @param user
	 * @return Map
	 * @throws GreensheetBaseException
	 */
	public Map getGrantsListForUser(GsUser user, boolean paylineOnly,
			boolean myPortfolio) throws GreensheetBaseException;

	/**
	 * Method searchForGrant.
	 * 
	 * @param searchType
	 * @param searchText
	 * @return Map
	 * @throws GreensheetBaseException
	 */
	public Map searchForGrant(String searchType, String searchText, GsUser user)
			throws GreensheetBaseException;

//	/**
//	 * Method searchForGrant.
//	 * 
//	 * @param user
//	 * @param grantSource
//	 * @param grantType
//	 * @param mechanism
//	 * @param onlyGrantsWithinPayline
//	 * @param grantNumber
//	 * @param piName
//	 * @return Map
//	 * @throws GreensheetBaseException
//	 */
//	public Map getGrantsListForProgramUser(GsUser user, String grantSource,
//			String grantType, String mechanism, String onlyGrantsWithinPayline,
//			String grantNumber, String piName) throws GreensheetBaseException;

	/**
	 * Method searchForGrant.
	 * 
	 * @param user
	 * @param grantSource
	 * @param grantType
	 * @param mechanism
	 * @param onlyGrantsWithinPayline
	 * @param grantNumber
	 * @param lastName
	 * @param firstName 
	 * @return Map
	 * @throws GreensheetBaseException
	 */
	public Map getGrantsListForProgramUser(GsUser user, String grantSource,
			String grantType, String mechanism, String onlyGrantsWithinPayline,
			String grantNumber, String lastName, String firstName) throws GreensheetBaseException;	
	
	public Map searchForProgramUserGrantList(GsUser user, String grantSource,
			String grantType, String mechanism, String onlyGrantsWithinPayline,
			String grantNumber, String lastName, String firstName) throws GreensheetBaseException;
}
