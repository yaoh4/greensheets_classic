package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GsBaseActionForm;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.ImageButtonBean;

public class ProgramPreferencesForm extends GsBaseActionForm {

	public String grantSource;

	public String grantType;

	public String mechanism;

	public String onlyGrantsWithinPayline;

	public String grantNumber;

	public String piName;

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

	public String getPiName() {
		return piName;
	}

	public void setPiName(String piName) {
		this.piName = piName;
	}
	
}
