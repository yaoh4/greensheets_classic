/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * 
 * Enumeration of possible Greensheet form Status values
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetStatus extends ValuedEnum {

	public static final int NOT_STARTED_VALUE = 1;

	public static final int FROZEN_VALUE = 2;

	public static final int SAVED_VALUE = 3;

	public static final int SUBMITTED_VALUE = 4;

	public static final int UNSUBMITTED_VALUE = 5;

	public static final GreensheetStatus NOT_STARTED = new GreensheetStatus(
			"NOT_STARTED", NOT_STARTED_VALUE);

	public static final GreensheetStatus FROZEN = new GreensheetStatus(
			"FROZEN", FROZEN_VALUE);

	public static final GreensheetStatus SAVED = new GreensheetStatus("SAVED",
			SAVED_VALUE);

	public static final GreensheetStatus SUBMITTED = new GreensheetStatus(
			"SUBMITTED", SUBMITTED_VALUE);

	public static final GreensheetStatus UNSUBMITTED = new GreensheetStatus(
			"UNSUBMITTED", UNSUBMITTED_VALUE);

	/**
	 * Constructor for GreensheetStatus.
	 * 
	 * @param name
	 * @param value
	 */
	private GreensheetStatus(String name, int value) {
		super(name, value);
	}

}
