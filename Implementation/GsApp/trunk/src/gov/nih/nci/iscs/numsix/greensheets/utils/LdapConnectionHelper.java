/**
 * Copyright (c) 2002, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */
package gov.nih.nci.iscs.numsix.greensheets.utils;

import gov.nih.nci.iscs.util.*;
import java.util.*;

import javax.naming.directory.*;
import org.apache.log4j.*;
/**
 * Helper class used to establish connection to the NIC ldap system
 * 
 * @author kpuscas
 *
 */
public class LdapConnectionHelper {


    private static String LDAP_CONFIG_FILENAME;

 
    protected DirCtxDispenser dirCtxDispenser;
    private Properties configParameters;


    private static final Logger logger = Logger.getLogger(LdapConnectionHelper.class);
    /**
     * Creates a new LdapConnectionHelper object.
     *
     * @param configFile DOCUMENT ME!
     */
    public LdapConnectionHelper(Properties props) {
        this.configParameters = props;
    }


    /**
     * returns an LDAP context used for searching and getting attributes from the LDAP
     *
     * @return <code>DirContext</code> - connection to the LDAP
     * @throws Exception
     */
    public synchronized DirContext getConnection() throws Exception {


        DirContext context = null;
        if(dirCtxDispenser == null) {
            logger.info("Constructing LdapConnection from dispenser!!! ");
            dirCtxDispenser = new DirCtxDispenser();
            try {
                logger.info("configName = " + configParameters.getProperty("configname"));
                logger.info("password = " + configParameters.getProperty("password"));

                List list = DirCtxDispenserConfigurator.getConfig((String)configParameters.getProperty("configname"),
                                                                  (String)configParameters.getProperty("password"));
                dirCtxDispenser.init(list);
            } catch(DirCtxDispenserException e) {
                throw new Exception(" Error when getting a context in - getContext() ");
            }
        }

        try {
            logger.info("Getting context from Dispenser...");
            logger.info("dirCtxDispenser = null?? " + (dirCtxDispenser == null));
            context = dirCtxDispenser.getContext();
            logger.debug("...Got context from Dispenser");
        } catch(Exception e) {
            
            throw new Exception("Problem obtaining context from contextDispenser - getContext()");
        }

        return context;
    }

    /**
     * Returns the search base used for the given context.
     *
     * @param context connection to the LDAP
     * @return <code>String</code> - search base for the given context
     * @throws DirCtxDispenserException exception in getting a context
     * @throws IntraException wraps any exceptions generated
     */
    public String getContextName(DirContext context) throws Exception {

        String contextName = null;
        if(context == null) {
            throw new Exception("Error getting the context name");
        } else {
            try {

                Hashtable env = context.getEnvironment();
                contextName = (String)configParameters.get("contextName");
            } catch(javax.naming.NamingException e) {
                throw new Exception(" exception in getContextName(context)");
            }
        }

        if(contextName == null) {

            contextName = (String)configParameters.get("contextName");
        }


        return contextName;
    }

}
