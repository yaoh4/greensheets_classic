/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.grantmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;
import java.util.*;
/**
 *
 *  Test Implementation of GrantMgr interface
 * 
 *  @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr
 * 
 *  @author kpuscas, Number Six Software
 */
public class GrantMgrTestImpl implements GrantMgr {


    /**
     * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#findGrantByApplId(String)
     */
    public GsGrant findGrantByApplId(String applId) throws GreensheetBaseException {
        return null;
    }

    /**
     * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#getGrantsListForUser(GsUser, boolean)
     */
    public Map getGrantsListForUser(GsUser user, boolean paylineOnly,boolean myPortfolio) throws GreensheetBaseException {
        return null;
    }

    /**
     * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#searchForGrant(String, String, GsUser)
     */
    public Map searchForGrant(String searchType, String searchText, GsUser user) throws GreensheetBaseException {
        return null;
    }

    /**
     * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#findGrantById(String)
     */
    public GsGrant findGrantById(String applId, String grantNumber) throws GreensheetBaseException {
        return null;
    }

}

