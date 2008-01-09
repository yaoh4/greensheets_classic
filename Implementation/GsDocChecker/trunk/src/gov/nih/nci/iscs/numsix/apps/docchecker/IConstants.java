package gov.nih.nci.iscs.numsix.apps.docchecker;

public interface IConstants {
	// RECONCILE, LOCATE, and NONE are the three actions to be performed
	public static final String ACTION_NONE = "NONE";
	public static final String ACTION_LOCATE = "LOCATE";
	public static final String ACTION_RECONCILE = "RECONCILE";
	
	public static final String APP_CONFIG_FILE = "conf/config.xml";	// Stores the application specific configuration.
	public static final String DB_CONFIG_FILE = "conf.db";	// Stores the database User ID/Password, and connection string.
//	public static final String DB_CONFIG_FILE = "conf/db.properties";	// Stores the database User ID/Password, and connection string.
	public static final String LOG4J_PROP_FILE = "conf/log4j.properties"; // Not needed right now.

	// Following are the names of the elements, attributes used in the Application Configuration file.
	public static final String APP_BUILD_DATE = "application.build-date";
	public static final String APP_BUILD_VERSION = "application.build-version";
	public static final String APP_RUNNING_ENV = "running-env";
	public static final String DOC_LOC_ON_DISK = "environment.disk-location";
	public static final String FILE_PATH_PREFIX = "environment.db-prefix";
	public static final String REPORT_LOC = "environment.report-loc";
	public static final String REPORT_TYPE = "report.report-type";
	public static final String REPORT_LAST_RUN = "report.report-last-run";
	public static final String EMAIL_REPORT_RECIPIENTS = "report-recipients.subscribers-email";
	public static final String EMAIL_MESSAGE_FILENAME = "conf/email_body.properties";
//	public static final String EMAIL_MESSAGE = "report-recipients.message-body";
//	public static final String EMAIL_MESSAGE = "Note: This is an automatically generated message."
//												+ " Please do not reply to it. If you have concerns about this message, please send an e-mail to nciweb@mail.nih.gov."
//												+ "\n\r"
//												+ "Attached is a Greensheets Attachments Reconciliation report. "
//												+ "Please review the report and contact the Greensheets application maintenance team if necessary."
//												+ "\n\r"
//												+ "Thank you.";
	public static final String EMAIL_SENDER = "report-recipients.sender";
	public static final String EMAIL_SUBJECT = "report-recipients.subject";
	
	public static final String SMTP_HOST = "mailfwd.nih.gov";
	public static final int SMTP_PORT = 25;
	
	// Following are the constants used to denote the type of the report.
	public static final String REPORT_TYPE_FULL = "FULL"; 
	public static final String REPORT_TYPE_INCREMENTAL = "INCREMENTAL";	
	
	// Following are the command line switches passed in to the application (can automate if needed).
	public static final String CMD_LINE_OPTION_REPORT_TYPE = "-reporttype";
	public static final String CMD_LINE_OPTION_REPORT_FROM = "-reportfrom";

	// Following are the comments used in the report.
	public static final String RPT_COMMENT_DB_PREFIX = "DB prefix mismatch";	//DB prefix is different than in the properties file.
	public static final String RPT_ZERO_LENGTH_FILE = "File Empty";	// The file on the disk was found to be 0KB in length.
	
	// Misc. attributes
	public static final String FILE_PATH_SEPARATOR = "/";
	
	// Method return codes
	public static final int SUCCESS = 1;
	public static final int FAILURE = -1;
}