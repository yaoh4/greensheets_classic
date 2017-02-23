package gov.nih.nci.cbiit.atsc.dao;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

public interface GreensheetFormDAO {
    public GreensheetForm getGreensheetForm(FormGrant grant, String formtype) throws GreensheetBaseException;

    public boolean checkActionStatusByApplId(String applId) throws GreensheetBaseException;

    public boolean checkActionStatusByGrantId(String grantId) throws GreensheetBaseException;

    /**
     * GREENSHEET-495
     * 
     * Is a given APPL_ID a Type 6 Grant with Award Type 1, 2, 4, 5,8 9 in same FY?
     */
	public boolean isValidGrantType(String applId, StringBuffer gn) throws GreensheetBaseException;

}
