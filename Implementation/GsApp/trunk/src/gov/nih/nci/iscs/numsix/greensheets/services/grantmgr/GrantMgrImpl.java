/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.grantmgr;

import gov.nih.nci.iscs.i2e.grantretriever.*;
import gov.nih.nci.iscs.i2e.oracle.grantretrieverimpl.*;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import org.apache.log4j.*;

import org.apache.commons.lang.*;

/**
 *  Production Implementation of GrantMgr interface
 * 
 *  @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr
 * 
 *  @author kpuscas, Number Six Software
 */

public class GrantMgrImpl implements GrantMgr {

	private static final Logger logger = Logger.getLogger(GrantMgrImpl.class);


	public GsGrant findGrantById(String applId, String grantNumber)
		throws GreensheetBaseException {
		List l = null;
		GsGrant g = null;
		try {
			ViewGrantRetrieverImpl vgr = new ViewGrantRetrieverImpl();
			vgr.setView("FORM_GRANT_VW");
			ValueToken vtApplId = new ValueToken();
			vtApplId.setColumnKey("applId");
			vtApplId.setValue(applId);
			vgr.addCondition(vtApplId);
			l = vgr.getGrantList();

			if ((grantNumber != null) && (l.size() > 1 || l.size() < 1)) {

				logger.debug("Using Grant Number to find Grant " + grantNumber);
				vgr.clearConditions();
				vgr.setView("FORM_GRANT_VW");
				ValueToken vtGrantNum = new ValueToken();
				vtGrantNum.setColumnKey("fullGrantNum");
				vtGrantNum.setValue(grantNumber);
				vgr.addCondition(vtGrantNum);
				l = vgr.getGrantList();
			}
			g = new GsGrant();
			g.setFormGrant((FormGrant) l.get(0));
		} catch (GrantRetrieverException e) {
			throw new GreensheetBaseException("Error finding Grant", e);
		}

		return g;
	} 
	
	RangeToken getLatestBudgetStartDateCriteria() {
	    int fiscalStartingYear = this.getFiscalStartingYear();
	    int fiscalEndingYear = this.getFiscalEndingYear();
	    
	    String startingDateString = "October 1, " + fiscalStartingYear;
	    String endingDateString = "September 30, " + fiscalEndingYear;
	    
	    DateFormat df = DateFormat.getDateInstance();
	    Date startingDate = null;
	    Date endingDate = null;
	    
	    try {
	        startingDate = df.parse(startingDateString);
         }
         catch(ParseException e) {
            System.out.println("Unable to parse starting date- " + startingDateString);
         }
	    
         try {
             endingDate = df.parse(endingDateString);
          }
          catch(ParseException e) {
             System.out.println("Unable to parse ending date- " + endingDateString);
          }
	    
        RangeToken rtps = new RangeToken();
        rtps.setColumnKey("latestBudgetStartDate");
        rtps.setMinValue(startingDate);
        rtps.setMaxValue(endingDate);
        rtps.setInclusive(true);
        
        return rtps;
	}
	
