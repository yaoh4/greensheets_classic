package gov.nih.nci.iscs.numsix.apps.docchecker;

import gov.nih.nci.iscs.numsix.apps.docchecker.exception.DocCheckerException;
import gov.nih.nci.iscs.numsix.apps.docchecker.util.DbConnectionHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Date;
import java.sql.Statement;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

public class AppManager {
	public static final Logger logger = Logger.getLogger(AppManager.class);
	private static AppManager appManager = new AppManager();
	private DocCheckerConfig docCheckerConfig = null;
	private XMLConfiguration config = null;
	private Connection conn = null;
	private ResourceBundle dbBundle = null;
	private boolean isConnectedToDb = false;
	private EmailManager emailManager = null;
	private List reportRecipients; 
	private String emailMessage;
	private String emailSender;
	private String emailSubject;
	
	private AppManager() {
	}

	public void init() throws DocCheckerException {
		logger.debug("Inside init() method.");
		String appBuildDate = null;
		String reportLastRun = null;
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		docCheckerConfig = new DocCheckerConfig();
		emailManager = new EmailManager(IConstants.SMTP_HOST, IConstants.SMTP_PORT);
		Properties prop = new Properties();
		
		try {
			logger.debug("Initializing the application configuration information from the files.");
			config = new XMLConfiguration(IConstants.APP_CONFIG_FILE);

			// Load the application configuration information.
			docCheckerConfig.setBuildVersion(config.getString(IConstants.APP_BUILD_VERSION));
			appBuildDate = config.getString(IConstants.APP_BUILD_DATE);
			docCheckerConfig.setBuildDate((Date) formatter.parseObject(appBuildDate));
			docCheckerConfig.setRunningEnv(config.getString(IConstants.APP_RUNNING_ENV));
			docCheckerConfig.setDiskLocation(config.getString(IConstants.DOC_LOC_ON_DISK));
			docCheckerConfig.setDbPrefix(config.getString(IConstants.FILE_PATH_PREFIX));
			docCheckerConfig.setReportLoc(config.getString(IConstants.REPORT_LOC));			
			docCheckerConfig.setReportType(config.getString(IConstants.REPORT_TYPE));
			reportLastRun = config.getString(IConstants.REPORT_LAST_RUN);
			docCheckerConfig.setReportLastRun((Date) formatter.parseObject(reportLastRun));
			
			// Obtain the contents for the email message body from the .properties file on the local file system.
			this.setReportRecipients(config.getList(IConstants.EMAIL_REPORT_RECIPIENTS));
//			this.setEmailMessage(config.getString(IConstants.EMAIL_MESSAGE));
//			this.setEmailMessage(IConstants.EMAIL_MESSAGE);
	        prop.load(new FileInputStream(IConstants.EMAIL_MESSAGE_FILENAME));
	        this.setEmailMessage(prop.getProperty("message"));
			this.setEmailSender(config.getString(IConstants.EMAIL_SENDER));
			this.setEmailSubject(config.getString(IConstants.EMAIL_SUBJECT));
		} catch (ConfigurationException cex) {
			logger.error("ConfigurationException occurred. " + cex.getMessage() );
			// something went wrong, e.g. the file was not found
			throw new DocCheckerException("Exception occurred while initializing the application configuration information from the files." + cex.getMessage());
		} catch(ParseException pex) {
			logger.error("ParseException occurred. " + pex.getMessage() );
			 //Build date could not be obtained from the config file
			throw new DocCheckerException("Exception occurred while parsing the application configuration information from the files." + pex.getMessage());
		} catch(IOException ioex) {
			logger.error("IOException occurred. " + ioex.getMessage() );
			throw new DocCheckerException("Exception occurred while loading the properties file." + ioex.getMessage());			
		}
		this.setDbBundle(this.loadConfigInfo(IConstants.DB_CONFIG_FILE));
		logger.info("AppManager construction over.");
	}	
	
	public static AppManager getInstance() {
		return appManager;
	}

	// Load the configuration settings 
	public ResourceBundle loadConfigInfo(String configFile) {
		logger.debug("Inside loadConfigInfo().");
		ResourceBundle bundle = null;
		bundle = ResourceBundle.getBundle(configFile);
		return bundle;
	}
	
	public String createNewFileName(String reportType) {
		logger.debug("Creating the new file name for the report...");
		String newFileName = null;
		String formattedDateTime = null;
		Date currentDate = new Date();
		Format formatter;

		formatter = new SimpleDateFormat("yyyy_MMM_dd_hh_mm_ss");
		formattedDateTime = formatter.format(currentDate);
		newFileName = "DocChecker_" + formattedDateTime + "_" + reportType.substring(0, 4) + ".pdf";
		logger.debug("Coined new file name for the report: " + newFileName);
		return newFileName;
	}

