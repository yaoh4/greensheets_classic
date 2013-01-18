package gov.nih.nci.cbiit.atsc.dao;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

public interface UserRoleDataDAO {
	public GsUser getUserRolesActivitiesNPEIDs(String nciOracleID);
}
