/**
 * Copyright (c) 2003, Number Six Software, Inc. Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE Written for
 * National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.fwrk.GreensheetBaseException;
import gov.nih.nci.iscs.numsix.greensheets.services.FormGrantProxy;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Helper class for working with question file attachments
 * 
 * @author kpuscas, Number Six Software
 */
class AttachmentHelper {

    private static final Logger logger = Logger
            .getLogger(AttachmentHelper.class);

    /**
     * Constructor for AttachmentHelper.
     */
    AttachmentHelper() {
    }
    
    /**
     * Deletes from the database the records of attachments that are not among the attachments of 
     * the form being saved when this method is called.  With proper concurrency/record versioning
     * control, this method should not be necessary - it is a quick patch for the fact that for
     * now, users totally can be saving the same form simultaneously. It does not commit anything,
     * and should be run inside a broader transaction of saving the form, which is why it should 
     * not be obtaining its own connection, but should receive one as an argument.
     * @param dbConn - Connection within which the larger transaction is running.
     * @param formId - the DB ID of the greensheet form whose orphan attachments we need to delete 
     * @param questionsMap - questions that have attachment-type answers in the in-memory form the 
     *        user is trying to save.
     */
    void deleteOrphanAttachments(Connection dbConn, int formId, Map questionsMap) throws SQLException {
    	if (dbConn != null && !dbConn.isClosed()) {
    		
    		// Get database IDs of all attachment records currently saved in the database for this form.
    		String sqlGetRecordIds = "SELECT faa.id, faa.name, faa.file_location, faa.fqa_id, faa.create_user_id, " +
    				"to_char(faa.create_date, 'YYYY-MM-DD HH24:MI:SS') as create_date_str " +
    				"FROM form_answer_attachments_t FAA  JOIN  form_question_answers_t FQA  on faa.fqa_id = fqa.id " +
    				"WHERE fqa.frm_id = ?";
    		PreparedStatement selRecsStmt = dbConn.prepareStatement(sqlGetRecordIds);
    		selRecsStmt.setInt(1, formId);
    		ResultSet rs = selRecsStmt.executeQuery();
    		ArrayList<QuestionAttachment> existingAttachmentsForForm = new ArrayList<QuestionAttachment>();
    		Set<Integer> fileTypeRespRecIds = new HashSet<Integer>();
    		while (rs!=null && rs.next()) {
    			QuestionAttachment attchEntry = QuestionAttachment.createNewAttachment();
    			attchEntry.setDbId(rs.getInt("ID"));
    			attchEntry.setFilename(rs.getString("NAME"));
    			attchEntry.setFilePath(rs.getString("FILE_LOCATION"));
    			attchEntry.setFqaId(rs.getInt("FQA_ID"));
    			attchEntry.setCreateUserId(rs.getString("CREATE_USER_ID"));
    			attchEntry.setCreateDateAsString(rs.getString("CREATE_DATE_STR"));
    			existingAttachmentsForForm.add(attchEntry);
    			fileTypeRespRecIds.add(new Integer(rs.getInt("FQA_ID")));
    		}
    		if (rs!=null) {
    			rs.close();
    			rs = null;
    		}
    		if (selRecsStmt!=null) {
    			selRecsStmt.close();
    			selRecsStmt = null;
    		}
    		
    		// Get database IDs of all attachment records contained in the in-memory bean representing the greensheet 
    		// form the user is trying to save.
    		Map<Integer, QuestionAttachment> attsBeingSaved = new HashMap<Integer, QuestionAttachment>();
    		Set<Integer> fileTypeRespRecsBeingSaved = new HashSet<Integer>();
    		Iterator<QuestionResponseData> itr = (Iterator<QuestionResponseData>) questionsMap.values().iterator();
    		while (itr!=null && itr.hasNext()) {
    			QuestionResponseData responseData = itr.next();
    			fileTypeRespRecsBeingSaved.add(new Integer(responseData.getId()));
    			Iterator<QuestionAttachment> attsItr = 
    					(Iterator<QuestionAttachment>) responseData.getQuestionAttachments().values().iterator();
    			while (attsItr!=null && attsItr.hasNext()) {
    				QuestionAttachment att = attsItr.next();
    				int dbId = att.getDbId();
    				if (dbId!=0) {
    					attsBeingSaved.put(new Integer(dbId), att);
    				}
    			}
    		}
    		
    		// Build a list of attachment records that exist in the database but are not among the "existing" 
    		// attachments that were loaded when the user loaded the form - those should be deleted as they are orphaned.
    		ArrayList<QuestionAttachment> attRecsToDelete = new ArrayList<QuestionAttachment>();
    		for (int i=0; i < existingAttachmentsForForm.size(); i++) {
    			QuestionAttachment dbRec = existingAttachmentsForForm.get(i);
    			int dbId = dbRec.getDbId();
    			if (!attsBeingSaved.containsKey(new Integer(dbId))) {
    				attRecsToDelete.add(dbRec);
    			}
    		}
    		Set<Integer> fileTypeRespRecsToDelete = new HashSet<Integer>();
    		Iterator<Integer> itrQArecs = fileTypeRespRecIds.iterator();
    		while (itrQArecs!=null && itrQArecs.hasNext()) {
    			Integer oneId = itrQArecs.next();
    			if (!fileTypeRespRecsBeingSaved.contains(oneId)) {
    				fileTypeRespRecsToDelete.add(oneId);
    			}
    		}

    		
    		// Let's clean up a little before we move on...
    		attsBeingSaved.clear();
    		attsBeingSaved = null;
    		existingAttachmentsForForm.clear();
    		existingAttachmentsForForm = null;
    		
    		// OK, now all that's left to do is delete the actual orphan attachments:
    		String delOrphanAttchSql = "DELETE FROM form_answer_attachments_t WHERE fqa_id = ? and ID = ?";
    		PreparedStatement delOrhpanAttchStmt = dbConn.prepareStatement(delOrphanAttchSql);

    		Properties p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
    		String repositoryPath = "";
    		if (p!=null) {
    			repositoryPath = p.getProperty("attachemts.repository.path"); // TODO: this is misspelled - fix it one day?
    			if (!repositoryPath.endsWith(File.separator)) {
    				repositoryPath = repositoryPath + File.separator;
    			}
    		}
    		for (int i = 0; i < attRecsToDelete.size(); i++) {
    			QuestionAttachment recToDelete = attRecsToDelete.get(i);
    			delOrhpanAttchStmt.setInt(1, recToDelete.getFqaId());
    			delOrhpanAttchStmt.setInt(2, recToDelete.getDbId());
    			logger.info("\n - About to delete the record for the orphaned attachment (" +
    					recToDelete.getFilename() + ") \n\twith ID " + recToDelete.getDbId() +
    					" for the answer " + recToDelete.getFqaId() + " that was created by " +
    					recToDelete.getCreateUserId() + " on " + recToDelete.getCreateDateAsString());
    			delOrhpanAttchStmt.executeUpdate();

    			// delete the actual file form the filesystem
    			File f = new File(repositoryPath + recToDelete.getFilePath());
				if (f.delete()) {
					logger.info("\n - Also deleted " + recToDelete.getFilePath());
				}
				else {
    				logger.warn(" * *  Deleting from the filesystem the orphan attachment with ID " +
    						recToDelete.getDbId() + " at location " + recToDelete.getFilePath() + 
    						" was not successful.");
				}
    			logger.info("   - - done deleting.");
    		}
    		if (delOrhpanAttchStmt!=null) {
    			delOrhpanAttchStmt.close();
    			delOrhpanAttchStmt = null;
    		}
    		
    		// And finally, also delete the records from answers table whose child attachment records
    		// we just deleted:
    		String delOrphanAnswerRecSql = "DELETE FROM form_question_answers_t WHERE frm_id = ? and id = ?";
    		if (fileTypeRespRecsToDelete.size() > 0) {
    			PreparedStatement delOrphanAnswerRecStmt = dbConn.prepareStatement(delOrphanAnswerRecSql);
    			itrQArecs = fileTypeRespRecsToDelete.iterator();
    			while (itrQArecs!=null && itrQArecs.hasNext()) {
    				Integer oneId = itrQArecs.next();
    				delOrphanAnswerRecStmt.setInt(1, formId);
    				delOrphanAnswerRecStmt.setInt(2, oneId.intValue());
    				logger.info(" * * *  Also about to delete the ANSWER record with ID " + oneId + 
    						" for form " + formId + ".");
    				int rowCount = delOrphanAnswerRecStmt.executeUpdate();
    				logger.info(" * * *  ... and, " + rowCount + " answer record(s) deleted.");
    			}
    			if (delOrphanAnswerRecStmt!=null) {
    				delOrphanAnswerRecStmt.close();
    				delOrphanAnswerRecStmt = null;
    			}
    		}
    	}
    	else {
    		logger.error("  !! In the method to delete orphan attachments for form " + formId +
    				", database connection passed as a parameter is not open/usable.");
    	}
    }

