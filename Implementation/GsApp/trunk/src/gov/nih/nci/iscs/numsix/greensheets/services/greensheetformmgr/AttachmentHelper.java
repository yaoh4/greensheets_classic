/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.GsGrant;
import gov.nih.nci.iscs.numsix.greensheets.utils.AppConfigProperties;
import gov.nih.nci.iscs.numsix.greensheets.utils.DbUtils;
import gov.nih.nci.iscs.numsix.greensheets.utils.GreensheetsKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Helper class for working with question file attachments
 * 
 * 
 * @author kpuscas, Number Six Software
 */
class AttachmentHelper {

	private static final Logger logger = Logger
			.getLogger(AttachmentHelper.class);
	ArrayList errorList = new ArrayList();
	/**
	 * Constructor for AttachmentHelper.
	 */
	AttachmentHelper() {
	}
	/**
	 * Method errorsExist.
	 * @param -  none
	 * returns true if errors were generated while creating or deleting attachments.
	 */
	
	public boolean errorsExist() {
		//return (errorList.size()==0)?true:false; had used this to replicate errors.
		return (errorList.size()==0)?false:true;
	}
	/**
	 * Method errorsText.
	 * @param -  none
	 * Method returns the errorText so that the name of the files that had problems are listed in the Error.jsp.
	 */
	
	public String errorsText() {
		
		StringBuffer serrorText = new StringBuffer("<OL> Following attachment files had errors : ");
		logger.debug("Iterating the errorList ArrayList to get the Errors....");
		Iterator iter = errorList.iterator();
	
		while (iter.hasNext()){
			serrorText.append("<LI> " + (String)iter.next() +"</LI>");
			
		}
		serrorText.append("</OL>");
		//logger.debug(serrorText.toString());
		logger.debug("Finished iterating the errorList ArrayList and got the errors");
		return serrorText.toString();
	}
	/**
	 * Method saveAttachments.
	 * 
	 * @param qrd
	 * @param grant
	 * @param conn
	 * @throws SQLException
	 */
	// Completeley Redone by Anand. streamlined the process and modularized the code.
	void saveAttachments(QuestionResponseData qrd, GsGrant grant,
			GreensheetForm form, Connection conn)
			throws GreensheetBaseException {
		logger.debug("Starting file attachments for question id " + qrd.getResponseDefId());
		logger.debug("The length of errorList is " + errorList.size());

		// Get the file path
		Properties p = (Properties) AppConfigProperties.getInstance()
		.getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
		String repositoryPath = p.getProperty("attachemts.repository.path");
		String rootDir = repositoryPath + File.separator
		+ grant.getFullGrantNumber() + File.separator
		+ form.getGroupTypeAsString() + File.separator
		+ qrd.getQuestionDefId();
		
		// Lets create separate array lists for creation, deletiong and existing files.
		
		ArrayList qaCreationList = new ArrayList();
		ArrayList qaDeletionList = new ArrayList();
		ArrayList qaExistingList = new ArrayList();
		
		Set entries = qrd.getQuestionAttachments().entrySet();
		if (qrd.getQuestionAttachments() != null) {
			Iterator iter = entries.iterator();
			while (iter.hasNext()) {
				Map.Entry me = (Map.Entry) iter.next();
				QuestionAttachment qa = (QuestionAttachment) me.getValue();
				if (qa.isToBeCreated()) qaCreationList.add(qa);
				if (qa.isToBeDeleted()) qaDeletionList.add(qa);
				if (qa.isExisting()) qaExistingList.add(qa);
			}
			//logger.debug("Map Size =" + entries.size());
			logger.debug("Attachments already existing = "+ qaExistingList.size());
			logger.debug("Attachments to be created = "+ qaCreationList.size());
			logger.debug("Attachments to be deleted = "+ qaDeletionList.size());
			
			logger.debug("Map Size = " + entries.size() + " and total ArrayList size = " + (qaCreationList.size()+ qaDeletionList.size()+ qaExistingList.size()));
		}
		
		//logger.debug("No of Files sent for Deletion = "+ qaDeletionList.size());
		boolean filesDeleted = false;
		if (qaDeletionList.size() > 0 ){
			logger.debug("Started Deleting Attachments = " + qrd.getResponseDefId());	
			filesDeleted = deleteAttachments(qaDeletionList, rootDir, conn);
			//if (!filesDeleted ) {throw new GreensheetBaseException("error.deletingAttachment");}
			logger.debug("Finished Deleting Attachments = " + qrd.getResponseDefId());	
		}
		
		
		
		//logger.debug("No of Files sent for Creation = "+ qaCreationList.size());
		boolean filesCreated = false ;
		if (qaCreationList.size() > 0 ){
			logger.debug("Started Creating Attachments = " + qrd.getResponseDefId());
			filesCreated = createAttachments(qaCreationList, form, qrd, rootDir, conn);
			//if (!filesCreated ){throw new GreensheetBaseException("error.creatingAttachment");}
			logger.debug("Finished Creating Attachments = " + qrd.getResponseDefId());
		}
		// Begin for deleting of form_questions_answers_t row
		// If the files were deleted and the total no of attachments passed 
		// is equal to the no of files deleted then delete the parent record.
		boolean parentqDeleteFlag = true;
		try {
			boolean questionEntryDeleteFlag = (entries.size() == qaDeletionList.size());
			
			logger.debug("Total file attachments on this Question ( " + entries.size() + " ) "+(questionEntryDeleteFlag?"=":"<>") + " Total Files sent for Deletion ( " + qaDeletionList.size() + " )");

			if (filesDeleted && questionEntryDeleteFlag ) {
				logger.debug("Started deleting parent Question row from F_Q_T table for Response ID = " + qrd.getResponseDefId() + " and FQA_ID = " + qrd.getId() );	
				deleteQuestionEntry(conn,qrd);
				logger.debug("Finished deleting parent Question row from F_Q_T table for Response ID = " + qrd.getResponseDefId() + " and FQA_ID = " + qrd.getId() );	
			}
		}  catch (Exception e){
			parentqDeleteFlag = false;
			//throw new GreensheetBaseException("error.deletequestion",e);
		}
		// End for deleting of form_questions_answers_t row
		logger.debug("Finished file attachments for question id " + qrd.getResponseDefId());
		if (qaDeletionList.size() == 0 ) {filesDeleted = true;}
		if (qaCreationList.size() == 0 ) {filesCreated = true;}
		
		if (!filesCreated) {
			throw new GreensheetBaseException("error.creatingAttachment"); 
		} 
		if (!filesDeleted) {
			throw new GreensheetBaseException("error.deletingAttachment"); 
		}
		if (!parentqDeleteFlag) {
			throw new GreensheetBaseException("error.deletequestion");
		}
	}


