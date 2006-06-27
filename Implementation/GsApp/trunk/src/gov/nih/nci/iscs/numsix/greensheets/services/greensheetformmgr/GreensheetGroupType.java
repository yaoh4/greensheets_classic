/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * Enumeration of Greensheet Group Types. These represent the Program (PGM)
 * Specialist(SPEC), and RMC (RMC) groups that are the ones who workup the forms
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetGroupType extends ValuedEnum {

	public static final int PGM_VALUE = 1;

	public static final int SPEC_VALUE = 2;

	public static final int RMC_VALUE = 3;

	public static final GreensheetGroupType PGM = new GreensheetGroupType(
			"PGM", PGM_VALUE);

	public static final GreensheetGroupType SPEC = new GreensheetGroupType(
			"SPEC", SPEC_VALUE);

	public static final GreensheetGroupType RMC = new GreensheetGroupType(
			"RMC", RMC_VALUE);

	/**
	 * Constructor for GreensheetGroupType.
	 * 
	 * @param name
	 * @param value
	 */
	private GreensheetGroupType(String name, int value) {
		super(name, value);
	}

	public static GreensheetGroupType getGreensheetGroupType(String val) {
		if (val.equalsIgnoreCase(PGM.getName())) {
			return PGM;
		} else if (val.equalsIgnoreCase(SPEC.getName())) {
			return SPEC;
		} else if (val.equalsIgnoreCase(RMC.getName())) {
			return RMC;
		} else {
			return null;
		}
	}

}