    /**
     * Method saveAttachments.
     * 
     * @param qrd
     * @param grant
     * @param conn
     * @throws SQLException
     */
    // Updated by KKanchinadam.
    //	void saveAttachments(QuestionResponseData qrd, GsGrant grant,	//Abdul Latheef: Used new FormGrantProxy instead of the old GsGrant.
    void saveAttachments(QuestionResponseData qrd, FormGrantProxy grant,
            GreensheetFormProxy form, Connection conn)
            throws GreensheetBaseException {
        logger.debug("In Method -- AttachmentHelper:saveAttachments:");

        // Get the file path
        Properties p = (Properties) AppConfigProperties.getInstance()
                .getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        String repositoryPath = p.getProperty("attachemts.repository.path");

        String subDir = grant.getFullGrantNumber() + File.separator
                + form.getGroupTypeAsString() + File.separator
                + qrd.getQuestionDefId();
        String rootDir = repositoryPath + File.separator + subDir;

        PreparedStatement pstmt = null;
        boolean makeNewQuestionEntry = false;

        File f = null;

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
                this.makeNewQuestionEntry(qrd, form, conn);
                logger.debug("Entering a new question with fqa_seq = " + newId);
            }

            // Now that the parent record is taken care of... take care of the
            // file attachments.
            // Also, folder structure is present. create or delete (not a good
            // idea in this situation....!!! ) the file attachments.

