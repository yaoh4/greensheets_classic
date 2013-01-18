package gov.nih.nci.iscs.numsix.greensheets.services;

import java.sql.Timestamp;

public interface GreensheetsMiscServices {
	public int getFiscalYear();
	public Timestamp getCurrentTimsestamp();
}