	private void makeNewQuestionEntry(QuestionResponseData qrd,
			GreensheetForm form, Connection conn) throws GreensheetBaseException {
		PreparedStatement pstmt = null;
		try {
			String fqaInsert = "insert into form_question_answers_t "
					+ "(id, frm_id, extrnl_question_id, extrnl_resp_def_id)"
					+ " values(?,?,?,?)";
			
			logger.debug("SQL used -> Insert into form_question_answers_t ( id, frm_id, extrnl_question_id, extrnl_resp_def_id )  values ( "  
					+ qrd.getId() + "," + form.getFormId() + "," + qrd.getQuestionDefId() + "," + qrd.getResponseDefId() + ")" );
			//logger.debug("Inserting into FQA " + qrd.getId());
			
			
			pstmt = conn.prepareStatement(fqaInsert);
			pstmt.setInt(1, qrd.getId());
			pstmt.setInt(2, form.getFormId());
			pstmt.setString(3, qrd.getQuestionDefId());
			pstmt.setString(4, qrd.getResponseDefId());

			pstmt.execute();
			//pstmt.close(); why are we closing the pstmnt twice. Since we are doing it in finally loop commented this out.
		} catch (SQLException se) {
			logger.error("SQLException while inserting row into form_questions_answers_t.",se);
			throw new GreensheetBaseException("SQLException while inserting row into form_questions_answers_t.",se);
		} catch (Exception e){
			logger.error("Exception while inserting row into form_questions_answers_t.",e);
			throw new GreensheetBaseException("Exception while inserting row into form_questions_answers_t.",e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e ) {
				logger.error("Exception while closing pstmt after inserting row into form_questions_answers_t");
				throw new GreensheetBaseException("SQLException while closing pstmt after inserting row into form_questions_answers_t",e);
			}
		}
	}