            boolean fileAttachmentsPresentInDb = false; // flag to track whether
                                                        // the question response
                                                        // data will eventually
                                                        // contain any file or
                                                        // not.
            if (qrd.getQuestionAttachments() != null) {
                Set entries = qrd.getQuestionAttachments().entrySet();
                logger.debug("Number of file entries = " + entries.size());

                /*
                 * Problem: If you have two files with the same name with
                 * different status: Filename : x.doc with Status = NEW Filename :
                 * x.doc with Status = DELETED Since the order of elements in a
                 * HashMap is not guaranteed, a situation could arise where, 1.
                 * x.doc with Status = NEW is created. Thus, the existing x.doc
                 * on the file system is replaced by the new x.doc. 2. x.doc
                 * with status = DELETED is processed. This will delete the new
                 * x.doc created in the above step. Next time, when attempting
                 * to view the file, a FileNotFoundException will be thrown,
                 * since the file no longer exists on the drive.
                 * 
                 * Solution: Create an arraylist, with all DELETED items,
                 * followed by NEW Items, followed by EXISTING items. you need
                 * EXISTING items since need to determine if any file
                 * attachments exist for the Question or not.
                 */

                ArrayList qaList = new ArrayList();
                Iterator iter = entries.iterator();
                while (iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    if (qa.isToBeDeleted()) {
                        qaList.add(qa);
                    }
                }

                iter = entries.iterator();
                while (iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    if (qa.isToBeCreated()) {
                        qaList.add(qa);
                    }
                }

                iter = entries.iterator();
                while (iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    if (qa.isExisting()) {
                        qaList.add(qa);
                    }
                }

                logger.debug("Map Size =" + entries.size()
                        + " , ArrayList size = " + qaList.size());

                iter = qaList.iterator();

                // Iterate thru the entries. Delete if status is deleted, else
                // add it.
                int countFiles = 1;
                while (iter.hasNext()) {
                    logger.debug("File #" + countFiles);
                    // Map.Entry me = (Map.Entry) iter.next();
                    // QuestionAttachment qa = (QuestionAttachment)
                    // me.getValue();
                    QuestionAttachment qa = (QuestionAttachment) iter.next();

                    // get the status
                    int qaStatus = qa.getStatus();
                    logger.debug(countFiles + ".) -- Filename, Status  = "
                            + qa.getFilename() + ", " + qa.getStatusValue());

                    // If status = NEW or EXISTING, then eventually at least one
                    // file attachment will exist. So no need to delete the Form
                    // Question Answer from the database.
                    if (qa.isExisting() || qa.isToBeCreated()) {
                        fileAttachmentsPresentInDb = true;
                        logger.debug("fileAttachmentsPresentInDb = TRUE.");
                    }

                    /*
                     * Status = NEW, create the attachment in the database.
                     * Status = DELETED, delete the attachment Status =
                     * EXISTING, no modification required.
                     */

                    // Get the file
                    f = new File(rootDir + File.separator + qa.getFilename());

                    String sqlAction = null;
                    String sqlDebug = null;

                    if (qa.isToBeDeleted()) {
                        // First, delete the record, then delete the file.
                        try {
                            // conn.setAutoCommit(false);
                            sqlAction = "delete from form_answer_attachments_t where id=?";
                            logger.debug("Deleting the attachment ,sql is :");
                            logger.debug(sqlAction);
                            pstmt = conn.prepareStatement(sqlAction);
                            pstmt.setInt(1, qa.getDbId());

                            sqlDebug = "delete from form_answer_attachments_t where id="
                                    + qa.getDbId();

                            pstmt.execute();
                            pstmt.close();
                            // conn.commit();  Commented out by Anatoli Mar. 24, 2013: we need only one commit per saving the whole form!

                            // Now delete the file
                            f.delete();
                        } catch (SQLException se) {
                            fileAttachmentsPresentInDb = true; // File not
                                                               // deleted,
                                                               // hence parent
                                                               // question will
                                                               // have
                                                               // attachments
                                                               // for sure..
                            conn.rollback();

                            logger
                                    .debug("Sql Exception when Deleting a file attachment record in the DB.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException(
                                    "error.deletefile.sql", se);
                        } finally {
                            try {
                                if (pstmt != null) {
                                    pstmt.close();
                                }

                                if (f != null) {
                                    f = null;
                                }
                            } catch (SQLException se) {
                                throw new GreensheetBaseException(
                                        "error.savefile.sql", se);
                            }
                        }
                    } else if (qa.isToBeCreated()) {
                        // Create the file first. Then create the record.
                        File newFile = null;
                        FileOutputStream fs = null;
                        try {
                            // Create the file on the file system.
                            fs = new FileOutputStream(f);
                            fs.write(qa.getDocData());
                            fs.close();
                            fs = null;
                            qa.setFilePath(rootDir + File.separator
                                    + qa.getFilename());
                            qa.setSubDir(subDir + File.separator
                                    + qa.getFilename());

                            newFile = new File(rootDir + File.separator
                                    + qa.getFilename()); // If db error, then
                                                         // delete the file
                                                         // maybe.

                            conn.setAutoCommit(false);
                            sqlAction = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?)";
                            pstmt = conn.prepareStatement(sqlAction);
                            pstmt.setInt(1, qrd.getId());
                            pstmt.setString(2, qa.getFilename());
                            //  pstmt.setString(3, qa.getFilePath());
                            pstmt.setString(3, qa.getSubDir());

                            sqlDebug = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?) ---- ";
                            sqlDebug += "Values are SeqValue, " + qrd.getId()
                                    + ", " + qa.getFilename() + ", "
                                    + qa.getSubDir();

                            logger.debug("Creating a new attachment, sql is");
                            logger.debug(sqlAction);

                            pstmt.execute();
                            pstmt.close();
                            conn.commit();
                        } catch (SQLException se) {
                            conn.rollback();

                            logger
                                    .debug("Sql Exception when creating a new file attachment record in the DB.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException(
                                    "error.savefile.sql", se);
                        } catch (FileNotFoundException fe) {
                            conn.rollback();

                            logger
                                    .debug("Sql Exception when creating a new file on the file system.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException(
                                    "error.savefile.fe", fe);
                        } catch (IOException ie) {
                            conn.rollback();

                            logger
                                    .debug("Error in Creating a new file attachment. IO Exception occurred.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException(
                                    "error.savefile.io", ie);
                        } finally {
                            try {
                                if (pstmt != null) {
                                    pstmt.close();
                                }

                                if (f != null) {
                                    f = null;
                                }

                                if (newFile != null) {
                                    newFile = null;
                                }

                                if (fs != null) {
                                    fs.close();
                                    fs = null;
                                }
                            } catch (SQLException se) {
                                throw new GreensheetBaseException(
                                        "error.savefile.sql", se);
                            }
                        }
                    }

