package gov.nih.nci.iscs.numsix.greensheets.fwrk;

/**
 * An exception class to notify the user about "optimistic locking failure" when he/she 
 * tries to save a Greensheet form - i.e., that after they retrieved the greensheet to edit
 * it, and before they tried to save it, someone else saved it and so they might overwrite 
 * that other person's changes. It is a GreensheetsBaseException extended to hold the 
 * username and the date (expressed as a String) of that more recent udpdate (username of the
 * user who performed it).   
 * @author Anatoli Kouznetsov
 */
public class GsStaleDataException extends GreensheetBaseException {

	private static final long serialVersionUID = 4086066277230374876L;

	private String message;
	private String username;
	private String lastUpdateDateAsString;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/** The username of the user who made a more recent update of the data */
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	/** The timestamp, as a String, of when the more recent update was made */
	public String getLastUpdateDateAsString() {
		return lastUpdateDateAsString;
	}
	public void setLastUpdateDateAsString(String lastUpdateDateAsString) {
		this.lastUpdateDateAsString = lastUpdateDateAsString;
	}
	
}