	void getQuestionAttachmentData(QuestionAttachment qa)
			throws GreensheetBaseException {

		String fullpath = qa.getFilePath();
		File file = new File(fullpath);

		byte[] bytes;
		try {

			InputStream is = new FileInputStream(file);

			// Get the size of the file
			long length = file.length();

			// You cannot create an array using a long type.
			// It needs to be an int type.
			// Before converting to an int type, check
			// to ensure that file is not larger than Integer.MAX_VALUE.
			if (length > Integer.MAX_VALUE) {
				throw new IOException("File too large :: " + file.getName());
			}

			// Create the byte array to hold the data
			bytes = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < bytes.length) {
				throw new IOException("Could not completely read file "
						+ file.getName());
			}

			// Close the input stream and return bytes
			is.close();
		} catch (FileNotFoundException fne) {
			logger.error("FileNotFoundException while getting Attachments",fne);
			throw new GreensheetBaseException("FileNotFoundException while getting Attachments",fne);
		} catch (IOException ioe) {
			logger.error("IOException while getting Attachments",ioe);
			throw new GreensheetBaseException("IOException while getting Attachments",ioe);
		} catch (Exception e ) {
			logger.error("IOException while getting Attachments",e);
			throw new GreensheetBaseException("Exception while getting Attachments",e);
		}
		qa.setDocData(bytes);
	}
	private boolean deleteAttachments(ArrayList qaDelete, String rootDir, Connection conn) throws GreensheetBaseException {
		
		boolean returnFlag = true; // used to check whether there was an error while deleting attachment. 
		//false = error and true = noerror.
		
		PreparedStatement pstmt = null;
		String sqlAction = null;
		int countFiles = 1;
		Iterator iter = qaDelete.iterator();
	    logger.debug("Begining While loop in deleteAttachments method   ....");
	    boolean filesDeletedFlag = true;
		while (iter.hasNext()) {
			QuestionAttachment qa = (QuestionAttachment) iter.next();
			logger.debug("Starting deleting File #" + countFiles + "File name is -> " + qa.getFilename());
			File f = null;
			try {
				
				f = new File(rootDir + File.separator + qa.getFilename());
				// First, delete the file then the record.
				f.delete();
	
				conn.setAutoCommit(false);
				sqlAction = "delete from form_answer_attachments_t where id=?";
				pstmt = conn.prepareStatement(sqlAction);
				pstmt.setInt(1, qa.getDbId());
				logger.debug("Sql used -> " + "delete from form_answer_attachments_t where id = " + qa.getDbId());
				
				pstmt.execute();
				pstmt.close();
				conn.commit();

			} catch (SQLException se) {
				filesDeletedFlag = false; // File was not deleted.
				logger.debug("SqlException when Deleting a file attachment record in the DB.");
			} catch (Exception se) {
				filesDeletedFlag = false; // File was not deleted.
				logger.debug("Exception when Deleting a file attachment record in the DB.");
			} finally {
				try {
					if (!filesDeletedFlag) {
						errorList.add(f.getName() + " was not deleted.");
						conn.rollback();
					}
					if (pstmt != null) pstmt.close();
					if (f != null) f = null;
				} catch (SQLException se) {
					filesDeletedFlag = false;
					logger.debug("SQLEXCEPTION while closing the statement or rolling back transaction in Delete loop of Question Attachments.");
					errorList.add("SQLException while rolling back failed deletion of " + f.getName());
				} catch (Exception e ) {
					filesDeletedFlag = false;
					logger.debug("EXCEPTION while closing the statement or rolling back transaction in Delete loop of Question Attachments.");
					errorList.add("Exception while rolling back failed deletion of " + f.getName());
				}
			}
			returnFlag = (returnFlag && filesDeletedFlag);
			logger.debug("Finished Deleting File #" + (countFiles++) + "File name is -> " + qa.getFilename());	
		}
		logger.debug("Finished while loop in deleteAttachments method.");	
	
	return returnFlag;
	
	}
	private boolean createAttachments(ArrayList qaCreate, GreensheetForm form, QuestionResponseData qrd, String rootDir, Connection conn) throws GreensheetBaseException {
		
		boolean returnFlag = true;
		
		boolean newQuestionEntryFlag = true;
		
		try {
			// Create the folder structure in the file system.
			File dir = new File(rootDir);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			// If this is a new question, make an entry in the Database in the
			// Form Question Answers table.
			if (qrd.getId() == 0) {
				int newId = DbUtils.getNewRowId(conn, "fqa_seq.nextval");
				qrd.setId(newId);
				logger.debug("Started entering a new question row from F_Q_T table for Response ID = " + qrd.getResponseDefId() + " and FQA_ID = " + qrd.getId() );
				this.makeNewQuestionEntry(qrd, form, conn);
				logger.debug("Finished entering a new question row from F_Q_T table for Response ID = " + qrd.getResponseDefId() + " and FQA_ID = " + qrd.getId() );
			}
		} catch (Exception e) {
			newQuestionEntryFlag = false;
			// no Point in going ahead as the Question row is not added in to the FORM_QUESTION_ANSWERS_T table.
			logger.debug("EXCEPTION while Inserting row into Form_Questions_Answers_T Table.");
			//throw new GreensheetBaseException("error.createquestion", e);
		} finally {
			try {
				if (!newQuestionEntryFlag) {
					errorList.add("The Attachments were not created as there was an error while creating a record of " + qrd.getQuestionDefId()+" in form_question_answers_t.");
					conn.rollback();
				} 
			}
			catch (Exception e ) {
				newQuestionEntryFlag = false;
				errorList.add("The Attachments were not created as there was an error while creating a record of " + qrd.getQuestionDefId()+" in form_question_answers_t.");
				logger.debug("EXCEPTION while rolling back transaction due to error in Creating New Question Entry");
				//throw new GreensheetBaseException("error.createquestion", e);
			}
		}

		
		PreparedStatement pstmt = null;
		
		if (newQuestionEntryFlag) {
			logger.debug("No of Files to be Created = " + qaCreate.size());
			boolean filesCreatedFlag = true;
			int countFiles = 1;
			Iterator iter = qaCreate.iterator();
			logger.debug("Begining While loop in createAttachments method   ....");
			while (iter.hasNext()) {
				QuestionAttachment qa = (QuestionAttachment) iter.next();
				logger.debug("Starting creating File #" + countFiles +  "File name is -> " + qa.getFilename());
				// Create the file first. Then create the record.
				File newFile = null;
				FileOutputStream fs = null;
				String sqlAction = null;
				String sqlDebug = null;

				try {
					newFile = new File(rootDir + File.separator
							+ qa.getFilename()); // If db error, then delete the file maybe.
		
					fs = new FileOutputStream(newFile);
					fs.write(qa.getDocData());
					fs.close();
					fs = null;
					qa.setFilePath(rootDir + File.separator
							+ qa.getFilename());
		
					
					conn.setAutoCommit(false);
					sqlAction = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?)";
					pstmt = conn.prepareStatement(sqlAction);
					pstmt.setInt(1, qrd.getId());
					pstmt.setString(2, qa.getFilename());
					pstmt.setString(3, qa.getFilePath());
		
					sqlDebug = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,";
					sqlDebug += qrd.getId() + " , " + qa.getFilename() + " , " + qa.getFilePath() + ")";
		
					logger.debug("Sql used -> " + sqlDebug );
					
					if (countFiles == 3 ) { newFile.delete() ; }
					
					if (newFile.exists()) {
						pstmt.execute();
						pstmt.close();
						conn.commit();
					} else {
						pstmt.close();
						throw new FileNotFoundException("error.creatingAttachment");
					}
				} catch (SQLException se) {
					filesCreatedFlag = false;
					logger.error("SqlException when creating a new file attachment record in the DB.",se);
				} catch (FileNotFoundException fe) {
					filesCreatedFlag = false;	
					logger.error("FileNotFoundException when creating a new file on the file system.",fe);
				} catch (IOException ie) {
					filesCreatedFlag = false;
					logger.error("IOException while Creating a new file attachment.",ie);
				} finally {
					try {
						if (!filesCreatedFlag) {
							errorList.add(newFile.getName() + " was not uploaded.");
							conn.rollback();
						}
						if (pstmt != null) pstmt.close();
						
						if (newFile != null) newFile = null;
						
						if (fs != null) {
							fs.close();
							fs = null;
						}
					} catch ( Exception e ) {
						filesCreatedFlag = false;
						logger.error("EXCEPTION while closing the statement or rolling back transaction in create loop of Question Attachments.",e);
						errorList.add("Exception while rolling back failed creation of " + newFile.getName());
					}
				}
				returnFlag = (returnFlag && filesCreatedFlag);
				logger.debug("Finished Creating File #" + (countFiles++) + "File name is -> " + qa.getFilename());
			}
			logger.debug("Finished while loop in createAttachments method.");
		}
		
		returnFlag = (returnFlag && newQuestionEntryFlag);
		return returnFlag;
	}
	
	private void deleteQuestionEntry(Connection conn, QuestionResponseData qrd) throws GreensheetBaseException {
		logger.debug("Starting delete of parent QuestionEntry");
		PreparedStatement pstmt = null;
		try {	
				conn.setAutoCommit(false);
				String sqlDelete = "delete from form_question_answers_t where id =?";
				pstmt = conn.prepareStatement(sqlDelete);
				pstmt.setInt(1, qrd.getId());
	
				logger.debug ("Sql used -> " + sqlDelete + " " + qrd.getId());
				pstmt.execute();
				pstmt.close();
				conn.commit();
			
		} catch (SQLException se) {
			logger.error("SQL EXCEPTION while deleting from form_questions_answers_t.",se);
			errorList.add("Could not delete the rows from form_question_answers_t to complete removal of attachments. Id = " + qrd.getQuestionDefId());
			throw new GreensheetBaseException("error.deletequestion", se);
		} catch (Exception e) {
			logger.error("EXCEPTION while deleting from form_questions_answers_t.",e);
			errorList.add("Could not delete the rows from form_question_answers_t to complete removal of attachments. Id = " + qrd.getQuestionDefId() );
			throw new GreensheetBaseException("error.deletequestion", e);
		}
		logger.debug("Finished delete of parent QuestionEntry");
	}

}
