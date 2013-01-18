/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr;

import gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager;
import gov.nih.nci.iscs.i2e.greensheets.GreensheetsResourceException;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;

import java.util.Map;

/**
 * Test Implementation of GreensheetResourceManager interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetResourceManagerTestImpl implements
		GreensheetResourceManager {

	/**
	 * @see gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager#getResource(String,
	 *      int)
	 */
	public String getResource(String id, int type)
			throws GreensheetsResourceException {

		Map map = (Map) AppConfigProperties.getInstance().getProperty(
				"TEMPLATE_RESOURCE_MAP");

		String template = ((GreensheetTemplateWrapper) map.get(id))
				.getTemplate();

		return template;
	}

}
