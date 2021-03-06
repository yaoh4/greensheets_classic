/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr;

import gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager;
import gov.nih.nci.iscs.i2e.greensheets.GreensheetsResourceException;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormTemplateService;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Production Implementation of GreensheetResourceManager interface
 * 
 * @see gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager
 * @author kpuscas, Number Six Software
 */
public class GreensheetResourceDraftManagerImpl implements GreensheetResourceManager {

    private static final Logger logger = Logger
            .getLogger(GreensheetResourceDraftManagerImpl.class);

    private boolean frozen = false;

    /**
     * @see gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager#getResource(String, int)
     */
    public String getResource(String id, int type)
            throws GreensheetsResourceException {
System.out.println("............. in GreensheetResourceDraftManagerImpl");
        if (id.indexOf("F") > -1) {
            this.frozen = true;
            id = id.substring(1, id.length());
            logger.debug("New String Id = " + id);
        }

        GreensheetTemplateWrapper tw = null;
        //		try {
        //			tw = this.loadVelocityTemplate(id);
        //		} catch (Exception e) {
        //			throw new GreensheetsResourceException();
        //		}

        // ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        ApplicationContext context = new ClassPathXmlApplicationContext("GS_Velocity_ResourceMgmt.xml");
        // This is a much more compact context that contains only the few beans this class needs.
        
        GreensheetFormTemplateService greensheetFormTemplateService = (GreensheetFormTemplateService) context
                .getBean("greensheetFormTemplateService");
 
        String formTemplate = greensheetFormTemplateService.loadGreensheetFormDraftTemplate(id, this.frozen);

        tw = new GreensheetTemplateWrapper(Integer.parseInt(id), formTemplate);

        return tw.getTemplate();
    }

}
