/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgrTestImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormMgrTestImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr.GreensheetResourceManagerImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr.GreensheetResourceManagerTestImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgrImpl;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgrTestImpl;

/**
 * Factory class used to generate application services managers. These managers
 * are used to provides specific services to calling clients. The factory
 * returns and interface for each Manager. A type is required to determine which
 * interface implementation to return.
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetMgrFactory {

	public static final int TEST = 1;

	public static final int PROD = 2;

	private GreensheetMgrFactory() {
	}

	public static GrantMgr createGrantMgr(int type) {

		if (type == TEST) {
			return new GrantMgrTestImpl();
		} else {
			return new GrantMgrImpl();
		}

	}

	public static GreensheetFormMgr createGreensheetFormMgr(int type) {

		if (type == TEST) {
			return new GreensheetFormMgrTestImpl();
		} else {
			return new GreensheetFormMgrImpl();
		}

	}

	public static GreensheetUserMgr createGreensheetUserMgr(int type) {

		if (type == TEST) {
			return new GreensheetUserMgrTestImpl();
		} else {
			return new GreensheetUserMgrImpl();
		}

	}

	public static GreensheetResourceManager createGreenSheetResourceMgr(int type) {

		if (type == TEST) {
			return new GreensheetResourceManagerTestImpl();
		} else {
			return new GreensheetResourceManagerImpl();
		}

	}

}
