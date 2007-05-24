/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * Enumeration of the different user roles that a user of the application my
 * have.
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GsUserRole extends ValuedEnum {

	public static final int PGM_DIR_VALUE = 1;

	public static final int PGM_ANL_VALUE = 2;

	public static final int RMC_QA_VALUE = 3;

	public static final int RMC_VALUE = 4;

	public static final int SPEC_VALUE = 5;

	public static final int GS_GUEST_VALUE = 6;

	public static final int NO_SYSTEM_ROLE_VALUE = 5;

	public static final GsUserRole PGM_DIR = new GsUserRole("PGM_DIR",
			PGM_DIR_VALUE);

	public static final GsUserRole PGM_ANL = new GsUserRole("PGM_ANL",
			PGM_ANL_VALUE);

	public static final GsUserRole RMC_QA = new GsUserRole("RMC_QA",
			RMC_QA_VALUE);

	public static final GsUserRole RMC = new GsUserRole("RMC", RMC_VALUE);

	public static final GsUserRole SPEC = new GsUserRole("SPEC", SPEC_VALUE);

	public static final GsUserRole GS_GUEST = new GsUserRole("GS_GUEST",
			GS_GUEST_VALUE);

	public static final GsUserRole NO_SYSTEM_ROLE = new GsUserRole(
			"NO_SYSTEM_ROLE", NO_SYSTEM_ROLE_VALUE);

	/**
	 * Constructor for GsUserRole.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	private GsUserRole(String name, int value) {
		super(name, value);
	}

}
