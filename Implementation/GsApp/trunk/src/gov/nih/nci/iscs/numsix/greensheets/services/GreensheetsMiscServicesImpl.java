package gov.nih.nci.iscs.numsix.greensheets.services;

import java.sql.Timestamp;

import gov.nih.nci.cbiit.atsc.dao.MiscDataDAO;

public class GreensheetsMiscServicesImpl implements GreensheetsMiscServices {
	private MiscDataDAO miscDataDAO;

	public MiscDataDAO getMiscDataDAO() {
		return this.miscDataDAO;
	}

	public void setMiscDataDAO(MiscDataDAO miscDataDAO) {
		this.miscDataDAO = miscDataDAO;
	}
	
	public int getFiscalYear() {
		return this.miscDataDAO.getFiscalYear();
	}	
	
	public Timestamp getCurrentTimsestamp() {
		return this.miscDataDAO.getCurrentTimsestamp();
	}	
}
