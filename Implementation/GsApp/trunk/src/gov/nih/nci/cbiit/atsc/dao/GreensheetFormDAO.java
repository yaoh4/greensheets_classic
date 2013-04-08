package gov.nih.nci.cbiit.atsc.dao;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

public interface GreensheetFormDAO {
	public GreensheetForm getGreensheetForm(FormGrant grant, String formtype) throws GreensheetBaseException;
}
