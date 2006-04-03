/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */


package gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;

import java.util.*;

/**
 * Provides services for Greensheet Preferences
 * 
 * 
 *  @author kkanchinadam, Number Six Software
 */
public interface GreensheetPreferencesMgr {
      /**
     * Method getUserPreferences.
     * @param grant
     * @param type
     * @return HashMap
     * @throws GreensheetBaseException
     */
    public HashMap getUserPreferences() throws GreensheetBaseException;
       

    /**
     * Method saveForm.
     * @param userPreferences
     * @throws GreensheetBaseException
     */
    public void saveUserPreferences(HashMap userPreferences) throws GreensheetBaseException;
 }

