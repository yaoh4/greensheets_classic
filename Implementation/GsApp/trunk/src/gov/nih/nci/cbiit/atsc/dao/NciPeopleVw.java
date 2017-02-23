package gov.nih.nci.cbiit.atsc.dao;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents Nci User Information
 * @author tembharend
 *
 */
public class NciPeopleVw implements Serializable {
    
    private java.lang.Long npnId;  
    private java.lang.String nihNetworkId;
    private java.lang.String oracleId;
    private java.lang.String lastName;    
    private java.lang.String firstName;
    private java.lang.String emailAddress;
    private java.util.Date inactiveDate;
    private java.lang.String activeFlag;   

   /**
    * Default Constructor
    */
    public NciPeopleVw(){    	
    }   

	/**
 * @param npnId
 * @param nihNetworkId
 * @param oracleId
 * @param lastName
 * @param firstName
 * @param emailAddress
 * @param inactiveDate
 * @param activeFlag
 */
public NciPeopleVw(Long npnId, String nihNetworkId, String oracleId, String lastName, String firstName,
		String emailAddress, Date inactiveDate, String activeFlag) {
	super();
	this.npnId = npnId;
	this.nihNetworkId = nihNetworkId;
	this.oracleId = oracleId;
	this.lastName = lastName;
	this.firstName = firstName;
	this.emailAddress = emailAddress;
	this.inactiveDate = inactiveDate;
	this.activeFlag = activeFlag;
}



	/**
	 * @return the npnId
	 */
	public java.lang.Long getNpnId() {
		return npnId;
	}



	/**
	 * @param npnId the npnId to set
	 */
	public void setNpnId(java.lang.Long npnId) {
		this.npnId = npnId;
	}



	/**
	 * @return the nihNetworkId
	 */
	public java.lang.String getNihNetworkId() {
		return nihNetworkId;
	}



	/**
	 * @param nihNetworkId the nihNetworkId to set
	 */
	public void setNihNetworkId(java.lang.String nihNetworkId) {
		this.nihNetworkId = nihNetworkId;
	}



	/**
	 * @return the oracleId
	 */
	public java.lang.String getOracleId() {
		return oracleId;
	}



	/**
	 * @param oracleId the oracleId to set
	 */
	public void setOracleId(java.lang.String oracleId) {
		this.oracleId = oracleId;
	}



	/**
	 * @return the lastName
	 */
	public java.lang.String getLastName() {
		return lastName;
	}



	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}



	/**
	 * @return the firstName
	 */
	public java.lang.String getFirstName() {
		return firstName;
	}



	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}



	/**
	 * @return the emailAddress
	 */
	public java.lang.String getEmailAddress() {
		return emailAddress;
	}



	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(java.lang.String emailAddress) {
		this.emailAddress = emailAddress;
	}



	/**
	 * @return the inactiveDate
	 */
	public java.util.Date getInactiveDate() {
		return inactiveDate;
	}



	/**
	 * @param inactiveDate the inactiveDate to set
	 */
	public void setInactiveDate(java.util.Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}



	/**
	 * @return the activeFlag
	 */
	public java.lang.String getActiveFlag() {
		return activeFlag;
	}



	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(java.lang.String activeFlag) {
		this.activeFlag = activeFlag;
	}

}