	/**
	           * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#getGrantsListForUser(GsUser, boolean)
	           */
	public Map getGrantsListForUser(
		GsUser user,
		boolean paylineOnly,
		boolean myPortfolio)
		throws GreensheetBaseException {

		Map map = new HashMap();
		List l;
		try {
			ViewGrantRetrieverImpl vgr = new ViewGrantRetrieverImpl();
			vgr.setView("FORM_GRANT_VW");

			Properties p = 
				(Properties) AppConfigProperties.getInstance().getProperty(
					GreensheetsKeys.KEY_CONFIG_PROPERTIES);
            
            // Add the condition for Latest Budget Start Date.
			vgr.addCondition(this.getLatestBudgetStartDateCriteria());
            
            // Query Rules for Program users
			if (user.getRole().equals(GsUserRole.PGM_DIR)
				|| user.getRole().equals(GsUserRole.PGM_ANL)) {

				logger.debug(
					"Start Program Grant List Query " + new Date().toString());
				setBaseProgramGrantsListTokens(user, vgr, p);
                
                // Check if the payline and or My portfolio options were selected
				if (paylineOnly) {
					ValueToken vtps = new ValueToken();
					vtps.setColumnKey("withinPaylineFlag");
					vtps.setValue("Y");
					vgr.addCondition(vtps);
				}

				if (myPortfolio && (user.getMyPortfolioIds() != null)) {
					ListToken lt = new ListToken();
					lt.setColumnKey("pdNpeId");
					lt.setValue(user.getMyPortfolioIds());
					vgr.addCondition(lt);
				}

				l = vgr.getGrantList();
                
                
				// Now need to get the non-competing grants
				vgr.clearConditions();
				this.setBaseProgramGrantsListTokens(user, vgr, p);
				
				//Add the condition for Latest Budget Start Date.
				vgr.addCondition(this.getLatestBudgetStartDateCriteria());
				
				LikeToken lt4 = new LikeToken();
				lt4.setColumnKey("councilMeetingDate");
				lt4.setValue("%00");
				vgr.addCondition(lt4);
                // Get the non-competes is My Portfolio restriction enabled
				if (myPortfolio && (user.getMyPortfolioIds() != null)) {
					ListToken lt = new ListToken();
					lt.setColumnKey("pdNpeId");
					lt.setValue(user.getMyPortfolioIds());
					vgr.addCondition(lt);
				}

				List nonCompetingList = vgr.getGrantList();
				l.addAll(nonCompetingList);
				logger.debug(
					"End Program Grant List Query " + new Date().toString());
			} else {

				ValueToken vt1 = new ValueToken();
				vt1.setColumnKey("onControlFlag");
				vt1.setValue("Y");
				vgr.addCondition(vt1);
				ValueToken vt = new ValueToken();
				vt.setColumnKey("specFormStatus");
				vt.setValue("SUBMITTED");
				vt.setComparison(ValueToken.EQUALS);
				vt.setNegative(true);
				vgr.addCondition(vt);
				LikeToken lt = new LikeToken();
				lt.setColumnKey("allGmsUserids");
				lt.setValue("%" + user.getOracleId() + "%");
				vgr.addCondition(lt);
				OrderByToken obt = new OrderByToken();
				obt.setFieldName("budgetStartDate");
				obt.setSortOrder("ASC");
				l = vgr.getGrantList();
			}

		} catch (GrantRetrieverException e) {
			e.printStackTrace();
			throw new GreensheetBaseException("error.usergrantlist", e);
		}

		int size = l.size();
		logger.debug("NUMBER Of Grants Found " + size);
		for (int i = 0; i < size; i++) {
			FormGrant g = (FormGrant) l.get(i);
			GsGrant gg = new GsGrant();
			gg.setFormGrant(g);
			map.put(gg.getFullGrantNumber(), gg);
		}

		return map;
	}

