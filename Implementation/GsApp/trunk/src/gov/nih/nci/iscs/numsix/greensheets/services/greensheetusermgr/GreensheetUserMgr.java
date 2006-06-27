/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

/**
 * 
 * Provides services for retrieving GsUser objects that represent the user
 * currently using the application.
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public interface GreensheetUserMgr {

	/**
	 * Method findUserByRemoteUserName.
	 * 
	 * @param remoteUserName
	 * @return GsUser
	 */
	public GsUser findUserByUserName(String remoteUserName)
			throws GreensheetBaseException;

}
