package gov.nih.nci.iscs.numsix.apps.docchecker;

import gov.nih.nci.iscs.numsix.apps.docchecker.exception.DocCheckerException;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FilesVerifier {
	public Logger log = Logger.getLogger("gov.nih.nci.iscs.numsix.apps.docchecker");
	public String docRootOnServer = "";
	public String docRootPrefixInDb = "";
	private Map filesOnDiskMap = null;
	private Map filesOnDbMap = null;
	private DocCheckerResults docCheckerResults = null;
	private DocCheckerConfig docCheckerConfig = null;
	
	public DocCheckerResults verifyFiles(Connection conn) throws DocCheckerException {
		log.debug("Entering FilesVerifier.verifyFiles()...");
		Map docCheckerResultsMap;
		long totalFileCount;
		long filesToBeReconciled;
		long filesToBeLocated;
		long filesRequiringNoAction;
		long emptyFilesRequiringNoAction;
		long emptyFilesToBeReconciled;
		int docRootPrefixInDbLen;
		long fileLength = 0;
		File docRootDir;
		String filesysMapKeyCurrent;
		String dbMapKeyCurrent;
		Set filesOnDiskMapKeys;
		Set filesOnDbMapKeys;
		Iterator filesOnDiskMapKeysItr;
		Iterator dbKeysItr;
		Iterator filesOnDiskItr;
		DocCheckerFileAttrib fileAttribCurrent;
		Collection fileCollection;
		String fileNameInDb;
		String dbPrefix;
		String reportType = null;
		Date reportRunFrom = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		docCheckerConfig = AppManager.getInstance().getDocCheckerConfig();
		
		this.docRootOnServer = docCheckerConfig.getDiskLocation();
		this.docRootOnServer = this.appendSuffix(this.docRootOnServer, IConstants.FILE_PATH_SEPARATOR);
		this.docRootPrefixInDb = docCheckerConfig.getDbPrefix();
		this.docRootPrefixInDb = this.appendSuffix(this.docRootPrefixInDb, IConstants.FILE_PATH_SEPARATOR);	// This step is required because Prod settinng does not end with the forward slash.
		docRootPrefixInDbLen = docRootPrefixInDb.length();

		// Get the report type requested.
		reportType = docCheckerConfig.getReportType();
		if (reportType == null || reportType.trim().equalsIgnoreCase("")) {
			log.error("Could not decide on the Report type.");
			throw new DocCheckerException("Could not decide on the Report type. Check the Application config file or enter a valid command line switch for the report type.");
		}
		reportRunFrom = docCheckerConfig.getReportLastRun();
		if (reportRunFrom == null) {
			log.error("Could not decide on the Report Date.");
			throw new DocCheckerException("Could not decide on the Report Date. Check the Application config file or enter a valid command line option for the date the report needs to run from.");
		}
		
		// Initialize the maps that holds the names of the files.
		this.filesOnDiskMap = new HashMap();
		this.filesOnDbMap = new HashMap();
		this.docCheckerResults = new DocCheckerResults();
		
		docCheckerResultsMap = new HashMap();
		totalFileCount = 0;
		filesToBeReconciled = 0;
		filesToBeLocated = 0;
		filesRequiringNoAction = 0;
		emptyFilesToBeReconciled = 0;
		emptyFilesRequiringNoAction = 0;
		
		// Collect the list of the files on the server's disk into a map.
		log.debug("Collecting the list of files on the disk.");
		docRootDir = new File(docRootOnServer);
		fileCollection = FileUtils.listFiles(docRootDir, null, true);
		filesOnDiskItr = fileCollection.iterator();

		while (filesOnDiskItr.hasNext()) {
			File file = (File) filesOnDiskItr.next();
			long fileLastModified = 0L;
			Date fileLastModifiedDt = null;
			// Do not include the (disk) file whose creation/modification datetime stamp is before the Date the Report was last run for the INCREMENTAL report type. 
			if (reportType.equalsIgnoreCase(IConstants.REPORT_TYPE_INCREMENTAL)) {
				fileLastModified = file.lastModified();
				if (fileLastModified != 0L) {
					fileLastModifiedDt = new Date(fileLastModified);
					if (fileLastModifiedDt.compareTo(reportRunFrom) < 0) {
						filesOnDiskItr.remove();
						continue;
					}
				}
			}
			fileLength = file.length();

			fileAttribCurrent = new DocCheckerFileAttrib();
			fileAttribCurrent.setFileName(file.getPath());
			fileAttribCurrent.setFoundOnDisk(true);
			fileAttribCurrent.setRecordedInDb(false);
			fileAttribCurrent.setFileLength(fileLength);
			fileAttribCurrent.setActionRequired(" ");
			fileAttribCurrent.setActionTaken(" ");
			fileAttribCurrent.setComments(" ");

			filesOnDiskMap.put(file.getPath(), fileAttribCurrent);
		}
		
		// Collect the names of the files recorded in the DB into a map.
		log.debug("Collecting the list of files from the Database.");
		try {
			if (reportType.equalsIgnoreCase(IConstants.REPORT_TYPE_FULL)) {
				stmt = conn.prepareStatement("select file_location from FORM_ANSWER_ATTACHMENTS_T");
			} else {
				stmt = conn.prepareStatement("select file_location from FORM_ANSWER_ATTACHMENTS_T where create_date >= ? OR last_change_date >= ?");
				java.sql.Date sqlDate = new java.sql.Date(reportRunFrom.getTime());				
				stmt.setDate(1, sqlDate);
				stmt.setDate(2, sqlDate);
			}
			log.debug("Executing the Database query for Report Type:" + reportType);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				fileNameInDb = rs.getString(1);
				if (!rs.wasNull()) {
					// Replace the docRootPrefixInDb with the docRootOnServer.
					dbPrefix = fileNameInDb.substring(0, docRootPrefixInDbLen);
					if (!docRootPrefixInDb.equalsIgnoreCase(dbPrefix)) {
						fileAttribCurrent = new DocCheckerFileAttrib();
						fileAttribCurrent.setFileName(fileNameInDb.toString());
						fileAttribCurrent.setRecordedInDb(true);
						fileAttribCurrent.setFoundOnDisk(false);
						fileAttribCurrent.setComments(IConstants.RPT_COMMENT_DB_PREFIX);
						File file = new File(fileNameInDb);
						filesOnDbMap.put(file.getPath(), fileAttribCurrent);		
						continue;
					} else {
						fileNameInDb = docRootOnServer.concat(fileNameInDb.substring(docRootPrefixInDbLen));
					}
					File file = new File(fileNameInDb);
					filesOnDbMap.put(file.getPath(), null);			
				}
			}
			// Close the DB Connection
			rs.close();
			stmt.close();
			log.debug("Database connection closed.");
		} catch (SQLException exp) {
			log.error(exp.getMessage() + ". Error retrieving from DB, processing results or closing the connection.");
			throw new DocCheckerException(this.getClass().getName() + ": " + exp.getClass().getName() + " occurred while reading from Database." + exp.getMessage());
		} catch (Exception exp) {
			log.error(exp.getMessage() + ". Error retrieving while processing DB results.");
			throw new DocCheckerException(this.getClass().getName() + ": " + exp.getClass().getName() + " occurred while collecting the names of the files recorded in the DB." + exp.getMessage());
		}
		
		// Following is the segment to load the filesOnDbMap (when not connecting to the DB).
//		String filesTemp[] = {
//				"/data/test/greensheets/subdirb/PaylistIndividualOraImpl-work.java",
//				"/data/test/greensheets/httpd_v2.2.3.conf",
//				"/data/test/greensheets/subdira/dirinsidesubdira/subinsidesubinsidesubdira/unknownfile2.pdf",
//				"/data/test/greensheets/subdira/dirinsidesubdira/subinsidesubinsidesubdira/httpd_v1.3.27.conf",
//				"/data/test/greensheets/jvm.config",
//				"/data/test/greensheets/duplicated.config",
//				"/data/test/greensheets/duplicated.config",
//				"/data/test/greensheets/unknownfile.doc",
//				"/data/test/greensheets/subdira/dirinsidesubdira/project.properties",
//				"/data/gs/files/path/subdirc/project.xml",
//				"/data/test/greensheets/subdirb/subinsidesubdirb/maven.xml" };
//		
//		for (int index = 0; index < filesTemp.length; index++) {
//			fileNameInDb = filesTemp[index];
//			dbPrefix = fileNameInDb.substring(0, docRootPrefixInDbLen);
//			if (!docRootPrefixInDb.equalsIgnoreCase(dbPrefix)) {
//				fileAttribCurrent = new DocCheckerFileAttrib();
//				fileAttribCurrent.setFileName(fileNameInDb.toString());
//				fileAttribCurrent.setRecordedInDb(true);
//				fileAttribCurrent.setFoundOnDisk(false);
//				fileAttribCurrent.setComments(IConstants.RPT_COMMENT_DB_PREFIX);
//				File file = new File(fileNameInDb);
//				filesOnDbMap.put(file.getPath(), fileAttribCurrent);
//				continue;
//			} else {
//				fileNameInDb = docRootOnServer.concat(fileNameInDb.substring(docRootPrefixInDbLen));
//			}
//			File file = new File(fileNameInDb);
//			filesOnDbMap.put(file.getPath(), null);
//		}
		// Above is the segment to load the filesOnDbMap (when not connecting to the DB).
		log.debug("Comparing the list of files on the disk to the list of attachments recorded in the Database.");
		filesOnDiskMapKeys = filesOnDiskMap.keySet();
		filesOnDiskMapKeysItr = filesOnDiskMapKeys.iterator();
		filesOnDbMapKeys = filesOnDbMap.keySet();
		while (filesOnDiskMapKeysItr.hasNext()) {
			filesysMapKeyCurrent = (String) filesOnDiskMapKeysItr.next();
			
			fileAttribCurrent = (DocCheckerFileAttrib) filesOnDiskMap.get(filesysMapKeyCurrent);
			if (filesOnDbMap.containsKey(filesysMapKeyCurrent)) {
				fileAttribCurrent.setRecordedInDb(true);
				fileAttribCurrent.setActionRequired(IConstants.ACTION_NONE);
				if (fileAttribCurrent.getFileLength() == 0) {
					fileAttribCurrent.setComments(IConstants.RPT_ZERO_LENGTH_FILE);
					emptyFilesRequiringNoAction++;
				}
				filesRequiringNoAction++;
			} else {
				fileAttribCurrent.setRecordedInDb(false);
				fileAttribCurrent.setActionRequired(IConstants.ACTION_RECONCILE);
				if (fileAttribCurrent.getFileLength() == 0) {
					fileAttribCurrent.setComments(IConstants.RPT_ZERO_LENGTH_FILE);
					emptyFilesToBeReconciled++;
				}				
				filesToBeReconciled++;
			}
			totalFileCount++;
			
			fileAttribCurrent.setActionTaken("");
			if ((fileAttribCurrent.getComments() == null) || (fileAttribCurrent.getComments().trim().equals(""))) {
				fileAttribCurrent.setComments("");
			}			
			docCheckerResultsMap.put(filesysMapKeyCurrent, fileAttribCurrent);
		}
		
		dbKeysItr = filesOnDbMapKeys.iterator();
		while (dbKeysItr.hasNext()) {
			dbMapKeyCurrent = (String) dbKeysItr.next();
			
			fileAttribCurrent = (DocCheckerFileAttrib) filesOnDbMap.get(dbMapKeyCurrent);
			if (fileAttribCurrent == null) {
				fileAttribCurrent = new DocCheckerFileAttrib();
				fileAttribCurrent.setFileName(dbMapKeyCurrent);
				fileAttribCurrent.setRecordedInDb(true);
			}
			
			if (!docCheckerResultsMap.containsKey(dbMapKeyCurrent)) {
				fileAttribCurrent.setFoundOnDisk(false);
				fileAttribCurrent.setActionRequired(IConstants.ACTION_LOCATE);
				fileAttribCurrent.setActionTaken("");
				if ((fileAttribCurrent.getComments() == null) || (fileAttribCurrent.getComments().trim().equals(""))) {
					fileAttribCurrent.setComments("");
				}
				docCheckerResultsMap.put(dbMapKeyCurrent, fileAttribCurrent);				
				filesToBeLocated++;
				totalFileCount++;
			}			
		}

		log.debug("Storing the various file counts...");
		
		// Put the file counts, results map into the DocCheckerResults instance variable.
		docCheckerResults.setDocCheckerResultsMap(docCheckerResultsMap);
		docCheckerResults.setTotalFileCount(totalFileCount);
		docCheckerResults.setFilesToBeReconciled(filesToBeReconciled);
		docCheckerResults.setFilesToBeLocated(filesToBeLocated);
		docCheckerResults.setFilesRequiringNoAction(filesRequiringNoAction);
		docCheckerResults.setEmptyFilesToBeReconciled(emptyFilesToBeReconciled);
		docCheckerResults.setEmptyFilesRequiringNoAction(emptyFilesRequiringNoAction);
		
		log.debug("Returning from FilesVerifier.verifyFiles().");
		return docCheckerResults;
	}
	
	// Append the suffix only if the source String does not end with the suffix.
	public String appendSuffix(String srcStr, String suffix) {
		String resultStr = null;
		if (srcStr != null && suffix != null) {
			if (srcStr.endsWith(suffix)) {
				return srcStr; 
			} else {
				resultStr = srcStr.concat(suffix);
				return resultStr;
			}
		} else {
			return null;
		}
	}
}
