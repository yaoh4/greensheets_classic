package gov.nih.nci.cbiit.atsc.dao;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.NciPerson;

public interface NCIPersonDAO {
	public NciPerson getUserByUserName(String userName);
}