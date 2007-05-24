package gov.nih.nci.iscs.i2e.greensheets;


public interface GreensheetResourceManager {
	public static final int VMTEMPLATE_RESOURCE = 1;

	public String getResource(String id, int type)
			throws GreensheetsResourceException;

}
