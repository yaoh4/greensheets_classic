package gov.nih.nci.iscs.numsix.greensheets.application;

import gov.nih.nci.iscs.numsix.greensheets.services.GreensheetMgrFactory;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GreensheetUserMgr;
import gov.nih.nci.iscs.numsix.greensheets.services.greensheetusermgr.GsUser;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbConnectionHelper;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 * 
 * 
 * @author kpuscas, Number Six Software
 */
public class ErrorTestAction extends Action {
	public static final Logger logger = Logger.getLogger(ErrorTestAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm aForm,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		boolean exceptionRaised = false;
		String errorMsg = " ";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int fiscalYear = 0;
		GsUser user = null;
		
		// Get the LDAP user ID from the properties file.
		Properties appProperties = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
		String statusTestLDAPUserID = appProperties.getProperty("statusTestUser");
		String appStatusTestsDir = appProperties.getProperty("appStatusTestsDir");
		String checkDBStatus = appProperties.getProperty("checkDBStatus");
		String checkFilePermissions = appProperties.getProperty("checkFilePermissions");
		boolean dBStatusCheckRequired = true;
		boolean filePermissionsCheckRequired = true;
		boolean LDAPStatusCheckPassed = false;
		boolean DBStatusCheckPassed = false;
		boolean filePermissionsCheckPassed = false;
		String httpResponseMsg = "";
		
		if (statusTestLDAPUserID == null || statusTestLDAPUserID.trim().equals("")) {
			exceptionRaised = true;
			errorMsg = "No application status test userID found in the properties file.";
		}
		
		if (!exceptionRaised) {
			logger.debug("The application status test userID found in the properties file = " + statusTestLDAPUserID);
			GreensheetUserMgr userMgr = GreensheetMgrFactory.createGreensheetUserMgr(GreensheetMgrFactory.PROD);
			user = userMgr.findUserByUserName(statusTestLDAPUserID.trim());
			if (user == null) {
				exceptionRaised = true;
				errorMsg = "Error in finding Test User in LDAP";
			} else {
				logger.debug("The LDAP user " + user.getDisplayUserName() + " with nciOracleID " + user.getOracleId() + " is found.");
				LDAPStatusCheckPassed = true;
				
				if (checkDBStatus != null && checkDBStatus.trim().toUpperCase().startsWith("N")) {
					dBStatusCheckRequired = false;
				}
				if (dBStatusCheckRequired) {// DB Status Check block begins here.
					try {
						logger.debug("Trying to get the JDBC Connection ...");
						conn = DbConnectionHelper.getInstance().getConnection(user.getOracleId());
						logger.debug("Got the Database Connection.");
						stmt = conn.createStatement();
						stmt.execute("select F_GET_FY() from dual");
						rs = stmt.getResultSet();
						logger.debug("About to call the Oracle Stored function F_GET_FY() ...");
						while (rs.next()) {
							fiscalYear = rs.getInt(1);
						}
						logger.debug("Executed the Oracle Stored function F_GET_FY()");
						if (fiscalYear > 0) {	//if the value is SQL NULL, the value returned is 0
							logger.debug("Per DB, the current fiscal year is " + fiscalYear);
							DBStatusCheckPassed = true;
						}
					} catch (SQLException e) {
						logger.debug("Error retreiving the current fiscal year from the Database.");
						errorMsg = "Error retreiving the current fiscal year from the Database.";
						exceptionRaised = true;
					} finally {
						try {
							if (rs != null) {
								rs.close();
							}
						} catch(SQLException e) {
							logger.debug("Could not close the JDBC ResultSet object.");
							errorMsg = "Could not close the JDBC ResultSet object.";
							exceptionRaised = true;				
						}
	
						try {
							if (stmt != null) {
								stmt.close();
							}
						} catch(SQLException e) {
							logger.debug("Could not close the JDBC Statement object.");
							errorMsg = "Could not close the JDBC Statement object.";
							exceptionRaised = true;				
						}
						
						try {
							if (conn != null) {
								conn.close();
							}
						} catch(SQLException e) {
							logger.debug("Could not close the JDBC Connection object.");
							errorMsg = "Could not close the JDBC Connection object.";
							exceptionRaised = true;				
						}
					}
				} // DB Status Check block ends here.
				
				if (checkFilePermissions != null && checkFilePermissions.trim().toUpperCase().startsWith("N")) {
					filePermissionsCheckRequired = false;
				}
				if (filePermissionsCheckRequired) {// File Permissions Check block begins here.
					if (appStatusTestsDir == null || appStatusTestsDir.trim().equals("")) {
						exceptionRaised = true;
						errorMsg = "The name of the test directory where the Greensheets application would create the users' file attachments is not found in the properties file.";			
					} else {
						String repositoryPath = appProperties.getProperty("attachemts.repository.path");
						String testDir = null;
						testDir = repositoryPath + File.separator + appStatusTestsDir;
						logger.debug("Tests to see if " + testDir + " exists already or needs to be created...");
						File dir = new File(testDir);
						
						try {
							if (!dir.isDirectory()) {
								dir.mkdirs();
							}
							
							if (dir.exists()) {
								String newFileName = null;
								String formattedDateTime = null;
								Date currentDate = new Date();
								Format formatter;

								formatter = new SimpleDateFormat("yyyy_MMM_dd_hh_mm_ss");
								formattedDateTime = formatter.format(currentDate);
								newFileName = "GSFileSysCheck_" + formattedDateTime + ".txt";
								logger.debug("Coined new file name for the Greensheets app's file system check: " + newFileName);
							    
								try {
							        BufferedWriter out = new BufferedWriter(new FileWriter(dir + File.separator + newFileName));
							        out.write("Checks to see if the Greensheets application has the write permission to write to the server machine's disk.");
							        out.close();
							        logger.debug("The Greensheets application has enough permission to write to the server machine's disk.");
							    } catch (IOException e) {
									exceptionRaised = true;
									errorMsg = "The Greensheets application does not have 'write' permission to create directories or files in the server machine.";							    	
							    }
							    
							    File newFileCreated = new File(dir + File.separator + newFileName);
								if (newFileCreated.exists()) {
									logger.debug("Checking to see if the Greensheets application has enough file permission to delete from the server machine's disk...");
									//The following test is not reliable.
//									boolean dirDeleted = newFileCreated.delete();
//									if (dirDeleted) {
//										logger.debug("Deleted the directory " + newFileCreated.getAbsolutePath() + " ... Done");
//									} else {
//										logger.error("Could not delete " + newFileCreated.getAbsolutePath());
//									}
									newFileCreated.deleteOnExit();	// This works reliably on Windows.
									filePermissionsCheckPassed = true;
								} else {
									exceptionRaised = true;
									errorMsg = "The Greensheets application does not create file in the server machine.";									
								}
							} else {
								exceptionRaised = true;
								errorMsg = "The Greensheets application does not have enough file permissions to create or delete in the server machine.";							
							}
						} catch(SecurityException secExcep) {
							exceptionRaised = true;
							errorMsg = "The Greensheets application does not have file permissions to create or delete in the server machine.";							
						}
					}
				}
			}
		}
	
		if (exceptionRaised) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
		} else {
			if (LDAPStatusCheckPassed) httpResponseMsg += "\nPassed the LDAP connection test";
			if (DBStatusCheckPassed) httpResponseMsg += "\nPassed the I2E Database connection test";
			if (filePermissionsCheckPassed) httpResponseMsg += "\nPassed File permissions tests";
			resp.sendError(HttpServletResponse.SC_OK, httpResponseMsg);				
		}
		return null;
	}
}