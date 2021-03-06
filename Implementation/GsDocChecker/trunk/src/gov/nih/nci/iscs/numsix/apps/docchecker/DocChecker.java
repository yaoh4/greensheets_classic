package gov.nih.nci.iscs.numsix.apps.docchecker;

import gov.nih.nci.iscs.numsix.apps.docchecker.exception.DocCheckerException;
import gov.nih.nci.iscs.numsix.apps.docchecker.util.ReportWriter;

import java.sql.Connection;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class DocChecker {
	public static final Logger logger = Logger.getLogger(DocChecker.class);
		
	public static void main(String[] args) {
		logger.debug("Entering the Greensheets Document Checker application.");
		DocChecker docChecker;

//		System.out.println("args.length = " + args.length);
//		for(int i = 0; i< args.length; i++) {
//			System.out.println("args.length = " + args[i]);
//		}
		
		try {
			docChecker = new DocChecker();
			docChecker.initializeLog4J();
	        logger.debug("The effective level of the logger " + logger.getName() + " is " + logger.getEffectiveLevel() + ".");	// getLevel() returns the Assigned Level.
	        logger.debug("Log4j: Turned on to log debug message.");
	        logger.info("Log4j: Turned on to log informational message.");
	        logger.warn("Log4j: Turned on to log warning message.");
	        logger.error("Log4j: Turned on to log error message.");
	        logger.fatal("Log4j: Turned on to log fatal message.");			
			docChecker.runDocChecker(args);
		} catch (DocCheckerException exp) {
			exp.printStackTrace();
			logger.error("Error running the DocChecker application. Exiting the Greensheets Document Checker application." + exp.getMessage());
			System.exit(-1);			
		}
		logger.info("Exiting the Greensheets Document Checker application successfully.");
	}
	
	public void runDocChecker(String prefs[]) throws DocCheckerException {
		AppManager appManager = null;
		DocCheckerConfig docCheckerConfig = null;
		DocCheckerResults docCheckerResults = null; 
		FilesVerifier filesVerifier = null;
		ReportWriter reporter = null;
		String reportFileName = null;
		String reportType = null;
		Date reportLastRun = null;		
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		boolean reportTypeOverrided = false;
		int index;
		Connection conn = null;
		
		logger.debug("Getting an instance of the application manager.");
		appManager = AppManager.getInstance();
		appManager.init();
		logger.debug("Obtained an instance of the application manager.");
		
		logger.debug("Getting DocCheckerConfig bean.");
		docCheckerConfig = appManager.getDocCheckerConfig();
//		dbBundle = appManager.loadConfigInfo(IConstants.DB_CONFIG_FILE);
		
//		Read the command line parameters. 
		if (prefs.length > 0) {
			logger.info("Parsing the command line parameters...");
			index = 0;
			while (index < prefs.length) {
				if (IConstants.CMD_LINE_OPTION_REPORT_TYPE.equalsIgnoreCase(prefs[index])) {
					if (IConstants.REPORT_TYPE_FULL.equalsIgnoreCase(prefs[index + 1]) || IConstants.REPORT_TYPE_INCREMENTAL.equalsIgnoreCase(prefs[index + 1])) {
						docCheckerConfig.setReportType(prefs[index + 1].toUpperCase());
						logger.debug("There was a command line parameter for the Report type: " + prefs[index + 1].toUpperCase());
						index = index + 2;
						reportTypeOverrided = true;
						continue;
					} else {
						logger.error("Invalid switch entered for the Report type.");
						throw new DocCheckerException("Invalid switch. Use the format: " + IConstants.CMD_LINE_OPTION_REPORT_TYPE + " " + IConstants.REPORT_TYPE_FULL + "|" + IConstants.REPORT_TYPE_INCREMENTAL);
					}
				} else if (IConstants.CMD_LINE_OPTION_REPORT_FROM.equalsIgnoreCase(prefs[index])) {
					try {
						reportLastRun = (Date) formatter.parseObject(prefs[index + 1]);
						docCheckerConfig.setReportLastRun(reportLastRun);
						logger.debug("There was a command line parameter for the Date the Report was last run." + prefs[index + 1]);
						index = index + 2;
						continue;
					} catch(ParseException exp) {
						logger.error("ParseException occurred while parsing the command line parameters. Invalid switch entered for the Date the Report was last run.");
						throw new DocCheckerException("Invalid switch. Use the format: " + IConstants.CMD_LINE_OPTION_REPORT_FROM + " " + "DD-MON-YYYY");
					}
				} else {
					index++;
					continue;					
				}
			}
			logger.info("Parsing the command line parameters is over.");
		}
		
//		Determine the Report type if it is not overrided.
		reportType = docCheckerConfig.getReportType();
		if (!reportTypeOverrided && reportType == null) {
			logger.error("Could not decide on the Report type. Check the Application config file or enter a valid command line option for the report type.");
			throw new DocCheckerException("Could not decide on the Report type. Check the Application config file or enter a valid command line option for the report type.");
		}
		
//		Determine the date when the utility needs to check the datestamps.
		if (reportType != null && IConstants.REPORT_TYPE_INCREMENTAL.equalsIgnoreCase(reportType)) {
			reportLastRun = docCheckerConfig.getReportLastRun();
			if (reportLastRun == null) {
				logger.error("Could not decide on the Report Date. Check the Application config file or enter a valid command line option for the date the report needs to run from.");
				throw new DocCheckerException("Could not decide on the Report Date. Check the Application config file or enter a valid command line option for the date the report needs to run from.");				
			}
		}
		
		logger.debug("Coining the new file name for the report.");
		reportFileName = appManager.createNewFileName(reportType);

		conn = appManager.getConnection(false);
		
		filesVerifier = new FilesVerifier();
		logger.debug("Calling FilesVerifier.verifyFiles()...");
		docCheckerResults = filesVerifier.verifyFiles(conn);
		appManager.releaseConnection(conn);
		
		reportFileName = docCheckerConfig.getReportLoc() + reportFileName;
		
		reporter = new ReportWriter();
		logger.debug("Calling ReportWriter.writeToPdf()...");
		reporter.writeToPdf(docCheckerResults, reportFileName);
		
		conn = appManager.getConnection(false);
		if (IConstants.REPORT_TYPE_INCREMENTAL.equalsIgnoreCase(reportType)) {
			appManager.saveReportingDateStamp(conn);
		}
		appManager.releaseConnection(conn);

		logger.debug("Calling emailReport() from DocChecker.");
		appManager.emailReport(reportFileName);

		logger.debug("Returning from runDocChecker()...");
	}
	
	private void initializeLog4J() {
        System.out.println(" Inside initializeLog4J() ");

        String log4jPropFile = IConstants.LOG4J_PROP_FILE;
        System.out.println("Log4j properties file:" + log4jPropFile);
        if(log4jPropFile != null && !(log4jPropFile.trim().equals(""))) {
            System.out.println("Initializing from the the Log4j properties file...");
            PropertyConfigurator.configure(log4jPropFile);
            logger.info("Initialized from the the Log4j properties file successfully.");
        } else {
            System.out.println("Could not initialize from the the log4j.properties file.");
        }
	}
}
