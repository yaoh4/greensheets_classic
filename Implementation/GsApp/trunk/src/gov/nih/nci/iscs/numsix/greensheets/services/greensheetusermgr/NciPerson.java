/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This is a base object representing an NCI user. Information for this user is
 * based on related information in the NCI LDAP
 * 
 * @author kpuscas, Number Six Software
 * 
 */
public class NciPerson implements Serializable {

	private ArrayList groupMembership;

	private String adminUnit;

	private String commonName;

	private String distinguishedName;

	private String email;

	private String externalOrganization;

	private String firstName;

	private String fullName;

	private String functionalRole;

	private String lastName;

	private String manager;

	private String memeber;

	private String middleInitial;

	private String oracleId;

	private String organization;

	private String tfsId;

	private String title;
	
	private boolean isSuperUser;
	
	
	public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean isSuperUser) {
        this.isSuperUser = isSuperUser;
    }

    // unique ldap ID of a person, in the form dn=VasudeSa,ou=6116,ou=nci,o=nih
	private String uid;

	/**
	 * Sets the adminUnit.
	 * 
	 * @param adminUnit
	 *            The adminUnit to set
	 */
	public void setAdminUnit(String adminUnit) {
		this.adminUnit = adminUnit;
	}

	/**
	 * Returns the adminUnit.
	 * 
	 * @return String
	 */
	public String getAdminUnit() {

		return adminUnit;
	}

	/**
	 * Sets the commonName.
	 * 
	 * @param commonName
	 *            The commonName to set
	 */
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	/**
	 * Returns the commonName.
	 * 
	 * @return String
	 */
	public String getCommonName() {

		return commonName;
	}

	/**
	 * Sets the distinguishedName.
	 * 
	 * @param distinguishedName
	 *            The distinguishedName to set
	 */
	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	/**
	 * Returns the distinguishedName.
	 * 
	 * @return String
	 */
	public String getDistinguishedName() {

		return distinguishedName;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the email.
	 * 
	 * @return String
	 */
	public String getEmail() {

		return email;
	}

	/**
	 * Sets the externalOrganization.
	 * 
	 * @param externalOrganization
	 *            The externalOrganization to set
	 */
	public void setExternalOrganization(String externalOrganization) {
		this.externalOrganization = externalOrganization;
	}

	/**
	 * Returns the externalOrganization.
	 * 
	 * @return String
	 */
	public String getExternalOrganization() {

		return externalOrganization;
	}

	/**
	 * Sets the firstName.
	 * 
	 * @param firstName
	 *            The firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the firstName.
	 * 
	 * @return String
	 */
	public String getFirstName() {

		return firstName;
	}

	/**
	 * Sets the fullName.
	 * 
	 * @param fullName
	 *            The fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Returns the fullName.
	 * 
	 * @return String
	 */
	public String getFullName() {

		return fullName;
	}

	/**
	 * Sets the functionalRole.
	 * 
	 * @param functionalRole
	 *            The functionalRole to set
	 */
	public void setFunctionalRole(String functionalRole) {
		this.functionalRole = functionalRole;
	}

	/**
	 * Returns the functionalRole.
	 * 
	 * @return String
	 */
	public String getFunctionalRole() {

		return functionalRole;
	}

	/**
	 * Sets the groupMembership.
	 * 
	 * @param groupMembership
	 *            The groupMembership to set
	 */
	public void setGroupMembership(ArrayList groupMembership) {
		this.groupMembership = groupMembership;
	}

	/**
	 * Returns the groupMembership.
	 * 
	 * @return ArrayList
	 */
	public ArrayList getGroupMembership() {

		return groupMembership;
	}

	/**
	 * Sets the lastName.
	 * 
	 * @param lastName
	 *            The lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the lastName.
	 * 
	 * @return String
	 */
	public String getLastName() {

		return lastName;
	}

	/**
	 * Sets the manager.
	 * 
	 * @param manager
	 *            The manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}

	/**
	 * Returns the manager.
	 * 
	 * @return String
	 */
	public String getManager() {

		return manager;
	}

	/**
	 * Sets the memeber.
	 * 
	 * @param memeber
	 *            The memeber to set
	 */
	public void setMemeber(String memeber) {
		this.memeber = memeber;
	}

	/**
	 * Returns the memeber.
	 * 
	 * @return String
	 */
	public String getMemeber() {

		return memeber;
	}

	/**
	 * Sets the middleInitial.
	 * 
	 * @param middleInitial
	 *            The middleInitial to set
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	/**
	 * Returns the middleInitial.
	 * 
	 * @return String
	 */
	public String getMiddleInitial() {

		return middleInitial;
	}

	/**
	 * Sets the oracleId.
	 * 
	 * @param oracleId
	 *            The oracleId to set
	 */
	public void setOracleId(String oracleId) {
		this.oracleId = oracleId;
	}

	/**
	 * Returns the oracleId.
	 * 
	 * @return String
	 */
	public String getOracleId() {

		return oracleId;
	}

	/**
	 * Sets the organization.
	 * 
	 * @param organization
	 *            The organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * Returns the organization.
	 * 
	 * @return String
	 */
	public String getOrganization() {

		return organization;
	}

	/**
	 * Sets the tfsId.
	 * 
	 * @param tfsId
	 *            The tfsId to set
	 */
	public void setTfsId(String tfsId) {
		this.tfsId = tfsId;
	}

	/**
	 * Returns the tfsId.
	 * 
	 * @return String
	 */
	public String getTfsId() {

		return tfsId;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the title.
	 * 
	 * @return String
	 */
	public String getTitle() {

		return title;
	}

	/**
	 * Sets the uid.
	 * 
	 * @param uid
	 *            The uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Returns the uid.
	 * 
	 * @return String
	 */
	public String getUid() {

		return uid;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String toString() {

		return ToStringBuilder.reflectionToString(this);

	}

}
