/**
 * Copyright (c) 2003, Number Six Software, Inc.
 * Licensed under The Apache Software License, http://www.apache.org/licenses/LICENSE
 * Written for National Cancer Institute
 */

package gov.nih.nci.iscs.numsix.greensheets.services.greensheetformmgr;

import gov.nih.nci.iscs.numsix.greensheets.application.QuestionAttachmentsAction;
import gov.nih.nci.iscs.numsix.greensheets.fwrk.*;
import gov.nih.nci.iscs.numsix.greensheets.services.grantmgr.*;
import gov.nih.nci.iscs.numsix.greensheets.utils.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import org.apache.log4j.*;
/**
 * Helper class for working with question file attachments
 * 
 * 
 *  @author kpuscas, Number Six Software
 */
class AttachmentHelper {

    private static final Logger logger = Logger.getLogger(AttachmentHelper.class);
    /**
     * Constructor for AttachmentHelper.
     */
    AttachmentHelper() {
    }
    
    /**
     * Method saveAttachments.
     * @param qrd
     * @param grant
     * @param conn
     * @throws SQLException
     */
    // Updated by KKanchinadam.
    
    void saveAttachments(QuestionResponseData qrd, GsGrant grant, GreensheetForm form, Connection conn) throws GreensheetBaseException {
        logger.debug("In Method -- AttachmentHelper:saveAttachments:");
        
        // Get the file path
        Properties p = (Properties) AppConfigProperties.getInstance().getProperty(GreensheetsKeys.KEY_CONFIG_PROPERTIES);
        String repositoryPath = p.getProperty("attachemts.repository.path");
        String rootDir = repositoryPath + File.separator + grant.getFullGrantNumber() + File.separator + form.getGroupTypeAsString() + File.separator + qrd.getQuestionDefId();

        PreparedStatement pstmt = null;
        boolean makeNewQuestionEntry = false;
        
        File f = null;
        
        try {
            // Create the folder structure in the file system.
            File dir = new File(rootDir);
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            
            //If  this is a new question, make an entry in the Database in the Form Question Answers table.
            if(qrd.getId() == 0) {
                int newId = DbUtils.getNewRowId(conn, "fqa_seq.nextval");
                qrd.setId(newId);
                this.makeNewQuestionEntry(qrd,form,conn);
                logger.debug("Entering a new question with fqa_seq = " + newId);
            }
            
            // Now that the parent record is taken care of... take care of the file attachments.
            // Also, folder structure is present. create or delete (not a good idea in this situation....!!! )  the file attachments.
            
            boolean fileAttachmentsPresentInDb = false; //flag to track whether the question response data will eventually contain any file or not.
            if (qrd.getQuestionAttachments() != null) {
                Set entries = qrd.getQuestionAttachments().entrySet();
                logger.debug("Number of file entries = " + entries.size());
                
                /*
                 	Problem: If you have two files with the same name with different status:
                 			 Filename : x.doc with Status = NEW
                 			 Filename : x.doc with Status = DELETED
                 	Since the order of elements in a HashMap is not guaranteed,  a situation could arise where,
                 	1. x.doc with Status = NEW is created. Thus, the existing x.doc on the file system is replaced by the new x.doc.
                 	2. x.doc with status = DELETED is  processed. This will delete the new x.doc created in the above step.
                 	Next time, when attempting to view the file, a FileNotFoundException will be thrown, since the file no longer exists on the drive.
                 	
                 	Solution: Create an arraylist, with all DELETED items, followed by NEW Items, followed by EXISTING items.     	
                 	you need EXISTING items since need to determine if any file attachments exist for the Question or not.		                                
                */
                
                ArrayList qaList = new ArrayList();
                Iterator iter = entries.iterator();
                while(iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    if (qa.isToBeDeleted()) {
                        qaList.add(qa);
                    }
                }
                
                iter = entries.iterator();
                while(iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    if (qa.isToBeCreated()) {
                        qaList.add(qa);
                    }
                }
                
                iter = entries.iterator();
                while(iter.hasNext()) {
                    Map.Entry me = (Map.Entry) iter.next();
                    QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    if (qa.isExisting()) {
                        qaList.add(qa);
                    }
                }
                
                logger.debug("Map Size =" + entries.size() + " , ArrayList size = " + qaList.size());
                
                iter = qaList.iterator();
                
                // Iterate thru the entries. Delete if status is deleted, else add it.
                int countFiles = 1;
                while (iter.hasNext()) {
                    logger.debug("File #" + countFiles );
                    //Map.Entry me = (Map.Entry) iter.next();
                    //QuestionAttachment qa = (QuestionAttachment) me.getValue();
                    QuestionAttachment qa = (QuestionAttachment) iter.next();
                    
                    // get the status
                    int qaStatus = qa.getStatus();
                    logger.debug(countFiles + ".) -- Filename, Status  = " + qa.getFilename() + ", " + qa.getStatusValue());
                    
                    // If status = NEW or EXISTING, then eventually at least one file attachment will exist. So no need to delete the Form Question Answer from the database.
                    if (qa.isExisting() || qa.isToBeCreated()) {
                        fileAttachmentsPresentInDb = true;
                        logger.debug("fileAttachmentsPresentInDb = TRUE."); 
                    }
                    
                    /*
                    *	Status = NEW, create the attachment in the database.
                    *	Status = DELETED, delete the attachment
                    *	Status = EXISTING, no modification required.
                    */
                    
                    // Get the file
                    f = new File(rootDir + File.separator + qa.getFilename());
                    
                    String sqlAction = null;
                    String sqlDebug = null;
                    
                    if (qa.isToBeDeleted()) {
                        // First, delete the record, then delete the file.
                        try {
                            conn.setAutoCommit(false);
                            sqlAction = "delete from form_answer_attachments_t where id=?";
                            logger.debug("Deleting the attachment ,sql is :");
                            logger.debug(sqlAction);
                            pstmt = conn.prepareStatement(sqlAction);
                            pstmt.setInt(1,qa.getDbId());
                            
                            sqlDebug = "delete from form_answer_attachments_t where id=" + qa.getDbId();
                            
                            pstmt.execute();
                            pstmt.close();  
                            conn.commit();
                            
                            // Now delete the file
                            f.delete();
                        }
                         catch (SQLException se) {
                            fileAttachmentsPresentInDb = true; // File not deleted, hence parent question will have attachments for sure..
                            conn.rollback();
                            
                            logger.debug("Sql Exception when Deleting a file attachment record in the DB.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException("error.deletefile.sql", se);
                        }
                        finally {
                            try {
                                if (pstmt != null) {
                                    pstmt.close();
                                }
                            }
                            catch (SQLException se) {
                                throw new GreensheetBaseException("error.savefile.sql", se);
                            }
                        }             
                     }
                    else if (qa.isToBeCreated()) {
                        // Create the file first. Then create the record.
                        File newFile = null;
                        try {
//                          Create the file on the file system.
                            FileOutputStream fs = new FileOutputStream(f);
                            fs.write(qa.getDocData());
                            fs.close();
                            qa.setFilePath(rootDir + File.separator + qa.getFilename());
                            
                            newFile = new File(rootDir + File.separator + qa.getFilename()); // If db error, then delete the file maybe.
                         
                            conn.setAutoCommit(false);
                            sqlAction = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?)";
                            pstmt = conn.prepareStatement(sqlAction);
                            pstmt.setInt(1,qrd.getId());
                            pstmt.setString(2,qa.getFilename());
                            pstmt.setString(3,qa.getFilePath());
                            
                            sqlDebug = "insert into form_answer_attachments_t (id,fqa_id,name,file_location) values(faa_seq.nextval,?,?,?) ---- ";
                            sqlDebug += "Values are SeqValue, " + qrd.getId() + ", " + qa.getFilename() + ", " + qa.getFilePath();
                            
                            logger.debug("Creating a new attachment, sql is");
                            logger.debug(sqlAction);
                            
                            pstmt.execute();
                            pstmt.close();  
                            conn.commit();
                        }
                        catch (SQLException se) {
                            conn.rollback();
                            
                            logger.debug("Sql Exception when creating a new file attachment record in the DB.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException("error.savefile.sql", se);
                        }
                        catch (FileNotFoundException fe) {
                            conn.rollback();
                            
                            logger.debug("Sql Exception when creating a new file on the file system.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException("error.savefile.fe", fe);                            
                        }
                        catch (IOException ie) {
                            conn.rollback();
                            
                            logger.debug("Error in Creating a new file attachment. IO Exception occurred.");
                            logger.debug("Sql is = " + sqlDebug);
                            throw new GreensheetBaseException("error.savefile.io", ie);
                        }
                        finally {
                            try {
                                if (pstmt != null) {
                                    pstmt.close();
                                }
                            }
                            catch (SQLException se) {
                                throw new GreensheetBaseException("error.savefile.sql", se);
                            }
                        }                        	
                    }
                    
                    countFiles++;
                 }
                // Check the fileAttachmentsPresentInDb flag. If this is true, it means there is at least one file attachment in the database for the qrd.
                // If this flag is false, there are no file attachments in the db for this QRD. Hence, delete the parent record.
                if (!fileAttachmentsPresentInDb) {
                    conn.setAutoCommit(false);
                    String sqlDelete = "delete from form_question_answers_t where id =?";
                    pstmt = conn.prepareStatement(sqlDelete);
                    pstmt.setInt(1,qrd.getId());
                
                    pstmt.execute();
                    pstmt.close();   
                    conn.commit();
                }
            }
        }
        catch (SQLException se) {
            logger.debug("SQL EXCEPTION");
            throw new GreensheetBaseException("error.savefile.sql", se);
        }        
        catch(Exception e) {
            logger.debug("EXCEPTION ");
            throw new GreensheetBaseException("error in file attachment", e);
        }
    }
    

    private void makeNewQuestionEntry(QuestionResponseData qrd,GreensheetForm form,Connection conn) throws SQLException{
        PreparedStatement pstmt = null;
        try{
	        String fqaInsert =  "insert into form_question_answers_t "  
				+ "(id, frm_id, extrnl_question_id, extrnl_resp_def_id)"
				+ " values(?,?,?,?)";                    
	        
	        logger.debug("Inserting into FQA " + qrd.getId());
	        
	        pstmt = conn.prepareStatement(fqaInsert);
	        pstmt.setInt(1,qrd.getId());
	        pstmt.setInt(2,form.getFormId());
	        pstmt.setString(3,qrd.getQuestionDefId());
	        pstmt.setString(4,qrd.getResponseDefId());
	        
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
    
    void getQuestionAttachmentData(QuestionAttachment qa) throws GreensheetBaseException {

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
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
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
