/**
 * Copyright (c) 2002, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.fwrk;

import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigLoader;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * Struts plugin for application initialization routines
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class GreensheetsInitPlugIn implements PlugIn {

	/**
	 * @see org.apache.struts.action.PlugIn#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see org.apache.struts.action.PlugIn#init(ActionServlet, ModuleConfig)
	 */
	public void init(ActionServlet serv, ModuleConfig arg1)
			throws ServletException {

		String root = System.getProperty("conf.dir");

		String configPath = root + "/" + serv.getServletContext().getInitParameter("CONFIG_PATH");
		try {
			AppConfigLoader.initErrorMessages(serv.getServletContext().getRealPath("WEB-INF"));
			AppConfigLoader.initAppConfigProperties(configPath);
			AppConfigLoader.initDbProperties(configPath);
			AppConfigLoader.initPreferences(configPath);
			AppConfigLoader.initLdapProperties(configPath);
			System.out.println("\n<<<<<<<<< App configuration complete >>>>>>>>\n");
			
			// Abdul: Tomcat/Java 5 migration. Add runtime-platform specific path separator.
			AppConfigLoader.loadQuestionsXmlSrc(serv.getServletContext().getRealPath("WEB-INF") + File.separator); 

			DbConnectionHelper.getInstance().initOracleConnectionPool((Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_DB_PROPERTIES));

			System.out.println("\n<<<<<<<<<< Database configuration complete >>>>>>>>\n");

			AppConfigProperties.getInstance().addProperty("TEMPLATE_RESOURCE_MAP", new HashMap());
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
