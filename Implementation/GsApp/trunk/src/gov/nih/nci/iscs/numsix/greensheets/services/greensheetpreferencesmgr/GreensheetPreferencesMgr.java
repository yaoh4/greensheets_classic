/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */


package gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.util.*;

/**
 * Provides services for Greensheet Preferences
 * 
 * 
 *  @author kkanchinadam, Number Six Software
 */
public interface GreensheetPreferencesMgr {     

	/** 
	 * restoreUserPreferences restores preferences for a user (if any)
     * @return applPrefsMap
	 */
    public Map restoreUserPreferences() throws GreensheetBaseException;
    
	/** 
	 * restoreApplicationPreferences restores application default preferences
     * @return applPrefsMap
     */
    public Map restoreApplicationPreferences() throws GreensheetBaseException;    

	/** 
	 * savePreferences saves preferences for a user
     * @arg  GsUser
     */
	public void savePreferences(GsUser user, HashMap prefsMap) throws GreensheetBaseException;
 }

