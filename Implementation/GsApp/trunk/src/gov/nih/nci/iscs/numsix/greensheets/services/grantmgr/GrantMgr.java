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
 * Provides services for finding GsGrant information
 * 
 *  @author kpuscas, Number Six Software
 */

public interface GrantMgr {


   public GsGrant findGrantById(String applId, String grantNumber) throws GreensheetBaseException;



    /**
     * Method getGrantsListForUser.
     * @param user
     * @return Map
     * @throws GreensheetBaseException
     */
    public Map getGrantsListForUser(GsUser user, boolean paylineOnly, boolean myPortfolio) throws GreensheetBaseException;

    
    /**
     * Method searchForGrant.
     * @param searchType
     * @param searchText
     * @return Map
     * @throws GreensheetBaseException
     */
    public Map searchForGrant(String searchType, String searchText, GsUser user) throws GreensheetBaseException;
    
    
}

