package gov.nih.nci.iscs.numsix.greensheets.services;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;

public interface GreensheetFormService {
    public GreensheetFormProxy getGreensheetForm(FormGrantProxy formGrantProxy, String formRoleCode)
            throws GreensheetBaseException;

}
