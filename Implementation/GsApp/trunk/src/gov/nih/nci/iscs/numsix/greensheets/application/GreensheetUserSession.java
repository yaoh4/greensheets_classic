/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr.GreensheetFormProxy;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * @author kpuscas, Number Six Software
 */
public class GreensheetUserSession implements HttpSessionBindingListener, Serializable {

    private static final long serialVersionUID = -7209209641340843953L;

    private Map greensheetFormSessions = new HashMap();

    private GsUser user;

    //	private Map grantsMap = new HashMap(); //Abdul Latheef: Task to do: Commented out for time being!!!

    private boolean paylineOnly = true;

    private boolean newSession = false;

    private boolean myPortfolio = false;

    private static final Logger logger = Logger.getLogger(GreensheetUserSession.class);

    /**
     * Constructor for GreensheetUserSession.
     */
    public GreensheetUserSession(GsUser user) {
        this.user = user;
        this.newSession = true;
    }

    /**
     * Method addGrant to the users Session.
     * 
     * @param g
     */
    //	public void addGrant(GsGrant g) {	//Abdul Latheef: Used FormGrant instead of GsGrant.
    //		this.grantsMap.put(g.getFullGrantNumber(), g);
    //	}

    //	public void addGrant(FormGrant formGrant) {	//Abdul Latheef: Task to do: Commented out the method for time being!!!
    //		this.grantsMap.put(formGrant.getFullGrantNum(), formGrant);
    //	}

    /**
     * Method setGrants.
     * 
     * @param map
     */
    //	public void setGrants(Map map) {	//Abdul Latheef: Task to do: Commented out the method for time being!!!
    //		this.grantsMap.clear();
    //		this.grantsMap.putAll(map);
    //	}

    /**
     * Method getGrantByGrantNumber.
     * 
     * @param grantNumber
     * @return GsGrant
     */
    //	public GsGrant getGrantByGrantNumber(String grantNumber) { //Abdul Latheef: Used FormGrant instead of GsGrant.
    //		return (GsGrant) grantsMap.get(grantNumber);
    //	}

    //	public FormGrant getGrantByGrantNumber(String grantNumber) {	//Abdul Latheef: Task to do: Commented out the method for time being!!!
    //		return (FormGrant) grantsMap.get(grantNumber);
    //	}

    /**
     * Method addGreensheetFormSession.
     * 
     * @param gfs
     * @return String
     */
    public String addGreensheetFormSession(GreensheetFormSession gfs) {
        String id = RandomStringUtils.randomAlphabetic(5);
        greensheetFormSessions.put(id, gfs);
        return id;
    }

    /**
     * Method getGreensheetFormSession.
     * 
     * @param id
     * @return GreensheetFormSession
     */
    public GreensheetFormSession getGreensheetFormSession(String id) {
        return (GreensheetFormSession) greensheetFormSessions.get(id);
    }

    /**
     * @return the map of possibly multiple "Greensheet Form" objects the user has opened and whose data is being maintained in this
     *         GreensheetsUserSession.
     */
    public Map getGreensheetFormSessions() {
        return greensheetFormSessions;
    }

    public GreensheetFormProxy getGreensheetForm(String formSessionId) {
        GreensheetFormProxy g = ((GreensheetFormSession) greensheetFormSessions
                .get(formSessionId)).getForm();
        return g;
    }

    /**
     * Method removeGreensheetFormSession.
     * 
     * @param id
     */
    public void removeGreensheetFormSession(String id) {
        greensheetFormSessions.remove(id);
    }

    /**
     * Returns the user.
     * 
     * @return GsUser
     */
    public GsUser getUser() {
        return user;
    }

    /**
     * Method getFormSessionGrant.
     * 
     * @param formSessionId
     * @return GsGrant
     */
    //	public GsGrant getFormSessionGrant(String formSessionId) { //Abdul Latheef: Method is rewritten using new FormGrant instead of the old GsGrant.
    //		GsGrant g = ((GreensheetFormSession) greensheetFormSessions
    //				.get(formSessionId)).getGrant();
    //		return g;
    //	}

    public FormGrantProxy getFormSessionGrant(String formSessionId) {
        GreensheetFormSession oneFormSession = null;
        FormGrantProxy grant = null;
        if (formSessionId != null && !"".equals(formSessionId)) {
            oneFormSession = (GreensheetFormSession) greensheetFormSessions.get(formSessionId);
            if (oneFormSession != null) {
                grant = oneFormSession.getGrant();
            }
        }
        return grant;
    }

    /**
     * Returns the paylineOnly.
     * 
     * @return boolean
     */
    public boolean isPaylineOnly() {
        return paylineOnly;
    }

    public void flipPayLineOnly() {
        if (this.paylineOnly) {
            this.paylineOnly = false;
        } else {
            this.paylineOnly = true;
        }
    }

    /**
     * Sets the paylineOnly.
     * 
     * @param paylineOnly
     *            The paylineOnly to set
     */
    public void setPaylineOnly(boolean paylineOnly) {
        this.paylineOnly = paylineOnly;
    }

    /**
     * Returns the newSession.
     * 
     * @return boolean
     */
    public boolean isNewSession() {
        return newSession;
    }

    /**
     * Sets the newSession.
     * 
     * @param newSession
     *            The newSession to set
     */
    public void setNewSession(boolean newSession) {
        this.newSession = newSession;
    }

    /**
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent be) {

        logger.debug("User Session Bound:: user "
                + this.user.getRemoteUserName() + "  sessionId "
                + be.getSession().getId());

    }

    /**
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent be) {

        logger.debug("User Session Unbound:: user "
                + this.user.getRemoteUserName() + "  sessionId "
                + be.getSession().getId());
    }

    /**
     * Returns the myPortfolio.
     * 
     * @return boolean
     */
    public boolean isMyPortfolio() {
        return myPortfolio;
    }

    /**
     * Sets the myPortfolio.
     * 
     * @param myPortfolio
     *            The myPortfolio to set
     */
    public void setMyPortfolio(boolean myPortfolio) {
        this.myPortfolio = myPortfolio;
    }

}
