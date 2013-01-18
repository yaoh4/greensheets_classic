package gov.nih.nci.cbiit.atsc.dao;

import java.sql.Timestamp;

public interface MiscDataDAO {
	public int getFiscalYear();
	public Timestamp getCurrentTimsestamp();
}
