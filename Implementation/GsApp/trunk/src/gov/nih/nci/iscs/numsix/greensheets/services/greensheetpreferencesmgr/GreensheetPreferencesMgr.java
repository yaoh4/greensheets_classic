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
     * Method saveForm.
     * @param userPreferences
     * @throws GreensheetBaseException
     */
    public void saveUserPreferences(HashMap userPreferences) throws GreensheetBaseException;
    

	public HashMap getUserPrefs();

	public String getGrantSource() ;
	
	public String getGrantType() ;
	
	public String getGrantMechanism();
	
	public String getGrantPayline() ;
	
	
	public String isGrantSourceOptionSelected(String optionValue, boolean isDropdown) ;

	public String isGrantTypeOptionSelected(String optionValue, boolean isDropdown) ;

	public String isGrantPaylineOptionSelected(String optionValue, boolean isDropdown) ;

	public boolean isGrantSourcePortfolioSelected() ;

	public boolean isGrantSourceMyActivitiesSelected() ;

	public boolean isGrantSourceAllSelected() ;
	
	
	public boolean isGrantTypeBothSelected() ;
	
	public boolean isGrantTypeCompetingSelected() ; 
	
	public boolean isGrantTypeNonCompetingSelected() ;
	
	public boolean isGrantYesSelected() ;
	
	public boolean isGrantNoSelected() ;
	
	
	public String getGrantMechanismDefault() ;

	public String getGrantMechanismName() ;

	public String getGrantPaylineDefault() ;

	public String getGrantPaylineName() ;

	public String getGrantPaylineNo() ;

	public String getGrantPaylineYes() ;

	public String getGrantSourceAll() ;

	public String getGrantSourceDefault() ;

	public String getGrantSourceMyActivities();

	public String getGrantSourceName() ;

	public String getGrantSourcePortfolio() ;

	public String getGrantTypeBoth() ;

	public String getGrantTypeCompeting() ;

	public String getGrantTypeDefault() ;

	public String getGrantTypeName();

	public String getGrantTypeNonCompeting() ;
 }