	public Connection getConnection(boolean autoCommit)
			throws DocCheckerException {
		logger.debug("Inside AppManager.getConnection()");
		
		if (this.isConnectedToDb()) {
			return conn;
		}
		
		try {
			// Obtain the DB connection.
			conn = DbConnectionHelper.getInstance().getConnection(dbBundle);
			conn.setAutoCommit(autoCommit);
			this.setConnectedToDb(true);
		} catch (SQLException exp) {
			logger.error(exp.getMessage() + ". Error connecting to the Database.");
			throw new DocCheckerException("Error connecting to the Database.");
		} catch (Exception exp) {
			logger.error("Some error occurred while connecting to the Database.");
			throw new DocCheckerException("Some error occurred while connecting to the Database");
		}
		
		logger.debug("Exiting AppManager.getConnection()");
		return conn;
	}

	public void releaseConnection(Connection conn) throws DocCheckerException {
		logger.debug("Inside AppManager.releaseConnection()");
		
		if (!this.isConnectedToDb()) {
			return;
		}		
		
		try {
			// Disconnect from the DB.
			conn.close();
			this.setConnectedToDb(false);
		} catch (SQLException exp) {
			logger.error(exp.getMessage() + ". Error disconnecting from the Database.");
			throw new DocCheckerException("Error connecting from the Database.");
		} catch (Exception exp) {
			logger.error("Some error occurred while connecting from the Database.");
			throw new DocCheckerException("Some error occurred while connecting from the Database");
		}
		logger.debug("Exiting AppManager.releaseConnection()");		
	}	
	
	public void saveReportingDateStamp(Connection conn) throws DocCheckerException {
		logger.debug("Updating the datetimestamp the app was run...");
		
		String sql = "SELECT TO_CHAR(SYSDATE, 'DD-MON-YYYY') AS dbcurrentdate FROM DUAL";
		String formattedDateTime = null;
		String dbServerDate = null;
		Date currentDate = null;
		Format formatter;
		Statement stmt;
		
		formatter = new SimpleDateFormat("dd-MMM-yyyy");
		logger.debug("SQL = " + sql);
		
		try {
			stmt = conn.createStatement();
			logger.debug("created SQL Statement");
			stmt.execute(sql);
			logger.debug("executed SQL");
			ResultSet rs = stmt.getResultSet();
			logger.debug("got SQL resultset");
			while (rs.next()) {
				logger.debug("rs.next(): true");
				dbServerDate = rs.getString("dbcurrentdate");
			}
		} catch (SQLException cex) {
//			System.out.println(cex.getMessage());
			logger.error("Exception occurred while getting the current date from the Database Server.");
			throw new DocCheckerException("Could not update the report last run datestamp in the application configuration file.");
		}
		
		try {
			currentDate = (Date) formatter.parseObject(dbServerDate);
			formattedDateTime = formatter.format(currentDate);
		} catch (ParseException exp) {
			logger.error("Could not parese the DB Server's current date.");
			throw new DocCheckerException(
					"Could not parese the DB Server's current date.");
		}
		
		docCheckerConfig.setReportLastRun(currentDate);
		
		try {
			config.setProperty(IConstants.REPORT_LAST_RUN, formattedDateTime);
			config.save();
		} catch (ConfigurationException cex) {
//			System.out.println(cex.getMessage());
			logger.error("Exception occurred while saving the application configuration file by updating the datetimestamp the app was run.");
			throw new DocCheckerException("Exception occurred while saving the application configuration file by updating the datetimestamp the app was run.");
		}
		logger.info("Saved the application configuration file by updating the datetimestamp the app was run.");
	}

	public DocCheckerConfig getDocCheckerConfig() {
		return docCheckerConfig;
	}

	public void setDocCheckerConfig(DocCheckerConfig docCheckerConfig) {
		this.docCheckerConfig = docCheckerConfig;
	}
	
	public boolean isConnectedToDb() {
		return isConnectedToDb;
	}

	public void setConnectedToDb(boolean isConnectedToDb) {
		this.isConnectedToDb = isConnectedToDb;
	}

	public ResourceBundle getDbBundle() {
		return dbBundle;
	}

	public void setDbBundle(ResourceBundle dbBundle) {
		this.dbBundle = dbBundle;
	}

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}

	public List getReportRecipients() {
		return reportRecipients;
	}

	public void setReportRecipients(List reportRecipients) {
		this.reportRecipients = reportRecipients;
	}

	public String getEmailSender() {
		return emailSender;
	}

	public void setEmailSender(String emailSender) {
		this.emailSender = emailSender;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	
	public int emailReport(String reportFileName) throws DocCheckerException {
		logger.debug("Calling sendFile() method from the AppManager.");
		return emailManager.sendFile(reportRecipients,
							emailSender, 
							emailSubject, 
							emailMessage, 
							reportFileName);
	}
}
