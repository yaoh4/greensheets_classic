package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetGroupType;

public interface GreensheetFormService {
	public GreensheetFormProxy getGreensheetForm(FormGrantProxy formGrantProxy, String formRoleCode);
}