	private void setBaseProgramGrantsListTokens(
		GsUser user,
		ViewGrantRetrieverImpl vgr,
		Properties p)
		throws GrantRetrieverException {

		ListToken lt = new ListToken();
		lt.setColumnKey("cayCode");
		lt.setValue(user.getCancerActivities());
		vgr.addCondition(lt);
		ValueToken vt = new ValueToken();
		vt.setColumnKey("pgmFormStatus");
		vt.setValue("SUBMITTED");
		vt.setComparison(ValueToken.EQUALS);
		vt.setNegative(true);
		vgr.addCondition(vt);
		ValueToken vt2 = new ValueToken();
		vt2.setColumnKey("pgmFormStatus");
		vt2.setValue("FROZEN");
		vt2.setComparison(ValueToken.EQUALS);
		vt2.setNegative(true);
		vgr.addCondition(vt2);
		int lookahead =
			Integer.parseInt(p.getProperty("budgetstartdate.lookahead.months"));
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.add(Calendar.MONTH, lookahead);
		ValueToken vt3 = new ValueToken();
		vt3.setColumnKey("budgetStartDate");
		vt3.setValue(now.getTime());
		vt3.setComparison(ValueToken.LESS_THAN_EQUAL_TO);
		vgr.addCondition(vt3);
        ValueToken vt4 = new ValueToken();
        vt4.setColumnKey("applStatusGroupCode");
        vt4.setValue("A");
        vt4.setComparison(ValueToken.EQUALS);
        vt4.setNegative(true);
        vgr.addCondition(vt4);
		OrderByToken obt = new OrderByToken();
		obt.setFieldName("budgetStartDate");
		obt.setSortOrder("ASC");
	}
	/**
	        * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#searchForGrant(String, String)
	        */
	public Map searchForGrant(
		String searchType,
		String searchText,
		GsUser user)
		throws GreensheetBaseException {

		Map map = new HashMap();
		List l;
		try {
			ViewGrantRetrieverImpl vgr = new ViewGrantRetrieverImpl();
			vgr.setView("FORM_GRANT_VW");
			if (searchText.trim().equalsIgnoreCase("")) {
				logger.debug("Search text is blank");
			} else {

				if (user.getRole().equals(GsUserRole.PGM_DIR)
					|| user.getRole().equals(GsUserRole.PGM_ANL)) {
					if (searchType
						.trim()
						.equalsIgnoreCase(GreensheetsKeys.SEARCH_GS_NUMBER)) {

						LikeToken lt = new LikeToken();
						logger.debug("searched on a grant number");
						lt.setColumnKey("fullGrantNum");
						lt.setValue(
							"%" + searchText.trim().toUpperCase() + "%");
						vgr.addCondition(lt);
					} else if (
						searchType.trim().equalsIgnoreCase(
							GreensheetsKeys.SEARCH_GS_NAME)) {

						LikeToken lt = new LikeToken();
						logger.debug("searched on a last name");
						lt.setColumnKey("lastName");
						lt.setValue(searchText.trim().toUpperCase() + "%");
						vgr.addCondition(lt);
						ValueToken vtca = new ValueToken();
						vtca.setColumnKey("adminPhsOrgCode");
						vtca.setValue("CA");
						vtca.setComparison(ValueToken.EQUALS);
						vgr.addCondition(vtca);
					}
					logger.debug("START" + new Date().toString());
					l = vgr.getGrantList();
					logger.debug("END" + new Date().toString());
					int size = l.size();
					logger.debug("NUMBER Of Grants Found " + size);
					for (int i = 0; i < size; i++) {
						FormGrant g = (FormGrant) l.get(i);
						GsGrant gg = new GsGrant();
						gg.setFormGrant(g);
						map.put(gg.getFullGrantNumber(), gg);
					}

				} else {

					if (searchType
						.trim()
						.equalsIgnoreCase(GreensheetsKeys.SEARCH_GS_NUMBER)) {

						LikeToken lt = new LikeToken();
						logger.debug("searched on a grant number");
						lt.setColumnKey("fullGrantNum");
						lt.setValue(
							"%" + searchText.trim().toUpperCase() + "%");
						vgr.addCondition(lt);
					} else if (
						searchType.trim().equalsIgnoreCase(
							GreensheetsKeys.SEARCH_GS_NAME)) {

						LikeToken lt = new LikeToken();
						logger.debug("searched on a last name");
						lt.setColumnKey("lastName");
						lt.setValue(searchText.trim().toUpperCase() + "%");
						vgr.addCondition(lt);
					}
					logger.debug("START" + new Date().toString());
					l = vgr.getGrantList();
					logger.debug("END" + new Date().toString());
					int size = l.size();
					logger.debug("NUMBER Of Grants Found " + size);
					for (int i = 0; i < size; i++) {
						FormGrant g = (FormGrant) l.get(i);
						GsGrant gg = new GsGrant();
						gg.setFormGrant(g);
						map.put(gg.getFullGrantNumber(), gg);
					}

				}
			}

		} catch (GrantRetrieverException e) {
			e.printStackTrace();
			throw new GreensheetBaseException("error.usergrantlist", e);
		}

		return map;
	}

	private int getCurrentFiscalYear() {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int iy = cal.get(Calendar.YEAR);
		int im = cal.get(Calendar.MONTH);
		// Returns the current fiscal year
		if (im > 9) {
		    iy += 1;
		}
		
		return iy;
	}
	
	private int getFiscalStartingYear() {
	    int currentFiscalYear = this.getCurrentFiscalYear();
	    return (currentFiscalYear -1);
	}
	
	private int getFiscalEndingYear() {
	    int currentFiscalYear = this.getCurrentFiscalYear();   
		return currentFiscalYear;
	}
}
