/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.grantmgr;

import gov.nih.nci.iscs.i2e.dataretriever.ViewDataRetriever;
import gov.nih.nci.iscs.i2e.dataretriever.ViewDataRetrieverFactory;
import gov.nih.nci.iscs.i2e.grantretriever.FormGrant;
import gov.nih.nci.iscs.i2e.grantretriever.GrantRetrieverException;
import gov.nih.nci.iscs.i2e.grantretriever.LikeToken;
import gov.nih.nci.iscs.i2e.grantretriever.ListToken;
import gov.nih.nci.iscs.i2e.grantretriever.OrderByToken;
import gov.nih.nci.iscs.i2e.grantretriever.RangeToken;
import gov.nih.nci.iscs.i2e.grantretriever.ValueToken;
import gov.nih.nci.iscs.i2e.oracle.grantretrieverimpl.ViewGrantRetrieverImpl;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.Constants;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUserRole;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Production Implementation of GrantMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr
 * 
 * @author kpuscas, Number Six Software
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

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#getGrantsListForUser(GsUser,
	 *      boolean)
	 */
	public Map getGrantsListForUser(GsUser user, boolean paylineOnly,
			boolean myPortfolio) throws GreensheetBaseException {

		Map map = new HashMap();
		List l;
		try {
			ViewGrantRetrieverImpl vgr = new ViewGrantRetrieverImpl();
			vgr.setView("FORM_GRANT_VW");

			Properties p = (Properties) AppConfigProperties.getInstance()
					.getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);

			// Add the condition for Latest Budget Start Date.
			vgr.addCondition(this.getLatestBudgetStartDateCriteria());

			// Query Rules for Program users
			if (user.getRole().equals(GsUserRole.PGM_DIR)
					|| user.getRole().equals(GsUserRole.PGM_ANL)) {

				logger.debug("Start Program Grant List Query "
						+ new Date().toString());
				setBaseProgramGrantsListTokens(user, vgr, p);

				// Check if the payline was selected (paylineOnly=true/false)
				if (paylineOnly) {
					ValueToken vtps = new ValueToken();
					vtps.setColumnKey("withinPaylineFlag");
					vtps.setValue("Y");
					vgr.addCondition(vtps);
				}

				// Check if My Porfolio was selected (myPortfolio = true)
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

				// Add the condition for Latest Budget Start Date.
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

				logger.debug("End Program Grant List Query "
						+ new Date().toString());
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

	/**
	 * @see gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GrantMgr#searchForGrant(String,
	 *      String)
	 */
	public Map searchForGrant(String searchType, String searchText, GsUser user)
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
					if (searchType.trim().equalsIgnoreCase(
							GreensheetsKeys.SEARCH_GS_NUMBER)) {

						LikeToken lt = new LikeToken();
						logger.debug("searched on a grant number");
						lt.setColumnKey("fullGrantNum");
						lt
								.setValue("%" + searchText.trim().toUpperCase()
										+ "%");
						vgr.addCondition(lt);
					} else if (searchType.trim().equalsIgnoreCase(
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

					if (searchType.trim().equalsIgnoreCase(
							GreensheetsKeys.SEARCH_GS_NUMBER)) {

						LikeToken lt = new LikeToken();
						logger.debug("searched on a grant number");
						lt.setColumnKey("fullGrantNum");
						lt
								.setValue("%" + searchText.trim().toUpperCase()
										+ "%");
						vgr.addCondition(lt);
					} else if (searchType.trim().equalsIgnoreCase(
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
			throw new GreensheetBaseException("error.usergrantlist", e);
		}

		return map;
	}

	/** getGrantsListForProgramUser */
	public Map getGrantsListForProgramUser(GsUser user, String grantSource,
			String grantType, String mechanism, String onlyGrantsWithinPayline,
			String grantNumber, String piName) throws GreensheetBaseException {

		// initialize map and list result holders
		Map grantsMap = new HashMap();
		List grantsList;
		try {
			// use the view data retriever factory to get a handle on an 
			// initialized view data retriever object
			ViewDataRetrieverFactory drFactory = new ViewDataRetrieverFactory();
			ViewDataRetriever viewDataRetriever = drFactory
					.getViewDataRetriever("FORM_GRANT_VW");

			// add conditions here...
			addLatestBudgetStartDateCriteria(viewDataRetriever);
			addGrantSourceCriteria(viewDataRetriever, user, grantSource);		
			addGrantTypeCriteria(viewDataRetriever,grantType);
			addGrantsPaylineCriteria(viewDataRetriever, onlyGrantsWithinPayline);
			addMechanismCriteria(viewDataRetriever, mechanism);
			addGrantNumberCriteria(viewDataRetriever, grantNumber);
			addPiNameCriteria(viewDataRetriever, piName);

			// execute query and put in grants list
			grantsList = viewDataRetriever.getDataList();

		} catch (Exception e) {
			throw new GreensheetBaseException("error.usergrantlist", e);
		}

		// prepare result for return
		int grantsListSize = grantsList.size();
		logger.debug("NUMBER Of Grants Found " + grantsListSize);
		for (int i = 0; i < grantsListSize; i++) {
			FormGrant formGrant = (FormGrant) grantsList.get(i);
			GsGrant gsGrant = new GsGrant();
			gsGrant.setFormGrant(formGrant);
			grantsMap.put(gsGrant.getFullGrantNumber(), gsGrant);
		}
		// return result
		return grantsMap;
	}

	/** getLatestBudgetStartDateCriteria */
	private RangeToken getLatestBudgetStartDateCriteria() {
		int fiscalStartingYear = this.getFiscalStartingYear();
		int fiscalEndingYear = this.getFiscalEndingYear();

		String startingDateString = "October 1, " + fiscalStartingYear;
		String endingDateString = "September 30, " + fiscalEndingYear;

		DateFormat df = DateFormat.getDateInstance();
		Date startingDate = null;
		Date endingDate = null;

		try {
			startingDate = df.parse(startingDateString);
		} catch (ParseException e) {
			System.out.println("Unable to parse starting date- "
					+ startingDateString);
		}

		try {
			endingDate = df.parse(endingDateString);
		} catch (ParseException e) {
			System.out.println("Unable to parse ending date- "
					+ endingDateString);
		}

		RangeToken rtps = new RangeToken();
		rtps.setColumnKey("latestBudgetStartDate");
		rtps.setMinValue(startingDate);
		rtps.setMaxValue(endingDate);
		rtps.setInclusive(true);

		return rtps;
	}

	/** setBaseProgramGrantsListTokens */
	private void setBaseProgramGrantsListTokens(GsUser user,
			ViewGrantRetrieverImpl vgr, Properties p)
			throws GrantRetrieverException {

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

		ValueToken vt4 = new ValueToken();
		vt4.setColumnKey("applStatusGroupCode");
		vt4.setValue("A");
		vt4.setComparison(ValueToken.EQUALS);
		vt4.setNegative(true);
		vgr.addCondition(vt4);

		String minNames = p.getProperty("minoritysupplements.userids");

		if (StringUtils.contains(minNames, user.getOracleId())) {
			ValueToken vt5 = new ValueToken();
			vt5.setColumnKey("mbMinorityFlag");
			vt5.setValue("Y");
			vgr.addCondition(vt5);
		} else {
			ListToken lt = new ListToken();
			lt.setColumnKey("cayCode");
			lt.setValue(user.getCancerActivities());
			vgr.addCondition(lt);
		}

		OrderByToken obt = new OrderByToken();
		obt.setFieldName("budgetStartDate");
		obt.setSortOrder("ASC");
	}

	/** getCurrentFiscalYear */
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

	/** getFiscalStartingYear */
	private int getFiscalStartingYear() {
		int currentFiscalYear = this.getCurrentFiscalYear();
		return (currentFiscalYear - 1);
	}

	/** getFiscalEndingYear */
	private int getFiscalEndingYear() {
		int currentFiscalYear = this.getCurrentFiscalYear();
		return currentFiscalYear;
	}

	/** addLatestBudgetStartDateCriteria */
	private void addLatestBudgetStartDateCriteria(
			ViewDataRetriever viewDataRetriever) throws Exception {
		viewDataRetriever.addCondition(this.getLatestBudgetStartDateCriteria());
	}

	/** addGrantsPaylineCriteria */
	private void addGrantsPaylineCriteria(ViewDataRetriever viewDataRetriever,
			String onlyGrantsWithinPayline) throws Exception {
		ValueToken vtps = new ValueToken();
		vtps.setColumnKey("withinPaylineFlag");
		if (onlyGrantsWithinPayline.equals(Constants.PREFERENCES_YES)) {
			vtps.setValue("Y");
		} else {
			vtps.setValue("N");
		}
		viewDataRetriever.addCondition(vtps);
	}
	
	/** addGrantSourceCriteria */
	private void addGrantSourceCriteria(ViewDataRetriever viewDataRetriever, GsUser user,
			String grantSource) throws Exception {

		if (grantSource.equals(Constants.PREFERENCES_MYPORTFOLIO) && (user.getMyPortfolioIds() != null)) {
			ListToken lt = new ListToken();
			lt.setColumnKey("pdNpeId");
			lt.setValue(user.getMyPortfolioIds());
			viewDataRetriever.addCondition(lt);
		}
		
		if (grantSource.equals(Constants.PREFERENCES_MYCANCERACTIVITY)) {
			ListToken lt = new ListToken();
			lt.setColumnKey("cayCode");
			lt.setValue(user.getCancerActivities());
			viewDataRetriever.addCondition(lt);			
		}
		
		if (grantSource.equals(Constants.PREFERENCES_ALLNCIGRANTS)) {
			// do nothing	
		}
   }

    /** addGrantTypeCriteria */
	private void addGrantTypeCriteria(ViewDataRetriever viewDataRetriever,
			String grantSource) throws Exception {
		
		if (grantSource.equals(Constants.PREFERENCES_NONCOMPETINGGRANTS)) {
			LikeToken lt = new LikeToken();
			lt.setColumnKey("councilMeetingDate");
			lt.setValue("%00");
			viewDataRetriever.addCondition(lt);			
		}
		
		if (grantSource.equals(Constants.PREFERENCES_COMPETINGGRANTS)) {
			LikeToken lt = new LikeToken();
			lt.setColumnKey("councilMeetingDate");
			lt.setValue("%00");
			lt.setNegative(true);
			viewDataRetriever.addCondition(lt);			
		}		
		
		if (grantSource.equals(Constants.PREFERENCES_BOTH)) {
			 // do nothing
		}
		
	}
	
    /** addMechanismCriteria */
	private void addMechanismCriteria(ViewDataRetriever viewDataRetriever,
			String mechanism) throws Exception {	
		if (!(mechanism==null||mechanism.equals(null)||mechanism.equals(""))) {
			ValueToken vt = new ValueToken();
			vt.setColumnKey("activityCode");
			vt.setValue(mechanism.toUpperCase());
			viewDataRetriever.addCondition(vt);			
		} else  {
			 // do nothing
		}
	}	
	
    /** addGrantNumberCriteria */
	private void addGrantNumberCriteria(ViewDataRetriever viewDataRetriever,
			String grantNumber) throws Exception {	
		if (!(grantNumber==null||grantNumber.equals(null)||grantNumber.equals(""))) {
			ValueToken vt = new ValueToken();
			vt.setColumnKey("fullGrantNum");
			vt.setValue(grantNumber.toUpperCase());
			viewDataRetriever.addCondition(vt);			
		} else  {
			 // do nothing
		}
	}		

    /** addPiNameCriteria */
	private void addPiNameCriteria(ViewDataRetriever viewDataRetriever,
			String piName) throws Exception {	
		if (!(piName==null||piName.equals(null)||piName.equals(""))) {
			LikeToken lt = new LikeToken();
			lt.setColumnKey("piName");
			lt.setValue("%"+piName.toUpperCase()+"%");
			viewDataRetriever.addCondition(lt);				
		} else  {
			 // do nothing
		}
	}		
}