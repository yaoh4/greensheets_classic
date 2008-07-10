package gov.nih.nci.iscs.numsix.greensheets.utils;


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
// log4j imports
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

    

public class Log4J extends HttpServlet {

//    private Logger logger;
	private static final Logger logger = Logger.getLogger(Log4J.class);
	
    //Initialize global variables
    public void init() throws ServletException {
        // initialize the log file so that the messages can be outputted to the log
        initializeLog4j();
        logger.debug("The effective level of the logger " + logger.getName() + " is " + logger.getEffectiveLevel() + ".");	// getLevel() returns the Assigned Level.
        logger.debug("Log4j: Turned on to log debug messages.");
        logger.info("Log4j: Turned on to log informational messages.");
        logger.warn("Log4j: Turned on to log warning messages.");
        logger.error("Log4j: Turned on to log error messages.");
        logger.fatal("Log4j: Turned on to log fatal messages.");
    }

    /**
	 * This method initializes log4j properties this is called from the init()
	 * method of the servelet The method loads the parameter(log4j-init-file)
	 * from the web.xml file
	 */
	private void initializeLog4j() {
		System.out.println(" inside initializeLog4j() ");
		File file = null;
		
        String root = System.getProperty("conf.dir");
        String log4jPropFile = root + "/"
				+ getServletContext().getInitParameter("CONFIG_PATH")
				+ "/"
				+ getServletContext().getInitParameter("log4j-init-file");
        System.out.println("The log4j properties file:" + log4jPropFile);
        if ((log4jPropFile != null) && !(log4jPropFile.trim().equals(""))) {
        	file = new File(log4jPropFile);
    		if (!file.exists()) {
    			System.out.println("log4j.properties file does not exist and log4j COULD NOT BE INITIALIZED.");
    		} else {
    			System.out.println("Initializing log4j ...");
    			PropertyConfigurator.configure(log4jPropFile);
    			System.out.println("Log4j INITIALIZED for Greensheets application.");
    		}        	
        } else {
        	System.out.println("Cannot determine the name of the log4j properties file and log4j COULD NOT BE INITIALIZED.");        	
        }
	}

    // Process the HTTP Get request
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException,
        IOException {
        System.out.println("Do GET");
    }


    //Process the HTTP Post request
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException,
        IOException {
        System.out.println("Do Post");
    }

    //Clean up resources
    public void destroy() {
    }
}
