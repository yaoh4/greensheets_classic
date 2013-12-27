package gov.nih.nci.cbiit.scimgmt.greensheets.qstnfrmanalyzer;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *  Represents one form - characterized by a different combination of questions than 
 *  any other form - and the list of Application Type/Funding Mechanism combinations 
 *  for which this form is applicable
 */
public class GSForm {
	private String formStaffRole = "";  // PGM, SPEC, DM
	private String generatedName = "";
	private String initialFilename = "";
	private SortedSet<String> applicability = new TreeSet<String>();
	private long occurrences = 0l;  // how many forms with this content were filled out - combined
		// across all type/mech combos that share this form's question content
	
	
	/** converts form groups abbreviations into staff role abbreviations - for example, 
	 *  for both PC and PNC this method will return 'PGM' */
	public static String determineStaffRole(String formGroup) {
		String staffRole = "";
		if (formGroup.charAt(0) == 'P') {
			staffRole = "PGM";
		}
		else if (formGroup.charAt(0) == 'S') {
			staffRole = "SPEC";
		}
		else if (formGroup.charAt(0) == 'D') {
			staffRole = "DM";
		}
		return staffRole;
	}
	
	
	public String getApplicabilityAsString() {
		StringBuilder s = new StringBuilder();
		boolean pastOne = false;
		for (String a : this.applicability) {
			if (pastOne) { s.append(", "); }
			s.append(a);
			pastOne = true;
		}
		return s.toString();
	}
	
	
	public String getFormStaffRole() {
		return formStaffRole;
	}
	public void setFormStaffRole(String formStaffRole) {
		this.formStaffRole = formStaffRole;
	}
	public String getGeneratedName() {
		return generatedName;
	}
	public void setGeneratedName(String aName) {
		this.generatedName = aName;
	}
	public String getInitialFilename() {
		return initialFilename;
	}
	public void setInitialFilename(String initialFilename) {
		this.initialFilename = initialFilename;
	}
	public SortedSet<String> getApplicability() {
		return applicability;
	}
	public void setApplicability(SortedSet<String> typeMechSet) {
		this.applicability = typeMechSet;
	}
	public long getOccurrences() {
		return occurrences;
	}
	public void setOccurrences(long occurrences) {
		this.occurrences = occurrences;
	}
}
