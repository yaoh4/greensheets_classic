package gov.nih.nci.iscs.numsix.greensheets.utils;

import gov.nih.nci.cbiit.atsc.dao.FormGrant;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * Utility class that keeps track of when e-mail notifications have been sent to
 * support e-mail address about specific grant numbers / appl_ids.  An instance of 
 * this class is meant to be placed in web application's context at startup, and 
 * calls to recordTheSending(grant) on that instance should be made whenever a 
 * notification is sent. (The method sending the message should get the instance of
 * this class from the web app context.)   
 *   
 * @author Anatoli Kouznetsov
 *
 */
public class RedundantEmailPreventer {

	/** grant numbers about which the needed notifications to support e-mail address have 
	 * already been sent, along with date sent */
	private Map<String, Date> grantNumbers;
	
	/** appl_ids about which the needed notifications to support e-mail address have 
	 * already been sent, along with date sent */
	private Map<String, Date> applIds;
	
	
	public RedundantEmailPreventer() {
		grantNumbers = new HashMap<String, Date>();
		applIds = new HashMap<String, Date>();
	}
	

	/** grant numbers about which the needed notifications to support e-mail address have 
	 * already been sent, along with date sent */
	public Map<String, Date> getGrantNumbers() {
		return grantNumbers;
	}

	public void setGrantNumbers(HashMap<String, Date> grantNumbers) {
		this.grantNumbers = grantNumbers;
	}

	/** appl_ids about which the needed notifications to support e-mail address have 
	 * already been sent, along with date sent */
	public Map<String, Date> getApplIds() {
		return applIds;
	}

	public void setApplIds(HashMap<String, Date> applIds) {
		this.applIds = applIds;
	}
	
	/** 
	 * @return has a notification about this grant number been e-mailed within the 
	 * last 30 days?
	 */
	public boolean grantNumberNotificationAlreadySent (String grantNumber) {
		boolean alreadySent = false;
		if (grantNumbers !=null) {
			Date dateWhenSent = grantNumbers.get(grantNumber);
			if (dateWhenSent!=null) {
				if (!dateExpired(dateWhenSent)) {
					alreadySent = true;
				}
			}
		}
		return alreadySent;
	}
	
	/** 
	 * @return  has a notification about this appl_id been e-mailed within the 
	 * last 30 days?
	 */
	public boolean applIdNotificationAlreadySent (String applId) {
		boolean alreadySent = false;
		if (applIds !=null) {
			Date dateWhenSent = applIds.get(applId);
			if (dateWhenSent!=null) {
				if (!dateExpired(dateWhenSent)) {
					alreadySent = true;
				}
			}
		}
		return alreadySent;		
	}
	
	
	/**
	 *  Add to the tracking list of dates when notifications were e-mailed to the 
	 *  support e-mail address the date when a notification was sent about this grant.  
	 */
	public void recordTheSending(FormGrant grant) {
		Date d = new Date();
		String grantNumber = grant.getFullGrantNum();
		String applId = Long.toString(grant.getApplId());
		if (grantNumber!=null && !"".equals(grantNumber) && grantNumbers!=null) {
			grantNumbers.put(grantNumber, d);
		}
		if (applId!=null && !"0".equals(applId) && !"".equals(applId) 
				&& applIds!=null) {
			applIds.put(applId, d);
		}
	}
	
	
	private boolean dateExpired (Date d) {
		boolean expired = false;
		if (d != null) {
			Calendar cal = GregorianCalendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -30);  
			// With this ^, cal stores the date that's 30 days ago from 
			// right now. 30 is a hard-coded arbitrary value that could be 
			// refactored to be configurable, if it's worth the trouble.
			if (d.before(cal.getTime())) {
				expired = true;
			}
		}
		return expired;
	}
}
