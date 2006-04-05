/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;

import java.util.*;
import org.apache.log4j.*;
import java.sql.*;

/**
 * Production Implementation of GreensheetPreferencesMgr interface
 * 
 * @see gov.nih.nci.iscs.numsix.greensheets.services.greensheetpreferencesmgr
 * 
 *  @author kkanchinadam, Number Six Software
 */
public class GreensheetPreferencesMgrImpl implements GreensheetPreferencesMgr {

	private static final Logger logger =
		Logger.getLogger(GreensheetPreferencesMgrImpl.class);

	public GreensheetPreferencesMgrImpl() {
    }
	
	public HashMap getUserPreferences()  {
		HashMap userPrefs = new HashMap();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbConnectionHelper.getInstance().getConnection();
			stmt = conn.createStatement();
			
			String sql = "SELECT PREFERENCE_NAME, PREFERENCE_VALUE FROM USER_PREFERENCES_T WHERE APPLICATION_NAME='GREENSHEETS' AND USERNAME='GS_PD'";
			logger.debug("Retrieving user preferences, sql is =" + sql);
			
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String prefName = rs.getString("PREFERENCE_NAME");
				String prefValue = rs.getString("PREFERENCE_VALUE");
				userPrefs.put(prefName, prefValue);				
			}
		}
		catch (Exception e) {
			logger.debug(e.getMessage());
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				
				DbConnectionHelper.getInstance().freeConnection(conn);
			}
			catch (Exception e) {
				logger.debug(e.getMessage());
			}
		}
				
		return userPrefs;
	}
	
	public void saveUserPreferences(HashMap userPrefs) throws GreensheetBaseException {
		
	}
}