                    countFiles++;
                }
                // Check the fileAttachmentsPresentInDb flag. If this is true,
                // it means there is at least one file attachment in the
                // database for the qrd.
                // If this flag is false, there are no file attachments in the
                // db for this QRD. Hence, delete the parent record.
                if (!fileAttachmentsPresentInDb) {
                    conn.setAutoCommit(false);
                    String sqlDelete = "delete from form_question_answers_t where id =?";
                    pstmt = conn.prepareStatement(sqlDelete);
                    pstmt.setInt(1, qrd.getId());

                    pstmt.execute();
                    pstmt.close();
                    conn.commit();
                }
            }
        } catch (SQLException se) {
            logger.debug("SQL EXCEPTION");
            throw new GreensheetBaseException("error.savefile.sql", se);
        } catch (Exception e) {
            logger.debug("EXCEPTION ");
            throw new GreensheetBaseException("error in file attachment", e);
        }
    }

    private void makeNewQuestionEntry(QuestionResponseData qrd,
            GreensheetFormProxy form, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String fqaInsert = "insert into form_question_answers_t "
                    + "(id, frm_id, extrnl_question_id, extrnl_resp_def_id)"
                    + " values(?,?,?,?)";

            logger.debug("Inserting into FQA " + qrd.getId());

            pstmt = conn.prepareStatement(fqaInsert);
            pstmt.setInt(1, qrd.getId());
            pstmt.setInt(2, form.getFormId());
            pstmt.setString(3, qrd.getQuestionDefId());
            pstmt.setString(4, qrd.getResponseDefId());

            pstmt.execute();
            pstmt.close();
        } catch (SQLException se) {
            throw se;
        } finally {
            try {

                if (pstmt != null)
                    pstmt.close();

            } catch (SQLException se) {
                throw se;
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
        } catch (FileNotFoundException e) {
            throw new GreensheetBaseException(e);
        } catch (IOException e) {
            throw new GreensheetBaseException(e);
        }
        qa.setDocData(bytes);
    }
    
}
