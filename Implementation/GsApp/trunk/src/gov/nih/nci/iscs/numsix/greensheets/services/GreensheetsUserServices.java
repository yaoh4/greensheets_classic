package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

public interface GreensheetsUserServices {
	public GsUser getUserByUserName(String userName);

	public GsUser getUserRolesActivitiesNPEIDs(String nciOracleID);
}