/**
 * Copyright (c) 2002, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.fwrk;

import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.util.*;

import javax.servlet.*;
import org.apache.struts.action.*;
import org.apache.struts.config.*;

/**
 *  Struts plugin for application initialization routines
 * 
 * 
 *  @author kpuscas, Number Six Software
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
    public void init(ActionServlet serv, ModuleConfig arg1) throws ServletException {

        String root = System.getProperty("conf.dir");

        String configPath = root + "/" + serv.getServletContext().getInitParameter("CONFIG_PATH") + "/";

        try {
 
            
            AppConfigLoader.initErrorMessages(configPath);
            AppConfigLoader.initAppConfigProperties(configPath);
            AppConfigLoader.initDbProperties(configPath);
            AppConfigLoader.initLdapProperties(configPath);
            AppConfigLoader.loadQuestionsXmlSrc(configPath);

            System.out.println("\n<<<<<<<<< AppConfig Init Complete >>>>>>>>\n");

            DbConnectionHelper.getInstance().initOracleConnectionPool(
                (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_DB_PROPERTIES));

            System.out.println("\n<<<<<<<<<< Database Init Complete >>>>>>>>\n");
            
             AppConfigProperties.getInstance().addProperty("TEMPLATE_RESOURCE_MAP", new HashMap());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
