package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseActionForm;

public class ProgramPreferencesForm extends GsBaseActionForm {

    public String grantSource;

    public String grantType;

    public String mechanism;

    public String onlyGrantsWithinPayline;

    public String grantNumber;

    //public String piName;	// Bug#4204 Abdul: Commented out in place of the two new fields
    public String lastName; // Bug#4204 Abdul: Introduced the new field lastName

    public String firstName; // Bug#4204 Abdul: Introduced the new field firstName

    public ProgramPreferencesForm() {
    }

    public String getGrantNumber() {
        return grantNumber;
    }

    public void setGrantNumber(String grantNumber) {
        this.grantNumber = grantNumber;
    }

    public String getGrantSource() {
        return grantSource;
    }

    public void setGrantSource(String grantsFrom) {
        this.grantSource = grantsFrom;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getMechanism() {
        return mechanism;
    }

    public void setMechanism(String mechanism) {
        this.mechanism = mechanism;
    }

    public String getOnlyGrantsWithinPayline() {
        return onlyGrantsWithinPayline;
    }

    public void setOnlyGrantsWithinPayline(String onlyGrantsWithinPayline) {
        this.onlyGrantsWithinPayline = onlyGrantsWithinPayline;
    }

    public boolean isPaylineOnly() {

        if (this.onlyGrantsWithinPayline == null)
            return false;

        return ((this.onlyGrantsWithinPayline.trim()).equals("yes"));

    }

    //	 Bug#4204 Abdul: Commented out for the sake of two new fields specified below
    //	public String getPiName() {
    //		return piName;
    //	}
    //
    //	public void setPiName(String piName) {
    //		this.piName = piName;
    //	}

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
