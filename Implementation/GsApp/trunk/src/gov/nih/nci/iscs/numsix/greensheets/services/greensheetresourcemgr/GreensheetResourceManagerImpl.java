/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetresourcemgr;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager;
import gov.nih.nci.iscs.i2e.greensheets.GreensheetsResourceException;
import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetFormTemplateService;

/**
 * Production Implementation of GreensheetResourceManager interface
 * 
 * @see gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager
 * @author kpuscas, Number Six Software
 */
public class GreensheetResourceManagerImpl implements GreensheetResourceManager {

    private static final Logger logger = Logger
            .getLogger(GreensheetResourceManagerImpl.class);

    private boolean frozen = false;

    /**
     * @see gov.nih.nci.iscs.i2e.greensheets.GreensheetResourceManager#getResource(String, int)
     */
    public String getResource(String id, int type)
            throws GreensheetsResourceException {

    	String[] split = id.split(",");
		String templateId = split[0];
		String applTypeCode = split[1];
		String activityCode = split[2];
		
		if (templateId.indexOf("F") > -1) {
            this.frozen = true;
            templateId = templateId.substring(1, templateId.length());
            logger.debug("New String Id = " + templateId);
        }

		id = templateId + "," + applTypeCode + "," + activityCode;
		
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

        //application.getAttribute("draftData");
       // HttpServletRequest req = sra.getRequest();     
        //System.out.println("Inside GreensheetResourceManagerImpl.java requestAtt is " +requestAtt);
        //HttpServletRequest req = requestAtt.getRequest();
       // System.out.println("Inside GreensheetResourceManagerImpl.java request is " +request);
        String isDraft = "No";
        String formTemplate = null;
      //  if (request.getSession().getAttribute("draftDisplay") != null){
        	//isDraft = (String)request.getSession().getAttribute("draftDisplay");
       // }
        
        //System.out.println("Inside GreensheetResourceManagerImpl.java isDraft is " +isDraft);
    // id = "1723";
        formTemplate = greensheetFormTemplateService.loadGreensheetFormTemplate(id, this.frozen);
       // System.out.println("Inside GreensheetResourceManagerImpl.java formTemplate is " +formTemplate);
        
        //write a new method by checking session value 

       tw = new GreensheetTemplateWrapper(Integer.parseInt(templateId), formTemplate);
      // System.out.println("Inside GreensheetResourceManagerImpl.java tw is " +tw);
        return formTemplate;
    }

}
